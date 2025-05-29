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

public class PolarPosition implements SoundPosition, com.nttdocomo.ui.Audio3DLocalization 
{
    private float coordinateFactor = 1.0f;
    private float distance = 0.0f;
    private float direction = 0.0f;
    private float elevation = 0.0f;
    private Vector3D velocity = null;

    public PolarPosition() { }

    public PolarPosition(float coordinateFactor) 
    {
        setDefaultCoordinateFactor(coordinateFactor);
    }

    public static void setDefaultCoordinateFactor(float coordinateFactor) 
    {
        if (Float.isInfinite(coordinateFactor) || Float.isNaN(coordinateFactor) || coordinateFactor <= 0.0f) { throw new IllegalArgumentException(); }
        // TODO
    }

    public float getCoordinateFactor() { return coordinateFactor; }

    public void setDistance(float distance) 
    {
        if (Float.isNaN(distance) || distance < 0) { throw new IllegalArgumentException("Invalid distance value"); }
        this.distance = distance * coordinateFactor;
    }

    public float getDistance() { return distance; }

    public void setDirection(float direction) 
    {
        if (Float.isInfinite(direction) || Float.isNaN(direction)) { throw new IllegalArgumentException("Invalid direction value"); }

        this.direction = direction;
    }

    public float getDirection() { return direction; }

    public void setElevation(float elevation) 
    {
        if (Float.isInfinite(elevation) || Float.isNaN(elevation)) { throw new IllegalArgumentException("Invalid elevation value"); }

        this.elevation = elevation;
    }

    public float getElevation() { return elevation; }

    public void setPosition(Vector3D vector) 
    {
        if (vector == null) { throw new NullPointerException("Received a null vector"); }

        setDistance(vector.getX());
        setDirection(vector.getY());
        setElevation(vector.getZ());
    }

    public void setVelocity(Vector3D vector) { this.velocity = vector; }

    public Vector3D getVelocity() { return velocity; }
}