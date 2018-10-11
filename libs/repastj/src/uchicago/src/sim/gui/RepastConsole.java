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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import net.sourceforge.indigosim.GUI.CopyPrintStream;
import uchicago.src.sim.util.SimUtilities;

public class RepastConsole extends WindowAdapter implements WindowListener,
		Runnable {
	private JFrame frame;

	private JTextArea textArea;

	private Thread reader;

	private boolean quit;

	private boolean stdoutOn = true;

	private boolean stderrOn = true;

	protected final static PrintStream oldStdout = System.out;

	protected final static PrintStream oldStderr = System.err;

	protected static PrintStream newStdout;

	protected static PrintStream newStderr;

	protected static ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

	public RepastConsole(boolean stdout, boolean stderr) {
		frame = FrameFactory.createFrame("RePast Output");
		Rectangle rect = FrameFactory.getBounds("RePast Output");
		if (rect == null) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

			Dimension frameSize = new Dimension((screenSize.width - 40), 180);
			int x = (20);
			int y = (screenSize.height - 220);

			frame.setBounds(x, y, frameSize.width, frameSize.height);
		}

		textArea = new JTextArea();
		textArea.setEditable(false);

		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(new JScrollPane(textArea),
				BorderLayout.CENTER);
		frame.addWindowListener(this);
		newStdout = new CopyPrintStream(new PrintStream(byteStream), oldStdout);
		newStderr = new CopyPrintStream(new PrintStream(byteStream), oldStderr);

		setStdoutOn(stdout);
		setStderrOn(stderr);
		quit = false;
		display();
	}

	public void display() {
		frame.setVisible(true);
		if (stdoutOn | stderrOn) {
			reader = new Thread(this);
			reader.setDaemon(true);
			reader.start();
		}
	}

	public boolean isStdoutOn() {
		return stdoutOn;
	}

	public void setStdoutOn(boolean stdoutOn) {
		this.stdoutOn = stdoutOn;
		if (stdoutOn) {
			System.setOut(newStdout);
		} else {
			System.setOut(oldStdout);
		}
	}

	public boolean isStderrOn() {
		return stderrOn;
	}

	public void setStderrOn(boolean stderrOn) {
		this.stderrOn = stderrOn;
		if (stderrOn) {
			System.setErr(newStderr);
		} else {
			System.setErr(oldStderr);
		}
	}

	public synchronized void dispose() {
		quit = true;
		// this.notifyAll();
		frame.dispose();
	}

	public synchronized void windowClosed(WindowEvent evt) {
		quit = true;
		this.notifyAll();
		quit = true;
		this.notifyAll();

		System.setErr(oldStderr);
		System.setOut(oldStdout);
	}

	public synchronized void windowClosing(WindowEvent evt) {
		frame.setVisible(false);
		frame.dispose();
	}

	public synchronized void run() {
		try {

			copyOutputToConsole();

		} catch (Exception e) {
			SimUtilities.showError("Error displaying message", e);
		}
	}

	protected synchronized void copyOutputToConsole() throws Exception {
		Cat cat = new Cat();

		while (true) {
			try {
				this.wait(100);
			} catch (InterruptedException ie) {
			}

			if (byteStream.size() > 0) {
				final String input = byteStream.toString();
				byteStream.reset();

				SwingUtilities.invokeAndWait(cat.set(input));
			}

			if (quit)
				return;
		}
	}

	protected class Cat implements Runnable {
		String message;

		Cat set(String m) {
			message = m;
			return this;
		}

		public void run() {
			textArea.append(message);

			textArea.setCaretPosition(textArea.getDocument().getLength());
		}
	}

	public static void main(String[] arg) {
		new RepastConsole(true, true);
		System.out.println("This is my message");
		System.err.println("This is a test of err");
		System.out.println("This is my log message");
	}
}
