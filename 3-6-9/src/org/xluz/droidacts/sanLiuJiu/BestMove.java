package org.xluz.droidacts.sanLiuJiu;

/* 
  Find the best move available
 
Copyright (c) 2014 Cecil Cheung
This software is released under the GNU General Public License version 3.
See, for example, "http://www.gnu.org/licenses/gpl.html".
 */

public class BestMove {
	private GamePlay board0;
	private int theMove, AIlevel=2;

	public BestMove(int[] moves) {
		board0 = new GamePlay(moves);
		this.theMove = -1;
		if(board0.getStatus()<82) go(AIlevel);
	}

	public int getAIlevel() {
		return AIlevel;
	}

	public void setAIlevel(int L) {
		AIlevel = L;
	}

	public int getTheMove() {
		return theMove;
	}

	public int go(int AI) {
		this.theMove = -1;
		if(AI==0) {
		// random play, for testing
			java.util.Random RANG = new java.util.Random();
			int n = RANG.nextInt(82-board0.getStatus());
			for(int i=0; i<81; i++) {
				if(board0.board[i/9][i%9]==0) n--;
				if(n<0) {
					theMove = i;
					break;
				}
			}
			try {
				Thread.sleep(1500);  // insert some delay for testing
			} catch (InterruptedException e) {}
		}
		else if(AI==1) {
		// max next move score
			AIlevel1();
			try {
				Thread.sleep(1000);  // insert some delay for testing
			} catch (InterruptedException e) {}
		}
		else if(AI==2) {  // more like level 1.5
		// find potential scores
			AIlevel1();
		// think forward 1 more step
			AIlevel2();
			try {
				Thread.sleep(900);  // insert some delay for testing
			} catch (InterruptedException e) {}
		}
		else if(AI==3) {
		// find potential scores
			AIlevel1();
		// minimize score-giveaway
			AIlevel3();
		}
		return theMove;
	}
	
	private void AIlevel3() {
		if(this.theMove < 0 || this.theMove > 80) return;  // something not right
		
//		int mxsc = board0.board[this.theMove/9][this.theMove%9];
		int sc, s, mnsc, mngiveaway=100;
//		int[][] board1 = new int[81][81];
//		for(int i=0; i<81; i++) 
//			board1[i/9][i%9] = board0.board[i/9][i%9];

		for(int i=0; i<81; i++) {
			if(board0.board[i/9][i%9] > 0) continue;
			sc = board0.board[i/9][i%9];
			board0.board[i/9][i%9] = 1;
			mnsc = 0;
		// find the lowest counter-scores
			for(int j=0; j<81; j++) {
				if(board0.board[j/9][j%9] <= 0) { // empty squares
					s = board0.checkScores(j);
					if(mnsc < s) mnsc = s;        // find max potential score
				}
			}
			if(mngiveaway > mnsc) {
				mngiveaway = mnsc;
			}
			board0.board[i/9][i%9] = sc - mngiveaway;
		}
		mnsc = -200;
		for(int i=0; i<81; i++) {
			sc = board0.board[i/9][i%9];
			if(sc >= 0) continue;
			if(mnsc < sc) {
				mnsc = sc;
				this.theMove = i;
			}
		}
		
	}
	
	private void AIlevel2() {
		if(this.theMove < 0 || this.theMove > 80) return;  // something not right
		
		int mxsc = board0.board[this.theMove/9][this.theMove%9];
		int s, mnsc, mngiveaway=100;
		int n = this.theMove;
		
		for(int i=0; i<81; i++) {
		// only examine highest score moves
			if(board0.board[n/9][n%9] != mxsc) continue;
			board0.board[n/9][n%9] = 1;
			mnsc = 0;
		// find the lowest counter-scores
			for(int j=0; j<81; j++) {
				if(board0.board[j/9][j%9] <= 0) { // empty squares
					s = board0.checkScores(j);
					if(mnsc < s) mnsc = s;        // find max potential score
				}
			}
			if(mngiveaway > mnsc) {
				mngiveaway = mnsc;
				this.theMove = n;
			}
			board0.board[n/9][n%9] = mxsc;
			n++;
			if(n>80) n = 0;
		}
	}

	private void AIlevel1() {
		java.util.Random RANG = new java.util.Random();
		int n = RANG.nextInt(81);  // search starts at random board location
		int mxsc=0, sc;
	// find the last highest score move
		for(int i=0; i<81; i++) {
			if(board0.board[n/9][n%9]==0) {
				sc = board0.checkScores(n);
				board0.board[n/9][n%9] = sc - 100;
				if(mxsc <= sc) {
					mxsc = sc;
					this.theMove = n;
				}
			}
			n++;
			if(n>80) n = 0;
		}		
	}
	
}
