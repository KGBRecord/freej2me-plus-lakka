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
package com.sun.midp.lcdui;

public interface InputMethodClient 
{
    String[] getAllowedModes();
    
    int getConstraints();
    
    javax.microedition.lcdui.Display getDisplay();
    
    String getInputMode();
    
    boolean isNewInputEntry();
    
    boolean isNewSentence();
    
    boolean isNewWord();
    
    void keyEntered(int keyCode);
    
    void setAllowedModes(String[] allowedModes);
    
    boolean setConstraints(int constraints);
    
    void setCurrent(javax.microedition.lcdui.Displayable displayable, javax.microedition.lcdui.Display display);
    
    void setInputMethodHandler(InputMethodHandler imh);
    
    void setInputMode(String inputMode);
    
    void showInputMode(int mode);
}