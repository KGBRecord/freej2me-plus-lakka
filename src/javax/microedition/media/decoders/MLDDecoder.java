/*
	This file is part of FreeJ2ME.

	FreeJ2ME is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	FreeJ2ME is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with FreeJ2ME.  If not, see http://www.gnu.org/licenses/
*/
package javax.microedition.media.decoders;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.nio.ByteOrder;
import java.util.Arrays;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import org.recompile.mobile.Mobile;

// Decoder for NTT DoCoMo's MLD/MFi format, closely related to CMF: https://web.archive.org/web/20220912152735/https://datatracker.ietf.org/doc/html/draft-atarius-cmf-00
public final class MLDDecoder
{

    private static final byte MLD_EXTA_MSG  = (byte) 0x3F;
    private static final byte MLD_EXTB_MSG  = (byte) 0x7F;
    private static final byte MLD_EXTC_MSG  = (byte) 0xBF;
    private static final byte MLD_SYSEX_MSG = (byte) 0xFF;

    private static final byte MIDI_CHANNELS = 16;

    // These are used only for debugging
    private static final String[] formatTypes = {"RESERVED", "0x1: Melody", "0x2: Song"};
    private static final String[] melodyTypes = {"RESERVED", "0x1: Complete Melody", "0x2: Part of Melody"};
    private static final String[] extInfoTypes = 
    {
        "RESERVED",     // %b11110000
        "WAV_DATA",     // %b11110001
        "TEXT_DATA",    // %b11110010
        "PICT_DATA",    // %b11110011
        "ANIM_DATA",    // %b11110100
        "RESERVED",     // %b11110101
        "RESERVED",     // %b11110110
        "RESERVED",     // %b11110111
        "RESERVED",     // %b11111000
        "RESERVED",     // %b11111001
        "RESERVED",     // %b11111010
        "RESERVED",     // %b11111011
        "RESERVED",     // %b11111100
        "RESERVED",     // %b11111101
        "RESERVED",     // %b11111110
        "RESERVED",     // %b11111111
    };

    private static final String[] sourceFromTypes = 
    {
        "Network",
        "Terminal",
        "External",
        "RESERVED"
    };

    private static final int[] timebases = new int[] 
    {
        6,   // %b0000
        12,  // %b0001
        24,  // %b0010
        48,  // %b0011
        96,  // %b0100
        192, // %b0101
        384, // %b0110
        0,   // %b0111 - RESERVED
        15,  // %b1000
        30,  // %b1001
        60,  // %b1010
        120, // %b1011
        240, // %b1100
        480, // %b1101
        960, // %b1110
        0    // %b1111 - RESERVED
    };

    // Create a new sequence and track for the converted MLD file
    private static byte numTracks;
    private static Sequence sequence;
    private static Track track;
    private static boolean noteHas3Bytes;
    private static int curTrack; // This increases by 1 for every "trac" chunk in the same MLD

    private static MLDChannelData[] channelData;

    private static byte[] input;

    private static int decodePos = 0;
    private static int exst = -1;

    private static int[] cuePoints;

    private static int globalTimebase;
    private static int globalTempo;

    // PCM-Specific variables
    public static boolean isPCM = false; // Used by PlatformPlayer to decide whether it'll load the decoded data into a WAVPlayer or MIDIPlayer

    // Structures that hold decoded MLD data
    public static List<InputStream> pcmData = null;
    public static InputStream SequenceData = null;
    public static Map<Integer, Integer> pcmDataPositions = new HashMap<Integer, Integer>();
    public static Map<Integer, Integer> pcmDataVelocities = new HashMap<Integer, Integer>();

    public static synchronized void decodeMLD(byte[] data)
	{
        // Reset any and all static variables to their defaults
        isPCM = false;
        decodePos = 0;
        exst = -1;
        numTracks = 0;
        sequence = null; // Clear the previous MIDI sequence
        noteHas3Bytes = true;
        curTrack = 0;
        globalTimebase = timebases[3];
        globalTempo = 125;

        // Clear previous decoded data objects
        if(pcmData != null) { pcmData.clear(); }
        pcmData = null;
        SequenceData = null;
        pcmData = new ArrayList<InputStream>();
        pcmDataPositions.clear();
        pcmDataVelocities.clear();

        input = data;

        boolean parsingData = true;

        // Start parsing the file.
        decodeHeader(); // melo (file header)

        while(parsingData && decodePos < data.length)
        {
            String chunkID = "" + (char) input[decodePos] + (char) input[decodePos+1] + (char) input[decodePos+2] + (char) input[decodePos+3];

            if (chunkID.equals("adat"))      { decodeADATChunk(); }
            else if (chunkID.equals("adpm")) { decodeADPMChunk(); }
            else if (chunkID.equals("ainf")) { decodeAINFChunk(); }
            else if (chunkID.equals("auth")) { decodeAUTHChunk(); } 
            else if (chunkID.equals("copy")) { decodeCOPYChunk(); } 
            else if (chunkID.equals("cuep")) { decodeCUEPChunk(); } // TODO: Untested
            else if (chunkID.equals("date")) { decodeDATEChunk(); } 
            else if (chunkID.equals("exst")) { decodeEXSTChunk(); } // TODO: Untested
            else if (chunkID.equals("note")) { decodeNOTEChunk(); } 
            else if (chunkID.equals("prot")) { decodePROTChunk(); } 
            else if (chunkID.equals("sorc")) { decodeSORCChunk(); } 
            else if (chunkID.equals("supt")) { decodeSUPTChunk(); } 
            else if (chunkID.equals("thrd")) { decodeTHRDChunk(); } // TODO: Properly parse this, right now no 3D positioning is accounted for
            else if (chunkID.equals("titl")) { decodeTITLChunk(); } 
            else if (chunkID.equals("vers")) { decodeVERSChunk(); } 
            else if (chunkID.equals("trac")) { decodeTRACChunk(); } 
            else                             { parsingData = false; } // Assume we reached EOF
        }

        try
        {
            // Convert the resulting sequence to byte array and send to the player.
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            MidiSystem.write(sequence, 0, output);
            SequenceData = new ByteArrayInputStream(output.toByteArray());

            Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + " MFi parsing and conversion finished, Sequence data size:" + output.size() + " | number of PCM streams:" + pcmData.size());
        }
        catch (Exception e) 
        { 
            Mobile.log(Mobile.LOG_ERROR, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + " couldn't write converted MFi Data:" + e.getMessage()); 
            e.printStackTrace(); 
            SequenceData = null;
            pcmData = null;
        }
	}

    public static void decodeHeader() 
    {
        String fileChunkID = "" + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++]; // "MMMD"
        int fileChunkSize = (input[decodePos++] & 0xFF) << 24 | (input[decodePos++] & 0xFF) << 16 | (input[decodePos++] & 0xFF) << 8 | (input[decodePos++] & 0xFF) - 8;
        
        int headerLength = (input[decodePos++] & 0xFF) << 8 | (input[decodePos++] & 0xFF);
        
        int songType = input[decodePos++] & 0xFF;
        int instruments = input[decodePos++] & 0xFF;
        numTracks = (byte) (input[decodePos++] & 0xFF);

        Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"-------------------------- MLD CONTENT HEADER --------------------------");
        Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"fileChunkID: " + fileChunkID);
        Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"fileChunkSize: " + fileChunkSize);
        Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"headerSize: " + headerLength);
        Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"songType: " + formatTypes[songType]);
        if (songType == 0x0200) 
        {
            Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"contentClass has music events: " + ((instruments & 0x01) != 0));
            Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"contentClass has pcm data: " + ((instruments & 0x02) != 0));
            Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"contentClass has text data: " + ((instruments & 0x04) != 0));
            Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"contentClass has image data: " + ((instruments & 0x08) != 0));
            Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"contentClass has fem vocals: " + ((instruments & 0x10) != 0));
            Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"contentClass has male vocals: " + ((instruments & 0x20) != 0));
            Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"contentClass has other vocals: " + ((instruments & 0x40) != 0));
        } 
        else 
        {
            Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"melody type: " + melodyTypes[instruments]);
        }
        
        Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"numTracks: " + (4*numTracks) + (numTracks == 1 ? " (MFi)" : "(MFi 2)"));

        // We now have sufficient data to create a proper MIDI sequence.
        if(sequence == null) 
        { 
            try 
            {
                cuePoints = new int[numTracks];
                channelData = new MLDChannelData[4*numTracks];
                for(int i = 0; i < 4*numTracks; i++) { channelData[i] = new MLDChannelData(); }
                sequence = new Sequence(Sequence.PPQ, 48); // TODO: Default timebase for CMF/MFi is 48, see if keeping it as such works well on different timebase-tempo settings
                track = sequence.createTrack();
            } 
            catch(InvalidMidiDataException ie) { Mobile.log(Mobile.LOG_ERROR, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + " couldn't create MIDI Sequence to convert:" + ie.getMessage()); }
        }

        // OK, we're at the start of the "sorc" chunk, which means the content info chunk (content header) has been left behind
    }

    public static void decodeSORCChunk()
    {
        // We're at the Score Track Chunk, so let's decode the info about the audio data
        String sorc = "" + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++]; // "MTR" actually uses 4 chars, the last one appears to always be 0x01
        int chunkSize = (input[decodePos++] & 0xFF) << 8 | (input[decodePos++] & 0xFF); // length is 16 bit for most subchunks in MLD it seems
        byte sourceType = (byte) (input[decodePos++] & 0xFF);

        Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"Source info > From: " + sourceFromTypes[sourceType & 0xF7] + " | Has copyright: " + ((sourceType & 0x01) == 1 ? "Yes" : "No"));
    }

    public static void decodeTITLChunk() 
    {
        String chunkName = "" + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++];
        int chunkSize = (input[decodePos++] & 0xFF) << 8 | (input[decodePos++] & 0xFF); 
        byte[] byteData = new byte[chunkSize];
        
        for(int i = 0; i < chunkSize; i++) { byteData[i] = (byte) (input[decodePos++]); }

        try 
        {
            String MLDTrackData = new String(byteData, "Shift_JIS");

            Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"Title: " + MLDTrackData);
        }
        catch(UnsupportedEncodingException e) { }
    }

    public static void decodeVERSChunk() 
    {
        String chunkName = "" + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++];
        int chunkSize = (input[decodePos++] & 0xFF) << 8 | (input[decodePos++] & 0xFF); 
        byte[] byteData = new byte[chunkSize];
        
        for(int i = 0; i < chunkSize; i++) { byteData[i] = (byte) (input[decodePos++]); }

        String MLDTrackData = new String(byteData);

        Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"Version: " + MLDTrackData);
    }

    public static void decodeDATEChunk() 
    {
        String chunkName = "" + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++];
        int chunkSize = (input[decodePos++] & 0xFF) << 8 | (input[decodePos++] & 0xFF); 
        byte[] byteData = new byte[chunkSize];
        
        for(int i = 0; i < chunkSize; i++) { byteData[i] = (byte) (input[decodePos++]); }

        String MLDTrackData = new String(byteData);

        Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"Date: " + MLDTrackData);
    }

    public static void decodeEXSTChunk() 
    {
        String chunkName = "" + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++];
        int chunkSize = (input[decodePos++] & 0xFF) << 8 | (input[decodePos++] & 0xFF); 
        byte[] byteData = new byte[chunkSize];
        
        for(int i = 0; i < chunkSize; i++) { byteData[i] = (byte) (input[decodePos++]); }

        exst = byteData[0] + 0xFF + byteData[1];

        Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"Exst: " + exst);
    }


    public static void decodeCOPYChunk() 
    {
        String chunkName = "" + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++];
        int chunkSize = (input[decodePos++] & 0xFF) << 8 | (input[decodePos++] & 0xFF); 
        byte[] byteData = new byte[chunkSize];
        
        for(int i = 0; i < chunkSize; i++) { byteData[i] = (byte) (input[decodePos++]); }

        String MLDTrackData = new String(byteData);

        Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"Copyright: " + MLDTrackData);
    }

    public static void decodeSUPTChunk() 
    {
        String chunkName = "" + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++];
        int chunkSize = (input[decodePos++] & 0xFF) << 8 | (input[decodePos++] & 0xFF); 
        byte[] byteData = new byte[chunkSize];
        
        for(int i = 0; i < chunkSize; i++) { byteData[i] = (byte) (input[decodePos++]); }

        String MLDTrackData = "";
        try { MLDTrackData = new String(byteData, "Shift_JIS"); }
        catch (Exception e) { }

        Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"suptval: " + MLDTrackData);
    }

    public static void decodePROTChunk() 
    {
        String chunkName = "" + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++];
        int chunkSize = (input[decodePos++] & 0xFF) << 8 | (input[decodePos++] & 0xFF); 
        byte[] byteData = new byte[chunkSize];
        
        for(int i = 0; i < chunkSize; i++) { byteData[i] = (byte) (input[decodePos++]); }

        String contentProvider = new String(byteData);

        Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"Content Provider: " + contentProvider);
    }

    public static void decodeNOTEChunk() 
    {
        String chunkName = "" + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++];
        int chunkSize = (input[decodePos++] & 0xFF) << 8 | (input[decodePos++] & 0xFF); 
        int alignbyte = input[decodePos++] & 0xFF; // TODO: Find out if this is ever different from 0x00 (and if the NOTE chunk ever has a chunkSize larger than 2 bytes)
        noteHas3Bytes = (input[decodePos++] & 0xFF) == 0;
        
        Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"Notes are 3 Bytes: " + noteHas3Bytes);
    }

    public static void decodeAUTHChunk() 
    {
        String chunkName = "" + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++];
        int chunkSize = (input[decodePos++] & 0xFF) << 8 | (input[decodePos++] & 0xFF); 
        byte[] byteData = new byte[chunkSize];
        
        for(int i = 0; i < chunkSize; i++) { byteData[i] = (byte) (input[decodePos++]); }

        String author = new String(byteData);

        Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"Author: " + author);
    }

    public static void decodeTHRDChunk() 
    {
        String chunkName = "" + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++];
        int chunkSize = (input[decodePos++] & 0xFF) << 8 | (input[decodePos++] & 0xFF); 
        byte[] byteData = new byte[chunkSize];
        
        for(int i = 0; i < chunkSize; i++) { byteData[i] = (byte) (input[decodePos++]); }

        Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"-------------------------- 3D POS INFO CHUNK --------------------------");
        Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"3D Positioning data: " + Arrays.toString(byteData));
    }

    public static void decodeCUEPChunk() 
    {
        String chunkName = "" + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++];
        int chunkSize = (input[decodePos++] & 0xFF) << 8 | (input[decodePos++] & 0xFF); 
        byte[] byteData = new byte[chunkSize];
        
        for (int i = 0; i < cuePoints.length; i++) { cuePoints[i] = (input[decodePos++] & 0xFF) << 24 | (input[decodePos++] & 0xFF) << 16 | (input[decodePos++] & 0xFF) << 8 | (input[decodePos++] & 0xFF); }

        Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"-------------------------- 3D POS INFO CHUNK --------------------------");
        Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"3D Positioning data: " + Arrays.toString(byteData));
    }

    public static void decodeAINFChunk() 
    {
        String chunkName = "" + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++];
        int chunkSize = (input[decodePos++] & 0xFF) << 8 | (input[decodePos++] & 0xFF); 
        int numStreams = (input[decodePos++] & 0xFF);
        int hasPCMData = (input[decodePos++] & 0xFF);
        
        Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"-------------------------- AUDIO INFO CHUNK --------------------------");
        Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"Amount of (AD)PCM streams: " + numStreams);
        Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"Has following (AD)PCM streams: " + (hasPCMData == 0 ? "Yes" : "No"));
    }

    public static void decodeADATChunk() 
    {
        String chunkName = "" + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++];
        int chunkSize = ((input[decodePos++] & 0xFF) << 24 | (input[decodePos++] & 0xFF) << 16 | (input[decodePos++] & 0xFF) << 8 | (input[decodePos++] & 0xFF));
        int adpmHeaderLen = (input[decodePos++] & 0xFF) << 8 | (input[decodePos++] & 0xFF);
        int dataFormat = (input[decodePos++] & 0xFF);
        int dataAttribute = (input[decodePos++] & 0xFF);
        
        Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"-------------------------- AUDIO DATA CHUNK --------------------------");
        Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"Header Length: " + adpmHeaderLen);
        Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"Data format: " + dataFormat);
        Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"Data attribute: " + dataAttribute);
    }

    public static void decodeADPMChunk() 
    {
        String chunkName = "" + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++];
        int chunkSize = (input[decodePos++] & 0xFF) << 8 | (input[decodePos++] & 0xFF); 
        int sampleRate = (input[decodePos++] & 0xFF);
        int bitDepth = (input[decodePos++] & 0xFF);
        int numChannels = (input[decodePos++] & 0xFF);

        int wavSize = 0;

        while(!((char) input[wavSize+decodePos] == 't' && (char) input[wavSize+decodePos+1] == 'r' && (char) input[wavSize+decodePos+2] == 'a' && (char) input[wavSize+decodePos+3] == 'c')) 
        {
            wavSize++;
        }
        
        byte[] waveData = new byte[wavSize];

        for(int i = 0; i < wavSize; i++) 
        {
            waveData[i] = input[decodePos++];
        }

        Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"-------------------------- ADPCM DATA CHUNK --------------------------");
        Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"Sample Rate: " + (sampleRate * 1000) + "Hz");
        Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"Bit Depth: " + bitDepth + " bits"); // This is either 2 or 4 bits
        Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"Channel type: " + (numChannels == 1 ? "Mono" : "Stereo"));

        if(bitDepth == 4) { /* pcmData.add(new ByteArrayInputStream(WAVYamahaADPCMDecoder.ADPCMADecode(waveData, sampleRate, numChannels))); */  } // TODO: Not working properly yet
        else { Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"2-bit depth decoding not supported!"); }
    }

    public static void decodeTRACChunk() 
    {
        String chunkName = "" + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++] + (char) input[decodePos++];
        int chunkSize = ((input[decodePos++] & 0xFF) << 24 | (input[decodePos++] & 0xFF) << 16 | (input[decodePos++] & 0xFF) << 8 | (input[decodePos++] & 0xFF));
        
        chunkSize = Math.min(chunkSize, input.length-decodePos); // The final track chunk might report an incorrect chunkSize (happens in some Gradius NEO Imperial files)
        
        byte[] byteData = new byte[chunkSize];
        
        for(int i = 0; i < chunkSize; i++) 
        { 
            if(decodePos == input.length) { break; }
            byteData[i] = (byte) (input[decodePos++]); 
        }
        
        Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"-------------------------- NEW TRACK CHUNK --------------------------");
        Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"trackSize: " + chunkSize);
        Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"trackBytes: " + Arrays.toString(byteData));
    
        try { decodeTRACEvents(byteData); curTrack++; }
        catch(Exception e) 
        {
            Mobile.log(Mobile.LOG_ERROR, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"Failed to decode track events: " + e.getMessage());
            e.printStackTrace();
            SequenceData = null;
        }
    }

    /* -------------------------------------------------------------------------------------- */
    /*           Lower level decoding functions (sequence event, duration, etc)               */
    /* -------------------------------------------------------------------------------------- */

    public static void decodeTRACEvents(byte[] data) throws InvalidMidiDataException 
    {        
        int offset = 0;
        int totalDuration = 0;
        byte status = 0;

        while (offset < data.length) 
        {
            int eventChannel = 0;
            int gateTime = 0;
            byte noteNumber = 0;
            byte octaveShift = 0;
            byte velocity = 127; // Assume max velocity for all notes, as 3 byte notes do not have velocity changes

            MidiEvent midiEvent = null;

            totalDuration += Math.round((data[offset++] & 0xFF) * getMillisecondsPerTick()); // Update total duration
            status = (byte) (data[offset++] & 0xFF); // Read status byte
        
            // Check against non-note status messages
            if(status == MLD_EXTA_MSG || status == MLD_EXTB_MSG || status == MLD_EXTC_MSG || status == MLD_SYSEX_MSG) 
            {
                byte eventParam = (byte) (data[offset++] & 0xFF);

                if(eventParam < (byte) 0x80) // ExtA Status Message (in CMF, this appears to be fine-pitch-bend)
                {
                    byte[] exstData = new byte[1 + exst];
                    for(int i = 0; i < 1 + exst; i++) { exstData[i] = data[offset++]; }
                    
                    Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Ext-A event. Data: " + Arrays.toString(exstData));
                }
                else if(eventParam >= (byte) 0x80 && eventParam < (byte) 0xF0) // ExtB Status Message
                {
                    int eventValue = data[offset++] & 0xFF;
                    int eventChannelIndex = eventValue >> 6;
                    eventChannel          = eventChannelIndex * curTrack;

                    int centsAdjustment = 0, pitchBendValue = 0, msb = 0, lsb = 0;
                    
                    ShortMessage event = new ShortMessage();

                    if (eventParam >= (byte) 0xC0 && eventParam <= (byte) 0xCF) // TIMEBASE-TEMPO event
                    {
                        Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "TIMEBASE TEMPO event! timebase:" + timebases[(eventParam & 0x0F)] + " tempo:" + (eventValue != 0 ? eventValue : globalTempo));
                        
                        globalTimebase = timebases[(eventParam & 0x0F)];
                        globalTempo = eventValue != 0 ? eventValue : globalTempo;
                        continue;
                    }
                    
                    // TODO: Improve these
                    switch (eventParam) 
                    {
                        case (byte) 0x80: // AUDIO CHANNEL VOLUME TODO: Is this where PCM samples are set to be played?
                            Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Audio channel volume (PCM) event not implemented! value " + (eventValue & 0x3f) + ", channel " + (curTrack * 4 + eventChannel));
                            pcmDataPositions.put(totalDuration+gateTime, (curTrack * 4 + eventChannel));
                            pcmDataVelocities.put(totalDuration+gateTime, (eventValue & 0x3f) * 2);
                            break;

                        case (byte) 0xB0: // MASTER_VOLUME
                            Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Setting Master Volume to " + (eventValue & 0x7F));
                            for(int i = 0; i < MIDI_CHANNELS; i++) 
                            {
                                event.setMessage(ShortMessage.CONTROL_CHANGE, i, 7, (eventValue & 0x7F));
                                midiEvent = new MidiEvent(event, totalDuration);
                                track.add(midiEvent);
                            }
                            break;

                        case (byte) 0xB1: // MASTER_BALANCE
                            Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Setting Master Balance to " + (eventValue & 0x3F));
                            for(int i = 0; i < MIDI_CHANNELS; i++) 
                            {
                                event.setMessage(ShortMessage.CONTROL_CHANGE, i, 10, (eventValue & 0x3F));
                                midiEvent = new MidiEvent(event, totalDuration);
                                track.add(midiEvent);
                            }
                            break;

                        case (byte) 0xB3: // MASTER_TUNE
                            if (eventValue < (byte) 0x34 || eventValue > (byte) 0x4C) 
                            {
                                Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Invalid master tune value: " + eventValue);
                                continue;
                            }

                            Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Setting Master Tune to " + eventValue);
                            
                            // Calculate the cents adjustment and the resulting master tune (Based on CMF's table)
                            centsAdjustment = (eventValue - 0x40) * 100;

                            pitchBendValue = (centsAdjustment * 8192) / 1200;
                            pitchBendValue += 8192;

                            pitchBendValue = Math.max(0, Math.min(16383, pitchBendValue));

                            // Split the pitch bend value into LSB and MSB
                            lsb = pitchBendValue & 0x7F;
                            msb = (pitchBendValue >> 7) & 0x7F;

                            // Send the pitch bend message to all channels
                            for (int i = 0; i < MIDI_CHANNELS; i++) 
                            {
                                event.setMessage(ShortMessage.PITCH_BEND, i, lsb, msb);
                                midiEvent = new MidiEvent(event, totalDuration);
                                track.add(midiEvent);
                            }
                            break;

                        case (byte) 0xBA: // Drum bank enable
                            Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Adding drum bank enable " + ((eventValue & 1) != 0) + " event " + " to channel " + (curTrack * 4 + eventChannel));
                            boolean enableDrumBank = (eventValue & 1) != 0;
                            event.setMessage(ShortMessage.CONTROL_CHANGE, (eventValue >> 3 & 15), (enableDrumBank ? handyPhoneBankToMidi(channelData[(eventValue >> 3 & 15)].currentInstrument) : channelData[(eventValue >> 3 & 15)].currentInstrument), 0);
                            midiEvent = new MidiEvent(event, totalDuration);
                            track.add(midiEvent);
                            break;

                        case (byte) 0xD0: // CUEPOINT
                            Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Cue point event type (not implemented): " + (eventValue == 0x00 ? "StartPoint" : "EndPoint") + " at time " + totalDuration);
                            // TODO: Apply these to the MIDI data (by dropping any notes outside of the cuepoint range maybe?)
                            break;

                        case (byte) 0xD1: // JUMP
                            int destination = (eventValue >> 6) & 0x03;
                            int jumpId = (eventValue >> 4) & 0x03;
                            int noOfJumps = eventValue & 0x0F;
                            
                            Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +
                                    "Parsed jump event (not implemented): " +
                                    "Destination: " + (destination == 0 ? "Destination Point" : "Jump Point") + ", " +
                                    "Jump ID: " + jumpId + ", " +
                                    "Number of Jumps: " + (noOfJumps == 15 ? "Infinity" : noOfJumps));

                            // TODO: Apply this to the MIDI data somehow
                            break;

                        case (byte) 0xDC: // NOP Type 2 (just do nothing)
                            Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Parsed NOP Type 2 event");
                            break;

                        case (byte) 0xDD: // Loop Point
                            Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Loop point event not implemented!");
                            break;

                        case (byte) 0xDE: // NOP (just do nothing)
                            Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Parsed NOP event");
                            break;

                        case (byte) 0xDF: // END_OF_TRACK
                            Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "END_OF_TRACK Reached!");
                            return;
            
                        case (byte) 0xE0: // Program Change
                            Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Adding program change number " + (eventValue & 0x3F) + " to channel " + (curTrack * 4 + eventChannel));
                            channelData[(curTrack * 4 + eventChannel)].currentInstrument = (byte) (eventValue & 0x3F);
                            event.setMessage(ShortMessage.PROGRAM_CHANGE, (curTrack * 4 + eventChannel), (eventValue & 0x3F), 0);
                            midiEvent = new MidiEvent(event, totalDuration);
                            track.add(midiEvent);
                            break;
                        
                        case (byte) 0xE1: // Bank Change
                            Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Adding bank change to channel " + (curTrack * 4 + eventChannel) + " with value " + (eventValue & 0x3F));
                            
                            if ((eventValue & 0x3F) >= 2 && (eventValue & 0x3F) <= 3) // Bank should map to General MIDI Instrument Patch Map
                            {
                                int bankNumber = (eventValue & 0x3F) - 2; // 0 selects the first 64 patches of the Patch Map, 1 selects the second 64 patches
                                
                                // Bank Select MSB
                                event.setMessage(ShortMessage.CONTROL_CHANGE, (curTrack * 4 + eventChannel), 0, bankNumber);
                                midiEvent = new MidiEvent(event, totalDuration);
                                track.add(midiEvent);

                                // Bank Select LSB
                                event.setMessage(ShortMessage.CONTROL_CHANGE, (curTrack * 4 + eventChannel), 32, 0); // Assuming LSB is 0 for simplicity
                                midiEvent = new MidiEvent(event, totalDuration);
                                track.add(midiEvent);
                            } 
                            else if ((eventValue & 0x3F) == 63) // Drum bank identical to MIDI Percussion Key Map
                            {
                                event.setMessage(ShortMessage.CONTROL_CHANGE, (curTrack * 4 + eventChannel), 0, (eventValue & 0x3F)); // Bank select for drums
                                midiEvent = new MidiEvent(event, totalDuration);
                                track.add(midiEvent);
                            }
                            // Any other value is basically reserved
                            break;

                        case (byte) 0xE2: // Volume change
                            Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Adding volume change " + eventValue +" to channel " + (curTrack * 4 + eventChannel));
                            event.setMessage(ShortMessage.CONTROL_CHANGE, (curTrack * 4 + eventChannel), 7, (eventValue & 0x3F));
                            midiEvent = new MidiEvent(event, totalDuration);
                            track.add(midiEvent);
                            break;

                        case (byte) 0xE3: // Panpot change
                            Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Adding panpot value" + (eventValue & 0x3F) + " to channel " + (curTrack * 4 + eventChannel));
                            event.setMessage(ShortMessage.CONTROL_CHANGE, (curTrack * 4 + eventChannel), 10, (eventValue & 0x3F));
                            midiEvent = new MidiEvent(event, totalDuration);
                            track.add(midiEvent);
                            break;

                        case (byte) 0xE4: // Pitch Bend (Basing off of CMF's Table)
                            int pitchBendIndex = eventValue & 0x3F;

                            if (pitchBendIndex < 32) { centsAdjustment = -(32 - pitchBendIndex) * 100; } 
                            else { centsAdjustment = (pitchBendIndex - 32) * 100; }

                            pitchBendValue = (centsAdjustment * 32) / 100;
                            pitchBendValue += 8192;

                            // Separate the pitch bend value into MSB and LSB, as we need to send both separately
                            lsb = pitchBendValue & 0x7F;
                            msb = (pitchBendValue >> 7) & 0x7F;

                            Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Adding pitch bend MSB " + msb + " LSB " + lsb + " to channel " + (curTrack * 4 + eventChannel));
                            event.setMessage(ShortMessage.PITCH_BEND, (curTrack * 4 + eventChannel), lsb, msb);
                            midiEvent = new MidiEvent(event, totalDuration);
                            track.add(midiEvent);
                            break;

                        case (byte) 0xE5: // CHANNEL_ASSIGN
                            Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Channel Change event not implemented! Voice: " + (eventValue & 0xC0) + " Channel:" + (eventValue & 0x0F));
                            //channelData[(curTrack * 4 + eventChannel)].pitchBendRange = (byte) (eventValue & 0x3F);
                            break;

                        case (byte) 0xE6: // Expression change
                            Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Adding Expression change event value:" + (eventValue & 0x7F) + " to channel" + (curTrack * 4 + eventChannel));
                            event.setMessage(ShortMessage.CONTROL_CHANGE, (curTrack * 4 + eventChannel), 11, (eventValue & 0x7F));
                            midiEvent = new MidiEvent(event, totalDuration);
                            track.add(midiEvent);
                            break;

                        case (byte) 0xE7: // PITCH_BEND_RANGE
                            Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Adding pitch bend range " + (eventValue & 0x3F) + " to channel " + (curTrack * 4 + eventChannel));
                            channelData[(curTrack * 4 + eventChannel)].pitchBendRange = (byte) (eventValue & 0x3F);
                            break;

                        case (byte) 0xE8: // FINE_PITCH_BEND_A / WAVE_CHANNEL_VOLUME
                            Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Fine Pitch Bend A (Wave volume) not implemented! Value: " + (eventValue & 0x7F));
                            //event.setMessage(ShortMessage.CONTROL_CHANGE, (curTrack * 4 + eventChannel), 1, (eventValue & 0x3F) * 2);
                            //midiEvent = new MidiEvent(event, totalDuration);
                            //track.add(midiEvent);
                            break;

                        case (byte) 0xE9: // FINE_PITCH_BEND_B / WAVE_CHANNEL_PANPOT
                            Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Fine Pitch Bend B (Wave Panpot) not implemented! Value: " + (eventValue & 0x3F));
                            //event.setMessage(ShortMessage.CONTROL_CHANGE, (curTrack * 4 + eventChannel), 1, (eventValue & 0x3F) * 2);
                            //midiEvent = new MidiEvent(event, totalDuration);
                            //track.add(midiEvent);
                            break;
                        
                        case (byte) 0xEA: // MODULATION DEPTH
                            Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Adding modulation depth value " + (eventValue & 0x3F) + "(" + ((eventValue & 0x3F) * 2) + ") to channel " + (curTrack * 4 + eventChannel));
                            event.setMessage(ShortMessage.CONTROL_CHANGE, (curTrack * 4 + eventChannel), 1, (eventValue & 0x3F) * 2);
                            midiEvent = new MidiEvent(event, totalDuration);
                            track.add(midiEvent);
                            break;

                        // These are not supported at all yet, and thus, are skipped
                        case (byte) 0xB9: // PART_CONFIGURATION
                        case (byte) 0xBD: // PAUSE
                        case (byte) 0xBF: // RESET
                        case (byte) 0xBE: // STOP
                            Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Skipping event type: " + String.format("0x%02X", eventParam));
                            break;
                        
                        default:
                            // Unknown status
                            Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Unknown status byte: " + String.format("0x%02X", eventParam));
                            break;
                    }
                }
                else if (eventParam > (byte) 0xEF && eventParam <= (byte) 0xFF) // ExtInfo Status Message, SysEx
                {
                    int length = (data[offset++] & 0xFF) << 8 | (data[offset++] & 0xFF);
                    Mobile.log(Mobile.LOG_WARNING, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Ext info event. Type: " + extInfoTypes[eventParam & 0x0F]);
                    byte[] evtData = new byte[2+length];
                    evtData[0] = (byte) ((length / 0x100) & 0xff);
                    evtData[1] = (byte) ((length % 0x100) & 0xff);
                    for(int i = 0; i < length; i++) 
                    {
                        evtData[i+2] = (byte) (data[offset++] & 0xFF);
                    }
                }
            }
            else // If it didn't match above, we're dealing with a note message
            {
                eventChannel = (status >> 6) & 0x03;
                noteNumber = (byte) ((status & 0x3F) + 45); // Note 15 in CMF/MFi is C4, which means that all note values should be increased by 45 (60 is C4 in midi)
        
                gateTime = Math.round((data[offset++] & 0xFF) * getMillisecondsPerTick());

                // TODO: On SMAF documentation, gateTime cannot be zero. This indicates either a corrupted file or a parse error... let's assume the same for CMF/MFi
                if (gateTime <= 0) 
                {
                    Mobile.log(Mobile.LOG_ERROR, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "note gateTime value cannot be zero. ");
                    return;
                }

                if(!noteHas3Bytes) // Notes with 4 bytes have an additional byte for octaveShift and velocity
                {
                    byte additionalByte = (byte) (data[offset++] & 0xFF);
                    //velocity = (byte) ((additionalByte & 0x3F) * 2); // TODO: Does it really work like this though? Doesn't sound right
                    octaveShift = (byte) ((additionalByte >> 6) & 0x03);

                    switch (octaveShift) 
                    {
                        case 0: // No change
                            break; 
                        case 1: // Increase one octave
                            noteNumber += 12;
                            break; 
                        case 2: // Decrease two octaves
                            noteNumber -= 24;
                            break; 
                        case 3: // Decrease one octave
                            noteNumber -= 12;
                            break; 
                    }
                }

                // Make sure the resulting note is still within range. We probably don't need this, but better safe than sorry
                if (noteNumber < 0) { noteNumber = 0; } 
                else if (noteNumber > 127) { noteNumber = 127; }
                
                if(noteNumber < 20)
                {
                    Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Adding PCM Request/Note value " + noteNumber + " to channel " + ((curTrack * 4 + eventChannel)) + " at duration " + totalDuration);
                    pcmDataPositions.put(totalDuration+gateTime, (int) noteNumber);
                }
                else { Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " + "Adding note value " + noteNumber + " to channel " + (curTrack * 4 + eventChannel)); }
                
                // We still add the notes no matter, just so that the sequencer can actually reach the PCM request time
                ShortMessage noteOn = new ShortMessage();
                noteOn.setMessage(ShortMessage.NOTE_ON, (curTrack * 4 + eventChannel), noteNumber, velocity);
                midiEvent = new MidiEvent(noteOn, totalDuration);
                track.add(midiEvent);
                
                channelData[(curTrack * 4 + eventChannel)].lastNote = noteNumber;

                ShortMessage noteOff = new ShortMessage();
                noteOff.setMessage(ShortMessage.NOTE_OFF, (curTrack * 4 + eventChannel), channelData[(curTrack * 4 + eventChannel)].lastNote, 0);
                midiEvent = new MidiEvent(noteOff, totalDuration+gateTime);
                track.add(midiEvent);
                
                continue;
            }
        }
        Mobile.log(Mobile.LOG_DEBUG, MLDDecoder.class.getPackage().getName() + "." + MLDDecoder.class.getSimpleName() + ": " +"Sequence finished. Returning.");
    }

    public static final float getMillisecondsPerTick() 
    { 
        float tickMultiplier = Math.round(60000.0f / (globalTempo * globalTimebase)); // With the default timeBase of 48 and tempo of 125, this results in 10(ms), and the ratio adjusts based on timebase and tempo
        return tickMultiplier / 10f; // Return the multiplier for gateTime and totalDuration, as we can't really change the MIDI sequence's PPQ after creating it
    }

    // TODO: Maybe MLD's functionality is different from SMAF's handy phone format for Drum banks, has to be researched
    private static byte handyPhoneBankToMidi(byte instr)
    {        
        switch(instr) 
        {
            case 24: return 108; // SeqClick H
            case 25: return 106; // Brush Tap
            case 26: return 107; // Brush Swirl L
            case 27: return 109; // Brush Slap
            case 28: return 110; // Brush Swirl H
            case 29: return 60;  // Snare Roll
            case 30: return 65;  // Castanet
            case 31: return 61;  // Snare L
            case 32: return 36;  // SeqClick H (Acoustic Bass Drum)
            case 33: return 34;  // Brush Tap (Fingered Bass)
            case 34: return 35;  // Bass Drum L
            case 35: return 36;  // Bass Drum M
            case 36: return 37;  // Closed Rim Shot
            case 37: return 38;  // Snare M
            case 38: return 39;  // Hand Clap
            case 39: return 41;  // Floor Tom L
            case 40: return 40;  // Snare H
            case 41: return 43;  // Floor Tom H
            case 42: return 42;  // Hi-Hat Closed
            case 43: return 45;  // Floor Tom M
            case 44: return 46;  // Hi-Hat Pedal
            case 45: return 47;  // Low Tom
            case 46: return 48;  // Hi-Hat Open
            case 47: return 49;  // Mid Tom L
            case 48: return 50;  // Mid Tom H
            case 49: return 49;  // Crash Cymbal 1
            case 50: return 50;  // High Tom
            case 51: return 51;  // Ride Cymbal 1
            case 52: return 52;  // Chinese Cymbal
            case 53: return 53;  // Ride Cymbal Cup
            case 54: return 54;  // Tambourine
            case 55: return 57;  // Splash Cymbal
            case 56: return 58;  // Cowbell
            case 57: return 59;  // Crash Cymbal 2
            case 58: return 60;  // Vibraslap
            case 59: return 61;  // Bongo H
            case 60: return 62;  // Bongo L
            case 61: return 63;  // Conga H
            case 62: return 64;  // Conga H Open
            case 63: return 65;  // Conga L
            case 64: return 66;  // Timbale H
            case 65: return 67;  // Timbale L
            case 66: return 68;  // Agogo H
            case 67: return 69;  // Agogo L
            case 68: return 70;  // Cabasa
            case 69: return 71;  // Maracas
            case 70: return 72;  // Tambourine
            case 71: return 73;  // Triangle
            case 72: return 74;  // Shaker
            case 73: return 75;  // Jingle Bell
            case 74: return 76;  // Bottle Wood Block H
            case 75: return 77;  // Wood Block L
            case 76: return 78;  // Cuica Mute
            case 77: return 79;  // Cuica Open
            case 78: return 80;  // Square Lead
            case 79: return 81;  // Triangle Mute
            case 80: return 82;  // Saw Lead
            case 81: return 83;  // Triangle Open
            case 82: return 84;  // Shaker
            case 83: return 85;  // Jingle Bell
            case 84: return 86;  // Belltree
            default: return 0;   // Default case (we should never hit this)
        }
    }
}

class MLDChannelData 
{
    public byte currentInstrument, pitchBendRange, lastNote = 0;
    public boolean hasPendingNoteEvent = false;

    MLDChannelData() 
    {
        currentInstrument = 0;
        pitchBendRange = 0;
    }
}