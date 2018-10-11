/*$$
 * Copyright (c) 2004, Argonne National Laboratory (ANL)
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
 * Neither the name of ANL nor the names of its
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
package anl.repast.gis;

import java.awt.Paint;

import com.bbn.openmap.omGraphics.OMGraphic;

/*
 *  @author Robert Najlis
 *  Created on Sep 27, 2004
 *
 *
 */
public class DefaultGisAgent implements GisAgent 
{

    int gisAgentIndex;
     OMGraphic omg;
    
    

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
     * @see anl.repast.gis.GisAgent#gisPropertyList()
     */
    public String[] gisPropertyList() {
        return new String [] {"Agent Index", "getGisAgentIndex"};
    }

    /* (non-Javadoc)
     * @see anl.repast.gis.GisAgent#getFillPaint()
     */
    public Paint getFillPaint() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see anl.repast.gis.GisAgent#setNeighbors(int[])
     */
    public void setNeighbors(int[] neighbors) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see anl.repast.gis.GisAgent#getNeighbors()
     */
    public int[] getNeighbors() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see anl.repast.gis.GisAgent#getOMGraphic()
     */
    public OMGraphic getOMGraphic() {
        return omg;
    }

    /* (non-Javadoc)
     * @see anl.repast.gis.GisAgent#setOMGraphic(com.bbn.openmap.omGraphics.OMGraphic)
     */
    public void setOMGraphic(OMGraphic omg) {
        this.omg = omg;
    }
}
