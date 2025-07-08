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
package com.sonyericsson.capuchin;

import javax.microedition.lcdui.Displayable;

public class FlashPlayer 
{
    private enum State 
    {
        PLAYING,
        STOPPED,
        PAUSED
    }

    private State state;
    private FlashImage flashImage;
    private FlashCanvas flashCanvas;

    private FlashPlayer(FlashImage flashImage, FlashCanvas flashCanvas) 
    {
        this.flashImage = flashImage;
        this.flashCanvas = flashCanvas;
        this.state = State.PLAYING;
    }

    public static FlashPlayer createFlashPlayer(FlashImage flashImage, FlashCanvas flashCanvas) 
    {
        if (flashImage == null || flashCanvas == null) { throw new NullPointerException("flashImage and flashCanvas cannot be null"); }
        return new FlashPlayer(flashImage, flashCanvas);
    }

    public Displayable getDisplayable() { return flashCanvas; }

    public void play() 
    {
        if (state == State.STOPPED || state == State.PAUSED) { state = State.PLAYING; } 
        else { throw new IllegalStateException("Cannot play: FlashPlayer is already playing"); }
    }

    public void pause() 
    {
        if (state == State.PLAYING) { state = State.PAUSED; }
        else { throw new IllegalStateException("Cannot pause: FlashPlayer is not currently playing"); }
    }

    public void stop() 
    {
        if (state == State.PLAYING || state == State.PAUSED) { state = State.STOPPED; }
        else { throw new IllegalStateException("Cannot stop: FlashPlayer is already stopped"); }
    }
}