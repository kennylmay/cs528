import java.util.HashMap;
import java.util.LinkedList;


public class BreadthFirstSearch {
private static BreadthFirstSearch instance = new BreadthFirstSearch();
	
	static void exec() {
       
        //Set the starting state
        State start = new State(State.totalMissionaries, State.totalCannibals, 1, null);
        
        //Run the breadth first search algorithm
  		State s = instance.breadth(start);
  		
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

	State breadth(State start) {
				
		LinkedList<State> q = new LinkedList<State>();
		HashMap<String, State> alreadyVisited = new HashMap<String, State>();


		// Add initial state
		q.addLast(start);
		int visits =0;
		while (q.size() != 0) {
			// Pop the first element off of q
			State n = q.poll();
			
			//Add it to the alreadyVisited set
			alreadyVisited.put(n.toString(), n);
			
			System.out.println("Visiting [" + n.toString() + "]");
			visits++;
			// Check if this is the goal
			if (n.equals(State.goal)) {
				System.out.println("BreadthFirstSearch found Goal, total nodes visited: " + Integer.toString(visits));
				return n;
			}
			
			// Get each valid successor
			for (State child : n.successors()) {
				if(alreadyVisited.containsKey(child.toString())) {
					continue;
				}
				//Add to the BACK of the queue
				q.addLast(child);
			}
		}
		return State.notFound;
	}
}
