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

public interface EffectControl extends javax.microedition.media.Control 
{
    static final int SCOPE_LIVE_AND_RECORD = 3;
    static final int SCOPE_LIVE_ONLY = 1;
    static final int SCOPE_RECORD_ONLY = 2;

    String getPreset();
    
    String[] getPresetNames();
    
    int getScope();
    
    boolean isEnabled();
    
    boolean isEnforced();
    
    void setEnabled(boolean enable);
    
    void setEnforced(boolean enforced);
    
    void setPreset(String preset);
    
    void setScope(int scope) throws javax.microedition.media.MediaException;
}