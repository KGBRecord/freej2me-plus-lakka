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

import com.nttdocomo.ui.impls.MediaSoundImpl;

import java.util.HashMap;
import java.util.Map;

import javax.microedition.media.Player;

import org.recompile.mobile.Mobile;
import org.recompile.mobile.PlatformPlayer;

public class AudioPresenter implements MediaPresenter 
{
    public static final int AUDIO_PLAYING = 1;
    public static final int AUDIO_STOPPED = 2;
    public static final int AUDIO_COMPLETE = 3;
    public static final int AUDIO_SYNC = 4;
    public static final int AUDIO_PAUSED = 5;
    public static final int AUDIO_RESTARTED = 6;
    public static final int AUDIO_LOOPED = 7;
    
    public static final int ATTR_SYNC_OFF = 0;
    public static final int ATTR_SYNC_ON = 1;

    public static final int SYNC_MODE = 2;
    public static final int TRANSPOSE_KEY = 3;
    public static final int SET_VOLUME = 4;
    public static final int CHANGE_TEMPO = 5;
    public static final int LOOP_COUNT = 6;
    
    public static final int PRIORITY = 1;
    public static final int NORM_PRIORITY = 5;
    public static final int MIN_PRIORITY = 1;
    public static final int MAX_PRIORITY = 10;

    public static final int MIN_OPTION_ATTR = 128;
    public static final int MAX_OPTION_ATTR = 255;

    protected static final int MIN_VENDOR_ATTR = 64;
    protected static final int MAX_VENDOR_ATTR = 127;

    protected static final int MIN_VENDOR_AUDIO_EVENT = 64;
    protected static final int MAX_VENDOR_AUDIO_EVENT = 127;
    
    private MediaData mediaData = null;
    private MediaSoundImpl mediaSound = null;
    private MediaListener listener = null;

    private int priority, loopCount = 1, volume = 100;

    private static Map<Integer, AudioPresenter> usedPorts =  new HashMap<Integer, AudioPresenter>();

    protected AudioPresenter() { }

    public static AudioPresenter getAudioPresenter() { return getAudioPresenter(0); }

    public static AudioPresenter getAudioPresenter(int port) 
    {
        AudioPresenter presenter = new AudioPresenter();

        // See if the port is already in use and override its currently placed AudioPresenter.
        if(!usedPorts.containsKey(port)) 
        {
            usedPorts.put(port, new AudioPresenter());
        }
        else 
        {
            usedPorts.replace(port, new AudioPresenter());
        }
        
        return usedPorts.get(port);
    }

    public static AudioTrackPresenter getAudioTrackPresenter() { return new AudioTrackPresenter(); }

    public MediaResource getMediaResource() 
    {
        if(mediaSound != null) { return mediaSound; }
        else if(mediaData != null) { return mediaData; }

        return null;
    }

    public Audio3D getAudio3D() 
    {
        Mobile.log(Mobile.LOG_WARNING, AudioPresenter.class.getPackage().getName() + "." + AudioPresenter.class.getSimpleName() + ": " + "getAudio3D (not implemented)");
        return new Audio3D();
    }

    public void play() { play(0); }

    public void play(int time) 
    {
        if((mediaSound == null && mediaData == null) ||
            (mediaSound != null && mediaSound.getPlayer().getState() < Player.REALIZED) // ||
            /* (mediaData != null && mediaSound.getPlayer.getState() < Player.REALIZED)  TODO*/
        )
        { throw new UIException(UIException.ILLEGAL_STATE, "Player is in an invalid state"); }
        if(time < 0) { throw new IllegalArgumentException("Invalid value received for time");}

        if(mediaSound.getPlayer().getState() >= Player.REALIZED) { mediaSound.getPlayer().setMediaTime(time); }
        if(mediaSound.getPlayer().getState() >= Player.REALIZED && mediaSound.getPlayer().getState() < Player.STARTED) { mediaSound.getPlayer().setLoopCount(loopCount); }
        ((PlatformPlayer)mediaSound.getPlayer()).setDoJaListener(listener, this);
        if(mediaSound.getPlayer().getState() >= Player.REALIZED) 
        { 
            ((PlatformPlayer.volumeControl)mediaSound.getPlayer().getControl("VolumeControl")).setLevel(volume);
        }
        mediaSound.getPlayer().start();
    }

    public void stop() 
    {
        if((mediaSound == null && mediaData == null) ||
            (mediaSound != null && mediaSound.getPlayer().getState() < Player.REALIZED) // ||
            /* (mediaData != null && mediaSound.getPlayer.getState() < Player.REALIZED)  TODO*/
        )
        { throw new UIException(UIException.ILLEGAL_STATE, "Player is in an invalid state"); }

        mediaSound.getPlayer().stop();
        if(mediaSound.getPlayer().getState() >= Player.REALIZED) { mediaSound.getPlayer().setMediaTime(0); }
    }

    // Despite the name, this is actually a resume call
    public void restart() 
    {
        if((mediaSound == null && mediaData == null) ||
            (mediaSound != null && mediaSound.getPlayer().getState() < Player.REALIZED) // ||
            /* (mediaData != null && mediaSound.getPlayer.getState() < Player.REALIZED)  TODO*/
        )
        { throw new UIException(UIException.ILLEGAL_STATE, "Player is in an invalid state"); }

        ((PlatformPlayer)mediaSound.getPlayer()).setDoJaListener(listener, this);
        if(mediaSound.getPlayer().getState() >= Player.REALIZED) 
        { 
            ((PlatformPlayer.volumeControl)mediaSound.getPlayer().getControl("VolumeControl")).setLevel(volume);
        }
        mediaSound.getPlayer().start();
    }

    public void pause() 
    { 
        if((mediaSound == null && mediaData == null) ||
            (mediaSound != null && mediaSound.getPlayer().getState() < Player.REALIZED) // ||
            /* (mediaData != null && mediaSound.getPlayer.getState() < Player.REALIZED)  TODO*/
        )
        { throw new UIException(UIException.ILLEGAL_STATE, "Player is in an invalid state"); }

        mediaSound.getPlayer().stop(); 
    }

    public int getCurrentTime() { return mediaSound.getPlayer() == null || mediaSound.getPlayer().getState() == Player.CLOSED ? 0 : (int) (mediaSound.getPlayer().getMediaTime() / 1000); }

    public int getTotalTime() { return mediaSound.getPlayer() == null || mediaSound.getPlayer().getState() == Player.CLOSED ? 0 : (int) (mediaSound.getPlayer().getDuration() / 1000); }

    public void setData(MediaData data) 
    { 
        Mobile.log(Mobile.LOG_WARNING, AudioPresenter.class.getPackage().getName() + "." + AudioPresenter.class.getSimpleName() + ": " + "setData called (not implemented)");
        this.mediaData = data; 
    }

    public void setSound(MediaSound sound) 
    { 
        this.mediaSound = (MediaSoundImpl) sound; 
    }

    public void setAttribute(int attribute, int value) 
    {
        switch (attribute) 
        {
            case PRIORITY:
                if(value < MIN_PRIORITY || value > MAX_PRIORITY) { throw new IllegalArgumentException("Invalid priority value: " + value); }
                Mobile.log(Mobile.LOG_WARNING, AudioPresenter.class.getPackage().getName() + "." + AudioPresenter.class.getSimpleName() + ": " + "setPriority (unused):" + value);
                this.priority = value;
                break;
            case SYNC_MODE:
                if(value != ATTR_SYNC_OFF && value != ATTR_SYNC_ON) { throw new IllegalArgumentException("Invalid sync mode value: " + value); }
                Mobile.log(Mobile.LOG_WARNING, AudioPresenter.class.getPackage().getName() + "." + AudioPresenter.class.getSimpleName() + ": " + "setSyncMode (not implemented):" + (value == 1));
                break;
            case TRANSPOSE_KEY:
                Mobile.log(Mobile.LOG_WARNING, AudioPresenter.class.getPackage().getName() + "." + AudioPresenter.class.getSimpleName() + ": " + "transposeKey (not implemented):" + value);
                //setTransposeKey(value); // TODO
                break;
            case CHANGE_TEMPO:
                Mobile.log(Mobile.LOG_WARNING, AudioPresenter.class.getPackage().getName() + "." + AudioPresenter.class.getSimpleName() + ": " + "changeTempo (not implemented):" + value);
                //setChangeTempo(value); // TODO
                break;
            case SET_VOLUME:
                Mobile.log(Mobile.LOG_DEBUG, AudioPresenter.class.getPackage().getName() + "." + AudioPresenter.class.getSimpleName() + ": " + "setVolume:" + value);
                volume = value;
                break;
            case LOOP_COUNT:
                Mobile.log(Mobile.LOG_DEBUG, AudioPresenter.class.getPackage().getName() + "." + AudioPresenter.class.getSimpleName() + ": " + "setLoopCount:" + value);
                loopCount = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid attribute: " + attribute);
        }
    }

    public void setMediaListener(MediaListener listener) { this.listener = listener; }

    public void setSyncEvent(int channel, int key) 
    { 
        Mobile.log(Mobile.LOG_WARNING, AudioPresenter.class.getPackage().getName() + "." + AudioPresenter.class.getSimpleName() + ": " + "setSyncEvent not implemented. channel: " + channel + " key:" + key);
    }
}