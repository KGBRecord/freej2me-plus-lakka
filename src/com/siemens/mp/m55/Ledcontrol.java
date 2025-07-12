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
package com.siemens.mp.m55;

public class Ledcontrol 
{
    public static final int LED_BOTTOM = 0;
    public static final int LED_TOP = 1; 

    public static final int P_BEAT = 0;
    public static final int P_CONSTANLY_LITEUP = 1;
    public static final int P_ETERNITY = 2;
    public static final int P_IDLE = 3;
    public static final int P_LIGHTHOUSE = 4;
    public static final int P_LIMELIGHT = 5;
    public static final int P_NORMAL_BLINKING = 6;
    public static final int P_PULSATING = 7;
    public static final int P_RUNWAY = 8;
    public static final int P_SPEED = 9;
    public static final int P_STROBO = 10;
    public static final int P_TRANCE = 11;
    public static final int P_WAVE = 12;

    public static void switchON(int led) 
	{
        if (led == LED_TOP) { } 
		else if (led == LED_BOTTOM) { } 
		else { throw new IllegalArgumentException("Invalid LED identifier"); }
    }

    public static void switchOFF(int led) 
	{
        if (led == LED_TOP) { } 
		else if (led == LED_BOTTOM) { } 
        else { throw new IllegalArgumentException("Invalid LED identifier"); }
    }

    public static void playPattern(int pattern) 
    { 
        if(pattern < P_BEAT || pattern > P_WAVE) { throw new IllegalArgumentException("Invalid pattern identifier"); }
    }

    public static void stopPattern() { }
}