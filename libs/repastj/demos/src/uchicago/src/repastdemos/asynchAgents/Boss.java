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

import java.awt.Color;

import uchicago.src.sim.gui.RectNetworkItem;
import uchicago.src.sim.network.DefaultDrawableNode;
import uchicago.src.sim.util.Random;

/**
 * This class represents the agent who outputs a value based on either exponential or 
 * polynomial function for a given input. The employees agents try to guess the boss's
 * output. 
 * 
 * @see Employee
 * @see Consultant
 *  
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/08/12 20:04:57 $
 */
public class Boss extends DefaultDrawableNode {
	protected Office office;
	protected AsynchAgentsModel officeSpace;

	protected int tasksAssignedPerTick;
	
	
	public Boss(AsynchAgentsModel officeSpace, Office office, double x, double y) {
		super(new RectNetworkItem(x, y));

		super.setColor(Color.RED);
		this.office = office;
		this.officeSpace = officeSpace;
	}

	public Boss(AsynchAgentsModel officeSpace, Office office) {
		this(officeSpace, office, 0, 0 );
	}
	
	/**
	 * This agents action, its "step" function.  This assigns tasks
	 * to random agents. 
	 */
	public void distributeTasks() {
		int maxEmpIndice = office.getEmployees().size() - 1;
		
		if (maxEmpIndice == -1)
			return;
		
		for (int i = 0; i < tasksAssignedPerTick; i++) {
			int randomEmployeeIndex = Random.uniform.nextIntFromTo(0, maxEmpIndice);
			
			Employee emp = (Employee) office.getEmployees().get(randomEmployeeIndex);
			
			// this will return false if the employee refuses the task
			emp.addTask(new Task());			
		}
	}
	
	/**
	 * @return how many tasks to randomly distribute each tick
	 */
	public int getTasksAssignedPerTick() {
		return tasksAssignedPerTick;
	}
	
	/**
	 * @param val how many tasks to randomly distribute each tick
	 */
	public void setTasksAssignedPerTick(int val) {
		this.tasksAssignedPerTick = val;
	}
}