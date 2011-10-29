
public class OrItemRule extends ItemRule {
	
	public OrItemRule(ItemRule  ... theSubRules) {
		for(ItemRule r: theSubRules) {
			subRules.add(r);
		}
	}
	@Override
	public boolean eval(LoLItem item) {
		//Or rules are true if any of the subrules are true
		  for(ItemRule g: subRules) {
			  if(g.eval(item) == true) {
				  return true;
			  }
		  }
		  return false;
	}
	
	private OrItemRule() {} ;
	
}