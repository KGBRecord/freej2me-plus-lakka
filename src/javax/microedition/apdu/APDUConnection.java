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
package javax.microedition.apdu;

import java.io.IOException;

public interface APDUConnection 
{

    byte[] changePin(int pinID) throws IOException;

    byte[] disablePin(int pinID) throws IOException;

    byte[] enablePin(int pinID) throws IOException;

    byte[] enterPin(int pinID) throws IOException;

    byte[] exchangeAPDU(byte[] commandAPDU) throws IOException;

    byte[] getATR();

    byte[] unblockPin(int blockedPinID, int unblockingPinID) throws IOException;
}