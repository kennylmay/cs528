import java.util.Iterator;
import java.util.LinkedList;

public class State {

	// Game Board Dimensions
	static public int boardSize = 8;

	// Black Piece Value
	static public int black = -1;

	// White Piece Value
	static public int white = 1;

	// Empty Space Value
	static public int empty = 0;

	private int[][] board;
	protected int currentTurn;

	static public int[][] startBoard = new int[][] {
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, -1, 0, 0, 0 },
			{ 0, 0, 0, -1, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } };

	static private int[][] placeValues = new int[][] {
			{ 100, -35, 10, 8, 8, 10, -35, 100 },
			{ -35, -50, -4, -5, -5, -4, -50, -35 },
			{ 10, -4, 3, 1, 1, 3, -4, 10 }, { 8, 3, 1, 5, 5, 1, -5, 8 },
			{ 8, 3, 1, 5, 5, 1, -5, 8 }, { 10, -4, 3, 1, 1, 3, -4, 10 },
			{ -35, -50, -4, -5, -5, -4, -50, -35 },
			{ 100, -35, 10, 8, 8, 10, -35, 100 } };

	@Override
	public String toString() {
		String output = "  1 2 3 4 5 6 7 8\n";
		int charRow = 'A';
		for (int i = 0; i < boardSize; i++) {
			output += Character.toString((char) charRow) + " ";
			for (int j = 0; j < boardSize; j++) {
				if (board[i][j] == State.empty) {
					output += "  ";
				} else if (board[i][j] == State.black) {
					output += "x ";
				} else if (board[i][j] == State.white) {
					output += "o ";
				}
			}
			output += "\n";
			charRow++;
		}

		if (currentTurn == 1) {
			output += "\nO's Move \n\n";
		} else {
			output += "\nX's Move \n\n";
		}

		return output;

	}

	State(int[][] b) {

		// Copy the board we were given into our board.
		this.board = new int[boardSize][boardSize];
		copyBoard(b, this.board);
	}

	State(int[][] b, int turn) {

		// Copy the board we were given into our board.
		this.board = new int[boardSize][boardSize];
		copyBoard(b, this.board);
		currentTurn = turn;
	}

	State(State previous, Move move) {

		// Copy the board we were given into our board.
		this.board = new int[boardSize][boardSize];
		copyBoard(previous.board, this.board);
		// Apply the move to our new board.
		this.board[move.row][move.col] = move.player;
		this.currentTurn = previous.currentTurn * -1;

		// Check in all directions
		for (int deltaCol = -1; deltaCol <= 1; deltaCol++) {
			for (int deltaRow = -1; deltaRow <= 1; deltaRow++) {

				// Check for one of the other players pieces.
				if (onBoard(move.row + deltaRow, move.col + deltaCol) && (this.board[move.row + deltaRow][move.col + deltaCol] == (move.player * -1))) {
					
					// Found other players piece, now we need to check if there
					// is one of our pieces somewhere after theirs.
					for (int i = 1; onBoard(move.row + (deltaRow * i), move.col + (deltaCol * i)); i++) {
						if (this.board[move.row + (deltaRow * i)][move.col + (deltaCol * i)] == State.empty) {
							break;
						}
						
						if (this.board[move.row + (deltaRow * i)][move.col + (deltaCol * i)] == move.player) {
							for (int j = 1; j < i; j++) {
								this.board[move.row + (deltaRow * j)][move.col + (deltaCol * j)] = move.player;
							}

							// Stop Searching in this direction.
							break;
						}
					}
				}
			}
		}
	}

	public boolean isValidMove(Move move) {

		if ((this.currentTurn != move.player)
				|| (this.board[move.row][move.col] != empty)) {
			return false;
		}

		// Check in all directions
		for (int deltaCol = -1; deltaCol <= 1; deltaCol++) {
			for (int deltaRow = -1; deltaRow <= 1; deltaRow++) {

				// Skip the center of the square.
				if ((deltaRow == 0) && (deltaCol == 0)) {
					continue;
				}
				// Check for one of the other players pieces.
				if (onBoard(move.row + deltaRow, move.col + deltaCol)
						&& (this.board[move.row + deltaRow][move.col + deltaCol] == (move.player * -1))) {
					// Found other players piece, now we need to check if there
					// is one of our pieces somewhere after theirs.
					for (int i = 1; onBoard(move.row + (deltaRow * i), move.col + (deltaCol * i)); i++) {
						if (this.board[move.row + (deltaRow * i)][move.col + (deltaCol * i)] == State.empty) {
							continue;
						}
						if (this.board[move.row + (deltaRow * i)][move.col + (deltaCol * i)] == move.player) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public LinkedList<Move> getValidMoves() {
		LinkedList<Move> movesList = new LinkedList<Move>();
		Move tmpMove;

		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {
				tmpMove = new Move(row, col, this.currentTurn);

				if (this.isValidMove(tmpMove)) {
					movesList.add(tmpMove);
				}
			}
		}
		return movesList;
	}
	
	public boolean playerHasMove(int player) {

		Move tmpMove;

		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {
				tmpMove = new Move(row, col, player);

				if (this.isValidMove(tmpMove)) {
					return true;
				}
			}
		}

		return false;
	}
	
	public boolean isNotGameOver() {
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {
				if (this.isValidMove(new Move(row, col, State.black))
						|| this.isValidMove(new Move(row, col, State.white))) {
					return true;
				}
			}
		}
		return false;
	}

	// Helper function to make it easier to copy a board into a new board array.
	private void copyBoard(int[][] in, int[][] out) {
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				out[i][j] = in[i][j];
			}
		}
	}
	
	public void switchTurn() {
		this.currentTurn *= -1;
	}

	public int getPlayerScore(int p) {

		int score = 0;

		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {

				if (this.board[row][col] == p) {
					score++;
				}
			}
		}

		return score;
	}

	// Level 2 Black Strategy.
	public Move getMaxMove(int player) {

		LinkedList<Move> moves = this.getValidMoves();
		State tmpState;
		Move highMove = null, tmpMove;
		int highScore = Integer.MIN_VALUE;
		int tmpScore;

		Iterator<Move> iter = moves.iterator();
		while (iter.hasNext()) {
			tmpMove = iter.next();
			tmpState = new State(this, tmpMove);
			tmpScore = tmpState.getPlayerScore(player);

			if (tmpScore > highScore) {
				highScore = tmpScore;
				highMove = tmpMove;
			}

		}

		return highMove;
	}
	
	private boolean onBoard(int row, int col) {
		
		if ((row >= 0) && (row < boardSize) && (col >= 0) && (col < boardSize)) {
			return true;
		}
		
		return false;
	}

}
