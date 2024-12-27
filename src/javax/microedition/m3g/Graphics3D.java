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
package javax.microedition.m3g;

import java.util.Hashtable;

import javax.microedition.m3g.Transform;

import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.image.DataBufferInt;

import org.recompile.mobile.Mobile;
import org.recompile.mobile.PlatformGraphics;

public class Graphics3D
{

	public static final int ANTIALIAS = 2;
	public static final int DITHER = 4;
	public static final int OVERWRITE = 16; // This might be unused here, as SW rasterization gives us direct control over pixels
	public static final int TRUE_COLOR = 8;


	public static final boolean SUPPORT_ANTIALIASING = false;
	public static final boolean SUPPORT_TRUE_COLOR = false;
	public static final boolean SUPPORT_DITHERING = false;
	public static final boolean SUPPORT_MIPMAPPING = false;
	public static final boolean SUPPORT_PERSPECTIVE_CORRECTION = true;
	public static final boolean SUPPORT_LOCAL_CAMERA_LIGHTING = false;
	public static final int MAX_LIGHTS = 8;
	public static final int MAX_VIEWPORT_WIDTH = 1024;
	public static final int MAX_VIEWPORT_HEIGHT = 1024;
	public static final int MAX_VIEWPORT_DIMENSION = 1024;
	public static final int MAX_TEXTURE_DIMENSION = 256;
	public static final int MAX_SPRITE_CROP_DIMENSION = 256;
	public static final int MAX_TRANSFORMS_PER_VERTEX = 2;
	public static final int NUM_TEXTURE_UNITS = 1;
	private static Hashtable properties;

	// Render target
	private Object target;

	// Viewport
	private int viewx;
	private int viewy;
	private int vieww;
	private int viewh;

	private boolean depthEnabled;
	private float[] depthBuffer;
	private float near;
	private float far;

	private int hints;

	private Camera currCam;
	private Transform currCamTrans;
	private Transform currCamTransInv;
	private ArrayList<Light> currLights;
	private ArrayList<Transform> currLightTrans;


	public Graphics3D()
	{
		/* 
		 * The default depth range used is that of window coordinates, so 0 to near, and 1 to far
		 * JSR-184 specifies that Normalized Device Coordinates (NDC) can also be used, which ranges from -1 to 1.
		 */
		this.near = 0;
		this.far = 1;
		this.currCam = null;
		this.currCamTrans = null;
		this.currCamTransInv = null;
		this.currLights = new ArrayList<Light>();
		this.currLightTrans = new ArrayList<Transform>();
	}


	public int addLight(Light light, Transform transform)
	{
		/* As per JSR-184, addLight() must throw a NullPointerException if no light is given */
		if (light == null) { throw new NullPointerException("addLight() was called but no light object was provided."); }

		if (transform == null) { transform = new Transform(); }

		this.currLights.add(light);
		this.currLightTrans.add(transform);
		return this.currLights.size() - 1;
	}

	public void bindTarget(Object target)
	{
		/* Calls the method below specifying the depth buffer as enabled, and no render hints, as per JSR-184. */
		this.bindTarget(target, true, 0);
	}

	public void bindTarget(Object target, boolean depthBuffer, int hints)
	{
		/* 
		 * As per JSR-184, this function returns: 
		 * NullPointerException: If no render target is received as argument
		 * IllegalStateException: If the current Graphics3D Object already has a render target
		 */
		if (target == null)
			{ throw new NullPointerException("bindTarget() was called but no render target was provided."); }
		if (this.target != null)
			{ throw new IllegalStateException("This Graphics3D object already has a render target."); }

		/* The target can be an Image2D Object, or a Graphics Object (PlatformGraphics in our case). */
		if (target instanceof Image2D)
		{
			Image2D i2d = (Image2D) target;

			/* JSR-184 specifies that Image2D render targets can only have RGB or RGBA format. */
			if (i2d.getFormat() != Image2D.RGB && i2d.getFormat() != Image2D.RGBA)
			{ throw new IllegalArgumentException("Received a 2D render target with invalid internal format"); }

			/* It's a 2D image, so paint the canvas with it starting from the top-left corner */
			this.viewx = 0;
			this.viewy = 0;
			this.vieww = i2d.getWidth();
			this.viewh = i2d.getHeight();
		}
		else if (target instanceof PlatformGraphics)
		{
			// This is supposed to be either of the following:
			//   - java.awt.Graphics
			//   - javax.microedition.lcdui.Graphics
			// but we're getting org.recompile.mobile.PlatformGraphics.
			//
			// I assume it serves the same purpose and will work as expected.

			PlatformGraphics grp = (PlatformGraphics) target;
			this.viewx = grp.getClipX();
			this.viewy = grp.getClipY();
			this.vieww = grp.getClipWidth();
			this.viewh = grp.getClipHeight();
		} else 
		{
			/* If it is neither of those, throw an IllegalArgumentException as per JSR-184. */ 
			throw new IllegalArgumentException("Received render target is neither an instance of Image2D nor Graphics");
		}

		/* 
		 * The final check performed before binding throws IllegalArgumentException if:
		 * 1 - The render target's width is larger than the max supported.
		 * 2 - The render target's height is taller than the max supported.
		 * 3 - The render hint is an OR bitmask that matches with one or more of [ANTIALIAS, DITHER, TRUE_COLOR, OVERWRITE], or not zero.
		 */
		if (this.vieww > MAX_VIEWPORT_WIDTH || this.viewh > MAX_VIEWPORT_HEIGHT || (hints & ~(ANTIALIAS | DITHER | TRUE_COLOR | OVERWRITE)) != 0)
			{ throw new IllegalArgumentException("Render target either has larger dimensions than supported, or the render hint is invalid"); }

		this.target = target;
		this.depthBuffer = new float[this.vieww * this.viewh];
		this.depthEnabled = depthBuffer;
		this.hints = hints;
	}

	public void clear(Background background)
	{
		/* As per JSR-184, throw IllegalStateException if this Graphics3D object does not have a render target. */
		if (this.target == null) { throw new IllegalStateException("Cannot clear Background on a Graphics3D without a render target."); }

		int color = 0;
		int x = viewx;
		int y = viewy;
		int w = vieww;
		int h = viewh;
		boolean clearColor = true;
		boolean clearDepth = true;

		if (background != null)
		{
			color = background.getColor();
			x = background.getCropX();
			y = background.getCropY();
			w = background.getCropWidth();
			h = background.getCropHeight();
			clearColor = background.isColorClearEnabled();
			clearDepth = background.isDepthClearEnabled();
		}
		else { color = 0x00000000; }

		/* 
		 * If the background object is null: 
		 * Color buffer is cleared to transparent black 
		 * Depth buffer is cleared to the max depth value, 1.0.
		 */

		if (clearColor)
		{
			if (this.target instanceof Image2D)
			{
				Mobile.log(Mobile.LOG_WARNING, Graphics3D.class.getPackage().getName() + "." + Graphics3D.class.getSimpleName() + ": " + "Clear to Image2D not Implemented");
				Image2D i2d = (Image2D) this.target;

				// CHECK is the bg image used only if clearColor is true?

				// TODO do this check in the PlatformGraphics branch too
				if (background.getImage() == null || background.getImage().getFormat() != i2d.getFormat())
				{ throw new IllegalArgumentException("The background image to be cleared does not have the same format as the render target."); }

				// TODO support clearing Image2D
			}
			else if (this.target instanceof PlatformGraphics)
			{
				PlatformGraphics grp = (PlatformGraphics) this.target;
				grp.getGraphics2D().setColor(new Color(color));
				grp.getGraphics2D().fillRect(x, y, w, h);

				// Draw the background's image
				if(background.getImage() != null) 
				{
					Mobile.log(Mobile.LOG_WARNING, Graphics3D.class.getPackage().getName() + "." + Graphics3D.class.getSimpleName() + ": " + "Clear with Background Image Untested");
					int[] rasterData = ((DataBufferInt) grp.getCanvas().getRaster().getDataBuffer()).getData();
					for(; y < h; y++) 
					{
						for(; x < w; x++) 
						{
							rasterData[y * grp.getCanvas().getWidth() + x] = background.getImage().getConvertedPixel(x, y);
						}
					}
					
				}
			}
		}

		if (clearDepth) { Arrays.fill(this.depthBuffer, this.far); }
	}

	public Camera getCamera(Transform transform)
	{
		if (transform != null) { transform.set(this.currCamTrans); }
		return this.currCam;
	}

	public float getDepthRangeFar() { return far; }

	public float getDepthRangeNear() { return near;}

	public int getHints() { return hints; }

	public static Graphics3D getInstance() { return Mobile.getGraphics3D(); }

	public Light getLight(int index, Transform transform)
	{
		/* As per JSR-184, throw IndexOutOfBoundsException if the requested light index is out of bounds. */
		if (index < 0 || index > this.currLights.size()) { throw new IndexOutOfBoundsException("The received light index is out of bounds."); }

		/* If a transform variable is received, use it to store the requested light's transform. */
		if (transform != null) { transform.set(this.currLightTrans.get(index)); }

		return this.currLights.get(index);
	}

	/* This is supposed to include nulls, so just return the size */
	public int getLightCount() { return this.currLights.size(); }

	public static Hashtable getProperties()
	{
		if (Graphics3D.properties != null)
			return Graphics3D.properties;

		Hashtable<String, Object> p = new Hashtable<String, Object>();
		p.put("supportAntialiasing", SUPPORT_ANTIALIASING);
		p.put("supportTrueColor", SUPPORT_TRUE_COLOR);
		p.put("supportDithering", SUPPORT_DITHERING);
		p.put("supportMipmapping", SUPPORT_MIPMAPPING);
		p.put("supportPerspectiveCorrection", SUPPORT_PERSPECTIVE_CORRECTION);
		p.put("supportLocalCameraLighting", SUPPORT_LOCAL_CAMERA_LIGHTING);
		p.put("maxLights", MAX_LIGHTS);
		p.put("maxViewportWidth", MAX_VIEWPORT_WIDTH);
		p.put("maxViewportHeight", MAX_VIEWPORT_HEIGHT);
		p.put("maxViewportDimension", MAX_VIEWPORT_DIMENSION);
		p.put("maxTextureDimension", MAX_TEXTURE_DIMENSION);
		p.put("maxSpriteCropDimension", MAX_SPRITE_CROP_DIMENSION);
		p.put("maxTransformsPerVertex", MAX_TRANSFORMS_PER_VERTEX);
		p.put("numTextureUnits", NUM_TEXTURE_UNITS);
		Graphics3D.properties = p;

		return Graphics3D.properties;
	}

	public Object getTarget() { return this.target; }

	public int getViewportHeight() { return viewh; }

	public int getViewportWidth() { return vieww; }

	public int getViewportX() { return viewx; }

	public int getViewportY() { return viewy; }

	public boolean isDepthBufferEnabled() { return this.depthEnabled; }

	public void releaseTarget()
	{
		/* Ignore the call if no render target is bound. */
		if(this.target != null) 
		{
			/* 
			 * TODO: Flush the rendered 3D image to this target before releasing it 
			 * in order to ensure that the 3D image becomes visible.
			 */
			
			/* If there is a render target, release it */ 
			this.target = null;
		}
	}

	public void render(Node node, Transform transform)
	{
		/* As per JSR-184, throw NullPointerException if no node is received. */
		if(node == null) { throw new NullPointerException("render() was called but no node was provided."); }
	
		/* Also per JSR-184, throw IllegalStateException if this method is called but there's no camera or render target available. */ 
		if (this.target == null || this.currCam == null) 
			{ throw new IllegalStateException("render() was called but there is no camera or render target."); }

		/* Also per JSR-184, throw IllegalStateException if if node is not a Sprite3D, Mesh, or Group Object. */
		if (!(node instanceof Mesh || node instanceof Sprite3D || node instanceof Group)) 
			{ throw new IllegalArgumentException("Node is not an instance of any of the following: Sprite3D, Mesh, Group"); }

		// if any Mesh that is rendered violates the constraints defined in
		//    Mesh, MorphingMesh, SkinnedMesh, VertexBuffer, or IndexBuffer
		//    throw new java.lang.IllegalStateException();

		/* Receiving a null transform indicates that the identity matrix must be used. */
		if (transform == null) { transform = new Transform(); }

		Mobile.log(Mobile.LOG_WARNING, Graphics3D.class.getPackage().getName() + "." + Graphics3D.class.getSimpleName() + ": " + "Graphics3D.render NT");
		// TODO implement Graphics3D.render(Node, Transform)
	}

	public void render(VertexBuffer vertices, IndexBuffer triangles, Appearance appearance, Transform transform) 
	{ this.render(vertices, triangles, appearance, transform, -1); }

	public void render(VertexBuffer vertices, IndexBuffer triangles, Appearance appearance, Transform transform, int scope) 
	{
		/* TODO: Check the scope used by the submesh to find out which lights need to be applied, if it needs to be rendered, etc. */

		/* As per JSR-184, if vertices, triangles or appearence are null, throw a NullPointerException. */
		if (vertices == null || triangles == null || appearance == null) { throw new NullPointerException("Tried to render a submesh with incomplete info."); }
		
		/* Also per JSR-184, throw IllegalStateException if the application tries to render without having set up a render target or camera beforehand. */
		if (this.target == null || this.currCam == null) { throw new IllegalStateException("Tried to render a submesh without having a render target or camera first."); }
		
		// if `vertices` or `triangles` violates the constraints
		//    defined in VertexBuffer or IndexBuffer
		//    throw new java.lang.IllegalStateException();

		// TODO: Those two are unused at the moment
		int cullingMode = appearance.getPolygonMode() != null ? appearance.getPolygonMode().getCulling() : PolygonMode.CULL_BACK;
		int shadingMode = appearance.getPolygonMode() != null ? appearance.getPolygonMode().getShading() : PolygonMode.SHADE_SMOOTH;
		
		int windingOrder = appearance.getPolygonMode() != null ? appearance.getPolygonMode().getWinding() : PolygonMode.WINDING_CCW;
		boolean perspectiveCorrectionEnabled = appearance.getPolygonMode() != null ? appearance.getPolygonMode().isPerspectiveCorrectionEnabled() : false;

		float[] scaleBias = new float[4];

		Transform tr = new Transform();
		Transform textr = new Transform();
		Transform texcomptr = new Transform();

		VertexArray vertColors = vertices.getColors();
		VertexArray vertPos = vertices.getPositions(scaleBias);
		int vertCount = vertPos.getVertexCount();

		int[] triIndices = new int[triangles.getIndexCount()];
		triangles.getIndices(triIndices);

		// Scale and translate mesh
		tr.preScale(scaleBias[0], scaleBias[0], scaleBias[0]);
		tr.preTranslate(scaleBias[1], scaleBias[2], scaleBias[3]);

		Texture2D tex = appearance.getTexture(0);
		Image2D teximg = tex == null ? null : tex.getImage();
		VertexArray texCoords = vertices.getTexCoords(0, scaleBias); // get Texture coordinates

		
		if (tex != null) { tex.getCompositeTransform(texcomptr); }

		// Scale and translate texture coordinates (same scaleBias)
		textr.preScale(scaleBias[0], scaleBias[0], scaleBias[0]);
		textr.preTranslate(scaleBias[1], scaleBias[2], scaleBias[3]);
		textr.preMultiply(texcomptr);
		
		// -> Local space

		Transform projection = new Transform();
		this.currCam.getProjection(projection);

		// Transform mesh from local coords to world coords
		tr.preMultiplyTry(transform);
		// -> World space

		// Apply the inverse of the camera's transform to the mesh
		tr.preMultiplyTry(this.currCamTransInv);
		// -> View space

		// Apply projection matrix
		tr.preMultiply(projection);
		// -> Clip space

		// Do the transformation
		float[] vertClip = new float[4 * vertCount];
		tr.transform(vertPos, vertClip, true);

		float[] texVert = new float[4 * vertCount];
		if (texCoords != null) { textr.transform(texCoords, texVert, true); }

		// Create Triangle objects for clipping
		Triangle[] trisClip = Triangle.fromVertAndTris(vertClip, texVert, triIndices);

		// Clip triangles
		Triangle[] trisScreen = Arrays.stream(trisClip)
				.flatMap(t -> t.clip())
				.toArray(Triangle[]::new);

		// If perspective correction is enabled, do it for texture coordinates
		if(perspectiveCorrectionEnabled)
		{
			Mobile.log(Mobile.LOG_WARNING, Graphics3D.class.getPackage().getName() + "." + Graphics3D.class.getSimpleName() + ": " + "Perspective Correction Enabled! UNTESTED");
			for (Triangle t : trisScreen) 
			{
				// Get the w components for each triangle vertex
				float wA = t.wA();
				float wB = t.wB();
				float wC = t.wC();
		
				// Calculate perspective-correct texture coordinates
				float[] texCoordA = {
					t.sA() / wA,
					t.tA() / wA,
					0, // rA
					1  // qA
				};
				float[] texCoordB = {
					t.sB() / wB,
					t.tB() / wB,
					0, // rB
					1  // qB
				};
				float[] texCoordC = {
					t.sC() / wC,
					t.tC() / wC,
					0, // rC
					1  // qC
				};
		
				// Set the corrected texture coordinates back into the triangle
				t.setTexCoords(texCoordA, texCoordB, texCoordC);
			}
		}
			
		
				// At this point the triangles in `trisScreen` are actually
		// in Normalized Device Coordinates, but they will be tranformed
		// to Screen space in-place, hence the name.




		// Reset transform
		tr.setIdentity();
		textr.setIdentity();

		// Fit to viewport
		tr.preScale(1, -1, 1);
		tr.preTranslate(1, 1, 0);
		tr.preScale((float) vieww / 2f, (float) viewh / 2f, 1f);
		if (teximg != null) { textr.preScale(teximg.getWidth(), teximg.getHeight(), 1); }

		// -> Screen space

		// Perform viewport transform
		Triangle.transform(trisScreen, tr, textr);

		if (this.target instanceof Image2D)
		{
			Mobile.log(Mobile.LOG_WARNING, Graphics3D.class.getPackage().getName() + "." + Graphics3D.class.getSimpleName() + ": " + "Render Target is instance of Image2D!");
			Image2D i2d = (Image2D) this.target;
			// TODO support rendering to Image2D
		}
		else if (this.target instanceof PlatformGraphics)
		{
			PlatformGraphics pgrp = (PlatformGraphics) this.target;
			Graphics2D grp = pgrp.getGraphics2D();
			int[] rasterData = ((DataBufferInt) pgrp.getCanvas().getRaster().getDataBuffer()).getData();

			Color colorOrig = grp.getColor();
			// Unused, as we are now getting vertex colors from the Triangle data
			//Color colorFill = new Color(0, 150, 240, 255);
			// Color colorDraw = new Color(255, 255, 255, 128);

			for (int tri_id = 0; tri_id < trisScreen.length; tri_id++)
			{
				if (tex == null || texCoords == null) // If there's no texture coords or a texture image, we should try rendering with vertex colors.
				{
					int[] coXr = new int[] 
					{
						Math.round(trisScreen[tri_id].xA()),
						Math.round(trisScreen[tri_id].xB()),
						Math.round(trisScreen[tri_id].xC())
					};
					int[] coYr = new int[] 
					{
						Math.round(trisScreen[tri_id].yA()),
						Math.round(trisScreen[tri_id].yB()),
						Math.round(trisScreen[tri_id].yC())
					};
					
					if(vertices.getColors() == null) { grp.setColor(new Color(vertices.getDefaultColor()));} // If there's no vertex colors, we have to render with the VertexBuffer's default color.
					else // If we have vertex colors, good. Read them to color up the triangles properly.
					{
						GradientPaint gradient;

						byte[][] color_vertex = new byte[3][4]; 

						Color[] colors = new Color[3];
					
						if(vertColors.getComponentCount() == 3)  // If 3 components, RGB
						{
							for (int i = 0; i < 3; i++) 
							{
								vertColors.get(trisScreen[tri_id].bufIndex[i], 1, color_vertex[i]);
								colors[i] = new Color (
								Byte.toUnsignedInt(color_vertex[i][0]), 
								Byte.toUnsignedInt(color_vertex[i][1]), 
								Byte.toUnsignedInt(color_vertex[i][2]));
							}
						}
						else // Else we'll assume RGBA, 4 components
						{
							for (int i = 0; i < 4; i++) 
							{
								vertColors.get(trisScreen[tri_id].bufIndex[i], 1, color_vertex[i]);
								colors[i] = new Color (
								Byte.toUnsignedInt(color_vertex[i][0]), 
								Byte.toUnsignedInt(color_vertex[i][1]), 
								Byte.toUnsignedInt(color_vertex[i][2]), 
								Byte.toUnsignedInt(color_vertex[i][3]));
							}
						}

						/* 
						 * TODO: Not accurate, as all 3 vertices of a triangle can have different colors that have to be interpolated,
						 * this method might not be doing it the correct way.
						 */
						Paint originalPaint = grp.getPaint();

						// Draw first gradient from color1 to color2
						gradient = new GradientPaint(
							coXr[0], coYr[0], colors[0],
							coXr[1], coYr[1], colors[1]
						);
						grp.setPaint(gradient);
						grp.fillPolygon(coXr, coYr, 3);

						// Draw second gradient from color2 to color3
						gradient = new GradientPaint(
							coXr[1], coYr[1], colors[1],
							coXr[2], coYr[2], colors[2]
						);
						grp.setPaint(gradient);
						grp.fillPolygon( new int[]{coXr[1], coXr[2], coXr[0]}, new int[]{coYr[1], coYr[2], coYr[0]}, 3 );

						grp.setPaint(originalPaint);

						continue; // continue because now we shouldn't hit the fillPolygon call below
					}
					
					grp.fillPolygon(coXr, coYr, 3);
					//grp.setColor(colorDraw);
					//grp.drawPolygon(coXr, coYr, 3);

					continue;
				}

				// Prepare ordering based on vertex positions
				Integer[] ord = {0, 1, 2};

				// Handle winding order
				if (windingOrder == PolygonMode.WINDING_CW) 
				{
					Mobile.log(Mobile.LOG_WARNING, Graphics3D.class.getPackage().getName() + "." + Graphics3D.class.getSimpleName() + ": " + "Polygon Winding is Clockwise! Untested, might render incorrectly");
					ord = new Integer[]{0, 2, 1}; // Adjust order for Clockwise Winding 
				}

				final int curID = tri_id;
				Arrays.sort(ord, (a, b) -> Float.compare(trisScreen[curID].v[4 * a + 0], trisScreen[curID].v[4 * b + 0]));
				Arrays.sort(ord, (a, b) -> Float.compare(trisScreen[curID].v[4 * a + 1], trisScreen[curID].v[4 * b + 1]));

				// Collect vertex attributes
				float[] coX = {trisScreen[tri_id].xA(), trisScreen[tri_id].xB(), trisScreen[tri_id].xC()};
				float[] coY = {trisScreen[tri_id].yA(), trisScreen[tri_id].yB(), trisScreen[tri_id].yC()};
				float[] coZ = {trisScreen[tri_id].zA(), trisScreen[tri_id].zB(), trisScreen[tri_id].zC()};
				float[] coS = {trisScreen[tri_id].sA(), trisScreen[tri_id].sB(), trisScreen[tri_id].sC()};
				float[] coT = {trisScreen[tri_id].tA(), trisScreen[tri_id].tB(), trisScreen[tri_id].tC()};

				// Extract ordered vertex attributes
				float[] xOrdered = {coX[ord[0]], coX[ord[1]], coX[ord[2]]};
				float[] yOrdered = {coY[ord[0]], coY[ord[1]], coY[ord[2]]};
				float[] zOrdered = {coZ[ord[0]], coZ[ord[1]], coZ[ord[2]]};
				float[] sOrdered = {coS[ord[0]], coS[ord[1]], coS[ord[2]]};
				float[] tOrdered = {coT[ord[0]], coT[ord[1]], coT[ord[2]]};

				// Define top, middle, and bottom vertices
				float xTop = xOrdered[0], xMidL = xOrdered[1], xBot = xOrdered[2];
				float yTop = yOrdered[0], yMid = yOrdered[1], yBot = yOrdered[2];
				float zTop = zOrdered[0], zMidL = zOrdered[1], zBot = zOrdered[2];
				float sTop = sOrdered[0], sMidL = sOrdered[1], sBot = sOrdered[2];
				float tTop = tOrdered[0], tMidL = tOrdered[1], tBot = tOrdered[2];

				// Calculate the right horizon
				float rHorizon = (yMid - yTop) / (yBot - yTop);
				float xMidR = xTop + rHorizon * (xBot - xTop);
				float zMidR = zTop + rHorizon * (zBot - zTop);
				float sMidR = sTop + rHorizon * (sBot - sTop);
				float tMidR = tTop + rHorizon * (tBot - tTop);

				// Swap midpoints if necessary
				if (xMidL > xMidR) 
				{
					float temp;

					// Swap values between left and right midpoints
					temp = xMidL; xMidL = xMidR; xMidR = temp;
					temp = zMidL; zMidL = zMidR; zMidR = temp;
					temp = sMidL; sMidL = sMidR; sMidR = temp;
					temp = tMidL; tMidL = tMidR; tMidR = temp;
				}

				// Draw both halves of the triangle
				for (int half = 0; half < 2; half++) 
				{
					// Determine the range for the y-coordinate
					int yStart = half == 0 ? Math.round(yTop) : Math.round(yMid);
					int yEnd = half == 0 ? Math.round(yMid) : Math.round(yBot);
					
					// Adjust drawY calculation based on half
					for (int y = yStart; y < yEnd; y++) 
					{
						float drawY = half == 0
							? (y - yTop) / (yMid - yTop)  // Upper half
							: 1f - (y - yMid) / (yBot - yMid); // Lower half
						drawY = Math.max(0f, Math.min(drawY, 1f));

						// Calculate interpolated values
						float xL = half == 0
							? xTop + drawY * (xMidL - xTop)
							: xBot + drawY * (xMidL - xBot);
						float xR = half == 0
							? xTop + drawY * (xMidR - xTop)
							: xBot + drawY * (xMidR - xBot);
						float zL = half == 0
							? zTop + drawY * (zMidL - zTop)
							: zBot + drawY * (zMidL - zBot);
						float zR = half == 0
							? zTop + drawY * (zMidR - zTop)
							: zBot + drawY * (zMidR - zBot);
						float sL = half == 0
							? sTop + drawY * (sMidL - sTop)
							: sBot + drawY * (sMidL - sBot);
						float sR = half == 0
							? sTop + drawY * (sMidR - sTop)
							: sBot + drawY * (sMidR - sBot);
						float tL = half == 0
							? tTop + drawY * (tMidL - tTop)
							: tBot + drawY * (tMidL - tBot);
						float tR = half == 0
							? tTop + drawY * (tMidR - tTop)
							: tBot + drawY * (tMidR - tBot);

						// Draw the pixels for the current y-coordinate
						for (int x = Math.round(xL); x < Math.round(xR); x++) 
						{
							try 
							{
								float drawX = (x - xL) / (xR - xL);
								drawX = Math.max(0f, Math.min(drawX, 1f));
								float z = zL + drawX * (zR - zL);
								
								// Only depth test if the compositingMode has the feature enabled. If compositingMode is not set, check if this target has depthBuffer enabled
								if((appearance.getCompositingMode() == null || (appearance.getCompositingMode() != null && appearance.getCompositingMode().isDepthTestEnabled() == true)) && isDepthBufferEnabled()) 
								{
									if (this.depthBuffer[this.vieww * y + x] < z) { continue; } // Skip if this pixel is not visible
								}
								

								float s = sL + drawX * (sR - sL);
								float t = tL + drawX * (tR - tL);
								int texPixel = teximg.getConvertedPixel(Math.round(s), Math.round(t));

								// Extract the alpha channel from the texture pixel
								int alpha = (texPixel >> 24) & 0xFF; // Assuming ARGB format

								if(appearance.getCompositingMode() != null) // Some games don't set up a compositingMode, so check it before using its threshold
								{
									if (alpha < (int) (appearance.getCompositingMode().getAlphaThreshold() * 255)) { continue; } // Skip transparent pixels below the alpha threshold
								}
								else 
								{
									if (alpha == 0) { continue; }
								}

								// Blend the pixel with the background
								int backgroundPixel = rasterData[y * pgrp.getCanvas().getWidth() + x];
								int blendedPixel;

								if (appearance.getCompositingMode() != null) // Blend the background with the texture using compositing mode's blend
								{
									blendedPixel = blendPixels(backgroundPixel, texPixel, alpha, appearance.getCompositingMode().getBlending());
								} 
								else // If compositingMode is absent, just use the texture's blend mode for blending
								{
									blendedPixel = blendPixels(backgroundPixel, texPixel, alpha, tex.getBlending());
								}
								rasterData[y * pgrp.getCanvas().getWidth() + x] = blendedPixel;

								// Update depth buffer, same as depth test, check this target's DepthBuffer if compositingMode is absent
								if((appearance.getCompositingMode() == null || (appearance.getCompositingMode() != null && appearance.getCompositingMode().isDepthWriteEnabled())) && isDepthBufferEnabled()) 
								{ 
									this.depthBuffer[this.vieww * y + x] = z; 
								}

							} catch (Exception e) { Mobile.log(Mobile.LOG_WARNING, Graphics3D.class.getPackage().getName() + "." + Graphics3D.class.getSimpleName() + ": " + "Error drawing triangle:" + e.getMessage()); }
						}
					}
				}
				// }
				// end of texture unit loop

			}

			grp.setColor(colorOrig);
		}
	}

	public void render(World world)
	{
		/* Clear the background first */
		clear(world.getBackground());

		/* As per JSR-184, throw NullPointerException if the received world is null. */
		if (world == null) { throw new NullPointerException("render(world) was called but no world was provided."); }
		
		/* Also per JSR-184, throw IllegalStateException this object has no render target yet. */
		if (this.target == null) { throw new IllegalStateException("render(world) was called but there is no render target."); }

		/* 
		 * if `world` has no active camera, or
		 * the active camera is not in that `world`
		 * throw new IllegalStateException();
		 */

		Transform tr = new Transform();

		Camera worldCamera = world.getActiveCamera();

		if(worldCamera == null) { throw new IllegalStateException("Cannot render a world that has no active camera."); }

		if(!worldCamera.getTransformTo(world, tr)) { throw new IllegalStateException("Active camera is not in world."); }
		/* 
		 * if the bg-img of `world` is not the same format as `this.target`:
		 * throw new IllegalStateException();
		 */

		/* 
		 * if any Mesh that is rendered violates the constraints defined in
		 * Mesh, MorphingMesh, SkinnedMesh, VertexBuffer, or IndexBuffer
		 * throw new IllegalStateException();
		 */

		/* 
		 * if the Transform from the active camera of `world`
		 * to the world space is uninvertible
		 * throw new ArithmeticException();
		 * Note: this will be thrown by Transform.invert() if appropriate
		 */

		Mobile.log(Mobile.LOG_WARNING, Graphics3D.class.getPackage().getName() + "." + Graphics3D.class.getSimpleName() + ": " + "Graphics3D.render W");
		// TODO implement Graphics3D.render(World)
	}

	public void resetLights()
	{
		this.currLights.clear();
		this.currLightTrans.clear();
	}

	public void setCamera(Camera camera, Transform transform)
	{
		this.currCam = camera;

		/* If no transform is given, the identity matrix is used as per JSR-184. */
		if (transform == null) 
		{ 
			this.currCamTrans = new Transform();
			this.currCamTransInv = new Transform();
		}
		else /* Else, set the transform and its inverse accordingly. */
		{
			this.currCamTrans = new Transform(transform);
			this.currCamTransInv = new Transform(transform);
		}
		this.currCamTransInv.invert(); /* This one will execute regardless of the given transform above. */
	}

	public void setDepthRange(float near, float far)
	{
		/* As per JSR-184, throw IllegalArgumentException if the received near and/or far planes have unsupported values. */
		if (near < 0 || far < 0 || 1 < near || 1 < far) { throw new IllegalArgumentException("The requested Depth Range values are invalid."); }
		else { this.near=near; this.far=far; }	
	}

	public void setLight(int index, Light light, Transform transform)
	{
		/* As per JSR-184, throw IndexOutOfBoundsException if index < 0 or index > CurrentAmountOfLights. */
		if (index < 0 || index > this.currLights.size()) { throw new IndexOutOfBoundsException("Tried to modify a Light on an out-of-bounds index."); }

		/* If no transform is received, use the identity matrix. */
		if (transform == null) { transform = new Transform(); }

		// Indices are NOT supposed to change here,
		// so we're simply updating the arrays at the index,
		// even if any new value is null.
		this.currLights.set(index, light);
		this.currLightTrans.set(index, transform);
	}

	public void setViewport(int x, int y, int width, int height)
	{
		/* As per JSR-184, throw IllegalArgumentException if the received width and height are < 0, or beyond the max allowed. */
		if (width <= 0 || height <= 0 || width > MAX_VIEWPORT_WIDTH || height > MAX_VIEWPORT_HEIGHT)
			{ throw new IllegalArgumentException("Tried to set a viewport of unsupported size."); }

		this.viewx = x;
		this.viewy = y;
		this.vieww = width;
		this.viewh = height;
	}


	/* Helper Methods */

	// This one is used for alpha pixel blending, supports both CompositingMode and Texture2D Blending modes
	private int blendPixels(int background, int foreground, int alpha, int blendMode) 
	{
		int bgA = (background >> 24) & 0xFF;
		int bgR = (background >> 16) & 0xFF;
		int bgG = (background >> 8) & 0xFF;
		int bgB = background & 0xFF;

		int fgR = (foreground >> 16) & 0xFF;
		int fgG = (foreground >> 8) & 0xFF;
		int fgB = foreground & 0xFF;

		int outR, outG, outB, outA;

		int fgAlpha, fgColor;

		float alphaNorm;

		switch (blendMode)
		{
			// CompositingMode.REPLACE isn't handled in here, as the result will just be one of the Texture2D modes.
			case CompositingMode.REPLACE:
				// If the foreground has transparency, we have to blend with the background
				 fgAlpha = (foreground >> 24) & 0xFF; // Extract alpha from the foreground
				 fgColor = foreground & 0x00FFFFFF;    // Extract color from the foreground

				// Replace the background color with the foreground color
				// Use the foreground alpha directly
				return (fgAlpha << 24) | fgColor; // Combine alpha and color

			case CompositingMode.ALPHA_ADD:
				alphaNorm = alpha / 255f;
				outR = (int) (fgR * alphaNorm);
				outG = (int) (fgG * alphaNorm);
				outB = (int) (fgB * alphaNorm);

				// Add to the background color
				outR = Math.min(255, outR + bgR);
				outG = Math.min(255, outG + bgG);
				outB = Math.min(255, outB + bgB);
				outA = bgA + (int)(alpha * (1 - (bgA / 255f)));
				return (Math.min(Math.max(outA, 0), 255) << 24) | (Math.min(Math.max(outR, 0), 255) << 16) | (Math.min(Math.max(outG, 0), 255) << 8) | Math.min(Math.max(outB, 0), 255);

			case CompositingMode.ALPHA:
				alphaNorm = alpha / 255f;
				outR = (int) (((fgR * alphaNorm) + (bgR * (1 - alphaNorm))));
				outG = (int) (((fgG * alphaNorm) + (bgG * (1 - alphaNorm))));
				outB = (int) ((((fgB * alphaNorm) + (bgB * (1 - alphaNorm)))));
				outA = (int) ((bgA * (1 - alphaNorm) + alpha)); 
				return (Math.min(Math.max(outA, 0), 255) << 24) | (Math.min(Math.max(outR, 0), 255) << 16) | (Math.min(Math.max(outG, 0), 255) << 8) | Math.min(Math.max(outB, 0), 255);

			case CompositingMode.MODULATE:
				// Multiply the source color by the destination color TODO: UNTESTED
				outR = (int) ((fgR * bgR) / 255);
				outG = (int) ((fgG * bgG) / 255);
				outB = (int) ((fgB * bgB) / 255);
				outA = (foreground >> 24) & 0xFF; // Keep alpha from the foreground
				return (Math.min(Math.max(outA, 0), 255) << 24) | (Math.min(Math.max(outR, 0), 255) << 16) | (Math.min(Math.max(outG, 0), 255) << 8) | Math.min(Math.max(outB, 0), 255);

			case CompositingMode.MODULATE_X2:
				// Multiply the source color by the destination color and double the source color TODO: UNTESTED
				outR = (int) (((2 * fgR) * bgR) / 255);
				outG = (int) (((2 * fgG) * bgG) / 255);
				outB = (int) (((2 * fgB) * bgB) / 255);
				outA = (foreground >> 24) & 0xFF; // Keep alpha from the foreground
				return (Math.min(Math.max(outA, 0), 255) << 24) | (Math.min(Math.max(outR, 0), 255) << 16) | (Math.min(Math.max(outG, 0), 255) << 8) | Math.min(Math.max(outB, 0), 255);

			// Texture blend modes
			case Texture2D.FUNC_REPLACE:
				// If the foreground has transparency, we have to blend with the background.
				 fgAlpha = (foreground >> 24) & 0xFF; // Extract alpha from the foreground
				 fgColor = foreground & 0x00FFFFFF;    // Extract color from the foreground

				// Replace the background color with the foreground color
				// Use the foreground alpha directly
				return (fgAlpha << 24) | fgColor; // Combine alpha and color


			case Texture2D.FUNC_MODULATE:
				// Multiply color components TODO: UNTESTED
				return ((fgR * bgR / 255) << 16) | ((fgG * bgG / 255) << 8) | (fgB * bgB / 255);

			case Texture2D.FUNC_DECAL:
				// TODO: Implement according to the Decal blending mode
				break;

			case Texture2D.FUNC_BLEND:
				alphaNorm = alpha / 255f;
				outR = (int) ((fgR * alphaNorm) + (bgR * (1 - alphaNorm)));
				outG = (int) ((fgG * alphaNorm) + (bgG * (1 - alphaNorm)));
				outB = (int) ((fgB * alphaNorm) + (bgB * (1 - alphaNorm)));
				outA = (int) ((alpha * alphaNorm) + (bgA * (1 - alphaNorm)));
				return (Math.min(Math.max(outA, 0), 255) << 24) | (Math.min(Math.max(outR, 0), 255) << 16) | (Math.min(Math.max(outG, 0), 255) << 8) | Math.min(Math.max(outB, 0), 255);

			case Texture2D.FUNC_ADD:
				// Extract color components from background and foreground. TODO: UNTESTED
	
				// Add the colors and clamp to [0, 255]
				outR = Math.min(Math.max((bgR + fgR), 0), 255);
				outG = Math.min(Math.max((bgG + fgG), 0), 255);
				outB = Math.min(Math.max((bgB + fgB), 0), 255);
	
				// Return the resulting color with full opacity
				return (255 << 24) | (outR << 16) | (outG << 8) | outB;

			default:
				return background; // Fallback
		}
		return background; // Default return
	}
}
