import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

class State {
	static int totalMissionaries;
	static int totalCannibals;

	State(int mis, int can, int boats, State parent) {
		this.mis = mis;
		this.can = can;
		this.boats = boats;
		this.parent = parent;
	}

	public final int mis;
	public final int can;
	public final int boats;
	public final State parent;

	public boolean isValid() {
		// If there are any missionaries, they must be equal to or greater than
		// the cannibals
		if (mis > 0) {
			if (mis < can) {
				return false;
			}
		}
		return true;
	}

	public State otherSide() {
		return new State(totalMissionaries - mis, totalCannibals - can,
				Math.abs(boats - 1), null);
	}

	//Used to check for equality
	@Override
	public boolean equals(Object arg0) {
		State b = (State) arg0;
		return mis == b.mis && can == b.can && boats == b.boats;
	}

	//Used to rebuild the path to a state
	public LinkedList<State> getPath() {
		LinkedList<State> ret = new LinkedList<State>();
		ret.addFirst(this);
		State p = this.parent;
		while (p != null) {
			ret.addFirst(p);
			p = p.parent;
		}
		return ret;
	}

    //Used for encoding the solution
	public String toString() {
		return Integer.toString(mis) + ", " + Integer.toString(can) + ", "
				+ Integer.toString(boats);
	}

	//Gets the valid children of a state
	public LinkedList<State> successors() {
		LinkedList<State> ret = new LinkedList<State>();

		// Get the boat direction
		int[] moves;
		if (boats == 1)
			moves = rightMoves;
		else
			moves = leftMoves;
		// Can move 0-2 mis
		for (int i : moves) {
			// Can move 0-2 can
			for (int j : moves) {
				// Can only move 1 or 2 at a time
				if ((Math.abs(i + j) < 1) || (Math.abs(i + j) > 2)) {
					continue;
				}
				if ((i + mis < 0 || j + can < 0)
						|| (i + mis > totalMissionaries || j + can > totalCannibals)) {
					continue;
				}
				State childState = new State(i + mis, j + can, moves == leftMoves ? 1 : 0, this);
				if (childState.isValid() && childState.otherSide().isValid()) {
					ret.add(childState);
				}
			}
		}
		return ret;

	}

	//Constants 
	static public State notFound = new State(-1, -1, -1, null);
	static public State goal = new State(0, 0, 0, null);
	static public int[] leftMoves = new int[] { 0, 1, 2 };
	static public int[] rightMoves = new int[] { 0, -1, -2 };
}

public class BestFirstSearch {

	void exec(int mis, int can, int boats) {
		State s = bfs(mis, can, boats);
		if (s == State.notFound) {
			System.out.println("Not found.");
			return;
		}
		LinkedList<State> path = s.getPath();

		for (State p : path) {
			System.out.println(p.toString());
		}
	}

	State bfs(int mis, int can, int boats) {
		State.totalMissionaries = mis;
		State.totalCannibals = can;

		LinkedList<State> open = new LinkedList<State>();
		HashMap<String, State> closed = new HashMap<String, State>();
		HeuristicSort cmp = new HeuristicSort();

		// Add initial state
		open.add(new State(mis, can, boats, null));

		while (open.size() != 0) {
			// Sort according to the heuristic function
			Collections.sort(open, cmp);

			// Pop front of open
			State n = open.poll();

			// Put it into closed
			closed.put(n.toString(), n);

			// Check if this is the goal
			if (n.equals(State.goal)) {
				System.out.println("Found Goal");
				return n;
			}

			// Get each valid successor
			for (State child : n.successors()) {
				if (!closed.containsKey(child.toString())) {

					open.add(child);
				}
			}
		}
		return State.notFound;
	}

	static Integer Heuristic(State s) {
		return s.mis - State.goal.mis + s.can - State.goal.can + s.boats
				- State.goal.boats;
	}

	class HeuristicSort implements Comparator<State> {
		@Override
		public int compare(State a, State b) {
			return Heuristic(a).compareTo(Heuristic(b));
		}
	}
}
