
public class MissionariesAndCannibals {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		//Set the parameters for total missionaries, total cannibals, and boat capacity. 
        State.setParameters(3, 3, 2);

        //Run best first search
		BestFirstSearch.exec();
		
		//Run breadth first search
		BreadthFirstSearch.exec();
		
		//Run depth first search
		new DepthFirstSearch();
		
		//TODO: run iteratively deepening depth first search
		
	}

}
