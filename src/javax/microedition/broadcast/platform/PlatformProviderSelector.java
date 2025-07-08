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
package javax.microedition.broadcast.platform;

import javax.microedition.broadcast.BroadcastServiceException;

public class PlatformProviderSelector 
{

    public static void abort() { }

    public static void addListener(PlatformProviderSelectorListener ppl) { return; }

    public static PlatformProvider createPlatformProvider(int frequency, long id, String name) 
            throws UnsupportedOperationException, IllegalArgumentException, NullPointerException 
    {
        if (frequency <= 0) { throw new IllegalArgumentException("Frequency must be positive."); }
        if (name == null) { throw new NullPointerException("Name cannot be null."); }

        return null;
    }

    public static void discover() { }

    public static PlatformProvider[] getAvailableProviders() { return null; }

    public static PlatformProvider getCurrentProvider() { return null; }

    public static java.util.Date getCurrentProviderDate() 
    {
        if (getCurrentProvider() == null) { throw new IllegalStateException("No selected broadcast network provider."); }

        return null;
    }

    public static PlatformProvider getDefaultProvider() throws BroadcastServiceException { return null; }

    public static void removeListener(PlatformProviderSelectorListener ppl) { return; }

    public static void select(PlatformProvider pp) { return; }
}