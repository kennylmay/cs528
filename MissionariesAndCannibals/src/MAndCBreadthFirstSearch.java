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
	}
	
	private Queue<DestinationShoreState> searchTree = new LinkedList<DestinationShoreState>();
	
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
				if (isValidState(moveOneCan))
				{
					searchTree.add(moveOneCan);
				}
				if (isValidState(moveTwoCan))
				{
					searchTree.add(moveTwoCan);
				}
				if (isValidState(moveOneMis))
				{
					searchTree.add(moveOneMis);
				}
				if (isValidState(moveTwoMis))
				{
					searchTree.add(moveTwoMis);
				}
				if (isValidState(moveOneEach))
				{
					searchTree.add(moveOneEach);
				}
			}
			
			if (searchTree.isEmpty())
			{
				System.out.println("Failure");
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
		return state.missionaries >= state.cannibals;
	}
	
	private boolean isGoalState(DestinationShoreState state)
	{
		return (state.cannibals == 3 && 
				state.missionaries == 3 &&
				state.boat == 1);
	}
	
	private DestinationShoreState moveOneCannibal(DestinationShoreState initialState)
	{
		DestinationShoreState returnState = new DestinationShoreState();
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
		DestinationShoreState returnState = new DestinationShoreState();
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
		DestinationShoreState returnState = new DestinationShoreState();
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
		DestinationShoreState returnState = new DestinationShoreState();
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
		DestinationShoreState returnState = new DestinationShoreState();
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
