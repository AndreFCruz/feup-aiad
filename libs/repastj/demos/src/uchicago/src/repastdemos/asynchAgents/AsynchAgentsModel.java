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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import uchicago.src.sim.analysis.AverageSequence;
import uchicago.src.sim.analysis.MaxSequence;
import uchicago.src.sim.analysis.MinSequence;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.engine.AsynchSchedule;
import uchicago.src.sim.engine.SimpleModel;
import uchicago.src.sim.gui.DefaultGraphLayout;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Network2DDisplay;
import uchicago.src.sim.util.Random;
import uchicago.src.sim.util.RepastException;
import uchicago.src.sim.util.SimUtilities;

/**
 * A model illustrating the use of the asynchronous agents and schedule.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/08/12 20:04:57 $
 */
public class AsynchAgentsModel extends SimpleModel {
	protected Office office;
	
	private int employeeCount = 9;

	private DisplaySurface officeDisplaySurface;

	private int officeWidth = 500, officeHeight = 500;

	private OpenSequenceGraph officeGraph;
	private OpenSequenceGraph individualGraph;
	
	
	public AsynchAgentsModel() {
		// setup the random generator
		Random.createUniform();
		// and the list of agents that will eventually hold everything
		// that gets "stepped"
		if (super.agentList == null)
			super.agentList = new ArrayList();
	}

	public String[] getInitParam() {
		return new String[] { 
			"OfficeHeight",
			"OfficeWidth",
			"EmployeeCount",
			"TasksAssignedPerTick"
		};
	}

	public void begin() {
		try {
			super.begin();

			// reset the agent numbers and such
			Employee.resetIndices();

			// hire all the workers
			office.hireEmployees(employeeCount, this);
			office.hireBoss(this);
			office.hireConsultant(this);

			for (Iterator iter = office.getEmployees().iterator(); iter.hasNext(); ) {
				Employee emp =  (Employee) iter.next();

				// The delay represents how long the agent will wait before
				// performing actions.  This would represent some agents taking
				// longer to perform tasks then others.
				double delay1 = Random.uniform.nextDoubleFromTo(.2, 1.5);
				double delay2 = Random.uniform.nextDoubleFromTo(.2, 1.5);
				
				if (delay1 < delay2) {
					emp.setDelayMinimum(delay1);
					emp.setDelayMaximum(delay2);
				} else {
					emp.setDelayMinimum(delay2);
					emp.setDelayMaximum(delay1);
				}
				
				// load up the agent with some tasks right away 
				int preLoadedTaskCount = 
					Random.uniform.nextIntFromTo(0, this.getTasksAssignedPerTick());
				
				for (int i = 0; i < preLoadedTaskCount; i++)
					emp.addTask(new Task());
			}
			
			// create all the displays
			this.buildDisplay();
			this.buildGraphs();

			// Schedule some pictures of the displays if you'd like
			//			new SnapshotScheduler("display", officeDisplaySurface,
			// "display").scheduleAtInterval(schedule, 200);
			//			new SnapshotScheduler("individual", individualGraph,
			// "individual").scheduleAtInterval(schedule, 400);
			//			new SnapshotScheduler("office", officeErrorGraph,
			// "office").scheduleAtInterval(schedule, 400);
		} catch (RepastException ex) {
			SimUtilities.showError("Error readying the model", ex);
			super.stop();
		}
	}

	/**
	 * This builds the display surface for the office
	 */
	protected void buildDisplay() {
		officeDisplaySurface = new DisplaySurface(new Dimension(officeWidth,
				officeHeight), this, "Office Display");

		// create the graph layout that holds the agents that get displayed
		DefaultGraphLayout layout = new DefaultGraphLayout(officeWidth,
				officeHeight);
		layout.getNodeList().addAll(office.getEmployees());
		layout.getNodeList().add(office.getBoss());

		// tell the display surface to display the layout (after wrapping
		// it in a Network2DDisplay
		Network2DDisplay officeNetDisplay = new Network2DDisplay(layout);
		officeDisplaySurface.addDisplayableProbeable(officeNetDisplay,
				"Office display");
		this.registerDisplaySurface("display", officeDisplaySurface);
		officeDisplaySurface.setBackground(Color.WHITE);
		officeDisplaySurface.display();
		
		registerDisplaySurface("office", officeDisplaySurface);
	}

	/**
	 * This builds the error graphs.
	 */
	protected void buildGraphs() {
		officeGraph = new OpenSequenceGraph("Office statistics", this);
		
		officeGraph.addSequence("Maximum tasks to perform", 
				new MaxSequence(office.getEmployees(), "getNumberOfTasks"));
		
		officeGraph.addSequence("Average tasks to perform", 
				new AverageSequence(office.getEmployees(), "getNumberOfTasks"));
		
		officeGraph.addSequence("Minimum tasks to perform", 
				new MinSequence(office.getEmployees(), "getNumberOfTasks"));
		
		individualGraph = new OpenSequenceGraph("Individual statistics", this);
		for (Iterator iter = office.getEmployees().iterator(); iter.hasNext(); ) {
			Employee emp = (Employee) iter.next();
			
			individualGraph.createSequence(emp.getNodeLabel() + " task count",
									emp, "getNumberOfTasks");
		}
		
		registerMediaProducer("office graph", officeGraph);
		registerMediaProducer("individual graph", individualGraph);
		
		officeGraph.display();
		individualGraph.display();
	}

	/**
	 * Sets up the model for the next run, clears out all the old employees and
	 * the old displays
	 */
	public void setup() {
		// load up an asynchronous schedule
	    schedule = new AsynchSchedule();

		office = new Office(officeWidth, officeHeight);

		officeDisplaySurface = null;

		if (officeGraph != null)
			officeGraph.dispose();
		
		officeGraph = null;

		if (individualGraph != null)
			individualGraph.dispose();
		
		individualGraph = null;
		
		// Add the custom action button that will randomly scatter the
		// agents on the display surface
		getModelManipulator().addButton("Spread out agents",
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Iterator iter = ((ArrayList) office.getEmployees()
								.clone()).iterator();
						for (; iter.hasNext();) {
							Employee emp = (Employee) iter.next();

							emp.setX(Random.uniform.nextIntFromTo(0, office
									.getWidth()));
							emp.setY(Random.uniform.nextIntFromTo(0, office
									.getHeight()));
						}

						officeDisplaySurface.updateDisplayDirect();
					}
				});
	}

	protected void step() {
		// Important to note here is that the model is not directly controlling
		// the employees.  The employees only perform tasks when they need to,
		// so the model doesn't have to worry about them, they will schedule
		// themselves to perform a task
		office.getBoss().distributeTasks();
		office.getConsultant().gradeEmployees();
		
		officeDisplaySurface.updateDisplay();
		officeGraph.step();
		individualGraph.step();
	}

	public String getName() {
		return "Asynchronous Agents Model";
	}

	/**
	 * @return Returns the number of employees in the office.
	 */
	public int getEmployeeCount() {
		return employeeCount;
	}

	/**
	 * @param employeeCount
	 *            The number of employees in the office.
	 */
	public void setEmployeeCount(int employeeCount) {
		this.employeeCount = employeeCount;
	}

	/**
	 * @return returns the officeHeight
	 */
	public int getOfficeHeight() {
		return officeHeight;
	}

	/**
	 * @param officeHeight
	 *            the officeHeight
	 */
	public void setOfficeHeight(int officeHeight) {
		this.officeHeight = officeHeight;
	}

	/**
	 * @return returns the officeWidth
	 */
	public int getOfficeWidth() {
		return officeWidth;
	}

	/**
	 * @param officeWidth
	 *            the officeWidth
	 */
	public void setOfficeWidth(int officeWidth) {
		this.officeWidth = officeWidth;
	}
	
	public int getTasksAssignedPerTick() {
		return office.getTasksAssignedPerTick();
	}
	public void setTasksAssignedPerTick(int val) {
		office.setTasksAssignedPerTick(val);
	}


	public static void main(String[] args) {
		uchicago.src.sim.engine.SimInit init = new uchicago.src.sim.engine.SimInit();
		AsynchAgentsModel model = new AsynchAgentsModel();
		if (args.length > 0)
			init.loadModel(model, args[0], false);
		else
			init.loadModel(model, null, false);
	}
}