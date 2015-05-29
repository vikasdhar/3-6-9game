package xluz.droidacts.sanLiuJiu;

import java.util.ArrayList;
import android.util.Log;

/* 
Find the best move available under original rules

Copyright (c) 2015 Cecil Cheung
This software is released under the GNU General Public License version 3.
See, for example, "http://www.gnu.org/licenses/gpl.html".
*/

@SuppressWarnings("unused")

// make class public for unit tests 
class BestMove0 extends BestMove {
	public static final int MOVESLISTSIZ = 124;
	private GamePlay0 board0;
	private int theMove, scoringFlag;
	private int Lps[];
	private static int[] theMoves = new int[MOVESLISTSIZ] ;

	public BestMove0(int[] moves) {
		//super();
		board0 = new GamePlay0(moves);
		this.theMove = -1;
		theMoves[0] = 0;                // [0] points to next move in the sequence
		Lps = new int[MOVESLISTSIZ];    // [0] stores total points, then the locations
	}

	@Override
	public int getTheMove() {
		return this.theMove;
	}

	@Override
	public int go() {
		this.theMove = -1;
		if(theMoves[0] > 0) {        // previously found move sequence
			if(myDebugLevel.Mode) 
				Log.d("moves seq","Move:"+Integer.toString(theMoves[0]));
			this.theMove = theMoves[theMoves[0]];
			theMoves[0]++;
			if(theMoves[theMoves[0]] < 0) theMoves[0] = 0;
			try {
				Thread.sleep(1200);  // insert some delay so user can see the moves
			} catch (InterruptedException e) {}
		}
		else if(super.getAIlevel() == 1) {
			long t0 = System.currentTimeMillis() + 1000;
			if(System.currentTimeMillis()%4 == 0) {  // about 1 in 4 chances
				this.theMove = AI0randomPlay();
			}
			else {
				AI1985a();
			}
			try {                    // so each move takes the same amount of time
				while(System.currentTimeMillis() < t0) Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
		else if(super.getAIlevel() == 2) {
			long t0 = System.currentTimeMillis() + 1200;
			AI1985a();
			t0 -= System.currentTimeMillis();
			try {                    // each move takes the same amount of time
				if(t0 > 0) Thread.sleep(t0);
			} catch (InterruptedException e) {}
			
		}
		else if(super.getAIlevel() == 3) {
			long t0 = System.currentTimeMillis() + 1500;
			AI1985a();
			if(this.scoringFlag == 6) AI1985b();
			try {
				t0 -= System.currentTimeMillis();
				if(t0 > 0) Thread.sleep(t0);
			} catch (InterruptedException e) {}
			
		}
		else if(super.getAIlevel() == 4) {
			long t0 = System.currentTimeMillis() + 1600;
			// New algorithm
			AI2015a();
			try {
				while(System.currentTimeMillis() < t0) Thread.sleep(200);
			} catch (InterruptedException e) {}
			
		}
		else {
			this.theMove = AI0randomPlay();
			try {
				Thread.sleep(600);  // insert some delay for testing
			} catch (InterruptedException e) {}			
		}
		return theMove;
	}

/* Find all the scoring moves
 * returns total points and scoring moves in Lps,
 * also mark the scoring moves on the board
 */
	private void findpts() {
		Lps[1] = -1;
		Lps[0] = 0;
		int m=1, sc;
		for(int i=0; i<81; i++) {
			if(board0.board[i/9][i%9] < 1) {     // location not occupied
				sc = board0.checkScores(i);
				board0.board[i/9][i%9] = sc - 100;
				if(sc > 0) {
					Lps[0] += sc;
					Lps[m] = i;
					m++;
					Lps[m] = -1;                 // mark end of array
				}
			}
		}
	}
	
/* Rewrite of the original 1985 algorithm
 * part A: find safe moves
 */
	private void AI1985a() {
		int ptsGive[] = new int[81];
		findpts();
		if(myDebugLevel.Msg > 1) {
			Log.d("AI1985", "Rnd0: "+Integer.toString(Lps[0])+
					", "+Integer.toString(Lps[1])+
					", "+Integer.toString(Lps[2]));
		}
		if(Lps[0] == 0) {       // multiple 0 point moves
			this.scoringFlag = 2;
			for(int n=0; n<81; n++) {  // set 1 location, find points given away
				ptsGive[n] = 999;
				if(board0.board[n/9][n%9] > 0) continue;
				board0.board[n/9][n%9] = 100;
				findpts();
				if(Lps[0] > 0) {       //remove conflicting moves & add all pts
					int s, i, j;
					ptsGive[n] = 0;
					for(i=0; i<9; i++) for(j=0; j<9; j++) {
						if(board0.board[i][j] < -90) {
							s = board0.board[i][j] + 100;
							if(s == 0) continue;
							ptsGive[n] += s;
							if(i+s < 9) 
								if(board0.board[i+s][j]==board0.board[i][j])
									board0.board[i+s][j] = 0;
							if(j+s < 9) 
								if(board0.board[i][j+s]==board0.board[i][j])
									board0.board[i][j+s] = 0;
						}
					}
				}
				else {
					ptsGive[n] = 0;
				}
				board0.board[n/9][n%9] = 0;
			}
		//find min ptsGive[], choose a random one if more than 1
			java.util.Random RANG = new java.util.Random();
			int k = RANG.nextInt(80);
			int m = ptsGive[k];
			this.theMove = k;
			for(int n=1; n<81; n++) {
				if(ptsGive[(n+k)%81] < m) {
					m = ptsGive[(n+k)%81];
					this.theMove = (n+k)%81;
				}
			}
		}
		else if(Lps[2] >= 0) {  // multiple scoring moves
		// needs to flag this case for higher AIlevel
			this.scoringFlag = 6;
			this.theMove = Lps[2];
		}
		else {                  // only one scoring move
			this.scoringFlag = 1;
			this.theMove = Lps[1];
		}
	}
	
/* Rewrite of the original 1985 algorithm
 * part B: maximize forward points
 * use after a call to 1985a()
 */
	private int AI1985b() {
		int Hp=0;
		ArrayList<int[]> moveslists = new ArrayList<int[]>(36);
		//findpts();    // this should reset the board and Lps[]
		
		moveslists.add(new int[MOVESLISTSIZ]);   // initial null chain
		moveslists.get(0)[0] = 1;
		moveslists.get(0)[1] = -1;
		moveslists.get(0)[2] = -1;
		if(myDebugLevel.Msg > 1)
			Log.d("AI1985b","Moveslists: "+Integer.toString(moveslists.size()));
		int n, m, p, m0;
		for(n=0; n < moveslists.size(); ) {
			int[] LL = moveslists.get(n);
			m = 1;
			while(m < MOVESLISTSIZ && LL[m] >= 0) m++;
			m--;
			for(int i=1; i<LL[0]; i++) {
				if(LL[i] > 99) continue;         // skip marked inactive moves
				board0.board[LL[i]/9][LL[i]%9] = n+1000;
			}
			findpts();
			if(LL[0] == m+1) {                   // if all moves are applied			
				if(Lps[0] > 0) {                 // new scoring moves found
					if(myDebugLevel.Msg > 1) 
						Log.d("AI1985b","Moveslist: "+Integer.toString(n)+" appended");
					for(int i=1; Lps[i] >= 0 && m < MOVESLISTSIZ-2; i++) {
						m++;
						LL[m] = Lps[i];
						LL[m+1] = -1;
					}
				} else {                      
					// clean up get ready to process next chain
					for(int i=1; i<=m; i++) {
						if(LL[i] > 99) continue; // skip marked inactive moves
						board0.board[LL[i]/9][LL[i]%9] = 0;
					}
					n++;                         // move on to next chain
					continue;
				}
			}

			// resolve conflicting moves
			m0 = LL[0];
			for(int j=m0; j<m; j++) {
				if(LL[j] > 99) continue;
				int Lp = LL[j];
				p = board0.board[Lp/9][Lp%9];
				board0.board[Lp/9][Lp%9] = 200;
				for(int k=j+1; k<=m; k++) {
					if(LL[k] > 99) continue;
					int s0=board0.board[LL[k]/9][LL[k]%9] + 100;
					int s = board0.checkScores(LL[k]);
					if(s != s0) {
						this.scoringFlag = 8;    // flag conflicting moves
						// construct a new moves chain
						int [] L0 = LL.clone();
						// mark the moves as conflicting
						L0[k] += 100;
						LL[j] += 100;
						moveslists.add(L0);
						Log.d("AI1985b","Moveslists: "+Integer.toString(moveslists.size()));
					}
				}
				board0.board[Lp/9][Lp%9] = p;
			}
			LL[0] = m + 1;                       // points to first move to examine
		}

		// find highest scores
		Hp = 0;
		for(n=0; n < moveslists.size(); n++) {
			int sc = 0;
			int[] LL = moveslists.get(n);
			LL[0] = 0;     // [0] reuse to be total points of chain
			for(int i=1; LL[i] >= 0 && i < MOVESLISTSIZ; i++) {
				if(LL[i] > 99) continue;
				sc = board0.checkScores(LL[i]);
				board0.board[LL[i]/9][LL[i]%9] = n+1000;
				if(sc > 0) LL[0] += sc;
				else break;
			}
			if(Hp < LL[0]) 	Hp = LL[0]; 
			for(int i=0; i<9; i++) for(int j=0; j<9; j++) {
				if(board0.board[i][j] == n+1000) board0.board[i][j] = 0;
			}
		}

		// find highest scores chain, and copy to theMoves[]
		for(n=0; n < moveslists.size(); n++) {
			if(moveslists.get(n)[0] == Hp) {
				if(myDebugLevel.Mode) 
					Log.d("AI1985b","Moveslist: "+Integer.toString(n)+" hi score:"+Integer.toString(Hp));
				for(int i=1, j=1; i < MOVESLISTSIZ; i++) {
					if(moveslists.get(n)[i] > 99) continue;
					theMoves[j] = moveslists.get(n)[i];
					j++;
					if(moveslists.get(n)[i] < 0) break;
				}
				this.theMove = theMoves[1];
				theMoves[0] = 2;
				break;
			}
		}

		// assert valid moves
		if(this.theMove < 0 || this.theMove > 80)    // should not happen
			this.theMove = -1;
		else if(theMoves[2] < 0)                     // should not happen
			theMoves[0] = 0;
		else  {                                      // assert proper moves chain termination
			for(int i=2; i < MOVESLISTSIZ; i++) {
				if(theMoves[i] >= 0 && theMoves[i] < 81) continue;
				else if(theMoves[i] < 0) break;
				else {
					theMoves[0] = 0;
					break;
				}
			}
		}
		return Hp;
	}

/* Random play, for testing purposes
 * return a random open location
 */
	private int AI0randomPlay() {
		java.util.Random RANG = new java.util.Random();
		int i, n = RANG.nextInt(82-board0.getStatus());
		for(i=0; i<81; i++) {
			if(board0.board[i/9][i%9] < 1) n--;
			if(n < 0) {
				break;
			}
		}
		return i;
	}
	
/* New algorithm: still needs tests
 * "just be thorough"
 */
	private void AI2015a() {
		BestMove0twin opp0, opp1;
		findpts();
		if(Lps[0] == 0) {                          // find opponent lowest scores
			this.theMove = Lps[1];                 // fail-safe
			int oppscore[] = new int[81];
			int k0 = (int) (System.currentTimeMillis() % 64);
			int Hp = 999;
			for(int j, i=0; i<81; i++) {
				j = (i + k0) % 81;
				if(board0.board[j/9][j%9] > 0) continue;
				board0.board[j/9][j%9] = 300;      // mark a trial move
				opp0 = new BestMove0twin(board0.board.clone());
				opp0.go();
				if(myDebugLevel.Msg > 0) 
					Log.d("AI2015-1","Counterparty launched:"+Integer.toString(j));
				oppscore[j] = opp0.Lps[0];
				board0.board[j/9][j%9] = 0;        // reset the board
				if(Hp > oppscore[j]) {             // find lowest lost
					Hp = oppscore[j];
					this.theMove = j;
				}
//				if(oppscore[j] == 0) {             // 1st found zero lost
//					this.theMove = j;
//					break;
//				}
			}
			if(myDebugLevel.Mode) 
				Log.d("AI2015-1","Move: "+Integer.toString(theMove)+" lowest lost:"+Integer.toString(Hp));
		}
		else if(Lps[0] > 0 ) {                     // at least 1 scoring move
			// find all move chains
			opp0 = new BestMove0twin(board0.board.clone());
			opp0.go();
			int n = opp0.moveslists.size();
			if(myDebugLevel.Mode) 
				Log.d("AI2015-2","Moveslist="+Integer.toString(n));
			if(n < 1) {                            // something is wrong, abort
				return;
			}
			for(int k=0; k<n; k++) {               // examine every chain & safe exit
				int oppscore[] = new int[81];
				int[] LL = opp0.moveslists.get(k);
				if(myDebugLevel.Msg > 1) 
					Log.d("AI2015-2"," Examine moveslist:"+Integer.toString(k));
				// apply move chain
				for(int i=1; LL[i]>=0 && i<MOVESLISTSIZ; i++) {
					if(LL[i] > 99) continue;       // skip marked inactive moves
					board0.board[LL[i]/9][LL[i]%9] = k+301;
				}
				int Hp = 999, xm = -1;
				for(int i=0; i<81; i++) {          // find min cost as exit move
					if(board0.board[i/9][i%9] > 0) continue;
					board0.board[i/9][i%9] = 300;
					opp1 = new BestMove0twin(board0.board.clone());
					opp1.go();
					oppscore[i] = opp1.Lps[0];
					board0.board[i/9][i%9] = 0;
					if(Hp > oppscore[i]) {
						Hp = oppscore[i];
						xm = i;
					}
				}
				if(Hp < 999) LL[0] -= Hp;          // net score after opponent next moves
				
				for(int i=1; ; i++) {              // append to move chain and reset board
					if(i > MOVESLISTSIZ-2)  break;
					else if(LL[i] < 0) {
						LL[i] = xm;
						LL[i+1] = -1;
						break;
					}
					if(LL[i] > 99) continue;       // skip marked inactive moves
					board0.board[LL[i]/9][LL[i]%9] = 0;
				}
				if(myDebugLevel.Mode) 
					Log.d("AI2015-2","Moveslist:"+Integer.toString(k)+" score:"+Integer.toString(LL[0]));
			}
			
			// find highest net scores from all chains
			int tmpHp = -999;
			int mx = -1;
			for(int k=0; k<n; k++) {
				int[] LL = opp0.moveslists.get(k);
				if(tmpHp < LL[0]) {
					tmpHp = LL[0];
					mx = k;
				}
			}
			if(myDebugLevel.Mode) 
				Log.d("AI2015-2","Moveslist: "+Integer.toString(mx)+" hi score:"+Integer.toString(tmpHp));
			if(mx < 0) mx = 0;                     // assertion against serious problems
			
			// copy move chain to theMoves
			for(int i=1, j=1; i < MOVESLISTSIZ; i++) {
				if(opp0.moveslists.get(mx)[i] > 99) continue;
				if(myDebugLevel.Msg > 1) 
					Log.d("AI2015-2"," Copy moveslist: "+Integer.toString(i)+"->"+Integer.toString(opp0.moveslists.get(mx)[i]));
				theMoves[j] = opp0.moveslists.get(mx)[i];
				j++;
				if(opp0.moveslists.get(mx)[i] < 0) break;
			}
			this.theMove = theMoves[1];
			if(theMoves[2] >= 0) theMoves[0] = 2;
			else theMoves[0] = 0;
			
		}
		else {                                     // something is really wrong
			this.theMove = AI0randomPlay();        // fail-safe
			theMoves[0] = 0;
			if(myDebugLevel.Mode) 
				Log.d("AI2015","exception in Lps: "+Integer.toString(Lps[0]));
		}
		
	}
	
}
