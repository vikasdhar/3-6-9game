package org.xluz.droidacts.sanLiuJiu;

/*
  This class is a View representing a 3-6-9 game board 

Copyright (c) 2014 Cecil Cheung
This software is released under the GNU General Public License version 3.
See, for example, "http://www.gnu.org/licenses/gpl.html".
*/

import android.content.Context;
//import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class GameBoard extends TextView {
	private Paint cursorPaint, markPaint;
	private Paint linePaint, scorPaint;
	private int curCol, curRow, gameState;
	GamePlay game0;
	
	public GameBoard (Context context) {
		super (context);
		init();
	}
	
	public GameBoard (Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public GameBoard (Context context, AttributeSet ats, int ds) {
		super (context, ats, ds);
		init();
	}
	
	private void init() {
		this.setTextColor(Color.GRAY);
		cursorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    cursorPaint.setColor(0xCC9900FF);
	    linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    linePaint.setStyle(Paint.Style.STROKE);
	    linePaint.setColor(Color.LTGRAY);
	    linePaint.setStrokeWidth(2);
		markPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    markPaint.setColor(0xCC9900FF);
	    markPaint.setStrokeWidth(3);
	    markPaint.setStyle(Paint.Style.STROKE);
	    scorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    scorPaint.setColor(0xFFFF9900);
	    scorPaint.setStrokeWidth(2.5f);
	    curCol = -1;
	    curRow = -1;
	    gameState = -1;
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
		if(gameState >= 0) {
		// Draw moves
			for(i=0; i<9; i++) {
				for(j=0; j<9; j++) {
					if(game0.board[i][j] > 0) {
						canvas.drawCircle(j*xx/10+xx/10, i*yy/10+yy/10, xx/20-xx/100, markPaint);
					}				
				}
			}
		// Draw cursor
			if(curCol>=0 && curRow>=0 && curCol<9 && curRow<9) {
				if(game0.board[curRow][curCol] == -1) {
					// Draw block cursor
					canvas.drawRect(curCol*xx/10+xx/20+xx/200, curRow*yy/10+yy/20+yy/200, 
							curCol*xx/10+xx/20+xx/10-xx/200, curRow*yy/10+yy/20+yy/10-yy/200, cursorPaint);
//						game0.scoringMoveCs = game0.scoringMoveCe = -1; 
//						game0.scoringMoveRs = game0.scoringMoveRe = -1;
//						game0.scoringMoveD0s = game0.scoringMoveD0e = -1; 
//						game0.scoringMoveD1s = game0.scoringMoveD1e = -1;
				}
			}
		// Draw current scoring move
			if(game0.getStatus() > 1) {
				int r = game0.movesSeq[game0.getStatus()-1] / 9;
				int c = game0.movesSeq[game0.getStatus()-1] % 9;
			// put a special mark on the last move?
				if(c>=0 && r>=0 && c<9 && r<9) {
					canvas.drawCircle(c*xx/10+xx/10, r*yy/10+yy/10, xx/60, scorPaint);
				}
				if(game0.movesScore[game0.getStatus()-1] > 0) {
					if(c>=0 && r>=0 && c<9 && r<9) {
						if(game0.scoringMoveCs>=0)
							canvas.drawLine(game0.scoringMoveCs*xx/10+3*xx/40, r*yy/10+yy/10,
									game0.scoringMoveCe*xx/10+5*xx/40, r*yy/10+yy/10, scorPaint);
						if(game0.scoringMoveRs>=0) 
							canvas.drawLine(c*xx/10+xx/10, game0.scoringMoveRs*yy/10+3*yy/40, 
									c*xx/10+xx/10, game0.scoringMoveRe*yy/10+5*yy/40, scorPaint);
						if(game0.scoringMoveD0s>=0) 
							canvas.drawLine(game0.scoringMoveD0s*xx/10+3*xx/40, (r-c+game0.scoringMoveD0s)*yy/10+3*yy/40, 
									game0.scoringMoveD0e*xx/10+5*xx/40, (r+game0.scoringMoveD0e-c)*yy/10+5*yy/40,
									scorPaint);
						if(game0.scoringMoveD1s>=0) 
							canvas.drawLine(game0.scoringMoveD1e*xx/10+3*xx/40, (r+c-game0.scoringMoveD1e)*yy/10+5*yy/40, 
									game0.scoringMoveD1s*xx/10+5*xx/40, (r+c-game0.scoringMoveD1s)*yy/10+3*yy/40,
									scorPaint);
					}
				}
			}
		}
		super.onDraw(canvas);
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

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Get the type of action this event represents
		int actionPerformed = event.getAction();
		if(actionPerformed==MotionEvent.ACTION_DOWN && (gameState>0 || gameState<1024)) {
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
			
			setBoardState(r, c);
			invalidate();  // only need it sometimes
		// Debug use
//			if(c>=0 && r>=0 && c<9 && r<9) {
//				this.setText("Col "+Integer.toString(c)+" , Row "+Integer.toString(r));
//			}
//			if(game0.getStatus() > 1)
//				if(game0.movesScore[game0.getStatus()-1] > 0) 
//					this.setText("+"+Integer.toString(game0.movesScore[game0.getStatus()-1]));

			return true;
		}
	
		return false;
	
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
	
	void setBoardState(int r, int c) {
		if(c!=curCol || r!=curRow)  // mark cursor position
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

	public void setGame0(GamePlay game0) {
		this.game0 = game0;
	}

	public void setGameState(int gameState) {
		this.gameState = gameState;
	}
	
	public GamePlay getGame0() {
		return game0;
	}
	
	public int getGameState() {
		return gameState;
	}
	

}


