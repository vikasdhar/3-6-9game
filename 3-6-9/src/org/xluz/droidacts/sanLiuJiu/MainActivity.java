package org.xluz.droidacts.sanLiuJiu;
/*
  A rendition of a childhood board game 3-6-9 

Copyright (c) 2014 Cecil Cheung
This software is released under the GNU General Public License version 3.
See, for example, "http://www.gnu.org/licenses/gpl.html".
*/

import java.io.*;
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
	GameBoard bd;
	GamePlay G0;
	private TextView p1, p2, s1, s2, tt;
	private int gameSettings=0;
	/* 0: no game in progress
	 * lowest 4 bits: AI level 1 to 15
	 * bit 5: original rules
	 * bit 6: 2-player
	 * bit 9:
	 * bit 10: AI routines in progress
	 * < 0 : error condition
	 */

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
		menu.add(0, 3, 3, "Resume last game");
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
        case 3:
            Toast.makeText(this, "Resuming last played", Toast.LENGTH_LONG).show();
            if(loadGameProgress() >= 0)
            	tt.setText("Previously...");
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
		if(myDebugLevel.Msg > 0)
			bd.setText(R.string.msg_release);
    }	

	@Override
	protected void onStart() {
		super.onStart();
		// make sure all the views are refreshed?
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if(savedInstanceState!=null) {
			int s = savedInstanceState.getInt("GameMode", -1);
			if(s>=0 && s<2048) {
				// check game mode and restore gameSettings
				this.gameSettings = s;
				if(savedInstanceState.containsKey("GameProgression")) {
					if((s/32)%2 == 0)
						G0 = new GamePlay(savedInstanceState.getIntArray("GameProgression"));
					else
						G0 = new GamePlay0(savedInstanceState.getIntArray("GameProgression"));
					bd.setGame0(G0);
					bd.setGameState(s);
					if(myDebugLevel.Msg > 0)
						bd.setText("Game resumed: "+Integer.toString(G0.movesSeq[0]));
				// need to restore player names
					if(savedInstanceState.containsKey("P1Name9")) {	
						p1.setText(savedInstanceState.getCharSequence("P1Name9"));
					}
					if(savedInstanceState.containsKey("P2Name9")) {
						p2.setText(savedInstanceState.getCharSequence("P2Name9"));
					}
				// trigger the event handler
					dispatchTouchEvent(MotionEvent.obtain(
							  SystemClock.uptimeMillis(), SystemClock.uptimeMillis()+100, 
							  MotionEvent.ACTION_UP, 0, 0, 0)
							);
				} else {
					if(myDebugLevel.Msg > 0)
						bd.setText("Failed to restore last game");
				}
			} else {
				// invalid game settings or progress
				if(myDebugLevel.Msg > 0)
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
			if(bd.getGameState() > 0)
				outState.putInt(SAVEGAMEKEY_MODE, bd.getGameState());
			else
				outState.putInt(SAVEGAMEKEY_MODE, this.gameSettings);
			if(G0.getStatus()<82) G0.movesSeq[G0.getStatus()] = -1;  // mark end of seq. may not need
			outState.putIntArray(SAVEGAMEKEY_MOVES, G0.movesSeq);
			outState.putCharSequence(SAVEGAMEKEY_NAME1, p1.getText());
			outState.putCharSequence(SAVEGAMEKEY_NAME2, p2.getText());
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStop() {
		if(bd.getGameState()>=0 && G0!=null) {
			saveGameProgress();
		}
		super.onStop();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
	// events not dealt with by the views
		if(event.getAction() == MotionEvent.ACTION_UP && G0!=null) {
			int s = G0.getStatus();
			if(s > 81) {
				p1.setBackgroundColor(0x00FFFFFF);
				p2.setBackgroundColor(0x00FFFFFF);
				s1.setText(Integer.toString(G0.getScores(1)));
				s2.setText(Integer.toString(G0.getScores(2)));
				bd.setGameState(0);
				Toast.makeText(this, "Game ended", Toast.LENGTH_LONG).show();
			}
			if(s == 0) {
				
			}
			else if(s == 1) {
				// initial display
				p1.setBackgroundColor(0xFFFFFF66);
				p2.setBackgroundColor(Color.WHITE);
			}
			else if(bd.getGameState()>0 && G0.huseturn==0) {
				// highlight player-2
				p2.setBackgroundColor(0xFFFFFF66);
				p1.setBackgroundColor(Color.WHITE);
				// update scores
				s1.setText(Integer.toString(G0.getScores(1)));
				s2.setText(Integer.toString(G0.getScores(2)));
				if((bd.getGameState()/32)%2 == 0) {
					tt.setText(p1.getText()+" +"+Integer.toString(G0.getLastscore()));
				} else {  // original rules
					if(G0.getLastscore() > 0) {
						tt.setText(p2.getText()+" +"+Integer.toString(G0.getLastscore()));
					} else {
						tt.setText(" ");
					}
				}
				// if 1-player game 
				if(bd.getGameState()<64) {  // vs. AI
					bd.setGameState(bd.getGameState()+1024);
					dispatchAI(G0.movesSeq);
				}
			}
			else if(bd.getGameState()>0 && G0.huseturn==1) {
				// highlight player-1
				p1.setBackgroundColor(0xFFFFFF66);
				p2.setBackgroundColor(Color.WHITE);
				// update scores
				s1.setText(Integer.toString(G0.getScores(1)));
				s2.setText(Integer.toString(G0.getScores(2)));
				if((bd.getGameState()/32)%2 == 0) {
					tt.setText(p2.getText()+" +"+Integer.toString(G0.getLastscore()));
				} else {  // original rules
					if(G0.getLastscore() > 0) {
						tt.setText(p1.getText()+" +"+Integer.toString(G0.getLastscore()));
					} else {
						tt.setText(" ");
					}
				}
			}
		}
		
		return super.onTouchEvent(event);
	}

	
	static final String STATUS_STORAGE = "GameInPlay";
	static final String SAVEGAMEKEY_MODE = "GameMode";
	static final String SAVEGAMEKEY_MOVES = "GameProgression";
	static final String SAVEGAMEKEY_NAME1 = "P1Name9";
	static final String SAVEGAMEKEY_NAME2 = "P2Name9";
	
	void saveGameProgress() {
		if(bd.getGameState()<0 || G0==null) {
			return ;
		}
	//This function is not fully tested
		if(myDebugLevel.Mode) {
			ObjectOutputStream outob;
			try {
				outob = new ObjectOutputStream(openFileOutput("gameinplay", 0));
				outob.writeChars(SAVEGAMEKEY_MODE);
				outob.writeInt(this.gameSettings);
				outob.writeChars(SAVEGAMEKEY_MOVES);
				outob.writeObject(G0.movesSeq);
				outob.writeChars(SAVEGAMEKEY_NAME1);
				outob.writeChars(p1.getText().toString());
				outob.writeChars(SAVEGAMEKEY_NAME2);
				outob.writeChars(p2.getText().toString());
				outob.flush();
				outob.close();
			} catch (Exception e) {
				//Log.d("savedGame","Error in saving game to internal file");
				e.printStackTrace();
			}
		}
		
		SharedPreferences savedGame = getSharedPreferences(STATUS_STORAGE, Activity.MODE_PRIVATE);
		SharedPreferences.Editor outxml = savedGame.edit();
		if(bd.getGameState() > 0)
			outxml.putInt(SAVEGAMEKEY_MODE, bd.getGameState());
		else
			outxml.putInt(SAVEGAMEKEY_MODE, this.gameSettings);
		for(int j=0; j<82; j++) {
			outxml.putInt(SAVEGAMEKEY_MOVES+"_"+Integer.toString(j), G0.movesSeq[j]);
		}
		outxml.putString(SAVEGAMEKEY_NAME1, p1.getText().toString());
		outxml.putString(SAVEGAMEKEY_NAME2, p2.getText().toString());
		outxml.putInt(SAVEGAMEKEY_MOVES, G0.getStatus());
		outxml.apply();
	}
	
	int loadGameProgress() {
		SharedPreferences savedGame = getSharedPreferences(STATUS_STORAGE, Activity.MODE_PRIVATE);
		int s = savedGame.getInt(SAVEGAMEKEY_MODE, -1);
		if(s>=0 && s<2048) {
			// check game mode and restore gameSettings
			this.gameSettings = s;
			if(savedGame.contains(SAVEGAMEKEY_MOVES)) {
				int p[] = new int[82];
				for(int j=0; j<82; j++) {
					//if(savedGame.contains("GameProgression_"+Integer.toString(j))) 
						p[j] = savedGame.getInt(SAVEGAMEKEY_MOVES+"_"+Integer.toString(j), -1);
				}
				if((s/32)%2 == 0)  //what to do for finished game?
					G0 = new GamePlay(p);
				else
					G0 = new GamePlay0(p);
				bd.setGame0(G0);
				bd.setGameState(s);
				if(myDebugLevel.Msg > 0)
					bd.setText("Game resumed: "+Integer.toString(G0.movesSeq[0]));
			// need to restore player names
				if(savedGame.contains(SAVEGAMEKEY_NAME1)) {	
					p1.setText(savedGame.getString("P1Name9","P 1"));
				}
				if(savedGame.contains(SAVEGAMEKEY_NAME2)) {
					p2.setText(savedGame.getString("P2Name9","P 2"));
				}
			// trigger the event handler
				dispatchTouchEvent(MotionEvent.obtain(
						  SystemClock.uptimeMillis(), SystemClock.uptimeMillis()+100, 
						  MotionEvent.ACTION_UP, 0, 0, 0)
						);
			} else {
				s = -1;
				if(myDebugLevel.Msg > 0)
					bd.setText("Failed to restore previous game");
			}
		} else {
			// invalid game settings or progress
			if(myDebugLevel.Msg > 0)
				bd.setText("No game to resume.");
		}
		return s;
	}
	
	private void dispatchAI(final int[] gameMoves) {
		new Thread(new Runnable(){
			public void run() {
				final BestMove nextMov;
				if((gameSettings/32)%2 == 0) 
					nextMov = new BestMove(gameMoves);
				else
					nextMov = new BestMove0(gameMoves);
				nextMov.setAIlevel(gameSettings%16);
				nextMov.go();
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
							bd.setText("No move found");
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
		if(settingsPrefs.getBoolean("oldRules", false)) {
			gameSettings += 32;
			G0 = new GamePlay0();
		} else {
			G0 = new GamePlay();
		}
		G0.recordMove((int)(101+Tnow));
		bd.setGame0(G0);
		bd.setGameState(gameSettings);
		// reset scores
		s1.setText(Integer.toString(G0.getScores(1)));
		s2.setText(Integer.toString(G0.getScores(2)));
		p1.setBackgroundColor(Color.WHITE);
		p2.setBackgroundColor(Color.WHITE);
		p1.setText(sTrimTo9(settingsPrefs.getString("P1name", "P1")));
		String[] AInames = getResources().getStringArray(R.array.LevelsDifficulty);
		if(mode == 1)
			p2.setText(AInames[gameSettings%16-1]);  //careful when modifying the strings
		else
			p2.setText(sTrimTo9(settingsPrefs.getString("P2name", "P2")));

		if(myDebugLevel.Msg > 0)
			bd.setText("Game starts: " + Integer.toString(G0.movesSeq[0]));
		// trigger the event handler
		dispatchTouchEvent(MotionEvent.obtain(
				  SystemClock.uptimeMillis(), SystemClock.uptimeMillis()+100, 
				  MotionEvent.ACTION_UP, 0, 0, 0)
				);
	}

	private String sTrimTo9(String s0) {
		String s = s0.trim();
		int l = s.length();
		if(l < 10) return s;
		else return s.substring(0, 9);
	}
	
}
