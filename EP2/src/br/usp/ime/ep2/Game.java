package br.usp.ime.ep2;

import javax.microedition.khronos.opengles.GL10;

import br.usp.ime.ep2.Constants.Collision;
import br.usp.ime.ep2.Constants.Colors;
import br.usp.ime.ep2.Constants.Forms;
import android.util.Log;

public class Game {
	private static final String TAG = Game.class.getSimpleName();

	private static final int SCREEN_INITIAL_X = 0;
	private static final int SCREEN_INITIAL_Y = 0;
	private float SCREEN_HIGHER_Y;
	private float SCREEN_LOWER_Y;
	private float SCREEN_HIGHER_X;
	private float SCREEN_LOWER_X;
	
	private long mPrevCurrentBeginFrameTime;
	private int mFramesWithoutBallMov; 
	
	private Quad mPaddle;
	private Ball mBall;
	private Quad[][] mBlocks;
	
	public Game() {
		SCREEN_HIGHER_Y = 1.0f;
		SCREEN_LOWER_Y = -1.0f;
		SCREEN_HIGHER_X = 1.0f;
		SCREEN_LOWER_X = -1.0f;		
		
		resetElements();
	}
	
	public void resetElements() {
		mPrevCurrentBeginFrameTime = 0;
		
		mPaddle = new Quad(Forms.PADDLE, Colors.RAINBOW, 0.0f, -0.7f, 0.1f);
		mBall = new Ball(Forms.BALL, Colors.RAINBOW, 0.0f, 0.0f, -0.05f, -0.05f, 0.1f, 4);
		mBlocks = Utils.createLevel(8, 12, -0.55f, 0.7f, 0.1f, 0.04f);
		
		mFramesWithoutBallMov = mBall.getSpeed();
	}
	
	public void drawElements(GL10 gl) {
		mPaddle.draw(gl);
		mBall.draw(gl);
		
		// Need to draw each block on surface
		for (int i=0; i<mBlocks.length; i++) {
			for (int j=0; j<mBlocks[i].length; j++) {
				mBlocks[i][j].draw(gl);
			}
		}
	}
	
	public void updatePaddleXPosition(float x) {
		mPaddle.setXPosition(x);
	}

	//Update next frame state
	public void updateState() {
		
		if (mPrevCurrentBeginFrameTime == 0) {
			mPrevCurrentBeginFrameTime = System.nanoTime();
		}
		
		long mCurrentTime = System.nanoTime();
		double elapsedFrameTime = (mCurrentTime - mPrevCurrentBeginFrameTime)/Constants.NANOS_PER_MS;
		if (elapsedFrameTime < Constants.MS_PER_FRAME)//it doesn't reach next frame yet
			return;
		
		//Now it's time to update next frame. 
		
		int collisionType = detectColision();	
		
		//I'm considering the ball is being updated in a different rate compared to the frame update rate.
		if (mFramesWithoutBallMov == 0) {
			switch (collisionType) {
			case Collision.WALL_RIGHT_LEFT_SIDE:
				mBall.turnToPerpendicularDirection(true);
				break;
			case Collision.WALL_TOP_BOTTOM_SIDE:
				mBall.turnToPerpendicularDirection(false);
				break;
		}			
			
//			Log.i(TAG, "time to move the ball");
			mBall.move();
			mFramesWithoutBallMov = mBall.getSpeed();
		}
		
		mFramesWithoutBallMov--;
		mPrevCurrentBeginFrameTime = mCurrentTime;
	}
	
	private int detectColision() {	
		float ballXPos = mBall.getXPos();
		float ballYPos = mBall.getYPos();
		
		//detecting collision between ball and wall
		if ((ballXPos > SCREEN_HIGHER_X) 			//collided in the right side
				|| (ballXPos < SCREEN_LOWER_X)) {	//collided in the left side 
			return Collision.WALL_RIGHT_LEFT_SIDE;
		} else if ((ballYPos > SCREEN_HIGHER_Y)	//collided in the top part
				|| (ballYPos < SCREEN_LOWER_Y)) {	//collided in the bottom part
			return Collision.WALL_TOP_BOTTOM_SIDE;
		}
		
		//detecting collision between the ball and the paddle
		
		return -1;
	}

	public void updateScreenMeasures(float screenWidth, float screenHeight) {
		Log.i(TAG, "screenWidth: "+screenWidth+", screenHeight: "+screenHeight);
		SCREEN_LOWER_X = SCREEN_INITIAL_X - screenWidth/2;
		SCREEN_HIGHER_X = SCREEN_INITIAL_X + screenWidth/2;
		SCREEN_LOWER_Y = SCREEN_INITIAL_Y - screenHeight/2;
		SCREEN_HIGHER_Y = SCREEN_INITIAL_Y + screenHeight/2;
		
	}
}
