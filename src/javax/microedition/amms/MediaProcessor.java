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
package javax.microedition.amms;

public interface MediaProcessor extends javax.microedition.media.Controllable 
{
    static final int REALIZED = 200;
    static final int STARTED = 400;
    static final int STOPPED = 300;
    static final int UNKNOWN = -1;
    static final int UNREALIZED = 100;

    void abort();
    
    void addMediaProcessorListener(MediaProcessorListener mediaProcessorListener);
    
    void complete() throws javax.microedition.media.MediaException;
    
    int getProgress();
    
    int getState();
    
    void removeMediaProcessorListener(MediaProcessorListener mediaProcessorListener);
    
    void setInput(java.io.InputStream input, int length) throws javax.microedition.media.MediaException;
    
    void setInput(java.lang.Object image) throws javax.microedition.media.MediaException;
    
    void setOutput(java.io.OutputStream output);
    
    void start() throws javax.microedition.media.MediaException;
    
    void stop() throws javax.microedition.media.MediaException;
}