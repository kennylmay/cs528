import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 */

/**
 * @author kresss
 * 
 */
public class Game {

	static int computer = 0;
	static int person = 1;
	
	public final int blackPlayerType;
	public final int whitePlayerType;
	
	private State currentState = new State(State.startBoard, State.black);
	
	public Game(int blackPlayer, int whitePlayer) {
		
		blackPlayerType = blackPlayer;
		whitePlayerType = whitePlayer;
	}
	
	public void play() {

		int currentPlayerType = 0;
		Move nextMove = new Move(0, 0, State.black);

		System.out.print(currentState.toString());

		while (currentState.isNotGameOver()) {

			if (currentState.currentTurn == State.black) {
				currentPlayerType = blackPlayerType;
			} else {
				currentPlayerType = whitePlayerType;
			}

			if (!currentState.playerHasMove(currentState.currentTurn)) {
				if (currentState.currentTurn == State.black) {
					System.out.println("X's Cannot Move!");
				} else {
					System.out.println("O's Cannot Move!");
				}
				
				currentState.switchTurn();
			}
			
			if (currentPlayerType != 0) {

				nextMove = currentState.getMaxMove(currentState.currentTurn);

				System.out.println("The Computer " + nextMove.toString());

			} else {

				if (currentState.currentTurn == State.black) {
					System.out.println("Enter X's Move (ex. A1): ");
				} else {
					System.out.println("Enter O's Move (ex. A1): ");
				}

				nextMove = readMove(currentState);

				System.out.println("The Player " + nextMove.toString());

			}

			System.out.println();
			currentState = new State(currentState, nextMove);
			System.out.print(currentState.toString());

		}
		
		// Print Out Score.
		System.out.println("Game Over, There are no more legal moves.");
		System.out.println("Final Score:");
		System.out.println("X's : " + currentState.getPlayerScore(State.black));
		System.out.println("O's : " + currentState.getPlayerScore(State.white));
		System.out.println("");
		
		if (currentState.getPlayerScore(State.black) > currentState
				.getPlayerScore(State.white)) {
			System.out.println("X's WIN!!!!");
		} else {
			System.out.println("O's WIN!!!!");
		}
	}
	
 	static public int promptForPlayerType() {

		String userInput;
		int userChoice = 0;
		boolean goodInput = false;

		InputStreamReader inStream = new InputStreamReader(System.in);
		BufferedReader buffRead = new BufferedReader(inStream);

		while (!goodInput) {
			System.out.println("");
			System.out
					.println("What kind of player would you like?");
			System.out.println("1: Person");
			System.out.println("2: Easy Computer");
			//System.out.println("3: Person vs Computer");
			//System.out.println("4: Computer vs Computer");
			System.out.println("");
			System.out.println("Enter choice: ");
			try {
				userInput = buffRead.readLine();
				userChoice = Integer.parseInt(userInput);

				if (userChoice > 0 && userChoice < 3) {
					goodInput = true;
				} else {
					System.out.println("Bad Choice, Try again.");
				}
			} catch (IOException err) {
				System.out.println("Error reading line");
				System.exit(0);
			} catch (NumberFormatException err) {
				System.out.println("Error Converting to a Number, Try again.");
				goodInput = false;
			}
		}
		
		return userChoice-1;
		
	}

	static public Move readMove(State currentState) {

		InputStreamReader inStream = new InputStreamReader(System.in);
		BufferedReader buffRead = new BufferedReader(inStream);

		String userInput;
		boolean goodInput = false;

		Move nextMove = new Move(0, 0, State.black);

		goodInput = false;
		do {
			try {
				userInput = buffRead.readLine();

				if (userInput.length() != 2) {
					System.out.println("Error Reading Choice, Try again.");
					continue;
				}

				int userRow = userInput.charAt(0) - 'A';
				int userCol = Integer.parseInt(userInput.substring(1)) - 1;

				if ((userRow < 0)
						|| (userRow > State.boardSize || userCol < 0 || userCol > State.boardSize)) {
					System.out.println("Bad Choice, Try again.");
				} else {
					nextMove = new Move(userRow, userCol,
							currentState.currentTurn);

					if (!currentState.isValidMove(nextMove)) {
						System.out
								.println("That is not a valid move, Try again.");
						continue;
					}
					goodInput = true;
				}
			} catch (IOException err) {
				System.out.println("Error reading line");
				System.exit(0);
			} catch (NumberFormatException err) {
				System.out.println("Error Converting to a Number, Try again.");
				goodInput = false;
			} catch (IndexOutOfBoundsException err) {
				System.out.println("Error reading line");
				goodInput = false;
			}
		} while (!goodInput);

		return nextMove;

	}
	
}
