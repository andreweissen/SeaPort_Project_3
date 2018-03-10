/**
 * PassengerShip.java - Class for <code>PassengerShip</code> objects
 * Begun 01/15/18
 * @author Andrew Eissen
 */

//package project3;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Class for the <code>Ship</code> subclass <code>PassengerShip</code> which represents a passenger
 * ship. Contains data pertaining to rooms and passengers, set in the proper field by the required
 * <code>Scanner</code> constructor (per the rubric). Also contains the necessary overridden
 * <code>toString()</code> method.
 * <br />
 * <br />
 * Class extends <code>Ship</code>
 * @see project3.Ship
 * @see project3.Thing
 * @author Andrew Eissen
 */
final class PassengerShip extends Ship {

    // Rubric-required fields
    private int numberOfOccupiedRooms, numberOfPassengers, numberOfRooms;

    /**
     * Parameterized constructor
     * @param scannerContents Contents of <code>.txt</code> file
     * @param docksMap <code>HashMap</code> of <code>Dock</code>s, from <code>World</code>
     * @param portsMap <code>HashMap</code> of <code>SeaPort</code>s, from <code>World</code>
     */
    protected PassengerShip(Scanner scannerContents, HashMap<Integer, Dock> docksMap,
            HashMap<Integer, SeaPort> portsMap) {

        super(scannerContents, docksMap, portsMap);

        if (scannerContents.hasNextInt()) {
            this.setNumberOfPassengers(scannerContents.nextInt());
        }

        if (scannerContents.hasNextInt()) {
            this.setNumberOfRooms(scannerContents.nextInt());
        }

        if (scannerContents.hasNextInt()) {
            this.setNumberOfOccupiedRooms(scannerContents.nextInt());
        }
    }

    // Setters

    /**
     * Setter for <code>numberOfPassengers</code>
     * @param numberOfPassengers <code>int</code>
     * @return void
     */
    private void setNumberOfPassengers(int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }

    /**
     * Setter for <code>numberOfRooms</code>
     * @param numberOfRooms <code>int</code>
     * @return void
     */
    private void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    /**
     * Setter for <code>numeberOfOccupiedRooms</code>
     * @param numberOfOccupiedRooms <code>int</code>
     * @return void
     */
    private void setNumberOfOccupiedRooms(int numberOfOccupiedRooms) {
        this.numberOfOccupiedRooms = numberOfOccupiedRooms;
    }

    // Getters

    /**
     * Getter for <code>numberOfPassengers</code>
     * @return <code>this.numberOfPassengers</code>
     */
    protected int getNumberOfPassengers() {
        return this.numberOfPassengers;
    }

    /**
     * Getter for <code>numberOfRooms</code>
     * @return <code>this.numberOfRooms</code>
     */
    protected int getNumberOfRooms() {
        return this.numberOfRooms;
    }

    /**
     * Getter for <code>numberOfOccupiedRooms</code>
     * @return <code>this.numberOfOccupiedRooms</code>
     */
    protected int getNumberOfOccupiedRooms() {
        return this.numberOfOccupiedRooms;
    }

    // Overriden methods

    /**
     * @inheritdoc
     * @return stringOutput <code>String</code>
     */
    @Override
    public String toString() {
        String stringOutput;

        stringOutput = "Passenger Ship: " + super.toString() + "\n\tPassengers: "
            + this.getNumberOfPassengers() + "\n\tRooms: " + this.getNumberOfRooms()
            + "\n\tOccupied Rooms: " + this.getNumberOfOccupiedRooms();

        return stringOutput;
    }
}