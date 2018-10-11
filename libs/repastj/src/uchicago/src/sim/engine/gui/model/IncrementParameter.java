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
package uchicago.src.sim.engine.gui.model;

/**
 * @author wes maciorowski
 */
public class IncrementParameter extends DataParameter {
	Object end;

	Object increment;

	Object start;

	/**
	 * @param runs
	 * @param name
	 * @param dataType
	 * @param input
	 */
	public IncrementParameter(int runs, String name, String dataType,
			boolean input, Object start, Object end, Object increment) {
		super(runs, name, dataType, input);
		this.start = start;
		this.end = end;
		this.increment = increment;
	}

	/**
	 * @param end
	 *            The end to set.
	 */
	public void setEnd(Object end) {
		this.end = end;
	}

	/**
	 * @return Returns the end.
	 */
	public Object getEnd() {
		return end;
	}

	/**
	 * @param increment
	 *            The increment to set.
	 */
	public void setIncrement(Object increment) {
		this.increment = increment;
	}

	/**
	 * @return Returns the increment.
	 */
	public Object getIncrement() {
		return increment;
	}

	/**
	 * @param start
	 *            The start to set.
	 */
	public void setStart(Object start) {
		this.start = start;
	}

	/**
	 * @return Returns the start.
	 */
	public Object getStart() {
		return start;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer(name);
		buf.append('(').append(dataType).append(',');
		buf.append(input).append(',').append(runs).append(')');
		buf.append("[start=").append(start).append(",end=").append(end);
		buf.append(",increment=").append(increment).append(']');

		return buf.toString();
	}

	public String toXMLOpenString() {
		StringBuffer buf = new StringBuffer("Repast:Param name=");
		buf.append('"').append(name).append('"');

		// do type
		buf.append(" type=").append('"').append("incr").append('"');

		// do value
		buf.append(" start=").append('"').append(start).append('"');
		buf.append(" end=").append('"').append(end).append('"');
		buf.append(" incr=").append('"').append(increment).append('"');

     	// do input/output
     	buf.append(getIOString());
		
		return buf.toString();
	}

	public String toXMLCloseString() {
		return "Repast:Param";
	}
}
