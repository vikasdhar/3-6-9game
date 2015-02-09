package org.xluz.droidacts.sanLiuJiu;

import android.util.Log;

/* 
Find the best move available under old rules

Copyright (c) 2015 Cecil Cheung
This software is released under the GNU General Public License version 3.
See, for example, "http://www.gnu.org/licenses/gpl.html".
*/

class BestMove0 extends BestMove {
	private GamePlay0 board0;
	private int theMove;
	private int HiP[];
	//private int theMoves[];

	public BestMove0(int[] moves) {
		//super();
		board0 = new GamePlay0(moves);
		this.theMove = -1;
		HiP = new int[83];    // [0] stores hi pt, then the locations
	}

	@Override
	public int getTheMove() {
		return this.theMove;
	}

	@Override
	public int go() {
		this.theMove = -1;
		if(super.getAIlevel() <= 0 || board0.getStatus() <= 6) {
		// random play, for testing
			java.util.Random RANG = new java.util.Random();
			int n = RANG.nextInt(82-board0.getStatus());
			for(int i=0; i<81; i++) {
				if(board0.board[i/9][i%9] == 0) n--;
				if(n<0) {
					theMove = i;
					break;
				}
			}
			try {
				Thread.sleep(900);  // insert some delay for testing
			} catch (InterruptedException e) {}
		}
		else {
			AI1985a();
			try {
				Thread.sleep(1000);  // insert some delay for testing
			} catch (InterruptedException e) {}
			
		}
		return theMove;
	}

/* Rewrite of the original 1985 algorithm
 * 
 */
	private void AI1985a() {
		int ptsGive[] = new int[81];
		findpts();
		Log.d("AI1985", "Rnd1: "+Integer.toString(HiP[0])+
				", "+Integer.toString(HiP[1])+
				", "+Integer.toString(HiP[2])+
				", "+Integer.toString(HiP[3]));
		if(HiP[0] < 0) {  // board is full??

		}
		else if(HiP[2] < 0) {  // only one hi pt move
			this.theMove = HiP[1];
		}
		else if(HiP[0] == 0) { // multiple 0 pt moves
			for(int n=0; n<81; n++) {
				ptsGive[n] = 999;
				if(board0.board[n/9][n%9] > 0) continue;
				board0.board[n/9][n%9] = 100;
				findpts();
				if(HiP[0] > 0) {
					//remove conflicting & add all pts
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
		else if(HiP[0] > 0) {  // multiple positive pt moves
			this.theMove = HiP[1];
			//max the potential scores...
			
		}
	}
	
	private void findpts() {
		int n=1, sc;
		HiP[0] = -1;
		for(int i=0; i<81; i++) {
			if(board0.board[i/9][i%9] < 1) {  // location not occupied
				sc = board0.checkScores(i);
				board0.board[i/9][i%9] = sc - 100;
				if(HiP[0] == sc) {      // multiple highest points
					HiP[0] = sc;
					HiP[n] = i;
					n++;
					HiP[n] = -1;        // mark end of array
				}
				else if(HiP[0] < sc) {  // find highest points
					HiP[0] = sc;
					HiP[1] = i;
					HiP[2] = -1;        // mark end of array
					n = 2;
				}
			}
		}
		
	}
}
