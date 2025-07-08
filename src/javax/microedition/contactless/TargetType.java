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
package javax.microedition.contactless;

public class TargetType 
{

    public static final TargetType ISO14443_CARD = new TargetType("ISO14443_CARD");
    public static final TargetType NDEF_TAG = new TargetType("NDEF_TAG");
    public static final TargetType RFID_TAG = new TargetType("RFID_TAG");
    public static final TargetType VISUAL_TAG = new TargetType("VISUAL_TAG");

    private final String name;

    private TargetType(String name) { this.name = name; }

    public String toString() { return name; }
}