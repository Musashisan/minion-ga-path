package com.musashisan.ga;

import lombok.Getter;
import lombok.Setter;
import processing.core.PApplet;

public class Obstacle {

	@Getter
	@Setter
	private int color;

	private static PApplet applet;
	private float x, y, width, height;

	public Obstacle(PApplet applet, float x, float y, float width, float height, int color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
		this.applet = applet;
	}

	public void show() {
		this.applet.fill(this.color);
		this.applet.rect(this.x, this.y, this.width, this.height);
	}

	public boolean collision(float x, float y) {
		if (x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height) {
			return true;
		}
		return false;
	}
}
