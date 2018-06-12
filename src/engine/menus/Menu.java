package engine.menus;

import javax.swing.*;

import engine.Resources;
import engine.Window;

public abstract class Menu extends JPanel {
	private static final long serialVersionUID = 1L;
	
	protected Window win;
	protected String[] items;
	protected int focusedItem;
	
	public Menu(Window win) { 
		super(); 
		this.win = win; 
	}
	
	public void setFocusedItem(int val) { this.focusedItem = val; }
	
	public void incFocusedItem() { 
		focusedItem++; 
		if(focusedItem==items.length) { focusedItem=0; } 
		Resources.playCycleMenuSound();
	}
	
	public void decFocusedItem() {
		focusedItem--; 
		if(focusedItem==-1) { focusedItem=items.length-1; } 
		Resources.playCycleMenuSound();
	}

	public abstract void selectFocusedItem();

	public abstract void exitMenu();
	
	protected abstract void initPanel();
}
