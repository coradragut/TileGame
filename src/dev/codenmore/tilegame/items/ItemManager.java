package dev.codenmore.tilegame.items;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

import dev.codenmore.tilegame.Handler;

public class ItemManager { //will only store items currently in the game, lying on the ground
	
	private Handler handler;
	private ArrayList<Item> items;
	
	public ItemManager(Handler handler) {
		this.handler = handler;
		items = new ArrayList<Item>();
	}

	public void tick() {
		Iterator<Item> it = items.iterator();
		while(it.hasNext()) {
			Item i = it.next();
			i.tick();
			if(i.isPickedUp()) {it.remove();}
		}
	}
	
	public void render(Graphics g) {
		for(Item i: items) {
			i.render(g);
		}
	}
	
	public void addItem(Item i) {
		i.setHandler(handler); //in order for the handler not to be null so we can render that item to the screen
		items.add(i);
	}

	
	//Getters and Setters
	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}
}
