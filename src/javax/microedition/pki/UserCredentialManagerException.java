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
package javax.microedition.pki;

public final class UserCredentialManagerException extends Exception 
{
    
    public static final byte CREDENTIAL_NOT_FOUND = 5;
    public static final byte CREDENTIAL_NOT_SAVED = 0;
    public static final byte SE_NO_KEYGEN = 1;
    public static final byte SE_NO_KEYS = 2;
    public static final byte SE_NO_UNASSOCIATED_KEYS = 3;
    public static final byte SE_NOT_FOUND = 4;

    private final byte reasonCode;

    public UserCredentialManagerException(byte code) 
    {
        super("UserCredentialManagerException: " + getMessageForCode(code));
        this.reasonCode = code;
    }

    public byte getReason() { return reasonCode; }

    private static String getMessageForCode(byte code) 
    {
        switch (code) 
        {
            case CREDENTIAL_NOT_FOUND:
                return "Credential not found.";
            case CREDENTIAL_NOT_SAVED:
                return "Credential could not be saved.";
            case SE_NO_KEYGEN:
                return "Security element does not support key generation.";
            case SE_NO_KEYS:
                return "No keys available for certificate requests.";
            case SE_NO_UNASSOCIATED_KEYS:
                return "No unassociated keys available.";
            case SE_NOT_FOUND:
                return "Security element not found.";
            default:
                return "Unknown error.";
        }
    }
}