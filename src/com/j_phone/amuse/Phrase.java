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
package com.j_phone.amuse;

import java.io.IOException;

public class Phrase extends com.jblend.media.smaf.phrase.Phrase
{
    private com.jblend.media.smaf.phrase.Phrase phrase;

	public Phrase(byte[] data) throws IOException 
    {
        super(data);
        phrase = (com.jblend.media.smaf.phrase.Phrase) this;
	}

	public Phrase(String data) throws IOException 
    {
        super(data);
		phrase = (com.jblend.media.smaf.phrase.Phrase) this;
	}

    public com.jblend.media.smaf.phrase.Phrase getPhraseImpl() { return phrase; }
}