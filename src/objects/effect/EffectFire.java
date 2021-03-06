package objects.effect;

import java.awt.Color;

import engine.Message;
import engine.Resources;
import objects.mob.Mob;

public class EffectFire extends EffectOther {
	public EffectFire() { super("BRN"); maxDuration=duration=5; this.id=0; }

	@Override
	public String name() { return "Fire"; }
	
	@Override
	public Color getColor() { return Resources.lightOrange; }

	@Override
	public void start(Mob m) {
		affected=m;
		m.setEffect(this);
		maxDuration=duration=5;
	}
	
	@Override
	public void stop() {}

	@Override
	public boolean apply() {
		if(duration>0) {
			affected.hp-=duration;
			duration--;
			affected.getLog().appendMessage(affected+" is burning!", Message.Type.Important);
			if(affected.hp<=0) { affected.murder(); return false; }
		} else {
			affected.getLog().appendMessage(affected+" set off the fire!", Message.Type.Important);
			affected.setEffect(new EffectNormal());
		}
		return true;
	}

}
