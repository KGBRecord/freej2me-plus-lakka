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
package com.nttdocomo.ui;

import org.recompile.mobile.Mobile;

import javax.microedition.media.Manager;

public class PhoneSystem 
{

    public static final int ATTR_BACKLIGHT_OFF = 0;
    public static final int ATTR_BACKLIGHT_ON = 1;
    public static final int ATTR_VIBRATOR_OFF = 0;
    public static final int ATTR_VIBRATOR_ON = 1;
    public static final int DEV_BACKLIGHT = 0;
    public static final int DEV_VIBRATOR = 1;
    public static final int SOUND_INFO = 0;
    public static final int SOUND_WARNING = 1;
    public static final int SOUND_ERROR = 2;
    public static final int SOUND_ALARM = 3;
    public static final int SOUND_CONFIRM = 4;

    private PhoneSystem() {}

    public static void setAttribute(int attr, int value) 
    {
        if (!isValidAttribute(attr, value)) { throw new IllegalArgumentException("Invalid attribute or value."); }

        Mobile.log(Mobile.LOG_DEBUG, IApplication.class.getPackage().getName() + "." + IApplication.class.getSimpleName() + ": " + "I-Appli set " + (attr == 0 ? "backlight" : "vibrator") + " to:" + (value == 1));
    }

    public static void playSound(int type) 
    {
        if (type < SOUND_INFO || type > SOUND_CONFIRM) { throw new IllegalArgumentException("Invalid sound type."); }

        try 
        { 
            DoJaSoundPlayer player = new DoJaSoundPlayer(type);
            Thread soundThread = new Thread(player, "DoJa-PlaySound");
            soundThread.start();
        }
        catch (Exception e) { }
    }

    private static boolean isValidAttribute(int attr, int value) 
    {
        if (attr == DEV_BACKLIGHT) 
        {
            return value == ATTR_BACKLIGHT_OFF || value == ATTR_BACKLIGHT_ON;
            
        } 
        else if (attr == DEV_VIBRATOR) 
        {
            return value == ATTR_VIBRATOR_OFF || value == ATTR_VIBRATOR_ON;
        }
        return false; 
    }

    public static final boolean isAvailable(final int n) { return true; }
}

class DoJaSoundPlayer implements Runnable 
{
    private final int type;

    public DoJaSoundPlayer(int type) { this.type = type; }

    public final void run()
    {
        try 
        {
            switch (type) 
            {
                case PhoneSystem.SOUND_INFO:
                    Manager.playTone(0, 30, 127);
                    Thread.sleep(40);
                    Manager.playTone(88, 90, 127);
                    Thread.sleep(60);
                    Manager.playTone(90, 90, 127);
                    Thread.sleep(60);
                    Manager.playTone(96, 180, 127);
                    break;
            
                case PhoneSystem.SOUND_WARNING:
                    Manager.playTone(0, 30, 127);
                    Thread.sleep(40);
                    Manager.playTone(86, 100, 127);
                    Thread.sleep(70);
                    Manager.playTone(83, 100, 127);
                    Thread.sleep(70);
                    Manager.playTone(86, 100, 127);
                    Thread.sleep(70);
                    Manager.playTone(84, 100, 127);
                    break;

                case PhoneSystem.SOUND_ERROR:
                    Manager.playTone(0, 30, 127);
                    Thread.sleep(40);
                    Manager.playTone(64, 120, 127);
                    Manager.playTone(72, 150, 127);
                    Thread.sleep(90);
                    Manager.playTone(64, 120, 127);
                    Thread.sleep(60);
                    Manager.playTone(62, 150, 127);
                    Thread.sleep(60);
                    Manager.playTone(59, 150, 127);
                    Thread.sleep(60);
                    Manager.playTone(57, 200, 127);
                    break;

                case PhoneSystem.SOUND_ALARM:
                    Manager.playTone(0, 30, 127);
                    Thread.sleep(40);
                    Manager.playTone(88, 300, 127);
                    Thread.sleep(250);
                    Manager.playTone(82, 300, 127);
                    Thread.sleep(250);
                    Manager.playTone(88, 300, 127);
                    Thread.sleep(250);
                    Manager.playTone(82, 350, 127);
                    break;

                case PhoneSystem.SOUND_CONFIRM:
                    Manager.playTone(0, 30, 127);
                    Thread.sleep(40);
                    Manager.playTone(84, 40, 127);
                    Thread.sleep(40);
                    Manager.playTone(91, 40, 127);
                    break;

                default:
                    break;
            }
        }
        catch (Exception e) { Mobile.log(Mobile.LOG_DEBUG, IApplication.class.getPackage().getName() + "." + IApplication.class.getSimpleName() + ": " + "Failed to play sound " + e.getMessage()); }
    }
}