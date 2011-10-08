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

}
