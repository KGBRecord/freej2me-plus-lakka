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

import java.io.IOException;
import java.io.OutputStream;

public class FileOutputStream extends OutputStream 
{
    private final XFile file;

    public FileOutputStream(XFile f) throws IOException 
    {
        this.file = f;
    }

    public FileOutputStream(String name) throws IOException 
    {
        this.file = new XFile(name, XFile.WRITE);
    }

    @Override
    public void write(int b) throws IOException 
    {
        file.write(new byte[]{(byte) b}, 0, 1);
    }

    @Override
    public void write(byte[] b) throws IOException 
    {
        file.write(b, 0, b.length);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException 
    {
        file.write(b, off, len);
    }

    @Override
    public void close() throws IOException { file.close(); }

    @Override
    public void flush() throws IOException { file.flush(); }
}
