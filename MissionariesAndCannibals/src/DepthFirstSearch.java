import java.util.*;

import DepthFirstSearch.State;


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

	 State(int lcann, int lmiss,int rcann, int rmiss, String bt){
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
			this.boat="L";
		}else{
			this.rightMissionaries++;
			this.leftMissionaries--;
			this.boat="R";
		}
	 }
	 
	 void moveTwoMissionaries(){
		 if (this.boat == "R"){
			this.rightMissionaries-=2;
			this.leftMissionaries+=2;
			this.boat="L";
		 }else{
			this.rightMissionaries-=2;
			this.leftMissionaries+=2;
			this.boat="R";
		 }
	 }
	 
	 void moveOneCannabal(){
		 if (this.boat == "R"){
			this.rightCannibals--;
			this.leftCannibals++;
			this.boat="L";
		 }else{
			this.rightCannibals++;
			this.leftCannibals--;
			this.boat="R";
		 }
	 }
	 
	 void moveTwoCannabals(){
		 if (this.boat == "R"){
			this.rightCannibals-=2;
			this.leftCannibals+=2;
			this.boat="L";
		 }else{
			this.rightCannibals-=2;
			this.rightCannibals+=2;
			this.boat="R";
		 }
	 }
	 
	 void moveOneCannabelOneMissionary(){
		 if (this.boat == "R"){
			this.rightMissionaries--;
			this.leftMissionaries++;
			this.rightCannibals--;
			this.leftCannibals++;
			this.boat="L";
		 }else{
			this.rightMissionaries++;
			this.leftMissionaries--;
			this.rightCannibals++;
			this.leftCannibals--;
			this.boat="R";
		 }
	 }
	  
	 
		 /*
	  * 
	  */
	  void testAddState(Stack<State> stack){
		  // If we have no violated any rules then we can push a new state onto the queue
		  if ((this.rightCannibals <= this.rightMissionaries) && (this.leftCannibals <= this.leftMissionaries) &&
			  (this.rightCannibals >= 0) && (this.rightMissionaries >= 0) && 
			  (this.leftCannibals >= 0) && (this.leftMissionaries >= 0)){
		  }
	  }
	  /*
	   * Function used to see if the solution has been found.
	   */
      boolean isSolution(){
    	  if(this.leftCannibals == 3 && this.leftMissionaries == 3 && this.boat == "R"){
    		  return true;
    	  }else{
    		  return false;
    	  }
      }
      
      boolean newNode(Stack<State> stack){
    	     moveOneMissionary();		 
    		 moveTwoMissionaries(); 
    		 moveOneCannabal();
    		 moveTwoCannabals();
    		 moveOneCannabelOneMissionary();
    	  return false;
    	  
      }
  }
  

   DepthFirstSearch(){

       Stack<State> stack = new Stack<State>();
	   State state = new State(3, 3, 0, 0, "L");
	   stack.add(state);
	   state.newNode(stack);
	   
	   
	   
   }
}
