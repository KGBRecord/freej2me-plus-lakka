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
package com.nttdocomo.lang;

public final class MemoryManager 
{
    public static final int JAVA_HEAP = 0;

    private static final MemoryManager INSTANCE = new MemoryManager();

    private MemoryManager() {
        // Private constructor to prevent instantiation
    }

    public static MemoryManager getMemoryManager() { return INSTANCE; }

    // I don't think java even supports memory partitioning
    public int getNumPartitions() { return 1; }

    public long[] totalMemory() { return new long[]{Runtime.getRuntime().totalMemory()}; }

    public long[] freeMemory() { return new long[]{Runtime.getRuntime().freeMemory()}; }

    // Java has no concept of this either, return a "reasonable" value of 32MB
    public long[] maxContiguousMemory() { return new long[]{Short.MAX_VALUE}; }
}