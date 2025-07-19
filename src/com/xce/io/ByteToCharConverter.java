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
package com.xce.io;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;

public abstract class ByteToCharConverter 
{
    private final CharsetDecoder decoder;

    private static final byte[] emptyByteArray = new byte[0];

    protected ByteToCharConverter(CharsetDecoder decoder) 
    {
        this.decoder = decoder;
    }

    public int convert(byte[] input, int inStart, int inLength, char[] output, int outStart, int outLength) 
    {
        ByteBuffer bb = ByteBuffer.wrap(input, inStart, inLength);
        CharBuffer cb = CharBuffer.wrap(output, outStart, outLength);
        decoder.decode(bb, cb, false);
        return cb.position() - outStart;
    }

    public int flush(char[] output, int outStart, int outLength) 
    {
        ByteBuffer bb = ByteBuffer.wrap(emptyByteArray);
        CharBuffer cb = CharBuffer.wrap(output, outStart, outLength);
        decoder.decode(bb, cb, true);
        decoder.flush(cb);
        decoder.reset();
        return cb.position() - outStart;
    }
}
