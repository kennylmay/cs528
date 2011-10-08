/**
 * 
 */

/**
 * @author kresss
 *
 */
public class reversi {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello World!");

		State happy = new State(State.startBoard);
		System.out.print(happy.toString());
		
		System.out.println();
		
		State happy2 = new State(happy, new Move('A',1,State.black));
		System.out.print(happy2.toString());
		
		
	}

}
