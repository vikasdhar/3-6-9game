package org.xluz.droidacts.sanLiuJiu.test;

import junit.framework.*;

public class AI1985tests extends TestCase {

	static final int AILEVELTOTEST = 3;

	public void testGo() {
		fail("Not yet implemented"); // TODO
	}
	public void testBoard1() {
		int[] moves = { 0, 55, 64, -1};
		org.xluz.droidacts.sanLiuJiu.BestMove0 AImov;
		AImov = new org.xluz.droidacts.sanLiuJiu.BestMove0(moves);
		Assert.assertEquals("Initial move"+Integer.toString(AImov.getTheMove()), -1, AImov.getTheMove());
//		System.out.println("Initial move:"+Integer.toString(AImov.getTheMove()));
		AImov.setAIlevel(AILEVELTOTEST);
		AImov.go();
		Assert.assertTrue("Move found:"+Integer.toString(AImov.getTheMove()), 
				AImov.getTheMove()==46 || AImov.getTheMove()==73);
		System.out.println("Move found:"+Integer.toString(AImov.getTheMove()));
	}

	public void testBoard2() {
		int[] moves = { 0, 55, 64, 74, 75, -1};
		org.xluz.droidacts.sanLiuJiu.BestMove0 AImov;
		AImov = new org.xluz.droidacts.sanLiuJiu.BestMove0(moves);
		Assert.assertEquals("Initial move"+Integer.toString(AImov.getTheMove()), -1, AImov.getTheMove());
		AImov.setAIlevel(AILEVELTOTEST);
		AImov.go();
		Assert.assertTrue("Move found:"+Integer.toString(AImov.getTheMove()), 
				AImov.getTheMove() >= 0);
		System.out.println("Move found:"+Integer.toString(AImov.getTheMove()));
	}

	public void testBoard3() {
		int[] moves = { 0, 55, 64, 74, 75, 67, 59, 51, -1};
		org.xluz.droidacts.sanLiuJiu.BestMove0 AImov;
		AImov = new org.xluz.droidacts.sanLiuJiu.BestMove0(moves);
		Assert.assertEquals("Initial move"+Integer.toString(AImov.getTheMove()), -1, AImov.getTheMove());
		AImov.setAIlevel(AILEVELTOTEST);
		AImov.go();
		Assert.assertTrue("Move found:"+Integer.toString(AImov.getTheMove()), 
				AImov.getTheMove() == 76);
		System.out.println("Move found:"+Integer.toString(AImov.getTheMove()));
		AImov.go();
		Assert.assertTrue("Move found:"+Integer.toString(AImov.getTheMove()), 
				AImov.getTheMove() == 46);
		System.out.println("Move found:"+Integer.toString(AImov.getTheMove()));
		AImov.go();
		Assert.assertTrue("Move found:"+Integer.toString(AImov.getTheMove()), 
				AImov.getTheMove() == 58);
		System.out.println("Move found:"+Integer.toString(AImov.getTheMove()));
		AImov.go();
		Assert.assertTrue("Move found:"+Integer.toString(AImov.getTheMove()), 
				AImov.getTheMove() == 60);
		System.out.println("Move found:"+Integer.toString(AImov.getTheMove()));
		AImov.go();
		System.out.println("Move found:"+Integer.toString(AImov.getTheMove()));
		AImov.go();
		System.out.println("Move found:"+Integer.toString(AImov.getTheMove()));
		AImov.go();
		System.out.println("Move found:"+Integer.toString(AImov.getTheMove()));
	}

}
