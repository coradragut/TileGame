package dev.codenmore.tilegame;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import dev.codenmore.tilegame.display.Display;
import dev.codenmore.tilegame.gfx.Assets;
import dev.codenmore.tilegame.gfx.GameCamera;
import dev.codenmore.tilegame.input.KeyManager;
import dev.codenmore.tilegame.input.MouseManager;
import dev.codenmore.tilegame.states.GameState;
import dev.codenmore.tilegame.states.MenuState;
import dev.codenmore.tilegame.states.State;

public class Game implements Runnable {
	
	private Display display;
	private int width, height;
	public String title;
	
	private boolean running = false;
	private Thread thread;
	
	private BufferStrategy bs;
	private Graphics g;
	
	//STATES
	public State gameState;
	public State menuState;
	
	//Input
	private KeyManager keyManager;
	private MouseManager mouseManager;
	
	//Camera
	private GameCamera gameCamera;
	
	//Handler
	private Handler handler;
	
	public Game(String title, int width, int height) {
		this.width = width;
		this.height = height;
		this.title = title;
		keyManager = new KeyManager();
		mouseManager = new MouseManager();
	}
	
	private void init() {
		display = new Display(title, width, height);
		display.getFrame().addKeyListener(keyManager);
		display.getFrame().addMouseListener(mouseManager);
		display.getFrame().addMouseMotionListener(mouseManager);
		display.getCanvas().addMouseListener(mouseManager);
		display.getCanvas().addMouseMotionListener(mouseManager);
//		test = ImageLoader.loadImage("/textures/sprite.png");
//		sheet = new SpriteSheet(test);
		
		Assets.init();
		handler = new Handler(this);
		gameCamera = new GameCamera(handler,0,0);
		
		gameState = new GameState(handler);
		menuState = new MenuState(handler);
		State.setState(menuState);
	}
	
	
	private void tick() { //aka update
		keyManager.tick();
		
		if(State.getState() != null) {
			State.getState().tick();
		}
	}
	
	private void render() {
		bs = display.getCanvas().getBufferStrategy();//tells the computer how it should draw things to the screen, using buffers
		if(bs==null) {
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics(); //how we create "magical" paintbrush
		
		//Clear Screen
		g.clearRect(0, 0, width, height);
		
		//Draw here
		
		//null is for Image Observer
		if(State.getState() != null) {
			State.getState().render(g);
		}
		
		//End draw
		
		bs.show();
		g.dispose(); //makes sure our graphics object gets done with properly
		
	}
	
	public void run() {
		
		init();
		
		int fps = 60;//how many times per second we want the tick and render methods to run
		double timePerTick = 1000000000 / fps;//to measure in nanoseconds ; 
		//aka max.amount of time, in nanoseconds, we have until we have to call the ticks and render methods again 
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;
		
		while(running) {
			now = System.nanoTime();
			delta += (now-lastTime) / timePerTick;
			timer += now-lastTime;
			lastTime = now;
			
			if(delta>=1) {
				tick();
				render();
				ticks++;
				delta--;
			}
			
			if(timer >= 1000000000) { //timer has been running for at least 1 sec
				System.out.println("Ticks and Frames: " + ticks);
				ticks =0;
				timer = 0;
			}
			
		}
		
		stop();
	}
	
	public KeyManager getKeyManager() {
		return keyManager;
	}
	
	public MouseManager getMouseManager() {
		return mouseManager; 
	}
	
	public GameCamera getGameCamera() {
		return gameCamera;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public synchronized void start() {
		if(running) {
			return;
		}
		running = true;
		thread = new Thread(this);
		thread.start(); //will call the run method
	}
	
	public synchronized void stop() {
		if(!running) {
			return;
		}
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
