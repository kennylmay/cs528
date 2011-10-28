import java.util.LinkedList;


public class Rule {
	public LinkedList<Rule> subRules = new LinkedList<Rule>();
	public BaseRule fact;
	
	//A rule is true if all it's subrules are true
	boolean eval(LoLItem item) {
	  for(Rule g: subRules) {
		  if(g.eval(item) == false) {
			  return false;
		  }
	  }
	  
	  //If no subrules, then this is a base case. Check the fact.
	  if(subRules.size() == 0) {
		  return fact.eval(item);
	  }
	  return true;
	}
	
	
	//Rules can only be constructed in two ways: with a set of subrules or with a fact.
	public Rule() {
		
	}
	
	public Rule(Rule ... theSubRules) {
		for(Rule r: theSubRules) {
			subRules.add(r);
		}
	}
	
	public Rule(BaseRule f) {
		fact = f;
	}

	
    //Base Rules
	public class _ItemHas implements BaseRule {
		private  String attributeName;
		@Override
		public boolean eval(LoLItem item) {
			return item.containsKey(attributeName);
		}
		public _ItemHas(String attributeName) {
			this.attributeName = attributeName;
		}
	}
	

	public class _Not extends Rule {
		private Rule r;
		public _Not(Rule r) {
			this.r = r;
		}
		
		public boolean eval(LoLItem item) {
			//Flip the value of r.eval
			return !r.eval(item);
		}
	}
	
	
}
