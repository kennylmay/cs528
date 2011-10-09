import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 
 */

/**
 * @author kresss
 * 
 */
public class reversi {

	static int computer = 0;
	static int person = 1;

	static private int promptForPlayerType() {

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

				if (userChoice < 0 && userChoice < 3) {
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

	static private Move readMove(State currentState) {

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

	static private int getTurnType(State currentState) {
		
		if 
		
		return computer;
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		boolean computerMove = false;

		int blackPlayerType = 0;
		int whitePlayerType = 0;

		Move nextMove = new Move(0, 0, State.black);

		System.out.print("X's ");
		blackPlayerType = promptForPlayerType();
		

		System.out.print("O's ");
		blackPlayerType = promptForPlayerType();

		State currentState = new State(State.startBoard, State.black);
		System.out.print(currentState.toString());

		for (int i = 0; i < 2; i++) {
			if ((currentState.currentTurn == State.black)
					&& (blackPlayerType == person)) {

				computerMove = false;
				System.out.println("Enter X's Move (ex. A1): ");

			} else if ((currentState.currentTurn == State.black)
					&& (blackPlayerType == computer)) {

				computerMove = true;
				System.out.println("Enter X's Move (ex. A1): ");

			} else if ((currentState.currentTurn == State.white)
					&& (whitePlayerType == person)) {

				computerMove = false;
				System.out.println("Enter O's Move (ex. A1): ");

			} else if ((currentState.currentTurn == State.white)
					&& (whitePlayerType == computer)) {

				computerMove = true;
				System.out.println("Enter O's Move (ex. A1): ");

			}

			if (computerMove) {

				nextMove = currentState.getMaxMove(currentState.currentTurn);

				System.out.println("The Computer takes " + nextMove.toString());

			} else {
				nextMove = readMove(currentState);
			}

			System.out.println();
			currentState = new State(currentState, nextMove);
			System.out.print(currentState.toString());

			LinkedList<Move> moves = currentState.getValidMoves();
			Iterator<Move> iter = moves.iterator();
			while (iter.hasNext()) {
				System.out.print(iter.next() + " ");
			}

		}
	}

}
