package uchicago.src.sim.parameter.rpl;

/**
 *  Intermediate representation of a double value used by the compiler.
 *
 * @version $Revision: 1.2 $ $Date: 2003/04/10 15:57:47 $
 */

public class RPLDoubleValue extends RPLValue {

  private double value;

  /**
   * Creates a RPLDoubleValue with the specified value.
   *
   * @param value the double value.
   */

  public RPLDoubleValue(double value) {
    this.value = value;
  }

  /**
   * Gets the value of the RPLDoubleValue as a Double.
   */
  public Object getValue() {
    return new Double(value);
  }

  /**
   * Returns double.class.
   */
  public Class getType() {
    return double.class;
  }
}
