package org.xluz.droidacts.sanLiuJiu;
/*
  A rendition of a childhood board game 3-6-9 

Copyright (c) 2014 Cecil Cheung
This software is released under the GNU General Public License version 3.
See, for example, "http://www.gnu.org/licenses/gpl.html".
*/

import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private  TextView p1, p2, s1, s2, tt;
	GameBoard bd;
	GamePlay G0;

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

    private void CreateMenu(Menu menu) {
		menu.add(0, 0, 0,"Intro");
		menu.add(0, 1, 1, "1-player game");
		menu.add(0, 2, 2, "2-player game");
		menu.add (0, 3, 3, "Settings");
	}

    private boolean MenuChoice(MenuItem item) { 
        switch (item.getItemId()) {
        case 0:
        	AlertDialog.Builder introDialog = new AlertDialog.Builder(this);
			introDialog.setTitle(R.string.intro);
			introDialog.setMessage(R.string.instruction);
			introDialog.setPositiveButton("OK", null);
			introDialog.show();
            return true;
        case 1:
            Toast.makeText(this, "U vs Logic", Toast.LENGTH_LONG).show();
            tt.setText("Against your phone");
            G0 = new GamePlay();
            bd.setGame0(G0);
            bd.setGameState(1);
            // reset scores
			s1.setText(Integer.toString(G0.scores1));
			s2.setText(Integer.toString(G0.scores2));
			p1.setBackgroundColor(Color.WHITE);
			p2.setBackgroundColor(Color.WHITE);
            p2.setText("Logic");
			bd.setText("Game starts: "+Integer.toString(G0.movesSeq[0]));
            return true;
        case 2:
            Toast.makeText(this, "2-players", Toast.LENGTH_LONG).show();
            tt.setText("2-players");
            G0 = new GamePlay();
            bd.setGame0(G0);
            bd.setGameState(2);
            // reset scores
			s1.setText(Integer.toString(G0.scores1));
			s2.setText(Integer.toString(G0.scores2));
			p1.setBackgroundColor(Color.WHITE);
			p2.setBackgroundColor(Color.WHITE);
			p2.setText("Player 2");
			bd.setText("Game starts: "+Integer.toString(G0.movesSeq[0]));
            return true;
        case 3:
            Toast.makeText(this, "Nothing to set (yet)",
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		bd = (GameBoard)findViewById(R.id.editText3);
		tt = (TextView)findViewById(R.id.textView1);
		p1 = (TextView)findViewById(R.id.editText1);
		p2 = (TextView)findViewById(R.id.editText2);
		s1 = (TextView)findViewById(R.id.textView2);
		s2 = (TextView)findViewById(R.id.textView3);
    }	

	@Override
	protected void onStart() {
		super.onStart();
		// make sure all the views are refreshed?
//		if(G0 != null) {
//			s1.setText(Integer.toString(G0.scores1));
//			s2.setText(Integer.toString(G0.scores2));
////			s1.invalidate();
////			s2.invalidate();
//		}
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if(savedInstanceState!=null) {
			int s = savedInstanceState.getInt("GameMode", 0);
			if(s>=0) {
				if(savedInstanceState.containsKey("GameProgression")) {
					G0 = new GamePlay(savedInstanceState.getIntArray("GameProgression"));
					bd.setGame0(G0);
					bd.setGameState(s);
					bd.setText("Game resumed: "+Integer.toString(G0.movesSeq[0]));
				// trigger the event handler
					dispatchTouchEvent(MotionEvent.obtain(
							  SystemClock.uptimeMillis(), SystemClock.uptimeMillis()+100, 
							  MotionEvent.ACTION_UP, 0, 0, 0)
							);
				}
			}
		}
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("GameMode", bd.getGameState());
		if(bd.getGameState()>=0 && G0!=null) {
			outState.putInt("GameState", G0.getStatus());
			if(G0.getStatus()<82) G0.movesSeq[G0.getStatus()] = -1;  // mark end of seq.
			outState.putIntArray("GameProgression", G0.movesSeq);
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
	// events not dealt with by the views
		if(event.getAction() == MotionEvent.ACTION_UP && G0!=null) {
			int s = G0.getStatus();
			if(s == 0) {
				
			}
			else if(s%2==0) {
				p1.setBackgroundColor(Color.WHITE);
				// update scores
				s1.setText(Integer.toString(G0.scores1));
				s2.setText(Integer.toString(G0.scores2));
				tt.setText("Player 1 scores +"+Integer.toString(G0.movesScore[s-1])); 
				// highlight player-2
				p2.setBackgroundColor(0xFFFFFF66);
				if(bd.getGameState()==1) {  // vs. AI
					bd.setGameState(102);
					dispatchAI(G0.movesSeq);
				}
			}
			else if(s%2==1) {
				// highlight player-1
				p1.setBackgroundColor(0xFFFFFF66);
				p2.setBackgroundColor(Color.WHITE);
				// update scores
				s1.setText(Integer.toString(G0.scores1));
				s2.setText(Integer.toString(G0.scores2));
				tt.setText(p2.getText()+" scores +"+Integer.toString(G0.movesScore[s-1])); 
			}
			if(s > 81) {
				Toast.makeText(this, "Game ended", Toast.LENGTH_LONG).show();
				p1.setBackgroundColor(0x00FFFFFF);
				p2.setBackgroundColor(0x00FFFFFF);
				bd.setGameState(0);
			}
		}
		
		return super.onTouchEvent(event);
	}

	private void dispatchAI(final int[] gameMoves) {
		new Thread(new Runnable(){
			public void run() {
				final BestMove nextMov = new BestMove(gameMoves);
				bd.post(new Runnable(){
					public void run() {
						int m = nextMov.getTheMove();
						if(m >= 0 && G0.board[m/9][m%9] <= 0) {
							bd.setBoardState(m/9, m%9);
							bd.setBoardState(m/9, m%9);  // double tap
							bd.invalidate();
							bd.setGameState(1);
						}
					// send a fake touch event to trigger the main UI
						dispatchTouchEvent(MotionEvent.obtain(
						  SystemClock.uptimeMillis(), SystemClock.uptimeMillis()+100, 
						  MotionEvent.ACTION_UP, 0, 0, 0)
						);
					}
				});
			}
		}).start();
// may switch to AsyncTask class later if progress update is needed				

	}    

}
