package org.xluz.droidacts.sanLiuJiu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

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
		float xx=getMeasuredWidth();
		float yy=getMeasuredHeight();
		// Draw ruled lines
		int i=0;
		while (i <= 9) {
			canvas.drawLine(xx/20+xx/10*i, yy/20, xx/10*i+xx/20, yy-yy/20, linePaint);
			canvas.drawLine(xx/20, yy/10*i+yy/20,xx-xx/20, yy/10*i+yy/20, linePaint);
			i++;
		}
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
	 @Override
	  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    // The compass is a circle that fills as much space as possible.
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
	      // Return a default size of 200 if no bounds are specified.
	      result = 200;
	    } else {
	      // As you want to fill the available space
	      // always return the full available bounds.
	      result = specSize;
	    }
	    return result;
	  }

}


