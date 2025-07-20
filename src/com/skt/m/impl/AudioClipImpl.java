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
package com.skt.m.impl;

import com.skt.m.AudioClip;
import com.skt.m.ResourceAllocException;
import com.skt.m.UnsupportedFormatException;
import com.skt.m.UserStopException;

import java.io.IOException;
import java.util.concurrent.locks.LockSupport;

// based off of Samsung.util.AudioClip, but play() is blocking and only seems to support mmf
public class AudioClipImpl implements AudioClip 
{

    private int volume = 5;
    private com.samsung.util.AudioClip player;

    public void open(byte[] data, int offset, int bufferSize) throws UnsupportedFormatException, ResourceAllocException 
    {
        player = new com.samsung.util.AudioClip(com.samsung.util.AudioClip.TYPE_MMF, data, offset, bufferSize);
    }

    public void close() throws IOException 
    { 
        player.close();
        player = null;
    }

    public void play() throws UserStopException, IOException { play(1, volume); }

    public void loop() throws UserStopException, IOException { play(0, volume); } // 0 loops is infinite looping for MMF

    private void play(int loops, int volume) 
    {
        player.play(loops, volume);

        // SKT's AudioClip playback is thread-blocking
        while(player.isRunning()) { LockSupport.parkNanos(1000000); }
    }

    public void stop() throws IOException { player.stop(); }

    public void pause() throws IOException { player.pause(); }

    public void resume() throws IOException { player.resume(); }

    public int getVolume() { return volume; }

    public void setVolume(int volume) { this.volume = volume; }
}
