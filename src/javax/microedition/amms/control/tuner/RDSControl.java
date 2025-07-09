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

public interface RDSControl extends javax.microedition.media.Control 
{
    
    static final String RADIO_CHANGED = "radio_changed";
    static final String RDS_NEW_ALARM = "RDS_ALARM";
    static final String RDS_NEW_DATA = "RDS_NEW_DATA";

    boolean getAutomaticSwitching();
    
    boolean getAutomaticTA();
    
    java.util.Date getCT();
    
    int[] getFreqsByPTY(short PTY);
    
    int[][] getFreqsByTA(boolean TA);
    
    short getPI();
    
    String getPS();
    
    String[] getPSByPTY(short PTY);
    
    String[] getPSByTA(boolean TA);
    
    short getPTY();
    
    String getPTYString(boolean longer);
    
    String getRT();
    
    boolean getTA();
    
    boolean getTP();
    
    boolean isRDSSignal();
    
    void setAutomaticSwitching(boolean automatic) throws javax.microedition.media.MediaException;
    
    void setAutomaticTA(boolean automatic) throws javax.microedition.media.MediaException;
}