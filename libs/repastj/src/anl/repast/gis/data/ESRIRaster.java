package anl.repast.gis.data;

import com.esri.arcgis.geodatabase.*;
import com.esri.arcgis.datasourcesraster.*;
import com.esri.arcgis.system.EngineInitializer;
import com.esri.arcgis.system.AoInitialize;
import com.esri.arcgis.system.esriLicenseProductCode;
import com.esri.arcgis.geometry.*;

import java.io.IOException;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * @author Nick Collier
 * @version $revision$ $date$
 */
public class ESRIRaster {

  private IRasterDataset2 rasterDataset;
  private java.util.List properties = new ArrayList();


  private static boolean initialized = false;

  private class RasterProperties {

    int rows, cols;
    Rectangle2D extent;
    double cellWidth, cellHeight;
    Object noDataValue;
    boolean yDecreasing;

    public RasterProperties(IRasterProps rasProps) throws IOException {
      IEnvelope env = rasProps.getExtent();
      extent = new Rectangle2D.Double(env.getUpperLeft().getX(), env.getUpperLeft().getY(),
              env.getWidth(), env.getHeight());
      this.rows = rasProps.getHeight();
      this.cols = rasProps.getWidth();
      cellWidth = extent.getWidth() / cols;
      cellHeight = extent.getHeight() / rows;
      noDataValue = rasProps.getNoDataValue();
    }

    public Point2D pixelToMap(int x, int y) {
      double px = cellWidth * x + extent.getX();
      double py = cellHeight * (-y) + extent.getY();
      return new Point2D.Double(px, py);

    }

    public Point2D mapToPixel(double x, double y) {
      double px = (x - extent.getX()) / cellWidth;
      double py = (extent.getY() - y) / cellHeight;
      //if (py < 0) py = -py;
      return new Point2D.Double(px, py);
    }
  }

  public static void init() throws IOException {
    if (!initialized) {
      EngineInitializer.initializeEngine();
      AoInitialize aoInit = new AoInitialize();
      aoInit.initialize(esriLicenseProductCode.esriLicenseProductCodeEngineGeoDB);
      initialized = true;
    }
  }

  //todo constructor for creating a new raster -- see email
  public ESRIRaster(String path, String rasterName) throws IOException {
    ESRIRaster.init();

    IWorkspaceFactory factory = new RasterWorkspaceFactory();
    IRasterWorkspace2 workspace = new IRasterWorkspace2Proxy(factory.openFromFile(path, 0));
    rasterDataset = new IRasterDataset2Proxy(workspace.openRasterDataset(rasterName));

    IGeoDataset geoDataset = new IGeoDatasetProxy(rasterDataset);
    IRasterBandCollection rasterBands = new IRasterBandCollectionProxy(geoDataset);
    for (int i = 0, n = rasterBands.getCount(); i < n; i++) {
      IRasterBand band = rasterBands.item(i);
      IRasterProps rasProps = new IRasterPropsProxy(band);
      RasterProperties props = new RasterProperties(rasProps);
      properties.add(props);
    }
  }

  public int getRows(int bandIndex) throws IOException {
    RasterProperties props = (RasterProperties) properties.get(bandIndex);
    return props.rows;
  }

  public int getColumns(int bandIndex) throws IOException {
    RasterProperties props = (RasterProperties) properties.get(bandIndex);
    return props.cols;
  }

  public Point2D mapToPixel(double x, double y, int bandIndex) {
    RasterProperties props = (RasterProperties) properties.get(bandIndex);
    return props.mapToPixel(x, y);
  }

  public Point2D pixelToMap(int x, int y, int bandIndex) {
    RasterProperties props = (RasterProperties) properties.get(bandIndex);
    return props.pixelToMap(x, y);
  }

  public int getBandCount() throws IOException {
    IGeoDataset geoDataset = new IGeoDatasetProxy(rasterDataset);
    IRasterBandCollection rasterBands = new IRasterBandCollectionProxy(geoDataset);
    return rasterBands.getCount();
  }

  public Object getPixelValue(int row, int col, int bandIndex) throws IOException {
    IGeoDataset geoDataset = new IGeoDatasetProxy(rasterDataset);
    IRasterBandCollection rasterBands = new IRasterBandCollectionProxy(geoDataset);
    IRasterBand band = rasterBands.item(bandIndex);
    IRawPixels rawPix = new IRawPixelsProxy(band);
    IPnt pnt = new DblPnt();
    pnt.setCoords(col, row);
    IPnt size = new DblPnt();
    size.setCoords(1, 1);
    // creates a pixelBlk of size 1,1
    IPixelBlock pixelBlk = rawPix.createPixelBlock(size);
    // reads the data into the pixelBlk
    rawPix.read(pnt, pixelBlk);
    // get the value at the first band and the first cell in the block
    return pixelBlk.getVal(0, 0, 0);
  }

  public double[] getPixelMooreNghValues(int row, int col, int bandIndex) throws IOException {
    IGeoDataset geoDataset = new IGeoDatasetProxy(rasterDataset);
    IRasterBandCollection rasterBands = new IRasterBandCollectionProxy(geoDataset);
    IRasterBand band = rasterBands.item(bandIndex);
    IRawPixels rawPix = new IRawPixelsProxy(band);
    IPnt pnt = new DblPnt();
    // northwest corner
    pnt.setCoords(col - 1, row - 1);
    IPnt size = new DblPnt();
    size.setCoords(3, 3);
    IPixelBlock pixelBlk = rawPix.createPixelBlock(size);
    // reads the data into the pixelBlk
    rawPix.read(pnt, pixelBlk);
    double[] data = new double[8];
    RasterProperties props = (RasterProperties) properties.get(bandIndex);
    int index = 0;
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        if (x == 1 && y == 1) continue;
        Object val = pixelBlk.getVal(0, x, y);
        double dVal = ((Number) val).doubleValue();
        if (val.equals(props.noDataValue)) {
          dVal = Double.NaN;
        }
        data[index++] = dVal;
      }
    }
    return data;
  }

  public Object getPixelValueAtMap(double x, double y, int bandIndex) throws IOException {
    /*
    IRaster raster = rasterDataset.createFullRaster();
    Multipoint points = new Multipoint();
    IPoint pnt = new com.esri.arcgis.geometry.Point();
    pnt.setX(x);
    pnt.setY(y);
    points.addPoint(pnt, null, null);
    IRasterGeometryProc3 geo = new
            IRasterGeometryProc3Proxy(new RasterGeometryProc());
    IPointCollection pixelPoints =
            geo.pointsMap2PixelTransform(points, true, raster);
    pnt = pixelPoints.getPoint(0);
    */
    Point2D pnt = mapToPixel(x, y, bandIndex);
    return getPixelValue((int) pnt.getX(), (int) pnt.getY(), bandIndex);
  }

  /*
  public Point2D getPixelForMap(double x, double y) throws IOException {
    IRaster raster = rasterDataset.createFullRaster();
    Multipoint points = new Multipoint();
    IPoint pnt = new com.esri.arcgis.geometry.Point();
    pnt.setX(x);
    pnt.setY(y);
    points.addPoint(pnt, null, null);
    IRasterGeometryProc3 geo = new
            IRasterGeometryProc3Proxy(new RasterGeometryProc());
    IPointCollection pixelPoints =
            geo.pointsMap2PixelTransform(points, true, raster);
    pnt = pixelPoints.getPoint(0);
    return new Point2D.Double(pnt.getX(), pnt.getY());
  }
  */

  public void writeToPixel(byte val, int x, int y, int bandIndex) throws IOException {
    IRasterBandCollection bands = new IRasterBandCollectionProxy(rasterDataset);
    IRasterBand band = bands.item(bandIndex);
    IRawPixels rawPix = new IRawPixelsProxy(band);

    IRasterProps rasProps = new IRasterPropsProxy(rawPix);
    /*create pixelblock*/
    IPnt pnt = new DblPnt();
    pnt.setCoords(x, y);
    IPnt size = new DblPnt();
    size.setCoords(1, 1);
    IPixelBlock pixelBlk = rawPix.createPixelBlock(size);
    /*read rawPix into pixelblock */
    rawPix.read(pnt, pixelBlk);
    IPixelBlock3 pixelBlock = new IPixelBlock3Proxy(pixelBlk);
    //System.out.println("pixelBlock.getPixelData(0); = " + pixelBlock.getPixelData(0));
    byte[][] pixelData = (byte[][]) pixelBlock.getPixelData(0);
    pixelData[0][0] = val;
    pixelBlock.setPixelData(0, pixelData);
    IPixelBlock pblock = new IPixelBlockProxy(pixelBlock);
    rawPix.write(pnt, pblock);
    com.linar.jintegra.Cleaner.release(rawPix);
    com.linar.jintegra.Cleaner.release(rasProps);
    com.linar.jintegra.Cleaner.release(band);
    com.linar.jintegra.Cleaner.release(bands);
    com.linar.jintegra.Cleaner.release(rasterDataset);
  }

  public static void main(String[] args) {
    try {
      ESRIRaster raster = new ESRIRaster("c:\\tmp", "c3hmrng");

      System.out.println("raster.getBandCount() = " + raster.getBandCount());
      System.out.println("raster.getRows() = " + raster.getRows(0));
      System.out.println("raster.getRows() = " + raster.getColumns(0));

      System.out.println("raster.getPixelValue(1, -1 ,0) = " + raster.getPixelValue(1, -1, 0));

      /*
      double[] data = raster.getPixelMooreNghValues(0, 0, 0);
      for (int i = 0; i < data.length; i++) {
        System.out.println("data[" + i + "] = " + data[i]);
      }
      */


      Point2D p = new Point2D.Double(463801.48776679195, 3879064.485378691);


      System.out.println("p = " + p);
      Point2D point = raster.mapToPixel(p.getX(), p.getY(), 0);
      System.out.println("my raster map to pixel = " + point);

      System.out.println("raster.pixelToMap((int)point.getX(),  = " + raster.pixelToMap((int) point.getX(), (int)point.getY(), 0));


      /*
      data = raster.getPixelMooreNghValues((int) point.getX(), (int) point.getY(), 0);
      for (int i = 0; i < data.length; i++) {
        System.out.println("data[" + i + "] = " + data[i]);
      }
      */
      //System.out.println("esri map to pixel = " + raster.getPixelForMap(p.getX(), p.getY()));

      //System.out.println("my mapToPixel: " + raster.mapToPixel());

    } catch (IOException e) {
      System.out.println("Failure initializing AO: " + e);
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }


  }
}
