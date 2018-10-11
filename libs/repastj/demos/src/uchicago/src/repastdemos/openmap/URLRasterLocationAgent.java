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

import anl.repast.gis.OpenMapAgent;

import com.bbn.openmap.LatLonPoint;
import com.bbn.openmap.layer.location.URLRasterLocation;
import com.bbn.openmap.omGraphics.OMGraphic;

import uchicago.src.sim.util.Random;

import java.awt.Color;
import java.awt.Paint;


/**
 * @author Robert Najlis
 */
public class URLRasterLocationAgent implements OpenMapAgent {
	// URLRasterLocation circle;
	URLRasterLocation loc;
	int gisAgentIndex;

	public URLRasterLocationAgent() {
	}

	public URLRasterLocationAgent(float lat, float lon, String name,
								  String iconURL) {
		loc = new URLRasterLocation(lat, lon, name, iconURL);

		//      OMGraphic marker = new OMPoint();
		//    marker.setFillPaint(Color.BLUE);
		//  loc = new BasicLocation(lat, lon, 1, 1, "Basic Loc", marker);
	}

	public LatLonPoint getLatLon() {
		LatLonPoint llp = new LatLonPoint(loc.lat, loc.lon);

		return llp; //this.loc.getLatLon();

		// return loc.lat;
	}

	public void setLatLon(float lat, float lon) {
		this.loc.setLocation(lat, lon);

		//   this.loc.lat = lat;
	}

	/* (non-Javadoc)
	 * @see anl.repast.gis.adaptors.openmap.OMDisplayable#omPropertyList()
	 */
	public String[] gisPropertyList() {
		String[] s = { "LatLon", "getLatLon" };

		return s;
	}

	/* (non-Javadoc)
	 * @see anl.repast.gis.adaptors.openmap.OMDisplayable#getFillPaint()
	 */
	public Paint getFillPaint() {
		return Color.GREEN;
	}

	/* (non-Javadoc)
	 * @see anl.repast.gis.adaptors.openmap.OMDisplayable#getEsriGraphic()
	 */
	public OMGraphic getOMGraphic() {
		return loc;
	}

	public void step() {
		LatLonPoint llp = this.getLatLon();
		float lat = llp.getLatitude() +
					(float) Random.uniform.nextDoubleFromTo(-.5, .5);
		float lon = llp.getLongitude() +
					(float) Random.uniform.nextDoubleFromTo(-.5, .5);
		this.setLatLon(lat, lon);

		//        this.setLatLon(this.getLatitude() + (float)Random.uniform.nextDoubleFromTo(-.5,.5));
		//     this.setLongitude(this.getLongitude() + (float)Random.uniform.nextDoubleFromTo(-.5,.5));
	}

	/* (non-Javadoc)
	 * @see anl.repast.gis.GisAgent#getGisAgentIndex()
	 */
	public int getGisAgentIndex() {
		return gisAgentIndex;
	}

	/* (non-Javadoc)
	 * @see anl.repast.gis.GisAgent#setGisAgentIndex(int)
	 */
	public void setGisAgentIndex(int index) {
		this.gisAgentIndex = index;
	}

	/* (non-Javadoc)
	 * @see anl.repast.gis.GisAgent#setNeighbors(int[])
	 */
	public void setNeighbors(int[] neighbors) {
		
	}

	/* (non-Javadoc)
	 * @see anl.repast.gis.GisAgent#getNeighbors()
	 */
	public int[] getNeighbors() {
		return null;
	}

	/* (non-Javadoc)
	 * @see anl.repast.gis.GisAgent#setOMGraphic(com.bbn.openmap.omGraphics.OMGraphic)
	 */
	public void setOMGraphic(OMGraphic omg) {
		if (omg instanceof URLRasterLocation) {
			this.loc = (URLRasterLocation) omg;
		}
	}
}
