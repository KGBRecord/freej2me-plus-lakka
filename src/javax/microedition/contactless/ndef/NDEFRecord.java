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
package javax.microedition.contactless.ndef;

public class NDEFRecord 
{

    private NDEFRecordType recordType;
    private byte[] id;
    private byte[] payload;

    public NDEFRecord(byte[] data, int offset) 
    {
        if (data == null) { throw new NullPointerException(); }
        if (offset < 0 || offset >= data.length) { throw new ArrayIndexOutOfBoundsException(); }
    }

    public NDEFRecord(NDEFRecordType recordType, byte[] id, byte[] payload) 
    {
        if (recordType == null) { throw new NullPointerException(); }

        this.recordType = recordType;
        this.id = id != null ? id.clone() : null;
        this.payload = payload != null ? payload.clone() : null;
    }

    public void appendPayload(byte[] payload) 
    {
        if (payload == null || payload.length == 0) return;
        if (recordType.getFormat() == NDEFRecordType.EMPTY) { throw new IllegalArgumentException(); }
    }

    public byte[] getId() { return id != null ? id.clone() : null; }

    public NDEFMessage getNestedNDEFMessage(int offset) 
    {
        if (offset < 0 || payload == null || offset >= payload.length) { throw new ArrayIndexOutOfBoundsException(); }

        return null;
    }

    public byte[] getPayload() { return payload != null ? payload.clone() : null; }

    public long getPayloadLength() { return payload != null ? payload.length : 0; }

    public NDEFRecordType getRecordType() { return recordType; }

    public void setId(byte[] id) 
    {
        if (recordType.getFormat() == NDEFRecordType.EMPTY) { throw new IllegalArgumentException(); }

        this.id = id != null ? id.clone() : null;
    }

    public byte[] toByteArray() { return null; }
}