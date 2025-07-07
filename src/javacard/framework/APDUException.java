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
package javacard.framework;

public class APDUException extends CardRuntimeException 
{
    public static final short BAD_LENGTH = 3;
    public static final short BUFFER_BOUNDS = 2;
    public static final short ILLEGAL_USE = 1;
    public static final short IO_ERROR = 4;
    public static final short NO_T0_GETRESPONSE = 170;
    public static final short NO_T0_REISSUE = 171;
    public static final short T1_IFD_ABORT = 172;

    public APDUException(short reason) { super(reason); }

    public static void throwIt(short reason) throws APDUException 
    {
        throw new APDUException(reason);
    }
}