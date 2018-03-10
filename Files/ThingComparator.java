/**
 * ThingComparator.java - <code>Comparator</code> class for <code>Thing</code> objects
 * Begun 01/29/18
 * @author Andrew Eissen
 */

//package project3;

import java.util.Comparator;

/**
 * This class, implementing <code>Comparator</code>, is used to compare <code>Thing</code>s based
 * upon their name, or is used to compare <code>Ship</code>s based on weight, height, width, or
 * draft. For name, the default <code>Thing</code>'s own <code>compareTo</code> method is employed
 * to determine sorting by alphabetical order.
 * <br />
 * <br />
 * Class implements <code>Comparator</code>
 * @see java.util.Comparator
 * @author Andrew Eissen
 */
final class ThingComparator implements Comparator<Thing> {

    // Declaration
    private String target;

    /**
     * Parameterized constructor
     * @param attribute <code>String</code>
     */
    protected ThingComparator(String target) {
        this.setTarget(target);
    }

    // Setter

    /**
     * Setter for <code>target</code>
     * @param target <code>String</code>
     * @return void
     */
    private void setTarget(String target) {
        this.target = target;
    }

    // Getter

    /**
     * Getter for <code>target</code>
     * @return <code>this.target</code>
     */
    protected String getTarget() {
        return this.target;
    }

    /**
     * @inheritdoc
     * @param thing1 <code>Thing</code> instance 1
     * @param thing2 <code>Thing</code> instance 2
     * @return<code>int</code>, either 0, 1, -1
     */
    @Override
    public int compare(Thing thing1, Thing thing2) {

        switch (this.getTarget()) {
            case "By weight":
                if (((Ship) thing1).getWeight() == ((Ship) thing2).getWeight()) {
                    return 0;
                } else if (((Ship) thing1).getWeight() > ((Ship) thing2).getWeight()) {
                    return 1;
                } else {
                    return -1;
                }
            case "By length":
                if (((Ship) thing1).getLength() == ((Ship) thing2).getLength()) {
                    return 0;
                } else if (((Ship) thing1).getLength() > ((Ship) thing2).getLength()) {
                    return 1;
                } else {
                    return -1;
                }
            case "By draft":
                if (((Ship) thing1).getDraft() == ((Ship) thing2).getDraft()) {
                    return 0;
                } else if (((Ship) thing1).getDraft() > ((Ship) thing2).getDraft()) {
                    return 1;
                } else {
                    return -1;
                }
            case "By width":
                if (((Ship) thing1).getWidth() == ((Ship) thing2).getWidth()) {
                    return 0;
                } else if (((Ship) thing1).getWidth() > ((Ship) thing2).getWidth()) {
                    return 1;
                } else {
                    return -1;
                }
            case "By name":
                return thing1.getName().compareTo(thing2.getName());
            default:
                return -1;
        }
    }
}