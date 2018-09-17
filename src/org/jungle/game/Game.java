package org.jungle.game;

import org.jungle.Window;

public class Game {

	protected boolean running;
	protected Window win;
	protected IGameLogic logic;
	private GameOptions opt;

	public Game() {

	}

	public void start(GameOptions opt) {
		this.opt = opt;
		running = true;
		win = new Window();
		win.init(opt);
		win.show();
		logic.bind(this);
		try {
			logic.init(win);
		} catch (Exception e) {
			e.printStackTrace();
		}
		loop();
		logic.cleanup();
	}
	
	public void exit() {
		running = false;
	}
	
	public double getTime() {
        return System.nanoTime() / 1000000000.0;
	}

	public void loop() {
		double secsPerUpdate = 1.0d / 30.0d;
		double previous = getTime();
		double steps = 0.0;
		while (running) {
			double loopStartTime = getTime();
			double elapsed = loopStartTime - previous;

			previous = loopStartTime;
			steps += elapsed;

			handleInput();

			while (steps >= secsPerUpdate) {
				update();
				steps -= secsPerUpdate;
			}

			render();
			sync(loopStartTime);
		}
	}

	private void sync(double loopStartTime) {
		float loopSlot = 1f / 90;
		double endTime = loopStartTime + loopSlot;
		while (System.currentTimeMillis() < endTime) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException ie) {
			}
		}
	}

	public void handleInput() {
		logic.input(win);
	}

	public void update() {
		win.update();
		logic.update(1.0f);
	}

	public void render() {
		logic.render(win);
		win.render();
	}

}
