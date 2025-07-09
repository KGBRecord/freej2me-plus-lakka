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
package javax.microedition.amms;

public interface MediaProcessorListener 
{
    static final String PROCESSING_ABORTED = "processingAborted";
    static final String PROCESSING_COMPLETED = "processingCompleted";
    static final String PROCESSING_ERROR = "processingError";
    static final String PROCESSING_STARTED = "processingStarted";
    static final String PROCESSING_STOPPED = "processingStopped";
    static final String PROCESSOR_REALIZED = "processRealized";

    void mediaProcessorUpdate(MediaProcessor processor, String event, Object eventData);
}