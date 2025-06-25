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
package com.nokia.mid.setting;

import java.util.ArrayList;
import java.util.List;

public final class ToneSetting 
{
    public static final int SETTING_RINGTONE = 0x01;
    public static final int SETTING_RINGTONE_SIM2 = 0x02;
    public static final int SETTING_MESSAGETONE = 0x03;

    public static final String MESSAGETONE_PATH = "/messagetone.ota";
    public static final String RINGTONE_PATH = "/ringtone.ota";
    public static final String RINGTONE_SIM2_PATH = "/sim2ringtone.ota";

    private static List<ToneSettingListener> listeners = new ArrayList<ToneSettingListener>();

    public static String getToneSetting(int index) 
    {
        switch (index) 
        {
            case SETTING_RINGTONE:
                return RINGTONE_PATH;
            case SETTING_RINGTONE_SIM2:
                return RINGTONE_SIM2_PATH;
            case SETTING_MESSAGETONE:
                return MESSAGETONE_PATH;
            default:
                return null;
        }
    }

    public static int getVolumeSetting(int index) 
    {
        switch (index) 
        {
            case SETTING_RINGTONE:
                return 10;
            case SETTING_RINGTONE_SIM2:
                return 10;
            case SETTING_MESSAGETONE:
                return 10;
            default:
                return -1;
        }
    }

    public static void subscribeListener(ToneSettingListener listener) 
    {
        if (!listeners.contains(listener)) { listeners.add(listener); }
    }

    public static void unSubscribeListener(ToneSettingListener listener) 
    {
        listeners.remove(listener);
    }
}