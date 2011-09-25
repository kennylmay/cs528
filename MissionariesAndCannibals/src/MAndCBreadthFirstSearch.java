package missionariesAndCannibals;

import java.util.LinkedList;
import java.util.Queue;
import java.lang.Math;

public class MAndCBreadthFirstSearch {

	private class DestinationShoreState
	{
		public int cannibals = 0;
		public int missionaries = 0;
		public int boat = 0;
		
		DestinationShoreState ()
		{
		}
		
		DestinationShoreState (DestinationShoreState state)
		{
			this.cannibals = state.cannibals;
			this.missionaries = state.missionaries;
			this.boat = state.boat;
		}
		
		public boolean equals (Object other)
		{
			if (this == other)
				return true;
			if (!(other instanceof DestinationShoreState))
				return false;
			DestinationShoreState otherState = (DestinationShoreState)other;
			return (this.cannibals == otherState.cannibals &&
					this.missionaries == otherState.missionaries &&
					this.boat == otherState.boat);
		}
	}
	
	private Queue<DestinationShoreState> searchTree = new LinkedList<DestinationShoreState>();
	private LinkedList<DestinationShoreState> triedStates = new LinkedList<DestinationShoreState>();
	
	public static void main(String[] args) {
		MAndCBreadthFirstSearch searcher = new MAndCBreadthFirstSearch();
		searcher.PerformSearch();
	}
	
	public void PerformSearch()
	{
		DestinationShoreState currentSearchState = new DestinationShoreState();
		currentSearchState.cannibals = 0;
		currentSearchState.missionaries = 0;
		currentSearchState.boat = 0;
		triedStates.add(new DestinationShoreState(currentSearchState));
		
		while (currentSearchState != null)
		{
			// If we've reached the goal state, break
			if (isGoalState(currentSearchState))
			{
				break;
			}
			// Otherwise, collect the next row of the search tree
			else
			{
				// Create the next potential nodes
				DestinationShoreState moveOneCan = moveOneCannibal(currentSearchState);
				DestinationShoreState moveTwoCan = moveTwoCannibals(currentSearchState);
				DestinationShoreState moveOneMis = moveOneMissionary(currentSearchState);
				DestinationShoreState moveTwoMis = moveTwoMissionaries(currentSearchState);
				DestinationShoreState moveOneEach = moveOneCannibalAndOneMissionary(currentSearchState);
				
				// Add them to the queue if valid
				if (isValidState(moveOneCan) &&
					!triedStates.contains(moveOneCan))
				{
					searchTree.add(moveOneCan);
					triedStates.add(new DestinationShoreState(moveOneCan));
				}
				if (isValidState(moveTwoCan) &&
						!triedStates.contains(moveTwoCan))
				{
					searchTree.add(moveTwoCan);
					triedStates.add(new DestinationShoreState(moveTwoCan));
				}
				if (isValidState(moveOneMis) &&
						!triedStates.contains(moveOneMis))
				{
					searchTree.add(moveOneMis);
					triedStates.add(new DestinationShoreState(moveOneMis));
				}
				if (isValidState(moveTwoMis) &&
						!triedStates.contains(moveTwoMis))
				{
					searchTree.add(moveTwoMis);
					triedStates.add(new DestinationShoreState(moveTwoMis));
				}
				if (isValidState(moveOneEach) &&
						!triedStates.contains(moveOneEach))
				{
					searchTree.add(moveOneEach);
					triedStates.add(new DestinationShoreState(moveOneEach));
				}
			}
			
			if (searchTree.isEmpty())
			{
				System.out.println("Failure");
				return;
			}
			else
			{
				currentSearchState = searchTree.poll();
				System.out.print("Searching node: ");
				System.out.print(currentSearchState.cannibals);
				System.out.print(", ");
				System.out.print(currentSearchState.missionaries);
				System.out.print(", ");
				System.out.println(currentSearchState.boat);
			}
		}
	}
	
	private boolean isValidState(DestinationShoreState state)
	{
		boolean isValid = true;
		
		if ((state.missionaries > 0 && state.missionaries < state.cannibals) || // Check starting shore
			((3 - state.missionaries) > 0 && (3 - state.missionaries) < (3 - state.cannibals)) || // Check destination shore
			state.missionaries > 3 || state.missionaries < 0 ||
			state.cannibals > 3 || state.cannibals < 0)
		{
			isValid = false;
		}
		
		return isValid;
	}
	
	private boolean isGoalState(DestinationShoreState state)
	{
		return (state.cannibals == 3 && 
				state.missionaries == 3 &&
				state.boat == 1);
	}
	
	private DestinationShoreState moveOneCannibal(DestinationShoreState initialState)
	{
		DestinationShoreState returnState = new DestinationShoreState(initialState);
		// If the boat's on the other shore
		if (initialState.boat == 0)
		{
			// Bring over a cannibal
			returnState.cannibals = initialState.cannibals + 1;
			returnState.boat = 1;
		}
		else
		{
			// Otherwise, send one back
			returnState.cannibals = initialState.cannibals - 1;
			returnState.boat = 0;
		}
		return returnState;
	}

	private DestinationShoreState moveTwoCannibals(DestinationShoreState initialState)
	{
		DestinationShoreState returnState = new DestinationShoreState(initialState);
		// If the boat's on the other shore
		if (initialState.boat == 0)
		{
			// Bring over two cannibals
			returnState.cannibals = initialState.cannibals + 2;
			returnState.boat = 1;
		}
		else
		{
			// Otherwise, send two back
			returnState.cannibals = initialState.cannibals - 2;
			returnState.boat = 0;
		}
		return returnState;
	}
	
	private DestinationShoreState moveOneMissionary(DestinationShoreState initialState)
	{
		DestinationShoreState returnState = new DestinationShoreState(initialState);
		// If the boat's on the other shore
		if (initialState.boat == 0)
		{
			// Bring over a missionary
			returnState.missionaries = initialState.missionaries + 1;
			returnState.boat = 1;
		}
		else
		{
			// Otherwise, send one back
			returnState.missionaries = initialState.missionaries - 1;
			returnState.boat = 0;
		}
		return returnState;
	}
	
	private DestinationShoreState moveTwoMissionaries(DestinationShoreState initialState)
	{
		DestinationShoreState returnState = new DestinationShoreState(initialState);
		// If the boat's on the other shore
		if (initialState.boat == 0)
		{
			// Bring over two missionaries
			returnState.missionaries = initialState.missionaries + 2;
			returnState.boat = 1;
		}
		else
		{
			// Otherwise, send two back
			returnState.missionaries = initialState.missionaries - 2;
			returnState.boat = 0;
		}
		return returnState;
	}
	
	private DestinationShoreState moveOneCannibalAndOneMissionary(DestinationShoreState initialState)
	{
		DestinationShoreState returnState = new DestinationShoreState(initialState);
		// If the boat's on the other shore
		if (initialState.boat == 0)
		{
			// Bring over a cannibal and a missionary
			returnState.cannibals = initialState.cannibals + 1;
			returnState.missionaries = initialState.missionaries + 1;
			returnState.boat = 1;
		}
		else
		{
			// Otherwise, send them back
			returnState.cannibals = initialState.cannibals - 1;
			returnState.missionaries = initialState.missionaries - 1;
			returnState.boat = 0;
		}
		return returnState;
	}
}
