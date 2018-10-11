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
package uchicago.src.repastdemos.genetic;

/**
 * A simple class containing the coins an agent has
 *
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/08/12 20:04:55 $
 */
public class CoinStruct {
	public String toString()
	{
		StringBuffer coinString = new StringBuffer();

		coinString.append("total=").append(quarters + dimes + nickels + pennies);
		coinString.append(",value=").append(quarters * .25 + dimes * .1 + nickels * .05 + pennies * .01);
		coinString.append(",q=").append(quarters);
		coinString.append(",d=").append(dimes);
		coinString.append(",n=").append(nickels);
		coinString.append(",p=").append(pennies);

		return coinString.toString();
	}
	
	public int quarters;
	public int dimes;
	public int nickels;
	public int pennies;
}
