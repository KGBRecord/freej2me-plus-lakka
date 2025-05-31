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
package com.nttdocomo.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.URI;

import java.util.Map;

import javax.microedition.rms.RecordStore;

import java.util.Arrays;
import java.util.HashMap;

import com.nttdocomo.util.ScratchPadOutputStream;

import org.recompile.mobile.Mobile;

public class ScratchPadConnection implements javax.microedition.io.Connection 
{

	private String name;
	private int mode;
	private boolean timeouts;
	private int spIndex;

    private int pos = 0; // Read Offset
    private int length = 0; // Length to read

    private static RecordStore[] openedScratchPads = new RecordStore[16]; // With DoJa, there are only up to 16 scratchpads

	static 
	{
		for(int i = 0; i < openedScratchPads.length; i++) { openedScratchPads[i] = null; }
	}

    private byte[] scratchPadData;

	public ScratchPadConnection(String name) 
	{
		
		this.name = name;
		this.spIndex = Integer.parseInt(name.replace("scratchpad:///", "").split(";")[0]);
		try 
		{
			if(openedScratchPads[spIndex] == null) 
			{ 
				openedScratchPads[spIndex] = RecordStore.openRecordStore("ScratchPad-"+spIndex, true);
				openedScratchPads[spIndex].setScratchPadIndex(spIndex);
			}
			Mobile.log(Mobile.LOG_DEBUG, ScratchPadConnection.class.getPackage().getName() + "." + ScratchPadConnection.class.getSimpleName() + ": " + "New ScratchPad Connection: "+ this.name);
		}
		catch(Exception e) { Mobile.log(Mobile.LOG_DEBUG, ScratchPadConnection.class.getPackage().getName() + "." + ScratchPadConnection.class.getSimpleName() + ": " + "Failed to create ScratchPad Connection: "+ this.name + ". " + e.getMessage()); }
	}

	public ScratchPadConnection(String name, int mode) 
	{ 
		this.name = name; 
		this.spIndex = Integer.parseInt(name.replace("scratchpad:///", "").split(";")[0]);
		this.mode = mode;
		try 
		{
			if(openedScratchPads[spIndex] == null) 
			{ 
				openedScratchPads[spIndex] = RecordStore.openRecordStore("ScratchPad-"+spIndex, true);
				openedScratchPads[spIndex].setScratchPadIndex(spIndex);
			}
			Mobile.log(Mobile.LOG_DEBUG, ScratchPadConnection.class.getPackage().getName() + "." + ScratchPadConnection.class.getSimpleName() + ": " + "New ScratchPad Connection: "+ this.name + ". mode " + this.mode);
		}
		catch(Exception e) { Mobile.log(Mobile.LOG_DEBUG, ScratchPadConnection.class.getPackage().getName() + "." + ScratchPadConnection.class.getSimpleName() + ": " + "Failed to create ScratchPad Connection: "+ this.name + ". " + e.getMessage()); }
	}

	public ScratchPadConnection(String name, int mode, boolean timeouts) 
	{ 
		this.name = name; 
		this.spIndex = Integer.parseInt(name.replace("scratchpad:///", "").split(";")[0]);
		this.mode = mode; 
		this.timeouts = timeouts;
		try 
		{
			if(openedScratchPads[spIndex] == null) 
			{ 
				openedScratchPads[spIndex] = RecordStore.openRecordStore("ScratchPad-"+spIndex, true);
				openedScratchPads[spIndex].setScratchPadIndex(spIndex);
			}
			Mobile.log(Mobile.LOG_DEBUG, ScratchPadConnection.class.getPackage().getName() + "." + ScratchPadConnection.class.getSimpleName() + ": " + "New ScratchPad Connection: "+ this.name + ". mode " + this.mode + ". timeout:" + (this.timeouts ? "true" : "false"));
		}
		catch(Exception e) { Mobile.log(Mobile.LOG_DEBUG, ScratchPadConnection.class.getPackage().getName() + "." + ScratchPadConnection.class.getSimpleName() + ": " + "Failed to create ScratchPad Connection: "+ this.name + ". " + e.getMessage()); }
	}

	public void close() 
	{
		this.name = null; // TODO: Clear scratchpad data, maybe write any pending data prior to it?
	}

	public DataInputStream openDataInputStream() throws IOException, EOFException { return new DataInputStream(openInputStream()); }

	public InputStream openInputStream() throws EOFException
	{
		String[] parsedName = name.split(";");

		if(parsedName.length < 2 || parsedName[1].split(",").length < 1) {pos = 64; }
		else { pos = (Integer.parseInt(parsedName[1].split(",")[0].replace("pos=", "")) + 64); }

		if(openedScratchPads[spIndex].getNumRecords() == 0) // If there's no scratchpad data copy in the rms file 
		{
			try 
			{ 
				byte[] spData = loadScratchPadBinary(spIndex);
				openedScratchPads[spIndex].addRecord(spData, 0, spData.length); 
			} // create it
			catch(Exception e) { Mobile.log(Mobile.LOG_DEBUG, ScratchPadConnection.class.getPackage().getName() + "." + ScratchPadConnection.class.getSimpleName() + ": " + " Failed to add scratchpad data to record: " + e.getMessage()); }
		}

		try 
		{
			scratchPadData = openedScratchPads[spIndex].getRecord(spIndex+1);
			
			if(parsedName.length < 2 || parsedName[1].split(",").length < 2) { length = scratchPadData.length-pos-1; }
			else { length = Integer.parseInt(parsedName[1].split(",")[1].replace("length=", "")); }
			

			if(pos >= scratchPadData.length) { throw new EOFException("Cannot read out of bounds"); }

			if(pos + length > scratchPadData.length) { length = scratchPadData.length-pos; }

			byte[] returnData = new byte[length];
			for (int i = 0; i < length; i++) 
			{
				int index = pos + i;
				returnData[i] = scratchPadData[index];
			}
		
			return new ByteArrayInputStream(returnData);
		}
		catch(Exception e) { Mobile.log(Mobile.LOG_ERROR, ScratchPadConnection.class.getPackage().getName() + "." + ScratchPadConnection.class.getSimpleName() + ": " + " Failed to open ScratchPad Input stream: " + e.getMessage()); }
		return null;
	}

    public DataOutputStream openDataOutputStream() { return new DataOutputStream(openOutputStream()); }

	public OutputStream openOutputStream() 
	{
		Mobile.log(Mobile.LOG_WARNING, ScratchPadConnection.class.getPackage().getName() + "." + ScratchPadConnection.class.getSimpleName() + ": " + " Scratchpad Opened for writing (doesn't actually write yet)");
        String[] parsedName = name.split(";");

        if(parsedName.length < 2 || parsedName[1].split(",").length < 1) {pos = 64; }
		else { pos = (Integer.parseInt(parsedName[1].split(",")[0].replace("pos=", "")) + 64); }

		if(openedScratchPads[spIndex].getNumRecords() == 0) // If there's no scratchpad data copy in the rms file 
		{
			try 
			{ 
				byte[] spData = loadScratchPadBinary(spIndex);
				openedScratchPads[spIndex].addRecord(spData, 0, spData.length); 
			} // create it
			catch(Exception e) { Mobile.log(Mobile.LOG_DEBUG, ScratchPadConnection.class.getPackage().getName() + "." + ScratchPadConnection.class.getSimpleName() + ": " + " Failed to add scratchpad data to record: " + e.getMessage()); }
		}

		try 
		{
			scratchPadData = openedScratchPads[spIndex].getRecord(spIndex+1);

			if(parsedName.length < 2 || parsedName[1].split(",").length < 2) { length = scratchPadData.length-pos-1; }
			else { length = Integer.parseInt(parsedName[1].split(",")[1].replace("length=", "")); }

			return new ScratchPadOutputStream(scratchPadData, pos, length, spIndex);
		}
		catch(Exception e) { Mobile.log(Mobile.LOG_ERROR, ScratchPadConnection.class.getPackage().getName() + "." + ScratchPadConnection.class.getSimpleName() + ": " + " Failed to open ScratchPad Output stream: " + e.getMessage()); }
		return null;
	}

	public byte[] loadScratchPadBinary(int scratchPadIndex) 
	{
		String scratchPadPath = Mobile.getPlatform().loader.baseUrl.toString().replace(".jar", (".sp" + scratchPadIndex));

		try
		{
			File spFile = new File(new URI(scratchPadPath));

			// With single scratchpad Indices, it could either be .sp0 or .sp (iDKDoJa converted)
			if(!spFile.exists() && scratchPadIndex == 0) 
			{
				spFile = new File(new URI(scratchPadPath.replace(".sp0", ".sp")));
			}
			InputStream stream = new FileInputStream(spFile);
						
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int count=0;
			byte[] data = new byte[4096];
			while (count!=-1)
			{
				count = stream.read(data);
				if(count!=-1) { buffer.write(data, 0, count); }
			}
			return buffer.toByteArray();
		}
		catch (Exception e) { return new byte[0]; }
	}

	public static void writeScratchPad(int index, byte[] data) 
	{ 
		try { openedScratchPads[index].setRecord(index+1, data, 0, data.length);  }
		catch(Exception e) { Mobile.log(Mobile.LOG_ERROR, ScratchPadOutputStream.class.getPackage().getName() + "." + ScratchPadOutputStream.class.getSimpleName() + ": " + " Failed to write and close scratchpad output:" + e.getMessage()); }
	}

}
