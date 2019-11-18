package commons.routines;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IORoutines {
	public static void transmit(InputStream streamFrom, OutputStream streamTo) throws IOException {
		transmit(streamFrom, streamTo, 8192);
	}

	public static void transmit(InputStream streamFrom, OutputStream streamTo, int bufferSize) throws IOException {
		transmitSplit(streamFrom, bufferSize, streamTo);
	}

	public static void transmitSplit(InputStream streamFrom, OutputStream... streamTo) throws IOException {
		transmitSplit(streamFrom, 8192, streamTo);
	}

	public static void transmitSplit(InputStream streamFrom, int bufferSize, OutputStream... streamTo) throws IOException {
		int count;
		byte[] buffer = new byte[bufferSize];
		while ((count = streamFrom.read(buffer)) > 0) {
			for (OutputStream to : streamTo) {
				to.write(buffer, 0, count);
			}
		}
	}
}
