import java.util.Queue;

/**
 * @author kenny
 *
 */
public class DepthFirstSearch {
  
	
  class State{
	  int rightCannibals;
	  int rightMissionaries;
	  int leftCannibals;
	  int leftMissionaries;
	  String boat;

	  /*
	   * Defines the state that the missionaries and cannibals can be in.
	   */
	  State(int lcann, int rcann, int lmiss, int rmiss, String bt){
		  rightCannibals = rcann;
		  leftCannibals = lcann;
		  rightMissionaries = rmiss;
		  leftMissionaries = lmiss;
		  boat = bt;
	  }
	 /*
	  * Function used to add a new leaf to the tree, but only if its valid.
	  */
	  void testAddState(Queue<State> queue, State state){
		  // If we have no violated any rules then we can push a new state onto the queue
		  if ((state.rightCannibals <= state.rightMissionaries) && (state.leftCannibals <= state.leftMissionaries) &&
			  (state.rightCannibals >= 0) && (state.rightMissionaries >= 0)){
		  }
	  }
	  /*
	   * Function used to see if the solution has been found.
	   */
      boolean isSolution(State state){
    	  if(state.leftCannibals == 3 && state.leftMissionaries == 3 && state.boat == "R"){
    		  return true;
    	  }else{
    		  return false;
    	  }
      }
      
      /*
       * Function used to add all possible leaves on a branch
       */
      boolean newLeaf(Queue<State> queue, State state){
    	  
    	  if (state.boat == "R"){
    		// Add all possible numbers to the queue if the boat is on the right side
    	  }else{
    		// Add all possible numbers to the queue if the boat is on the left side
    	  }
    	  
    	  return false;
    	  
      }
  }
  

  /*
   * Actual algorithm used to search for a correct state.
   */
   DepthFirstSearch(){


	   State state = new State(0, 0, 0, 0, "L");	    
	   
	   
   }
}
