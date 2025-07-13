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
package com.nokia.mid.payment;

public class IAPClientProductData
{

	public static final int OTHER_DRM = 0;
	public static final int NOKIA_DRM = 1;

	private String productId;

	public IAPClientProductData(String pid) { productId = pid; }

	public String getProductId() { return productId; }

	public String getTitle() { return "Bypass Purchase"; }

	public String getShortDescription() { return "Purchase product requested by MIDlet"; }

	public String getLongDescription() { return "Purchase product requested by MIDlet."; }

	public String getPrice() { return "$ 0.00"; }

	public int getDrmProtection() { return NOKIA_DRM; }
}