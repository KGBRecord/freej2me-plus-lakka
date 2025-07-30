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
package com.nttdocomo.device.gesturereader;

public class RollData 
{
    public static final int QUALITY_RELIABLE = 0;
    public static final int QUALITY_LOW_DETAIL = 1;
    public static final int QUALITY_TOO_FAST = 2;
    public static final int QUALITY_SAME_VALUE = 3;

    private float[] accumulatedMotion;
    private float[] immediateMotion;
    private int quality;

    public RollData(float[] accumulatedMotion, float[] immediateMotion, int quality) 
    {
        this.accumulatedMotion = accumulatedMotion;
        this.immediateMotion = immediateMotion;
        this.quality = quality;
    }

    public float[] getAccumulatedMotion() 
    {
        return accumulatedMotion;
    }

    public float[] getImmediateMotion() 
    {
        return immediateMotion;
    }

    public int getQuality() 
    {
        return quality;
    }
}