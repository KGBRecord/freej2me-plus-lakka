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
package javax.microedition.amms.control;

public interface FormatControl extends javax.microedition.media.Control 
{
    static final int METADATA_NOT_SUPPORTED = 0;
    static final int METADATA_SUPPORTED_FIXED_KEYS = 1;
    static final int METADATA_SUPPORTED_FREE_KEYS = 2;

    static final String PARAM_BITRATE = "bitrate";
    static final String PARAM_BITRATE_TYPE = "bitrate ype";
    static final String PARAM_FRAMERATE = "frame rate";
    static final String PARAM_QUALITY = "quality";
    static final String PARAM_SAMPLERATE = "sample rate";
    static final String PARAM_VERSION_TYPE = "version type";

    int getEstimatedBitRate() throws javax.microedition.media.MediaException;
    
    String getFormat();
    
    int getIntParameterValue(String parameter);
    
    boolean getMetadataOverride();
    
    int getMetadataSupportMode();
    
    String getStrParameterValue(String parameter);
    
    String[] getSupportedFormats();
    
    int[] getSupportedIntParameterRange(String parameter);
    
    String[] getSupportedIntParameters();
    
    String[] getSupportedMetadataKeys();
    
    String[] getSupportedStrParameters();
    
    String[] getSupportedStrParameterValues(String parameter);
    
    void setFormat(String format);
    
    void setMetadata(String key, String value) throws javax.microedition.media.MediaException;
    
    void setMetadataOverride(boolean override);
    
    int setParameter(String parameter, int value);
    
    void setParameter(String parameter, String value);
}