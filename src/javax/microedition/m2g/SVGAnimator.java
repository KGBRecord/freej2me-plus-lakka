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

public abstract class SVGAnimator 
{

    protected SVGAnimator() { }

    public static SVGAnimator createAnimator(SVGImage svgImage) 
    {
        if (svgImage == null) { throw new NullPointerException("SVGImage cannot be null."); }
        // TODO: Create new animator
        return null;
    }

    public static SVGAnimator createAnimator(SVGImage svgImage, String componentBaseClass) 
    {
        if (svgImage == null) { throw new NullPointerException("SVGImage cannot be null."); }
        if (componentBaseClass == null) { return createAnimator(svgImage); }
        // TODO: Create new animator
        return null;
    }

    public abstract Object getTargetComponent();

    public abstract float getTimeIncrement();

    public abstract void invokeAndWait(Runnable runnable) throws InterruptedException;

    public abstract void invokeLater(Runnable runnable);

    public abstract void pause();

    public abstract void play();

    public abstract void setSVGEventListener(SVGEventListener svgEventListener);

    public abstract void setTimeIncrement(float timeIncrement);

    public abstract void stop();
}