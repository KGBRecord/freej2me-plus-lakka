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
package com.nttdocomo.ui;

import java.util.HashMap;

import org.recompile.mobile.MobilePlatform;
import org.recompile.mobile.Mobile;

public abstract class IApplication
{
    public static HashMap<String, String> properties;
    
    protected IApplication()
	{
		Mobile.log(Mobile.LOG_INFO, IApplication.class.getPackage().getName() + "." + IApplication.class.getSimpleName() + ": " + "Create DoJa IApplication");
		Mobile.iAppli = this;
	}

    public final String[] getArgs() 
    {
        return new String[0];
    }

    public static void initAppProperties(HashMap<String, String> initProperties)
	{
		properties = initProperties;
	}

    public String getSourceURL() { return properties.get("PackageURL"); }

    public static final IApplication getCurrentApp() 
    {
        return Mobile.iAppli; 
    }

    public abstract void start();

    public void resume() 
    {
        MobilePlatform.pauseResumeApp();
    }

    public final void terminate() 
    { 
        Mobile.log(Mobile.LOG_INFO, IApplication.class.getPackage().getName() + "." + IApplication.class.getSimpleName() + ": " + "I-Appli sent termination request");
		System.exit(0);
    }
}