import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;


public class Expert {

    public static LinkedList<String> countersFor(String attr) {

    	LinkedList<String> attrs = new LinkedList<String>();
    	if(attr.equals("AbilityPower")) {
    		attrs.add("Resist");
    		attrs.add("Health");
   
    	}
    	else if(attr.equals("Damage")) {
    		attrs.add("Armor");
    		attrs.add("Health");
    	}
    	else if(attr.equals("Critical")) {
    		attrs.add("Armor");
    		attrs.add("Health");
    	}
    	else if(attr.equals("AttackSpeed")) {
    		attrs.add("Armor");
    		attrs.add("Health");
    	}
    	else if(attr.equals("Lifesteal")) {
    	    attrs.add("Attack");
    		attrs.add("Armor");
    		attrs.add("AbilityPower");
    		attrs.add("AttackSpeed");
    	}
    	else if(attr.equals("SpellVamp")) {
    	    attrs.add("Attack");
    		attrs.add("Resist");
    		attrs.add("AbilityPower");
    		attrs.add("AttackSpeed");
    	}
    	else if(attr.equals("CDR")) {
    		attrs.add("Armor");
    		attrs.add("Resist");
    		attrs.add("Health");
    	}
    	else if(attr.equals("Armor")) {
    		attrs.add("ArmorPen");
    		attrs.add("AbilityPower");
    		attrs.add("Attack");
    	}
    	else if(attr.equals("Resist")) {
    		attrs.add("MagicPen");
    		attrs.add("AbilityPower");
    		attrs.add("Attack");
    	}
    	else if(attr.equals("Tanky")) {
    		attrs.add("MagicPen");
    		attrs.add("ArmorPen");
    		attrs.add("AbilityPower");
    		attrs.add("Damage");
    		attrs.add("AttackSpeed");
    		attrs.add("Critical");
    	}
    	else if(attr.equals("Mage")) {
    		attrs.add("Resist");
    		attrs.add("Movement");
    		attrs.add("Health");
    	}
    	else if(attr.equals("Fighter")) {
    		attrs.add("Armor");
    		attrs.add("Resist");
    		attrs.add("Health");
    		attrs.add("ArmorPen");
    		attrs.add("MagicPen");
    	}
    	else if(attr.equals("Support")) {
    		attrs.add("Attack");
    		attrs.add("Critical");
    		attrs.add("Damage");
    		attrs.add("AblityPower");
    		attrs.add("Lifesteal");
    	}
    	    	
    	return attrs;
    }

}

