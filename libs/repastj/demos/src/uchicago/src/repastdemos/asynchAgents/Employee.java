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
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import uchicago.src.sim.engine.AsynchAgent;
import uchicago.src.sim.engine.CustomProbeable;
import uchicago.src.sim.engine.SimModel;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.util.RepastException;


/**
 * This class represents the employees who perform tasks assigned by the boss.
 * This agent type is not directly executed by the model itself.  When the boss
 * assigns a tasks to the agent, the agent schedules itself to perform the task.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/08/12 20:04:57 $
 */
public class Employee extends AsynchAgent implements CustomProbeable {
	public static final int MAX_TASKS = 60;
	
	private static Image employeePicture;
	
	private static int baseIdNumber;

	private ArrayList taskList = new ArrayList();
	
	public Employee(double x, double y, SimModel model) throws RepastException {
		// to get semi-unique colors on the graphs
		super(model);
		
		super.setX(x);
		super.setY(y);
		
		this.setColor(getNextColor());
		
		loadEmployeePicture();
		this.setNodeLabel("Milton " + ++baseIdNumber);
	}

	public Employee(SimModel model) throws RepastException {
		this(0, 0, model);
	}

	public ArrayList getTaskList() {
		return taskList;
	}
	
	public void draw(SimGraphics g) {
		// draw the employee's picture
		g.drawImage(employeePicture);
		
		// grab the width of the picture
		int width = employeePicture.getWidth(null);
		
		g.setFont(super.getFont());
		
		// get the size of the node's text
		Rectangle2D bounds = g.getStringBounds(this.getNodeLabel());
		
		// set the graphics to draw the text above the label
		// the x coordinate is relative to the upper left corner of the image
		// so the coordinates are shifted to account for that
		g.setDrawingCoordinates((float) (this.getX() + width / 2.0 - bounds.getWidth() / 2.0),
								(float) (this.getY() - bounds.getHeight() - 2),
								0f);
		
		// draw the label
		g.drawString(getNodeLabel(), Color.BLACK);
	}
	
	/**
	 * Adds a task to this employee.  If this employee has too many tasks
	 * assigned he/she refuses to do it and returns false.
	 *  
	 * @param task	the task to perform
	 * @return if the task was added
	 */
	public boolean addTask(Task task) {
		if (taskList.size() > MAX_TASKS)
			return false;
		
		taskList.add(task);
		
		super.scheduleWhenAvailable("performTask", task);
		
		return true;
	}
	
	public void performTask(Task taskToPerform) {
		// perform task
		
		// .... task being performed ....
			
		// now that the task has been magically performed, 
		// remove it from the list
		taskList.remove(taskToPerform);
	}

	
	private static Color getNextColor() {
		if (colorIndex == colors.length)
			colorIndex = 0;
		
		return colors[colorIndex++];
	}

	private static int colorIndex = 0;
	private static final Color[] colors = new Color[] {
			Color.PINK,
			Color.BLUE,
			Color.GRAY,
			Color.GREEN,
			Color.MAGENTA,
			Color.YELLOW,
			Color.WHITE,
			Color.CYAN
	};

	
	public static void resetIndices() {
		baseIdNumber = 0;
	}
	
	private static void loadEmployeePicture() {
		if (employeePicture == null) {
			java.net.URL employeePicURL = Employee.class.getResource("person.gif");
			employeePicture = new ImageIcon(employeePicURL).getImage(); 
		}
	}

	public String[] getProbedProperties() {
		return new String[] { "NumberOfTasks", "NodeLabel" };
	}
	
	public int getNumberOfTasks() { return this.taskList.size(); }
}