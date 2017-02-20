package com.spiddekauga.utils;

import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Wrapper for an output stream with progress
 */
public class OutputStreamProgress extends OutputStream {
private static final EventBus mEventBus = EventBus.getInstance();
/** Original output stream */
private final OutputStream mOutstream;
/** Total bytes */
private final long mcTotalBytes;
/** Total bytes written */
private long mcWrittenBytes = 0;

/**
 * @param outputStream original output stream to wrap
 */
public OutputStreamProgress(OutputStream outputStream) {
	this(outputStream, -1L);
}

/**
 * @param outputStream original output stream to wrap
 * @param cTotalBytes total byte count, -1 if not known
 */
public OutputStreamProgress(OutputStream outputStream, Long cTotalBytes) {
	mOutstream = outputStream;
	mcTotalBytes = cTotalBytes;
}

@Override
public void write(int b) throws IOException {
	mOutstream.write(b);
	mcWrittenBytes++;
	sendWriteEvent();
}

@Override
public void write(@NotNull byte[] b) throws IOException {
	mOutstream.write(b);
	mcWrittenBytes += b.length;
	sendWriteEvent();
}

@Override
public void write(@NotNull byte[] b, int off, int len) throws IOException {
	mOutstream.write(b, off, len);
	mcWrittenBytes += len;
	sendWriteEvent();
}

@Override
public void flush() throws IOException {
	mOutstream.flush();
}

@Override
public void close() throws IOException {
	mOutstream.close();
}

/**
 * Send write event to listeners
 */
private void sendWriteEvent() {
	mEventBus.post(new ProgressEvent(mcWrittenBytes, mcTotalBytes));
}
}
