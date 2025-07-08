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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NDEFMessage 
{

    private List<NDEFRecord> records = new ArrayList<NDEFRecord>();

    public NDEFMessage() { }

    public NDEFMessage(byte[] data, int offset) 
    {
        if (data == null) { throw new NullPointerException(); }
        if (offset < 0 || offset >= data.length) { throw new ArrayIndexOutOfBoundsException(); }
    }

    public NDEFMessage(NDEFRecord[] records) 
    {
        if (records == null) { throw new NullPointerException(); }
        if (records.length == 0) { throw new IllegalArgumentException(); }

        this.records = new ArrayList<NDEFRecord>(Arrays.asList(records));
    }

    public void appendRecord(NDEFRecord record) 
    {
        if (record == null) { throw new NullPointerException(); }

        records.add(record);
    }

    public int getNumberOfRecords() { return records.size(); }

    public NDEFRecord getRecord(byte[] id) 
    {
        if (id == null) { throw new NullPointerException(); }

        return null;
    }

    public NDEFRecord getRecord(int index) 
    {
        if (index < 0 || index >= records.size()) { throw new IndexOutOfBoundsException(); }

        return records.get(index);
    }

    public NDEFRecord[] getRecord(NDEFRecordType recordType) 
    {
        if (recordType == null) { throw new NullPointerException(); }

        return null;
    }

    public NDEFRecord[] getRecords() { return null; }

    public NDEFRecordType[] getRecordTypes() { return null; }

    public void insertRecord(int index, NDEFRecord record) 
    {
        if (index < 0 || index > records.size()) { throw new IndexOutOfBoundsException(); }
        if (record == null) { throw new NullPointerException(); }

        records.add(index, record);
    }

    public void removeRecord(int index) {
        if (index < 0 || index >= records.size()) { throw new IndexOutOfBoundsException(); }

        records.remove(index);
    }

    public void setRecord(int index, NDEFRecord record) {
        if (index < 0 || index >= records.size()) { throw new IndexOutOfBoundsException(); }
        if (record == null) { throw new NullPointerException(); }

        records.set(index, record);
    }

    public byte[] toByteArray() { return null; }
}