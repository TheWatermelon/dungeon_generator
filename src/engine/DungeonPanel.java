package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.BorderFactory;

import objects.effect.EffectNormal;
import objects.looker.LookerStuff;
import tiles.*;

public class DungeonPanel extends GamePanel implements Observer {
	protected static final long serialVersionUID = 1L;
	
	protected int[][] light;
	protected boolean isLight;
	protected int[][] fireLine;
	
	protected Rectangle window;
	protected Rectangle player;
	
	// Indicators to check whether to refresh the table
	protected boolean dirty;
	protected String table;
	protected Color borderColor;
	
	// Theme colors
	protected Color wall;
	
	public DungeonPanel(Window w) {
		super(w);
		setBorder(BorderFactory.createLineBorder(Color.white));
		this.light = Resources.drawCircle(9);
		this.isLight=true;
		
		initPlayerRectangle();
		calculateWindowRectangle();
		
		this.dirty=true;
		this.table="";
		this.borderColor = Resources.white;
		
		this.wall = Resources.getInstance().theme;
	}
	
	/**
	 * setDirty : setter for dirty
	 * @param val
	 */
	public void setDirty(boolean val) { dirty = val; }
	
	/**
	 * showLight : enable isLight
	 */
	public void showLight() { isLight=true; }
	/**
	 * hideLight : disable isLight
	 */
	public void hideLight() { isLight=false; }
	
	/**
	 * prepareColor : set g color based on Tile in (i;j)
	 * @param g : brush
	 * @param t : Tile grid
	 * @param i : x axis
	 * @param j : y axis
	 */
	public void prepareColor(Graphics g, Tile[][] t, int i, int j) {
		if(!isLight(i, j)) { g.setColor(Resources.darkerGray); return; }
		
		if(t[i][j] instanceof TilePlayer) {
			g.setColor(win.getMap().getPlayer().getColor());
		} else if(t[i][j] instanceof TileMonster) {
			if(win.getMap().getMonster(j, i)!=null) { g.setColor(win.getMap().getMonster(j, i).getColor()); }
			else { g.setColor(t[i][j].getColor()); }
		} else if(win.getMap().isFireMode()) {
			if(isFireLine(i, j)) { g.setColor(Resources.white); }
			else { g.setColor(t[i][j].getColor()); }
		} else {
			g.setColor(t[i][j].getColor());
		}
	}
	
	/**
	 * printLooker : get player looker and print it with colors
	 * @param g
	 * @param offsetX
	 * @param offsetY
	 */
	public void printLooker(Graphics g, int offsetX, int offsetY) {
		Graphics2D g2d = (Graphics2D) g.create();
		Color newBorderColor = Color.BLACK;
		
		char[] looker = new char[3];
		looker[0]=' ';
		looker[1]=' ';
		looker[2]=' ';
		
		int fontSize = Resources.getDungeonFont().getSize();
		
		if(!win.getMap().getPlayer().getLooker().isVisible()) { 
			g2d.drawImage(Resources.getInstance().sprites.getSprite16((int)looker[0]), offsetX-(fontSize/2-1), offsetY, this);
			g2d.drawImage(Resources.getInstance().sprites.getSprite16((int)looker[1]), offsetX+(fontSize/2+1), offsetY, this);
			g2d.drawImage(Resources.getInstance().sprites.getSprite16((int)looker[2]), offsetX, offsetY-3, this);
			newBorderColor = Color.BLACK;
		} else {
			if(win.getMap().getPlayer().getHelmet().getMaxDurability()!=-1) {
				looker[2]=win.getMap().getPlayer().getHelmet().getTile().getSymbol();
				g2d.drawImage(Resources.getInstance().sprites.getColoredSprite16(
						Resources.getInstance().sprites.getSprite16((int)looker[2]),
						win.getMap().getPlayer().getHelmet().getColor()),
						offsetX, offsetY-3, this);
				newBorderColor = win.getMap().getPlayer().getHelmet().getColor();
			}
			looker[0]=win.getMap().getPlayer().getLooker().getLeft();
			g2d.drawImage(
				Resources.getInstance().sprites.getColoredSprite16(
					Resources.getInstance().sprites.getSprite16((int)looker[0]), 
					win.getMap().getPlayer().getLooker().getLeftColor()
				), 
				offsetX-(fontSize/2-1), 
				offsetY, 
				this
			);
			newBorderColor = win.getMap().getPlayer().getLooker().getLeftColor();
			looker[1]=win.getMap().getPlayer().getLooker().getRight();
			g2d.drawImage(
				Resources.getInstance().sprites.getColoredSprite16(
					Resources.getInstance().sprites.getSprite16((int)looker[1]), 
					win.getMap().getPlayer().getLooker().getRightColor()
				), 
				offsetX+(fontSize/2+1), 
				offsetY, 
				this
			);
			newBorderColor = win.getMap().getPlayer().getLooker().getRightColor();
		}
		// Mise a jour de la bordure du cadre de jeu
		newBorderColor = (win.getMap().getPlayer().getLooker() instanceof LookerStuff || newBorderColor==Color.black)?Resources.white:newBorderColor;
		newBorderColor = (win.getMap().getPlayer().getEffect() instanceof EffectNormal)?newBorderColor:win.getMap().getPlayer().getEffect().getColor();
		if(borderColor != newBorderColor && 
				newBorderColor != Resources.white && 
				newBorderColor != Resources.coolRed &&
				newBorderColor != Resources.brown) {
			borderColor = newBorderColor; 
			this.win.notifyColor(newBorderColor);
		} else if(newBorderColor == Resources.coolRed ||
				newBorderColor == Resources.brown) {
			this.win.notifyColor(newBorderColor);
		} else if(newBorderColor == Resources.white) {
			borderColor = newBorderColor; 
		}
	}
	
	/**
	 * printCommandsHelp : print commands for possible actions in squares around the player
	 * @param g
	 * @param offsetX
	 * @param offsetY
	 */
	public void printCommandsHelp(Graphics g, int offsetX, int offsetY) {
		char[] command = new char [4];
		//int fontSize = Resources.getDungeonFont().getSize();
		int fontSize = 16;
		offsetX+=2;
		offsetY-=2;
		
		// If there is an item under the player
		if(win.getMap().isItem(win.getMap().getPlayer().pos.x, win.getMap().getPlayer().pos.y) != null) {
			command[0] = Character.toUpperCase(Resources.Commands.Take.getKey());
		}
		
		// If there are monsters (may override item help)
		int[][] direction = {
				{0, -1},
				{1, 0},
				{0, 1},
				{-1, 0}
		};
		Resources.Commands[] allCommands = Resources.Commands.values();
		for(int i = 0; i<direction.length; i++) {
			int deltaX = win.getMap().getPlayer().pos.x + direction[i][0];
			int deltaY = win.getMap().getPlayer().pos.y + direction[i][1];
			if(win.getMap().isMonster(deltaX, deltaY)) {
				command[i] = Character.toUpperCase(allCommands[i].getKey());
			}
		}
		
		// Print the commands 2 cells away from the player
		int[][] guiDirection = {
				{offsetX, offsetY-fontSize},
				{offsetX+2*fontSize, offsetY+16},
				{offsetX, offsetY+3*(fontSize)},
				{offsetX-2*fontSize, offsetY+16}
		};
		
		g.setFont(Resources.getDungeonFont());
		for(int i = 0; i<guiDirection.length; i++) {
			if(command[i] != 0) {
				g.setColor(new Color(255, 0, 0, 100));
				g.drawRect(guiDirection[i][0]-3, guiDirection[i][1]-(fontSize-1), fontSize, (fontSize+2));
				g.setColor(Color.black);
				g.fillRect(guiDirection[i][0]-2, guiDirection[i][1]-(fontSize-2), fontSize-1, (fontSize+1));
				g.setColor(Color.white);
				g.drawChars(command, i, 1, guiDirection[i][0], guiDirection[i][1]);
			}
		}
	}
	
	/**
	 * isLight : adapt player coordinates to light table to determine if a point (i;j) is in the light
	 * @param i
	 * @param j
	 * @return
	 */
	public boolean isLight(int i, int j) {
		if(isLight) { return true; }
		
		int limit=(int)Math.round(light.length/2);
		if(i>win.getMap().getPlayer().pos.y-limit && i<win.getMap().getPlayer().pos.y+limit &&
				j>win.getMap().getPlayer().pos.x-limit && j<win.getMap().getPlayer().pos.x+limit) {
			int minX=win.getMap().getPlayer().pos.x-limit;
			int minY=win.getMap().getPlayer().pos.y-limit;
			int pointX = j-minX;
			int pointY = i-minY;
			
			if(light[pointY][pointX]==1) {
				return true;
			}
		} 
		return false;
	}
	
	/**
	 * isFireLine : adapt player coordinates to fireLine table to determine if a point (i;j) is in the fireLine
	 * @param i
	 * @param j
	 * @return
	 */
	public boolean isFireLine(int i, int j) {
		int limitY=(int)Math.round(fireLine.length/2);
		int limitX=(int)Math.round(fireLine[0].length/2);
		if(i>=win.getMap().getPlayer().pos.y-limitY && i<=win.getMap().getPlayer().pos.y+limitY &&
				j>=win.getMap().getPlayer().pos.x-limitX && j<=win.getMap().getPlayer().pos.x+limitX) {
			int pointX = j-(win.getMap().getPlayer().pos.x-limitX);
			int pointY = i-(win.getMap().getPlayer().pos.y-limitY);
			if(fireLine[pointY][pointX]==1) {
				return true;
			}
		} 
		
		return false;
	}
	
	/**
	 * initPlayerRectangle : setup a 9x9 rectangle around the player
	 */
	public void initPlayerRectangle() {
		int playerX1 = (win.getMap().getPlayer().pos.x-4)<0?0:win.getMap().getPlayer().pos.x-4;
		int playerY1 = (win.getMap().getPlayer().pos.y-4)<0?0:win.getMap().getPlayer().pos.y-4;
		int playerWidth = playerX1+9>win.getMap().getWidth()?win.getMap().getWidth()-playerX1:9;
		int playerHeight = playerY1+9>win.getMap().getHeight()?win.getMap().getHeight()-playerY1:9;
		player = new Rectangle(playerX1, playerY1, playerWidth, playerHeight);
	}
	
	/**
	 * calculatePlayerRectangle : reset player rectangle position after player's move
	 */
	public void calculatePlayerRectangle() {
		Point pos = win.getMap().getPlayer().pos;
		
		if(pos.x<player.x) {							// WEST
			player = new Rectangle(player.x-1, player.y, player.width, player.height);
		} else if(pos.x>player.x+player.width) {		// EAST
			player = new Rectangle(player.x+1, player.y, player.width, player.height);
		} else if(pos.y<player.y) {						// NORTH
			player = new Rectangle(player.x, player.y-1, player.width, player.height);
		} else if(pos.y>player.y+player.height) {		// SOUTH
			player = new Rectangle(player.x, player.y+1, player.width, player.height);
		}
	}
	
	/**
	 * calculateWindowRectangle : create a 9x9 rectangle around the player to fix the scroll limit
	 */
	public void calculateWindowRectangle() {
		int width = (int)Math.floor(getWidth()*54/782);
		int height = (int)Math.floor(getHeight()*29/488);
		int x1 = ((player.x+4-width/2)<0)?0:(player.x+4-width/2);
		int maxX = x1+width>win.getMap().getWidth()?win.getMap().getWidth()-x1:width;
		int y1 = ((player.y+4-height/2)<0)?0:(player.y+4-height/2);
		int maxY = y1+height>win.getMap().getHeight()?win.getMap().getHeight()-y1:height;
		window = new Rectangle(x1, y1, maxX, maxY);
	}
	
	/**
	 * refreshRectangles : refresh both player and window rectangles
	 */
	public void refreshRectangles() {
		if(!player.contains(win.getMap().getPlayer().pos)) {
			calculatePlayerRectangle();
		}
		calculateWindowRectangle();
	}
	
	/**
	 * refreshFireLine : draw fireLine based on player pos and fire point
	 */
	public void refreshFireLine() {
		if(win.getMap().isFireMode()) {
			int playerX = win.getMap().getPlayer().pos.x,
					playerY = win.getMap().getPlayer().pos.y,
					fireX = win.getMap().getFirePoint().x - playerX,
					fireY = win.getMap().getFirePoint().y - playerY;
			// Detect the octant
			int octant=0;
			if(fireX>=0) {
				if(fireY>=0) {
					octant = (fireX>fireY)? 0 : 1;
				} else {
					octant = (fireX>-fireY)? 7 : 6;
				}
			} else {
				if(fireY>=0) {
					octant = (-fireX>fireY)? 3 : 2;
				} else {
					octant = (-fireX>-fireY)? 4 : 5;
				}
			}
			Point p = Resources.switchToOctantZeroFrom(octant, fireX, fireY);		
			fireLine = Resources.drawLine(0, 0, p.x, p.y, octant);
		}
	}
	
	/**
	 * refreshTable : refresh string with all symbols from Tile table
	 */
	protected void refreshTable() {
		this.table="";
		Tile[][] table = win.getMap().getTable();
		for(int i=0; i<table.length; i++) {
			for(int j=0; j<table[0].length; j++) {
				this.table+=table[i][j].getSymbol();
			}
		}
	}
	
	/**
	 * paintComponent : draw current map on window
	 */
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g.setColor(Resources.getInstance().background);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Resources.getInstance().foreground);
		g.setFont(Resources.getDungeonFont());

		this.wall = Resources.getInstance().theme;
		
		if(dirty) { refreshTable(); dirty=false; }
		
		refreshFireLine();
		
		refreshRectangles();
			
		//int offsetX=15, offsetY=30, playerOffsetX=15, playerOffsetY=30;
		int offsetX=16, offsetY=32, playerOffsetX=16, playerOffsetY=32;
		char[] c = new char[1];
		for(int i=window.y; i<window.getHeight()+window.y; i++) {
			for(int j=window.x; j<window.getWidth()+window.x; j++) {
				if(win.getMap().isFireMode() &&
						i==win.getMap().getFirePoint().y &&
						j==win.getMap().getFirePoint().x) {
					c[0] = 'X';
					g.setColor(Resources.white);
				} else {
					c[0]=table.charAt(i*win.getMap().getWidth()+j);
					prepareColor(g, win.getMap().getTable(), i, j);
				}
				//g.drawChars(c, 0, 1, offsetX, offsetY);
				BufferedImage sprite = Resources.getInstance().sprites.getSprite16((int)c[0]);
				sprite = Resources.getInstance().sprites.getColoredSprite16(sprite, g.getColor());
				g2d.drawImage(sprite, offsetX, offsetY, this);
				if(table.charAt(i*win.getMap().getWidth()+j)==TileFactory.getInstance().createTilePlayer().getSymbol()) { 
					printLooker(g, offsetX-1, offsetY); 
					playerOffsetX = offsetX;
					playerOffsetY = offsetY;
				}
				//offsetX+=Resources.getDungeonFont().getSize(); 
				offsetX+=16;
			}
			//offsetY+=Resources.getDungeonFont().getSize()+2; 
			offsetY+=16;
			//offsetX=15;
			offsetX=16;
		}
		
		if(Resources.getInstance().commandsHelp) {
			printCommandsHelp(g, playerOffsetX, playerOffsetY);
		}
		
		if(win.getDungeon().getPlayer().isDead()) setBorder(BorderFactory.createLineBorder(Color.RED));
		else {
			if(win.getMap().getPlayer().getEffect() instanceof EffectNormal) setBorder(BorderFactory.createLineBorder(Color.BLACK)); 
			else setBorder(BorderFactory.createLineBorder(win.getMap().getPlayer().getEffect().getColor()));
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		System.out.println("update");
	}

	@Override
	public KeyListener getKeyListener() {
		return new DungeonKeyListener(this.win.getDungeon(), this.win.getMap(), this.win);
	}
}
