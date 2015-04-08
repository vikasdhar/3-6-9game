package org.xluz.droidacts.sanLiuJiu.test;

import junit.framework.*;

public class AI1985tests extends TestCase {

	static final int AILEVELTOTEST = 3;

	public void testBoard1() {
		int[] moves = { 0, 55, 64, -1};
		org.xluz.droidacts.sanLiuJiu.BestMove0 AImov;
		AImov = new org.xluz.droidacts.sanLiuJiu.BestMove0(moves);
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
		org.xluz.droidacts.sanLiuJiu.BestMove0 AImov;
		AImov = new org.xluz.droidacts.sanLiuJiu.BestMove0(moves);
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
		org.xluz.droidacts.sanLiuJiu.BestMove0 AImov;
		AImov = new org.xluz.droidacts.sanLiuJiu.BestMove0(moves);
		Assert.assertEquals("Initial move"+Integer.toString(AImov.getTheMove()), -1, AImov.getTheMove());
		AImov.setAIlevel(AILEVELTOTEST);
		AImov.go();
		Assert.assertTrue("Invalid move:"+Integer.toString(AImov.getTheMove()), 
				AImov.getTheMove() >= 0);
		System.out.println("Move a:"+Integer.toString(AImov.getTheMove()));
	}

	public void testBoard4() {
		int[] moves = { 0, 55, 64, 74, 75, 67, 59, 51, -1};
		org.xluz.droidacts.sanLiuJiu.BestMove0 AImov;
		AImov = new org.xluz.droidacts.sanLiuJiu.BestMove0(moves);
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
	}

}
