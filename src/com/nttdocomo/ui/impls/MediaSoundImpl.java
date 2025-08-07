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
import java.util.Arrays;

import com.nttdocomo.io.ConnectionException;
import com.nttdocomo.ui.UIException;
import com.nttdocomo.util.ScratchPadConnection;

import javax.microedition.media.Manager;

import org.recompile.mobile.Mobile;

public class MediaSoundImpl implements com.nttdocomo.ui.MediaSound 
{
    private javax.microedition.media.Player player;

	private boolean isSingleUse = false, disposed = false, isRedistributable = false;

	public MediaSoundImpl(final String s) 
    { 
        try 
		{ 
			if(s.contains("scratchpad:"))
			{
				// MLD chunks are laid out very similarly to SMAF, and since the scratchpad string for audio data doesn't have a length specified,
				// we can derive the data length from the "melo" header chunk's size, as it has the whole file's length in it
				InputStream audioData = new ScratchPadConnection(s).openInputStream();
				byte[] meloLength = new byte[4];
				int meloChunkSize;

				audioData.mark(8);
				audioData.skip(4); // Skip the "melo" header, we only care about the length here
				audioData.read(meloLength);
				audioData.reset();

				meloChunkSize = (meloLength[0] & 0xFF) << 24 | (meloLength[1] & 0xFF) << 16 | (meloLength[2] & 0xFF) << 8 | (meloLength[3] & 0xFF);
				
				byte[] meloData = new byte[meloChunkSize];
				audioData.read(meloData);

				player = Manager.createPlayer(new ByteArrayInputStream(meloData), "audio/x-mld");
			}
			else { player = Manager.createPlayer(s); }
		}
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

	public void use() throws ConnectionException, UIException { use(null, false); }

	public void use(com.nttdocomo.ui.MediaResource overwritten, boolean useOnce) throws ConnectionException, UIException 
	{ 
		if(overwritten == this) { throw new IllegalArgumentException("Cannot overwrite object area of this same object");}
		if(disposed || isSingleUse) { throw new UIException(UIException.ILLEGAL_STATE, "MediaResource has already been disposed"); }

		if(overwritten != null) 
		{
			// TODO: Do we actually need to overwrite any prior resource's memory area? We aren't under memory constraints
		}
		
		player.prefetch(); 
		isSingleUse = useOnce;
	}

	public void unuse() { player.deallocate(); }

	public void dispose() 
	{ 
		disposed = true;
		player.close(); 
		player = null; 
	}

	public boolean setRedistributable(boolean redistributable) { return isRedistributable = redistributable; }

	public boolean isRedistributable() { return isRedistributable; }

	public void setProperty(String key, String value) { /* TODO */ }

	public String getProperty(String key) { return ""; /* TODO */ }

    public javax.microedition.media.Player getPlayer() { return player; }
}