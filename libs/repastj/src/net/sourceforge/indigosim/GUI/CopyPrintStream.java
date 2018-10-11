package net.sourceforge.indigosim.GUI;

import java.io.*;
/**
 * A PrintStream that will copy output to two different OutputStreams
 * <p>
 * Reuben Grinberg
 * reuben.grinberg@aya.yale.edu
 * August 4th, 2005
 */
public class CopyPrintStream extends PrintStream
{
	protected PrintStream out2;
	protected PrintStream out1;
	
	
	/*Create a new print stream. */
	public CopyPrintStream(OutputStream out1, OutputStream out2)  {
		super(new ByteArrayOutputStream(0));
		this.out1 = new PrintStream(out1);
		this.out2 = new PrintStream(out2);	

	}

	/* Create a new print stream. */
	public CopyPrintStream(OutputStream out1, OutputStream out2, boolean autoFlush) {
		super(new ByteArrayOutputStream(0), autoFlush);
		out1 = new PrintStream(out1, autoFlush);
		out2 = new PrintStream(out2, autoFlush);
	}
	
	/** Create a new print stream. */
	public CopyPrintStream(OutputStream out1, OutputStream out2, boolean autoFlush, String encoding) throws UnsupportedEncodingException {
		super(new ByteArrayOutputStream(0), autoFlush, encoding);
		out1 = new PrintStream(out1, autoFlush, encoding);
		out2 = new PrintStream(out2, autoFlush, encoding);
	}	
	
	/** Flush the stream and check its error state. */
	public boolean checkError() {
		return errorOccured || out1.checkError() || out2.checkError();
	}
	
	private boolean errorOccured = false;
	          
	/** Close the stream. */
	public void close() {
		out1.close();
		out2.close();
	}
	
	/** Flush the stream. */
	public void flush() {
		out1.flush();
		out2.flush();
	}
	
	/** Print a boolean value. */
	public void print(boolean b) {
		out1.print(b);
		out2.print(b);
	}
	
	public void print(char c) {
		out1.print(c);
		out2.print(c);
	}
	
	public void print(char[] s) {
		out1.print(s);
		out2.print(s);
	}
	
	public void print(double d) {
		out1.print(d);
		out2.print(d);
	}
	
	public void print(float f) {
		out1.print(f);
		out2.print(f);
	}
    
	public void print(int i) {
		out1.print(i);
		out2.print(i);
	}		
	
	public void print(long l) {
		out1.print(l);
		out2.print(l);
	}
	
	
	public void  print(Object obj) {
		out1.print(obj);
		out2.print(obj);
	}
	
	public void  print(String s) {
		out1.print(s);
		out2.print(s);
	}
	
	public void  println() {
		out1.println();
		out2.println();
	}	
	
	/** println a boolean value. */
	public void println(boolean b) {
		out1.println(b);
		out2.println(b);
	}
	
	public void println(char c) {
		out1.println(c);
		out2.println(c);
	}
	
	public void println(char[] s) {
		out1.println(s);
		out2.println(s);
	}
	
	public void println(double d) {
		out1.println(d);
		out2.println(d);
	}
	
	public void  println(float f) {
		out1.println(f);
		out2.println(f);
	}
    
	public void println(int i) {
		out1.println(i);
		out2.println(i);
	}		
	
	public void println(long l) {
		out1.println(l);
		out2.println(l);
	}
	
	
	public void  println(Object obj) {
		out1.println(obj);
		out2.println(obj);
	}
	
	protected void setError() {
		errorOccured = true;
/*		out1.setError();
		out2.setError();*/
	}
	
	public void  println(String s) {
		out1.println(s);
		out2.println(s);
		
		
//		System.err.println( " ***********   PRINTING: " + s);
	}
	
	public void  write(byte[] buf, int off, int len) {
		out1.write(buf, off, len);
		out2.write(buf, off, len);
	}
	public void  write(int b) {
		out1.write(b);
		out2.write(b);
	}
	
}