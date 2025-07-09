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

public interface ZoomControl extends javax.microedition.media.Control 
{
    static final int NEXT = -1001;
    static final int PREVIOUS = -1002;
    static final int UNKNOWN = -1004;

    int getDigitalZoom();
    
    int getDigitalZoomLevels();
    
    int getMaxDigitalZoom();
    
    int getMaxOpticalZoom();
    
    int getMinFocalLength();
    
    int getOpticalZoom();
    
    int getOpticalZoomLevels();
    
    int setDigitalZoom(int level) throws java.lang.IllegalArgumentException;
    
    int setOpticalZoom(int level) throws java.lang.IllegalArgumentException;
}