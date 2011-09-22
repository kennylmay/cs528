import java.util.LinkedList;


class State {
	//Total missionaries in the problem. Default is 3.
	static int totalMissionaries = 3;
	
	//Total cannibals in the problem. Default is 3.
	static int totalCannibals = 3;

	//Boat capacity. Default is 2.
	static int boatCapacity = 2;
	
	//Set up the type of M & C problem 
	public static void setParameters(int totalMissionaries, int totalCannibals, int boatCapacity) {
		State.totalCannibals = totalCannibals;
		State.totalMissionaries = totalMissionaries;
		State.boatCapacity = boatCapacity;
		
		//Setup the moves based on boat capacity
	    leftMoves = new int[boatCapacity+1];
		for(int i=0; i<= boatCapacity; i++) {
			leftMoves[i] = i;
		}
        rightMoves = new int[boatCapacity+1];
		for(int i=0; i<= boatCapacity; i++) {
			rightMoves[i] = i*-1;
		}
		
	}
	
	//Special "not found" state
	static public State notFound = new State(-1, -1, -1, null);
	
	//Goal state
	static public State goal = new State(0, 0, 0, null);
	
	//Left moves. For the standard missionaries and cannibals problem, this is 0,1,2.
	static public int[] leftMoves = new int[] { 0, 1, 2 };
	
	//Right moves. For the standard missionaries and cannibals problem, this is 0,-1,-2.
	static public int[] rightMoves = new int[] { 0, -1, -2 };
	
	//Constructor.
	//State is encoded as the number of missionaries, cannibals,
	//and boats on the "Starting" side of the problem.
	State(int mis, int can, int boats, State parent) {
		this.mis = mis;
		this.can = can;
		this.boats = boats;
		this.parent = parent;
	}

	//Member variables
	public final int mis;
	public final int can;
	public final int boats;
	public final State parent;


	//Returns true if no missionaries will not be eaten at this state.
	public boolean missionariesAreSafe() {
		//Check the start side
		
		// If there are any missionaries, they must be equal to or greater than
		// the cannibals
		if (mis > 0) {
			if (mis < can) {
				return false;
			}
		}
		
		//Check the other side
		if((totalMissionaries - mis) > 0) {
			if( (totalMissionaries - mis) < (totalCannibals - can)) {
               return false;				
			}
		}
		
		//They are safe.
		return true;
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
	@Override
	public String toString() {
		return Integer.toString(mis) + ", " + Integer.toString(can) + ", "
				+ Integer.toString(boats);
	}

	//Gets the valid children of a state
	public LinkedList<State> successors() {
		LinkedList<State> ret = new LinkedList<State>();

		// Setup moves
		int[] moves;
		if (boats == 1)
			moves = rightMoves;
		else
			moves = leftMoves;
		// For each possible missionary move
		for (int i : moves) {
			// For each possible cannibal move
			for (int j : moves) {
				// Can only move 1 to boatCapcity at a time
				if ((Math.abs(i + j) < 1) || (Math.abs(i + j) > boatCapacity)) {
					continue;
				}
				
				//Can't move missionaries that don't exist
				if ((i + mis < 0 || j + can < 0)
						|| (i + mis > totalMissionaries || j + can > totalCannibals)) {
					continue;
				}
				
				//Create the candidate state. 
				State childState = new State(i + mis, j + can, moves == leftMoves ? 1 : 0, this);
				
				//Check if missionaries are going to die 
				if (childState.missionariesAreSafe()) {
					// Add it as a successor.
					ret.add(childState);
				}
			}
		}
		return ret;
	}
	
	


}