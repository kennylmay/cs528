import java.util.*;

/**
 * @author kenny
 * 
 */
public class DepthFirstSearch {

	class State {
		int rightCannibals;
		int rightMissionaries;
		int leftCannibals;
		int leftMissionaries;
		String boat;

		State(int lcann, int lmiss, int rcann, int rmiss, String bt) {
			rightCannibals = rcann;
			leftCannibals = lcann;
			rightMissionaries = rmiss;
			leftMissionaries = lmiss;
			boat = bt;
		}

		State moveOneMissionary() {
			State state;
			if (this.boat == "R") {
				state = new State(this.leftCannibals,
						this.leftMissionaries + 1, this.rightCannibals,
						this.rightMissionaries - 1, "L");
			} else {
				state = new State(this.leftCannibals,
						this.leftMissionaries - 1, this.rightCannibals,
						this.rightMissionaries + 1, "R");
			}
			return state;
		}

		State moveTwoMissionaries() {
			State state;
			if (this.boat == "R") {
				state = new State(this.leftCannibals,
						this.leftMissionaries + 2, this.rightCannibals,
						this.rightMissionaries - 2, "L");
			} else {
				state = new State(this.leftCannibals,
						this.leftMissionaries - 2, this.rightCannibals,
						this.rightMissionaries + 2, "R");
			}
			return state;
		}

		State moveOneCannabal() {
			State state;
			if (this.boat == "R") {
				state = new State(this.leftCannibals + 1,
						this.leftMissionaries, this.rightCannibals - 1,
						this.rightMissionaries, "L");
			} else {
				state = new State(this.leftCannibals - 1,
						this.leftMissionaries, this.rightCannibals + 1,
						this.rightMissionaries, "R");
			}
			return state;
		}

		State moveTwoCannabals() {
			State state;
			if (this.boat == "R") {
				state = new State(this.leftCannibals + 2,
						this.leftMissionaries, this.rightCannibals - 2,
						this.rightMissionaries, "L");
			} else {
				state = new State(this.leftCannibals - 2,
						this.leftMissionaries, this.rightCannibals + 2,
						this.rightMissionaries, "R");
			}
			return state;
		}

		State moveOneCannabelOneMissionary() {
			State state;
			if (this.boat == "R") {
				state = new State(this.leftCannibals + 1,
						this.leftMissionaries + 1, this.rightCannibals - 1,
						this.rightMissionaries - 1, "L");
			} else {
				state = new State(this.leftCannibals - 1,
						this.leftMissionaries - 1, this.rightCannibals + 1,
						this.rightMissionaries + 1, "R");
			}
			return state;
		}

		/*
	  * 
	  */
		boolean isValid() {
			// If we have no violated any rules then we can push a new state
			// onto the queue
			if ((this.rightCannibals <= this.rightMissionaries || this.rightMissionaries == 0)
					&& (this.leftCannibals <= this.leftMissionaries || this.leftMissionaries == 0)
					&& (this.rightCannibals >= 0)
					&& (this.rightMissionaries >= 0)
					&& (this.leftCannibals >= 0)
					&& (this.leftMissionaries >= 0)) {
				return true;
			}

			return false;
		}

		/*
		 * Function used to see if the solution has been found.
		 */
		boolean isSolution() {
			if (this.leftCannibals == 0 && this.leftMissionaries == 0
					&& this.boat == "R") {
				return true;
			} else {
				return false;
			}
		}

		State newNode() {
			State state;

			state = moveOneMissionary();
			if (state.isValid()) {
				return state;
			}

			state = moveTwoMissionaries();
			if (state.isValid()) {
				return state;
			}

			state = moveOneCannabal();
			if (state.isValid()) {
				return state;
			}

			state = moveTwoCannabals();
			if (state.isValid()) {
				return state;
			}

			state = moveOneCannabelOneMissionary();
			if (state.isValid()) {
				return state;
			}
			
		  return null;
		}

		private boolean isUnique(State state, Stack<State> stack) {
			int x = 0;
			while (!isEqual(state, stack.elementAt(x))) {
				if (isEqual(state, stack.elementAt(x))) {
					return false;
				}
				x++;
			}
			return true;
		}

		private boolean isEqual(State state1, State state2) {
			if ((state1.leftCannibals == state2.leftCannibals)
					&& (state1.leftMissionaries == state2.leftMissionaries)
					&& (state1.rightCannibals == state2.rightCannibals)
					&& (state1.rightMissionaries == state2.rightMissionaries)
					&& (state1.boat == state2.boat)) {
				return true;
			}
			return false;

		}

		public String toString(State state) {
			String text = state.leftCannibals + " " + state.leftMissionaries
					+ " " + state.boat;
			return text;

		}
	}

	DepthFirstSearch() {

		Stack<State> stack = new Stack<State>();
		State state = new State(3, 3, 0, 0, "L");
		stack.add(state);

		while (stack.peek().isValid()) {
			System.out.println(state.toString(stack.peek()));
			if (stack.peek().isSolution()) {
				System.out.println("Found Solution");
			} else {
				stack.add(state.newNode());
			}
			if (stack.isEmpty()) {
				System.out.println("No Solution Found");
			}

			state = stack.firstElement();
			stack.remove(0);
		}
	}
}
