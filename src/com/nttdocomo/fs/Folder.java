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

// Fully stubbed at the moment
public class Folder extends Object 
{

    private String path;

    public Folder(String path) { this.path = path; }

    public File createFile(String fileName) throws java.io.IOException { return createFile(fileName, null); }

    public File createFile(String fileName, FileAttribute[] attributes) throws java.io.IOException 
    { return null; }

    public AccessToken getAccessToken() { return null; }

    public File getFile(String fileName) { return null; }

    public File[] getFiles() { return new File[0]; }

    public long getFreeSize() throws java.io.IOException { return 0; }

    public String getPath() { return path; }

    public com.nttdocomo.device.StorageDevice getStorageDevice() { return null; }

    public boolean isFileAttributeSupported(Class clazz) { return false; }
}