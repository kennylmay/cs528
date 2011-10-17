/**
 * 
 */

/**
 * @author kresss
 * 
 */
public class reversi {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Game theGame;

		int blackPlayerType = 0;
		int whitePlayerType = 0;

		System.out.print("X's ");
		blackPlayerType = Game.promptForPlayerType();

		System.out.print("O's ");
		whitePlayerType = Game.promptForPlayerType();

		theGame = new Game(blackPlayerType, whitePlayerType);

		theGame.play();

	}

}
