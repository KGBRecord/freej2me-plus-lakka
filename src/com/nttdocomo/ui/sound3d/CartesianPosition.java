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

public class CartesianPosition implements SoundPosition, com.nttdocomo.ui.Audio3DLocalization 
{
    private CartesianListener listener;
    private Vector3D position;
    private Vector3D velocity = null;
    private float coordinateFactor = 1.0f;

    public CartesianPosition(CartesianListener listener) 
    {
        if (listener == null) { throw new NullPointerException("Null listener received"); }
        this.listener = listener;
        this.position = new Vector3D(0.0f, 0.0f, 0.0f);
    }

    public CartesianPosition(CartesianListener listener, float coordinateFactor) 
    {
        if (listener == null) { throw new NullPointerException("Null listener received"); }

        if (Float.isInfinite(coordinateFactor) || Float.isNaN(coordinateFactor) || coordinateFactor <= 0.0f) { throw new IllegalArgumentException("Invalid coordinate received"); }
        this.listener = listener;
        this.coordinateFactor = coordinateFactor;
        this.position = new Vector3D(0.0f, 0.0f, 0.0f);
    }

    public static void setDefaultCoordinateFactor(float coordinateFactor) 
    {
        if (Float.isInfinite(coordinateFactor) || Float.isNaN(coordinateFactor) || coordinateFactor <= 0.0f) { throw new IllegalArgumentException("invalid coordinate received"); }
        // TODO
    }

    public float getCoordinateFactor() { return coordinateFactor; }

    public CartesianListener getListener() { return listener; }

    public Vector3D getPosition() { return position; }

    public void setPosition(Vector3D vector) 
    {
        if (vector == null) { throw new NullPointerException("Null vector received"); }

        this.position = vector; 
    }

    public void setVelocity(Vector3D vector) { this.velocity = vector; }

    public Vector3D getVelocity() { return velocity; }
}