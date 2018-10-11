/*
 * Created on Oct 19, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package uchicago.src;

import junit.framework.Test;
import junit.framework.TestSuite;
import uchicago.src.collection.CollectionTest;
import uchicago.src.collection.NewMatrixTest;
import uchicago.src.collection.RangeMapTest;
import uchicago.src.sim.engine.GroupTest;
import uchicago.src.sim.engine.QueueTest;
import uchicago.src.sim.engine.ScheduleListTest;
import uchicago.src.sim.engine.ScheduleTest;
import uchicago.src.sim.math.CEquationTest;
import uchicago.src.sim.network.DefaultNodeTest;
import uchicago.src.sim.network.NetworkTest;
import uchicago.src.sim.network.ShortestPathTest;
import uchicago.src.sim.parameter.DefaultParameterTest;
import uchicago.src.sim.parameter.RPLParameterTest;
import uchicago.src.sim.space.HexTest;
import uchicago.src.sim.space.MultiGridTest;
import uchicago.src.sim.space.RectTest;

/**
 * @author Howe
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AllTests {

	public static void main(String[] args) {
	}

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for uchicago.src");
		//$JUnit-BEGIN$
		suite.addTest(CollectionTest.suite());
		suite.addTest(RangeMapTest.suite());
		suite.addTest(GroupTest.suite());
		suite.addTest(QueueTest.suite());
		suite.addTest(ScheduleListTest.suite());
		suite.addTest(ScheduleTest.suite());
		suite.addTest(DefaultNodeTest.suite());
		suite.addTest(NetworkTest.suite());
		suite.addTest(ShortestPathTest.suite());
		suite.addTest(DefaultParameterTest.suite());
		suite.addTest(RPLParameterTest.suite());
		suite.addTest(HexTest.suite());
		suite.addTest(MultiGridTest.suite());
		suite.addTest(RectTest.suite());
		suite.addTest(NewMatrixTest.suite());
		suite.addTest(CEquationTest.suite());
		//$JUnit-END$
		return suite;
	}
}
