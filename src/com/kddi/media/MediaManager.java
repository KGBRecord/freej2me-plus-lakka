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
package com.kddi.media;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.recompile.mobile.Mobile;

// This class manages resources related to MediaPlayer.
// This is not written in documentation, and should be moved to other package.
public class MediaManager 
{
    
    static Map<MediaResource, byte[]> resourceMap = new HashMap<MediaResource, byte[]>();
    static Map<MediaResource, Set<MediaPlayerBox>> playerBoxMap = new HashMap<MediaResource, Set<MediaPlayerBox>>();

    public static byte[] getResource(MediaResource resource) { return resourceMap.get(resource); }

    public static void putResource(MediaResource resource, byte[] data) 
    {
        resourceMap.put(resource, data);
        playerBoxMap.put(resource, new HashSet<MediaPlayerBox>());
    }

    public static void removeResource(MediaResource resource) 
    {
        resourceMap.remove(resource);
        playerBoxMap.remove(resource);
    }

    public static MediaPlayerBox[] getMediaPlayerBoxes(MediaResource resource) 
    {
        return playerBoxMap.get(resource).toArray(new MediaPlayerBox[0]);
    }

    public static void linkMediaResourceToMediaPlayerBox(MediaResource resource, MediaPlayerBox box) 
    {
        playerBoxMap.putIfAbsent(resource, new HashSet<MediaPlayerBox>());
        Set<MediaPlayerBox> playerBoxSet = playerBoxMap.get(resource);
        playerBoxSet.add(box);
    }

    public static void unlinkMediaResource(MediaResource resource, MediaPlayerBox box) 
    {
        Set<MediaPlayerBox> playerBoxSet = playerBoxMap.get(resource);
        if (playerBoxSet == null) 
        {
            Mobile.log(Mobile.LOG_WARNING, MediaManager.class.getPackage().getName() + "." + MediaManager.class.getSimpleName() + ": " + "The requested resource to unlink is not registered");
            return;
        }

        if (playerBoxSet.contains(box)) { playerBoxSet.remove(box); } 
        else 
        {
            Mobile.log(Mobile.LOG_WARNING, MediaManager.class.getPackage().getName() + "." + MediaManager.class.getSimpleName() + ": " + "The requested resource to unlink is not linked to a mediaPlayerBox");
        }
    } 
}
