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
package javax.microedition.amms.control.camera;

public interface SnapshotControl extends javax.microedition.media.Control 
{

    static final int FREEZE = -2;
    static final int FREEZE_AND_CONFIRM = -1;
    static final String SHOOTING_STOPPED = "SHOOTING_STOPPED";
    static final String STORAGE_ERROR = "STORAGE_ERROR";
    static final String WAITING_UNFREEZE = "WAITING_UNFREEZE";

    String getDirectory();
    
    String getFilePrefix();
    
    String getFileSuffix();
    
    void setDirectory(String directory) throws java.lang.IllegalArgumentException, java.lang.SecurityException;
    
    void setFilePrefix(String prefix) throws java.lang.IllegalArgumentException;
    
    void setFileSuffix(String suffix) throws java.lang.IllegalArgumentException;
    
    void start(int maxShots) throws java.lang.SecurityException;
    
    void stop();
    
    void unfreeze(boolean save);
}