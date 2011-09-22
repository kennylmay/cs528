import java.util.*;


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
	  
	 State moveOneMissionary(){
		State state;
		if (this.boat == "R"){
			state = new State(this.leftCannibals, this.leftMissionaries + 1, this.rightCannibals, this.rightMissionaries - 1,"L");
		}else{
			state = new State(this.leftCannibals, this.leftMissionaries - 1, this.rightCannibals, this.rightMissionaries + 1,"R");
		}
		return state;
	 }
	 
	 State moveTwoMissionaries(){
		 State state;
		 if (this.boat == "R"){
			 state = new State(this.leftCannibals, this.leftMissionaries + 2, this.rightCannibals, this.rightMissionaries - 2,"L");
		 }else{
			 state = new State(this.leftCannibals, this.leftMissionaries - 2, this.rightCannibals, this.rightMissionaries + 2,"R");
		 }
		 return state;
	 }
	 
	 State moveOneCannabal(){
		 State state;
		 if (this.boat == "R"){
			 state = new State(this.leftCannibals + 1, this.leftMissionaries, this.rightCannibals - 1, this.rightMissionaries,"L");
		 }else{
			 state = new State(this.leftCannibals - 1, this.leftMissionaries, this.rightCannibals + 1, this.rightMissionaries,"R");
		 }
		 return state;
	 }
	 
	 State moveTwoCannabals(){
		 State state;
		 if (this.boat == "R"){
			 state = new State(this.leftCannibals + 2, this.leftMissionaries, this.rightCannibals - 2, this.rightMissionaries,"L");
		 }else{
			 state = new State(this.leftCannibals - 2, this.leftMissionaries, this.rightCannibals + 2, this.rightMissionaries,"R");
		 }
		 return state;
	 }
	 
	 State moveOneCannabelOneMissionary(){
		 State state;
		 if (this.boat == "R"){
			 state = new State(this.leftCannibals + 1, this.leftMissionaries + 1, this.rightCannibals - 1, this.rightMissionaries - 1,"L");
		 }else{
			 state = new State(this.leftCannibals - 1, this.leftMissionaries - 1, this.rightCannibals + 1, this.rightMissionaries + 1,"R");
		 }
		 return state;
	 }
	  
	 
		 /*
	  * 
	  */
	  boolean isValid(){
		  // If we have no violated any rules then we can push a new state onto the queue
		  if ((this.rightCannibals <= this.rightMissionaries) && (this.leftCannibals <= this.leftMissionaries) &&
			  (this.rightCannibals >= 0) && (this.rightMissionaries >= 0) && 
			  (this.leftCannibals >= 0) && (this.leftMissionaries >= 0)){
			  return true;
		  }     	
		  
		  return false;
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

    	  stack.add(moveOneMissionary());
          if (stack.peek().isSolution()){
             return true;
          }
          if (stack.peek().isValid()) {
        	  if (newNode(stack)) {
        		  return true;
        	  }
          }
          
    	  stack.pop();

    	  while(stack.peek().isValid()){
             stack.add(moveTwoMissionaries()); 
    	     if (stack.peek().isSolution()){
         	    return true;
             }
    	  }
    	  stack.pop();
    	  
    	  while(stack.peek().isValid()){
    		 stack.add(moveOneCannabal()); 
     	     if (stack.peek().isSolution()){
          	    return true;
             }
     	  }
     	  stack.pop();
     	  
     	 while(stack.peek().isValid()){
     		 stack.add(moveTwoCannabals()); 
    	     if (stack.peek().isSolution()){
         	    return true;
             }
    	  }
    	  stack.pop();
    	  
    	  while(stack.peek().isValid()){
    		 stack.add(moveOneCannabelOneMissionary()); 
     	     if (stack.peek().isSolution()){
          	    return true;
             }
     	  }
     	  stack.pop();
     	  
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
