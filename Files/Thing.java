/**
 * Thing.java - Progenitor of all <code>World</code>-based objects
 * Begun 01/15/18
 * @author Andrew Eissen
 */

//package project3;

import java.util.Scanner;

/**
 * This class is the progenitor of all <code>World</code>-based objects, from ship types to docks
 * and ports. As per the rubric, it implements the interface <code>Comparable</code>, which sees no
 * use during the first project but is included anyway.
 * <br />
 * <br />
 * Class implements <code>Comparable</code>
 * @see java.lang.Comparable
 * @author Andrew Eissen
 */
class Thing implements Comparable<Thing> {

    // Rubric-required variables in use in all objects
    private int index;
    private String name;
    private int parent;

    /**
     * Parameterized constructor, accepts <code>Scanner</code> contents as per rubric
     * @param scannerContents Content of the <code>.txt</code> file
     */
    protected Thing(Scanner scannerContents) {
        if (scannerContents.hasNext()) {
            this.setName(scannerContents.next());
        }

        if (scannerContents.hasNextInt()) {
            this.setIndex(scannerContents.nextInt());
        }

        if (scannerContents.hasNextInt()) {
            this.setParent(scannerContents.nextInt());
        }
    }

    // Setters

    /**
     * Setter for <code>index</code>
     * @param index <code>int</code>
     * @return void
     */
    private void setIndex(int index) {
        this.index = index;
    }

    /**
     * Setter for <code>name</code>
     * @param name <code>String</code>
     * @return void
     */
    private void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for <code>parent</code>
     * @param parent <code>int</code>
     * @return void
     */
    private void setParent(int parent) {
        this.parent = parent;
    }

    // Getters

    /**
     * Getter for <code>index</code>
     * @return <code>this.index</code>
     */
    protected int getIndex() {
        return this.index;
    }

    /**
     * Getter for <code>name</code>
     * @return <code>this.name</code>
     */
    protected String getName() {
        return this.name;
    }

    /**
     * Getter for <code>parent</code>
     * @return <code>this.parent</code>
     */
    protected int getParent() {
        return this.parent;
    }

    // Overridden methods

    /**
     * @inheritdoc
     * @return <code>String</code>
     */
    @Override
    public String toString() {
        // It didn't make sense to include parent, since things are organized by parent anyway
        return this.getName() + " " + this.getIndex();
    }

    /**
     * @inheritdoc
     * @param thingInstance <code>Thing</code>
     * @return <code>boolean</code>
     */
    @Override
    public int compareTo(Thing thingInstance) {
        if (
            (thingInstance.getIndex() == this.getIndex()) &&
            (thingInstance.getName().equals(this.getName())) &&
            (thingInstance.getParent() == this.getParent())
        ) {
            return 1;
        } else {
            return 0;
        }
    }
}