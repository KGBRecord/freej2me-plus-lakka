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
package com.nttdocomo.fs;

import com.nttdocomo.io.FileEntity;

public class File
{

    public static final int MODE_READ_ONLY = 0;
    public static final int MODE_WRITE_ONLY = 1;
    public static final int MODE_READ_WRITE = 2;

    private String path;
    FileEntity file;

    public File(String path) throws java.io.IOException
    { 
        file = new FileEntity(path);
        this.path = path; 
    }

    public void delete() throws java.io.IOException { file.deleteFile(); }

    // Todo
    public AccessToken getAccessToken() { return null; }

    public Folder getFolder() { return null; }

    // Todo
    public long getLastModified() throws java.io.IOException { return 0; }

    // Todo
    public long getLength() throws java.io.IOException { return 0; }

    public String getPath() { return path; }

    public FileEntity open(int mode) throws java.io.IOException 
    {
        if (mode < 0 || mode > 2) { throw new IllegalArgumentException("Invalid mode."); }

        return file;
    }
}