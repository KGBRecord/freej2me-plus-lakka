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
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.Map;
import java.util.HashMap;

import com.nttdocomo.util.ScratchPadOutputStream;

import org.recompile.mobile.Mobile;

public class ScratchPadConnection implements javax.microedition.io.Connection 
{

	private static String name;
	private static int mode;
	private static boolean timeouts;

    private static int pos = 0; // Read Offset
    private static int length = 0; // Length to read

    private static Map<String, Boolean> openedScratchPads = new HashMap<String, Boolean>();

    private static byte[] scratchPadData;

	public ScratchPadConnection(String name) 
	{
		this.name = name;
        if(!openedScratchPads.containsKey(name)) { openedScratchPads.put(name.split(";")[0], false); }
		Mobile.log(Mobile.LOG_DEBUG, ScratchPadConnection.class.getPackage().getName() + "." + ScratchPadConnection.class.getSimpleName() + ": " + "New ScratchPad Connection: "+ this.name);
	}

	public ScratchPadConnection(String name, int mode) 
	{ 
		this.name = name; 
		this.mode = mode;
        if(!openedScratchPads.containsKey(name)) { openedScratchPads.put(name.split(";")[0], false); }
		Mobile.log(Mobile.LOG_DEBUG, ScratchPadConnection.class.getPackage().getName() + "." + ScratchPadConnection.class.getSimpleName() + ": " + "New ScratchPad Connection: "+ this.name + ". mode " + this.mode);
	}

	public ScratchPadConnection(String name, int mode, boolean timeouts) 
	{ 
		this.name = name; 
		this.mode = mode; 
		this.timeouts = timeouts;
        if(!openedScratchPads.containsKey(name)) { openedScratchPads.put(name.split(";")[0], false); }
		Mobile.log(Mobile.LOG_DEBUG, ScratchPadConnection.class.getPackage().getName() + "." + ScratchPadConnection.class.getSimpleName() + ": " + "New ScratchPad Connection: "+ this.name + ". mode " + this.mode + ". timeout:" + (this.timeouts ? "true" : "false"));
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

		if(openedScratchPads.get(parsedName[0]) == false) 
		{
			try { scratchPadData = Mobile.getIAppliScratchPadAsByteArray(parsedName[0]); } 
			catch (Exception e) { Mobile.log(Mobile.LOG_WARNING, ScratchPadConnection.class.getPackage().getName() + "." + ScratchPadConnection.class.getSimpleName() + ": " + " Failed to open ScratchPad:" + e.getMessage());}

			openedScratchPads.put(parsedName[0], true);
		}

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
		
		if(Mobile.minLogLevel == Mobile.LOG_DEBUG) 
		{
			StringBuilder hexString = new StringBuilder();
			for (byte b : returnData) 
			{
				hexString.append(String.format("%02X ", b));
			}
			Mobile.log(Mobile.LOG_DEBUG, ScratchPadConnection.class.getPackage().getName() + "." + ScratchPadConnection.class.getSimpleName() + ": " + " Scratchpad Hex Data read: " + hexString.toString());
		}

		return new ByteArrayInputStream(returnData);
	}

    public DataOutputStream openDataOutputStream() { return new DataOutputStream(openOutputStream()); }

	public OutputStream openOutputStream() 
	{
		Mobile.log(Mobile.LOG_WARNING, ScratchPadConnection.class.getPackage().getName() + "." + ScratchPadConnection.class.getSimpleName() + ": " + " Scratchpad Opened for writing (doesn't actually write yet)");
        String[] parsedName = name.split(";");

        if(parsedName.length < 2 || parsedName[1].split(",").length < 1) {pos = 64; }
		else { pos = (Integer.parseInt(parsedName[1].split(",")[0].replace("pos=", "")) + 64); }

        if(openedScratchPads.get(parsedName[0]) == false) 
        {
            try { scratchPadData = Mobile.getIAppliScratchPadAsByteArray(parsedName[0]); } 
            catch (Exception e) { Mobile.log(Mobile.LOG_WARNING, ScratchPadConnection.class.getPackage().getName() + "." + ScratchPadConnection.class.getSimpleName() + ": " + " Failed to open ScratchPad:" + e.getMessage());}

            openedScratchPads.put(parsedName[0], true);
        }

		if(parsedName.length < 2 || parsedName[1].split(",").length < 2) { length = scratchPadData.length-pos-1; }
		else { length = Integer.parseInt(parsedName[1].split(",")[1].replace("length=", "")); }

		return new ScratchPadOutputStream(scratchPadData, pos, length);
	}

}
