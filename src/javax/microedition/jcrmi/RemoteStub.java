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
package javax.microedition.jcrmi;

public class RemoteStub 
{
    protected RemoteRefImpl ref;

    public RemoteStub() {}

    public void setRef(RemoteRefImpl ref) { this.ref = ref; }

    public boolean equals(Object obj) 
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RemoteStub that = (RemoteStub) obj;
        return ref != null && ref.remoteEquals(that.ref);
    }

    public int hashCode() 
    {
        return ref != null ? ref.remoteHashCode() : 0;
    }
}