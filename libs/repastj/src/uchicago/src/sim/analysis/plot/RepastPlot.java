/* A signal plotter.

@Copyright (c) 1997-1999 The Regents of the University of California.
All rights reserved.

Permission is hereby granted, without written agreement and without
license or royalty fees, to use, copy, modify, and distribute this
software and its documentation for any purpose, provided that the
above copyright notice and the following two paragraphs appear in all
copies of this software.

IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY
FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.

THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
ENHANCEMENTS, OR MODIFICATIONS.

                                                PT_COPYRIGHT_VERSION_2
                                                COPYRIGHTENDKEY
@ProposedRating Yellow (cxh@eecs.berkeley.edu)
@AcceptedRating Yellow (cxh@eecs.berkeley.edu)
*/
package uchicago.src.sim.analysis.plot;


import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Vector;

import ptolemy.plot.EPSGraphics;
import ptolemy.plot.PlotPoint;

/**
 * A flexible signal plotter.  The plot can be configured and data can
 * be provided either through a file with commands or through direct
 * invocation of the public methods of the class.
 * <p>
 * When calling the public methods, in most cases the changes will not
 * be visible until paint() is called.  To request that this
 * be done, call repaint().  One exception is addPoint(), which
 * makes the new point visible immediately if the plot is visible on
 * the screen.<p>
 *
 * BELOW ADDED BY NICK COLLIER
 *
 * This is almost entirely a cut and paste from Ptolemy's Plot class.
 * This approach was necessary as the scope of some methods needed to
 * be changed. See the Ptolemy docs for more information.
 * @author Edward A. Lee, Christopher Hylands with some small additions
 * by Nick Collier
 * @version $Revision: 1.7 $ $Date: 2004/11/03 19:51:02 $
 */
public class RepastPlot extends RepastPlotBox {

  public RepastPlot(ZoomListener listener) {
    super(listener);
  }

    /** Add a legend (displayed at the upper right) for the specified
     *  data set with the specified string.  Short strings generally
     *  fit better than long strings.
     *  @param dataset The dataset index.
     *  @param legend The label for the dataset.
     */
    public void addLegend(int dataset, String legend) {
        _checkDatasetIndex(dataset);
        super.addLegend(dataset, legend);
    }

  public void addLegend(int dataset, String legend, Color color) {
    _checkDatasetIndex(dataset);
    super.addLegend(dataset, legend, color);
  }

  public void addLegend(int dataset, String legend, int markStyle) {
    _checkDatasetIndex(dataset);
    super.addLegend(dataset, legend, markStyle);
  }

  public void addLegend(int dataset, String legend, Color color,
                        int markStyle)
  {
    _checkDatasetIndex(dataset);
    super.addLegend(dataset, legend, color, markStyle);
  }

    /** In the specified data set, add the specified x, y point to the
     *  plot.  Data set indices begin with zero.  If the data set
     *  does not exist, create it.  The fourth argument indicates
     *  whether the point should be connected by a line to the previous
     *  point.  Regardless of the value of this argument, a line will not
     *  drawn if either there has been no previous point for this dataset
     *  or setConnected() has been called with a false argument.
     *  The new point will be made visible if the plot is visible
     *  on the screen.  Otherwise, it will be drawn the next time the plot
     *  is drawn on the screen.
     *  @param dataset The data set index.
     *  @param x The X position of the new point.
     *  @param y The Y position of the new point.
     *  @param connected If true, a line is drawn to connect to the previous
     *   point.
     */
    public synchronized void addPoint(int dataset, double x, double y,
            boolean connected) {
        if (_xlog) {
            if (x <= 0.0) {
                System.err.println("Can't plot non-positive X values "+
                        "when the logarithmic X axis value is specified: " +
                        x);
                return;
            }
            x = Math.log(x)*_LOG10SCALE;
        }
        if (_ylog) {
            if (y <= 0.0) {
                System.err.println("Can't plot non-positive Y values "+
                        "when the logarithmic Y axis value is specified: " +
                        y);
                return;
            }
            y = Math.log(y)*_LOG10SCALE;
        }
        // This point is not an error bar so we set yLowEB
        // and yHighEB to 0
        _addPoint(dataset, x, y, 0, 0, connected, false);
    }

    /** In the specified data set, add the specified x, y point to the
     *  plot with error bars.  Data set indices begin with zero.  If
     *  the dataset does not exist, create it.  yLowEB and
     *  yHighEB are the lower and upper error bars.  The sixth argument
     *  indicates whether the point should be connected by a line to
     *  the previous point.
     *  The new point will be made visible if the plot is visible
     *  on the screen.  Otherwise, it will be drawn the next time the plot
     *  is drawn on the screen.
     *  This method is based on a suggestion by
     *  Michael Altmann <michael@email.labmed.umn.edu>.
     *
     *  @param dataset The data set index.
     *  @param x The X position of the new point.
     *  @param y The Y position of the new point.
     *  @param yLowEB The low point of the error bar.
     *  @param yHighEB The high point of the error bar.
     *  @param connected If true, a line is drawn to connect to the previous
     *   point.
     */
    public synchronized void addPointWithErrorBars(int dataset,
            double x, double y, double yLowEB, double yHighEB,
            boolean connected) {
        if (_xlog) {
            if (x <= 0.0) {
                System.err.println("Can't plot non-positive X values "+
                        "when the logarithmic X axis value is specified: " +
                        x);
                return;
            }
            x = Math.log(x)*_LOG10SCALE;
        }
        if (_ylog) {
            if (y <= 0.0 || yLowEB <= 0.0 || yHighEB <= 0.0) {
                System.err.println("Can't plot non-positive Y values "+
                        "when the logarithmic Y axis value is specified: " +
                        y);
                return;
            }
            y = Math.log(y)*_LOG10SCALE;
            yLowEB = Math.log(yLowEB)*_LOG10SCALE;
            yHighEB = Math.log(yHighEB)*_LOG10SCALE;
        }
        _addPoint(dataset, x, y,
                yLowEB, yHighEB, connected, true);
    }

    /** Clear the plot of all data points.  If the argument is true, then
     *  reset all parameters to their initial conditions, including
     *  the persistence, plotting format, and axes formats.
     *  For the change to take effect, you must call repaint().
     *  @param format If true, clear the format controls as well.
     */
    public synchronized void clear(boolean format) {
        super.clear(format);
        _currentdataset = -1;
        //int size = _points.size();
        _points = new Vector();
        _prevx = new Vector();
        _prevy = new Vector();
        _painted = false;
        _maxdataset = -1;
        _firstinset = true;
        _sawfirstdataset = false;
        _xyInvalid = false;
        //_filename = null;
        _showing = false;

        if (format) {
            // Reset format controls
            _formats = new Vector();
            _marks = 0;
            _pointsPersistence = 0;
            //_sweepsPersistence = 0;
            _bars = false;
            _barwidth = 0.5;
            _baroffset = 0.05;
            _connected = true;
            _impulses = false;
            _reusedatasets = false;
        }
    }

    /** Clear the plot of data points in the specified dataset.
     *  This calls repaint() to request an update of the display.
     *  @param dataset The dataset to clear.
     */
    public synchronized void clear(int dataset) {
        _checkDatasetIndex(dataset);
        Vector points = (Vector)_points.elementAt(dataset);

        points.clear();
        repaint();
    }

    /** Erase the point at the given index in the given dataset.  If
     *  lines are being drawn, also erase the line to the next points
     *  (note: not to the previous point).  The point is not checked to
     *  see whether it is in range, so care must be taken by the caller
     *  to ensure that it is.
     *  The change will be made visible if the plot is visible
     *  on the screen.  Otherwise, it will take effect the next time the plot
     *  is drawn on the screen.
     *
     *  @param dataset The data set index.
     *  @param index The index of the point to erase.
     */
    public synchronized void erasePoint(int dataset, int index) {
        _checkDatasetIndex(dataset);
        if (isShowing()) {
            _erasePoint(getGraphics(), dataset, index);
        }
        Vector points = (Vector)_points.elementAt(dataset);
        if (points != null) {
            // If this point is at the maximum or minimum x or y boundary,
            // then flag that boundary needs to be recalculated next time
            // fillPlot() is called.
            PlotPoint pt = (PlotPoint)points.elementAt(index);
            if (pt != null) {
                if (pt.x == _xBottom || pt.x == _xTop ||
                        pt.y == _yBottom || pt.y == _yTop) {
                    _xyInvalid = true;
                }

                points.removeElementAt(index);
            }
        }
    }

    /** Rescale so that the data that is currently plotted just fits.
     *  This overrides the base class method to ensure that the protected
     *  variables _xBottom, _xTop, _yBottom, and _yTop are valid.
     *  This method calls repaint(), which eventually causes the display
     *  to be updated.
     */
    public synchronized void fillPlot() {
        if (_xyInvalid) {
            // Recalculate the boundaries based on currently visible data
            _xBottom = Double.MAX_VALUE;
            _xTop = - Double.MIN_VALUE;
            _yBottom = Double.MAX_VALUE;
            _yTop = - Double.MIN_VALUE;
            for (int dataset = 0; dataset < _points.size(); dataset++) {
                Vector points = (Vector)_points.elementAt(dataset);
                for (int index = 0; index < points.size(); index++) {
                    PlotPoint pt = (PlotPoint)points.elementAt(index);
                    if (pt.x < _xBottom) _xBottom = pt.x;
                    if (pt.x > _xTop) _xTop = pt.x;
                    if (pt.y < _yBottom) _yBottom = pt.y;
                    if (pt.y > _yTop) _yTop = pt.y;
                }
            }

            // Gulya:
            // If this is a bar graph, then make sure that all the bars for the
            // maximum value are included.
            if (_bars) {
              _xTop += (_points.size()-2) * _baroffset + _barwidth;
            }
        }
        _xyInvalid = false;
        // If this is a bar graph, then make sure the Y range includes 0
        if (_bars) {
            if (_yBottom > 0.0) _yBottom = 0.0;
            if (_yTop < 0.0) _yTop = 0.0;
        }
        super.fillPlot();
    }

    /** Return whether the default is to connect
     *  subsequent points with a line.  If the result is false, then
     *  points are not connected.  When points are by default
     *  connected, individual points can be not connected by giving the
     *  appropriate argument to addPoint().  Also, a different default
     *  can be set for each dataset, overriding this global default.
     */
    public boolean getConnected() {
        return _connected;
    }

    /** Return whether a line will be drawn from any
     *  plotted point down to the x axis.
     *  A plot with such lines is also known as a stem plot.
     *  @param on If true, draw a stem plot.
     */
    public boolean getImpulses() {
        return _impulses;
    }

    /** Get the marks style, which is one of
     *  "none", "points", "dots", or "various".
     *  @return A string specifying the style for points.
     */
    public String getMarksStyle() {
        // NOTE: If the number of marks increases, we will need to do
        // something better here...
        if (_marks == 0) {
            return "none";
        } else if (_marks == 1) {
            return "points";
        } else if (_marks == 2) {
            return "dots";
        } else {
            return "various";
        }
    }

    /** Return the maximum number of data sets.
     *  This method is deprecated, since there is no longer an upper bound.
     *  @deprecated
     */
    public int getMaxDataSets() {
        return Integer.MAX_VALUE;
    }

    /** Return the actual number of data sets.
     *  @return The number of data sets that have been created.
     */
    public int getNumDataSets() {
        return _points.size();
    }

    /** Override the base class to indicate that a new data set is being read.
     *  This method is deprecated.  Use read() instead (to read the old
     *  file format) or one of the classes in the plotml package to read
     *  the new (XML) file format.
     *  @deprecated
     */
    public void parseFile(String filespec, URL documentBase) {
        _firstinset = true;
        _sawfirstdataset = false;
        super.parseFile(filespec, documentBase);
    }

    /** Read a file with the old syntax (non-XML).
     *  Override the base class to register that we are reading a new
     *  data set.
     *  @param inputstream The input stream.
     *  @exception IOException If the stream cannot be read.
     */
    public void read(InputStream in) throws IOException {
        super.read(in);
        _firstinset = true;
        _sawfirstdataset = false;
    }

    /** Create a sample plot.
     */
    public void samplePlot() {
        // Create a sample plot.
        this.clear(true);

        this.setTitle("Sample plot");
        this.setYRange(-4, 4);
        this.setXRange(0, 100);
        this.setXLabel("time");
        this.setYLabel("value");
        this.addYTick("-PI", -Math.PI);
        this.addYTick("-PI/2", -Math.PI/2);
        this.addYTick("0", 0);
        this.addYTick("PI/2", Math.PI/2);
        this.addYTick("PI", Math.PI);
        this.setMarksStyle("none");
        this.setImpulses(true);

        boolean first = true;
        for (int i = 0; i <= 100; i++) {
            this.addPoint(0, (double)i,
                    5 * Math.cos(Math.PI * i/20), !first);
            this.addPoint(1, (double)i,
                    4.5 * Math.cos(Math.PI * i/25), !first);
            this.addPoint(2, (double)i,
                    4 * Math.cos(Math.PI * i/30), !first);
            this.addPoint(3, (double)i,
                    3.5* Math.cos(Math.PI * i/35), !first);
            this.addPoint(4, (double)i,
                    3 * Math.cos(Math.PI * i/40), !first);
            this.addPoint(5, (double)i,
                    2.5 * Math.cos(Math.PI * i/45), !first);
            this.addPoint(6, (double)i,
                    2 * Math.cos(Math.PI * i/50), !first);
            this.addPoint(7, (double)i,
                    1.5 * Math.cos(Math.PI * i/55), !first);
            this.addPoint(8, (double)i,
                    1 * Math.cos(Math.PI * i/60), !first);
            this.addPoint(9, (double)i,
                    0.5 * Math.cos(Math.PI * i/65), !first);
            first = false;
        }
        this.repaint();
    }

    /** Turn bars on or off (for bar charts).
     *  @param on If true, turn bars on.
     */
    public void setBars(boolean on) {
        _bars = on;
    }

    /** Turn bars on and set the width and offset.  Both are specified
     *  in units of the x axis.  The offset is the amount by which the
     *  i < sup>th</sup> data set is shifted to the right, so that it
     *  peeks out from behind the earlier data sets.
     *  @param width The width of the bars.
     *  @param offset The offset per data set.
     */
    public void setBars(double width, double offset) {
        _barwidth = width;
        _baroffset = offset;
        _bars = true;
    }

    /** If the argument is true, then the default is to connect
     *  subsequent points with a line.  If the argument is false, then
     *  points are not connected.  When points are by default
     *  connected, individual points can be not connected by giving the
     *  appropriate argument to addPoint().  Also, a different default
     *  can be set for each dataset, overriding this global default.
     */
    public void setConnected(boolean on) {
        _connected = on;
    }

    /** If the first argument is true, then by default for the specified
     *  dataset, points will be connected by a line.  Otherwise, the
     *  points will not be connected. When points are by default
     *  connected, individual points can be not connected by giving the
     *  appropriate argument to addPoint().
     *  @param on If true, draw lines between points.
     *  @param dataset The dataset to which this should apply.
     */
    public void setConnected(boolean on, int dataset) {
        _checkDatasetIndex(dataset);
        Format fmt = (Format)_formats.elementAt(dataset);
        fmt.connected = on;
        fmt.connectedUseDefault = false;
    }

    /** If the argument is true, then a line will be drawn from any
     *  plotted point down to the x axis.  Otherwise, this feature is
     *  disabled.  A plot with such lines is also known as a stem plot.
     *  @param on If true, draw a stem plot.
     */
    public void setImpulses(boolean on) {
        _impulses = on;
    }

    /** If the first argument is true, then a line will be drawn from any
     *  plotted point in the specified dataset down to the x axis.
     *  Otherwise, this feature is
     *  disabled.  A plot with such lines is also known as a stem plot.
     *  @param on If true, draw a stem plot.
     *  @param dataset The dataset to which this should apply.
     */
    public void setImpulses(boolean on, int dataset) {
        _checkDatasetIndex(dataset);
        Format fmt = (Format)_formats.elementAt(dataset);
        fmt.impulses = on;
        fmt.impulsesUseDefault = false;
    }

    /** Set the marks style to "none", "points", "dots", or "various".
     *  In the last case, unique marks are used for the first ten data
     *  sets, then recycled.
     *  @param style A string specifying the style for points.
     */
    public void setMarksStyle(String style) {
        if (style.equalsIgnoreCase("none")) {
            _marks = 0;
        } else if (style.equalsIgnoreCase("points")) {
            _marks = 1;
        } else if (style.equalsIgnoreCase("dots")) {
            _marks = 2;
        } else if (style.equalsIgnoreCase("various")) {
            _marks = 3;
        }
    }

    /** Set the marks style to "none", "points", "dots", or "various"
     *  for the specified dataset.
     *  In the last case, unique marks are used for the first ten data
     *  sets, then recycled.
     *  @param style A string specifying the style for points.
     *  @param dataset The dataset to which this should apply.
     */
    public void setMarksStyle(String style, int dataset) {
        _checkDatasetIndex(dataset);
        Format fmt = (Format)_formats.elementAt(dataset);
        if (style.equalsIgnoreCase("none")) {
            fmt.marks = 0;
        } else if (style.equalsIgnoreCase("points")) {
            fmt.marks = 1;
        } else if (style.equalsIgnoreCase("dots")) {
            fmt.marks = 2;
        } else if (style.equalsIgnoreCase("various")) {
            fmt.marks = 3;
        }
        fmt.marksUseDefault = false;
    }

    /** Specify the number of data sets to be plotted together.
     *  This method is deprecated, since it is no longer necessary to
     *  specify the number of data sets ahead of time.
     *  It has the effect of clearing all previously plotted points.
     *  This method should be called before setPointsPersistence().
     *  This method throws IllegalArgumentException if the number is less
     *  than 1.  This is a runtime exception, so it need not be declared.
     *  @param numsets The number of data sets.
     *  @deprecated
     */
    public void setNumSets(int numsets) {
        if (numsets < 1) {
            throw new IllegalArgumentException("Number of data sets ("+
                    numsets + ") must be greater than 0.");

        }
        _currentdataset = -1;
        _points.removeAllElements();
        _formats.removeAllElements();
        _prevx.removeAllElements();
        _prevy.removeAllElements();
        for (int i = 0; i < numsets; i++) {
            _points.addElement(new Vector());
            _formats.addElement(new Format());
            _prevx.addElement(new Long(0));
            _prevy.addElement(new Long(0));
        }
    }

    /** Calling this method with a positive argument sets the
     *  persistence of the plot to the given number of points.  Calling
     *  with a zero argument turns off this feature, reverting to
     *  infinite memory (unless sweeps persistence is set).  If both
     *  sweeps and points persistence are set then sweeps take
     *  precedence.
     */
    public void setPointsPersistence(int persistence) {
        //   FIXME: No file format yet.
        _pointsPersistence = persistence;
    }

    /** If the argument is true, then datasets with the same name
     *  are merged into a single dataset.
     *  @param on If true, then merge datasets.
     */
    public void setReuseDatasets(boolean on) {
        _reusedatasets = on;
    }

    /** A sweep is a sequence of points where the value of X is
     *  increasing.  A point that is added with a smaller x than the
     *  previous point increments the sweep count.  Calling this method
     *  with a non-zero argument sets the persistence of the plot to
     *  the given number of sweeps.  Calling with a zero argument turns
     *  off this feature.  If both sweeps and points persistence are
     *  set then sweeps take precedence.
     *  <b> This feature is not implemented yet, so this method has no
     *  effect</b>.
     */
    public void setSweepsPersistence(int persistence) {
        //   * FIXME: No file format yet.
        //_sweepsPersistence = persistence;
    }

    ///////////////////////////////////////////////////////////////////
    ////                         protected methods                 ////


    /** Check the argument to ensure that it is a valid data set index.
     *  If it is less than zero, throw an IllegalArgumentException (which
     *  is a runtime exception).  If it does not refer to an existing
     *  data set, then fill out the _points Vector so that it does refer
     *  to an existing data set. All other dataset-related vectors are
     *  similarly filled out.
     *  @param dataset The data set index.
     */
    protected void _checkDatasetIndex(int dataset) {
        if (dataset < 0) {
            throw new IllegalArgumentException("Plot._checkDatasetIndex: Cannot"
                    + " give a negative number for the data set index.");
        }
        while (dataset >= _points.size()) {
            _points.addElement(new Vector());
            _formats.addElement(new Format());
            _prevx.addElement(new Long(0));
            _prevy.addElement(new Long(0));
        }
    }

    /** Draw bar from the specified point to the y axis.
     *  If the specified point is below the y axis or outside the
     *  x range, do nothing.  If the <i>clip</i> argument is true,
     *  then do not draw above the y range.
     *  Note that paint() should be called before
     *  calling this method so that _xscale and _yscale are properly set.
     *  @param graphics The graphics context.
     *  @param dataset The index of the dataset.
     *  @param xpos The x position.
     *  @param ypos The y position.
     *  @param clip If true, then do not draw outside the range.
     */
    protected void _drawBar(Graphics graphics, int dataset,
            long xpos, long ypos, boolean clip) {
        if (clip) {
            if (ypos < _uly) {
                ypos = _uly;
            } if (ypos > _lry) {
                ypos = _lry;
            }
        }
        if (ypos <= _lry && xpos <= _lrx && xpos >= _ulx) {
            // left x position of bar.
            int barlx = (int)(xpos - _barwidth * _xscale/2 +
                    dataset * _baroffset * _xscale);
            // right x position of bar
            int barrx = (int)(barlx + _barwidth * _xscale);
            if (barlx < _ulx) barlx = _ulx;
            if (barrx > _lrx) barrx = _lrx;
            // Make sure that a bar is always at least one pixel wide.
            if (barlx >= barrx) barrx = barlx+1;
            // The y position of the zero line.
            long zeroypos = _lry - (long) ((0-_yMin) * _yscale);
            if (_lry < zeroypos) zeroypos = _lry;
            if (_uly > zeroypos) zeroypos = _uly;

            if (_yMin >= 0 || ypos <= zeroypos) {
                graphics.fillRect(barlx, (int)ypos,
                        barrx - barlx, (int)(zeroypos - ypos));
            } else {
                graphics.fillRect(barlx, (int)zeroypos,
                        barrx - barlx, (int)(ypos - zeroypos));
            }
        }
    }

    /** Draw an error bar for the specified yLowEB and yHighEB values.
     *  If the specified point is below the y axis or outside the
     *  x range, do nothing.  If the <i>clip</i> argument is true,
     *  then do not draw above the y range.
     *  @param graphics The graphics context.
     *  @param dataset The index of the dataset.
     *  @param xpos The x position.
     *  @param yLowEBPos The lower y position of the error bar.
     *  @param yHighEBPos The upper y position of the error bar.
     *  @param clip If true, then do not draw above the range.
     */
    protected void _drawErrorBar(Graphics graphics, int dataset,
            long xpos, long yLowEBPos, long yHighEBPos,
            boolean clip) {
        _drawLine(graphics, dataset, xpos - _ERRORBAR_LEG_LENGTH, yHighEBPos,
                xpos + _ERRORBAR_LEG_LENGTH, yHighEBPos, clip);
        _drawLine(graphics, dataset, xpos, yLowEBPos, xpos, yHighEBPos, clip);
        _drawLine(graphics, dataset, xpos - _ERRORBAR_LEG_LENGTH, yLowEBPos,
                xpos + _ERRORBAR_LEG_LENGTH, yLowEBPos, clip);
    }

    /** Draw a line from the specified point to the y axis.
     *  If the specified point is below the y axis or outside the
     *  x range, do nothing.  If the <i>clip</i> argument is true,
     *  then do not draw above the y range.
     *  @param graphics The graphics context.
     *  @param xpos The x position.
     *  @param ypos The y position.
     *  @param clip If true, then do not draw outside the range.
     */
    protected void _drawImpulse(Graphics graphics,
            long xpos, long ypos, boolean clip) {
        if (clip) {
            if (ypos < _uly) {
                ypos = _uly;
            } if (ypos > _lry) {
                ypos = _lry;
            }
        }
        if (ypos <= _lry && xpos <= _lrx && xpos >= _ulx) {
            // The y position of the zero line.
            double zeroypos = _lry - (long) ((0-_yMin) * _yscale);
            if (_lry < zeroypos) zeroypos = _lry;
            if (_uly > zeroypos) zeroypos = _uly;
            graphics.drawLine((int)xpos, (int)ypos, (int)xpos,
                    (int)zeroypos);
        }
    }

    /** Draw a line from the specified starting point to the specified
     *  ending point.  The current color is used.  If the <i>clip</i> argument
     *  is true, then draw only that portion of the line that lies within the
     *  plotting rectangle.
     *  @param graphics The graphics context.
     *  @param dataset The index of the dataset.
     *  @param startx The starting x position.
     *  @param starty The starting y position.
     *  @param endx The ending x position.
     *  @param endy The ending y position.
     *  @param clip If true, then do not draw outside the range.
     */
    protected void _drawLine(Graphics graphics,
            int dataset, long startx, long starty, long endx, long endy,
            boolean clip) {

        if (clip) {
            // Rule out impossible cases.
            if (!((endx <= _ulx && startx <= _ulx) ||
                    (endx >= _lrx && startx >= _lrx) ||
                    (endy <= _uly && starty <= _uly) ||
                    (endy >= _lry && starty >= _lry))) {
                // If the end point is out of x range, adjust
                // end point to boundary.
                // The integer arithmetic has to be done with longs so as
                // to not loose precision on extremely close zooms.
                if (startx != endx) {
                    if (endx < _ulx) {
                        endy = (int)(endy + ((long)(starty - endy) *
                                (_ulx - endx))/(startx - endx));
                        endx = _ulx;
                    } else if (endx > _lrx) {
                        endy = (int)(endy + ((long)(starty - endy) *
                                (_lrx - endx))/(startx - endx));
                        endx = _lrx;
                    }
                }

                // If end point is out of y range, adjust to boundary.
                // Note that y increases downward
                if (starty != endy) {
                    if (endy < _uly) {
                        endx = (int)(endx + ((long)(startx - endx) *
                                (_uly - endy))/(starty - endy));
                        endy = _uly;
                    } else if (endy > _lry) {
                        endx = (int)(endx + ((long)(startx - endx) *
                                (_lry - endy))/(starty - endy));
                        endy = _lry;
                    }
                }

                // Adjust current point to lie on the boundary.
                if (startx != endx) {
                    if (startx < _ulx) {
                        starty = (int)(starty + ((long)(endy - starty) *
                                (_ulx - startx))/(endx - startx));
                        startx = _ulx;
                    } else if (startx > _lrx) {
                        starty = (int)(starty + ((long)(endy - starty) *
                                (_lrx - startx))/(endx - startx));
                        startx = _lrx;
                    }
                }
                if (starty != endy) {
                    if (starty < _uly) {
                        startx = (int)(startx + ((long)(endx - startx) *
                                (_uly - starty))/(endy - starty));
                        starty = _uly;
                    } else if (starty > _lry) {
                        startx = (int)(startx + ((long)(endx - startx) *
                                (_lry - starty))/(endy - starty));
                        starty = _lry;
                    }
                }
            }

            // Are the new points in range?
            if (endx >= _ulx && endx <= _lrx &&
                    endy >= _uly && endy <= _lry &&
                    startx >= _ulx && startx <= _lrx &&
                    starty >= _uly && starty <= _lry) {
                graphics.drawLine((int)startx, (int)starty,
                        (int)endx, (int)endy);
            }
        } else {
            // draw unconditionally.
            graphics.drawLine((int)startx, (int)starty,
                    (int)endx, (int)endy);
        }
    }

    /** Draw the axes and then plot all points.  This is synchronized
     *  to prevent multiple threads from drawing the plot at the same
     *  time.  It sets _painted true and calls notifyAll() at the end so that a
     *  thread can use <code>wait()</code> to prevent it plotting
     *  points before the axes have been first drawn.  If the second
     *  argument is true, clear the display first.
     *  This method is called by paint().  To cause it to be called you
     *  would normally call repaint(), which eventually causes paint() to
     *  be called.
     *  @param graphics The graphics context.
     *  @param clearfirst If true, clear the plot before proceeding.
     */
    protected synchronized void _drawPlot(Graphics graphics,
            boolean clearfirst) {
        // We must call PlotBox._drawPlot() before calling _drawPlotPoint
        // so that _xscale and _yscale are set.
        super._drawPlot(graphics, clearfirst);

        _showing = graphics != null;//true;

        // Plot the points in reverse order so that the first colors
        // appear on top.
        for (int dataset = _points.size() - 1; dataset >= 0 ; dataset--) {
            Vector data = (Vector)_points.elementAt(dataset);
            for (int pointnum = 0; pointnum < data.size(); pointnum++) {
                _drawPlotPoint(graphics, dataset, pointnum);
            }
        }
        _painted = true;
        notifyAll();
    }

    /** Put a mark corresponding to the specified dataset at the
     *  specified x and y position. The mark is drawn in the current
     *  color. What kind of mark is drawn depends on the _marks
     *  variable and the dataset argument. If the fourth argument is
     *  true, then check the range and plot only points that
     *  are in range.
     *  @param graphics The graphics context.
     *  @param dataset The index of the dataset.
     *  @param xpos The x position.
     *  @param ypos The y position.
     *  @param clip If true, then do not draw outside the range.
     */
    protected void _drawPoint(Graphics graphics,
            int dataset, long xpos, long ypos,
            boolean clip) {

        // If the point is not out of range, draw it.
        boolean pointinside = ypos <= _lry && ypos >= _uly &&
            xpos <= _lrx && xpos >= _ulx;
        if (!clip || pointinside) {
            int xposi = (int)xpos;
            int yposi = (int)ypos;

            // Check to see whether the dataset has a marks directive
            //Format fmt = (Format)_formats.elementAt(dataset);
            //int marks = _marks;
            //if (!fmt.marksUseDefault) marks = fmt.marks;
            int marks = dsMarkMap.get(dataset);

            // If the point is out of range, and being drawn, then it is
            // probably a legend point.  When printing in black and white,
            // we want to use a line rather than a point for the legend.
            // (So that line patterns are visible). The only exception is
            // when the marks style uses distinct marks, or if there is
            // no line being drawn.
            // NOTE: It is unfortunate to have to test the class of graphics,
            // but there is no easy way around this that I can think of.
            if (!pointinside && marks != 3 && _isConnected(dataset) &&
                    (graphics instanceof EPSGraphics)) {
                graphics.drawLine(xposi-6, yposi, xposi+6, yposi);
            } else {
                // Color display.  Use normal legend.

              /*
                switch (marks) {
                case 0:
                    // If no mark style is given, draw a filled rectangle.
                    // This is used, for example, to draw the legend.
                    graphics.fillRect(xposi-6, yposi-6, 6, 6);
                    break;
                case 1:
                    // points -- use 3-pixel ovals.
                    graphics.fillOval(xposi-1, yposi-1, 3, 3);
                    break;
                case 2:
                    // dots
                    graphics.fillOval(xposi-_radius, yposi-_radius,
                            _diameter, _diameter);
                    break;
                case 3:
                    // marks
                    int xpoints[], ypoints[];
                    // Points are only distinguished up to _MAX_MARKS data sets.
                    int mark = dataset % _MAX_MARKS;
              */
              int xpoints[], ypoints[];
                    switch (marks) {
                    case 0:
                        // filled circle
                        graphics.fillOval(xposi-_radius, yposi-_radius,
                                _diameter, _diameter);
                        break;
                    case 1:
                        // cross
                        graphics.drawLine(xposi-_radius, yposi-_radius,
                                xposi+_radius, yposi+_radius);
                        graphics.drawLine(xposi+_radius, yposi-_radius,
                                xposi-_radius, yposi+_radius);
                        break;
                    case 2:
                        // square
                        graphics.drawRect(xposi-_radius, yposi-_radius,
                                _diameter, _diameter);
                        break;
                    case 3:
                        // filled triangle
                        xpoints = new int[4];
                        ypoints = new int[4];
                        xpoints[0] = xposi; ypoints[0] = yposi-_radius;
                        xpoints[1] = xposi+_radius; ypoints[1] = yposi+_radius;
                        xpoints[2] = xposi-_radius; ypoints[2] = yposi+_radius;
                        xpoints[3] = xposi; ypoints[3] = yposi-_radius;
                        graphics.fillPolygon(xpoints, ypoints, 4);
                        break;
                    case 4:
                        // diamond
                        xpoints = new int[5];
                        ypoints = new int[5];
                        xpoints[0] = xposi; ypoints[0] = yposi-_radius;
                        xpoints[1] = xposi+_radius; ypoints[1] = yposi;
                        xpoints[2] = xposi; ypoints[2] = yposi+_radius;
                        xpoints[3] = xposi-_radius; ypoints[3] = yposi;
                        xpoints[4] = xposi; ypoints[4] = yposi-_radius;
                        graphics.drawPolygon(xpoints, ypoints, 5);
                        break;
                    case 5:
                        // circle
                        graphics.drawOval(xposi-_radius, yposi-_radius,
                                _diameter, _diameter);
                        break;
                    case 6:
                        // plus sign
                        graphics.drawLine(xposi, yposi-_radius, xposi,
                                yposi+_radius);
                        graphics.drawLine(xposi-_radius, yposi, xposi+_radius,
                                yposi);
                        break;
                    case 7:
                        // filled square
                        graphics.fillRect(xposi-_radius, yposi-_radius,
                                _diameter, _diameter);
                        break;
                    case 8:
                        // triangle
                        xpoints = new int[4];
                        ypoints = new int[4];
                        xpoints[0] = xposi; ypoints[0] = yposi-_radius;
                        xpoints[1] = xposi+_radius; ypoints[1] = yposi+_radius;
                        xpoints[2] = xposi-_radius; ypoints[2] = yposi+_radius;
                        xpoints[3] = xposi; ypoints[3] = yposi-_radius;
                        graphics.drawPolygon(xpoints, ypoints, 4);
                        break;
                    case 9:
                        // filled diamond
                        xpoints = new int[5];
                        ypoints = new int[5];
                        xpoints[0] = xposi; ypoints[0] = yposi-_radius;
                        xpoints[1] = xposi+_radius; ypoints[1] = yposi;
                        xpoints[2] = xposi; ypoints[2] = yposi+_radius;
                        xpoints[3] = xposi-_radius; ypoints[3] = yposi;
                        xpoints[4] = xposi; ypoints[4] = yposi-_radius;
                        graphics.fillPolygon(xpoints, ypoints, 5);
                        break;
                    }
                    //break;
                    //default:
                    // none
            }
        }
    }

    /** Parse a line that gives plotting information. Return true if
     *  the line is recognized.  Lines with syntax errors are ignored.
     *  @param line A command line.
     *  @return True if the line is recognized.
     */
    protected boolean _parseLine(String line) {
        boolean connected = false;
        if (_isConnected(_currentdataset)) {
            connected = true;
        }
        // parse only if the super class does not recognize the line.
        if (super._parseLine(line)) {
            return true;
        } else {
            // We convert the line to lower case so that the command
            // names are case insensitive
            String lcLine = new String(line.toLowerCase());
            if (lcLine.startsWith("marks:")) {
                // If we have seen a dataset directive, then apply the
                // request to the current dataset only.
                String style = (line.substring(6)).trim();
                if (_sawfirstdataset) {
                    setMarksStyle(style, _currentdataset);
                } else {
                    setMarksStyle(style);
                }
                return true;
            } else if (lcLine.startsWith("numsets:")) {
                // Ignore.  No longer relevant.
                return true;
            } else if (lcLine.startsWith("reusedatasets:")) {
                if (lcLine.indexOf("off", 16) >= 0) {
                    setReuseDatasets(false);
                } else {
                    setReuseDatasets(true);
                }
                return true;
            } else if (lcLine.startsWith("dataset:")) {
                if (_reusedatasets && lcLine.length() > 0) {
                    String tlegend = (line.substring(8)).trim();
                    _currentdataset = -1;
                    int i;
                    for ( i = 0; i <= _maxdataset; i++) {
                        if (getLegend(i).compareTo(tlegend) == 0) {
                            _currentdataset = i;
                        }
                    }
                    if (_currentdataset != -1) {
                        return true;
                    } else {
                        _currentdataset = _maxdataset;
                    }
                }

                // new data set
                _firstinset = true;
                _sawfirstdataset = true;
                _currentdataset++;
                if (lcLine.length() > 0) {
                    String legend = (line.substring(8)).trim();
                    if (legend != null && legend.length() > 0) {
                        addLegend(_currentdataset, legend);
                    }
                }
                _maxdataset = _currentdataset;
                return true;
            } else if (lcLine.startsWith("lines:")) {
                if (lcLine.indexOf("off", 6) >= 0) {
                    setConnected(false);
                } else {
                    setConnected(true);
                }
                return true;
            } else if (lcLine.startsWith("impulses:")) {
                // If we have not yet seen a dataset, then this is interpreted
                // as the global default.  Otherwise, it is assumed to apply
                // only to the current dataset.
                if (_sawfirstdataset) {
                    if (lcLine.indexOf("off", 9) >= 0) {
                        setImpulses(false, _currentdataset);
                    } else {
                        setImpulses(true, _currentdataset);
                    }
                } else {
                    if (lcLine.indexOf("off", 9) >= 0) {
                        setImpulses(false);
                    } else {
                        setImpulses(true);
                    }
                }
                return true;
            } else if (lcLine.startsWith("bars:")) {
                if (lcLine.indexOf("off", 5) >= 0) {
                    setBars(false);
                } else {
                    setBars(true);
                    int comma = line.indexOf(",", 5);
                    String barwidth;
                    String baroffset = null;
                    if (comma > 0) {
                        barwidth = (line.substring(5, comma)).trim();
                        baroffset = (line.substring(comma+1)).trim();
                    } else {
                        barwidth = (line.substring(5)).trim();
                    }
                    try {
                        Double bwidth = new Double(barwidth);
                        double boffset = _baroffset;
                        if (baroffset != null) {
                            boffset = (new Double(baroffset)).
                                doubleValue();
                        }
                        setBars(bwidth.doubleValue(), boffset);
                    } catch (NumberFormatException e) {
                        // ignore if format is bogus.
                    }
                }
                return true;
            } else if (line.startsWith("move:")) {
                // a disconnected point
                connected = false;
                // deal with 'move: 1 2' and 'move:2 2'
                line = line.substring(5, line.length()).trim();
            } else if (line.startsWith("move")) {
                // a disconnected point
                connected = false;
                // deal with 'move 1 2' and 'move2 2'
                line = line.substring(4, line.length()).trim();
            } else if (line.startsWith("draw:")) {
                // a connected point, if connect is enabled.
                line = line.substring(5, line.length()).trim();
            } else if (line.startsWith("draw")) {
                // a connected point, if connect is enabled.
                line = line.substring(4, line.length()).trim();
            }
            line = line.trim();

            // We can't use StreamTokenizer here because it can't
            // process numbers like 1E-01.
            // This code is somewhat optimized for speed, since
            // most data consists of two data points, we want
            // to handle that case as efficiently as possible.

            int fieldsplit = line.indexOf(",");
            if (fieldsplit == -1) {
                fieldsplit = line.indexOf(" ");
            }
            if (fieldsplit == -1) {
                fieldsplit = line.indexOf("\t");  // a tab
            }

            if (fieldsplit > 0) {
                String x = (line.substring(0, fieldsplit)).trim();
                String y = (line.substring(fieldsplit+1)).trim();
                // Any more separators?
                int fieldsplit2 = y.indexOf(",");
                if (fieldsplit2 == -1) {
                    fieldsplit2 = y.indexOf(" ");
                }
                if (fieldsplit2 == -1) {
                    fieldsplit2 = y.indexOf("\t");  // a tab
                }
                if (fieldsplit2 > 0) {
                    line = (y.substring(fieldsplit2+1)).trim();
                    y = (y.substring(0, fieldsplit2)).trim();
                }
                try {
                    Double xpt = new Double(x);
                    Double ypt = new Double(y);
                    if (fieldsplit2 > 0) {
                        // There was one separator after the y value, now
                        // look for another separator.
                        int fieldsplit3 = line.indexOf(",");
                        if (fieldsplit3 == -1) {
                            fieldsplit3 = line.indexOf(" ");
                        }
                        if (fieldsplit3 == -1) {
                            fieldsplit2 = line.indexOf("\t");  // a tab
                        }

                        if (fieldsplit3 > 0) {
                            // We have more numbers, assume that this is
                            // an error bar
                            String yl = (line.substring(0,
                                    fieldsplit3)).trim();
                            String yh = (line.substring(fieldsplit3+1)).trim();
                            Double yLowEB = new Double(yl);
                            Double yHighEB = new Double(yh);
                            connected = _addLegendIfNecessary(connected);
                            addPointWithErrorBars(_currentdataset,
                                    xpt.doubleValue(),
                                    ypt.doubleValue(),
                                    yLowEB.doubleValue(),
                                    yHighEB.doubleValue(),
                                    connected);
                            return true;
                        } else {
                            // It is unlikely that we have a fieldsplit2 >0
                            // but not fieldsplit3 >0, but just in case:

                            connected = _addLegendIfNecessary(connected);
                            addPoint(_currentdataset, xpt.doubleValue(),
                                    ypt.doubleValue(), connected);
                            return true;
                        }
                    } else {
                        // There were no more fields, so this is
                        // a regular pt.
                        connected = _addLegendIfNecessary(connected);
                        addPoint(_currentdataset, xpt.doubleValue(),
                                ypt.doubleValue(), connected);
                        return true;
                    }
                } catch (NumberFormatException e) {
                    // ignore if format is bogus.
                }
            }
        }
        return false;
    }

    /** Write plot information to the specified output stream in
     *  PlotML, an XML extension.
     *  Derived classes should override this method to first call
     *  the parent class method, then add whatever additional information
     *  they wish to add to the stream.
     *  @param output A buffered print writer.
     */
    protected void _write(PrintWriter output) {
        super._write(output);

        if (_reusedatasets) output.println("<reuseDatasets/>");

        StringBuffer defaults = new StringBuffer();

        if (!_connected) defaults.append(" connected=\"no\"");

        switch(_marks) {
        case 1:
            defaults.append(" marks=\"points\"");
            break;
        case 2:
            defaults.append(" marks=\"dots\"");
            break;
        case 3:
            defaults.append(" marks=\"various\"");
            break;
        }

        // Write the defaults for formats that can be controlled by dataset
        if (_impulses) defaults.append(" stems=\"yes\"");

        if (defaults.length() > 0) {
            output.println("<default" + defaults.toString() + "/>");
        }

        if (_bars) output.println(
                "<barGraph width=\"" + _barwidth
                + "\" offset=\"" + _baroffset + "\"/>");

        for (int dataset = 0; dataset < _points.size(); dataset++) {

            StringBuffer options = new StringBuffer();

            Format fmt = (Format)_formats.elementAt(dataset);

            if (!fmt.connectedUseDefault) {
                if (_isConnected(dataset)) {
                    options.append(" connected=\"yes\"");
                } else {
                    options.append(" connected=\"no\"");
                }
            }

            if (!fmt.impulsesUseDefault) {
                if (fmt.impulses) options.append(" stems=\"yes\"");
                else output.println(" stems=\"no\"");
            }

            if (!fmt.marksUseDefault) {
                switch(fmt.marks) {
                case 0:
                    options.append(" marks=\"none\"");
                case 1:
                    options.append(" marks=\"points\"");
                case 2:
                    options.append(" marks=\"dots\"");
                case 3:
                    options.append(" marks=\"various\"");
                }
            }

            String legend = getLegend(dataset);
            if (legend != null) {
                options.append(" name=\"" + getLegend(dataset) + "\"");
            }

            output.println("<dataset" + options.toString() + ">");

            // Write the data
            Vector pts = (Vector)_points.elementAt(dataset);
            for (int pointnum = 0; pointnum < pts.size(); pointnum++) {
                PlotPoint pt = (PlotPoint)pts.elementAt(pointnum);
                if (!pt.connected) {
                    output.print("<m ");
                } else {
                    output.print("<p ");
                }
                output.print("x=\"" + pt.x + "\" y=\"" + pt.y + "\"");
                if (pt.errorBar) {
                    output.print(" lowErrorBar=\"" + pt.yLowEB
                            + "\" highErrorBar=\"" + pt.yHighEB + "\"");
                }
                output.println("/>");
            }

            output.println("</dataset>");
        }
    }

    /** Write plot information to the specified output stream in
     *  the "old syntax," which predates PlotML.
     *  Derived classes should override this method to first call
     *  the parent class method, then add whatever additional information
     *  they wish to add to the stream.
     *  @param output A buffered print writer.
     *  @deprecated
     */
    protected void _writeOldSyntax(PrintWriter output) {
        super._write(output);

        // NOTE: NumSets is obsolete, so we don't write it.

        if (_reusedatasets) output.println("ReuseDatasets: on");
        if (!_connected) output.println("Lines: off");
        if (_bars) output.println("Bars: " + _barwidth + ", " + _baroffset);

        // Write the defaults for formats that can be controlled by dataset
        if (_impulses) output.println("Impulses: on");
        switch(_marks) {
        case 1:
            output.println("Marks: points");
        case 2:
            output.println("Marks: dots");
        case 3:
            output.println("Marks: various");
        }

        for (int dataset = 0; dataset < _points.size(); dataset++) {
            // Write the dataset directive
            String legend = getLegend(dataset);
            if (legend != null) {
                output.println("DataSet: " + getLegend(dataset));
            } else {
                output.println("DataSet:");
            }
            // Write dataset-specific format information
            Format fmt = (Format)_formats.elementAt(dataset);
            if (!fmt.impulsesUseDefault) {
                if (fmt.impulses) output.println("Impulses: on");
                else output.println("Impulses: off");
            }
            if (!fmt.marksUseDefault) {
                switch(fmt.marks) {
                case 0:
                    output.println("Marks: none");
                case 1:
                    output.println("Marks: points");
                case 2:
                    output.println("Marks: dots");
                case 3:
                    output.println("Marks: various");
                }
            }
            // Write the data
            Vector pts = (Vector)_points.elementAt(dataset);
            for (int pointnum = 0; pointnum < pts.size(); pointnum++) {
                PlotPoint pt = (PlotPoint)pts.elementAt(pointnum);
                if (!pt.connected) output.print("move: ");
                if (pt.errorBar) {
                    output.println(pt.x + ", " + pt.y + ", "
                            + pt.yLowEB + ", " + pt.yHighEB);
                } else {
                    output.println(pt.x + ", " + pt.y);
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////
    ////                         protected variables               ////

    /** @serial The current dataset. */
    protected int _currentdataset = -1;

    /** @serial A vector of datasets. */
    protected Vector _points = new Vector();

    /** @serial An indicator of the marks style.  See _parseLine method for
     * interpretation.
     */
    protected int _marks;

    /** @serial Indicate that painting is complete. */
    protected boolean _painted = false;

    ///////////////////////////////////////////////////////////////////
    ////                         private methods                   ////

    /* Add a legend if necessary, return the value of the connected flag.
     */
    private boolean _addLegendIfNecessary(boolean connected) {
        if ((! _sawfirstdataset  || _currentdataset < 0) &&
                ! _reusedatasets) {
            // We did not set a DataSet line, but
            // we did get called with -<digit> args and
            // we did not see reusedatasets: yes
            _sawfirstdataset = true;
            _currentdataset++;
        }
        if (! _sawfirstdataset && getLegend(_currentdataset) == null) {
            // We did not see a "DataSet" string yet,
            // nor did we call addLegend().
            _firstinset = true;
            _sawfirstdataset = true;
            addLegend(_currentdataset,
                    new String("Set "+ _currentdataset));
        }
        if (_firstinset && ! _reusedatasets) {
            connected = false;
            _firstinset = false;
        }
        return connected;
    }

    /* In the specified data set, add the specified x, y point to the
     * plot.  Data set indices begin with zero.  If the dataset
     * argument is less than zero, throw an IllegalArgumentException
     * (a runtime exception).  If it refers to a data set that does
     * not exist, create the data set.  The fourth argument indicates
     * whether the point should be connected by a line to the previous
     * point.  However, this argument is ignored if setConnected() has
     * been called with a false argument.  In that case, a point is never
     * connected to the previous point.  That argument is also ignored
     * if the point is the first in the specified dataset.
     * The point is drawn on the screen only if is visible.
     * Otherwise, it is drawn the next time paint() is called.
     */
    private synchronized void _addPoint(
            int dataset, double x, double y, double yLowEB, double yHighEB,
            boolean connected, boolean errorBar) {
        _checkDatasetIndex(dataset);

        if(_wrap && _xRangeGiven) {
            double width = _xhighgiven - _xlowgiven;
            if (x < _xlowgiven) {
                x += width*Math.floor(1.0 + (_xlowgiven-x)/width);
            } else if (x > _xhighgiven) {
                x -= width*Math.floor(1.0 + (x-_xhighgiven)/width);
                // NOTE: Could quantization errors be a problem here?
                if (x == _xlowgiven) x = _xhighgiven;
            }
        }

        // For auto-ranging, keep track of min and max.
        if (x < _xBottom) _xBottom = x;
        if (x > _xTop) {
          _xTop = x;
          // Gulya:
          // If this is a bar graph, make sure all bars for the max value are
          // included.
          if (_bars) {
            _xTop += (_points.size()-2) * _baroffset + _barwidth;
          }
        }
        if (y < _yBottom) _yBottom = y;
        if (y > _yTop) _yTop = y;

        // FIXME: Ignoring sweeps for now.
        PlotPoint pt = new PlotPoint();
        pt.x = x;
        pt.y = y;
        pt.connected = connected && _isConnected(dataset);

        if (errorBar) {
            if (yLowEB < _yBottom) _yBottom = yLowEB;
            if (yLowEB > _yTop) _yTop = yLowEB;
            if (yHighEB < _yBottom) _yBottom = yHighEB;
            if (yHighEB > _yTop) _yTop = yHighEB;
            pt.yLowEB = yLowEB;
            pt.yHighEB = yHighEB;
            pt.errorBar = true;
        }

        Vector pts = (Vector)_points.elementAt(dataset);
        // If this is the first point in the dataset, clear the connected bit.
        int size = pts.size();
        if (size == 0) {
            pt.connected = false;
        } else if(_wrap && _xRangeGiven) {
            // Do not connect points if wrapping...
            PlotPoint old = (PlotPoint)(pts.elementAt(size-1));
            if (old.x > x) pt.connected = false;
        }
        pts.addElement(pt);
        if (_pointsPersistence > 0) {
            if (size > _pointsPersistence)
                erasePoint(dataset, 0);
        }

        Graphics g = getGraphics();
        if (g != null) {
        // Draw the point on the screen only if the plot is showing.
        if (_showing && drawOnPlotPoint) {
            _drawPlotPoint(g, dataset, pts.size() - 1);
        }
        }

        if(_wrap && _xRangeGiven && x == _xhighgiven) {
            // Plot a second point at the low end of the range.
            _addPoint(dataset, _xlowgiven, y, yLowEB, yHighEB, false, errorBar);
        }
    }

    /* Draw the specified point and associated lines, if any.
     * Note that paint() should be called before
     * calling this method so that it calls _drawPlot(), which sets
     * _xscale and _yscale. Note that this does not check the dataset
     * index.  It is up to the caller to do that.
     */
    private synchronized void _drawPlotPoint(Graphics graphics,
            int dataset, int index) {
        // Set the color
        if (_pointsPersistence > 0) {
            // To allow erasing to work by just redrawing the points.
            graphics.setXORMode(_background);
        }
        if (_usecolor) {
          //int color = dataset % _colors.length;
          //graphics.setColor(_colors[color]);
          graphics.setColor((Color)dsColorMap.get(dataset));
        } else {
            graphics.setColor(_foreground);
        }

        Vector pts = (Vector)_points.elementAt(dataset);
        PlotPoint pt = (PlotPoint)pts.elementAt(index);
        // Use long here because these numbers can be quite large
        // (when we are zoomed out a lot).
        long ypos = _lry - (long)((pt.y - _yMin) * _yscale);
        long xpos = _ulx + (long)((pt.x - _xMin) * _xscale);

        // Draw the line to the previous point.
        long prevx = ((Long)_prevx.elementAt(dataset)).longValue();
        long prevy = ((Long)_prevy.elementAt(dataset)).longValue();
        // MIN_VALUE is a flag that there has been no previous x or y.
        if (pt.connected) {
            _drawLine(graphics, dataset, xpos, ypos, prevx, prevy, true);
        }

        // Save the current point as the "previous" point for future
        // line drawing.
        _prevx.setElementAt(new Long(xpos), dataset);
        _prevy.setElementAt(new Long(ypos), dataset);

        // Draw decorations that may be specified on a per-dataset basis
        Format fmt = (Format)_formats.elementAt(dataset);
        if (fmt.impulsesUseDefault) {
            if (_impulses) _drawImpulse(graphics, xpos, ypos, true);
        } else {
            if (fmt.impulses) _drawImpulse(graphics, xpos, ypos, true);
        }

        // Check to see whether the dataset has a marks directive
        int marks = _marks;
        if (!fmt.marksUseDefault) marks = fmt.marks;
        if (marks != 0) _drawPoint(graphics, dataset, xpos, ypos, true);

        if (_bars) _drawBar(graphics, dataset, xpos, ypos, true);
        if (pt.errorBar)
            _drawErrorBar(graphics, dataset, xpos,
                    _lry - (long)((pt.yLowEB - _yMin) * _yscale),
                    _lry - (long)((pt.yHighEB - _yMin) * _yscale), true);

        // Restore the color, in case the box gets redrawn.
        graphics.setColor(_foreground);
        if (_pointsPersistence > 0) {
            // Restore paint mode in case axes get redrawn.
            graphics.setPaintMode();
        }
    }

    /* Erase the point at the given index in the given dataset.  If
     * lines are being drawn, also erase the line to the next points
     * (note: not to the previous point).
     * Note that paint() should be called before
     * calling this method so that it calls _drawPlot(), which sets
     * _xscale and _yscale.  It should be adequate to check isShowing()
     * before calling this.
     */
    private synchronized void _erasePoint(Graphics graphics,
            int dataset, int index) {
        // Set the color
        if (_pointsPersistence > 0) {
            // To allow erasing to work by just redrawing the points.
            graphics.setXORMode(_background);
        }
        if (_usecolor) {
            int color = dataset % _colors.length;
            graphics.setColor(_colors[color]);
        } else {
            graphics.setColor(_foreground);
        }

        Vector pts = (Vector)_points.elementAt(dataset);
        PlotPoint pt = (PlotPoint)pts.elementAt(index);
        long ypos = _lry - (long) ((pt.y - _yMin) * _yscale);
        long xpos = _ulx + (long) ((pt.x - _xMin) * _xscale);

        // Erase line to the next point, if appropriate.
        if (index < pts.size() - 1) {
            PlotPoint nextp = (PlotPoint)pts.elementAt(index+1);
            int nextx = _ulx + (int) ((nextp.x - _xMin) * _xscale);
            int nexty = _lry - (int) ((nextp.y - _yMin) * _yscale);
            // NOTE: I have no idea why I have to give this point backwards.
            if (nextp.connected) _drawLine(graphics, dataset,
                    nextx, nexty,  xpos, ypos, true);
            nextp.connected = false;
        }

        // Draw decorations that may be specified on a per-dataset basis
        Format fmt = (Format)_formats.elementAt(dataset);
        if (fmt.impulsesUseDefault) {
            if (_impulses) _drawImpulse(graphics, xpos, ypos, true);
        } else {
            if (fmt.impulses) _drawImpulse(graphics, xpos, ypos, true);
        }

        // Check to see whether the dataset has a marks directive
        int marks = _marks;
        if (!fmt.marksUseDefault) marks = fmt.marks;
        if (marks != 0) _drawPoint(graphics, dataset, xpos, ypos, true);

        if (_bars) _drawBar(graphics, dataset, xpos, ypos, true);
        if (pt.errorBar)
            _drawErrorBar(graphics, dataset, xpos,
                    _lry - (long)((pt.yLowEB - _yMin) * _yscale),
                    _lry - (long)((pt.yHighEB - _yMin) * _yscale), true);

        // Restore the color, in case the box gets redrawn.
        graphics.setColor(_foreground);
        if (_pointsPersistence > 0) {
            // Restore paint mode in case axes get redrawn.
            graphics.setPaintMode();
        }
    }

    // Return true if the specified dataset is connected by default.
    private boolean _isConnected(int dataset) {
        if (dataset < 0) return _connected;
        _checkDatasetIndex(dataset);
        Format fmt = (Format)_formats.elementAt(dataset);
        if (fmt.connectedUseDefault) {
            return _connected;
        } else {
            return fmt.connected;
        }
    }

    // N. Collier additions
    public synchronized void clearPoints() {
      for (int i = 0; i < _points.size(); i++) {
        Vector pts = (Vector)_points.get(i);
        pts.clear();
      }
    }

    public void setDrawOnAddPoint(boolean draw) {
      drawOnPlotPoint = draw;
    }


    ///////////////////////////////////////////////////////////////////
    ////                         private variables                 ////

    /** @serial Number of points to persist for. */
    private int _pointsPersistence = 0;

    /** @serial Number of sweeps to persist for. */
    //private int _sweepsPersistence = 0;

    /** @serial True if this is a bar plot. */
    private boolean _bars = false;

    /** @serial Width of a bar in x axis units. */
    private double _barwidth = 0.5;

    /** @serial Offset per dataset in x axis units. */
    private double _baroffset = 0.05;

    /** @serial True if the points are connected. */
    private boolean _connected = true;

    /** @serial True if this is an impulse plot. */
    private boolean _impulses = false;

    /** @serial The highest data set used. */
    private int _maxdataset = -1;

    /** @serial True if we saw 'reusedatasets: on' in the file. */
    private boolean _reusedatasets = false;

    /** @serial Is this the first datapoint in a set? */
    private boolean _firstinset = true;

    /** @serial Have we seen a DataSet line in the current data file? */
    private boolean _sawfirstdataset = false;

    /** @serial Give the radius of a point for efficiency. */
    private int _radius = 3;

    /** @serial Give the diameter of a point for efficiency. */
    private int _diameter = 6;

    /** @serial Information about the previously plotted point. */
    private Vector _prevx = new Vector(), _prevy = new Vector();

    // Half of the length of the error bar horizontal leg length;
    private static final int _ERRORBAR_LEG_LENGTH = 5;


    /** @serial Flag indicating validity of _xBottom, _xTop,
     *  _yBottom, and _yTop.
     */
    private boolean _xyInvalid = false;

    /** @serial Last filename seen in command-line arguments. */
    //private String _filename = null;

    /** @serial Set by _drawPlot(), and reset by clear(). */
    private boolean _showing = false;

    /** @serial Format information on a per data set basis. */
    private Vector _formats = new Vector();

     // whether to draw the point when addPoint() called or
     // merely add the points but don't draw - useful
     // for batch updates.
     private boolean drawOnPlotPoint = true;

    ///////////////////////////////////////////////////////////////////
    ////                         inner classes                     ////

    private class Format {
        // Indicate whether the current dataset is connected.
        public boolean connected;

        // Indicate whether the above variable should be ignored.
        public boolean connectedUseDefault = true;

        // Indicate whether a stem plot should be drawn for this data set.
        // This is ignored unless the following variable is set to false.
        public boolean impulses;

        // Indicate whether the above variable should be ignored.
        public boolean impulsesUseDefault = true;

        // Indicate what type of mark to use.
        // This is ignored unless the following variable is set to false.
        public int marks;

        // Indicate whether the above variable should be ignored.
        public boolean marksUseDefault = true;

    }
}
