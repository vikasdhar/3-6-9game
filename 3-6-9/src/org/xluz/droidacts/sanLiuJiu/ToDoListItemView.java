package org.xluz.droidacts.sanLiuJiu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

public class ToDoListItemView extends TextView {
	private Paint marginPaint;
	private Paint linePaint;
	private int paperColor;
	private float margin;
	
	public ToDoListItemView (Context context, AttributeSet ats, int ds) {
		super (context, ats, ds);
		init();
	}
	
	public ToDoListItemView (Context context) {
		super (context);
		init();
	}
	public ToDoListItemView (Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		marginPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    marginPaint.setColor(0xFF0000FF);
	    linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    linePaint.setColor(0x90FF00FF);

	}
	@Override 
	public void onDraw (Canvas canvas) {
		float xx=getMeasuredWidth()-5;
		float yy=getMeasuredHeight()-5;
		// Draw ruled lines
	    canvas.drawLine(0, 0, 0, yy, linePaint);
	    canvas.drawLine(0, 0, xx, 0, linePaint);
	   
	    canvas.drawLine(xx/9, 0, xx/9, yy, linePaint);
	    canvas.drawLine(0, getMeasuredHeight()/9,
	                       getMeasuredWidth(), getMeasuredHeight()/9,
	                       linePaint);
	    canvas.drawLine(xx/9*2, 0, xx/9*2, yy, linePaint);
	    canvas.drawLine(0, getMeasuredHeight()/9*2,
	                       getMeasuredWidth(), getMeasuredHeight()/9*2,
	                       linePaint);
	    canvas.drawLine(xx/9*3, 0, xx/9*3, yy, linePaint);
	    canvas.drawLine(0, getMeasuredHeight()/9*3,
	                       getMeasuredWidth(), getMeasuredHeight()/9*3,
	                       linePaint);
	    canvas.drawLine(xx/9*4, 0, xx/9*4, yy, linePaint);
	    canvas.drawLine(0, getMeasuredHeight()/9*4,
	                       getMeasuredWidth(), getMeasuredHeight()/9*4,
	                       linePaint);
	    canvas.drawLine(xx/9*5, 0, xx/9*5, yy, linePaint);
	    canvas.drawLine(0, getMeasuredHeight()/9*5,
	                       getMeasuredWidth(), getMeasuredHeight()/9*5,
	                       linePaint);
	    canvas.drawLine(xx/9*6, 0, xx/9*6, yy, linePaint);
	    canvas.drawLine(0, getMeasuredHeight()/9*6,
	                       getMeasuredWidth(), getMeasuredHeight()/9*6,
	                       linePaint);
	    canvas.drawLine(xx/9*7, 0, xx/9*7, yy, linePaint);
	    canvas.drawLine(0, getMeasuredHeight()/9*7,
	                       getMeasuredWidth(), getMeasuredHeight()/9*7,
	                       linePaint);
	    canvas.drawLine(xx/9*8, 0, xx/9*8, yy, linePaint);
	    canvas.drawLine(0, getMeasuredHeight()/9*8,
	                       getMeasuredWidth(), getMeasuredHeight()/9*8,
	                       linePaint);
	    canvas.drawLine(xx/9*9, 0, xx/9*9, yy, linePaint);
	    canvas.drawLine(0, yy/9*9,
	                       getMeasuredWidth(), yy/9*9,
	                       linePaint);
	   
	   super.onDraw(canvas);
	}
	
//	@Override
//	  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//	    int measuredHeight = measureHeight(heightMeasureSpec);
//	    int measuredWidth = measureWidth(widthMeasureSpec);
//
//	    setMeasuredDimension(measuredHeight, measuredWidth);
//	  }
//
//	  private int measureHeight(int measureSpec) {
//	    int specMode = MeasureSpec.getMode(measureSpec);
//	    int specSize = MeasureSpec.getSize(measureSpec);
//
//	    //  Default size if no limits are specified.
//	    int result = 500;
//
//	    if (specMode == MeasureSpec.AT_MOST) {
//	      // Calculate the ideal size of your
//	      // control within this maximum size.
//	      // If your control fills the available
//	      // space return the outer bound.
//	      result = specSize;
//	    } else if (specMode == MeasureSpec.EXACTLY) {
//	      // If your control can fit within these bounds return that value.
//	      result = specSize;
//	    }
//	    return result;
//	  }
//
//	  private int measureWidth(int measureSpec) {
//	    int specMode = MeasureSpec.getMode(measureSpec);
//	    int specSize = MeasureSpec.getSize(measureSpec);
//
//	    //  Default size if no limits are specified.
//	    int result = 500;
//
//	    if (specMode == MeasureSpec.AT_MOST) {
//	      // Calculate the ideal size of your control
//	      // within this maximum size.
//	      // If your control fills the available space
//	      // return the outer bound.
//	      result = specSize;
//	    } else if (specMode == MeasureSpec.EXACTLY) {
//	      // If your control can fit within these bounds return that value.
//	      result = specSize;
//	    }
//	    return result;
//	  }
	  

	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Get the type of action this event represents
		int actionPerformed = event.getAction();
		if(actionPerformed==MotionEvent.ACTION_DOWN) {
			int x0=(int) event.getX();
			int y0=(int) event.getY();
			TextView tt=(TextView)findViewById(R.id.editText1);
			this.setText("At "+Integer.toString(x0)+","+Integer.toString(y0));
		}
		//tt.setText("At "+Integer.toString(x0)+","+Integer.toString(y0));
		// Return true if the event was handled.
		return true;
	}

}


