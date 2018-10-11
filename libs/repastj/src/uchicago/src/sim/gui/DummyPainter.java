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
package uchicago.src.sim.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.util.Collections;

import javax.imageio.ImageIO;

import uchicago.src.sim.util.SimUtilities;

/**
 * A dummy implementation of Painter that forwards paint calls back to the
 * display surface. This is used as the initial painter in a display surface
 * and is replaced once a displayable is added to the surface.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/08/05 18:14:43 $
 */
public class DummyPainter extends Painter {


  /**
   * Creates a DummyPainter
   */
  public DummyPainter(DisplaySurface surface) {
    super();
    this.surface = surface; 
  }

  /**
   * Forwards the paint call back to the DisplaySurface.
   */
  public void paint(final Graphics g) {
    surface.paint(g);
  }

  /**
   * Empty dummy method.
   * @param g
   * @param left
   * @param top
   * @param width
   * @param height
   */ 
  public void drawRect(Graphics g, int left, int top, int width, int height) {
    
  }

  /**
   * Empty dummy method.
   * @param g
   */ 
  public void eraseRect(Graphics g) {
   
  }

  /**
   * Empty dummy method.
   * @param os
   */ 
  public void takeSnapshot(DataOutputStream os) {
    
  }
}
