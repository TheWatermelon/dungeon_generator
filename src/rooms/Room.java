package rooms;

import java.awt.Point;
import java.util.Vector;

import tiles.Tile;
import tiles.TileFactory;
import objects.Door;

public abstract class Room {
	protected String description;
	public Point p1;
	public Point p2;
	public Vector<Door> door;
	protected Point item;
	protected boolean show;
	protected Tile floor;
	protected Tile[][] room;
	
	public final Tile getTile() { return this.floor; }
	public final int getWidth() { return (this.p2.x - this.p1.x); }
	public final int getHeight() { return (this.p2.y - this.p1.y); }
	public final String toString() { return this.description; }
	public final void show() { this.show=true; }
	public final void hide() { this.show=false; }
	public final void addDoor(Point p) { this.door.add(new Door(p)); }
	public final void addDoor(Point p, Tile t) { this.door.add(new Door(p, t)); }
	
	protected abstract void parsingFloor();
	
	public final void isItem(int x, int y) {
		if(x == this.item.x && y == this.item.y) {
			this.item = new Point();
		}
	}
	
	public final void printOn(Tile[][] tab) {
		if(show) {
			print(tab);
			printDoors(tab);
		}
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
	
	protected final void printDoors(Tile[][] tab) {
		for(int i=0; i<this.door.size(); i++) {
			tab[this.door.get(i).getY()][this.door.get(i).getX()] = this.door.get(i).getFloor();
		}
	}
	
	public final boolean isDoor(int x, int y) {
		boolean ret = false;
		
		for(int i=0; i<this.door.size(); i++) {
			if(x == this.door.get(i).getX() && y == this.door.get(i).getY()) { 
				this.door.get(i).open();
				show();
				ret =  true;
			}
		}
		return ret;
	}
}
