import java.util.LinkedList;


public class Rule {
	public LinkedList<Rule> subRules = new LinkedList<Rule>();
	public Fact fact;
	
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

	
    public class OrRule extends Rule {
    	
    	public OrRule(Rule  ... theSubRules) {
    		for(Rule r: theSubRules) {
    			subRules.add(r);
    		}
    	}
    	@Override
    	public boolean eval(LoLItem item) {
    		//Or rules are true if any of the subrules are true
    		  for(Rule g: subRules) {
    			  if(g.eval(item) == true) {
    				  return true;
    			  }
    		  }
    		  return false;
    	}
    	
    	private OrRule() {} ;
    	
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
	
	public Rule Not(Rule r) {
		return new _Not(r);
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
	Rule hasArmorPen = new Rule(ItemHas("ArmorPen"));
	Rule hasMagicPen = new Rule(ItemHas("MagicPen"));
	Rule hasAbilityPower = new Rule(ItemHas("AbilityPower"));
	Rule hasCritChance = new Rule(ItemHas("Critical"));
	Rule hasMovementSpeed = new Rule(ItemHas("Movement"));
	Rule hasLifeSteal = new Rule(ItemHas("LifeSteal"));
	Rule hasSpellVamp = new Rule(ItemHas("SpellVamp"));
	Rule hasDodge = new Rule(ItemHas("SpellVamp"));
	Rule hasCDR = new Rule(ItemHas("CDR"));
	Rule hasTenacity = new Rule(ItemHas("Tenacity"));
	Rule hasAttackSpeed = new Rule(ItemHas("AttackSpeed"));
	
	
    /////////////////////////////////////////////////////////////
	//Higher order rules
	/////////////////////////////////////////////////////////////
	Rule isTanky = new OrRule(hasHealth, hasHealthRegen, hasArmor, hasMagicResist, hasDodge, hasTenacity, hasLifeSteal, hasSpellVamp);
	Rule isMage = new OrRule(hasMana, hasManaRegen, hasSpellVamp, hasMagicPen, hasMana, hasCDR);
	Rule isADCarry = new OrRule(hasAttackDamage, hasCritChance, hasAttackSpeed);
	Rule isBurstMage = new Rule(Not(isADCarry), isMage);
	Rule isTankyMage = new Rule(isTanky, isMage);
	Rule isADAPHybrid = new Rule(isADCarry, isMage);

	
	
}
