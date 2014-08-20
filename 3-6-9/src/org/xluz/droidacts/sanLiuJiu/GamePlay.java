package org.xluz.droidacts.sanLiuJiu;

/*
  Game data object 

Copyright (c) 2014 Cecil Cheung
This software is released under the GNU General Public License version 3.
See, for example, "http://www.gnu.org/licenses/gpl.html".
*/

public class GamePlay {
	private int cstatus;
	int[] movesSeq; // cell=row*9+col
	int[][] board;  // 0: empty, -1: selected, 1: occupied
	int[] movesScore;
	int scores1, scores2;
	int scoringMoveCs, scoringMoveCe, scoringMoveRs, scoringMoveRe;
	int scoringMoveD0s, scoringMoveD0e, scoringMoveD1s, scoringMoveD1e;
	
	public GamePlay(int[] movesSeq) {
		scoringMoveCs = scoringMoveCe = -1; 
		scoringMoveRs = scoringMoveRe = -1;
		scoringMoveD0s = scoringMoveD0e = -1; 
		scoringMoveD1s = scoringMoveD1e = -1;
		scores1 = scores2 = 0;
		movesScore = new int[82];
		this.movesSeq = new int[82];
		board = new int[9][9];
		for(int i=0; i < 81; i++) {
			board[i/9][i%9] = 0;
		}
		try {
			this.movesSeq[0] = movesSeq[0];
			for(cstatus=1; cstatus<82; cstatus++) {
				this.movesSeq[cstatus] = movesSeq[cstatus];
				if(movesSeq[cstatus]==-1) {
					break;
				}
				else {
					board[movesSeq[cstatus]/9][movesSeq[cstatus]%9] = 1;
					movesScore[cstatus] = checkScores(movesSeq[cstatus]);
					if(movesScore[cstatus]<0) movesScore[cstatus]=0;  // should not happen
					else {
						if(cstatus%2==0) scores2 += movesScore[cstatus];
						else scores1 += movesScore[cstatus];

					}
				}
			}
		} catch(Exception e) {  // in case of invalid movesSeq[]
			scores1 = scores2 = cstatus = 0;
			this.movesSeq[0] = 0;
		}
	}
	
	public GamePlay() {
		scores1 = scores2 = cstatus = 0;
		movesScore = new int[82];
		movesSeq = new int[82];
		board = new int[9][9];
		for(int i=0; i < 81; i++) {
			movesSeq[i+1] = -1;
			board[i/9][i%9] = 0;
		}
		scoringMoveCs = scoringMoveCe = -1; 
		scoringMoveRs = scoringMoveRe = -1;
		scoringMoveD0s = scoringMoveD0e = -1; 
		scoringMoveD1s = scoringMoveD1e = -1;
	}

	public int getStatus() {
		return cstatus;
	}

	public int recordMove(int m) {
		if(cstatus > 81) return -1; // bounds checks
		movesSeq[cstatus] = m;
		int sc=checkScores(m);
		if(sc >= 0 && cstatus > 0) {
			if(cstatus%2==0) scores2 += sc;
			else scores1 += sc;
			movesScore[cstatus] = sc;
		}
		cstatus++;
		return sc;
	}
	
	int checkScores(int move) {
		int r = move/9;
		int c = move%9;
		int pts = 0;
		int hc=1, vc=1, dc=1;
		if(move < 0 || move > 81) return -1;
		// chk left direction for contiguously occupied cells
		this.scoringMoveCs = 0;
		if(c > 0) {
			for(int i=c-1; i>=0; i--) {
				if(board[r][i] < 1) {
					this.scoringMoveCs = i+1;
					break;
				}
				hc++;
			}
		}
		// chk right direction
		this.scoringMoveCe = 8;
		if(c < 8) {
			for(int i=c+1; i<9; i++) {
				if(board[r][i] < 1) {
					this.scoringMoveCe = i-1;
					break;
				}
				hc++;
			}
		}
		// column scores
		if(hc%3 == 0) {
			pts += hc;
		} else {
			this.scoringMoveCs = this.scoringMoveCe = -1;
		}
		// chk up direction
		this.scoringMoveRs = 0;
		if(r > 0) {
			for(int j=r-1; j>=0; j--) {
				if(board[j][c] < 1) {
					this.scoringMoveRs = j+1;
					break;
				}
				vc++;
			}
		}
		// chk down direction
		this.scoringMoveRe = 8;
		if(r < 8) {
			for(int j=r+1; j<9; j++) {
				if(board[j][c] < 1) {
					this.scoringMoveRe = j-1;
					break;
				}
				vc++;
			}
		}
		// row scores
		if(vc%3 == 0) {
			pts += vc;
		} else {
			this.scoringMoveRs = this.scoringMoveRe = -1;
		}
		// chk +ive diagonal directions
		if(c>r) {
			scoringMoveD0s = c - r;
			scoringMoveD0e = 8;
		} else {
			scoringMoveD0s = 0;
			scoringMoveD0e = 8 - r + c;			
		}
		if(c > 0 && r > 0) {
			for(int i=c-1, j=r-1; i>=0 && j>=0; i--, j--) {
				if(board[j][i] < 1) {
					this.scoringMoveD0s = i+1;
					break;
				}
				dc++;
			}
		}
		if(c < 8 && r < 8) {
			for(int i=c+1, j=r+1; i<9 && j<9; i++, j++) {
				if(board[j][i] < 1) {
					this.scoringMoveD0e = i-1;
					break;
				}
				dc++;
			}
		}
		if(dc%3 == 0) {
			pts += dc;
		} else {
			this.scoringMoveD0s = this.scoringMoveD0e = -1;
		}
		
		// chk -ive diagonal directions
		dc = 1;
		if(c<8-r) {
			scoringMoveD1s = c + r;
			scoringMoveD1e = 0;
		} else {
			scoringMoveD1s = 8;
			scoringMoveD1e = c + r - 8;			
		}
		if(c < 8 && r > 0) {
			for(int i=c+1, j=r-1; i<9 && j>=0; i++, j--) {
				if(board[j][i] < 1) {
					this.scoringMoveD1s = i-1;
					break;
				}
				dc++;
			}
		}
		//this.scoringMoveD1e = c;
		if(c > 0 && r < 8) {
			for(int i=c-1, j=r+1; i>=0 && j<9; i--, j++) {
				if(board[j][i] < 1) {
					this.scoringMoveD1e = i+1;
					break;
				}
				dc++;
			}
		}
		if(dc%3 == 0) {
			pts += dc;
		} else {
			this.scoringMoveD1s = this.scoringMoveD1e = -1;
		}

		return pts;
	}

}
