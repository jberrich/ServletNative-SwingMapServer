package ma.jberrich.swingmap.server;

public interface ConsoleMessage {
	
	public void displayRequest(String host, double latitude, double longitude);
	public void displayResponse(String host);

}
