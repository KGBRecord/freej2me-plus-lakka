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
package javacard.framework.service;

public class ServiceException extends javacard.framework.CardRuntimeException 
{
    public static final short CANNOT_ACCESS_IN_COMMAND = 4;
    public static final short CANNOT_ACCESS_OUT_COMMAND = 5;
    public static final short COMMAND_DATA_TOO_LONG = 3;
    public static final short COMMAND_IS_FINISHED = 6;
    public static final short DISPATCH_TABLE_FULL = 2;
    public static final short ILLEGAL_PARAM = 1;
    public static final short REMOTE_OBJECT_NOT_EXPORTED = 7;

    public ServiceException(short reason) { super(reason); }

    public static void throwIt(short reason) throws ServiceException 
    {
        throw new ServiceException(reason);
    }
}