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
package javax.microedition.content;

public class Registry 
{

    ResponseListener listener;
    Invocation invocation;

    public void cancelGetResponse() { }

    public ContentHandler[] findHandler(Invocation invocation) { return null; }

    public ContentHandler[] forAction(String action) { return null; }

    public ContentHandler forID(String ID, boolean exact) { return null; }

    public ContentHandler[] forSuffix(String suffix) { return null; }

    public ContentHandler[] forType(String type) { return null; }

    public String[] getActions() { return null; }

    public String getID() { return ""; }

    public String[] getIDs() { return null; }

    public static Registry getRegistry(String classname) { return null; }

    public Invocation getResponse(boolean wait) { return null; }

    public static ContentHandlerServer getServer(String classname) { return null; }

    public String[] getSuffixes() { return null; }

    public String[] getTypes() { return null; }

    public boolean invoke(Invocation invocation) 
    {
        invocation.invokerID = "";
        invocation.invokerAuth = "";
        invocation.invokerName = "";
        this.invocation = invocation;
        return false; 
    }

    public boolean invoke(Invocation invocation, Invocation previous) 
    {
        invocation.invokerID = "";
        invocation.invokerAuth = "";
        invocation.invokerName = "";
        invocation.setPrevious(previous);
        this.invocation = invocation;
        return false; 
    }

    public ContentHandlerServer register(String classname, String[] types, String[] suffixes, String[] actions, ActionNameMap[] actionnames, String ID, String[] accessAllowed) 
    {
        return null;
    }

    public boolean reinvoke(Invocation invocation) { return false; }

    public void setListener(ResponseListener listener) { this.listener = listener; }

    public boolean unregister(String classname) { return false; }
}