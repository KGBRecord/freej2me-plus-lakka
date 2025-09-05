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
package com.nttdocomo.opt.ui;

import com.nttdocomo.ui.Image;
import com.nttdocomo.ui.impls.ImageImpl;
import org.recompile.mobile.Mobile;

// This class is used by some DoJa jars (Lumines), but there's zero documentation on it
public abstract class Canvas2 extends com.nttdocomo.ui.Canvas
{ 
	
    // This constructor has an argument that, as of now, is unknown
    public Canvas2(int arg1) 
    { 
        super(); 

        Mobile.log(Mobile.LOG_INFO, Canvas2.class.getPackage().getName() + "." + Canvas2.class.getSimpleName() + ": " + "Create I-Appli Canvas2:" + width+", "+height);
    }
}