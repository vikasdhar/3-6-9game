package xluz.droidacts.sanLiuJiu;
/*
  A rendition of a childhood board game 3-6-9 

Copyright (c) 2014 Cecil Cheung
This software is released under the GNU General Public License version 3.
See, for example, "http://www.gnu.org/licenses/gpl.html".
*/

import java.io.*;
import java.util.Scanner;
import android.util.Log;
import android.os.Bundle;
import android.content.DialogInterface.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("unused")

public class MainActivity extends Activity {
	SharedPreferences settingsPrefs;
	GameBoard bd;
	GamePlay G0;
	private TextView p1, p2, s1, s2, tt;
	private int UIoptions=0;
	private int gameSettings=0;
	/* 0: no game in progress
	 * lowest 4 bits: AI level 1 to 15
	 * bit 4: 
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
      // more menu items
		menu.add(0, 1, 1, "1-player game");
		menu.add(0, 2, 2, "2-player game");
		menu.add(0, 3, 3, "Resume last game");
		menu.add(0, 4, 4,"Send game");
        return true;
    }

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(UIoptions/16%2 == 1) {
			menu.findItem(4).setVisible(true);
		} else {
			menu.findItem(4).setVisible(false);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
        switch (item.getItemId()) {
        case 4:
        	AlertDialog.Builder introDialog = new AlertDialog.Builder(this);
        	introDialog.setTitle(R.string.send_game);
        	introDialog.setMessage(R.string.msg_emailgame);
        	introDialog.setNegativeButton("Cancel", null);
        	introDialog.setPositiveButton("Send", new OnClickListener() {
        		public void onClick(DialogInterface dialog, int arg1) {
        			mailGameProgress();
        		}
        	});
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
			AlertDialog.Builder aboutDialog = new AlertDialog.Builder(this);
			aboutDialog.setTitle(R.string.msg_about);
			aboutDialog.setMessage(R.string.msg_desc);
			aboutDialog.setPositiveButton("OK", null);
			aboutDialog.show();
			return true;
		case R.id.howtoplay:
			AlertDialog.Builder aboutDialog1 = new AlertDialog.Builder(this);
			aboutDialog1.setTitle(R.string.intro);
			aboutDialog1.setMessage(R.string.instruction);
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
		if(myDebugLevel.Msg > 0) bd.setText(R.string.msg_release);
		if(myDebugLevel.Msg > 1) UIoptions += 16;

		// OS 4.2+ may not need this extra text scaling
		if(android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			// Find out when the screen is finished drawing
			final LinearLayout wDisp = (LinearLayout)findViewById(R.id.blankDisplay);
			ViewTreeObserver obs = wDisp.getViewTreeObserver();
			obs.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {	
				@SuppressWarnings("deprecation")
				@Override
				public void onGlobalLayout() {
					wDisp.getViewTreeObserver().removeGlobalOnLayoutListener(this); 
					float sc = 1.0f;                   
					if(wDisp.getMeasuredWidth() > 1000) {
						sc = 3;
					}
					else if(wDisp.getMeasuredWidth() > 700) {
						sc = 2;
					}
					else if(wDisp.getMeasuredWidth() > 450) {
						sc = 1.4f;
					}
					if(sc > 1.0) {                 // based on 320px width
						tt.setTextSize(tt.getTextSize()*sc);
						p1.setTextSize(p1.getTextSize()*sc);
						p2.setTextSize(p2.getTextSize()*sc);
						s1.setTextSize(s1.getTextSize()*sc);
						s2.setTextSize(s2.getTextSize()*sc);
					}
					
					if(myDebugLevel.Msg > 0) 
						Log.d("UI_info1", "Screen width:"+
								Integer.toString(wDisp.getMeasuredWidth())+"  "+
								Integer.toString(wDisp.getWidth())+" ->"+Float.toString(sc));
				} 
			});
		}
	}	

	@Override
	protected void onStart() {
		super.onStart();
        settingsPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(!settingsPrefs.contains("PrefsVersion")) {
        	SharedPreferences.Editor out = settingsPrefs.edit();
        	out.putInt("PrefsVersion", 1);
        	out.apply();
        }
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if(savedInstanceState!=null) {
			int s = savedInstanceState.getInt(SAVEGAMEKEY_MODE, -1);
			if(s>=0 && s<2048) {
				// check game mode and restore gameSettings
				this.gameSettings = s;
				if(savedInstanceState.containsKey(SAVEGAMEKEY_MOVES)) {
					if((s/32)%2 == 0)
						G0 = new GamePlay(savedInstanceState.getIntArray(SAVEGAMEKEY_MOVES));
					else
						G0 = new GamePlay0(savedInstanceState.getIntArray(SAVEGAMEKEY_MOVES));
					bd.setGame0(G0);
					bd.setGameState(s);
					if(myDebugLevel.Msg > 0)
						bd.setText("Game resumed: "+Integer.toString(G0.movesSeq[0]));
				// need to restore player names
					if(savedInstanceState.containsKey(SAVEGAMEKEY_NAME1)) {	
						p1.setText(savedInstanceState.getCharSequence(SAVEGAMEKEY_NAME1));
					}
					if(savedInstanceState.containsKey(SAVEGAMEKEY_NAME2)) {
						p2.setText(savedInstanceState.getCharSequence(SAVEGAMEKEY_NAME2));
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
		//if(bd.getGameState()>=0 && G0!=null) {
			saveGameProgress();
		//}
		super.onStop();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		TextView pn[] = { p1, p2 };
		
	// events not dealt with by the views
		if(event.getAction() == MotionEvent.ACTION_UP && G0!=null) {
			int s = G0.getStatus();
			if(bd.getGameState() > 0) {
				// update scores
				if(s > 1) {
					s1.setText(Integer.toString(G0.getScores(1)));
					s2.setText(Integer.toString(G0.getScores(2)));
					if((bd.getGameState()/32)%2 == 0) 
						tt.setText(pn[G0.huseturn].getText()+" +"+Integer.toString(G0.getLastscore()));
					else {  // original rules
						if(getScoresSeq().compareTo(" ") != 0)
							tt.setText(pn[(G0.huseturn+1)%2].getText()+getScoresSeq());
						else if(tt.getText().toString().startsWith(pn[(G0.huseturn+1)%2].getText().toString()))
							tt.setText("  ");
					}
				}
				
				if(s <= 0) {
					// should not happen
				}
				else if(s == 1) {
					// initial display
					p1.setBackgroundColor(0xFFFFFF66);
					p2.setBackgroundColor(Color.WHITE);
				}
				else if(s > 81) {
					// just ended
					p1.setBackgroundColor(0x00FFFFFF);
					p2.setBackgroundColor(0x00FFFFFF);
					saveGameProgress();
					if(gameSettings < 64 && gameSettings%16 >= 4 && G0.getScores(1) > G0.getScores(2)) {
						Toast.makeText(this, R.string.msg_winlogic,	Toast.LENGTH_LONG).show();
						UIoptions += 16;
					}
					else
						Toast.makeText(this, R.string.msg_gameend, Toast.LENGTH_LONG).show();
					bd.setGameState(0);
				}
				else {
					pn[(G0.huseturn+1)%2].setBackgroundColor(0xFFFFFF66);
					pn[G0.huseturn].setBackgroundColor(Color.WHITE);

					// if 1-player game 
					if(bd.getGameState()<64 && G0.huseturn==0) {  // vs. AI
						bd.setGameState(bd.getGameState()+1024);
						dispatchAI(G0.movesSeq);
					}
				}
				
			}
			else {
				if(s > 81) {
					Toast.makeText(this, R.string.hello_world, Toast.LENGTH_LONG).show();
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
	static final String STATUS_UIOPTIONS = "UIoptions";
	
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
				Log.d("savedGame","Error in saving game to internal file");
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
		outxml.putInt(STATUS_UIOPTIONS, UIoptions);
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
				bd.setText("No game to resume!");
		}
		return s;
	}
	
	void mailGameProgress() {
		String mySavedGame;
		File mySavedGameF = new File(getApplicationInfo().dataDir,"shared_prefs/"+STATUS_STORAGE+".xml");
		if(myDebugLevel.Msg > 1)
			Log.d("sendGame", mySavedGameF.getAbsolutePath());
		try {
			mySavedGame = new Scanner(mySavedGameF).useDelimiter("\\A").next();
		} catch (FileNotFoundException e) {
			mySavedGame = "";
		}
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"support@transluminance.com"});
		i.putExtra(Intent.EXTRA_SUBJECT, "user game report");
		i.putExtra(Intent.EXTRA_TEXT   , "<!- Game moves below: -->\n"+mySavedGame);
		//i.putExtra(Intent.EXTRA_STREAM, mySavedGame.toURI());
		//i.setData(Uri.parse("mailto:default@recipient.com"));
		//i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(Intent.createChooser(i, "Send game progress"));
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
		Tnow -= 978307200L;                           // since 2001
		
		// Get game settings from preference
		if(mode == 1) {  // 1 player
			gameSettings = Integer.parseInt(settingsPrefs.getString("LevelsOfDifficulty", "1"));
		} else { // 2 or more players
			gameSettings = 64;
		}
		if(settingsPrefs.getBoolean("oldRules", false)) {
			gameSettings += 32;
			bd.setText("Classic game");
			G0 = new GamePlay0();
		} else {
			bd.setText("Modern game");
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
			p2.setText(AInames[gameSettings%16-1]);   //careful when modifying the strings
		else
			p2.setText(sTrimTo9(settingsPrefs.getString("P2name", "P2")));

		if(myDebugLevel.Msg > 0)
			bd.setText(bd.getText()+" : " + Integer.toString(G0.movesSeq[0]));
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
	
/* Produces a string show scores sequence in current moves
 * only make sense under original play rules
 */
	private String getScoresSeq() {
		StringBuffer scoresMsg = new StringBuffer(22);
		scoresMsg.append(" ");
		if(G0 != null && G0.getLastscore() > 0) {
			int k = G0.getStatus() - 1;
			for(int n=0 ; n < 7 && k>0 && G0.movesScore[k]>0; n++, k--) {
				scoresMsg.insert(0, G0.movesScore[k]);
				scoresMsg.insert(0, "+");
			}
			int p = 0;
			for( ; k>0 && G0.movesScore[k]>0; k--) {
				p += G0.movesScore[k];
			}
			if(p > 0) {
				scoresMsg.insert(0, p);
				scoresMsg.insert(0, "+");				
			}
		}
		return scoresMsg.toString();
	}
	
}
