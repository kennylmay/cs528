
import java.util.HashMap;
import java.util.LinkedList;

public class IterativeDepthFirstSearch {

	private static IterativeDepthFirstSearch instance = new IterativeDepthFirstSearch();
	
	static void exec() {
       
        //Set the starting state
        State start = new State(State.totalMissionaries, State.totalCannibals, 1, null);
        
        //Run the IDFS algorithm
  		State s = instance.idfs(start);
  		
  		//Check for solution not found
		if (s == State.notFound) {
			System.out.println("Not found.");
			return;
		}
		
		//A good solution was found. Get the path
		LinkedList<State> path = s.getPath();

		//Print the path
		for (State p : path) {
			System.out.println(p.toString());
		}
	}

	State idfs(State start) {
				
		LinkedList<State> open = new LinkedList<State>();
		HashMap<String, State> closed = new HashMap<String, State>();

		// Current Max Searching Depth
		int curDepth = 1;
		// Overall Max Depth to stop infinite searching.
		int maxDepth = 100;
        
		// Add initial state
		open.add(start);
		int visits = 0;
		
        System.out.println("Searching to a depth of " + curDepth);
        
		while (curDepth <= maxDepth) {

			// Pop first of open
			State n = open.poll();
			System.out.println("Visiting [" + n.toString() + "]");
			visits++;
			// Put it into closed
			closed.put(n.toString(), n);

			// Check if this is the goal
			if (n.equals(State.goal)) {
				System.out.println("IterativeDepthFirstSearch Found Goal, total nodes visited: " + Integer.toString(visits));
				return n;
			}

			// Check how deep this node is before continuing with its successors
			if (n.getPath().size() <= curDepth) {
				// Get each valid successor
				for (State child : n.successors()) {
					if (!closed.containsKey(child.toString())) {
						//Add to begin open se we go depth first.
						open.addFirst(child);
					}
				}
			}
			
			// If we have exhausted the tree at the current max depth then increase how far we will search and reinitialize closed.
			if (open.size() == 0) {
				open.add(start);
				curDepth++;
				closed.clear();
				System.out.println("Searching to a depth of " + curDepth);
			}			

		}
		return State.notFound;
	}
}
