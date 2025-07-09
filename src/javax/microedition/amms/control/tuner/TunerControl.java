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
package javax.microedition.amms.control.tuner;

public interface TunerControl extends javax.microedition.media.Control 
{
    
    static final int AUTO = 3;
    static final String MODULATION_AM = "am";
    static final String MODULATION_FM = "fm";
    static final int MONO = 1;
    static final int STEREO = 2;

    int getFrequency();
    
    int getMaxFreq(String modulation) throws java.lang.IllegalArgumentException;
    
    int getMinFreq(String modulation) throws java.lang.IllegalArgumentException;
    
    String getModulation();
    
    int getNumberOfPresets();
    
    int getPresetFrequency(int preset) throws java.lang.IllegalArgumentException;
    
    String getPresetModulation(int preset) throws java.lang.IllegalArgumentException;
    
    String getPresetName(int preset) throws java.lang.IllegalArgumentException;
    
    int getPresetStereoMode(int preset) throws javax.microedition.media.MediaException;
    
    int getSignalStrength() throws javax.microedition.media.MediaException;
    
    boolean getSquelch();
    
    int getStereoMode();
    
    int seek(int startFreq, String modulation, boolean upwards) throws javax.microedition.media.MediaException;
    
    int setFrequency(int freq, String modulation) throws java.lang.IllegalArgumentException;
    
    void setPreset(int preset) throws java.lang.IllegalArgumentException;
    
    void setPreset(int preset, int freq, String mod, int stereoMode) throws java.lang.IllegalArgumentException;
    
    void setPresetName(int preset, String name) throws java.lang.IllegalArgumentException;
    
    void setSquelch(boolean squelch) throws javax.microedition.media.MediaException;
    
    void setStereoMode(int mode) throws java.lang.IllegalArgumentException;
    
    void usePreset(int preset) throws java.lang.IllegalArgumentException;
}