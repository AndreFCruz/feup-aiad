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
package uchicago.src.sim.space;


/**
 * A torus object that can hold more than one object in its cells.
 * The cells themselves store their occupants without
 * any order. Use an <code>OrderedMulti2DTorus</code> if you need
 * the cell occupants to be stored in an ordered fashion. The object
 * return by getObjectAt is a <code>BagCell</code>.
 *
 * The hexagonal cells are referenced by x, y coordinates as follows:
 * <pre>
 *       _
 *   _ / 1 \ _
 * / 0 \ _ / 2 \
 * \ _ / 4 \ _ /
 * / 3 \ _ / 5 \
 * \ _ / 7 \ _ /
 * / 6 \ _ / 8 \
 * \ _ /   \ _ /
 *
 * </pre>
 *
 * Here we have a 3 x 3 hexagonal grid. The first row of cells is 0,
 * 1, 2 such that 0,0 refers to cell 0, and 0,2 refers to cell 2. The
 * next row of cells is 3, 4, 5, so 1,0 refers to cell 3 and so
 * on. The last row of cells is 6, 7, and 8, so 2, 0 refers to cell 6.
 * The ring of neighbors with radius one that surrounds cell 4 is
 * composed of 1, 2, 5, 7, 3, and 0. The grid wraps as a toriod such
 * that cell -1, 0 refers to cell 2 and cell 0, -1 is cell 6.
 
 * @version $Revision: 1.4 $ $Date: 2004/11/03 19:50:58 $
 *
 */


public class Multi2DHexagonalTorus extends AbsMulti2DHexagonalTorus {

    /**
     * Creates this Multi2DHexagonalTorus with the specified dimensions.
     * sparse specifies whether the torus will be sparsely filled or not.
     *
     * @param xSize the number of columns in the grid
     * @param ySize the number of rows in the grid
     * @param sparse whether the torus will be sparsely populated or not
     */
    public Multi2DHexagonalTorus(int xSize, int ySize, boolean sparse) {
        super(xSize, ySize, sparse);
    }

    /**
     * Puts the specified Object into the cell at the specified coordinates.
     * The contents of the cell are unordered.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param object the object to put
     */
    public void putObjectAt(int x, int y, Object object) {
        x = xnorm(x);
        y = ynorm(y);
        Cell c = (Cell) matrix.get(x, y);

        if (c == null) {
            c = new BagCell();
            matrix.put(x, y, c);
        }
        c.add(object);
    }
}

