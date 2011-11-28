package chatbot;

import java.util.HashMap;

import opennlp.tools.parser.Parse;

//This is the response interface. It exists so that we can pair
//functions to text or POS tag patterns in a table like form
//
//Parameters:
//  Parse p - 
//    this is the top level parse for the user sentence. 
//  HashMap<String,Object>  context -
//    this is a free form structure that can contain context
//    attributes such as mood, past conversation facts, etc. It is
//    optionally read and updated by response()
public interface Response {
	public String response(Parse p, HashMap<String,Object> context);
}


