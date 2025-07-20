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
package com.sun.midp.lcdui.impls;

import com.sun.midp.lcdui.InputMethodHandler;
import com.sun.midp.lcdui.InputMethodClient;

import java.util.ArrayList;
import java.util.List;

import org.recompile.mobile.Mobile;

public class InputMethodHandlerImpl extends InputMethodHandler
{

    private InputMethodClient currentClient;
    private List<String> allowedModes;
    private int inputMode = 1;

    public InputMethodHandlerImpl() 
    {
        allowedModes = new ArrayList<String>();
        allowedModes.add("DEFAULT");
        allowedModes.add("NUMERIC");
        allowedModes.add("ALPHABETIC");
    }

    public void switchInputMode(int mode) 
    {
        Mobile.log(Mobile.LOG_WARNING, InputMethodHandler.class.getPackage().getName() + "." + InputMethodHandler.class.getSimpleName() + ": " + "switchInputMode:" + mode);
        if (mode >= 0 && mode < allowedModes.size()) { inputMode = mode; }
    }

    public int getInputMode() 
    { 
        Mobile.log(Mobile.LOG_WARNING, InputMethodHandler.class.getPackage().getName() + "." + InputMethodHandler.class.getSimpleName() + ": " + "getInputMode:" + inputMode);
        return inputMode; 
    }

    public void setInputMethodClient(InputMethodClient imc) 
    {
        this.currentClient = imc;
    }

    public boolean clearInputMethodClient(InputMethodClient imc) 
    {
        if (currentClient == imc) 
        {
            currentClient = null;
            return true;
        }
        return false;
    }

    public int keyPressed(int keyCode) 
    {
        if (currentClient != null) 
        {
            currentClient.keyEntered(keyCode);
            return keyCode;
        }
        return KEYCODE_NONE;
    }

    public int keyReleased(int keyCode) 
    {
        return keyCode;
    }

    public int keyRepeated(int keyCode) 
    {
        return keyCode;
    }

    public int keyTyped(char c) 
    {
        return (int) c;
    }

    public void flush() { }

    public String[] supportedInputModes() 
    {
        return allowedModes.toArray(new String[0]);
    }

    public boolean setConstraints(int constraints) 
    {
        return true; 
    }

    public void endComposition(boolean discard) 
    {

    }
}