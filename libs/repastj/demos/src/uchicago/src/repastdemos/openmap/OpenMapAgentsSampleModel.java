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
package uchicago.src.repastdemos.openmap;

import anl.repast.gis.data.OpenMapData;
import anl.repast.gis.display.OpenMapDisplay;

import uchicago.src.sim.engine.AbstractGUIController;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.util.Random;
import uchicago.src.sim.util.SimUtilities;

import java.util.ArrayList;


/**
 * @author Robert Najlis
 */
public class OpenMapAgentsSampleModel extends SimModelImpl {
	Schedule schedule;
	OpenMapData gisData;
	OpenMapDisplay omDisplay;
	ArrayList pointAgents;
	ArrayList circleAgents;
	ArrayList locAgents;

	public OpenMapAgentsSampleModel() {
	}

	public static void main(String[] args) {
		SimInit init = new SimInit();
		OpenMapAgentsSampleModel model = new OpenMapAgentsSampleModel();
		init.loadModel(model, null, false);
	}

	public void step() {
		// update point Agents
		for (int i = 0; i < pointAgents.size(); i++) {
			((PointAgent) pointAgents.get(i)).step();
		}

		for (int i = 0; i < circleAgents.size(); i++) {
			((CircleAgent) circleAgents.get(i)).step();
		}

		for (int i = 0; i < locAgents.size(); i++) {
			((URLRasterLocationAgent) locAgents.get(i)).step();
		}

		omDisplay.updateDisplay();

		//	omDisplay.updateLayer(pointAgents, "Point Agents");
		//	omDisplay.updateLayer(circleAgents, "CircleAgents");
		//	omDisplay.updateLayer(locAgents, "locationAgents");
	}

	public void buildModel() {
		gisData = OpenMapData.getInstance();
		omDisplay = new OpenMapDisplay("CreateGisAgentsModel");

		//esriHandler = ESRIHandler.getInstance();
		Random.createUniform();

		try {
			// create point agents
			pointAgents = new ArrayList();

			for (int i = 0; i < 20; i++) {
				float lat = Random.uniform.nextFloatFromTo(40, 50);
				float lon = Random.uniform.nextFloatFromTo(-70, -80);

				PointAgent pa = new PointAgent(lat, lon);

				//  pa.getOMGraphic().setLinePaint(Color.GREEN); 
				pointAgents.add(pa);
			}

			circleAgents = new ArrayList();

			for (int i = 0; i < 20; i++) {
				float lat = Random.uniform.nextFloatFromTo(40, 50);
				float lon = Random.uniform.nextFloatFromTo(-70, -80);
				float radius = (float) .5;
				CircleAgent ca = new CircleAgent(lat, lon, radius);
				ca.setGisAgentIndex(i);

				//  pa.getOMGraphic().setLinePaint(Color.GREEN); 
				circleAgents.add(ca);
			}

			locAgents = new ArrayList();

			for (int i = 0; i < 20; i++) {
				float lat = Random.uniform.nextFloatFromTo(30, 60);
				float lon = Random.uniform.nextFloatFromTo(-60, -90);
				URLRasterLocationAgent ca = 
					new URLRasterLocationAgent(lat,
												lon,
												"" + i,
												"airplane.gif");

				//  pa.getOMGraphic().setLinePaint(Color.GREEN); 
				ca.setGisAgentIndex(i);
				locAgents.add(ca);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buildSchedule() {
		schedule.scheduleActionBeginning(1.0, this, "step");
	}

	/* (non-Javadoc)
	 * @see uchicago.src.sim.engine.SimModel#begin()
	 */
	public void begin() {
		buildModel();
		buildDisplay();
		buildSchedule();
	}

	/* (non-Javadoc)
	 * @see uchicago.src.sim.engine.SimModel#setup()
	 */
	public void setup() {
		schedule = null;
		System.gc();

		schedule = new Schedule();

		AbstractGUIController.CONSOLE_ERR = false;
		AbstractGUIController.CONSOLE_OUT = false;
		AbstractGUIController.ALPHA_ORDER = false;

		Random.createUniform();
	}

	public void reset() {
		this.schedule = null;

		System.gc();

		// get the new input paramenters
		this.getInitParam();

		Random.createUniform();
		Random.createNormal(0.0, 1.0);
	}

	public void buildDisplay() {
		//openmapHandler.buildMapDisplay(SluGIS.datasource, "SluGIS");
		//gisHandler.buildMapDisplay(SluGIS.datasource, "SluGIS");
		// omDisplay.buildMapDisplay("SluGIS");
		omDisplay.addLayer(pointAgents, "Point Agents");
		omDisplay.addLayer(circleAgents, "Circle Agents");
		omDisplay.addLayer(locAgents, "Location Agents");
		String shapeFileName = SimUtilities.getDataFileName("openmap/chicago.shp");
		omDisplay.addShapeLayer(shapeFileName, "Chicago");

		//	omData.writeAgentsNoShp(pointAgents, omDisplay.getProjection(), "/Volumes/Indigo/Users/rnajlis/Programming/GIS/Data/ParcelallTest/pointAgents.shp");
	}

	/* (non-Javadoc)
	 * @see uchicago.src.sim.engine.SimModel#getSchedule()
	 */
	public Schedule getSchedule() {
		return schedule;
	}

	/* (non-Javadoc)
	 * @see uchicago.src.sim.engine.SimModel#getInitParam()
	 */
	public String[] getInitParam() {
		String[] params = {  };

		return params;
	}

	/* (non-Javadoc)
	 * @see uchicago.src.sim.engine.SimModel#getName()
	 */
	public String getName() {
		return "CreateGisAgentsModel";
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}
}
