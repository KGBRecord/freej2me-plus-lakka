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

public interface ExposureControl extends javax.microedition.media.Control 
{
    
    int getExposureCompensation();
    
    int getExposureTime();
    
    int getExposureValue();
    
    int getFStop();
    
    int getISO();
    
    String getLightMetering();
    
    int getMaxExposureTime();
    
    int getMinExposureTime();
    
    int[] getSupportedExposureCompensations();
    
    int[] getSupportedFStops();
    
    int[] getSupportedISOs();
    
    String[] getSupportedLightMeterings();
    
    void setExposureCompensation(int ec) throws javax.microedition.media.MediaException;
    
    int setExposureTime(int time) throws javax.microedition.media.MediaException;
    
    void setFStop(int aperture) throws javax.microedition.media.MediaException;
    
    void setISO(int iso) throws javax.microedition.media.MediaException;
    
    void setLightMetering(String metering) throws java.lang.IllegalArgumentException;
}