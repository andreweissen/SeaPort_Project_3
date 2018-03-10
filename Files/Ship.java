/**
 * Ship.java - Progenitor of <code>Ship</code> objects
 * Begun 01/15/18
 * @author Andrew Eissen
 */

//package project3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * This class represents general ship objects to be stored in queues or docks in port objects. The
 * class serves as the progenitor parent of two subclasses, namely <code>PassengerShip</code> and
 * <code>CargoShip</code>, and contains fields and <code>ArrayList</code>s related to the specific
 * physical properties of the vessel in question. However, like many of the world objects, the class
 * contains mainly setters and getters, the required <code>Scanner</code> based constructor, and the
 * overridden <code>toString()</code> method.
 * <br />
 * <br />
 * Interestingly, the Project 1 design rubric makes no mention of what to do with the
 * <code>PortTime</code> values <code>arrivalTime</code> and <code>dockTime</code>, so they are
 * simply included but left untouched.
 * <br />
 * <br />
 * As per Project 3, a series of additional methods able to be inherited by both
 * <code>CargoShip</code>s and <code>PassengerShip</code>s have been added to more readily
 * facilitate the <code>Job</code> thread progression operations. The constructor now accepts a pair
 * of <code>World</code> <code>HashMap</code>s of both <code>Dock</code> and <code>SeaPort</code>
 * values and vessels/piers related to the Ship's various <code>Integer</code> indices are set
 * herein.
 * <br />
 * <br />
 * Class extends <code>Thing</code>
 * @see project3.Thing
 * @see project3.PassengerShip
 * @see project3.CargoShip
 * @author Andrew Eissen
 */
class Ship extends Thing {

    // Rubric-required fields
    private PortTime arrivalTime, dockTime;
    private double draft, length, weight, width;
    private ArrayList<Job> jobs;

    // Project 3 & 4 fields
    private SeaPort port;
    private Dock dock;
    private HashMap<Integer, Dock> docksMap;

    /**
     * Parameterized constructor
     * @param scannerContents Contents of <code>.txt</code> file
     * @param docksMap <code>HashMap</code> of <code>Dock</code>s, from <code>World</code>
     * @param portsMap <code>HashMap</code> of <code>SeaPort</code>s, from <code>World</code>
     */
    protected Ship(Scanner scannerContents, HashMap<Integer, Dock> docksMap,
            HashMap<Integer, SeaPort> portsMap) {

        super(scannerContents);

        if (scannerContents.hasNextDouble()) {
            this.setWeight(scannerContents.nextDouble());
        }

        if (scannerContents.hasNextDouble()) {
            this.setLength(scannerContents.nextDouble());
        }

        if (scannerContents.hasNextDouble()) {
            this.setWidth(scannerContents.nextDouble());
        }

        if (scannerContents.hasNextDouble()) {
            this.setDraft(scannerContents.nextDouble());
        }

        this.setJobs(new ArrayList<>());
        this.setPort(docksMap, portsMap);
        this.setDocksMap(docksMap);
        this.setDock();
    }

    // Setters

    /**
     * Setter for <code>arrivalTime</code>
     * @param arrivalTime <code>PortTime</code>
     * @return void
     */
    private void setArrivalTime(PortTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    /**
     * Setter for <code>dockTime</code>
     * @param dockTime <code>PortTime</code>
     * @return void
     */
    private void setDockTime(PortTime dockTime) {
        this.dockTime = dockTime;
    }

    /**
     * Setter for <code>weight</code>
     * @param weight <code>double</code>
     * @return void
     */
    private void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Setter for <code>length</code>
     * @param length <code>double</code>
     * @return void
     */
    private void setLength(double length) {
        this.length = length;
    }

    /**
     * Setter for <code>width</code>
     * @param width <code>double</code>
     * @return void
     */
    private void setWidth(double width) {
        this.width = width;
    }

    /**
     * Setter for <code>draft</code>
     * @param draft <code>double</code>
     * @return void
     */
    private void setDraft(double draft) {
        this.draft = draft;
    }

    /**
     * Setter for <code>jobs</code>
     * @param jobs <code>ArrayList</code>
     * @return void
     */
    private void setJobs(ArrayList<Job> jobs) {
        this.jobs = jobs;
    }

    /**
     * Setter for <code>this.port</code>, using <code>World</code>'s localized <code>HashMap</code>s
     * @param docksMap <code>HashMap</code>
     * @param portsMap <code>HashMap</code>
     * @return void
     */
    private void setPort(HashMap<Integer, Dock> docksMap, HashMap<Integer, SeaPort> portsMap) {
        this.port = portsMap.get(this.getParent());

        if (this.port == null) {
            Dock pier = docksMap.get(this.getParent());
            this.port = portsMap.get(pier.getParent());
        }
    }

    /**
     * Setter for <code>docksMap</code>
     * @param docksMap <code>HashMap</code>
     * @return void
     */
    private void setDocksMap(HashMap<Integer, Dock> docksMap) {
        this.docksMap = docksMap;
    }

    /**
     * No-parameter setter for <code>this.dock</code>, called within this class
     * @return void
     */
    private void setDock() {
        if (this.getDocksMap().containsKey(this.getParent())) {
            this.dock = this.getDocksMap().get(this.getParent());
        } else {
            this.dock = null;
        }
    }

    /**
     * Parameterized setter for <code>this.dock</code>, called from within <code>Job.run</code>
     * @param dock <code>Dock</code>
     * @return void
     */
    protected void setDock(Dock dock) {
        this.dock = dock;
    }

    // Getters

    /**
     * Getter for <code>arrivalTime</code>
     * @return <code>this.arrivalTime</code>
     */
    protected PortTime getArrivalTime() {
        return this.arrivalTime;
    }

    /**
     * Getter for <code>dockTime</code>
     * @return <code>this.dockTime</code>
     */
    protected PortTime getDockTime() {
        return this.dockTime;
    }

    /**
     * Getter for <code>weight</code>
     * @return <code>this.weight</code>
     */
    protected double getWeight() {
        return this.weight;
    }

    /**
     * Getter for <code>length</code>
     * @return <code>this.length</code>
     */
    protected double getLength() {
        return this.length;
    }

    /**
     * Getter for <code>width</code>
     * @return <code>this.width</code>
     */
    protected double getWidth() {
        return this.width;
    }

    /**
     * Getter for <code>draft</code>
     * @return <code>this.draft</code>
     */
    protected double getDraft() {
        return this.draft;
    }

    /**
     * Getter for <code>jobs</code>
     * @return <code>this.jobs</code>
     */
    protected ArrayList<Job> getJobs() {
        return this.jobs;
    }

    /**
     * Getter for <code>port</code>
     * @return <code>this.port</code>
     */
    protected SeaPort getPort() {
        return this.port;
    }

    /**
     * Getter for <code>docksMap</code>
     * @return <code>this.docksMap</code>
     */
    private HashMap<Integer, Dock> getDocksMap() {
        return this.docksMap;
    }

    /**
     * Getter for <code>dock</code>
     * @return <code>this.dock</code>
     */
    protected Dock getDock() {
        return this.dock;
    }

    // Overriden methods

    /**
     * @inheritdoc
     * @return stringOutput <code>String</code>
     */
    @Override
    public String toString() {
        String stringOutput;

        stringOutput = super.toString() + "\n\tWeight: " + this.getWeight() + "\n\tLength: "
            + this.getLength() + "\n\tWidth: " + this.getWidth() + "\n\tDraft: " + this.getDraft()
            + "\n\tJobs:";

        if (this.getJobs().isEmpty()){
            stringOutput += " None";
        } else {
            for (Job newJob : this.getJobs()) {
                stringOutput += "\n" + newJob.toString();
            }
        }

        return stringOutput;
    }
}