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
package javax.microedition.m2g;

public class ScalableGraphics 
{

    public static final int RENDERING_QUALITY_LOW = 0;
    public static final int RENDERING_QUALITY_HIGH = 1;

    private Object target;
    private int renderingQuality = RENDERING_QUALITY_HIGH;
    private float transparency = 1.0f;

    public static ScalableGraphics createInstance() { return new ScalableGraphics(); }

    public void bindTarget(Object target) 
    {
        if (target == null) { throw new NullPointerException("Target cannot be null."); }
        if (this.target != null) { throw new IllegalStateException("Target is already bound."); }

        this.target = target;
    }

    public void releaseTarget() 
    {
        if (target == null) { throw new IllegalStateException("No target is bound."); }
        
        target = null;
    }

    public void render(int x, int y, ScalableImage image) 
    {
        if (image == null) { throw new NullPointerException("Image cannot be null."); }
        if (target == null) { throw new IllegalStateException("Target is not bound."); }
        // TODO: Rendering
    }

    public void setRenderingQuality(int mode) 
    {
        if (mode != RENDERING_QUALITY_LOW && mode != RENDERING_QUALITY_HIGH) { throw new IllegalArgumentException("Invalid rendering quality mode."); }
        
        this.renderingQuality = mode;
    }

    public void setTransparency(float alpha) 
    {
        if (alpha < 0.0f || alpha > 1.0f) { throw new IllegalArgumentException("Alpha must be between 0.0 and 1.0."); }
        
        this.transparency = alpha;
    }
}