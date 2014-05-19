package br.usp.ime.retrobreaker.forms;

public class Paddle extends Quad {
	public static final float[] VERTICES = {
		-1.0f, -0.2f, // bottom left
		-1.0f,  0.2f, // top left
		1.0f, -0.2f, // bottom right
		1.0f,  0.2f, // top right
	};

	public Paddle(float[] colors, float posX, float posY, float scale) {
		super(VERTICES, colors, posX , posY, scale);
	}
	
}