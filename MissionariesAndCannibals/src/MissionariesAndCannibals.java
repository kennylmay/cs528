
public class MissionariesAndCannibals {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		//Set the parameters for total missionaries, total cannibals, and boat capacity. 
        State.setParameters(3, 3, 2);
		
        //Run BFS
		BestFirstSearch.exec();
		
		
	}

}
