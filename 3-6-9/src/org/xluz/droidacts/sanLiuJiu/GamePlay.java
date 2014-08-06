package org.xluz.droidacts.sanLiuJiu;

public class GamePlay {
	int[] movesSeq; // cell=row*9+col
	int[][] board;  // 0: empty, -1: selected, 1: occupied
	int scores1, scores2;
	private int cstatus;
	
	public GamePlay(int[] movesSeq, int[][] board, int scores1, int scores2) {
		super();
		this.movesSeq = movesSeq;
		this.board = board;
		this.scores1 = scores1;
		this.scores2 = scores2;
	}
	
	public GamePlay() {
		scores1 = scores2 = cstatus = 0;
		movesSeq = new int[82];
		board = new int[9][9];
		for(int i=0; i < 81; i++) board[i/9][i%9] = 0;
	}

	public int getStatus() {
		return cstatus;
	}

	public int recordMove(int m) {
		if(cstatus > 81) return -1; // bounds checks
		movesSeq[cstatus] = m;
		if(cstatus > 0) {
			if(cstatus%2==0) scores2 += checkScores(m);
			else scores1 += checkScores(m);
		}
		cstatus++;
		return checkScores(m);
	}
	
	int checkScores(int move) {
		return 0;
	}


}
