package games.util;

public interface Game {

	public void start();
	public void end();
	public void registerListener();
	public void unregisterListener();
	public void registerPoints();
	public void registerWinner();
	
	
	
}