import java.util.HashMap;




public class ItemRules {
	
	public HashMap<String,ItemRule> ruleDict = new HashMap<String,ItemRule>();
	
	//////////////////////////////////////////////////////////////
	//Base Rules
	//////////////////////////////////////////////////////////////
	public ItemRule hasHealth = ItemHas("Health");
	public ItemRule hasMana = ItemHas("Mana");
	public ItemRule hasHealthRegen = ItemHas("HealthRegen");
	public ItemRule hasManaRegen = ItemHas("ManaRegen");
	public ItemRule hasArmor = ItemHas("Armor");
	public ItemRule hasMagicResist = ItemHas("Resist");
	public ItemRule hasAttackDamage = ItemHas("Damage");
	public ItemRule hasArmorPen = ItemHas("ArmorPen");
	public ItemRule hasMagicPen = ItemHas("MagicPen");
	public ItemRule hasAbilityPower = ItemHas("AbilityPower");
	public ItemRule hasCritChance = ItemHas("Critical");
	public ItemRule hasMovementSpeed = ItemHas("Movement");
	public ItemRule hasLifeSteal = ItemHas("LifeSteal");
	public ItemRule hasSpellVamp = ItemHas("SpellVamp");
	public ItemRule hasDodge = ItemHas("SpellVamp");
	public ItemRule hasCDR = ItemHas("CDR");
	public ItemRule hasTenacity = ItemHas("Tenacity");
	public ItemRule hasAttackSpeed = ItemHas("AttackSpeed");
	
	
    /////////////////////////////////////////////////////////////
	//Higher order rules
	/////////////////////////////////////////////////////////////
	
	public ItemRule isMage = new OrItemRule(hasMana, hasManaRegen, hasSpellVamp, hasMagicPen, hasMana, hasCDR, hasAbilityPower);
	public ItemRule isFighter = new OrItemRule(hasAttackDamage, hasCritChance, hasAttackSpeed, hasTenacity);
	public ItemRule isSupport = new OrItemRule(hasHealth, hasMana, hasMagicResist, hasTenacity, hasManaRegen, hasHealthRegen, hasCDR);
	public ItemRule isOffensive = new OrItemRule(hasAttackDamage, hasCritChance, hasAttackSpeed,hasMagicPen, hasCDR, hasAbilityPower);
	public ItemRule isDefensive = new OrItemRule(hasHealth, hasArmor, hasMagicResist, hasDodge, hasTenacity);
	public ItemRule isSustain = new OrItemRule(hasHealthRegen, hasLifeSteal, hasSpellVamp);
	public ItemRule isTanky = new OrItemRule(isSustain, isDefensive);
	

	
	public ItemRules() {
	  //Put high order rules into the dictionary. Most of the base rules are already added via
	  // the ItemHas function.
      ruleDict.put("Tanky", isTanky);
      ruleDict.put("Mage", isMage);
      ruleDict.put("Fighter", isFighter);
      ruleDict.put("Support", isSupport);
      ruleDict.put("Offensive", isOffensive);
      ruleDict.put("Defensive", isDefensive);
      ruleDict.put("Sustain", isSustain);
      
	}

	
	public ItemRule Not(ItemRule r) {
		return new ItemRule().new _Not(r);
	}
	public ItemRule ItemHas(String attributeName) {
		ItemRule._ItemHas tmp = new ItemRule().new _ItemHas(attributeName);
		
		ItemRule ret = new ItemRule(tmp, attributeName);
		
		//Lazy shortcut, add this to the dictionary 
		ruleDict.put(attributeName, ret);
		
		return ret;
	}
	
}
