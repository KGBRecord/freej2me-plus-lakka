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

public interface FlashControl extends javax.microedition.media.Control 
{
    static final int AUTO = 2;
    static final int AUTO_WITH_REDEYEREDUCE = 3;
    static final int FILLIN = 6;
    static final int FORCE = 4;
    static final int FORCE_WITH_REDEYEREDUCE = 5;
    static final int OFF = 1;

    int getMode();
    
    int[] getSupportedModes();
    
    boolean isFlashReady();
    
    void setMode(int mode) throws java.lang.IllegalArgumentException;
}