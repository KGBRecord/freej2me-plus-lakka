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
package javax.microedition.broadcast;

import javax.microedition.broadcast.connection.BroadcastDatagramConnection;
import javax.microedition.broadcast.esg.Service;

public abstract class ServiceContext 
{
    public static final int ACQUIRING_SERVICE = 2;
    public static final int CLOSED = 0;
    public static final String LANGUAGE = "language";
    public static final int PREPARING_MEDIA = 4;
    public static final int PRESENTING = 5;
    public static final int SERVICE_ACQUIRED = 3;
    public static final int STOPPED = 1;

    public ServiceContext() { }

    public abstract void addListener(ServiceContextListener listener) throws IllegalStateException;

    public abstract void close() throws SecurityException;

    public static ServiceContext createServiceContext() throws SecurityException 
    {
        return null;
    }

    public abstract ServiceComponent[] getAllComponents() throws IllegalStateException;

    public abstract String[] getAllPreferenceKeys() throws IllegalStateException;

    public abstract BroadcastDatagramConnection getBroadcastDatagramConnection(ServiceComponent component)
            throws BroadcastServiceException, SecurityException, IllegalStateException, NullPointerException;

            public static ServiceContext getDefaultContext() throws SecurityException 
    {
        return null;
    }
    public abstract javax.microedition.media.Player getPlayer(ServiceComponent component)
            throws BroadcastServiceException, IllegalStateException, NullPointerException;

    public abstract javax.microedition.media.Player[] getPlayers() throws IllegalStateException;

    public abstract Object getPreference(String key) throws IllegalStateException, NullPointerException;

    public abstract ServiceComponent[] getSelectedComponents() throws IllegalStateException;

    public abstract Service getService() throws IllegalStateException;

    public abstract int getSignalQuality() throws IllegalStateException;

    public abstract int getState();

    public abstract ServiceComponent[] getUnrecognizedComponents() throws IllegalStateException;

    public abstract ServiceComponent[] getUnselectedComponents() throws IllegalStateException;

    public abstract void removeListener(ServiceContextListener listener) throws IllegalStateException;

    public abstract void select(Service service) throws IllegalArgumentException, SecurityException, IllegalStateException, NullPointerException;

    public abstract void setComponents(ServiceComponent[] components)
            throws InsufficientResourcesException, IllegalStateException, BroadcastServiceException, NullPointerException;

    public abstract Object setPreference(String key, Object value) throws IllegalStateException, NullPointerException;

    public abstract void stop() throws IllegalStateException;
}