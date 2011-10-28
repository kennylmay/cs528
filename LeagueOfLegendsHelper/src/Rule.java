import java.util.LinkedList;


public class Rule {
	public LinkedList<Rule> subRules = new LinkedList<Rule>();
	public Fact fact;
	
	//a  is true if all it's subrules are true
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
	private Rule() {
		
	}
	
	public Rule(Rule ... theSubRules) {
		for(Rule r: theSubRules) {
			subRules.add(r);
		}
	}
	
	
	public Rule(Fact f) {
		fact = f;
	}
	
	//Facts
	public _ItemHas ItemHas(String attributeName) {
		return new _ItemHas(attributeName);
	}
	
	
	public class _ItemHas implements Fact {

		private  String attributeName;
		@Override
		public boolean eval(LoLItem item) {
			return item.containsKey(attributeName);
		}
		public _ItemHas(String attributeName) {
			this.attributeName = attributeName;
		}

	}
	//////////////////////////////////////////////////////////////
	//Base Rules
	//////////////////////////////////////////////////////////////
	Rule hasHealth = new Rule(ItemHas("Health"));
	Rule hasMana = new Rule(ItemHas("Mana"));
	Rule hasHealthRegen = new Rule(ItemHas("HealthRegen"));
	Rule hasManaRegen = new Rule(ItemHas("ManaRegen"));
	Rule hasArmor = new Rule(ItemHas("Armor"));
	Rule hasMagicResist = new Rule(ItemHas("Resist"));
	Rule hasAttackDamage = new Rule(ItemHas("Damage"));
	Rule hasAbilityPower = new Rule(ItemHas("AbilityPower"));
	Rule hasCritChance = new Rule(ItemHas("Critical"));
	Rule hasMovementSpeed = new Rule(ItemHas("Movement"));
	Rule hasLifeSteal = new Rule(ItemHas("LifeSteal"));
	Rule hasSpellVamp = new Rule(ItemHas("SpellVamp"));
	Rule hasDodge = new Rule(ItemHas("SpellVamp"));
	
	
	
	

	
	
	
	
}
