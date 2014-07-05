package org.xluz.droidacts.sanLiuJiu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
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
	    linePaint.setColor(0xEEF8E0A0);

	}
	@Override 
	public void onDraw (Canvas canvas) {
		super.onDraw(canvas);
	}
	
}


