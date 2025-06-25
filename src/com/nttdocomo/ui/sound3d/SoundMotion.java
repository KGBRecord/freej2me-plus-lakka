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
package com.nttdocomo.ui.sound3d;

import com.nttdocomo.ui.util3d.Vector3D;

import java.util.ArrayList;
import java.util.List;

public class SoundMotion implements com.nttdocomo.ui.Audio3DLocalization 
{
    private List<SoundPosition> positions;
    private List<Integer> times;

    public SoundMotion() 
    {
        positions = new ArrayList<SoundPosition>();
        times = new ArrayList<Integer>();
    }

    public void addPosition(int time, SoundPosition position) 
    {
        if (time < 0) { throw new IllegalArgumentException("Time cannot be negative."); }
        if (position == null) { throw new NullPointerException("Position cannot be null."); }
        if (!(position instanceof SoundPosition)) { throw new IllegalArgumentException("Invalid SoundPosition object."); }

        // Replace existing position if time is the same
        int index = times.indexOf(time);
        if (index != -1) { positions.set(index, position); } 
        else 
        {
            positions.add(position);
            times.add(time);
        }
    }

    public SoundPosition getPosition(int index) { return positions.get(index); }

    public int getTime(int index) { return times.get(index); }

    public void removePosition(int index) 
    {
        positions.remove(index);
        times.remove(index);
    }

    public int size() { return positions.size(); }
}