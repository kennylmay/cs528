package NGrams;

public class parseObject{
	String pos;
	String phrase;
	int numOfOcc;
	
	public  parseObject(){
		pos = "";
		phrase = "";
		numOfOcc = 0;
	}
	public  parseObject(String POS, String PHRASE, int NUMOFOCC){
		pos = POS;
		phrase = PHRASE;
		numOfOcc = NUMOFOCC;
	}
	public String toString(){
		
		return this.phrase + " : " + this.numOfOcc + " : "+ this.pos; 
	}
}