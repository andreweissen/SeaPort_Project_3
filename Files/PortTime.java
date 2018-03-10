/**
 * PortTime.java - Class for <code>PortTime</code> objects
 * Begun 01/15/18
 * @author Andrew Eissen
 */

//package project3;

/**
 * As of Project 3, this class sits unused, but is designed to represent the specific times related
 * to the entry of ships in to <code>SeaPort</code> locales, presumably. As it is not an extended
 * subclass of <code>Thing</code>, it does not possess a <code>Scanner</code>-based constructor, but
 * rather accepts a value in a more conventional constructor design.
 * <br />
 * <br />
 * @author Andrew Eissen
 */
final class PortTime {

    // Rubric-required field
    private int time;

    /**
     * Parameterized constructor
     * @param time <code>int</code>
     */
    protected PortTime(int time) {
        this.setTime(time);
    }

    // Setter

    /**
     * Setter for <code>time</code>
     * @param time <code>int</code>
     * @return void
     */
    private void setTime(int time) {
        this.time = time;
    }

    // Getter

    /**
     * Getter for <code>time</code>
     * @return <code>this.time</code>
     */
    protected int getTime() {
        return this.time;
    }

    // Overriden method

    /**
     * @inheritdoc
     * @return <code>String</code>
     */
    @Override
    public String toString() {
        return "Time: " + this.getTime();
    }
}