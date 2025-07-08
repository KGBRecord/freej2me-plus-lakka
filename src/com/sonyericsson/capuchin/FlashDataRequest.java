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
package com.sonyericsson.capuchin;

import java.util.HashMap;
import java.util.Map;

public final class FlashDataRequest 
{
    private final String[] args;
    private final Map<String, Object> properties = new HashMap<String, Object>();
    private boolean completed = false;

    public FlashDataRequest(String[] args) 
    {
        if (args == null) { throw new NullPointerException("args cannot be null"); }
        this.args = args;
    }

    public String[] getArgs() { return args.clone(); }

    public void setProperty(String name, String value) 
    {
        validateProperty(name);
        properties.put(name, value);
    }

    public void setProperty(String name, int value) 
    {
        validateProperty(name);
        properties.put(name, value);
    }

    public void complete() { completed = true; }

    private void validateProperty(String name) 
    {
        if (name == null) { throw new NullPointerException("name cannot be null"); }
        if (completed) { throw new IllegalStateException("This request has been completed"); }
    }
}