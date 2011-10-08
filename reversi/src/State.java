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

    static public int[][] startBoard = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 1, -1, 0, 0, 0 }, { 0, 0, 0, -1, 1, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0 } };
	
	@Override
	public String toString() {
		String output = "  1 2 3 4 5 6 7 8\n";
		int charRow = 'A';
		for(int i=0;i<boardSize;i++){
			output += Character.toString((char) charRow) + " "; 
			for(int j=0;j<boardSize;j++){
				if (board[i][j] == State.empty) {
					output += "  ";
				} else if (board[i][j] == State.black) {
					output += "x ";
				} else if (board[i][j] == State.white) {
					output += "o ";
				}
			}
			output += "\n";
			charRow ++;
		}
		
		return output;
		
	}
	
	State(int[][] b) {
		
		// Copy the board we were given into our board.
		this.board = new int [boardSize][boardSize];
		copyBoard(b, this.board);
	}
	
	State(State previous, Move move) {

		// Copy the board we were given into our board.
		this.board = new int [boardSize][boardSize];
		copyBoard(previous.board, this.board);
		// Apply the move to our new board.
		this.board[move.row][move.col] = move.player; 
		
		
	}
	
	
	
	// Helper function to make it easier to copy a board into a new board array.
 	private void copyBoard(int[][] in, int[][] out) {
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				out[i][j] = in[i][j];
			}
		}
	}
}
