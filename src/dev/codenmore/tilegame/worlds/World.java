package dev.codenmore.tilegame.worlds;

import java.awt.Graphics;

import dev.codenmore.tilegame.Handler;
import dev.codenmore.tilegame.entities.EntityManager;
import dev.codenmore.tilegame.entities.creatures.Player;
import dev.codenmore.tilegame.entities.statics.Rock;
import dev.codenmore.tilegame.entities.statics.Tree;
import dev.codenmore.tilegame.items.ItemManager;
import dev.codenmore.tilegame.tiles.Tile;
import dev.codenmore.tilegame.utils.Utils;

public class World {
	
	private Handler handler;
	private int width, height;
	private int spawnX, spawnY;
	private int[][] tiles;
	
	//Entities
	private EntityManager entityManager;
	
	//Item
	private ItemManager itemManager;
	
	public World(Handler handler, String path) {
		this.handler = handler;
		entityManager = new EntityManager(handler, new Player(handler, 100, 100));
		itemManager = new ItemManager(handler);
		entityManager.addEntity(new Tree(handler, 100, 200));
		entityManager.addEntity(new Tree(handler, 250, 650));
		entityManager.addEntity(new Tree(handler, 350, 380));
		entityManager.addEntity(new Tree(handler, 390, 550));
		entityManager.addEntity(new Tree(handler, 650, 400));
		entityManager.addEntity(new Tree(handler, 650, 900));
		entityManager.addEntity(new Tree(handler, 900, 300));
		entityManager.addEntity(new Tree(handler, 900, 750));
		
		entityManager.addEntity(new Rock(handler, 100, 400));
		entityManager.addEntity(new Rock(handler, 200, 850));
		entityManager.addEntity(new Rock(handler, 300, 750));
		entityManager.addEntity(new Rock(handler, 650, 750));
		entityManager.addEntity(new Rock(handler, 550, 250));
		entityManager.addEntity(new Rock(handler, 700, 1000));
		entityManager.addEntity(new Rock(handler, 900, 550));
		entityManager.addEntity(new Rock(handler, 1000, 250));
		
		loadWorld(path);		
		entityManager.getPlayer().setX(spawnX);
		entityManager.getPlayer().setY(spawnY);
		
	}
	
	public void tick() {
		itemManager.tick(); 
		entityManager.tick();
	}
	
	public void render(Graphics g) {
		int xStart = (int) Math.max(0, handler.getGameCamera().getxOffset() / Tile.TILEWIDTH);
		int xEnd = (int) Math.min(width, (handler.getGameCamera().getxOffset() + handler.getWidth()) / Tile.TILEWIDTH +1);
		int yStart = (int) Math.max(0, handler.getGameCamera().getyOffset() / Tile.TILEHEIGHT);
		int yEnd = (int) Math.min(width, (handler.getGameCamera().getyOffset() + handler.getHeight()) / Tile.TILEHEIGHT +1);
		
		for(int y= yStart; y< yEnd; y++) { //better to begin with y to avoid some problems
			for(int x=xStart; x< xEnd; x++) {
				getTile(x,y).render(g, (int) (x * Tile.TILEWIDTH - handler.getGameCamera().getxOffset()),
						(int) (y * Tile.TILEHEIGHT - handler.getGameCamera().getyOffset()));
			}
		}
		
		//Item
		itemManager.render(g);
		
		//Entities
		entityManager.render(g);
	}
	
	public Tile getTile(int x, int y) {
		if(x<0 || y<0 || x>=width || y>=height) {return Tile.grassTile;}
		
		
		Tile t = Tile.tiles[tiles[x][y]];
		if(t == null) {
			return Tile.dirtTile;
		}
		return t;
	}
	
	private void loadWorld(String path) {
		String file = Utils.loadFileAsString(path);
		String[] tokens = file.split("\\s+");
		width = Utils.parseInt(tokens[0]);
		height = Utils.parseInt(tokens[1]);
		spawnX = Utils.parseInt(tokens[2]);
		spawnY = Utils.parseInt(tokens[3]);
		
		tiles = new int[width][height];
		for(int y=0; y<height; y++) {
			for(int x=0; x<width; x++) {
				tiles[x][y] = Utils.parseInt(tokens[(x + y * width) + 4]);
			}
		}
		
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public ItemManager getItemManager() {
		return itemManager;
	}

	public void setItemManager(ItemManager itemManager) {
		this.itemManager = itemManager;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

}
