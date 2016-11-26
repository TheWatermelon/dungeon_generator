package objects.mob;

import java.awt.Point;
import java.util.Random;

import tiles.TileFactory;

public class Monster extends Mob {
	public Monster(int x, int y, char s, String desc) {
		this.hp = 20;
		this.atk = pickAtk();
		this.def = pickDef();
		this.vit = pickVit();
		this.dead = false;
		this.pos = new Point(x, y);
		this.symbol = s;
		this.description = desc;
		this.floor = TileFactory.getInstance().createTileMonster(this.symbol);
		this.mobTile = TileFactory.getInstance().createTileMonster(this.symbol);
	}
	
	protected int pickAtk() {
		Random rnd = new Random();
		return rnd.nextInt(4)+3;
	}
	
	protected int pickDef() {
		Random rnd = new Random();
		return rnd.nextInt(4);
	}
	
	protected int pickVit() {
		Random rnd = new Random();
		return rnd.nextInt(2);
	}
	
	public void murder() {
		this.dead = true;
		this.mobTile = TileFactory.getInstance().createTileCorpse();
	}
	
	public String fightTurn(Player p) {
		String battleLog="";
		Random rnd = new Random();
		int deg;
		
		deg = rnd.nextInt(3);
		if(deg==0) { 
			battleLog+=this.description+" miss"; 
		} else if(((deg*getAtk())-p.getDef())<=0) {
			p.useShield();
			battleLog+=p.description+" dodged"; 
		} else {
			int dmg = (deg*getAtk())-p.getDef();
			p.hp -= dmg;
			p.useShield();
			if(deg==1) {
				battleLog+=description+" deals "+dmg+" to "+p.description;
			} else if(deg==2) {
				battleLog+=description+" deals !"+dmg+"! to "+p.description;
			}
		}
		return battleLog;
	}
	
	public int getAtk() {
		return this.atk;
	}
	
	public int getDef() {
		return this.def;
	}
}