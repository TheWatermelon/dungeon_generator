package editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JPanel;

import tiles.Tile;
import tiles.TileFactory;

public class ToolsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Tile currentTile;
	
	public ToolsPanel() {
		super();
		this.setPreferredSize(new Dimension(50,600));
		this.setLayout(new FlowLayout());

		for(int i=0; i<TileFactory.getInstance().getTilesLength(); i++) {
			TileTool t = new TileTool(this, TileFactory.getInstance().getTileAt(i));
			t.addMouseListener(new ToolMouseListener(t));
			this.add(t);
		}
	}
	
	public void setCurrentTile(Tile t) {
		this.currentTile = t;
		//System.out.println("Current tile : "+this.currentTile);
	}
	
	public Tile getCurrentTile() {
		return this.currentTile;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, 50, 600);
	}
}