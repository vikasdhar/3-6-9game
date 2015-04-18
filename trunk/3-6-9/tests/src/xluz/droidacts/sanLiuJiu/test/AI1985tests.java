package xluz.droidacts.sanLiuJiu.test;

/* Test cases for computer play with the old rules
 * 
 * Copyright (c) 2015 Cecil Cheung
 * This software is released under the GNU General Public License version 3.
 * See, for example, "http://www.gnu.org/licenses/gpl.html".
 */

import junit.framework.*;

public class AI1985tests extends TestCase {

	static final int AILEVELTOTEST = 3;

	public void testBoard1() {
		int[] moves = { 0, 55, 64, -1};
		xluz.droidacts.sanLiuJiu.BestMove0 AImov;
		AImov = new xluz.droidacts.sanLiuJiu.BestMove0(moves);
		Assert.assertEquals("Initial move "+Integer.toString(AImov.getTheMove()), -1, AImov.getTheMove());
		AImov.setAIlevel(AILEVELTOTEST);
		Assert.assertEquals("AI level not set", 3, AImov.getAIlevel());
		AImov.go();
		Assert.assertTrue("1st move:"+Integer.toString(AImov.getTheMove()), 
				AImov.getTheMove()==46 || AImov.getTheMove()==73);
//		System.out.println("Move found:"+Integer.toString(AImov.getTheMove()));
	}

	public void testBoard2() {
		int[] moves = { 0, 11, 12, 38, 39, -1};
		xluz.droidacts.sanLiuJiu.BestMove0 AImov;
		AImov = new xluz.droidacts.sanLiuJiu.BestMove0(moves);
		Assert.assertEquals("Initial move"+Integer.toString(AImov.getTheMove()), -1, AImov.getTheMove());
		AImov.setAIlevel(AILEVELTOTEST);
		AImov.go();
		Assert.assertTrue("Invalid move:"+Integer.toString(AImov.getTheMove()), 
				AImov.getTheMove() >= 0);
		System.out.println("Move a:"+Integer.toString(AImov.getTheMove()));
		AImov.go();
		Assert.assertTrue("Invalid move:"+Integer.toString(AImov.getTheMove()), 
				AImov.getTheMove() >= 0);
		System.out.println("Move b:"+Integer.toString(AImov.getTheMove()));
	}

	public void testBoard3() {
		int[] moves = { 0, 55, 64, 74, 75, -1};
		xluz.droidacts.sanLiuJiu.BestMove0 AImov;
		AImov = new xluz.droidacts.sanLiuJiu.BestMove0(moves);
		Assert.assertEquals("Initial move"+Integer.toString(AImov.getTheMove()), -1, AImov.getTheMove());
		AImov.setAIlevel(AILEVELTOTEST);
		AImov.go();
		Assert.assertTrue("Invalid move:"+Integer.toString(AImov.getTheMove()), 
				AImov.getTheMove() >= 0);
		System.out.println("Move a:"+Integer.toString(AImov.getTheMove()));
	}

	public void testBoard4() {
		int[] moves = { 0, 55, 64, 74, 75, 67, 59, 51, -1};
		xluz.droidacts.sanLiuJiu.BestMove0 AImov;
		AImov = new xluz.droidacts.sanLiuJiu.BestMove0(moves);
		Assert.assertEquals("Initial move"+Integer.toString(AImov.getTheMove()), -1, AImov.getTheMove());
		AImov.setAIlevel(AILEVELTOTEST);
		AImov.go();
		Assert.assertTrue("Incorrect move:"+Integer.toString(AImov.getTheMove()), 
				AImov.getTheMove() == 76);
		System.out.println("Move a:"+Integer.toString(AImov.getTheMove()));
		AImov.go();
		Assert.assertTrue("Incorrect move:"+Integer.toString(AImov.getTheMove()), 
				AImov.getTheMove() == 58);
		System.out.println("Move b:"+Integer.toString(AImov.getTheMove()));
		AImov.go();
		Assert.assertTrue("Incorrect move:"+Integer.toString(AImov.getTheMove()), 
				AImov.getTheMove() == 73);
		System.out.println("Move c:"+Integer.toString(AImov.getTheMove()));
		AImov.go();
		Assert.assertTrue("Incorrect move:"+Integer.toString(AImov.getTheMove()), 
				AImov.getTheMove() == 60);
		System.out.println("Move d:"+Integer.toString(AImov.getTheMove()));
		AImov.go();
		System.out.println("Move e:"+Integer.toString(AImov.getTheMove()));
		AImov.go();
		System.out.println("Move f:"+Integer.toString(AImov.getTheMove()));
		AImov.go();
		System.out.println("Move g:"+Integer.toString(AImov.getTheMove()));
		AImov.go();
		System.out.println("Move h:"+Integer.toString(AImov.getTheMove()));
	}

	public void testBoard9() {
		int[] moves = { 449389106, 1, 2, 3, 5, 6, 7, 8, 10, 12, 13, 14, 15, 16, 
				18, 19, 20, 21, 25, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 43,
				45, 46, 47, 48, 49, 50, 52, 54, 55,56, 57, 59, 60, 61, 
				63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 75, 77, 78, 79, -1, -1};
		xluz.droidacts.sanLiuJiu.BestMove0 AImov;
		AImov = new xluz.droidacts.sanLiuJiu.BestMove0(moves);
		Assert.assertEquals("Initial move "+Integer.toString(AImov.getTheMove()), -1, AImov.getTheMove());
		AImov.setAIlevel(AILEVELTOTEST);
		Assert.assertEquals("AI level not set", 3, AImov.getAIlevel());
		AImov.go();
		Assert.assertTrue("Invalid move:"+Integer.toString(AImov.getTheMove()), 
				AImov.getTheMove() >= 0);
		for(int k=0; k<21 && AImov.getTheMove() >=0; ++k, AImov.go()) {
			System.out.println("Move found:"+Integer.toString(AImov.getTheMove()));
		}
	}

}
