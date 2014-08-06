package org.xluz.droidacts.sanLiuJiu;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	GamePlay G0;
	//private int Bcolor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        CreateMenu(menu);
        return true;
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return MenuChoice(item);
	}
	
	private void CreateMenu(Menu menu)
	{
		MenuItem mnu1 = menu.add(0, 0, 0,"Intro");
		{
			
			//mnu1.setAlphabeticShortcut('a');
			mnu1.setIcon(R.drawable.ic_launcher);
		}
		MenuItem mnu2 = menu.add(0, 1, 1, "1-player game");
		{
			//mnu2.setAlphabeticShortcut('b');
			mnu2.setIcon(R.drawable.ic_launcher);
		}
		MenuItem mnu3 = menu.add(0, 2, 2, "2-player game");
		{
			//mnu3.setAlphabeticShortcut ('c');
			mnu3.setIcon(R.drawable.ic_launcher);
		}
		menu.add (0, 3, 3, "Settings");
	}
	
	private boolean MenuChoice(MenuItem item)
	{ 
        switch (item.getItemId()) {
        case 0:
        	AlertDialog.Builder introDialog = new AlertDialog.Builder(this);
			introDialog.setTitle(R.string.intro);
			introDialog.setMessage(R.string.instruction);
			introDialog.setPositiveButton("OK", null);
			introDialog.show();
            return true;
        case 1:
            Toast.makeText(this, "You vs. AI (to be done)",
                Toast.LENGTH_LONG).show();
            return true;
        case 2:
            Toast.makeText(this, "2-players", Toast.LENGTH_LONG).show();
            TextView tt = (TextView)findViewById(R.id.textView1);
            tt.setText("2-players");
            G0 = new GamePlay();
            GameBoard bd = (GameBoard)findViewById(R.id.editText3);
            bd.setGame0(G0);
            bd.setGameState(2);
            return true;
        case 3:
            Toast.makeText(this, "You clicked on Item 4",
                Toast.LENGTH_LONG).show();
            return true;
		case R.id.action_settings:
			AlertDialog.Builder aboutDialog1 = new AlertDialog.Builder(this);
			aboutDialog1.setTitle(R.string.msg_about);
			aboutDialog1.setMessage(R.string.msg_desc);
			aboutDialog1.setPositiveButton("OK", null);
			aboutDialog1.show();
			return true;
        }
        return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
	// events not dealt with by the views
		if(event.getAction() == MotionEvent.ACTION_UP && G0!=null) {
			int s = G0.getStatus();
			if(s == 0) {
				
			}
			else if(s > 81) {
				Toast.makeText(this, "Game ended", Toast.LENGTH_LONG).show();
			}
			else if(s%2==1) {
				TextView t1 = (TextView)findViewById(R.id.editText1);
				// highlight player-1
				t1.setBackgroundColor(0xFFFFFF66);
				TextView t2 = (TextView)findViewById(R.id.editText2);
				t2.setBackgroundColor(0xFFFFFFFF);
				// update score
				TextView s2 = (TextView)findViewById(R.id.textView3);
				s2.setText(Integer.toString(G0.scores2));
			}
			else if(s%2==0) {
				TextView t1 = (TextView)findViewById(R.id.editText1);
				t1.setBackgroundColor(0xFFFFFFFF);
				// update score
				TextView s1 = (TextView)findViewById(R.id.textView2);
				s1.setText(Integer.toString(G0.scores1));
				TextView t2 = (TextView)findViewById(R.id.editText2);
				// highlight player-2
				t2.setBackgroundColor(0xFFFFFF66);				
			}
		}
		return super.onTouchEvent(event);
	}    

}
