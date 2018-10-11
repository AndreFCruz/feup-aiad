/*$$
 * Copyright (c) 2004, Repast Organization for Architecture and Design (ROAD)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with 
 * or without modification, are permitted provided that the following 
 * conditions are met:
 *
 *	 Redistributions of source code must retain the above copyright notice,
 *	 this list of conditions and the following disclaimer.
 *
 *	 Redistributions in binary form must reproduce the above copyright notice,
 *	 this list of conditions and the following disclaimer in the documentation
 *	 and/or other materials provided with the distribution.
 *
 * Neither the name of the ROAD nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE TRUSTEES OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *$$*/
package uchicago.src.repastdemos.asynchAgents;

import java.util.Iterator;

import uchicago.src.sim.util.Random;

/**
 * This is the agent that moves agents according to how many tasks they have
 * relative to the rest of the employees.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/08/12 20:04:57 $
 */
public class Consultant extends Boss {
	
	public Consultant(AsynchAgentsModel officeSpace, Office office) {
		super(officeSpace, office);
	}
	
	
	public void gradeEmployees() {
		// this will hold the largest number of tasks any agent has or
		// 1 if there are no tasks assigned (this avoid division by zero
		// problems)
		int mostTasks = Math.max(getLongestTaskListLength(), 1);
		
		// iterate through the employees moving them around the office based
		// on how many pending tasks they have
		for (Iterator iter = office.getEmployees().iterator(); iter.hasNext();) {
			Employee emp = (Employee) iter.next();
			
			// distance from optimal (0 tasks is optimal)
			int dist = emp.getTaskList().size();
			
			// the normalized distance (-.1 to make sure they stay on screen)
			double hypotenuse = (dist - .1) / mostTasks;
			
			// this code limits the distance an employee can move
			double[] tmp = screenCoordsToNormalizedCartesianCoords(emp.getX(), emp.getY());
			double oldHypotenuse = Math.sqrt(tmp[0] * tmp[0] + tmp[1] * tmp[1]);
			if (Math.abs(oldHypotenuse - hypotenuse) > .1) {
				if (oldHypotenuse > hypotenuse)
					hypotenuse = oldHypotenuse - .1;
				else
					hypotenuse = oldHypotenuse + .1;
			}
				 
			
			// the angle the agent is from the center of the office
			double theta = getTheta(emp);
			
//			System.out.println("theta: " + theta);
			
			// the agent is moved along a line with angle theta (with the 
			// +x axis) to a distance of frac.  This adjusts the angle based
			// on what quadrant the agent is in
			theta = getAngle(emp.getX() < office.getWidth() / 2, theta);
			
			// add some random deviation
			theta += Random.uniform.nextDoubleFromTo(-Math.PI / 8, Math.PI / 8);
			
			// the new agent location
			double newX = hypotenuse * Math.cos(theta);
			double newY = hypotenuse * Math.sin(theta);
			
			double[] screenCoords = normalizedCartesianCoordsToScreenCoords(newX, newY);
//			System.out.println(screenCoords[0]);
//			System.out.println(screenCoords[1]);
			emp.setX(screenCoords[0]);
			emp.setY(screenCoords[1]);
		}
	}
	
	
	
	
	public double[] screenCoordsToNormalizedCartesianCoords(double x, double y) {
		// divide width/height by two
		double width	= office.getWidth() / 2.0;
		double height	= office.getHeight() / 2.0;
		
		double cartesianX = x - width;
		double cartesianY = (y > height ? height - y : -(y - height));
//		System.out.println("cx: " + cartesianX + ", cy:" + cartesianY);
		double normalizedCX = cartesianX / width;
		double normalizedCY = cartesianY / height;
		
		return new double[] { normalizedCX, normalizedCY };
	}
	
	public double[] normalizedCartesianCoordsToScreenCoords(double x, double y) {
		double width	= office.getWidth() / 2.0;
		double height	= office.getHeight() / 2.0;
		
		double cartesianX = x * width;
		double cartesianY = y * height;

		double screenX = cartesianX + width;
		double screenY = (cartesianY > height ? -height - cartesianY : (-cartesianY + height));
				
		return new double[] { screenX, screenY };
	}
	
	private double getTheta(Employee emp) {
		double coords[] = 
			this.screenCoordsToNormalizedCartesianCoords(emp.getX(), emp.getY());
//		System.out.println("x: " + coords[0] + ", y:" + coords[1]);
		// rise over run
		if (coords[0] == 0)
			coords[0] = .00001;
		
		return Math.atan(coords[1] / coords[0]);
	}
	
	private double getAngle(boolean inLeftQuads, double angle) {
		return (inLeftQuads ? Math.PI + angle : angle);
	}
	
	private int getLongestTaskListLength() {
		int longestLength = 0;
		
		for (Iterator iter = office.getEmployees().iterator(); iter.hasNext();) {
			Employee emp = (Employee) iter.next();
			
			if (longestLength < emp.getTaskList().size())
				longestLength = emp.getTaskList().size();
		}
		
		return longestLength;
	}
}