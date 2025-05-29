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
package com.nttdocomo.ui.impls;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.nttdocomo.io.ConnectionException;
import com.nttdocomo.ui.UIException;
import javax.microedition.media.Manager;

import org.recompile.mobile.Mobile;

public class MediaSoundImpl implements com.nttdocomo.ui.MediaSound 
{
    private javax.microedition.media.Player player;

	public MediaSoundImpl(final String s) 
    { 
        try { player = Manager.createPlayer(s); }
        catch (Exception e) { Mobile.log(Mobile.LOG_WARNING, MediaSoundImpl.class.getPackage().getName() + "." + MediaSoundImpl.class.getSimpleName() + ": " + "Failed to create Player from "+ s + " :" + e.getMessage()); }
    }

	public MediaSoundImpl(final InputStream inputStream) 
    { 
        try { player = Manager.createPlayer(inputStream, ""); }
        catch (Exception e) { Mobile.log(Mobile.LOG_WARNING, MediaSoundImpl.class.getPackage().getName() + "." + MediaSoundImpl.class.getSimpleName() + ": " + "Failed to create Player from inputStream:" + e.getMessage()); }
    }

	public MediaSoundImpl(final byte[] array) 
    { 
        try { player = Manager.createPlayer(new ByteArrayInputStream(array), ""); }
        catch (Exception e) { Mobile.log(Mobile.LOG_WARNING, MediaSoundImpl.class.getPackage().getName() + "." + MediaSoundImpl.class.getSimpleName() + ": " + "Failed to create Player from byte array:" + e.getMessage()); }
    }

	public void use() throws ConnectionException, UIException { }

	public void unuse() { player.deallocate(); }

	public void dispose() { player.close(); player = null; }

    public javax.microedition.media.Player getPlayer() { return player; }
}