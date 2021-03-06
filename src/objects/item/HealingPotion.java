package objects.item;

import java.awt.Color;

import engine.Resources;

public class HealingPotion extends Potion {
	public HealingPotion(int x, int y) {
		super(x, y);
		this.val=1;
		this.description = "Healing Potion";
	}
	
	@Override
	public boolean isEqualTo(Item i) {
		if(i instanceof HealingPotion) { return true; }
		return false;
	}
	
	@Override
	public Color getColor() {
		return Resources.green;
	}
}
