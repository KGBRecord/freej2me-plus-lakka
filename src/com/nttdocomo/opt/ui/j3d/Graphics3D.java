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
package com.nttdocomo.opt.ui.j3d;

public abstract interface Graphics3D
{
  public abstract void setViewTrans(AffineTrans paramAffineTrans);
  
  public abstract void setScreenScale(int paramInt1, int paramInt2);
  
  public abstract void setScreenCenter(int paramInt1, int paramInt2);
  
  public abstract void drawFigure(Figure paramFigure);
  
  public abstract void setSphereTexture(Texture paramTexture);
  
  public abstract void enableLight(boolean paramBoolean);
  
  public abstract void enableSphereMap(boolean paramBoolean);
  
  public abstract void setAmbientLight(int paramInt);
  
  public abstract void setDirectionLight(Vector3D paramVector3D, int paramInt);
  
  public abstract void enableSemiTransparent(boolean paramBoolean);
  
  public abstract void setClipRect3D(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

  public abstract void setPerspective(int n1, int n2, int n3);

  public abstract void executeCommandList(int[] a);

  public abstract void renderPrimitives(PrimitiveArray array, int num);

  public abstract void flush();
}