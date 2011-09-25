import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author kenny
 * 
 */

public class DepthFirstSearch {

	private static DepthFirstSearch instance = new DepthFirstSearch();

	static void exec() {

		// Set the starting state
		State start = new State(State.totalMissionaries, State.totalCannibals,
				1, null);

		// Run the DFS algorithm
		State s = instance.dfs(start);

		// Check for solution not found
		if (s == State.notFound) {
			System.out.println("Not found.");
			return;
		}

		// A good solution was found. Get the path
		LinkedList<State> path = s.getPath();

		// Print the path
		for (State p : path) {
			System.out.println(p.toString());
		}
	}

	State dfs(State start) {

		LinkedList<State> open = new LinkedList<State>();
		HashMap<String, State> closed = new HashMap<String, State>();

		// Add initial state
		open.add(start);
		int visits = 0;
		while (open.size() != 0) {

			// Pop front of open
			State n = open.poll();
			System.out.println("Visiting [" + n.toString() + "]");
			visits++;
			// Put it into closed
			closed.put(n.toString(), n);

			// Check if this is the goal
			if (n.equals(State.goal)) {
				System.out
						.println("DepthFirstSearch Found Goal, total nodes visited: "
								+ Integer.toString(visits));
				return n;
			}

			// Get each valid successor
			for (State child : n.successors()) {
				if (!closed.containsKey(child.toString())) {
					// Add to open
					open.addFirst(child);
				}
			}
		}
		return State.notFound;
	}

}
