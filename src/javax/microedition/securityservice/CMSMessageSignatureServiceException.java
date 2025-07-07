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
package javax.microedition.securityservice;

public final class CMSMessageSignatureServiceException extends Exception 
{
    public static final byte CRYPTO_FAILURE = 1;
    public static final byte CRYPTO_FORMAT_ERROR = 2;
    public static final byte CRYPTO_NO_CERTIFICATE = 8;
    public static final byte CRYPTO_NO_DETACHED_SIG = 3;
    public static final byte CRYPTO_NO_OPAQUE_SIG = 4;
    public static final byte SE_BUSY = 5;
    public static final byte SE_CRYPTO_FAILURE = 7;
    public static final byte SE_FAILURE = 6;

    private final byte reasonCode;

    public CMSMessageSignatureServiceException(byte code) 
    {
        super("Error code: " + code);
        this.reasonCode = code;
    }

    public byte getReason() 
    {
        return reasonCode;
    }
}