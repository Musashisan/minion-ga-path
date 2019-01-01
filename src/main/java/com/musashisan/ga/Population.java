package com.musashisan.ga;

import processing.core.PApplet;

public class Population {

	private static PApplet applet;
	private int size;
	private int generation;
	private float fitnessSum;
	private Minion[] minions;
	private int fittestMinionId;
	private float positionX, positionY;
	private float goalX, goalY;
	private int minStep, brainSize;
	private int color;

	public Population(PApplet papplet, int size, float positionX, float positionY, float goalX, float goalY,
			int brainSize, int color) {
		this.color = color;
		this.positionX = positionX;
		this.positionY = positionY;
		this.goalX = goalX;
		this.goalY = goalY;
		this.applet = papplet;
		this.generation = 0;
		this.brainSize = brainSize;
		this.minStep = brainSize - 1;
		this.size = size;
		this.minions = new Minion[this.size];
	}

	public void setup() {
		for (int i = 0; i < this.minions.length; i++) {
			this.minions[i] = this.minionFactory();
		}
	}

	private void minionSetup(Minion minion) {
		minion.setPosition(this.positionX, this.positionY);
		minion.setGoal(this.goalX, this.goalY);
		minion.setMaxStep(this.minStep);
		minion.setColor(this.color);
	}

	/*
	 * Generate a new minion with the current population configuration
	 */
	private Minion minionFactory() {
		Minion minion = new Minion(this.applet, this.brainSize);
		this.minionSetup(minion);
		return minion;
	}

	/*
	 * Generate replicas from the fittest minion
	 */
	private Minion fittestMinionFactory() {
		Minion minion;
		minion = this.minions[this.fittestMinionId].replica();
		this.minionSetup(minion);
		// override color
		minion.setColor(this.applet.color(255, 10, 20));
		return minion;
	}

	private Minion[] crossoverMinionFactory() {
		Minion parent;
		Minion[] babys;
		parent = this.selectParent();
		babys = parent.crossover(this.selectParent());
		for (int i = 0; i < babys.length; i++) {
			this.minionSetup(babys[i]);
		}
		return babys;
	}

	public void setGoal(int x, int y) {
		this.goalX = x;
		this.goalY = y;
		for (int i = 0; i < this.minions.length; i++) {
			this.minions[i].setGoal(x, y);
		}
	}

	public void update(Obstacle[] obstacles) {
		for (int i = 0; i < this.minions.length; i++) {
			this.minions[i].update();
			for (int j = 0; j < obstacles.length; j++) {
				if (obstacles[j].collision(this.minions[i].getPosition().x, this.minions[i].getPosition().y)) {
					this.minions[i].setDead(true);
				}
			}
		}

	}

	public void show() {
		for (int i = 0; i < this.minions.length; i++) {
			this.minions[i].show();
		}
	}

	public boolean isDead() {
		for (int i = 0; i < this.minions.length; i++) {
			if (!this.minions[i].isDead() && !this.minions[i].hasReachedGoal()) {
				return false;
			}
		}
		return true;
	}

	public void calculateFitness() {
		float maxFitness = 0.0f;
		float currentFitness;
		for (int i = 0; i < this.minions.length; i++) {
			currentFitness = this.minions[i].calculateFitness();
			if (currentFitness > maxFitness) {
				maxFitness = currentFitness;
				this.fittestMinionId = i;
			}
			if (this.minions[i].hasReachedGoal() && this.minStep > this.minions[i].getStep()) {
				this.minStep = this.minions[i].getStep();
			}
		}
	}

	public void naturalSelectionCrossover() {
		Minion fittestMinion;
		Minion[] babys;
		Minion[] newMinions = new Minion[this.minions.length];
		int i = 0;
		this.calculateFitnessSum();
		fittestMinion = this.fittestMinionFactory();
		while (i < newMinions.length - 1) {
			babys = this.crossoverMinionFactory();
			for (int j = 0; j < babys.length && i + j < newMinions.length; j++) {
				newMinions[i + j] = babys[j];
			}
			i += babys.length;
		}
		newMinions[newMinions.length - 1] = fittestMinion;

		this.minions = newMinions;
		this.generation++;
	}

	private void calculateFitnessSum() {
		this.fitnessSum = 0.0f;
		for (int i = 0; i < this.minions.length; i++) {
			this.fitnessSum += this.minions[i].getFitness();
		}
	}

	/*
	 * We select the parent at random with greater probability the ones with better
	 * fitness
	 */
	private Minion selectParent() {
		float rand = (float) (Math.random() * this.fitnessSum);
		float currentFitness = 0.0f;
		int i = -1;
		while (currentFitness < rand) {
			currentFitness += this.minions[++i].getFitness();
		}
		return this.minions[i];
	}

	public void mutate() {
		// population of 1 minion, mutate it
		if (this.minions.length == 1) {
			this.minions[0].mutate();
		}
		// mutate all minions except the last one, possible fittest
		for (int i = 0; i < this.minions.length - 1; i++) {
			this.minions[i].mutate();
		}
	}
}
