import java.util.LinkedList;


public class ItemRule {
	public LinkedList<ItemRule> subRules = new LinkedList<ItemRule>();
	public ItemBaseRule baseRule;
	
	//A rule is true if all it's subrules are true
	boolean eval(LoLItem item) {
	  for(ItemRule g: subRules) {
		  if(g.eval(item) == false) {
			  return false;
		  }
	  }
	  
	  //If no subrules, then this is a base case. Check the fact.
	  if(subRules.size() == 0) {
		  return baseRule.eval(item);
	  }
	  return true;
	}
	
	
	//Rules can only be constructed in two ways: with a set of subrules or with a base rule.
	public ItemRule() {
		
	}
	
	public ItemRule(ItemRule ... theSubRules) {
		for(ItemRule r: theSubRules) {
			subRules.add(r);
		}
	}
	
	public ItemRule(ItemBaseRule f) {
		baseRule = f;
	}

	
    //Base Rules
	public class _ItemHas implements ItemBaseRule {
		private  String attributeName;
		@Override
		public boolean eval(LoLItem item) {
			return item.containsKey(attributeName);
		}
		public _ItemHas(String attributeName) {
			this.attributeName = attributeName;
		}
	}
	

	public class _Not extends ItemRule {
		private ItemRule r;
		public _Not(ItemRule r) {
			this.r = r;
		}
		
		public boolean eval(LoLItem item) {
			//Flip the value of r.eval
			return !r.eval(item);
		}
	}
	
	
}
