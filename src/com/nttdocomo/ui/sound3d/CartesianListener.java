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
import com.nttdocomo.ui.util3d.Transform;

public class CartesianListener 
{
    private Vector3D position;
    private Vector3D look;
    private Vector3D up;
    private Vector3D velocity = null;

    public CartesianListener() 
    {
        this.position = new Vector3D(0.0f, 0.0f, 0.0f);
        this.look = new Vector3D(0.0f, 0.0f, 1.0f);
        this.up = new Vector3D(0.0f, -1.0f, 0.0f);
    }

    public void setLookAt(Vector3D position, Vector3D look, Vector3D up) 
    {
        if (position == null || look == null || up == null) { throw new NullPointerException("null vector received"); }

        if (up.getX() == 0 && up.getY() == 0 && up.getZ() == 0) { throw new IllegalArgumentException("Up vector cannot be zero."); }

        if (position.getX() == look.getX() &&
            position.getY() == look.getY() &&
            position.getZ() == look.getZ()) 
        {
            throw new IllegalArgumentException("Position and look vectors must not be equal.");
        }
        Vector3D direction = new Vector3D(look);
        direction.add(-position.getX(), -position.getY(), -position.getZ());
        if (direction.getX() == 0 && direction.getY() == 0 && direction.getZ() == 0) 
        {
            throw new IllegalArgumentException("Look direction and up vector are parallel.");
        }
        this.position = position;
        this.look = look;
        this.up = up;
    }

    public void setTransform(Transform transform) 
    {
        if (transform == null) { throw new NullPointerException("null transform received"); }
        // TODO: Apply transform
    }

    public void setVelocity(Vector3D vector) { this.velocity = vector; }

    public Vector3D getVelocity() { return velocity; }
}