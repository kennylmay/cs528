package NGrams;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import chatbot.Global;

public class POSDividedWordNGram extends WordNGram {
	final String pos;
	POSDividedWordNGram(String pos) {
		this.pos = pos;
	}
	

	@Override
	public void ProcessLine(String line) {
		if(line == null || line.length() == 0) 
			return;
		
		//Check if the line matches the POS segment
		Parse[] topParse = ParserTool.parseLine(line, Global.parser, 3);
		if(topParse == null || topParse.length == 0) 
			return;
		
		
		if(topParse[0].getChildren().length == 0) {
			return;
		}
		
		for(Parse child : topParse[0].getChildren()) {
			if(!child.getType().equalsIgnoreCase(pos)) {
				//Not the right POS type. ignore.
				return;
			}
		}
		
		//Pass: Do default processing
		super.ProcessLine(line);
		
	}
}