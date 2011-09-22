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

	 State(int lcann, int rcann, int lmiss, int rmiss, String bt){
		  rightCannibals = rcann;
		  leftCannibals = lcann;
		  rightMissionaries = rmiss;
		  leftMissionaries = lmiss;
		  boat = bt;
	 }
	  
	 void moveOneMissionary(){
		if (this.boat == "R"){
			this.rightMissionaries--;
			this.leftMissionaries++;
		}else{
			this.rightMissionaries++;
			this.leftMissionaries--;
		}
	 }
	 
	 void moveTwoMissionaries(){
		 if (this.boat == "R"){
			this.rightMissionaries-=2;
			this.leftMissionaries+=2;
		 }else{
			this.rightMissionaries-=2;
			this.leftMissionaries+=2;
		 }
	 }
	 
	 void moveOneCannabal(){
		 if (this.boat == "R"){
			this.rightCannibals--;
			this.leftCannibals++;
		 }else{
			this.rightCannibals++;
			this.leftCannibals--;
		 }
	 }
	 
	 void moveTwoCannabals(){
		 if (this.boat == "R"){
			this.rightCannibals-=2;
			this.leftCannibals+=2;
		 }else{
			this.rightCannibals-=2;
			this.rightCannibals+=2;
		 }
	 }
	 
	 void moveOneCannabelOneMissionary(){
		 if (this.boat == "R"){
			this.rightMissionaries--;
			this.leftMissionaries++;
			this.rightCannibals--;
			this.leftCannibals++;
		 }else{
			this.rightMissionaries++;
			this.leftMissionaries--;
			this.rightCannibals++;
			this.leftCannibals--;
		 }
	 }
	  
	 /*
	  * 
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
      
      boolean newLeaf(Queue<State> queue, State state){
    	  
    	  if (state.boat == "R"){
    		// Add all possible numbers to the queue if the boat is on the right side
    	  }else{
    		// Add all possible numbers to the queue if the boat is on the left side
    	  }
    	  
    	  return false;
    	  
      }
  }
  

   DepthFirstSearch(){


	   State state = new State(0, 0, 0, 0, "L");	    
	   
	   
   }
}
