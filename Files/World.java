/**
 * World.java - Central class that stores/organizes all other <code>Thing</code>-based objects
 * Begun 01/15/18
 * @author Andrew Eissen
 */

//package project3;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * This class represents the world as it exists per the contents of the user-selected
 * <code>.txt</code> file. It accepts <code>Scanner</code> data and sorts through it, assembling
 * proper instances of the applicable classes and moving them into the proper <code>ArrayList</code>
 * or field. It also hosts a global <code>allThings</code> <code>ArrayList</code> that contains
 * a complete listing of all <code>Thing</code>-based objects for ease of searching/sorting.
 * <br />
 * <br />
 * The class also contains a number of utility methods recommended by the rubric for ease in adding
 * appropriate objects to the chosen locations. Though
 * <a href="https://github.com/andreweissen">the author</a> believes that the use of a set of
 * <code>HashMap</code>s would have largely negated the need for such methods, they were included in
 * an attempt to remain true to the specifics of the rubric.
 * <br />
 * <br />
 * Class extends <code>Thing</code>
 * @see project3.Thing
 * @author Andrew Eissen
 */
final class World extends Thing {

    // Rubric-required fields
    private ArrayList<Thing> allThings;
    private ArrayList<SeaPort> ports;
    private PortTime time;

    /**
     * Parameterized constructor
     * @param scannerContents Content of the <code>.txt</code> file
     */
    protected World(Scanner scannerContents) {
        super(scannerContents);
        this.setAllThings(new ArrayList<>());
        this.setPorts(new ArrayList<>());
        this.process(scannerContents);
    }

    // Setters

    /**
     * Setter for <code>allThings</code>, the global <code>Thing</code> repository.
     * @param allThings <code>ArrayList</code>
     * @return void
     */
    private void setAllThings(ArrayList<Thing> allThings) {
        this.allThings = allThings;
    }

    /**
     * Setter for <code>ports</code>, the local <code>ArrayList</code> for <code>SeaPort</code>s.
     * @param ports <code>ArrayList</code>
     * @return void
     */
    private void setPorts(ArrayList<SeaPort> ports) {
        this.ports = ports;
    }

    /**
     * Setter for <code>time</code>, a yet unused variable that is not mentioned in the rubric.
     * @param time <code>PortTime</code>
     * @return void
     */
    private void setTime(PortTime time) {
        this.time = time;
    }

    // Getters

    /**
     * Getter for <code>allThings</code>, used often by <code>SeaPortProgram</code>.
     * @see project1.SeaPortProgram
     * @return <code>this.allThings</code>
     */
    protected ArrayList<Thing> getAllThings() {
        return this.allThings;
    }

    /**
     * Getter for <code>ports</code>
     * @return <code>this.ports</code>
     */
    protected ArrayList<SeaPort> getPorts() {
        return this.ports;
    }

    /**
     * Getter for <code>time</code>
     * @return <code>this.time</code>
     */
    protected PortTime getTime() {
        return this.time;
    }

    // Handlers

    /**
     * This is the main method of the <code>World</code> class, invoked after the definitions of the
     * <code>ArrayList</code>s <code>allThings</code> and <code>ports</code> in the body of the main
     * constructor.
     * <br />
     * <br />
     * As per the rubric, it breaks down the contents of the user-inputted <code>.txt</code> file
     * into individual lines representing specific <code>Thing</code>s in the world, building
     * appropriate objects of the given type and moving those into the proper locations as per the
     * organizational structure denoted on the first page of the Project 1 rubric. Blank lines are
     * skipped and beginning/ending whitespace is removed prior to evaluation. Each new object is
     * added to the global <code>allThings</code> listing and passed to an invoked utility handler
     * that assigns the object to the proper location.
     * <br />
     * <br />
     * As per the Project 2 rubric, this method now employs localized <code>HashMap</code>s for use
     * in getting parent indices quicker than having to invoke inefficient iterative methods.
     *
     * @param scannerContents Contents of the user-inputted <code>.txt</code> file
     * @return void
     */
    private void process(Scanner scannerContents) {

        // Assorted method fields
        String lineString;
        Scanner lineContents;

        // Localized HashMaps in Scanner method, per Project 2 rubric
        HashMap<Integer, SeaPort> portsMap = new HashMap<>();
        HashMap<Integer, Dock> docksMap = new HashMap<>();
        HashMap<Integer, Ship> shipsMap = new HashMap<>();

        while (scannerContents.hasNextLine()) {
            lineString = scannerContents.nextLine().trim(); // Remove spaces

            // Avoid evaluating any blank lines if exist
            if (lineString.length() == 0) {
                continue;
            }

            // Scanner for each line's contents
            lineContents = new Scanner(lineString);

            if (lineContents.hasNext()) {

                /**
                 * Builds <code>Thing</code> objects & stuff, passing them to the appropriate adder
                 * method. For ease of sorting later on, all <tt>Thing</tt>s are stuffed into an
                 * <code>ArrayList</code>, namely <code>allThings</code>. The program iterates over
                 * the structure in search of the proper input. It would be easier to use ye olde
                 * <code>HashMap</code>s but this is simpler and more in line with the ethos of the
                 * rubric.
                 */
                switch(lineContents.next().trim()) {
                    case "port":
                        SeaPort newSeaPort = new SeaPort(lineContents);
                        this.getAllThings().add(newSeaPort);
                        this.getPorts().add(newSeaPort);
                        portsMap.put(newSeaPort.getIndex(), newSeaPort);
                        break;
                    case "dock":
                        Dock newDock = new Dock(lineContents);
                        this.getAllThings().add(newDock);
                        this.addThingToList(portsMap, newDock, "getDocks");
                        docksMap.put(newDock.getIndex(), newDock);
                        break;
                    case "pship":
                        PassengerShip newPassengerShip = new PassengerShip(lineContents, docksMap,
                            portsMap);
                        this.getAllThings().add(newPassengerShip);
                        this.addShipToParent(newPassengerShip, docksMap, portsMap);
                        shipsMap.put(newPassengerShip.getIndex(), newPassengerShip);
                        break;
                    case "cship":
                        CargoShip newCargoShip = new CargoShip(lineContents, docksMap, portsMap);
                        this.getAllThings().add(newCargoShip);
                        this.addShipToParent(newCargoShip, docksMap, portsMap);
                        shipsMap.put(newCargoShip.getIndex(), newCargoShip);
                        break;
                    case "person":
                        Person newPerson = new Person(lineContents);
                        this.getAllThings().add(newPerson);
                        this.addThingToList(portsMap, newPerson, "getPersons");
                        break;
                    case "job":
                        Job newJob = new Job(lineContents, shipsMap);
                        this.getAllThings().add(newJob);
                        this.addJobToShip(newJob, shipsMap, docksMap);
                        break;
                    default: // Added because required by Google styleguide
                        break;
                }
            }
        }
    }

    /**
     * Generic addition method that replaces a fair bit of copy/pasta methods that were basically
     * identical. Accepts a new <code>Thing</code> subclass object and a <code>String</code>
     * representation of a declared method as parameters, allowing for the selection of the proper
     * <code>SeaPort</code> <code>ArrayList</code> getter method depending on input.
     *
     * @see java.lang.reflect
     * @param <T> extends <code>Thing</code>
     * @param portsMap <code>HashMap</code>
     * @param newThing <code>T</code>
     * @param methodName <code>String</code>
     */
    @SuppressWarnings("unchecked")
    private <T extends Thing> void addThingToList(HashMap<Integer, SeaPort> portsMap, T newThing,
            String methodName) {

        // Declarations
        SeaPort newPort;
        ArrayList<T> thingsList;
        Method getList;

        // Definition
        newPort = portsMap.get(newThing.getParent());

        try {
            // Either SeaPort.class.getPersons() or SeaPort.class.getDocks();
            getList = SeaPort.class.getDeclaredMethod(methodName);

            // See casting comment in above method
            thingsList = (ArrayList<T>) getList.invoke(newPort); // newPort.getList()

            if (newPort != null) {
                thingsList.add(newThing);
            }
        } catch (
            NoSuchMethodException |
            SecurityException |
            IllegalAccessException |
            IllegalArgumentException |
            InvocationTargetException ex
        ) {
            System.out.println("Error: " + ex);
        }
    }

    /**
     * This method was a tricky one, and is similar in scope to the method below. In some cases, the
     * value of <code>getParent</code> for the new <code>Job</code> instance will not match that of
     * an extant ship in any <code>SeaPort</code>'s <code>getShips()</code> <code>ArrayList</code>,
     * so the method must check each port's docks for moored ships, find the ship, and add the
     * new job to that ship.
     *
     * @param newJob <code>Job</code>
     * @param shipsMap <code>HashMap</code>
     * @param docksMap <code>HashMap</code>
     * @return void
     */
    private void addJobToShip(Job newJob, HashMap<Integer, Ship> shipsMap,
            HashMap<Integer, Dock> docksMap) {

        Dock newDock;
        Ship newShip = shipsMap.get(newJob.getParent());

        if (newShip != null) {
            newShip.getJobs().add(newJob);
        } else {
            newDock = docksMap.get(newJob.getParent());
            newDock.getShip().getJobs().add(newJob);
        }
    }

    /**
     * This method was tricky to get right. As not all <code>Ship</code> objects are moored at a
     * <code>Dock</code>, the value of <code>getParent()</code> may not correspond to any extant
     * <code>Dock</code> objects. If the ship's <code>parent</code> is not a dock (i.e. is
     * <code>null</code>), then we add it to the all ships <code>ArrayList</code> <code>ships</code>
     * and add it to the queue (<code>getQue()</code>). If the ship is moored, we add it to the all
     * ships listing and set the value of the specific <code>Dock</code>'s <code>getShip()</code> as
     * the ship.
     *
     * @param newship <code>Ship</code>
     * @param docksMap <code>HashMap</code>
     * @param portsMap <code>HashMap</code>
     * return void
     */
    private void addShipToParent(Ship newShip, HashMap<Integer, Dock> docksMap,
            HashMap<Integer, SeaPort> portsMap) {

        SeaPort myPort;
        Dock myDock = docksMap.get(newShip.getParent());

        if (myDock == null) {
            myPort = portsMap.get(newShip.getParent());
            myPort.getShips().add(newShip);
            myPort.getQue().add(newShip);
        } else {
            myPort = portsMap.get(myDock.getParent());
            myDock.setShip(newShip);
            myPort.getShips().add(newShip);
        }
    }

    /**
     * This method is called within the body of <code>SeaPortProgram</code>'s main assembly method,
     * <code>SeaPortProgram.class.readFileContents</code>, to provide the fully assembled
     * <code>DefaultMutableTreeNode</code> representing the complete <code>World</code> instance
     * for use in assembly of a <code>DefaultTreeModel</code>. The complete model is then displayed
     * in a <code>JTree</code> for a more user-friendly means of interacting with the contents of
     * the world.
     *
     * @see java.lang.reflect
     * @param <T> extends <code>Thing</code>
     * @return <code>mainNode</code> The complete <code>DefaultMutableTreeNode</code>
     */
    @SuppressWarnings("unchecked")
    protected <T extends Thing> DefaultMutableTreeNode toTree() {

        // Declarations
        DefaultMutableTreeNode mainNode, parentNode, childNode;
        Method getList;
        HashMap<String, String> classMethodMap;
        ArrayList<T> thingsList;

        // Definitions
        mainNode = new DefaultMutableTreeNode("World");
        classMethodMap = new HashMap<String, String>() {{
            put("Docks", "getDocks");
            put("Ships", "getShips");
            put("Que", "getQue");
            put("Persons", "getPersons");
        }};

        for (SeaPort newPort : this.getPorts()) {
            parentNode = new DefaultMutableTreeNode(newPort.getName() + " (" + newPort.getIndex()
                + ")");
            mainNode.add(parentNode);

            for (HashMap.Entry<String, String> pair : classMethodMap.entrySet()) {
                try {
                    // Reflection-related stuff
                    getList = SeaPort.class.getDeclaredMethod(pair.getValue());
                    thingsList = (ArrayList<T>) getList.invoke(newPort);

                    // Acquire each port's children and add them to the port parent
                    childNode = this.addBranch(pair.getKey(), thingsList);
                    parentNode.add(childNode);
                } catch (
                    NoSuchMethodException |
                    SecurityException |
                    IllegalAccessException |
                    IllegalArgumentException |
                    InvocationTargetException ex
                ) {
                    System.out.println("Error: " + ex);
                }
            }
        }

        return mainNode;
    }

    /**
     * This utility method is invoked via reflection by <code>World.class.toTree</code> to assemble
     * each sub-branch (i.e. persons, ships, que, and docks) of each <code>SeaPort</code>. Based on
     * the class type of each associated <code>ArrayList</code>, additional sub-sub branches may be
     * added as well, including moored ships at <code>Dock</code>s or <code>Job</code>s aboard
     * <code>Ship</code>s.
     *
     * @param <T> extends <code>Thing</code>
     * @param title The <code>String</code> value to be used as the title of the branch
     * @param thingsList Contents of an <code>ArrayList</code>
     * @return <code>parentNode</code> The complete <code>DefaultMutableTreeNode</code>
     */
    private <T extends Thing> DefaultMutableTreeNode addBranch(String title,
            ArrayList<T> thingsList) {

        // Declarations
        String newThingName, childTitle;
        DefaultMutableTreeNode parentNode, childNode;
        Dock thisDock;
        Ship mooredShip, newShip;

        // Definitions
        parentNode = new DefaultMutableTreeNode(title);

        for (T newThing : thingsList) {
            newThingName = newThing.getName() + " (" + newThing.getIndex() + ")";
            childNode = new DefaultMutableTreeNode(newThingName);

            /**
             * In cases of <code>Dock</code>s, the associated moored <code>Ship</code> (if present)
             * is displayed as well as a child node in its own right. Likewise, in cases of
             * <code>Ship</code>s, <code>Job</code>s are displayed as child nodes if they exist.
             */
            if (newThing instanceof Dock) { // Is Dock instance
                thisDock = (Dock) newThing;
                mooredShip = thisDock.getShip();

                if (thisDock.getShip() != null) {
                    childTitle = mooredShip.getName() + " (" + mooredShip.getIndex() + ")";
                    childNode.add(new DefaultMutableTreeNode(childTitle));
                }
            } else if (newThing instanceof Ship) {
                newShip = (Ship) newThing;

                if (!newShip.getJobs().isEmpty()) {
                    for (Job newJob : newShip.getJobs()) {
                        childTitle = newJob.getName();
                        childNode.add(new DefaultMutableTreeNode(childTitle));
                    }
                }
            }

            parentNode.add(childNode);
        }

        return parentNode;
    }

    // Overridden methods

    /**
     * @inheritdoc
     * @return <code>stringOutput</code>
     */
    @Override
    public String toString() {
        String stringOutput = ">>>>> The world:\n";

        for (SeaPort seaPort : this.getPorts()) {
            stringOutput += seaPort.toString() + "\n";
        }
        return stringOutput;
    }
}