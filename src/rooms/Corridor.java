package rooms;

import java.awt.Point;
import java.util.Random;
import java.util.Vector;

import objects.Door;
import objects.Gold;
import objects.Item;
import objects.Shield;
import objects.Weapon;
import tiles.Tile;
import tiles.TileFactory;

public class Corridor extends Room {	
	public Corridor(int x1, int y1, int x2, int y2, String s) {
		this.description = s;
		this.p1 = new Point(x1, y1);
		this.p2 = new Point(x2, y2);
		this.door = new Vector<Door>();
		this.floor = TileFactory.getInstance().createTileStone();
		this.show = false;
	}
	
	public Item parsingFloor() {
		Random rnd = new Random();
		int parsingChance = rnd.nextInt(4), floorType = rnd.nextInt(3), height=0, width=0;
		
		if(parsingChance==0) {
			if(getHeight()==2) {
				height=1;
				width=rnd.nextInt(getWidth()-1)+1; 
			} else if(getWidth()==2) {
				width=1;
				height=rnd.nextInt(getHeight()-1)+1;
			}
			
			if(floorType==0) {
				return new Gold(width+this.p1.x, height+this.p1.y);
			} else if(floorType==1) {
				return new Weapon(width+this.p1.x, height+this.p1.y);
			} else if(floorType==2) {
				return new Shield(width+this.p1.x, height+this.p1.y);
			}
		}
		return null;
	}
	
	public void print(Tile[][] tab) {
		for(int i=0; i<=this.getHeight(); i++) {
			for(int j=0; j<=this.getWidth(); j++) {
				if(i == 0 || i == this.getHeight() || j == 0 || j == this.getWidth()) {
					tab[this.p1.y+i][this.p1.x+j] = TileFactory.getInstance().createTileWall();
				} else {
					tab[this.p1.y+i][this.p1.x+j] = this.floor;
				}
			}
		}
	}
}
