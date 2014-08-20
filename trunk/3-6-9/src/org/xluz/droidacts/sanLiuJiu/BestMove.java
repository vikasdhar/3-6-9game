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
	private int theMove, AIlevel=0;

	public BestMove(int[] moves) {
		board0 = new GamePlay(moves);
		this.theMove = -1;
		if(board0.getStatus()<82) go(AIlevel);
	}

	public int go(int AI) {
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
		// original AI routines
			AIlevel1();
		}
		else if(AI==2) {
		// think forwards 2 steps
		}
		return theMove;
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

	private void AIlevel1() {
		
	}
}
