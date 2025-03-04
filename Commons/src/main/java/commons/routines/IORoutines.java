package commons.routines;

import commons.commands.Command;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

@SuppressWarnings("WeakerAccess")
public class IORoutines {
	private static int defaultTransmitBufferSize = 8 * 1024;

	public static void transmit(InputStream streamFrom, OutputStream streamTo) throws IOException {
		transmit(streamFrom, streamTo, defaultTransmitBufferSize);
	}

	public static void transmit(InputStream streamFrom, OutputStream streamTo, int bufferSize) throws IOException {
		int count;
		byte[] buffer = new byte[bufferSize];
		while ((count = streamFrom.read(buffer)) > 0) {
			streamTo.write(buffer, 0, count);
		}
	}

	@Deprecated
	public static void transmitSplit(InputStream streamFrom, OutputStream... streamTo) throws IOException {
		transmitSplit(streamFrom, defaultTransmitBufferSize, streamTo);
	}

	@Deprecated
	public static void transmitSplit(InputStream streamFrom, int bufferSize, OutputStream... streamTo) throws IOException {
		int count;
		byte[] buffer = new byte[bufferSize];
		while ((count = streamFrom.read(buffer)) > 0) {
			for (OutputStream to : streamTo) {
				to.write(buffer, 0, count);
			}
		}
	}

	public static void transmitNBytes(long n, InputStream streamFrom, OutputStream streamTo) throws IOException {
		transmitNBytes(n, streamFrom, streamTo, defaultTransmitBufferSize);
	}

	public static void transmitNBytes(long n, InputStream streamFrom, OutputStream streamTo, int bufferSize) throws IOException {
		int count;
		int total = 0;
		byte[] buffer = new byte[bufferSize];
		while (total < n && (count = streamFrom.read(buffer)) > 0) {
			streamTo.write(buffer, 0, count);
			total += count;
		}
	}

	@Deprecated
	public static void transmitNBytesSplit(long n, InputStream streamFrom, OutputStream... streamTo) throws IOException {
		transmitNBytesSplit(n, streamFrom, defaultTransmitBufferSize, streamTo);
	}

	@Deprecated
	public static void transmitNBytesSplit(long n, InputStream streamFrom, int bufferSize, OutputStream... streamTo) throws IOException {
		int count;
		int total = 0;
		byte[] buffer = new byte[bufferSize];
		while (total < n && (count = streamFrom.read(buffer)) > 0) {
			for (OutputStream to : streamTo) {
				to.write(buffer, 0, count);
			}
			total += count;
		}
	}

	public static void sendSignalOnce(InetAddress address, int port, Command command) throws IOException {
		Socket socket = new Socket(address, port);
		sendSignal(socket, command);
		socket.close();
	}

	public static void sendSignal(Socket socket, Command command) throws IOException {
		ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
		outStream.writeObject(command);
	}

	public static Command receiveSignal(Socket socket) throws IOException, ClassNotFoundException {
		ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
		return (Command) inputStream.readObject();
	}
}
