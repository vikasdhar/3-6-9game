package org.xluz.droidacts.sanLiuJiu;

import android.content.Context;
//import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class GameBoard extends TextView {
	private Paint cursorPaint, markPaint;
	private Paint linePaint;
//	private int paperColor;
	private int curCol, curRow, gameState;
	GamePlay game0;
	
	public GameBoard (Context context, AttributeSet ats, int ds) {
		super (context, ats, ds);
		init();
	}
	
	public GameBoard (Context context) {
		super (context);
		init();
	}
	public GameBoard (Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		cursorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    cursorPaint.setColor(0xFF0000FF);
	    linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    linePaint.setColor(0x90FF00FF);
		markPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    markPaint.setColor(0xFF0000FF);
	    markPaint.setStrokeWidth(2);
	    markPaint.setStyle(Paint.Style.STROKE);
	    curCol = -1;
	    curRow = -1;
	    gameState = 0;
	}
	
	@Override 
	public void onDraw (Canvas canvas) {
		float xx=getMeasuredWidth();
		float yy=getMeasuredHeight();
		// Draw ruled lines
		int j, i=0;
		while (i <= 9) {
			canvas.drawLine(xx/20+xx/10*i, yy/20, xx/10*i+xx/20, yy-yy/20, linePaint);
			canvas.drawLine(xx/20, yy/10*i+yy/20,xx-xx/20, yy/10*i+yy/20, linePaint);
			i++;
		}
		if(gameState > 0) {
		// Draw moves
		for(i=0; i<9; i++) {
			for(j=0; j<9; j++) {
				if(game0.board[i][j] > 0) {
					canvas.drawCircle(j*xx/10+xx/10, i*yy/10+yy/10, xx/20-xx/100, markPaint);
				}				
			}
		}
		// Draw 
		if(curCol>=0 && curRow>=0 && curCol<9 && curRow<9) {
			if(game0.board[curRow][curCol] == -1)
			// Draw block cursor
			canvas.drawRect(curCol*xx/10+xx/20+xx/200, curRow*yy/10+yy/20+yy/200, 
					curCol*xx/10+xx/20+xx/10-xx/200, curRow*yy/10+yy/20+yy/10-yy/200, cursorPaint);
		}
		}
		super.onDraw(canvas);
	}
	
	private void setBoardState(int r, int c) {
		if(r == -2) {
			for(int i=0; i < 81; i++) game0.board[i/9][i%9]=0;
		}
		else if(c!=curCol || r!=curRow) 
				if(curCol>=0 && curRow>=0 && curCol<9 && curRow<9)
					if(game0.board[curRow][curCol] == -1) game0.board[curRow][curCol] = 0;
		if(r>=0 && c>=0 && r<9 && c<9) {
			curCol = c; curRow = r;
			if(game0.board[curRow][curCol] == 0) game0.board[curRow][curCol] = -1;
			else if(game0.board[curRow][curCol] == -1) {
				game0.board[curRow][curCol] = 1;
				game0.recordMove(r*9+c);
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Get the type of action this event represents
		int actionPerformed = event.getAction();
		if(actionPerformed==MotionEvent.ACTION_DOWN && gameState>0) {
			float xx=getMeasuredWidth();
			float yy=getMeasuredHeight();
			float x0=event.getX()-xx/20;
			float y0=event.getY()-yy/20;
			int c = (int) (x0/xx*10);
			int r = (int) (y0/yy*10);
			if(x0<0) {
				c = -1;
			}
			if(y0<0) {
				r = -1;
			}
			if(c>=0 && r>=0 && c<9 && r<9) {
				this.setText("Col "+Integer.toString(c)+" , Row "+Integer.toString(r));
			}
			
			setBoardState(r, c);
			invalidate();
			return true;
		}
	
		return false;
	
	}
	 @Override
	  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    // Set the measured dimensions by figuring out the shortest boundary,
	    // height or width.
	    int measuredWidth = measure(widthMeasureSpec);
	    int measuredHeight = measure(heightMeasureSpec);

	    int d = Math.min(measuredWidth, measuredHeight);

	    setMeasuredDimension(d, d);
	  }

	  private int measure(int measureSpec) {
	    int result = 0;

	    // Decode the measurement specifications.
	    int specMode = MeasureSpec.getMode(measureSpec);
	    int specSize = MeasureSpec.getSize(measureSpec);

	    if (specMode == MeasureSpec.UNSPECIFIED) {
	      // Return a default size
	      result = 280;
	    } else {
	      // (specMode == MeasureSpec.AT_MOST)
	      // (specMode == MeasureSpec.EXACTLY)
	      result = specSize;
	    }
	    return result;
	  }

	public int getGameState() {
		return gameState;
	}

	public void setGameState(int gameState) {
		this.gameState = gameState;
		if(gameState==2) {
			setBoardState(-2, -2);
			invalidate();
		}
		// game starts
		if(gameState>0) game0.recordMove(101);
	}

	public GamePlay getGame0() {
		return game0;
	}

	public void setGame0(GamePlay game0) {
		this.game0 = game0;
	}

}


