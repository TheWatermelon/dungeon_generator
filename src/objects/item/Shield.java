package objects.item;

import java.awt.Point;
import java.util.Random;

import tiles.*;
import engine.Resources;
import objects.effect.*;

public class Shield extends Equipement {	
	public Shield() {
		this.val=0;
		this.maxDurability = -1;
		this.resetDurability();
		this.description = Resources.getEquipementAt(0);
		this.effect = new EffectNormal();
	}
	
	public Shield(int x, int y) {
		this.pos = new Point(x, y);
		this.val=pickVal(5);
		this.maxDurability = val*10;
		this.resetDurability();
		this.effect = pickEffect();
		if(effect instanceof EffectNormal) 
		{ this.description = Resources.getEquipementAt(val)+" Shield"; } 
		else { this.description = effect.name()+" "+Resources.getEquipementAt(val)+" Shield"; }
	}
	
	public Shield(int x, int y, int v) {
		this.pos = new Point(x, y);
		this.val=v;
		this.maxDurability = val*10;
		this.resetDurability();
		this.effect = pickEffect();
		if(effect instanceof EffectNormal) 
		{ this.description = Resources.getEquipementAt(val)+" Shield"; } 
		else { this.description = effect.name()+" "+Resources.getEquipementAt(val)+" Shield"; }
	}
	
	public Shield(int x, int y, int val, boolean isEquiped, int itemDur, int itemMaxDur, Effect e) {
		this.pos = new Point(x, y);
		this.val = val;
		this.durability = itemDur;
		this.maxDurability = itemMaxDur;
		this.effect = e;
		this.isEquiped = isEquiped;
		if(effect instanceof EffectNormal) 
		{ this.description = Resources.getEquipementAt(val)+" Shield"; } 
		else { this.description = effect.name()+" "+Resources.getEquipementAt(val)+" Shield"; }
	}
	
	@Override
	public Tile getTile() {
		return TileFactory.getInstance().createTileShield();
	}
	
	@Override
	public Effect pickEffect() {
		Random rnd = new Random();
		int effectChance=rnd.nextInt(9);
		
		if(effectChance<2) {
			return new EffectWeak();
		} else if(effectChance==2) {
			return new EffectHeal();
		} else if(effectChance==3) {
			return new EffectHeavy();
		} else if(effectChance==4) {
			return new EffectStrong();
		} else if(effectChance==5) {
			return new EffectToughness();
		}
		return new EffectNormal();
	}
	
	@Override
	public boolean isStackable() {
		return false;
	}
}
