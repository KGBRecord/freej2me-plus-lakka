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

import org.w3c.dom.Element;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.DOMException;

public interface SVGElement extends Element, EventTarget 
{

    Element getFirstElementChild();
    
    float getFloatTrait(String name) throws DOMException;
    
    String getId();
    
    SVGMatrix getMatrixTrait(String name) throws DOMException;
    
    Element getNextElementSibling();
    
    SVGPath getPathTrait(String name) throws DOMException;
    
    SVGRect getRectTrait(String name) throws DOMException;
    
    SVGRGBColor getRGBColorTrait(String name) throws DOMException;
    
    String getTrait(String name) throws DOMException;
    
    String getTraitNS(String namespaceURI, String name) throws DOMException;

    void setFloatTrait(String name, float value) throws DOMException;
    
    void setId(String Id) throws DOMException;
    
    void setMatrixTrait(String name, SVGMatrix matrix) throws DOMException;
    
    void setPathTrait(String name, SVGPath path) throws DOMException;
    
    void setRectTrait(String name, SVGRect rect) throws DOMException;
    
    void setRGBColorTrait(String name, SVGRGBColor color) throws DOMException;
    
    void setTrait(String name, String value) throws DOMException;
    
    void setTraitNS(String namespaceURI, String name, String value) throws DOMException;
}