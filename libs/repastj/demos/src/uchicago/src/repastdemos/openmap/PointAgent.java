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

import com.bbn.openmap.dataAccess.shape.EsriPoint;
import com.bbn.openmap.layer.location.Location;
import com.bbn.openmap.omGraphics.OMGraphic;

import uchicago.src.sim.util.Random;

import java.awt.Color;
import java.awt.Paint;


/**
 * @author Robert Najlis
 */
public class PointAgent implements OpenMapAgent {
	EsriPoint point;
	Location loc;

	public PointAgent() {
	}

	public PointAgent(float lat, float lon) {
		point = new EsriPoint(lat, lon);

		//      OMGraphic marker = new OMPoint();
		//    marker.setFillPaint(Color.BLUE);
		//  loc = new BasicLocation(lat, lon, 1, 1, "Basic Loc", marker);
	}

	public float getLatitude() {
		return this.point.getLat();
	}

	public void setLatitude(float lat) {
		this.point.setLat(lat);
	}

	public float getLongitude() {
		return this.point.getLon();
	}

	public void setLongitude(float lon) {
		this.point.setLon(lon);
	}

	/* (non-Javadoc)
	 * @see anl.repast.gis.adaptors.openmap.OMDisplayable#omPropertyList()
	 */
	public String[] gisPropertyList() {
		String[] s = { "Latitude", "getLatitude", "Longitude", "getLongitude" };

		return s;
	}

	/* (non-Javadoc)
	 * @see anl.repast.gis.adaptors.openmap.OMDisplayable#getFillPaint()
	 */
	public Paint getFillPaint() {
		return Color.ORANGE;
	}

	/* (non-Javadoc)
	 * @see anl.repast.gis.adaptors.openmap.OMDisplayable#getEsriGraphic()
	 */
	public OMGraphic getOMGraphic() {
		return point;
	}

	public void step() {
		this.setLatitude(this.getLatitude() +
						 (float) Random.uniform.nextDoubleFromTo(-.5, .5));
		this.setLongitude(this.getLongitude() +
						  (float) Random.uniform.nextDoubleFromTo(-.5, .5));
	}

	/* (non-Javadoc)
	 * @see anl.repast.gis.GisAgent#getGisAgentIndex()
	 */
	public int getGisAgentIndex() {

		return 0;
	}

	/* (non-Javadoc)
	 * @see anl.repast.gis.GisAgent#setGisAgentIndex(int)
	 */
	public void setGisAgentIndex(int index) {

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
		if (omg instanceof EsriPoint) {
			this.point = (EsriPoint) omg;
		}
	}
}
