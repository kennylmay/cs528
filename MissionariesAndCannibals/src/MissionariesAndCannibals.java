
public class MissionariesAndCannibals {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		//Set the parameters for total missionaries, total cannibals, and boat capacity. 
        State.setParameters(3, 3, 2);

        System.out.println("\nStarting Best First Search");
        //Run best first search
		BestFirstSearch.exec();
		
		//Run depth first search
		System.out.println("\nStarting Depth First Search");
		DepthFirstSearch.exec();
		
		//Run iterative depth first search
		System.out.println("\nStarting Iterative Depth First Search");
		IterativeDepthFirstSearch.exec();
		
		//Run breadth first search
		System.out.println("\nStarting Breadth First Search");
		BreadthFirstSearch.exec();
				
		System.out.println("\nStarting Breadth First Search 2");
		BreadthFirstSearch2 searcher = new BreadthFirstSearch2();
		searcher.PerformSearch();
	}

}
