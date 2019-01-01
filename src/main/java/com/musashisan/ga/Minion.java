package com.musashisan.ga;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import processing.core.PApplet;
import processing.core.PVector;

public class Minion {
	@Getter
	@Setter
	private int color;

	@Getter
	private int step;

	@Setter
	private int maxStep;
	private int brainSize;

	@Getter
	@Setter
	private boolean dead;

	@Accessors(fluent = true)
	@Getter
	private boolean hasReachedGoal;

	@Getter
	private float fitness;

	private static PApplet applet;
	private Brain brain;
	private PVector goal;
	private PVector position;
	private PVector velocity;
	private PVector acceleration;

	public Minion(PApplet papplet, int brainSize) {

		this.applet = papplet;
		this.maxStep = brainSize - 1;
		this.brainSize = brainSize;
		this.brain = new Brain(brainSize);
		this.brain.randomizeDirections();
		this.goal = new PVector(0, 0);
		this.position = new PVector(0, 0);
		this.velocity = new PVector(0, 0);
		this.acceleration = new PVector(0, 0);
		this.color = 0;
		this.step = 0;
		this.dead = false;
		this.hasReachedGoal = false;
	}

	// minion factory with te same Brain
	public Minion replica() {
		Minion clon = new Minion(this.applet, this.brainSize);
		clon.brain = new Brain(this.brain);
		return clon;
	}

	public PVector getPosition() {
		return this.position;
	}

	public void setPosition(float x, float y) {
		this.position.x = x;
		this.position.y = y;
	}

	public void setGoal(float x, float y) {
		this.goal.x = x;
		this.goal.y = y;
	}

	public void setPosition(PVector position) {
		this.position.x = position.x;
		this.position.y = position.y;
	}

	private void move() {
		if (this.step <= this.maxStep && this.step < this.brain.getSize()) {
			this.acceleration = this.brain.getDirection(this.step);
			this.step++;
		} else {
			this.dead = true;
		}
		this.velocity.add(this.acceleration).limit(5);
		this.position.add(this.velocity);
	}

	public void update() {
		if (this.dead || this.hasReachedGoal) {
			return;
		}
		this.move();
		if ((this.position.x < 2 || this.position.y < 2 || this.position.x > this.applet.width - 2
				|| this.position.y > this.applet.height - 2)) {
			this.dead = true;
		} else if (this.position.dist(this.goal) < 5) {
			this.hasReachedGoal = true;
		}
	}

	public void show() {
		this.applet.fill(this.color);
		this.applet.ellipse(this.position.x, this.position.y, 4, 4);
	}

	public float calculateFitness() {
		float distanceToGoal = this.position.dist(this.goal);
		if (this.hasReachedGoal) {
			this.fitness = (float) (1.0 / (distanceToGoal * distanceToGoal * this.step * this.step));
		} else {
			this.fitness = (float) (1.0 / (distanceToGoal * distanceToGoal));
		}
		return this.fitness;
	}

	// no crossover
	public Minion baby() {
		Minion baby = new Minion(this.applet, this.brainSize);
		baby.brain = new Brain(this.brain);
		return baby;
	}

	// crossover
	public Minion[] crossover(Minion partner) {
		Minion[] babys = new Minion[2];
		int crossoverPoint = 1 + (int) Math.random() * (this.brainSize - 1);
		babys[0] = new Minion(this.applet, this.brainSize);
		babys[1] = new Minion(this.applet, this.brainSize);
		babys[0].brain = new Brain(this.brain);
		babys[1].brain = new Brain(partner.brain);
		for (int i = 0; i <= crossoverPoint; i++) {
			babys[0].brain.setDirection(i, partner.brain.getDirection(i));
			babys[1].brain.setDirection(i, this.brain.getDirection(i));
		}
		return babys;
	}

	public void mutate() {
		float murationRate = 0.01f;
		float rand;
		float angle;
		for (int i = 0; i < this.brain.getSize(); i++) {
			rand = (float) Math.random();
			if (rand < murationRate) {
				angle = (float) (Math.random() * 2 * Math.PI);
				this.brain.setDirection(i, PVector.fromAngle(angle));
			}
		}

	}

}
