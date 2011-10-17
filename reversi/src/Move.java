public class Move {

	public final int row;
	public final int col;
	public final int player;

	public Move(int r, int c, int p) {
		this.row = r;
		this.col = c;
		this.player = p;
	}

	public Move(char r, int c, int p) {

		this.row = (int) r - 'A';
		this.col = c - 1;
		this.player = p;
	}

	@Override
	public String toString() {
		String output = "";

		if (this.player == State.black) {
			output += "X's take ";
		} else {
			output += "O's take ";
		}
		output += Character.toString((char) (this.row + 'A'));
		output += this.col + 1;

		return output;
	}

}
