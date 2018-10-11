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
package uchicago.src.sim.parameter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

import uchicago.src.sim.engine.gui.model.DataParameter;

/**
 * Creates an XML based parameter file.
 * 
 * @author wes maciorowski
 */
public class XMLParameterFileWriter {
	private static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";

	// private PrintStream out;

	/** String that represents the current indentation */
	private String indent = new String();

	/**
	 * Creates a parameter file using outputLocation and writes to it all input
	 * parameters stored in treeTop and all output parameters stored in .
	 * 
	 * @param outputLocation
	 * @param treeTop
	 */
	public void write(String outputLocation, DefaultMutableTreeNode treeTop,
			ArrayList outputParmList) {
		FileOutputStream outStream;
		try {
			outStream = new FileOutputStream(new File(outputLocation));
			PrintStream out = new PrintStream(outStream, false,
					DEFAULT_CHARACTER_ENCODING);
			// XML standard header
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			writeXml(out,
					"<Repast:Params xmlns:Repast=\"http://www.src.uchicago.edu\">");
			writeTree(out, treeTop);
			// writeParameterBlock(treeTop, out, 4);
			writeOutputParameters(outputParmList, out);
			writeXml(out, "</Repast:Params>");
			outStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * writes out the parameters based retrieved from a DefaultMutableTreeNode
	 * 
	 * @param outStream
	 *            a print stream to write the data to
	 * @param treeTop
	 *            a tree containing the DataParameters in the userObject
	 * @author Jerry Vos
	 */
	private void writeTree(PrintStream outStream, DefaultMutableTreeNode treeTop) {
		DefaultMutableTreeNode currNode = treeTop;

		Enumeration treeTopChildren = treeTop.children();
		
		Object currNodesObject = currNode.getUserObject();
		DataParameter currentParam = null;
		
		if (currNodesObject != null
				&& currNodesObject instanceof DataParameter) {
			currentParam = (DataParameter) currNodesObject;
		} else {
			currentParam = null;
		}
		
		if (currentParam == null) {
			while (treeTopChildren.hasMoreElements()) {
				writeTree(outStream, (DefaultMutableTreeNode) treeTopChildren
						.nextElement());
			}
		} else {
			if (treeTopChildren.hasMoreElements()) {
				openXmlBlock(outStream, currentParam.toXMLOpenString());

				while (treeTopChildren.hasMoreElements()) {
					writeTree(outStream, (DefaultMutableTreeNode) treeTopChildren
							.nextElement());
				}

				closeXmlBlock(outStream, currentParam.toXMLCloseString());
			} else {
				openAndCloseXmlBlock(outStream, currentParam.toXMLOpenString());
			}
		}
	}

	/**
	 * writes out some xml to a given stream using the current indentation
	 * 
	 * @param outStream
	 *            the stream to write to
	 * @param xmlToWrite
	 *            xml of the form "<property attr="" >"
	 * @author Jerry Vos
	 */
	private void writeXml(PrintStream outStream, String xmlToWrite) {
		outStream.println(indent + xmlToWrite);
	}

	/**
	 * Opens up an xml block by printing the given xml. This increments the
	 * indentation level
	 * 
	 * @param outStream
	 *            the stream to write to
	 * @param xmlToWrite
	 *            the interior of xml <> blocks, if xmlToWrite was "element
	 *            attribute='asdf' blah='afd'" it would become "<element
	 *            attribute='asdf' blah='afd'>"
	 * @author Jerry Vos
	 */
	private void openXmlBlock(PrintStream outStream, String xmlToWrite) {
		writeXml(outStream, "<" + xmlToWrite + ">");
		
		indent += "    ";
	}

	/**
	 * Writes a single xml element. Does not effect the indentation level.
	 * 
	 * @param outStream
	 *            the stream to write to
	 * @param xmlToWrite
	 *            the interior of xml <> blocks, if xmlToWrite was "element
	 *            attribute='asdf' blah='afd'" it would become "<element
	 *            attribute='asdf' blah='afd'/>"
	 * @author Jerry Vos
	 */
	private void openAndCloseXmlBlock(PrintStream outStream, String xmlToWrite) {
		writeXml(outStream, "<" + xmlToWrite + "/>");
	}

	/**
	 * Closes up an xml block by printing the given xml. This decrements the
	 * indentation level
	 * 
	 * @param outStream
	 *            the stream to write to
	 * @param xmlToWrite
	 *            the interior of xml <> blocks, if xmlToWrite was "element" it
	 *            would become "</element>
	 * @author Jerry Vos
	 */
	private void closeXmlBlock(PrintStream outStream, String blockToClose) {
		indent = indent.substring(0, indent.length() - 4);
		outStream.println(indent + "</" + blockToClose + ">");
	}

	private void writeOutputParameters(ArrayList outputParmList,
			PrintStream outStream) {
		DataParameter aParameter = null;
		for (int i = 0; i < outputParmList.size(); i++) {
			aParameter = (DataParameter) outputParmList.get(i);
			openAndCloseXmlBlock(outStream, aParameter.toXMLOpenString());
		}
	}
}
