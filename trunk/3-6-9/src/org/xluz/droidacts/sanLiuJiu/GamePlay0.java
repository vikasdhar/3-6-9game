package org.xluz.droidacts.sanLiuJiu;
/*
Game data object (under old rules)

Copyright (c) 2015 Cecil Cheung
This software is released under the GNU General Public License version 3.
See, for example, "http://www.gnu.org/licenses/gpl.html".
*/

class GamePlay0 extends GamePlay {
	//private int cstatus;
	
	public GamePlay0() {
		super();
		huseturn = 1;
	}

	public GamePlay0(int[] seq) {
		//super();
		huseturn = 1;
		try {
			this.movesSeq[0] = seq[0];
			for(cstatus=1; cstatus<82; cstatus++) {
				this.movesSeq[cstatus] = seq[cstatus];
				if(seq[cstatus]==-1) {
					break;
				}
				else {
					board[movesSeq[cstatus]/9][movesSeq[cstatus]%9] = 1;
					movesScore[cstatus] = checkScores(movesSeq[cstatus]);
					if(movesScore[cstatus]<0) movesScore[cstatus] = 0;  // should not happen
					else if(movesScore[cstatus]==0) huseturn = (huseturn + 1)%2;
					else {
						if(huseturn==0) scores2 += movesScore[cstatus];
						else scores1 += movesScore[cstatus];
					}
				}
			}
			//if(this.cstatus>81) huseturn = (huseturn + 1)%2;
		} catch(Exception e) {  // in case of invalid movesSeq[]
			scores1 = scores2 = cstatus = 0;
			this.movesSeq[0] = 0;
		}	
	}

	@Override
	public int recordMove(int m) {
		if(cstatus > 81) return -1; // bounds checks
		movesSeq[cstatus] = m;
		int sc=checkScores(m);
		if(sc > 0 && cstatus > 0) {
			if(huseturn==0) scores2 += sc;
			else scores1 += sc;
			movesScore[cstatus] = sc;
		}
		if(sc == 0 && cstatus > 0) {
			huseturn = (huseturn + 1)%2;			
		}
		cstatus++;

		return sc;
	}

	@Override
	int checkScores(int move) {
		int r = move/9;
		int c = move%9;
		int pts = 0;
		int hc=1, vc=1;
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
		this.scoringMoveD0s = this.scoringMoveD0e = -1;
		
		// chk -ive diagonal directions
		this.scoringMoveD1s = this.scoringMoveD1e = -1;

		return pts;
	}

}
