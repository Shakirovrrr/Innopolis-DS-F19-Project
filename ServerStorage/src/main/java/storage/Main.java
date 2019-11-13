package storage;

class Main {
	public static void main(String[] args) {
		System.out.println("Hello Storage!");

		ClientDispatcher dispatcher = new ClientDispatcher(14785);
		dispatcher.start();
	}
}