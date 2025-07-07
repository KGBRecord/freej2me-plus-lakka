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
package javacard.security;

public class CryptoException extends javacard.framework.CardRuntimeException  
{
    public static final short ILLEGAL_USE = 5;
    public static final short ILLEGAL_VALUE = 1;
    public static final short INVALID_INIT = 4;
    public static final short NO_SUCH_ALGORITHM = 3;
    public static final short UNINITIALIZED_KEY = 2;

    public CryptoException(short reason) { super(reason); }

    public static void throwIt(short reason) throws CryptoException { throw new CryptoException(reason); }
}