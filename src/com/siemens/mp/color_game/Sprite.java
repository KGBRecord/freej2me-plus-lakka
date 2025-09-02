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
package com.siemens.mp.color_game;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.recompile.mobile.Mobile;

public class Sprite extends Layer
{
	public static final int TRANS_NONE = 0;
	public static final int TRANS_ROT90 = 5;
	public static final int TRANS_ROT180 = 3;
	public static final int TRANS_ROT270 = 6;
	public static final int TRANS_MIRROR = 2;
	public static final int TRANS_MIRROR_ROT90 = 7;
	public static final int TRANS_MIRROR_ROT180 = 1;
	public static final int TRANS_MIRROR_ROT270 = 4;
	private static final int INVERTED_AXES = 0x4;
	private static final int X_FLIP = 0x2;
	private static final int Y_FLIP = 0x1;
	private static final int ALPHA_BITMASK = 0xff000000;
	private static final int FULLY_OPAQUE_ALPHA = 0xff000000;

	private Image sourceImage;
	private int numberFrames;
	private int[] frameCoordsX;
	private int[] frameCoordsY;
	private int srcFrameWidth;
	private int srcFrameHeight;
	private int[] sequence;
	private int sequenceIndex;
	private boolean customSequenceDefined;
	private int dRefX;
	private int dRefY;
	private int collisionRectX;
	private int collisionRectY;
	private int collisionRectWidth;
	private int collisionRectHeight;
	private int currentTransform;
	private int transformedCollisionRectX;
	private int transformedCollisionRectY;
	private int transformedCollisionRectWidth;
	private int transformedCollisionRectHeight;


	public Sprite(Image image)
	{
		super(image.getWidth(), image.getHeight());

		initializeFrames(image, image.getWidth(), image.getHeight(), false);
		initCollisionRectBounds();
		setTransform(TRANS_NONE);
	}

	public Sprite(Image image, int frameWidth, int frameHeight)
	{
		super(frameWidth, frameHeight);

		if ((frameWidth < 1 || frameHeight < 1) || ((image.getWidth() % frameWidth) != 0) || ((image.getHeight() % frameHeight) != 0))
			{ throw new IllegalArgumentException(); }

		initializeFrames(image, frameWidth, frameHeight, false);
		initCollisionRectBounds();
		setTransform(TRANS_NONE);
	}

	public Sprite(Sprite s)
	{
		super(s != null ? s.getWidth() : 0, s != null ? s.getHeight() : 0);

		if (s == null) { throw new NullPointerException(); }

		this.sourceImage = Image.createImage(s.sourceImage);
		this.numberFrames = s.numberFrames;
		this.frameCoordsX = new int[this.numberFrames];
		this.frameCoordsY = new int[this.numberFrames];

		System.arraycopy(s.frameCoordsX, 0, this.frameCoordsX, 0, s.getRawFrameCount());
		System.arraycopy(s.frameCoordsY, 0, this.frameCoordsY, 0, s.getRawFrameCount());

		this.x = s.getX();
		this.y = s.getY();

		this.dRefX = s.dRefX;
		this.dRefY = s.dRefY;

		this.collisionRectX = s.collisionRectX;
		this.collisionRectY = s.collisionRectY;
		this.collisionRectWidth = s.collisionRectWidth;
		this.collisionRectHeight = s.collisionRectHeight;

		this.srcFrameWidth = s.srcFrameWidth;
		this.srcFrameHeight = s.srcFrameHeight;

		setTransform(s.currentTransform);
		this.setVisible(s.isVisible());

		this.sequence = new int[s.getFrameSequenceLength()];
		this.setFrameSequence(s.sequence);
		this.setFrame(s.getFrame());

		this.setRefPixelPosition(s.getRefPixelX(), s.getRefPixelY());
	}

	public void defineReferencePixel(int x, int y) 
	{
		dRefX = x;
		dRefY = y;
	}

	public void setRefPixelPosition(int x, int y) 
	{
		this.x = x - getTransformedPos(dRefX, dRefY, this.currentTransform, true);
		this.y = y - getTransformedPos(dRefX, dRefY, this.currentTransform, false);
	}

	public int getRefPixelX() { return (this.x + getTransformedPos(dRefX, dRefY, this.currentTransform, true)); }

	public int getRefPixelY() { return (this.y + getTransformedPos(dRefX, dRefY, this.currentTransform, false)); }

	public void setFrame(int sequenceIndex)
	{
		if (sequenceIndex < 0 || sequenceIndex >= sequence.length) { throw new IndexOutOfBoundsException(); }
		this.sequenceIndex = sequenceIndex;
	}

	public final int getFrame() { return sequenceIndex; }

	public int getRawFrameCount() { return numberFrames; }

	public int getFrameSequenceLength() { return sequence.length; }

	public void nextFrame() { sequenceIndex = (sequenceIndex + 1) % sequence.length; }

	public void prevFrame()
	{
		if (sequenceIndex == 0) { sequenceIndex = sequence.length - 1; }
		else { sequenceIndex--; }
	}

	@Override
	public final void paint(Graphics g)
	{
		if (g == null) { throw new NullPointerException(); }

		if (visible)
		{
			g.drawRegion(sourceImage,
					frameCoordsX[sequence[sequenceIndex]],
					frameCoordsY[sequence[sequenceIndex]],
					srcFrameWidth,
					srcFrameHeight,
					currentTransform,
					this.x,
					this.y,
					Graphics.TOP | Graphics.LEFT);
		}

	}

	public void setFrameSequence(int sequence[])
	{
		if (sequence == null)
		{
			sequenceIndex = 0;
			customSequenceDefined = false;
			this.sequence = new int[numberFrames];
			for (int i = 0; i < numberFrames; i++) { this.sequence[i] = i; }
			return;
		}

		if (sequence.length < 1) { throw new IllegalArgumentException(); }

		for (int aSequence : sequence) { if (aSequence < 0 || aSequence >= numberFrames) { throw new ArrayIndexOutOfBoundsException(); } }
		customSequenceDefined = true;
		this.sequence = new int[sequence.length];
		System.arraycopy(sequence, 0, this.sequence, 0, sequence.length);
		sequenceIndex = 0;
	}

	public void setImage(Image img, int frameWidth, int frameHeight)
	{
		// if image is null image.getWidth() will throw NullPointerException
		if ((frameWidth < 1 || frameHeight < 1) || ((img.getWidth() % frameWidth) != 0) || ((img.getHeight() % frameHeight) != 0))
			{ throw new IllegalArgumentException();}

		final int noOfFrames = (img.getWidth() / frameWidth) * (img.getHeight() / frameHeight);

		boolean maintainCurFrame = true;

		if (noOfFrames < numberFrames)
		{
			maintainCurFrame = false;
			customSequenceDefined = false;
		}

		if (!((srcFrameWidth == frameWidth) && (srcFrameHeight == frameHeight)))
		{

			int oldX = this.x + getTransformedPos(dRefX, dRefY, this.currentTransform, true);
			int oldY = this.y + getTransformedPos(dRefX, dRefY, this.currentTransform, false);

			setWidth(frameWidth);
			setHeight(frameHeight);

			initializeFrames(img, frameWidth, frameHeight, maintainCurFrame);
			initCollisionRectBounds();

			this.x = oldX - getTransformedPos(dRefX, dRefY, this.currentTransform, true);
			this.y = oldY - getTransformedPos(dRefX, dRefY, this.currentTransform, false);
			computeTransformedBounds(this.currentTransform);

		}
		else { initializeFrames(img, frameWidth, frameHeight, maintainCurFrame); }
	}

	public void defineCollisionRectangle(int x, int y, int width, int height)
	{
		if (width < 0 || height < 0) { throw new IllegalArgumentException(); }

		collisionRectX = x;
		collisionRectY = y;
		collisionRectWidth = width;
		collisionRectHeight = height;

		setTransform(currentTransform);
	}

	public void setCollisionRectangle(int x, int y, int width, int height) { defineCollisionRectangle(x, y, width, height); }

	public void setTransform(int transform)
	{
		this.x = this.x + getTransformedPos(dRefX, dRefY, this.currentTransform, true) - getTransformedPos(dRefX, dRefY, transform, true);
		this.y = this.y + getTransformedPos(dRefX, dRefY, this.currentTransform, false) - getTransformedPos(dRefX, dRefY, transform, false);

		computeTransformedBounds(transform);
		currentTransform = transform;
	}

	public final boolean collidesWith(Sprite s, boolean pixelLevel) 
	{
		if (!(s.visible && this.visible)) { return false; }

		int otherLeft = s.x + s.collisionRectX;
		int otherTop = s.y + s.collisionRectY;
		int otherRight = otherLeft + s.collisionRectWidth;
		int otherBottom = otherTop + s.collisionRectHeight;

		int left = this.x + this.collisionRectX;
		int top = this.y + this.collisionRectY;
		int right = left + this.collisionRectWidth;
		int bottom = top + this.collisionRectHeight;

		if (intersectRect(otherLeft, otherTop, otherRight, otherBottom, left, top, right, bottom)) 
		{
			if (pixelLevel) 
			{
				if (this.collisionRectX < 0) { left = this.x; }
				if (this.collisionRectY < 0) { top = this.y; }
				if ((this.collisionRectX + this.collisionRectWidth) > this.width) { right = this.x + this.width; }
				if ((this.collisionRectY + this.collisionRectHeight) > this.height) { bottom = this.y + this.height; }

				if (s.collisionRectX < 0) { otherLeft = s.x; }
				if (s.collisionRectY < 0) { otherTop = s.y; }
				if ((s.collisionRectX + s.collisionRectWidth) > s.width) { otherRight = s.x + s.width; }
				if ((s.collisionRectY + s.collisionRectHeight) > s.height) { otherBottom = s.y + s.height; }

				if (!intersectRect(otherLeft, otherTop, otherRight, otherBottom, left, top, right, bottom)) { return false; }

				int intersectLeft = (left < otherLeft) ? otherLeft : left;
				int intersectTop = (top < otherTop) ? otherTop : top;
				int intersectRight = (right < otherRight) ? right : otherRight;
				int intersectBottom = (bottom < otherBottom) ? bottom : otherBottom;
				int intersectWidth = Math.abs(intersectRight - intersectLeft);
				int intersectHeight = Math.abs(intersectBottom - intersectTop);

				int thisImageXOffset = getImageTopLeft(intersectLeft, intersectTop, intersectRight, intersectBottom, true);
				int thisImageYOffset = getImageTopLeft(intersectLeft, intersectTop, intersectRight, intersectBottom, false);
				int otherImageXOffset = s.getImageTopLeft(intersectLeft, intersectTop, intersectRight, intersectBottom, true);
				int otherImageYOffset = s.getImageTopLeft(intersectLeft, intersectTop, intersectRight, intersectBottom, false);

				return doPixelCollision(thisImageXOffset, thisImageYOffset,
						otherImageXOffset, otherImageYOffset,
						this.sourceImage,
						this.currentTransform,
						s.sourceImage,
						s.currentTransform,
						intersectWidth, intersectHeight);
			} 
			else { return true; }
		}
		return false;
	}

	public final boolean collidesWith(TiledLayer t, boolean pixelLevel) 
	{
		if (!(t.visible && this.visible)) { return false; }
		int tLx1 = t.x;
		int tLy1 = t.y;
		int tLx2 = tLx1 + t.width;
		int tLy2 = tLy1 + t.height;

		int sx1 = this.x + this.collisionRectX;
		int sy1 = this.y + this.collisionRectY;
		int sx2 = sx1 + this.collisionRectWidth;
		int sy2 = sy1 + this.collisionRectHeight;

		if (!intersectRect(tLx1, tLy1, tLx2, tLy2, sx1, sy1, sx2, sy2)) { return false; }

		int tW = t.getCellWidth();
		int tH = t.getCellHeight();

		int tNumCols = t.getColumns();
		int tNumRows = t.getRows();

		int startCol = (sx1 <= tLx1) ? 0 : (sx1 - tLx1) / tW;
		int startRow = (sy1 <= tLy1) ? 0 : (sy1 - tLy1) / tH;
		int endCol = (sx2 < tLx2) ? ((sx2 - 1 - tLx1) / tW) : tNumCols - 1;
		int endRow = (sy2 < tLy2) ? ((sy2 - 1 - tLy1) / tH) : tNumRows - 1;

		if (!pixelLevel) 
		{
			for (int row = startRow; row <= endRow; row++) 
			{
				for (int col = startCol; col <= endCol; col++) 
				{
					if (t.getCell(col, row) != 0) { return true; }
				}
			}
			return false;
		} 
		else 
		{
			if (this.collisionRectX < 0) { sx1 = this.x; }
			if (this.collisionRectY < 0) { sy1 = this.y; }
			if ((this.collisionRectX + this.collisionRectWidth) > this.width) { sx2 = this.x + this.width; }
			if ((this.collisionRectY + this.collisionRectHeight) > this.height) { sy2 = this.y + this.height; }

			if (!intersectRect(tLx1, tLy1, tLx2, tLy2, sx1, sy1, sx2, sy2)) { return false; }

			startCol = (sx1 <= tLx1) ? 0 : (sx1 - tLx1) / tW;
			startRow = (sy1 <= tLy1) ? 0 : (sy1 - tLy1) / tH;
			endCol = (sx2 < tLx2) ? ((sx2 - 1 - tLx1) / tW) : tNumCols - 1;
			endRow = (sy2 < tLy2) ? ((sy2 - 1 - tLy1) / tH) : tNumRows - 1;

			int cellTop = startRow * tH + tLy1;
			int cellBottom = cellTop + tH;

			int tileIndex;

			for (int row = startRow; row <= endRow; row++, cellTop += tH, cellBottom += tH) 
			{
				int cellLeft = startCol * tW + tLx1;
				int cellRight = cellLeft + tW;

				for (int col = startCol; col <= endCol; col++, cellLeft += tW, cellRight += tW) 
				{
					tileIndex = t.getCell(col, row);

					if (tileIndex != 0) 
					{
						int intersectLeft = (sx1 < cellLeft) ? cellLeft : sx1;
						int intersectTop = (sy1 < cellTop) ? cellTop : sy1;
						int intersectRight = (sx2 < cellRight) ? sx2 : cellRight;
						int intersectBottom = (sy2 < cellBottom) ? sy2 : cellBottom;

						if (intersectLeft > intersectRight) 
						{
							int temp = intersectRight;
							intersectRight = intersectLeft;
							intersectLeft = temp;
						}

						if (intersectTop > intersectBottom) 
						{
							int temp = intersectBottom;
							intersectBottom = intersectTop;
							intersectTop = temp;
						}

						int intersectWidth = intersectRight - intersectLeft;
						int intersectHeight = intersectBottom - intersectTop;

						int image1XOffset = getImageTopLeft(intersectLeft, intersectTop, intersectRight, intersectBottom, true);
						int image1YOffset = getImageTopLeft(intersectLeft, intersectTop, intersectRight, intersectBottom, false);
						int image2XOffset = t.tileSetX[tileIndex] + (intersectLeft - cellLeft);
						int image2YOffset = t.tileSetY[tileIndex] + (intersectTop - cellTop);

						if (doPixelCollision(image1XOffset,
								image1YOffset,
								image2XOffset,
								image2YOffset,
								this.sourceImage,
								this.currentTransform,
								t.image,
								TRANS_NONE,
								intersectWidth, intersectHeight)) { return true; }
					}
				}
			}
			return false;
		}
	}

	public final boolean collidesWith(Image image, int x, int y, boolean pixelLevel) 
	{
		if (!(visible)) { return false; }

		int otherLeft = x;
		int otherTop = y;
		int otherRight = x + image.getWidth();
		int otherBottom = y + image.getHeight();

		int left = x + collisionRectX;
		int top = y + collisionRectY;
		int right = left + collisionRectWidth;
		int bottom = top + collisionRectHeight;

		if (intersectRect(otherLeft, otherTop, otherRight, otherBottom, left, top, right, bottom)) 
		{
			if (pixelLevel) 
			{
				if (this.collisionRectX < 0) { left = this.x; }
				if (this.collisionRectY < 0) { top = this.y; }
				if ((this.collisionRectX + this.collisionRectWidth) > this.width) { right = this.x + this.width; }
				if ((this.collisionRectY + this.collisionRectHeight) > this.height) { bottom = this.y + this.height; }

				if (!intersectRect(otherLeft, otherTop, otherRight, otherBottom, left, top, right, bottom)) { return false; }

				int intersectLeft = (left < otherLeft) ? otherLeft : left;
				int intersectTop = (top < otherTop) ? otherTop : top;

				int intersectRight = (right < otherRight) ? right : otherRight;
				int intersectBottom = (bottom < otherBottom) ? bottom : otherBottom;

				int intersectWidth = Math.abs(intersectRight - intersectLeft);
				int intersectHeight = Math.abs(intersectBottom - intersectTop);

				int thisImageXOffset = getImageTopLeft(intersectLeft,
						intersectTop,
						intersectRight,
						intersectBottom, true);

				int thisImageYOffset = getImageTopLeft(intersectLeft,
						intersectTop,
						intersectRight,
						intersectBottom, false);

				int otherImageXOffset = intersectLeft - x;
				int otherImageYOffset = intersectTop - y;

				return doPixelCollision(thisImageXOffset, thisImageYOffset,
						otherImageXOffset, otherImageYOffset,
						this.sourceImage,
						this.currentTransform,
						image,
						Sprite.TRANS_NONE,
						intersectWidth, intersectHeight);

			}
			else { return true; }
		}
		return false;
	}

	private void initializeFrames(Image image, int fWidth, int fHeight, boolean maintainCurFrame)
	{
		final int imageW = image.getWidth();
		final int imageH = image.getHeight();

		final int numHorizontalFrames = imageW / fWidth;
		final int numVerticalFrames = imageH / fHeight;

		sourceImage = image;

		srcFrameWidth = fWidth;
		srcFrameHeight = fHeight;

		numberFrames = numHorizontalFrames * numVerticalFrames;

		frameCoordsX = new int[numberFrames];
		frameCoordsY = new int[numberFrames];

		if (!maintainCurFrame) { sequenceIndex = 0; }
		if (!customSequenceDefined) { sequence = new int[numberFrames]; }

		int currentFrame = 0;

		for (int yy = 0; yy < imageH; yy += fHeight)
		{
			for (int xx = 0; xx < imageW; xx += fWidth)
			{

				frameCoordsX[currentFrame] = xx;
				frameCoordsY[currentFrame] = yy;

				if (!customSequenceDefined) { sequence[currentFrame] = currentFrame; }
				currentFrame++;
			}
		}
	}

	private void initCollisionRectBounds()
	{
		collisionRectX = 0;
		collisionRectY = 0;
		collisionRectWidth = this.width;
		collisionRectHeight = this.height;
	}

	private boolean intersectRect(int r1x1, int r1y1, int r1x2, int r1y2, int r2x1, int r2y1, int r2x2, int r2y2)
	{
		if (r2x1 >= r1x2 || r2y1 >= r1y2 || r2x2 <= r1x1 || r2y2 <= r1y1) { return false; }
		else { return true; }
	}

	private static boolean doPixelCollision(int image1XOffset, int image1YOffset, int image2XOffset, int image2YOffset,
		Image image1, int transform1, Image image2, int transform2, int width, int height) 
	{
		Mobile.log(Mobile.LOG_WARNING, Sprite.class.getPackage().getName() + "." + Sprite.class.getSimpleName() + ": " + "TiledLayer: Per-Pixel Collision Check!");

		final int[] argbData1 = getARGBData(image1, image1XOffset, image1YOffset, transform1, width, height);
		final int[] argbData2 = getARGBData(image2, image2XOffset, image2YOffset, transform2, width, height);

		return checkPixelCollision(argbData1, argbData2, width, height);
	}

	private static int[] getARGBData(Image image, int xOffset, int yOffset, int transform, int width, int height) 
	{
		int startY, xIncr, yIncr, numPixels = height * width;
		int[] argbData = new int[numPixels];

		if (0x0 != (transform & INVERTED_AXES)) 
		{
			if (0x0 != (transform & Y_FLIP)) 
			{
				xIncr = -(height);
				startY = numPixels - height;
			} 
			else 
			{
				xIncr = height;
				startY = 0;
			}

			if (0x0 != (transform & X_FLIP)) {
				yIncr = -1;
				startY += (height - 1);
			} else { yIncr = +1; }

			image.getRGB(argbData, 0, height, xOffset, yOffset, height, width);
		} 
		else 
		{
			if (0x0 != (transform & Y_FLIP))
			{
				startY = numPixels - width;
				yIncr = -(width);
			}
			else
			{
				startY = 0;
				yIncr = width;
			}

			if (0x0 != (transform & X_FLIP))
			{
				xIncr = -1;
				startY += (width - 1);
			}
			else { xIncr = +1; }

			image.getRGB(argbData, 0, width, xOffset, yOffset, width, height);
		}

		return argbData;
	}

	private static boolean checkPixelCollision(int[] argbData1, int[] argbData2, int width, int height) 
	{
		for (int i = 0; i < width * height; i++) 
		{
			if (((argbData1[i] & ALPHA_BITMASK) == FULLY_OPAQUE_ALPHA) && ((argbData2[i] & ALPHA_BITMASK) == FULLY_OPAQUE_ALPHA)) { return true; }
		}

		return false;
	}

	private int getImageTopLeft(int x1, int y1, int x2, int y2, boolean isX) 
	{
		int ret = 0;
	
		switch (this.currentTransform)
		{
			case TRANS_NONE:
			case TRANS_MIRROR_ROT180:
				ret = isX ? x1 - this.x : y1 - this.y;
				break;
			case TRANS_MIRROR:
			case TRANS_ROT180:
				ret = isX ? (this.x + this.width) - x2 : (this.y + this.height) - y2;
				break;
			case TRANS_ROT90:
			case TRANS_MIRROR_ROT270:
				ret = isX ? y1 - this.y : (this.x + this.width) - x2;
				break;
			case TRANS_ROT270:
			case TRANS_MIRROR_ROT90:
				ret = isX ? (this.y + this.height) - y2 : x1 - this.x;
				break;
			default:
				return ret;
		}
	
		ret += isX ? frameCoordsX[sequence[sequenceIndex]] : frameCoordsY[sequence[sequenceIndex]];
	
		return ret;
	}

	private void computeTransformedBounds(int transform) 
	{
		switch (transform) 
		{
			case TRANS_NONE:
				transformedCollisionRectX = collisionRectX;
				transformedCollisionRectY = collisionRectY;
				break;
	
			case TRANS_MIRROR:
				transformedCollisionRectX = srcFrameWidth - (collisionRectX + collisionRectWidth);
				transformedCollisionRectY = collisionRectY;
				break;
	
			case TRANS_MIRROR_ROT180:
				transformedCollisionRectX = collisionRectX;
				transformedCollisionRectY = srcFrameHeight - (collisionRectY + collisionRectHeight);
				break;
	
			case TRANS_ROT90:
				transformedCollisionRectX = srcFrameHeight - (collisionRectHeight + collisionRectY);
				transformedCollisionRectY = collisionRectX;
				break;
	
			case TRANS_ROT180:
				transformedCollisionRectX = srcFrameWidth - (collisionRectWidth + collisionRectX);
				transformedCollisionRectY = srcFrameHeight - (collisionRectHeight + collisionRectY);
				break;
	
			case TRANS_ROT270:
				transformedCollisionRectX = collisionRectY;
				transformedCollisionRectY = srcFrameWidth - (collisionRectWidth + collisionRectX);
				break;
	
			case TRANS_MIRROR_ROT90:
				transformedCollisionRectX = srcFrameHeight - (collisionRectHeight + collisionRectY);
				transformedCollisionRectY = srcFrameWidth - (collisionRectWidth + collisionRectX);
				break;
	
			case TRANS_MIRROR_ROT270:
				transformedCollisionRectX = collisionRectY;
				transformedCollisionRectY = collisionRectX;
				break;
	
			default:
				throw new IllegalArgumentException();
		}
	
		transformedCollisionRectWidth = (transform % 2 == 0) ? collisionRectWidth : collisionRectHeight;
		transformedCollisionRectHeight = (transform % 2 == 0) ? collisionRectHeight : collisionRectWidth;
	
		this.width = (transform % 2 == 0) ? srcFrameWidth : srcFrameHeight;
		this.height = (transform % 2 == 0) ? srcFrameHeight : srcFrameWidth;
	}

	private int getTransformedPos(int coordX, int coordY, int transform, boolean isX)
	{
		switch (transform)
		{
			case TRANS_NONE:
				return isX ? coordX : coordY;
			case TRANS_MIRROR:
				return isX ? srcFrameWidth - coordX - 1 : coordY;
			case TRANS_MIRROR_ROT180:
				return isX ? coordX : srcFrameHeight - coordY - 1;
			case TRANS_ROT90:
				return isX ? srcFrameHeight - coordY - 1 : coordX;
			case TRANS_ROT180:
				return isX ? srcFrameWidth - coordX - 1 : srcFrameHeight - coordY - 1;
			case TRANS_ROT270:
				return isX ? coordY : srcFrameWidth - coordX - 1;
			case TRANS_MIRROR_ROT90:
				return isX ? srcFrameHeight - coordY - 1 : srcFrameWidth - coordX - 1;
			case TRANS_MIRROR_ROT270:
				return isX ? coordY : coordX;
			default:
				return 0;
		}
	}

	private class Rect 
	{
		int left, top, right, bottom;

		Rect(int left, int top, int right, int bottom) 
		{
			this.left = left;
			this.top = top;
			this.right = right;
			this.bottom = bottom;
		}
	}
}