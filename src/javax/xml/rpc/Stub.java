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
package javax.xml.rpc;

public interface Stub 
{

    static final String USERNAME_PROPERTY = "javax.xml.rpc.username";
    static final String PASSWORD_PROPERTY = "javax.xml.rpc.password";
    static final String ENDPOINT_ADDRESS_PROPERTY = "javax.xml.rpc.service.endpoint.address";
    static final String SESSION_MAINTAIN_PROPERTY = "javax.xml.rpc.session.maintain";

    public Object _getProperty(String name);

    public void _setProperty(String name, Object value);
}
