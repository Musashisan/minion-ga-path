package com.musashisan.ga;

import processing.core.PApplet;
import processing.core.PVector;

public class Application extends PApplet {

	PVector goal;
	private Population[] populations;
	private Obstacle[] obtacles;

	public static void main(String[] args) {
		PApplet.main("com.musashisan.ga.Application");
	}

	@Override
	public void settings() {
		this.size(500, 400);
	}

	@Override
	public void setup() {
		this.goal = new PVector(70, 20);
		this.populations = new Population[3];
		for (int i = 0; i < this.populations.length; i++) {
			this.populations[i] = new Population(this, 500, this.width / 2, (float) Math.random() * this.height,
					this.goal.x, this.goal.y, 300,
					this.color(100, (float) Math.random() * 255, (float) Math.random() * 255));
			this.populations[i].setup();
		}

		this.obtacles = new Obstacle[3];
		this.obtacles[0] = new Obstacle(this, 50, 50, 200, 20, this.color(0, 0, 200));
		this.obtacles[1] = new Obstacle(this, 50, 70, 20, 70, this.color(0, 0, 200));
		this.obtacles[2] = new Obstacle(this, 100, 120, 20, 50, this.color(0, 0, 200));
		this.background(255);
	}

	@Override
	public void draw() {
		this.background(255);
		// draw goal
		this.fill(255, 0, 0);
		this.ellipse(this.goal.x, this.goal.y, 4, 4);

		for (int i = 0; i < this.obtacles.length; i++) {
			this.obtacles[i].show();
		}
		// move and draw population
		for (int i = 0; i < this.populations.length; i++) {
			if (this.populations[i].isDead()) {
				this.populations[i].calculateFitness();
				this.populations[i].naturalSelectionCrossover();
				this.populations[i].mutate();
			} else {
				this.populations[i].show();
				this.populations[i].update(this.obtacles);
			}
		}
	}
}
