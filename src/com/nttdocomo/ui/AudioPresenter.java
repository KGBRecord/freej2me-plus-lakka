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
    
    private MediaListener listener;
    private MediaData mediaData = null;
    private MediaSound mediaSound = null;

    private int priority;

    protected AudioPresenter() {}

    public static AudioPresenter getAudioPresenter() { return new AudioPresenter();  }

    public static AudioPresenter getAudioPresenter(int param) 
    {
        AudioPresenter presenter = new AudioPresenter();
        
        return presenter;
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
        return new Audio3D();
    }

    public void play() { play(0); }

    public void play(int time) 
    {
        if (listener != null) 
        {
            listener.mediaAction(this, AUDIO_PLAYING, 0);
        }
    }

    public void stop() 
    {
        if (listener != null) 
        {
            listener.mediaAction(this, AUDIO_STOPPED, 0);
        }
    }

    public void restart() 
    {
        if (listener != null) 
        {
            listener.mediaAction(this, AUDIO_RESTARTED, 0);
        }
    }

    public void pause() 
    {
        if (listener != null) 
        {
            listener.mediaAction(this, AUDIO_PAUSED, 0);
        }
    }

    public int getCurrentTime() { return 0; }

    public int getTotalTime() { return 0; }

    public void setData(MediaData data) { this.mediaData = data; }

    public void setSound(MediaSound sound) { this.mediaSound = sound; }

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
                //if(mediaSound.getPlayer() != null) { ((PlatformPlayer.volumeControl)mediaSound.getPlayer().getControl("VolumeControl")).setLevel(value); }
                break;
            case LOOP_COUNT:
                mediaSound.getPlayer().setLoopCount(value);
                break;
            default:
                throw new IllegalArgumentException("Invalid attribute: " + attribute);
        }
    }

    public void setMediaListener(MediaListener listener) { this.listener = listener; }

    public void setSyncEvent(int channel, int key) 
    { 
        Mobile.log(Mobile.LOG_DEBUG, AudioPresenter.class.getPackage().getName() + "." + AudioPresenter.class.getSimpleName() + ": " + "setSyncEvent not implemented. channel: " + channel + " key:" + key);
    }

    public void unuse() { }

    public void dispose() { }
}