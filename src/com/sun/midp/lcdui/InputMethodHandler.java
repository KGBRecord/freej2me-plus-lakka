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

import com.sun.midp.lcdui.impls.InputMethodHandlerImpl;

import org.recompile.mobile.Mobile;

public abstract class InputMethodHandler 
{
    public static final int KEYCODE_CLEAR = -2;
    public static final int KEYCODE_CLEARALL = -3;
    public static final int KEYCODE_NONE = -1;
    public static final int KEYCODE_SHIFT = -5;
    public static final int KEYCODE_SIGNCHANGE = -4;
    
    private static InputMethodHandlerImpl thisIM = new InputMethodHandlerImpl();
    private int inputMode = 1;

    protected InputMethodHandler() { }

    public static InputMethodHandler getInputMethodHandler() { return thisIM; }

    private static InputMethodHandler getInputMethodHandlerImpl() { return thisIM; }

    public abstract void switchInputMode(int mode);

    public abstract int getInputMode();

    public abstract void setInputMethodClient(InputMethodClient imc);

    public abstract boolean clearInputMethodClient(InputMethodClient imc);

    public abstract int keyPressed(int keyCode);

    public abstract int keyReleased(int keyCode);

    public abstract int keyRepeated(int keyCode);

    public abstract int keyTyped(char c);

    public abstract void flush();

    public abstract String[] supportedInputModes();

    public abstract boolean setConstraints(int constraints);

    public boolean isSymbol(char c) { return !Character.isLetterOrDigit(c); }

    public abstract void endComposition(boolean discard);
}