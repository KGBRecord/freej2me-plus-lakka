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
import java.io.IOException;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

import org.recompile.mobile.Mobile;

public class WAVTools 
{

    private static final byte PCMHEADERSIZE = 44;

    public static int hostSampleRate = 0;

	static // Get the host device's sample rate when this class is initially created, getDefaultAudioSampleRate() is rather expensive to call
	{
		hostSampleRate = getDefaultAudioSampleRate();
	}

    /*
	 * Since the header is always expected to be positioned right at the start
	 * of a byte array, read it to determine the WAV type.
	 * 
	 * Optionally it also returns some information about the audio format to help build a 
	 * new header for the decoded stream.
	*/
	public static final int[] readHeader(InputStream input) throws IOException 
	{
		/*
			The header of a WAV (RIFF) file is 44 bytes long and has the following format:

			CHAR[4] "RIFF" header
			UINT32  Size of the file (chunkSize).
			  CHAR[4] "WAVE" format
				CHAR[4] "fmt " header
				UINT32  SubChunkSize (examples: 12 for PCM unsigned 8-bit )
				  UINT16 AudioFormat (ex: 1 [PCM], 17 [IMA ADPCM] )
				  UINT16 NumChannels
				  UINT32 SampleRate
				  UINT32 BytesPerSec (samplerate*frame size)
				  UINT16 frameSize or blockAlign (256 on some gameloft games)
				  UINT16 BitsPerSample (gameloft games appear to use 4)
				CHAR[4] "data" header
				UINT32 Length of sample data.
				<Sample data>

			-- IMA ADPCM introduces the following before "data" header, and after BitsPerSample:
			UINT16 ByteExtraData
			UINT16 ExtraData
			CHAR[4] "fact" header
			UINT32 SubChunk2Size
			UINT32 NumOfSamples
		*/

		String riff = readInputStreamASCII(input, 4); // 0 - 4
		int dataSize = readInputStreamInt32(input);  // 4 - 8
		String format = readInputStreamASCII(input, 4);  // 8 - 12
		String fmt = readInputStreamASCII(input, 4);  // 12 - 16
		int chunkSize = readInputStreamInt32(input);  // 16 - 20
		short audioFormat = (short) readInputStreamInt16(input);  // 20 - 22
		short audioChannels = (short) readInputStreamInt16(input);  // 22 - 24
		int sampleRate = readInputStreamInt32(input);  // 24 - 28
		int bytesPerSec = readInputStreamInt32(input); // 28 - 32
		short frameSize = (short) readInputStreamInt16(input); // 32 - 34
		short bitsPerSample = (short) readInputStreamInt16(input); // 34 - 36
		
		// These are conditionally read depending on IMA ADPCM
		short ByteExtraData = 0;
		short ExtraData = 0;
		String factHeader = "";
		int SubChunk2Size = 0;
		int numOfSamples = 0;

		if(audioFormat == 0x11) 
		{
			ByteExtraData = (short) readInputStreamInt16(input); // 36 - 38 -- On IMA ADPCM
			ExtraData = (short) readInputStreamInt16(input); // 38 - 40 -- On IMA ADPCM
			factHeader = readInputStreamASCII(input, 4);  // 40 - 44 -- On IMA ADPCM
			SubChunk2Size = readInputStreamInt32(input);  // 44 - 48 -- On IMA ADPCM
			numOfSamples = readInputStreamInt32(input);  // 48 - 52 -- On IMA ADPCM
		}

		String dataHeader = readInputStreamASCII(input, 4);  // 36 - 40 -- On PCM WAV, 52-56 On IMA ADPCM
		int dataLen = readInputStreamInt32(input); // 40 - 44 -- On PCM WAV, 56-60 On IMA ADPCM

		Mobile.log(Mobile.LOG_DEBUG, WAVTools.class.getPackage().getName() + "." + WAVTools.class.getSimpleName() + ": " + (audioFormat == 0x11 ? "IMA ADPCM" : "PCM") + " WAV HEADER_START");

		Mobile.log(Mobile.LOG_DEBUG, WAVTools.class.getPackage().getName() + "." + WAVTools.class.getSimpleName() + ": " + riff);
		Mobile.log(Mobile.LOG_DEBUG, WAVTools.class.getPackage().getName() + "." + WAVTools.class.getSimpleName() + ": " + "FileSize:" + dataSize);
		Mobile.log(Mobile.LOG_DEBUG, WAVTools.class.getPackage().getName() + "." + WAVTools.class.getSimpleName() + ": " + "Format: " + format);

		Mobile.log(Mobile.LOG_DEBUG, WAVTools.class.getPackage().getName() + "." + WAVTools.class.getSimpleName() + ": " + "---'" + fmt + "' header---");
		Mobile.log(Mobile.LOG_DEBUG, WAVTools.class.getPackage().getName() + "." + WAVTools.class.getSimpleName() + ": " + "Header ChunkSize:" + Integer.toString(chunkSize));
		Mobile.log(Mobile.LOG_DEBUG, WAVTools.class.getPackage().getName() + "." + WAVTools.class.getSimpleName() + ": " + "AudioFormat: " + Integer.toString(audioFormat));
		Mobile.log(Mobile.LOG_DEBUG, WAVTools.class.getPackage().getName() + "." + WAVTools.class.getSimpleName() + ": " + "AudioChannels:" + Integer.toString(audioChannels));
		Mobile.log(Mobile.LOG_DEBUG, WAVTools.class.getPackage().getName() + "." + WAVTools.class.getSimpleName() + ": " + "SampleRate:" + Integer.toString(sampleRate));
		Mobile.log(Mobile.LOG_DEBUG, WAVTools.class.getPackage().getName() + "." + WAVTools.class.getSimpleName() + ": " + "BytesPerSec:" + Integer.toString(bytesPerSec));
		Mobile.log(Mobile.LOG_DEBUG, WAVTools.class.getPackage().getName() + "." + WAVTools.class.getSimpleName() + ": " + "FrameSize:" + Integer.toString(frameSize));
		Mobile.log(Mobile.LOG_DEBUG, WAVTools.class.getPackage().getName() + "." + WAVTools.class.getSimpleName() + ": " + "BitsPerSample:" + Integer.toString(bitsPerSample));
		
		if(audioFormat == 0x11) 
		{
			Mobile.log(Mobile.LOG_DEBUG, WAVTools.class.getPackage().getName() + "." + WAVTools.class.getSimpleName() + ": " + "ByteExtraData:" + Integer.toString(ByteExtraData));
			Mobile.log(Mobile.LOG_DEBUG, WAVTools.class.getPackage().getName() + "." + WAVTools.class.getSimpleName() + ": " + "ExtraData:" + Integer.toString(ExtraData));
			Mobile.log(Mobile.LOG_DEBUG, WAVTools.class.getPackage().getName() + "." + WAVTools.class.getSimpleName() + ": " + "---'" + factHeader +"' header---");
			Mobile.log(Mobile.LOG_DEBUG, WAVTools.class.getPackage().getName() + "." + WAVTools.class.getSimpleName() + ": " + "SubChunk2Size:" + Integer.toString(SubChunk2Size));
			Mobile.log(Mobile.LOG_DEBUG, WAVTools.class.getPackage().getName() + "." + WAVTools.class.getSimpleName() + ": " + "numOfSamples:" + Integer.toString(numOfSamples));
		}
		
		Mobile.log(Mobile.LOG_DEBUG, WAVTools.class.getPackage().getName() + "." + WAVTools.class.getSimpleName() + ": " + "---'" + dataHeader +"' header---");
		Mobile.log(Mobile.LOG_DEBUG, WAVTools.class.getPackage().getName() + "." + WAVTools.class.getSimpleName() + ": " + "SampleDataLength:" + Integer.toString(dataLen));

		Mobile.log(Mobile.LOG_DEBUG, WAVTools.class.getPackage().getName() + "." + WAVTools.class.getSimpleName() + ": " + "WAV HEADER_END");
		
		/* 
		 * We need the audio format to check if it's ADPCM or PCM, and the file's 
		 * dataSize, SampleRate and audioChannels to decode ADPCM and build a new header. 
		 */
		return new int[] {audioFormat, sampleRate, audioChannels, frameSize, bitsPerSample};
	}

	/* Read a 16-bit little-endian unsigned integer from input.*/
	private static final int readInputStreamInt16(InputStream input) throws IOException 
	{ return ( input.read() & 0xFF ) | ( ( input.read() & 0xFF ) << 8 ); }

	/* Read a 32-bit little-endian signed integer from input.*/
	private static final int readInputStreamInt32(InputStream input) throws IOException 
	{
		return ( input.read() & 0xFF ) | ( ( input.read() & 0xFF ) << 8 )
			| ( ( input.read() & 0xFF ) << 16 ) | ( ( input.read() & 0xFF ) << 24 );
	}

	/* Return a String containing 'n' Characters of ASCII/ISO-8859-1 text from input. */
	private static final String readInputStreamASCII(InputStream input, int nChars) throws IOException 
	{
		byte[] chars = new byte[nChars];
		readInputStreamData(input, chars, 0, nChars);
		return new String(chars, "ISO-8859-1");
	}

	/* Read 'n' Bytes from the InputStream starting from the specified offset into the output array. */
	public static final void readInputStreamData(InputStream input, byte[] output, int offset, int nBytes) throws IOException 
	{
		int end = offset + nBytes;
		while(offset < end) 
		{
			int read = input.read(output, offset, end - offset);
			if(read < 0) throw new java.io.EOFException();
			offset += read;
		}
	}

	/* 
	 * Builds a WAV header that describes the decoded ADPCM file on the first 44 bytes. 
	 * Data: little-endian, 16-bit, signed, same sample rate and channels as source IMA ADPCM.
	 */
	public static final void buildHeader(byte[] buffer, final short numChannels, final int sampleRate, final short numBits) 
	{ 
		final short bitsPerSample = numBits;   /* 16-bit or 8-bit PCM */
		final short audioFormat = 1;           /* WAV linear PCM */
		final int subChunkSize = 16;           /* Fixed size for Wav Linear PCM */
		final int chunk = 0x52494646;          /* 'RIFF' */ 
		final int format = 0x57415645;         /* 'WAVE' */ 
		final int subChunk1 = 0x666d7420;      /* 'fmt ' */ 
		final int subChunk2 = 0x64617461;      /* 'data' */ 

		/* 
		 * We'll have 16 bits per sample, so each sample has 2 bytes, with that we just divide
		 * the size of the byte buffer (minus the header) by (bitsPerSample/8) times the amount of channels.
		*/
		final int samplesPerChannel = (buffer.length - PCMHEADERSIZE) / ((bitsPerSample / 8) * numChannels);

		/* 
		 * Frame size is fairly standard, and PCM's fixed sample size makes it so the frameSize is either 2 bytes 
		 * for mono, or 4 bytes for stereo.
		 */
		final short frameSize = (short) (numChannels * (bitsPerSample / 8));

		/* 
		 * Previously only took into account mono streams. And since we know the framesize and
		 * the amount of samples per channel, in a format that has a fixed amount of bits per sample,
		 * we can account for multiple audio channels on sampleDataLength with a simpler calculus:
		 */
		final int sampleDataLength = (samplesPerChannel * numChannels) * frameSize;

		/* 
		 * Represents how many bytes are streamed per second. With all of the data above, it's trivial to
		 * calculate by getting the sample rate, the amount of channels and bytes per sample (bitsPerSample / 8)
		 */
		final int bytesPerSec = sampleRate * numChannels * (bitsPerSample / 8);
		
		/* NOTE: ChunkSize includes the header, so sampleDataLength + 44, which is the byte size of our header */
		writeIntBE(buffer, 0, chunk);                 // ChunkID
		writeIntLE(buffer, 4, sampleDataLength + 36); // ChunkSize
		writeIntBE(buffer, 8, format);                // Format
		writeIntBE(buffer, 12, subChunk1);            // SubchunkID (fmt)
		writeIntLE(buffer, 16, subChunkSize);         // SubchunkSize
		writeShort(buffer, 20, audioFormat);          // Audioformat
		writeShort(buffer, 22, numChannels);          // NumChannels
		writeIntLE(buffer, 24, sampleRate);           // SampleRate
		writeIntLE(buffer, 28, bytesPerSec);          // ByteRate
		writeShort(buffer, 32, frameSize);            // BlockAlign
		writeShort(buffer, 34, bitsPerSample);        // BitsPerSample
		writeIntBE(buffer, 36, subChunk2);            // Subchunk2ID (data)
		writeIntLE(buffer, 40, sampleDataLength);     // Subchunk2 Size
	}

	private static void writeIntLE(byte[] buffer, int index, int value) 
	{
		buffer[index] = (byte) (value & 0xFF);
		buffer[index + 1] = (byte) ((value >> 8) & 0xFF);
		buffer[index + 2] = (byte) ((value >> 16) & 0xFF);
		buffer[index + 3] = (byte) ((value >> 24) & 0xFF);
	}

	// A few of the header fields are big endian
	private static void writeIntBE(byte[] buffer, int index, int value) 
	{
		buffer[index] = (byte) ((value >> 24) & 0xFF);
		buffer[index + 1] = (byte) ((value >> 16) & 0xFF);
		buffer[index + 2] = (byte) ((value >> 8) & 0xFF);
		buffer[index + 3] = (byte) (value & 0xFF);
	}

	private static void writeShort(byte[] buffer, int index, short value) 
	{
		buffer[index] = (byte) (value & 0xFF);
		buffer[index + 1] = (byte) ((value >> 8) & 0xFF);
	}

    /* ------------------------------- NON-ADPCM SECTION -------------------------------*/


	// These will convert from different PCM formats to either 8 or 16-bit Signed PCM:
	public static final byte[] convert4BitWav(byte[] input, int numChannels, int sampleRate, boolean is2Complement) 
	{
		byte[] convertedWav = new byte[2*input.length];
		
		for (int i = 0; i < input.length; i++) 
		{
			// Get the upper 4 bits (MSB) and lower 4 bits (LSB), since we have 2 samples per byte on the original 4-bit wav
			int upperNibble = (input[i] >> 4) & 0x0F;
			int lowerNibble = input[i] & 0x0F;

			if(is2Complement)
			{
				upperNibble ^= 0x08;
				lowerNibble ^= 0x08;
			}

			convertedWav[i * 2] = (byte) (upperNibble < 8 ? upperNibble * 17 : (upperNibble - 8) * 17);
			convertedWav[i * 2 + 1] = (byte) (lowerNibble < 8 ? lowerNibble * 17 : (lowerNibble - 8) * 17);
		}

		return upsample(convertedWav, sampleRate, hostSampleRate, (short) numChannels, (short) 8);
	}

	// This one pretty much just converts from binary offset to 2's complement if needed, and builds a header
	public static final byte[] convert8BitWav(byte[] input, int numChannels, int sampleRate, boolean is2Complement) 
	{
		byte[] convertedWav = new byte[input.length];

		buildHeader(convertedWav, (short) numChannels, sampleRate, (short) 8);

		for (int i = 0; i < input.length; i++) 
		{
			if (is2Complement) { convertedWav[i] = (byte) (input[i] ^ 0x80); } 
			else { convertedWav[i] = (byte) (input[i]); }
		}

		return upsample(convertedWav, sampleRate, hostSampleRate, (short) numChannels, (short) 8);
	}

	// TODO: Does this kind of WAV even exist?
	public static final byte[] convert12BitWav(byte[] input, int numChannels, int sampleRate, boolean is2Complement) 
	{
		byte[] convertedWav = new byte[(int)(1.5 * input.length) + 1]; // Add an extra byte for safety

		buildHeader(convertedWav, (short) numChannels, sampleRate, (short) 16);

		for (int i = 0; i < input.length / 3; i++) 
		{
			int sampleIndex = i * 3;

			int sample = ((input[sampleIndex] & 0xFF) << 4) | ((input[sampleIndex + 1] & 0xFF) >> 4);

			if (is2Complement) 
			{
				convertedWav[i * 2] = (byte) (sample & 0xFF);
				convertedWav[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
			} 
			else 
			{
				sample -= 2048;
				convertedWav[i * 2] = (byte) (sample & 0xFF);
				convertedWav[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
			}
		}
		return upsample(convertedWav, sampleRate, hostSampleRate, (short) numChannels, (short) 16);
	}

	public static final byte[] convert16BitWav(byte[] input, int numChannels, int sampleRate, boolean is2Complement) 
	{
		byte[] convertedWav = new byte[input.length];

		buildHeader(convertedWav, (short) numChannels, sampleRate, (short) 16);

		for (int i = 0; i < input.length / 2; i++) 
		{
			int sampleIndex = i * 2;
			short sample = (short) ((input[sampleIndex] & 0xFF) | (input[sampleIndex + 1] << 8));

			if (is2Complement) { sample ^= 0x8000; }

			convertedWav[sampleIndex] = (byte) (sample & 0xFF);
			convertedWav[sampleIndex + 1] = (byte) ((sample >> 8) & 0xFF);
		}

		return upsample(convertedWav, sampleRate, hostSampleRate, (short) numChannels, (short) 16);
	}

	public static byte[] upsample(byte[] input, int originalSampleRate, int newSampleRate, short numChannels, short numBits) 
	{
		int inputLength = input.length, paddedSamples = 0;
		int newLength = (int) ((inputLength * (double) newSampleRate) / originalSampleRate);
		byte[] upsampled = new byte[PCMHEADERSIZE + newLength]; // Allocate for header + upsampled audio data

		buildHeader(upsampled, numChannels, newSampleRate, numBits);
		
		double ratio = (double) originalSampleRate / newSampleRate;

		// Upsample the audio data based on how many bits per sample it has
		for (int i = 0; i < newLength; i++) 
		{
			int originalIndex = (int) (i * ratio);
			double cosineFraction = (1 - Math.cos(((i * ratio) - originalIndex) * Math.PI)) / 2;

			if (numBits == 8) 
			{
				int sample1 = (input[originalIndex] & 0xFF);
				int sample2 = (originalIndex + 1 < inputLength) ? (input[originalIndex + 1] & 0xFF) : sample1;

				// Apply cosine interpolation instead of linear interpolation (results similar to cubic interp. at very little extra cost compared to linear)
				upsampled[PCMHEADERSIZE + i] = (byte) (sample1 + (sample2 - sample1) * cosineFraction);
			} 
			else if (numBits == 16)  // For 16-bit PCM WAV, each sample takes 2 bytes
			{
				if (originalIndex * 2 + 2 >= inputLength) { break; }

				int sample1 = ((input[originalIndex * 2] & 0xFF) | (input[originalIndex * 2 + 1] << 8));
				int sample2 = ((originalIndex + 1) * 2 < inputLength ? 
					(input[(originalIndex + 1) * 2] & 0xFF) | (input[(originalIndex + 1) * 2 + 1] << 8) : sample1);

				int interpolatedValue = (int) (sample1 + (sample2 - sample1) * cosineFraction);
				upsampled[PCMHEADERSIZE + i * 2] = (byte) (interpolatedValue & 0xFF); // Low byte
				upsampled[PCMHEADERSIZE + i * 2 + 1] = (byte) ((interpolatedValue >> 8) & 0xFF); // High byte
			}
		}

		return upsampled;
	}

	public static int getDefaultAudioSampleRate() 
	{
		Mixer.Info[] mixers = AudioSystem.getMixerInfo();
		for (Mixer.Info mixerInfo : mixers) 
		{
			Mixer mixer = AudioSystem.getMixer(mixerInfo);
			Line.Info[] lineInfos = mixer.getSourceLineInfo();
			for (Line.Info lineInfo : lineInfos) 
			{
				if (lineInfo instanceof Line.Info) 
				{
					Line line = null;
					try 
					{
						line = mixer.getLine(lineInfo);
						if (line instanceof SourceDataLine) 
						{
							return (int) ((SourceDataLine) line).getFormat().getSampleRate();
						}
					} catch (Exception e) { e.printStackTrace(); }
				}
			}
		}
		return 32000; // Default to 32KHz
	}
}