package games.util;

import net.minesucht.enums.Games;

public class GameLoader {

	public static void loadGame(Games game){
		java.lang.reflect.Method method = null;
		
		try {
		  method = game.getInstance().getClass().getMethod("start"); 
		  method.invoke(game.getInstance());
		} catch (Exception e){}
		
	}
	
}
