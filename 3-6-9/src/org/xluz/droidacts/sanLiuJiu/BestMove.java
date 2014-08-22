package org.xluz.droidacts.sanLiuJiu;

/* 
  Find the best move available
 
Copyright (c) 2014 Cecil Cheung
This software is released under the GNU General Public License version 3.
See, for example, "http://www.gnu.org/licenses/gpl.html".
 */

public class BestMove {
	private GamePlay board0;
//	private int[] movesGiven;
	private int theMove, AIlevel=1;

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
		}
		else if(AI==2) {
		// find potential scores
			AIlevel1();
		// think forward 1 more step
			AIlevel2();
		}
		return theMove;
	}
	
	private void AIlevel2() {// not finished!!
		if(this.theMove < 0) return;  // something not right
		
		int s, mnsc;
		int mxsc = board0.board[this.theMove/9][this.theMove%9];
		for(int i=0; i<81; i++) {
			if(board0.board[i/9][i%9] != mxsc) continue;
			board0.board[i/9][i%9] = 1;
			mnsc = 100;
		// find the fewest counter-scores
			for(int j=0; j<81; j++) {
				if(i==j) continue;
				if(board0.board[j/9][j%9]<=0) {
					s = board0.checkScores(j);
					if(mnsc >= s) mnsc = s;
				}
			}
			//won't work, need a second board
			board0.board[i/9][i%9] = mxsc - mnsc;
		}
		mnsc = -500;
		// find the highest combined scores
		for(int i=0; i<81; i++) {
			if(board0.board[i/9][i%9] < -1)
				if(board0.board[i/9][i%9] > mnsc) {
					mnsc = board0.board[i/9][i%9];
					this.theMove = i;
				}
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
		try {
			Thread.sleep(1000);  // insert some delay for testing
		} catch (InterruptedException e) {}
	}
}
