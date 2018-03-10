/**
 * SeaPort.java - Class for <code>SeaPort</code> objects
 * Begun 01/15/18
 * @author Andrew Eissen
 */

//package project3;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class represents ports in the world, containing a series of <code>Thing</code> subclass
 * <code>ArrayList</code>s that hold docks, queued ships, all ships, and persons present at the
 * location.
 * <br />
 * <br />
 * A number of <code>synchronized</code> methods for use the in the processing of Project 3's
 * required <code>Job</code> threads were added for use in the final two projects of the class.
 * The new additions basically assist in returning resources to the jockeying <code>Job</code>
 * threads for use in completing their tasks. Only one thread at a time may access the
 * <code>Person</code> worker contents contained within this class's <code>ArrayList</code>s.
 * <br />
 * <br />
 * Class extends <code>Thing</code>
 * @see project3.Thing
 * @author Andrew Eissen
 */
final class SeaPort extends Thing {

    // Rubric-required <code>ArrayList</code>s
    private ArrayList<Dock> docks;
    private ArrayList<Ship> que;
    private ArrayList<Ship> ships;
    private ArrayList<Person> persons;

    /**
     * Parameterized constructor
     * @param scannerContents - Contents of <code>.txt</code> file
     */
    protected SeaPort(Scanner scannerContents) {
        super(scannerContents);
        this.setDocks(new ArrayList<>());
        this.setQue(new ArrayList<>());
        this.setShips(new ArrayList<>());
        this.setPersons(new ArrayList<>());
    }

    // Setters

    /**
     * Setter for <code>docks</code>
     * @param docks <code>ArrayList</code>
     * @return void
     */
    private void setDocks(ArrayList<Dock> docks) {
        this.docks = docks;
    }

    /**
     * Setter for <code>que</code>
     * @param que <code>ArrayList</code>
     * @return void
     */
    private void setQue(ArrayList<Ship> que) {
        this.que = que;
    }

    /**
     * Setter for <code>ships</code>
     * @param ships <code>ArrayList</code>
     * @return void
     */
    private void setShips(ArrayList<Ship> ships) {
        this.ships = ships;
    }

    /**
     * Setter for <code>persons</code>
     * @param persons <code>ArrayList</code>
     * @return void
     */
    private void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }

    // Getters

    /**
     * Getter for <code>docks</code>
     * @return <code>this.dock</code>
     */
    protected ArrayList<Dock> getDocks() {
        return this.docks;
    }

    /**
     * Getter for <code>que</code>
     * @return <code>this.que</code>
     */
    protected ArrayList<Ship> getQue() {
        return this.que;
    }

    /**
     * Getter for <code>ships</code>
     * @return <code>this.ships</code>
     */
    protected ArrayList<Ship> getShips() {
        return this.ships;
    }

    /**
     * Getter for <code>persons</code>
     * @return <code>this.persons</code>
     */
    protected ArrayList<Person> getPersons() {
        return this.persons;
    }

    // Utility methods

    /**
     * This method assembles workers from its <code>ArrayList</code> of <code>Person</code>s,
     * <code>this.persons</code>, that match the skills required by the <code>Job</code>s related to
     * <code>Ship</code>s moored at one of this port's <code>Dock</code>s. Those that match the
     * requirements are flagged as working and passed to the job in question for use therein.
     * <br />
     * <br />
     * Originally, in an attempt to realistically demonstrate the movement of workers from ships
     * back to the worker pool, an <code>Iterator</code> was employed to actually physically remove
     * matching workers from <code>this.persons</code> and move them into the job's
     * <code>Job.workers</code> <code>ArrayList</code>, after which time the entries were readded by
     * <code>this.returnResources</code>. This in some ways simplified the searches for needed
     * entries, as the reduced contents of <code>this.persons</code> reflected the fact that the
     * missing <code>Person</code> entries were off undertaking jobs elsewhere were thus unavailable
     * for use on other jobs.
     * <br />
     * <br />
     * However, the author considered that this method/process, though reflective of reality in some
     * ways, was particularly taxing, requiring constant <code>ArrayList</code> manipulations that
     * could be more readily achieved through the use of a <code>boolean</code> flag attached to
     * each <code>Person</code> instance, namely <code>Person.isWorking</code> Furthermore, this
     * approach was similarly employed in the pseudo-code attached to the Project 3 rubric.
     * <br />
     * <br />
     * The method accepts a reference to the <code>Job</code> invoking this method to gain easier
     * access to the assorted <code>Job</code> attributes and methods that are required for this
     * method.
     *
     * @param job <code>Job</code>, reference to the job in question for easier access to methods
     * @return candidates <code>Person</code> <code>ArrayList</code>
     */
    protected synchronized ArrayList<Person> getResources(Job job) {

        // Declarations
        int counter;
        ArrayList<Person> candidates;
        boolean areAllRequirementsMet;
        String workerLogLine;

        // Definitions
        counter = this.checkForQualifiedWorkers(job.getRequirements());
        candidates = new ArrayList<>();
        areAllRequirementsMet = true;
        workerLogLine = "";

        /**
         * The author has run across cases wherein ships in certain <code>SeaPort</code>s possess
         * <code>Job</code>s that can never be fulfilled due to a lack of qualified dock workers. If
         * no workers in the port, working or otherwise, possess the needed skill required to
         * progress the job to completion, a simple empty <code>ArrayList</code> is returned to keep
         * the job thread from waiting indefinitely.
         * <br />
         * <br />
         * The author is not sure how to fix this, short of importing new workers from other
         * <code>SeaPort</code>s to assist in completing needed jobs on a temporary basis, or
         * perhaps having vessels relocate to other ports in search of locales with proper workers.
         */
        if (counter < job.getRequirements().size()) {
            job.getWorkerLog().append("No qualified workers found for " + job.getName()
                + " (" + job.getParentShip().getName() + ")\n");

            return new ArrayList<>();
        }

        /**
         * Fundamentally, since the job cannot be undertaken without a worker possessing each of the
         * requisite skills, we only allow the method to pass a <code>Person</code> listing to the
         * <code>Job</code> if all requirements are met. If, for example, we need a clerk and an
         * engineer, we cannot return anything to permit the job thread to continue if we have only
         * a clerk who is available for work. We need both to continue with the job.
         */
        outerLoop:
        for (String requirement : job.getRequirements()) {
            for (Person person : this.getPersons()) {

                if (person.getSkill().equals(requirement) && !person.getIsWorking()) {
                    person.setIsWorking(true); // Take worker out of pool for job's duration
                    candidates.add(person);

                    // As soon as we have one candidate, we can go on to the next desired skill
                    continue outerLoop;
                }
            }
            // If no candidate for any one skill found, no point looking for the rest of the skills
            areAllRequirementsMet = false;
            break;
        }

        // Basically a case of logical conjunction; we only return workers if all cases are true
        if (areAllRequirementsMet) {
            workerLogLine += job.getName() + " (" + job.getParentShip().getName() + ") reserving";
            for (int i = 0; i < candidates.size(); i++) {
                if (i == 0) {
                    workerLogLine += " ";
                } else if (i < candidates.size() - 1) {
                    workerLogLine += ", ";
                } else {
                    workerLogLine += " & ";
                }
                workerLogLine += candidates.get(i).getName();
            }
            job.getWorkerLog().append(workerLogLine + "\n");

            return candidates;
        } else {

            /**
             * Like the dining philosophers, job cannot be permitted to hold on to <s>chopsticks</s>
             * workers in preparation for gaining any remaining needed workers. If we don't have
             * all requirements met in the form of available workers, we return the worker to the
             * pool and return with <code>null</code>. The author shamefully admits that he failed
             * to realize this fact for far longer than he cares to admit, and that many tests were
             * run before he realized the associated bug.
             */
            this.returnResources(candidates);
            return null;
        }
    }

    /**
     * This method is used by both the <code>SeaPort</code> and <code>Job</code> classes to
     * symbolically return workers to their respective port resource pools at the conclusion of a
     * <code>Job</code> thread. Though before the method employed <code>ArrayList.addAll()</code>
     * to return the formerly removed <code>SeaPort.persons</code> entries to the listing, the
     * current incarnation more easily sets the <code>boolean</code> flag
     * <code>Person.isWorking</code> to false.
     *
     * @param resources <code>ArrayList</code> of <code>Person</code>s, used on previous job
     * @return void
     */
    protected synchronized void returnResources(ArrayList<Person> resources) {
        resources.forEach((worker) -> {
            worker.setIsWorking(false);
        });
    }

    /**
     * This utility method returns an <code>int</code> value that denotes the number of workers in
     * the current <code>SeaPort</code> who possess the skills required to complete the
     * <code>Job</code> in question. The author dislikes the present implementation, as its use
     * within the body of <code>SeaPort.getResources</code> requires two runs through double-nested
     * for loops in the course of completing that method. Furthermore, this really doesn't do
     * anything to advance the program, apart from noting in the worker's status log that some jobs
     * may be bereft of the required workers and thus are unable to complete their jobs. Perhaps in
     * future, this method can be used to help move ships from port to port in search of qualified
     * workers needed to complete their jobs.
     *
     * @param requirements <code>ArrayList</code>, specifically <code>Job.requirements</code>
     * @return counter <code>int</code>
     */
    private synchronized int checkForQualifiedWorkers(ArrayList<String> requirements) {
        int counter = 0;
        outerLoop:
        for (String requirement : requirements) {
            for (Person worker : this.getPersons()) {
                if (requirement.equals(worker.getSkill())) {
                    counter++;

                    // We only need to know if there is at least one qualified person per skill
                    continue outerLoop;
                }
            }
        }
        return counter;
    }

    // Overridden methods

    /**
     * @inheritdoc
     * @return stringOutput <code>String</code>
     */
    @Override
    public String toString() {
        String stringOutput;

        // A near-identical implementation of the method as denoted in the rubric
        stringOutput = "\n\nSeaPort: " + super.toString();
        for (Dock dock: this.getDocks()) {
            stringOutput += "\n> " + dock.toString();
        }

        stringOutput += "\n\n --- List of all ships in que:";
        for (Ship shipQue: this.getQue()) {
            stringOutput += "\n> " + shipQue.toString();
        }

        // Since the above output displays ship-related details, this one is just a quick summary
        stringOutput += "\n\n --- List of all ships:";
        for (Ship shipAll: this.getShips()) {
            stringOutput += "\n> " + shipAll.getName() + " " + shipAll.getIndex() + " ("
                + shipAll.getClass().getSimpleName() + ")";
        }

        stringOutput += "\n\n --- List of all persons:";
        for (Person person: this.getPersons()) {
            stringOutput += "\n> " + person.toString();
        }

        return stringOutput;
    }
}