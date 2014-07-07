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
	   
	   
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Get the type of action this event represents
		int actionPerformed = event.getAction();
		float x0=event.getX();
		float y0=event.getY();
//		Toast.makeText(this, "U have touched the board",Toast.LENGTH_LONG).show();
		// Return true if the event was handled.
		return true;
	}

}


