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
package org.w3c.dom.svg;

import org.w3c.dom.DOMException;

public interface SVGPath 
{

    static final short MOVE_TO = 'M';
    static final short LINE_TO = 'L';
    static final short CURVE_TO = 'C';
    static final short QUAD_TO = 'Q';
    static final short CLOSE = 'Z';

    void close();
    
    void curveTo(float x1, float y1, float x2, float y2, float x3, float y3);
    
    int getNumberOfSegments();
    
    short getSegment(int cmdIndex) throws DOMException;
    
    float getSegmentParam(int cmdIndex, int paramIndex) throws DOMException;
    
    void lineTo(float x, float y);
    
    void moveTo(float x, float y);
    
    void quadTo(float x1, float y1, float x2, float y2);
}