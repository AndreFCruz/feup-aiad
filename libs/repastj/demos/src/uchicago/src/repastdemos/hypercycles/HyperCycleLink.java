/*$$
 * Copyright (c) 1999, Trustees of the University of Chicago
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
 * Neither the name of the University of Chicago nor the names of its
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
package uchicago.src.repastdemos.hypercycles;

import java.awt.Color;

import uchicago.src.sim.gui.DrawableEdge;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.network.DefaultEdge;
import uchicago.src.sim.network.Node;


/**
 * A link from one cell to another indicating that these cells
 * participate in a hypercycle.
 *
 * An implementation of the simulation described in John Padgett's
 * "The Emergence of Simple Ecologies of Skill: A Hypercycle Approach to
 * Economic Organization" in _The Economy as an Evolving
 * Complex System II_, Eds. Arthur, Durlauf, and Lane. SFI
 * Studies in the Sciences of Complexity, Vol. XXVII, Addison-Wesley, 1997,
 * pp. 199-221. Thanks to John Padgett for allowing us to include it here.
 * jpadgett@midway.uchicago.edu
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/08/12 20:04:57 $
 */

public class HyperCycleLink extends DefaultEdge implements DrawableEdge {

  public HyperCycleLink(Node from, Node to) {
    super(from, to, "");
  }

  public void draw(SimGraphics g, int fromX, int toX, int fromY, int toY) {
    g.drawLink(Color.green, fromX, toX, fromY, toY);
  }
}




