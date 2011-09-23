import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

public class BestFirstSearch {

	private static BestFirstSearch instance = new BestFirstSearch();
	
	static void exec() {
       
        //Set the starting state
        State start = new State(State.totalMissionaries, State.totalCannibals, 1, null);
        
        //Run the BFS algorithm
  		State s = instance.bfs(start);
  		
  		//Check for not solution not found
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

	State bfs(State start) {
				
		LinkedList<State> open = new LinkedList<State>();
		HashMap<String, State> closed = new HashMap<String, State>();
		HeuristicSort cmp = new HeuristicSort();

		// Add initial state
		open.add(start);
        int visits = 0;
		while (open.size() != 0) {
			// Sort according to the heuristic function
			Collections.sort(open, cmp);

			// Pop front of open
			State n = open.poll();
			System.out.println("Visiting [" + n.toString() + "]");
			visits++;
			// Put it into closed
			closed.put(n.toString(), n);

			// Check if this is the goal
			if (n.equals(State.goal)) {
				System.out.println("BestFirstSearch Found Goal, total nodes visited: " + Integer.toString(visits));
				return n;
			}

			// Get each valid successor
			for (State child : n.successors()) {
				if (!closed.containsKey(child.toString())) {
					//Add to open
					open.addLast(child);
				}
			}
		}
		return State.notFound;
	}

	
	//Heuristic function
	//Very simplistic Heuristic that evaluates the total distance from the goal
	static Integer Heuristic(State s) {
		return s.mis - State.goal.mis + s.can - State.goal.can + s.boats
				- State.goal.boats;
	}

	//Sorting class that implements the heuristic function
	class HeuristicSort implements Comparator<State> {
		@Override
		public int compare(State a, State b) {
			return Heuristic(a).compareTo(Heuristic(b));
		}
	}
}
