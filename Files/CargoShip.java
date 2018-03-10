/**
 * CargoShip.java - Class for <code>CargoShip</code> objects
 * Begun 01/15/18
 * @author Andrew Eissen
 */

//package project3;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Class for the <code>Ship</code> subclass <code>CargoShip</code> which represents a cargo ship.
 * Using the required <code>Scanner</code>-based constructor, the program sets ship-specific data
 * pertaining to the onboard cargo characteristics. Contains setters/getters and the required
 * overridden <code>toString()</code> method.
 * <br />
 * <br />
 * Class extends <code>Ship</code>
 * @see project3.Ship
 * @see project3.Thing
 * @author Andrew Eissen
 */
final class CargoShip extends Ship {

    // Rubric-required fields
    private double cargoValue, cargoVolume, cargoWeight;

    /**
     * Parameterized constructor
     * @param scannerContents Contents of <code>.txt</code> file
     * @param docksMap <code>HashMap</code> of <code>Dock</code>s, from <code>World</code>
     * @param portsMap <code>HashMap</code> of <code>SeaPort</code>s, from <code>World</code>
     */
    protected CargoShip(Scanner scannerContents, HashMap<Integer, Dock> docksMap,
            HashMap<Integer, SeaPort> portsMap) {

        super(scannerContents, docksMap, portsMap);

        if (scannerContents.hasNextDouble()) {
            this.setCargoWeight(scannerContents.nextDouble());
        }

        if (scannerContents.hasNextDouble()) {
            this.setCargoVolume(scannerContents.nextDouble());
        }

        if (scannerContents.hasNextDouble()) {
            this.setCargoValue(scannerContents.nextDouble());
        }
    }

    // Setters

    /**
     * Setter for <code>cargoWeight</code>
     * @param cargoWeight <code>double</code>
     * @return void
     */
    private void setCargoWeight(double cargoWeight) {
        this.cargoWeight = cargoWeight;
    }

    /**
     * Setter for <code>cargoVolume</code>
     * @param cargoVolume <code>double</code>
     * @return void
     */
    private void setCargoVolume(double cargoVolume) {
        this.cargoVolume = cargoVolume;
    }

    /**
     * Setter for <code>cargoValue</code>
     * @param cargoValue <code>double</code>
     * @return void
     */
    private void setCargoValue(double cargoValue) {
        this.cargoValue = cargoValue;
    }

    // Getters

    /**
     * Getter for <code>cargoWeight</code>
     * @return <code>this.cargoWeight</code>
     */
    protected double getCargoWeight() {
        return this.cargoWeight;
    }

    /**
     * Getter for <code>cargoVolume</code>
     * @return <code>this.cargoVolume</code>
     */
    protected double getCargoVolume() {
        return this.cargoVolume;
    }

    /**
     * Getter for <code>cargoValue</code>
     * @return <code>this.cargoValue</code>
     */
    protected double getCargoValue() {
        return this.cargoValue;
    }

    // Overriden methods

    /**
     * @inheritdoc
     * @return stringOutput <code>String</code>
     */
    @Override
    public String toString() {
        String stringOutput;

        stringOutput =  "Cargo Ship: " + super.toString() + "\n\tCargo Weight: "
            + this.getCargoWeight() + "\n\tCargo Volume: " + this.getCargoVolume()
            + "\n\tCargo Value: " + this.getCargoValue();

        return stringOutput;
    }
}