package com.musashisan.ga;

import lombok.Getter;
import processing.core.PVector;

public class Brain {
	private PVector[] directions;

	@Getter
	private int size;

	public Brain(int size) {
		this.size = size;
		this.directions = new PVector[size];
	}

	public Brain(Brain source) {
		this.size = source.size;
		this.directions = new PVector[source.directions.length];
		for (int i = 0; i < source.directions.length; i++) {
			this.directions[i] = source.directions[i].copy();
		}
	}

	public void randomizeDirections() {
		double randomAngle;
		for (int i = 0; i < this.directions.length; i++) {
			randomAngle = Math.random() * 2 * Math.PI;
			this.directions[i] = PVector.fromAngle((float) randomAngle);
		}
	}

	public PVector getDirection(int index) {
		if (index > this.directions.length || index < 0) {
			return null;
		}
		return this.directions[index];
	}

	public void setDirection(int index, PVector direction) {
		if (index > this.directions.length || index < 0) {
			return;
		}
		this.directions[index] = direction;
	}

}
