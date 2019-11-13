package commons.ioroutines;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IORoutines {
	public static void transmit(InputStream streamFrom, OutputStream streamTo, int bufferSize) throws IOException {
		int count;
		byte[] buffer = new byte[bufferSize];
		while ((count = streamFrom.read(buffer)) > 0) {
			streamTo.write(buffer, 0, count);
		}
	}
}
