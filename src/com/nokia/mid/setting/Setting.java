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

public final class Setting 
{
    public static final int SETTING_FLIGHT_MODE = 0x01;
    public static final int SETTING_DATA_CONNECTION = 0x02;
    public static final int SETTING_ROAMING_DATA_CONNECTION = 0x03;
    public static final int SETTING_VIBRATOR = 0x04;
    public static final int SETTING_SILENT = 0x05;
    public static final int SETTING_BACKGROUND_DATA_CONNECTION = 0x06;
    public static final int INVALID = 0x00;
    public static final int ON = 0x01;
    public static final int OFF = 0x02;
    public static final int DENY = 0x03;
    public static final int ASK = 0x04;
    public static final int ACCEPT = 0x05;
    public static final int WIFIONLY = 0x06;

    private static List<SettingListener> listeners = new ArrayList<SettingListener>();

    public static int getSetting(int index) 
    {
        switch (index) 
        {
            case SETTING_FLIGHT_MODE:
                return ON;
            case SETTING_DATA_CONNECTION: // FreeJ2ME-Plus doesn't allow web connections
                return DENY;
            case SETTING_ROAMING_DATA_CONNECTION:
                return DENY;
            case SETTING_VIBRATOR: // Vibrator always enabled
                return ON;
            case SETTING_SILENT: // Never in silent mode
                return OFF;
            case SETTING_BACKGROUND_DATA_CONNECTION:
                return WIFIONLY; // Example value
            default:
                return INVALID;
        }
    }

    public static void subscribeListener(SettingListener listener) 
    {
        if (!listeners.contains(listener)) { listeners.add(listener); }
    }

    public static void unSubscribeListener(SettingListener listener) 
    {
        listeners.remove(listener);
    }
}