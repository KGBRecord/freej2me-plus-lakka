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
package com.nttdocomo.util;

import com.nttdocomo.lang.XString;

public interface Phone 
{
    public static final String TEL_AV = "tel-av:";
    public static final String TERMINAL_ID = "terminal-id";
    public static final String UIM_VERSION = "uim-version";
    public static final String USER_ID = "user-id";

	public static void call(String phoneNumber) {
        if (phoneNumber == null) {
            throw new NullPointerException("phoneNumber cannot be null");
        }
        if (!isValidPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number");
        }
        // Implement call logic here
    }

    public static void call(XString phoneNumber) {
        if (phoneNumber == null) {
            throw new NullPointerException("phoneNumber cannot be null");
        }
        call(phoneNumber.toString());
    }

    public static void call(String telType, XString phoneNumber) {
        if (!TEL_AV.equals(telType)) {
            throw new IllegalArgumentException("Invalid telType. Must be 'tel-av:'");
        }
        if (phoneNumber == null) {
            throw new NullPointerException("phoneNumber cannot be null");
        }
        call(phoneNumber.toString());
    }

    public static String getProperty(String key) 
	{
        if (key == null) { throw new NullPointerException("key cannot be null"); }
        if (key.isEmpty()) { throw new IllegalArgumentException("key cannot be empty"); }
        switch (key) 
		{
            case TERMINAL_ID:
                return getTerminalId();
            case USER_ID:
                return getUserId();
            case UIM_VERSION:
                return getUimVersion();
            default:
                return null;
        }
    }

    static boolean isValidPhoneNumber(String phoneNumber) { return true; }

    static String getTerminalId() { return "000000123456789"; }

    static String getUserId() { return "00000000000123456789"; }

    static String getUimVersion() { return "1.0.0"; }
}