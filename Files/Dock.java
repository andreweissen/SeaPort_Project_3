/**
 * Dock.java - Class for <code>Dock</code> objects
 * Begun 01/15/18
 * @author Andrew Eissen
 */

//package project3;

import java.util.Scanner;

/**
 * This class represents docks contained within specific <code>SeaPort</code> locales. Contains a
 * single unique field, <code>ship</code>, representing the vessel docked at the location. Apart
 * from setter/getter and overridden <code>toString()</code> method, this method is fairly simple
 * and contains no special methods.
 * <br />
 * <br />
 * Class extends <code>Thing</code>
 * @see project3.Thing
 * @author Andrew Eissen
 */
final class Dock extends Thing {

    // Rubric-required field
    private Ship ship;

    /**
     * Parameterized constructor
     * @param scannerContents Contents of <code>.txt</code> file
     */
    protected Dock(Scanner scannerContents) {
        super(scannerContents);
    }

    /**
     * Setter for <code>ship</code>; note that this setter is set to <code>protected</code>, the
     * only setter in the program not set to <code>private</code>. Since the program's
     * <code>World</code> instance needs to set the ship from within itself, using a private setting
     * would not work.
     * @param ship <code>Ship</code> instance
     * @return void
     */
    protected void setShip(Ship ship) {
        this.ship = ship;
    }

    /**
     * Getter for <code>ship</code>
     * @return <code>this.ship</code>
     */
    protected Ship getShip() {
        return this.ship;
    }

    /**
     * @inheritdoc
     * @return stringOutput <code>String</code>
     */
    @Override
    public String toString() {
        String stringOutput = "Dock: " + super.toString() + "\n\t";

        if (this.getShip() == null) {
            stringOutput += "EMPTY";
        } else {
            stringOutput += this.getShip().toString();
        }

        return stringOutput;
    }
}