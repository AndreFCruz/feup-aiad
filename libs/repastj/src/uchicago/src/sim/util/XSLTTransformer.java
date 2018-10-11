/*$$
 * Copyright (c) 2005, Argonne National Laboratory
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
package uchicago.src.sim.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Class used to transform xml documents using XSLT. This is used to convert xml
 * batch parameter files to the default repast batch parameter file format.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.2 $ $Date: 2005/08/12 16:13:29 $
 */
public class XSLTTransformer {
	private XSLTTransformer() {
		super();
	}

	/**
	 * Transforms a given xml file using a given XSLT translation file, writing
	 * the result to a given output file.
	 * 
	 * @param xsltFileName
	 *            the name of the XSLT translation file
	 * @param xmlFileToTransformName
	 *            the name of the xml file to translate
	 * @param outputFileName
	 *            the name of file to write to (can be null, in which case a
	 *            temporary file is used)
	 * @return the name of the output file
	 */
	public static String transform(String xsltFileName,
			String xmlFileToTransformName, String outputFileName) {
		try {
			return transform(new FileInputStream(xsltFileName),
					new FileInputStream(xsltFileName), outputFileName);
		} catch (FileNotFoundException e) {
			SimUtilities.showError(
					"XSLTTransformer.transform: Could not read an input file",
					e);
			return null;
		}
	}

	/**
	 * Transforms a given xml file using a given XSLT translation file, writing
	 * the result to a given output file.
	 * 
	 * @param xsltFileName
	 *            the XSLT translation file
	 * @param xmlFileToTransformName
	 *            the xml file to translate
	 * @param outputFileName
	 *            the name of file to write to (can be null, in which case a
	 *            temporary file is used)
	 * @return the name of the output file
	 */
	public static String transform(InputStream xsltFile,
			InputStream xmlFileToTransform, String outputFileName) {
		if (outputFileName == null) {
			try {
				outputFileName = File.createTempFile("pattern", ".suffix")
						.getAbsolutePath();
			} catch (IOException e) {
				SimUtilities
						.showError(
								"XSLTTransformer.transform: Could not get a temporary file",
								e);
				return null;
			}
		}

		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer(new StreamSource(
					xsltFile));

			transformer.transform(new StreamSource(xmlFileToTransform),
					new StreamResult(new FileOutputStream(outputFileName)));
		} catch (Exception ex) {
			SimUtilities
					.showError(
							"XSLTTransformer.transform: Error transforming xml file using xslt",
							ex);
			return null;
		}

		return outputFileName;
	}
}
