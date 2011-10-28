


public class Rules {
	//////////////////////////////////////////////////////////////
	//Base Rules
	//////////////////////////////////////////////////////////////
	public Rule hasHealth = new Rule(ItemHas("Health"));
	public Rule hasMana = new Rule(ItemHas("Mana"));
	public Rule hasHealthRegen = new Rule(ItemHas("HealthRegen"));
	public Rule hasManaRegen = new Rule(ItemHas("ManaRegen"));
	public Rule hasArmor = new Rule(ItemHas("Armor"));
	public Rule hasMagicResist = new Rule(ItemHas("Resist"));
	public Rule hasAttackDamage = new Rule(ItemHas("Damage"));
	public Rule hasArmorPen = new Rule(ItemHas("ArmorPen"));
	public Rule hasMagicPen = new Rule(ItemHas("MagicPen"));
	public Rule hasAbilityPower = new Rule(ItemHas("AbilityPower"));
	public Rule hasCritChance = new Rule(ItemHas("Critical"));
	public Rule hasMovementSpeed = new Rule(ItemHas("Movement"));
	public Rule hasLifeSteal = new Rule(ItemHas("LifeSteal"));
	public Rule hasSpellVamp = new Rule(ItemHas("SpellVamp"));
	public Rule hasDodge = new Rule(ItemHas("SpellVamp"));
	public Rule hasCDR = new Rule(ItemHas("CDR"));
	public Rule hasTenacity = new Rule(ItemHas("Tenacity"));
	public Rule hasAttackSpeed = new Rule(ItemHas("AttackSpeed"));
	
	
    /////////////////////////////////////////////////////////////
	//Higher order rules
	/////////////////////////////////////////////////////////////
	public Rule isTanky = new OrRule(hasHealth, hasHealthRegen, hasArmor, hasMagicResist, hasDodge, hasTenacity, hasLifeSteal, hasSpellVamp);
	public Rule isMage = new OrRule(hasMana, hasManaRegen, hasSpellVamp, hasMagicPen, hasMana, hasCDR);
	public Rule isADCarry = new OrRule(hasAttackDamage, hasCritChance, hasAttackSpeed);
	public Rule isBurstMage = new Rule(Not(isADCarry), isMage);
	public Rule isTankyMage = new Rule(isTanky, isMage);
	public Rule isADAPHybrid = new Rule(isADCarry, isMage);

	private Rule Not(Rule r) {
		return new Rule().new _Not(r);
	}
	private Rule._ItemHas ItemHas(String attributeName) {
		return new Rule().new _ItemHas(attributeName);
	}
	
}
