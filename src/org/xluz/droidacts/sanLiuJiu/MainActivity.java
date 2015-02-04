package org.xluz.droidacts.sanLiuJiu;
/*
  A rendition of a childhood board game 3-6-9 

Copyright (c) 2014 Cecil Cheung
This software is released under the GNU General Public License version 3.
See, for example, "http://www.gnu.org/licenses/gpl.html".
*/

import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	SharedPreferences settingsPrefs;
	private TextView p1, p2, s1, s2, tt;
	private int debugMsg=1;
	private int gameSettings=0;
	/* 0: no game in progress
	 * lowest 4 bits: AI level 1 to 15
	 * bit 5: original rules
	 * bit 6: 2-player
	 */
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
		//menu.add (0, 3, 3, "Settings");
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
            startAGame(1);
            return true;
        case 2:
            Toast.makeText(this, "2-players", Toast.LENGTH_LONG).show();
            tt.setText("2-players");
            startAGame(2);
            return true;
        case R.id.action_settings:
        	Intent itt = new Intent(this, AppPreferenceActivity.class);
			startActivityForResult(itt, 3);
            return true;
		case R.id.about_app:
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Back from settings screen
		if(requestCode == 3 && resultCode == RESULT_OK) {
			// Update players' names if game in progress?
		}
		super.onActivityResult(requestCode, resultCode, data);
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
        settingsPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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
			int s = savedInstanceState.getInt("GameMode", -1);
			if(s>=0 && s<2048) {
				// check game mode and restore gameSettings
				this.gameSettings = s;
				if(savedInstanceState.containsKey("GameProgression")) {
					G0 = new GamePlay(savedInstanceState.getIntArray("GameProgression"));
					bd.setGame0(G0);
					bd.setGameState(s);
					if(this.debugMsg > 0)
						bd.setText("Game resumed: "+Integer.toString(G0.movesSeq[0]));
				// need to restore player names
					if(savedInstanceState.containsKey("P1Name8")) {	
						p1.setText(savedInstanceState.getCharSequence("P1Name8"));
					}
					if(savedInstanceState.containsKey("P2Name8")) {
						p2.setText(savedInstanceState.getCharSequence("P2Name8"));
					}
				// trigger the event handler
					dispatchTouchEvent(MotionEvent.obtain(
							  SystemClock.uptimeMillis(), SystemClock.uptimeMillis()+100, 
							  MotionEvent.ACTION_UP, 0, 0, 0)
							);
				} else {
					if(this.debugMsg > 0)
						bd.setText("Failed to restore last game");
				}
			} else {
				// invalid game settings or progress
				if(this.debugMsg > 0)
					bd.setText("No game to resume ");
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
		if(bd.getGameState()>=0 && G0!=null) {
			outState.putInt("GameMode", bd.getGameState());
			//outState.putInt("GameState", G0.getStatus());
			if(G0.getStatus()<82) G0.movesSeq[G0.getStatus()] = -1;  // mark end of seq.
			outState.putIntArray("GameProgression", G0.movesSeq);
			outState.putCharSequence("P1Name8", p1.getText());
			outState.putCharSequence("P2Name8", p2.getText());
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
			else if(G0.huseturn == 0) {
				p1.setBackgroundColor(Color.WHITE);
				// update scores
				s1.setText(Integer.toString(G0.scores1));
				s2.setText(Integer.toString(G0.scores2));
				tt.setText(p1.getText()+" scores +"+Integer.toString(G0.movesScore[s-1])); 
				// highlight player-2
				p2.setBackgroundColor(0xFFFFFF66);
				if(bd.getGameState()>0 && bd.getGameState()<64) {  // vs. AI
					bd.setGameState(bd.getGameState()+1024);
					dispatchAI(G0.movesSeq);
				}
			}
			else if(G0.huseturn == 1) {
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
				nextMov.setAIlevel(gameSettings%16);
				nextMov.go();// inf loop if level out-of-range
				bd.post(new Runnable(){
					public void run() {
						int m = nextMov.getTheMove();
						if(m >= 0 && G0.board[m/9][m%9] <= 0) {
							bd.setBoardState(m/9, m%9);
							bd.setBoardState(m/9, m%9);  // double tap
							bd.invalidate();
							bd.setGameState(bd.getGameState()-1024);
						} else {
							// no move? surrender?
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

	private void startAGame(int mode) {
		long Tnow = System.currentTimeMillis()/1000;  // since 1970
		Tnow -= 978307200L;  // since 2001
		
		// Get game settings from preference
		if(mode == 1) {  // 1 player
			gameSettings = Integer.parseInt(settingsPrefs.getString("LevelsOfDifficulty", "1"));
		} else { // 2 or more players
			gameSettings = 64;
		}
		if(settingsPrefs.getBoolean("oldRules", true)) {
			gameSettings += 32;
		}

		G0 = new GamePlay();
		G0.recordMove((int)(101+Tnow));
		bd.setGame0(G0);
		bd.setGameState(gameSettings);
		// reset scores
		s1.setText(Integer.toString(G0.scores1));
		s2.setText(Integer.toString(G0.scores2));
		p1.setBackgroundColor(Color.WHITE);
		p2.setBackgroundColor(Color.WHITE);
		p1.setText(sTrimTo8(settingsPrefs.getString("P1name", "P1")));
		if(mode == 1)
			p2.setText(R.string.Pname_logic);
		else
			p2.setText(sTrimTo8(settingsPrefs.getString("P2name", "P2")));

		if(this.debugMsg > 0)
			bd.setText("Game starts: " + Integer.toString(G0.movesSeq[0]));
		// trigger the event handler
		dispatchTouchEvent(MotionEvent.obtain(
				  SystemClock.uptimeMillis(), SystemClock.uptimeMillis()+100, 
				  MotionEvent.ACTION_UP, 0, 0, 0)
				);
	}

	private String sTrimTo8(String s0) {
		String s = s0.trim();
		int l = s.length();
		if(l < 9) return s;
		else return s.substring(0, 8);
	}
	
}
