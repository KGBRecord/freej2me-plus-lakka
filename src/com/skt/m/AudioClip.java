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
package com.skt.m;

import java.io.IOException;

public interface AudioClip 
{
    void open(byte[] data, int offset, int bufferSize)
            throws UnsupportedFormatException, ResourceAllocException;

    void close() throws IOException;

    void play() throws UserStopException, IOException;

    void loop() throws UserStopException, IOException;

    void stop() throws IOException;

    void pause() throws IOException;

    void resume() throws IOException;
}
