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

public interface ServiceContextListener 
{
    static final String COMPONENTS_IDENTIFIED = "ComponentsIdentified";
    static final String COMPONENTS_SET = "ComponentsSet";
    static final String CONTEXT_CLOSED = "ContextClosed";
    static final String CONTEXT_STOPPED = "ContextStopped";
    static final String PLAYERS_REALIZED = "PlayersRealized";
    static final String PRESENTATION_STARTED = "PresentationStarted";
    static final String REASON_ALTERNATIVE_CONTENT = "AlternativeContent";
    static final String REASON_APPLICATION_REQUESTED = "ApplicationRequested";
    static final String REASON_EQUIVALENT_SERVICE = "EquivalentService";
    static final String REASON_NO_RIGHT = "NoRight";
    static final String REASON_NORMAL_CONTENT = "NormalContent";
    static final String REASON_OTHER = "Other";
    static final String REASON_RESOURCE_UNAVAILABLE = "ResourcesUnavailable";
    static final String REASON_SELECTION_FAILED = "SelectionFailed";
    static final String REASON_SERVICE_UNAVAILABLE = "ServiceUnavailable";
    static final String REASON_SWITCH_FORCED = "SwitchForced";
    static final String SELECTION_INITIATED = "SelectionInitiated";

    void contextUpdate(ServiceContext context, String event, Object data);
}