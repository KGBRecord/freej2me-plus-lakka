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

public interface CameraControl extends javax.microedition.media.Control 
{
    static final int ROTATE_LEFT = 2;
    static final int ROTATE_NONE = 1;
    static final int ROTATE_RIGHT = 3;
    static final int UNKNOWN = -1004;

    void enableShutterFeedback(boolean enable) throws javax.microedition.media.MediaException;
    
    int getCameraRotation();
    
    String getExposureMode();
    
    int getStillResolution();
    
    String[] getSupportedExposureModes();
    
    int[] getSupportedStillResolutions();
    
    int[] getSupportedVideoResolutions();
    
    int getVideoResolution();
    
    boolean isShutterFeedbackEnabled();
    
    void setExposureMode(String mode) throws java.lang.IllegalArgumentException;
    
    void setStillResolution(int index) throws java.lang.IllegalArgumentException;
    
    void setVideoResolution(int index) throws java.lang.IllegalArgumentException, java.lang.IllegalStateException;
}