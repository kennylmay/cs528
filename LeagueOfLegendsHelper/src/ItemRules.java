


public class ItemRules {
	//////////////////////////////////////////////////////////////
	//Base Rules
	//////////////////////////////////////////////////////////////
	public ItemRule hasHealth = new ItemRule(ItemHas("Health"));
	public ItemRule hasMana = new ItemRule(ItemHas("Mana"));
	public ItemRule hasHealthRegen = new ItemRule(ItemHas("HealthRegen"));
	public ItemRule hasManaRegen = new ItemRule(ItemHas("ManaRegen"));
	public ItemRule hasArmor = new ItemRule(ItemHas("Armor"));
	public ItemRule hasMagicResist = new ItemRule(ItemHas("Resist"));
	public ItemRule hasAttackDamage = new ItemRule(ItemHas("Damage"));
	public ItemRule hasArmorPen = new ItemRule(ItemHas("ArmorPen"));
	public ItemRule hasMagicPen = new ItemRule(ItemHas("MagicPen"));
	public ItemRule hasAbilityPower = new ItemRule(ItemHas("AbilityPower"));
	public ItemRule hasCritChance = new ItemRule(ItemHas("Critical"));
	public ItemRule hasMovementSpeed = new ItemRule(ItemHas("Movement"));
	public ItemRule hasLifeSteal = new ItemRule(ItemHas("LifeSteal"));
	public ItemRule hasSpellVamp = new ItemRule(ItemHas("SpellVamp"));
	public ItemRule hasDodge = new ItemRule(ItemHas("SpellVamp"));
	public ItemRule hasCDR = new ItemRule(ItemHas("CDR"));
	public ItemRule hasTenacity = new ItemRule(ItemHas("Tenacity"));
	public ItemRule hasAttackSpeed = new ItemRule(ItemHas("AttackSpeed"));
	
	
    /////////////////////////////////////////////////////////////
	//Higher order rules
	/////////////////////////////////////////////////////////////
	public ItemRule isTanky = new OrItemRule(hasHealth, hasHealthRegen, hasArmor, hasMagicResist, hasDodge, hasTenacity, hasLifeSteal, hasSpellVamp);
	public ItemRule isMage = new OrItemRule(hasMana, hasManaRegen, hasSpellVamp, hasMagicPen, hasMana, hasCDR);
	public ItemRule isADCarry = new OrItemRule(hasAttackDamage, hasCritChance, hasAttackSpeed);
	public ItemRule isBurstMage = new ItemRule(Not(isADCarry), isMage);
	public ItemRule isTankyMage = new ItemRule(isTanky, isMage);
	public ItemRule isADAPHybrid = new ItemRule(isADCarry, isMage);

	private ItemRule Not(ItemRule r) {
		return new ItemRule().new _Not(r);
	}
	private ItemRule._ItemHas ItemHas(String attributeName) {
		return new ItemRule().new _ItemHas(attributeName);
	}
	
}
