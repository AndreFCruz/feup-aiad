package uchicago.src.sim.parameter.rpl;

/**
 * Intermediate representation of a boolean value used by the compiler.
 *
 * @version $Revision: 1.2 $ $Date: 2003/04/10 15:57:47 $
 */

public class RPLBooleanValue extends RPLValue {

  private boolean value;

  /**
   * Creates a RPLBooleanValue with the specified value.
   *
   * @param value the value (true or false).
   */
  public RPLBooleanValue(boolean value) {
    this.value = value;
  }

  /**
   * Gets the value of the RPLBooleanValue as a Boolean.
   */
  public Object getValue() {
    return Boolean.valueOf(value);
  }

  /**
   * Returns boolean.class. 
   */
  public Class getType() {
    return boolean.class;
  }
}
