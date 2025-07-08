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
package javax.microedition.broadcast.recording;

import java.util.Date;

import javax.microedition.broadcast.esg.ProgramEvent;
import javax.microedition.broadcast.esg.Service;
import javax.microedition.media.Controllable;
import javax.microedition.media.Control;


public class Recording implements Controllable 
{

    public static final int STATE_FULLY_FAILED = 6;
    public static final int STATE_FULLY_RECORDED = 3;
    public static final int STATE_PARTLY_FAILED = 5;
    public static final int STATE_PARTLY_RECORDED = 4;
    public static final int STATE_RECORDING = 2;
    public static final int STATE_SCHEDULED = 1;
    public static final int STATE_UNDETERMINED = 0;

    private ProgramEvent program;
    private Service service;
    private Date startTime;
    private Date endTime;
    private int startOffset;
    private int endOffset;
    private int state;

    public Recording(ProgramEvent program, int startOffset, int endOffset) 
    {
        if (program == null) throw new NullPointerException("Program must not be null.");

        this.program = program;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.state = STATE_SCHEDULED;
    }

    public Recording(Service service, Date startTime, Date endTime, String programName) 
    {
        if (service == null || startTime == null || endTime == null || programName == null) { throw new NullPointerException("Parameters must not be null."); }
        if (startTime.compareTo(endTime) >= 0) { throw new IllegalArgumentException("Start time must be before end time."); }
        this.service = service;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = STATE_SCHEDULED;
    }

    public Control getControl(String controlType) { return null; }

    public Control[] getControls() { return null; }

    public int getEndOffset() { return endOffset; }

    public Date getEndTime() 
    {
        return new Date(endTime.getTime() + endOffset * 1000);
    }

    public ProgramEvent getProgram() 
    {
        if (state == STATE_FULLY_RECORDED || state == STATE_PARTLY_RECORDED ||
            state == STATE_PARTLY_FAILED || state == STATE_FULLY_FAILED || 
            state == STATE_UNDETERMINED) 
        {
            throw new IllegalStateException("Program event not available in this state.");
        }
        return program;
    }

    public String getProgramName() { return null; }

    public String getServiceName() { return null; }

    public int getStartOffset() { return startOffset; }

    public Date getStartTime() 
    {
        return new Date(startTime.getTime() - startOffset * 1000);
    }

    public int getState() { return state; }

    public String getURL() 
    {
        if (state != STATE_FULLY_RECORDED && state != STATE_PARTLY_RECORDED &&
            state != STATE_PARTLY_FAILED && state != STATE_RECORDING) 
        {
            throw new IllegalStateException("Recording is not in a valid state.");
        }
        return "recording_url";
    }

    public void stop() { }
}