package uchicago.src.sim.parameter.rpl;

/**
 * Intermediate representation of a String value used by the compiler.
 *
 * @version $Revision: 1.2 $ $Date: 2003/04/10 15:57:47 $
 */

public class RPLStringValue extends RPLValue {

  private String value;

  /**
   * Creates a RPLStringValue with the specified value.
   *
   * @param value the String value.
   */
  public RPLStringValue(String value) {
    this.value = value;
  }

  /**
   * Returns the value of this RPLStringValue
   */
  public Object getValue() {
    return value;
  }

  /**
   * Returns String.class.
   */
  public Class getType() {
    return java.lang.String.class;
  }
}
