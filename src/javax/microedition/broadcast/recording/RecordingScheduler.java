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

import javax.microedition.broadcast.BroadcastServiceException;
import javax.microedition.broadcast.esg.ProgramEvent;

public class RecordingScheduler 
{

    public static final String LANGUAGE = "Language";

    public static void add(Recording recording) throws BroadcastServiceException { }

    public static void addListener(RecordingSchedulerListener listener) { }

    public static Recording findRecording(ProgramEvent program) { return null; }

    public static String[] getAllPreferenceKeys() { return new String[]{LANGUAGE}; }

    public static Object getPreference(String key) { return null; }

    public static String getRecordDirectory() { return "record_directory"; }

    public static Recording[] listRecordings() { return null; }

    public static void remove(Recording recording) { }

    public static void removeListener(RecordingSchedulerListener listener) { }

    public static Object setPreference(String key, Object value) { return null; }

    public static void setRecordDirectory(String locator) { }
}