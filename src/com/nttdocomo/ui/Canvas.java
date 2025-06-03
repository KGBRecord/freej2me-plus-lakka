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
import org.recompile.mobile.MobilePlatform;

public abstract class Canvas extends Frame 
{
	
	private int barHeight;

    public Canvas() 
    { 
        super(); 

		barHeight = Font.getDefaultFont().getHeight();
        Mobile.log(Mobile.LOG_INFO, Canvas.class.getPackage().getName() + "." + Canvas.class.getSimpleName() + ": " + "Create I-Appli Canvas:" + width+", "+height);
    }

    public Graphics getGraphics() { return graphics; }

    public int getKeypadState() { return getKeypadState(0); }

	public int getKeypadState(int group) 
	{
		if (group < 0) { throw new IllegalArgumentException("group cannot be negative"); }
		
		return MobilePlatform.DoJaKeyState;
	}

    public abstract void paint(Graphics g);

    public void processEvent(int type, int param) { }

    public void repaint() { repaint(0, 0, getWidth(), getHeight()); }

	public void repaint(int x, int y, int width, int height)
	{
		if(!Mobile.compatImmediateRepaints) 
		{
			Mobile.getDisplay().postPaintRequest(() -> { repaintRequest(x, y, width, height); }); 
		}
		else { repaintRequest(x, y, width, height); }
	}

	public void repaintRequest(int x, int y, int width, int height) 
	{
		if(!isShown()) { return; }
		
		graphics.reset(x, y, width, height);

		try { paint(graphics); }
		catch (Exception e) 
		{
			Mobile.log(Mobile.LOG_ERROR, Canvas.class.getPackage().getName() + "." + Canvas.class.getSimpleName() + ": " + "Serious Exception hit in repaint(): " + e.getMessage());
			e.printStackTrace();
		}
		finally 
		{ 
			// Draw command bar whenever the canvas is not fullscreen and there are commands in the bar
			if (labelVisible) { paintCommandsBar(); }

			Mobile.getPlatform().flushGraphics(platformImage, x, y, width, labelVisible ? height+barHeight : height); // Extend the draw area if we have the commands bar visible
			Mobile.getPlatform().limitFps();
		}
	}

	private void paintCommandsBar() 
	{
		// labels should work independently of the current graphics translation, so translate back to 0,0 before any drawing and restore at the end
		int restoreX = graphics.getTranslateX(), restoreY = graphics.getTranslateY();
		int clipX = graphics.getClipX(), clipY = graphics.getClipY(), clipW = graphics.getClipWidth(), clipH = graphics.getClipHeight();

		graphics.setOrigin(0, 0);
		graphics.clearClip();

		graphics.setColor(Mobile.lcduiBGColor);
		graphics.fillRect(0, height-barHeight, width, barHeight);

		int textCenter;
		int xPos;

		graphics.setColor(Mobile.lcduiTextColor);
		graphics.drawLine(0, height-barHeight, width, height-barHeight);
		graphics.drawLine(width/2, height-barHeight, width/2, height);

		String label = softLabels[0] != null ? softLabels[0] : "";
		textCenter = (graphics.getGraphics2D().getFontMetrics().stringWidth(label))/2;
		xPos = (width / 4) - textCenter;
		graphics.drawString(label, xPos, height-barHeight, Graphics.LEFT);

		label = softLabels[1] != null ? softLabels[1] : "";
		textCenter = (graphics.getGraphics2D().getFontMetrics().stringWidth(label))/2;
		xPos = (3 * width / 4) - textCenter;
		graphics.drawString(softLabels[1], xPos, height-barHeight, Graphics.LEFT);

		graphics.setOrigin(restoreX, restoreY);
		graphics.setClip(clipX, clipY, clipW, clipH);
	}

}