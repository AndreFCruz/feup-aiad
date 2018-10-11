package uchicago.src.sim.parameter.rpl;

/**
 * Intermediate representation of a float value used by the compiler.
 *
 * @version $Revision: 1.2 $ $Date: 2003/04/10 15:57:47 $
 */

public class RPLFloatValue extends RPLValue {

  private float value;

  /**
   * Creates a RPLFloatValue with the specified value.
   *
   * @param value the float value.
   */
  public RPLFloatValue(float value) {
    this.value = value;
  }

  /**
   * Gets the value of this RPLFloatValue as a Float.
   */
  public Object getValue() {
    return new Float(value);
  }

  /**
   * Returns float.class.
   */
  public Class getType() {
    return float.class;
  }
}
