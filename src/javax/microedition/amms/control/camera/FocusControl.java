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

public interface FocusControl extends javax.microedition.media.Control 
{
    static final int AUTO = -1000;
    static final int AUTO_LOCK = -1005;
    static final int NEXT = -1001;
    static final int PREVIOUS = -1002;
    static final int UNKNOWN = -1004;

    int getFocus();
    
    int getFocusSteps();
    
    boolean getMacro();
    
    int getMinFocus();
    
    boolean isAutoFocusSupported();
    
    boolean isMacroSupported();
    
    boolean isManualFocusSupported();
    
    int setFocus(int distance) throws javax.microedition.media.MediaException;
    
    void setMacro(boolean enable) throws javax.microedition.media.MediaException;
}