/**
 * SeaPortProgram.java - Constructs the main interface
 * Begun 01/15/18
 * @author Andrew Eissen
 */

//package project3;

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.tree.TreeSelectionModel;

/**
 * This is the central class of the program. It initializes the program and assembles the GUI and
 * includes click handlers related to the three major buttons, "Read," "Search," and "Sort." It
 * contains a <code>private</code> instance of the <code>World</code> class which is built from the
 * <tt>.txt</tt> file selected by the user.
 * <br />
 * <br />
 * Class extends <code>JFrame</code>
 * @see javax.swing.JFrame
 * @author Andrew Eissen
 */
final class SeaPortProgram extends JFrame {

    // New world instance
    private World world;

    // Window-related fields
    private String title;
    private int width;
    private int height;

    // GUI related fields
    private JFrame mainFrame;
    private JTree mainTree;
    private JTextArea searchResultsTextArea, jobsStatusTextArea, jobsPoolTextArea;
    private JScrollPane treeScrollPane, searchResultsScrollPane, jobsScrollPane,
        jobsStatusScrollPane, jobsPoolScrollPane;
    private JPanel mainPanel, optionsPanel, worldPanel, treePanel, treeButtonsPanel, jobsPanel,
        jobsScrollPanePanel, jobsLogsPanel;
    private JButton readButton, searchButton, sortButton, treeDetailsButton, treeExpandButton,
        treeCollapseButton;
    private JLabel searchTextLabel, sortTextLabel;
    private JTextField searchTextField;
    private String[] searchComboBoxValues, sortPortComboBoxValues, sortTargetComboBoxValues,
        sortTypeComboBoxValues;
    private JComboBox<String> searchComboBox, sortPortComboBox, sortTargetComboBox,
        sortTypeComboBox;

    // User input-related fields
    private JFileChooser fileChooser;
    private Scanner scannerContents;

    /**
     * Default, no-parameters constructor
     */
    protected SeaPortProgram() {
        super("SeaPortProgram");
        this.setWindowTitle("SeaPortProgram");
        this.setWindowWidth(1280);
        this.setWindowHeight(720);
    }

    /**
     * Fully-parameterized constructor, accepting size parameters and title
     * @param title <code>String</code>
     * @param width <code>int</code>
     * @param height <code>int</code>
     */
    protected SeaPortProgram(String title, int width, int height) {
        super(title);
        this.setWindowTitle(title);
        this.setWindowWidth(width);
        this.setWindowHeight(height);
    }

    /**
     * Setter for <code>title</code>
     * @param title <code>String</code>
     * @return void
     */
    private void setWindowTitle(String title) {
        this.title = title;
    }

    /**
     * Setter for <code>width</code>
     * @param width <code>int</code>
     * @return void
     */
    private void setWindowWidth(int width) {
        if (width < 1280) {
            this.width = 1280;
        } else {
            this.width = width;
        }
    }

    /**
     * Setter for <code>height</code>
     * @param height <code>int</code>
     * @return void
     */
    private void setWindowHeight(int height) {
        if (height < 720) {
            this.height = 720;
        } else {
            this.height = height;
        }
    }

    /**
     * Getter for <code>title</code>
     * @return this.title
     */
    protected String getWindowTitle() {
        return this.title;
    }

    /**
     * Getter for <code>width</code>
     * @return this.width
     */
    protected int getWindowWidth() {
        return this.width;
    }

    /**
     * Getter for <code>height</code>
     * @return this.height
     */
    protected int getWindowHeight() {
        return this.height;
    }

    /**
     * The GUI was refactored significantly during the course of developing the third project. As
     * the progression of <code>Job</code> threads took center stage, the GUI was redesigned from
     * the bottom up to ensure that all <code>Job</code>-related panels and areas were visually
     * front and center across the interface. The former three-panel design that displayed a partial
     * <code>JTree</code>, <code>toString()</code> area, and search/sort log was scrapped, with the
     * <code>toString()</code> implementation removed due to lack of space and an expansion of the
     * tree that made the retaining of the panel redundant. The search/sort log was moved beneath
     * the tree to the <code>BorderLayout.WEST</code> position, and the rest of the GUI was reserved
     * for the job threads and their associated <code>JPanel</code>s.
     *
     * @return void
     */
    private void constructGUI() {

        this.mainPanel = new JPanel(new BorderLayout());                   // Complete super-panel
        this.optionsPanel = new JPanel(new GridLayout(1, 10, 5, 5));       // Upper user UI navbar
        this.worldPanel = new JPanel(new GridLayout(2, 1, 5, 5));          // Left panel with JTree
        this.treePanel = new JPanel(new BorderLayout());                   // All JTree things
        this.treeButtonsPanel = new JPanel(new GridLayout(1, 3, 10, 10));  // JTree action buttons
        this.jobsPanel = new JPanel(new GridLayout(2, 1, 5, 5));           // All job-related stuff
        this.jobsScrollPanePanel = new JPanel(new GridLayout(0, 1));       // Main panel for threads
        this.jobsLogsPanel = new JPanel(new GridLayout(1, 2, 5, 5));       // Container for 2 logs

        /**
         * UPPER OPTIONS NAVBAR ASSEMBLY
         */

        // Create buttons for options menu
        this.readButton = new JButton("Read");
        this.searchButton = new JButton("Search");
        this.sortButton = new JButton("Sort");

        // Search related definitions
        this.searchTextField = new JTextField("", 10);
        this.searchTextLabel = new JLabel("Search:", JLabel.RIGHT);

        // Sort related definitions
        this.sortTextLabel = new JLabel("Sort:", JLabel.RIGHT);

        /**
         * Combo box definitions, using <code>String</code> array idea from
         * <a href="www.codejava.net/java-se/swing/jcombobox-basic-tutorial-and-examples">this</a>.
         */
        this.sortPortComboBoxValues = new String[] {
            "All ports"
        };
        this.sortPortComboBox = new JComboBox<>(this.sortPortComboBoxValues);

        this.searchComboBoxValues = new String[] {
            "By name",
            "By index",
            "By skill"
        };
        this.searchComboBox = new JComboBox<>(this.searchComboBoxValues);

        this.sortTargetComboBoxValues = new String[] {
            "Que",
            "Ships",
            "Docks",
            "Persons",
            "Jobs"
        };
        this.sortTargetComboBox = new JComboBox<>(this.sortTargetComboBoxValues);

        this.sortTypeComboBoxValues = new String[] {
            "By name",
            "By weight",
            "By length",
            "By width",
            "By draft"
        };
        this.sortTypeComboBox = new JComboBox<>(this.sortTypeComboBoxValues);

        // Add UI options to top panels
        this.optionsPanel.add(this.readButton);         // Read button first
        this.optionsPanel.add(this.searchTextLabel);    // Search label "Search:"
        this.optionsPanel.add(this.searchTextField);    // Search bar itself
        this.optionsPanel.add(this.searchComboBox);     // Sorting options combo box
        this.optionsPanel.add(this.searchButton);       // Search button itself
        this.optionsPanel.add(this.sortTextLabel);      // Sort label "Sort:"
        this.optionsPanel.add(this.sortPortComboBox);   // Sort SeaPort selection
        this.optionsPanel.add(this.sortTargetComboBox); // Sort what selection box
        this.optionsPanel.add(this.sortTypeComboBox);   // Sort by selection box
        this.optionsPanel.add(this.sortButton);         // Sort button itself

        /**
         * LEFT PANEL ASSEMBLY - JTREE
         */

        // JTree results object
        this.mainTree = new JTree();
        this.mainTree.setModel(null);
        this.mainTree.getSelectionModel().setSelectionMode(
            TreeSelectionModel.SINGLE_TREE_SELECTION);

        // Add JTree to JScrollPane as above
        this.treeScrollPane = new JScrollPane(this.mainTree);

        // Add JTree buttons
        this.treeExpandButton = new JButton("Expand all");
        this.treeCollapseButton = new JButton("Collapse all");
        this.treeDetailsButton = new JButton("More info");

         // Add JTree buttons elements
        this.treeButtonsPanel.add(this.treeExpandButton);       // Expand all nodes
        this.treeButtonsPanel.add(this.treeCollapseButton);     // Collapse all nodes
        this.treeButtonsPanel.add(this.treeDetailsButton);      // Button for details on selection

        // Add actual tree itself and options buttons to panel
        this.treePanel.add(this.treeScrollPane, BorderLayout.CENTER);
        this.treePanel.add(this.treeButtonsPanel, BorderLayout.SOUTH);

        // Search results JTextArea
        this.searchResultsTextArea = new JTextArea();
        this.searchResultsTextArea.setEditable(false);
        this.searchResultsTextArea.setFont(new Font("Monospaced", 0, 12));
        this.searchResultsTextArea.setLineWrap(true);

        // Add searchResultsTextArea to JScrollPane
        this.searchResultsScrollPane = new JScrollPane(this.searchResultsTextArea);

        // Assemble Panel
        this.worldPanel.add(this.treePanel);
        this.worldPanel.add(this.searchResultsScrollPane);

        /**
         * RIGHT PANEL ASSEMBLY - JOBS, ETC
         */

        // Jobs status logs area
        this.jobsStatusTextArea = new JTextArea();
        this.jobsStatusTextArea.setEditable(false);
        this.jobsStatusTextArea.setFont(new Font("Monospaced", 0, 11));
        this.jobsStatusTextArea.setLineWrap(true);

        // Jobs status logs area
        this.jobsPoolTextArea = new JTextArea();
        this.jobsPoolTextArea.setEditable(false);
        this.jobsPoolTextArea.setFont(new Font("Monospaced", 0, 11));
        this.jobsPoolTextArea.setLineWrap(true);

        // Add searchResultsTextArea to JScrollPane
        this.jobsPoolScrollPane = new JScrollPane(this.jobsPoolTextArea);
        this.jobsStatusScrollPane = new JScrollPane(this.jobsStatusTextArea);
        this.jobsScrollPane = new JScrollPane(this.jobsScrollPanePanel);

        // Add search log and job status log to panel
        this.jobsLogsPanel.add(this.jobsStatusScrollPane);

        this.jobsLogsPanel.add(this.jobsStatusScrollPane);
        this.jobsLogsPanel.add(this.jobsPoolScrollPane);
        this.jobsPanel.add(this.jobsScrollPane);
        this.jobsPanel.add(this.jobsLogsPanel);

        /**
         * MAJOR COMPONENT ASSEMBLY
         */

        this.mainPanel.add(this.optionsPanel, BorderLayout.NORTH);
        this.mainPanel.add(this.worldPanel, BorderLayout.WEST);
        this.mainPanel.add(this.jobsPanel, BorderLayout.CENTER);

        // Add borders for a cleaner look
        this.optionsPanel.setBorder(BorderFactory.createTitledBorder("Options"));
        this.treePanel.setBorder(BorderFactory.createTitledBorder("World tree"));
        this.jobsScrollPane.setBorder(BorderFactory.createTitledBorder("Jobs Listing"));
        this.searchResultsScrollPane.setBorder(BorderFactory.createTitledBorder("Search/sort log"));
        this.jobsStatusScrollPane.setBorder(BorderFactory.createTitledBorder("Job status log"));
        this.jobsPoolScrollPane.setBorder(BorderFactory.createTitledBorder("Job resource pool"));
        this.mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // JTextArea borders
        this.jobsStatusTextArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        this.jobsPoolTextArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        this.searchResultsTextArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // Added for uniform white background/gray border look to all panels
        this.jobsScrollPanePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        this.jobsScrollPanePanel.setBackground(Color.WHITE);

        // Sorting target JComboBox event listener
        this.sortTargetComboBox.addActionListener((ActionEvent e) -> {
            this.provideProperSortOptions();
        });

        // Read button handler
        this.readButton.addActionListener((ActionEvent e) -> {
            this.readFileContents();
        });

        // Search button handler
        this.searchButton.addActionListener((ActionEvent e) -> {
            this.searchWorldContents();
        });

        // Sort button handler
        this.sortButton.addActionListener((ActionEvent e) -> {
            this.sortWorldContents();
        });

        // Expand nodes button handler
        this.treeExpandButton.addActionListener((ActionEvent e) -> {
            this.toggleNodes("expandRow");
        });

        // Collapse nodes button handler
        this.treeCollapseButton.addActionListener((ActionEvent e) -> {
            this.toggleNodes("collapseRow");
        });

        // JTree details button handler
        this.treeDetailsButton.addActionListener((ActionEvent e) -> {
            this.displaySelectionDetails();
        });

        // Placement/sizing details for main JFrame element
        this.mainFrame = new JFrame(this.getWindowTitle());
        this.mainFrame.setContentPane(this.mainPanel);
        this.mainFrame.setSize(this.getWindowWidth(), this.getWindowHeight());
        this.mainFrame.setVisible(true);
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * One of two such methods, this handler deals with <code>this.sortTypeComboBox</code>
     * <code>JComboBox</code> options changes based upon the selected value of
     * <code>this.sortTargetComboBox</code>. Only searches of queues permit the inclusion of the
     * four options weight, width, length, and draft.
     *
     * @return void
     */
    private void provideProperSortOptions() {
        this.sortTypeComboBox.removeAllItems();
        this.sortTypeComboBox.addItem("By name");
        if (this.sortTargetComboBox.getSelectedIndex() == 0) { // Is que == true
            this.sortTypeComboBox.addItem("By weight");
            this.sortTypeComboBox.addItem("By width");
            this.sortTypeComboBox.addItem("By length");
            this.sortTypeComboBox.addItem("By draft");
        }
    }

    /**
     * The second of two such similar methods, this handler is run after the creation of the class's
     * <code>World</code> instance, sorting the world's <code>SeaPort</code> objects by name and
     * adding their <code>Thing.class.getName</code> values as options in the
     * <code>this.sortPortComboBox</code> <code>JComboBox</code>.
     *
     * @return void
     */
    private void provideProperSeaPortSortOptions() {
        this.sortPortComboBox.removeAllItems();
        this.sortPortComboBox.addItem("All ports");
        Collections.sort(this.world.getPorts(), new ThingComparator("By name"));
        if (this.world.getPorts().size() > 1) {
            for (SeaPort newPort : this.world.getPorts()) {
                this.sortPortComboBox.addItem(newPort.getName());
            }
        }
    }

    /**
     * This method serves as the click handler for presses of the "Read" button. As per the project
     * design rubric, the method employs <code>JFileChooser</code> to open a file selection dialog
     * popup for the user to select the proper file. A <code>.txt</code> files-only filter was
     * applied to fix a bug the author discovered that would allow the program to run files of any
     * extension. Since the input will be a text file, such a restriction was deemed sensible.
     * Related to this was the subsequent restriction forbidding improperly-formatted text files
     * from erroneously being run.
     *
     * @return void
     */
    private void readFileContents() {

        // Declarations
        int selection;
        FileReader fileReader;
        FileNameExtensionFilter filter;

        // Main folder, as per rubric
        this.fileChooser = new JFileChooser(".");

        /**
         * Addition of <code>.txt</code> file-only filter, as per the answer on the following page:
         * <br />
         * <a href=
         * "//stackoverflow.com/questions/15771949/how-do-i-make-jfilechooser-only-accept-txt"
         * >See here</a>
         */
        filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        this.fileChooser.setFileFilter(filter);

        selection = this.fileChooser.showOpenDialog(new JFrame());

        if (selection == JFileChooser.APPROVE_OPTION) {
            try {
                fileReader = new FileReader(this.fileChooser.getSelectedFile());
                this.scannerContents = new Scanner(fileReader);
            } catch (FileNotFoundException ex) {
                this.displayErrorPopup("Error: No such file found. Please try again.");
            }
        }

        // Important to ensure that all jobs threads of possible former worlds are hard-stopped
        if (this.world != null) {
            this.clearAllJobs();
        }

        this.world = new World(this.scannerContents);

        // Forbid users from using a text file that is not in the proper format
        if (this.world.getAllThings().isEmpty()) {
            this.jobsStatusTextArea.setText("");
            this.jobsPoolTextArea.setText("");
            this.searchResultsTextArea.setText("");
            this.mainTree.setModel(null);
            this.clearAllJobs();
            this.world = null;
            this.displayErrorPopup("Error: File data may be empty or corrupted. Please try again.");
        } else {
            this.jobsStatusTextArea.setText("");
            this.jobsPoolTextArea.setText("");
            this.searchResultsTextArea.setText(""); // Clear past searches of old Worlds, etc
            this.mainTree.setModel(new DefaultTreeModel(this.world.toTree()));
            this.provideProperSeaPortSortOptions(); // Adds names of ports for specific sorting
            this.startAllJobs();
        }
    }

    /**
     * Method serves as the handler for clicks of the "Search" button. The method retrieves the
     * user-inputted values for <code>this.searchTextField</code> and
     * <code>this.searchComboBox</code> and makes evaluation decisions based on these options. The
     * author believes this would be far easier to implement with a set of <code>HashMap</code>s,
     * but in the interest of preserving professor sanity, simpler, less effective methods have been
     * employed instead.
     * <br />
     * <br />
     * Initially, there was a fair bit of code repetition contained within the body of the method's
     * following <code>switch</code> statement, much of which was reduced and moved into a few
     * individual methods for optimized reuse. As the operations of both selection by name and also
     * by index are basically the same, they were consolidated into a single operation that
     * passes their index to <code>assembleResults</code>. The index value ultimately determines
     * which method to be used for comparison, either <code>Thing.class.getIndex</code> or else
     * <code>Thing.class.getName</code>.
     * <br />
     * <br />
     * Skill is special, so it gets its own handler within the <code>switch</code> statement, seeing
     * as skills are encapsulated within <code>Person</code>s and not included within
     * <code>World.class.allThings</code>.
     *
     * @return void
     */
    private void searchWorldContents() {

        // Prevent users seeking to hit the search button before building da world
        if (this.world == null || this.scannerContents == null) {
            this.displayErrorPopup("Error: No world initialized. Please try again.");
            return;
        }

        // Declaration
        String resultsString, searchText;
        int dropdownSelection;

        // Definitions
        resultsString = "";
        searchText = this.searchTextField.getText().trim();
        dropdownSelection = this.searchComboBox.getSelectedIndex();

        // Catch users hitting the Search button without entering any content, sneaky buggers
        if (searchText.equals("")) {
            this.displayErrorPopup("Error: No search terms included. Please try again.");
            return;
        }

        // See discussion above for more details
        switch(dropdownSelection) {
            case 0: // By name
            case 1: // By index
                resultsString = this.assembleResults(dropdownSelection, searchText);
                this.displayStatus(resultsString, searchText);
                break;
            case 2: // By skill
                for (SeaPort port : this.world.getPorts()) {
                    for (Person person : port.getPersons()) {
                        if (person.getSkill().equals(searchText)) {
                            resultsString += "> " + person.getName() + " (id #" + person.getIndex()
                                + ")\n";
                        }
                    }
                }
                this.displayStatus(resultsString, searchText);
                break;
            default:
                break;
        }
    }

    /**
     * This method was added to minimize code repetition and inefficiency within the
     * <code>switch</code> statement of the preceeding method, <code>searchWorldContents</code>.
     * <br />
     * <br />
     * Initially, the author employed a ternary operation within the body of the <code>for</code>
     * loop to select which method to use to compare with the value of <code>target</code>. However,
     * to reduce the need to select the same operation each time when the operation is already known
     * the first time, the author removed this and replaced it in favor of reflection, choosing the
     * desired method before the loop and invoking that method as desired within. This removed the
     * needless <code>if</code> operation and slightly improved efficiency.
     *
     * @see java.lang.reflect
     * @param index The value of <code>this.searchComboBox.getSelectedIndex()</code>
     * @param target The search term inputted by the user
     * @return resultsString The assembled <code>String</code> of search values
     */
    private String assembleResults(int index, String target) {

        // Declarations/definitions
        Method getParam;
        String parameter, methodName;
        String resultsString = "";

        /**
         * Compare 1 ternary operation per method invocation vs
         * <code>World.class.allThings.size()</code> operations per method invocation. Thank the
         * Java gods for reflection.
         */
        methodName = (index == 0) ? "getName" : "getIndex";

        try {
            // Either Thing.class.getName or Thing.class.getIndex
            getParam = Thing.class.getDeclaredMethod(methodName);

            for (Thing item : this.world.getAllThings()) {

                // Hacky? Yes. But it leaves Strings alone and converts Integers to String
                parameter = "" + getParam.invoke(item);

                if (parameter.equals(target)) {
                    resultsString += "> " + item.getName() + " " + item.getIndex() + " ("
                        + item.getClass().getSimpleName() + ")\n";
                }
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
        return resultsString;
    }

    /**
     * This method originally displayed a <code>JOptionPane</code> window but was changed to make
     * use of Project 2's search results <code>JTextArea</code> to better track and log searches and
     * sorts of world data.
     * <br />
     * <br />
     * If the <code>String</code> is empty, that means there
     * is no matching input, and thus an error message variation is displayed. Otherwise, the
     * <code>String</code> is displayed as the results in a success message.
     *
     * @param resultsString The <code>String</code> representing the search results
     * @param target The search term itself, also a <code>String</code>
     * @return void
     */
    private void displayStatus(String resultsString, String target) {
        if (resultsString.equals("")) {
            this.searchResultsTextArea.append("Warning: '" + target + "' not found.\n\n");
        } else {
            this.searchResultsTextArea.append("Search results for '" + target + "'\n"
                + resultsString + "\n");
        }
    }

    /**
     * This method serves as the main sorting handler for clicks of the sort button. As per the
     * Project 2 rubric, all sortable <code>ArrayList</code>s are included as options, and sorts are
     * performed by specific port or globally, depending on option selected. Reflection is used
     * to invoke the name of a method that will be invoked to display relevant data pertaining to
     * search (i.e. when users search by weight, the sorted values will be displayed with each value
     * of <code>Ship.class.getWeight</code> displayed in parentheses), as well as to determine which
     * <code>SeaPort</code> <code>ArrayList</code> to get for purposes of adding its contents to
     * <code>thingsList</code> for sorting purposes.
     * <br />
     * <br />
     * The method originally was split into two, wherein a <code>switch</code> statement was used to
     * determine which <code>SeaPort</code> <code>ArrayList</code> to acquire, though after several
     * subsequent revisions and reinterpretations of the desired end the method were reconciled into
     * one.
     *
     * @see java.lang.reflect
     * @return void
     */
    @SuppressWarnings("unchecked")
    private void sortWorldContents() {

        // Prevent users seeking to hit the search button before building da world
        if (this.world == null || this.scannerContents == null) {
            this.displayErrorPopup("Error: No world initialized. Please try again.");
            return;
        }

        // Declarations
        String sortPort, sortTarget, sortType, result, fieldMethodName, listMethodName;
        Method getField, getList;
        ArrayList<Thing> thingsList, gottenList;
        HashMap<String, String> typeMethodMap, targetMethodMap;

        /**
         * It's a shame in the author's opinion that there is no static/predefined
         * <code>HashMap</code> data structure the way there is in JavaScript (i.e. JS objects,
         * object literals, etc. that are frequently used in i18n text substitution operations). As
         * such, the author used the following method to populate <code>HashMap</code>s that get
         * method names for use in acquiring relevant fields or <code>ArrayList</code>s.
         */
        typeMethodMap = new HashMap<String, String>() {{
            put("By name", "getIndex");
            put("By weight", "getWeight");
            put("By length", "getLength");
            put("By width", "getWidth");
            put("By draft", "getDraft");
        }};

        targetMethodMap = new HashMap<String, String>() {{
            put("Que", "getQue");
            put("Ships", "getShips");
            put("Docks", "getDocks");
            put("Persons", "getPersons");
            put("Jobs", "getShips"); // Gotta get ships because of port/ship/job nesting
        }};

        // Definitions
        sortPort = this.sortPortComboBox.getSelectedItem().toString();
        sortTarget = this.sortTargetComboBox.getSelectedItem().toString();
        sortType = this.sortTypeComboBox.getSelectedItem().toString();
        fieldMethodName = typeMethodMap.get(sortType);
        listMethodName = targetMethodMap.get(sortTarget);
        result = "";
        thingsList = new ArrayList<>();

        try {
            getList = SeaPort.class.getDeclaredMethod(listMethodName);

            if (sortTarget.equals("Que") && !sortType.equals("By name")) {
                getField = Ship.class.getDeclaredMethod(fieldMethodName);
            } else {
                getField = Thing.class.getDeclaredMethod(fieldMethodName);
            }

            if (sortPort.equals("All ports")) {
                sortPort = sortPort.toLowerCase();
                for (SeaPort newPort : world.getPorts()) {
                    gottenList = (ArrayList<Thing>) getList.invoke(newPort);
                    thingsList.addAll(gottenList);
                }
            } else {
                for (SeaPort newPort : this.world.getPorts()) {
                    if (newPort.getName().equals(sortPort)) {
                        gottenList = (ArrayList<Thing>) getList.invoke(newPort);
                        thingsList.addAll(gottenList);
                    }
                }
            }

            /**
             * This is admittedly a bit of a
             * <span style="color:yellow;font-family:'Comic Sans MS';">jAnKeD</span> means of
             * dealing with the nested nature of ship-based jobs. It was something of an
             * afterthought on the part of the author, who sought to construct the method in the
             * cleanest manner possible but forgot about jobs in the process, leading to the need
             * to integrate a messy but working means of getting all port-specific jobs. Hopefully
             * in future, this functionality can be handled more gracefully and efficiently.
             * <br />
             * <br />
             * <code>thingsList</code> should be composed of <code>Ship</code>s. For each, we add
             * the specific jobs to the <code>jobsList</code>, then clear <code>thingsList</code>
             * and replace its contents with <code>jobsList</code>, moving on to the sorting.
             */
            if (sortTarget.equals("Jobs")) {
                ArrayList<Job> jobsList = new ArrayList<>();

                for (Iterator<Thing> iterator = thingsList.iterator(); iterator.hasNext();) {
                    Ship newShip = (Ship) iterator.next();
                    for (Job newJob : newShip.getJobs()) {
                        jobsList.add(newJob);
                    }
                }
                thingsList.clear(); // Remove all remaining Ship instances
                thingsList.addAll(jobsList); // Replace with Jobs
            }

            // Catch cases wherein there are no appropriate results
            if (thingsList.isEmpty()) {
                result += "> No results found.\n";
            } else {
                // Sort through the collected results
                Collections.sort(thingsList, new ThingComparator(sortType));

                // Assemble results, displaying relevant data beside the entry in parentheses
                for (Thing newThing : thingsList) {
                    result += "> " + newThing.getName() + " (" + getField.invoke(newThing) + ")\n";
                }
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

        // Format searches through addition of readable header
        this.searchResultsTextArea.append("Sort results for '" + sortTarget + " "
            + sortType.toLowerCase() + " in " + sortPort + "'\n" + result + "\n");
    }

    /**
     * This method was originally used as a one-size-fits-all handler for a single
     * <code>JToggleButton</code> approach that changed the text of the button depending on whether
     * or not the user had expanded/collapsed the nodes on the <code>JTree</code>. However, in the
     * course of testing, the author discovered that if the user were to manually expand much of the
     * tree in the course of exploring the nodes and desired to collapse the tree, the button would
     * have to be pressed twice, first to expand the remainder, then to collapse them all. Rather
     * than deal with this inconvenience, the author reverted to the use of a pair of buttons.
     * <br />
     * <br />
     * As with many of the methods contained herein, reflection was employed to reduce the
     * repetition that would otherwise result from having slight differences in near-identical
     * click handlers.
     *
     * @param methodName <code>String</code> - <code>expandRow</code> or <code>collapseRow</code>
     * @return void
     */
    private void toggleNodes(String methodName) {

        // Prevent users seeking to hit the search button before building da world
        if (this.world == null || this.scannerContents == null) {
            this.displayErrorPopup("Error: No world initialized. Please try again.");
            return;
        }

        // Declaration
        Method toggleRow;

        try {
            toggleRow = JTree.class.getDeclaredMethod(methodName, Integer.TYPE);

            for (int i = 0; i < this.mainTree.getRowCount(); i++) {
                toggleRow.invoke(this.mainTree, i);
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
     * This method serves as the click handler for the <code>this.treeDetailsButton</code>. It grabs
     * the selected <code>JTree</code> node and assembles a readable <code>JTable</code> entry
     * containing relevant information, displaying the table in a <code>JOptionPane</code>. For all
     * <code>Ship</code> subclass entries, the details of that ship's essential construction are
     * displayed along with the default <code>name</code> and <code>index</code> values that are
     * standard to all <code>Thing</code>s.
     *
     * @return void
     */
    private void displaySelectionDetails() {

        /**
         * Handle all improper button use options, listed in order:
         * 1) If the user hasn't built the world or if the scanner doesn't exist
         * 2) If the user hasn't selected a <code>JTree</code> option
         * 3) If the user has intentionally selected a simple navigation header rather than node
         */
        if (this.world == null || this.scannerContents == null) {
            this.displayErrorPopup("Error: No world initialized. Please try again.");
            return;
        } else if (this.mainTree.getLastSelectedPathComponent() == null) {
            this.displayErrorPopup("Error: Please select an element from the tree above.");
            return;
        } else if (Arrays.asList(new String[] {"Que", "Docks", "Ships", "Persons", "World"})
                .contains(this.mainTree.getLastSelectedPathComponent().toString())) {
            this.displayErrorPopup("Error: Improper selection. Please select a different node.");
            return;
        }

        // Declarations
        String userSelection;
        JTable resultsTable;
        HashMap<String, String> defaultValues, shipValues, passengerShipValues,
            cargoShipValues, personValues;
        LinkedHashMap<String, String> applicableFields; // Could also be TreeMap
        Object[][] results;
        String[] columnNames, nameIndexArray;
        int counter;

        // Definitions
        userSelection = this.mainTree.getLastSelectedPathComponent().toString();
        nameIndexArray = userSelection.split(" ");
        userSelection = nameIndexArray[0].trim();
        applicableFields = new LinkedHashMap<>();
        columnNames = new String[] {"Field", "Value"};
        counter = 0;

        // Thing.class values - all items display these
        defaultValues = new HashMap<String, String>() {{
            put("ID", "getIndex");
            put("Name", "getName");
        }};

        // Ship.class values - all Ship subclasses display these
        shipValues = new HashMap<String, String>() {{
            put("Weight", "getWeight");
            put("Length", "getLength");
            put("Width", "getWidth");
            put("Draft", "getDraft");
        }};

        // PassengerShip.class values
        passengerShipValues = new HashMap<String, String>() {{
            put("Total rooms", "getNumberOfRooms");
            put("Occupied rooms", "getNumberOfOccupiedRooms");
            put("Passengers", "getNumberOfPassengers");
        }};

        // CargoShip.class values
        cargoShipValues = new HashMap<String, String>() {{
            put("Cargo volume", "getCargoVolume");
            put("Cargo value", "getCargoValue");
            put("Cargo weight", "getCargoWeight");
        }};

        // Person.class values (may expand in future to include location)
        personValues = new HashMap<String, String>() {{
            put("Occupation", "getSkill");
        }};

        /**
         * Basically, this <code>for</code> loop searches for a match by comparing names, taking the
         * above <code>HashMap</code>s and invoking <code>this.constructResults</code> to convert
         * their values from a <code>String</code> method name to a <code>String.valueOf</code>
         * representation of the method's returned result. The resultant <code>HashMap</code>s are
         * all themselves smashed together into the <code>applicableFields</code>
         * <code>LinkedHashMap</code> for optimal display purposes.
         */
        for (Thing newThing : this.world.getAllThings()) {
            if (newThing.getName().equals(userSelection)) {
                applicableFields.putAll(this.constructResults(newThing, Thing.class,
                    defaultValues));

                if (newThing instanceof Ship) {
                    applicableFields.putAll(this.constructResults(newThing, Ship.class,
                        shipValues));

                    if (newThing instanceof PassengerShip) {
                        applicableFields.putAll(this.constructResults(newThing, PassengerShip.class,
                            passengerShipValues));
                    } else if (newThing instanceof CargoShip) {
                        applicableFields.putAll(this.constructResults(newThing, CargoShip.class,
                            cargoShipValues));
                    }
                } else if (newThing instanceof Person) {
                    applicableFields.putAll(this.constructResults(newThing, Person.class,
                        personValues));
                }
            }
        }

        // Simple conversion process from LinkedHashMap to Object[][]
        results = new Object[applicableFields.size()][2];
        for (HashMap.Entry<String,String> entry : applicableFields.entrySet()) {
            results[counter][0] = entry.getKey();
            results[counter][1] = entry.getValue();
            counter++;
        }

        // Assemble JTable with results
        resultsTable = new JTable(results, columnNames);
        resultsTable.setPreferredScrollableViewportSize(resultsTable.getPreferredSize());
        resultsTable.setFillsViewportHeight(true);

        // Display JTable within a JOptionPane (experiment with JPopupMenu mayhaps?)
        JOptionPane.showMessageDialog(this.mainFrame, new JScrollPane(resultsTable), userSelection,
            JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * This conversion utility method takes a <code>String, String</code> <code>HashMap</code> entry
     * composed of display text and method names and returns a modified <code>String</code>
     * <code>HashMap</code> composed of display text and the <code>String.valueOf</code> resultant
     * representation from the invocation of the method in question.
     *
     * @param <T extends Thing>
     * @param newThing Can be <code>Thing</code> or <code>Ship</code> subclasses
     * @param className The class to which the methods in the <code>HashMap</code> belong.
     * @param values <code>HashMap</code> of associated text and method names
     * @return resultsMap <code>HashMap</code>
     */
    @SuppressWarnings("unchecked")
    private <T extends Thing> HashMap<String, String> constructResults(T newThing,
            Class className, HashMap<String, String> values) {

        // Declarations
        HashMap<String, String> resultsMap;
        Method getAttribute;

        // Definitions
        resultsMap = new HashMap<>();

        try {
            for (HashMap.Entry<String, String> row : values.entrySet()) {

                // Declarations
                String displayText, methodName, methodResult;
                Object getAttributeResult;

                // Definitions
                displayText = row.getKey();
                methodName = row.getValue();

                // Method related definitions and invocations
                getAttribute = className.getDeclaredMethod(methodName);
                getAttributeResult = getAttribute.invoke(newThing);

                /**
                 * Bit of a janky hack to handle the fact that <code>Thing.class.getName</code> is
                 * the only included method to not return either an <code>int</code> or
                 * <code>double</code> and thus does not need a <code>valueOf</code> representation.
                 */
                if (getAttributeResult instanceof String) {
                    methodResult = (String) getAttributeResult;
                } else {
                    methodResult = String.valueOf(getAttributeResult);
                }

                resultsMap.put(displayText, methodResult);
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

        return resultsMap;
    }

    /**
     * This method is called from with the body of the <code>readFileContents</code> method and is
     * used to start each of the threads pertaining to each <code>Job</code>. Rather than start each
     * thread as it is read from the data file, a method which could easily result in some janky
     * hijinks if a job is completed before the file is completely read, the threads themselves are
     * created as their <code>Job</code> objects are created, but aren't started until the file has
     * been completely read. This procedure was suggested by the website handout included in the
     * Project 3 design rubric; "An alternate approach is to create the job threads but not start
     * them until the data file is completely read, using a loop at the end of the read file
     * method." See <a href="http://sandsduchon.org/duchon/2016/f/cs335/ideas.html">here</a>.
     * <br />
     * <br />
     * Furthermore, this also provides the opportunity to investigate each of the <code>Dock</code>s
     * individually prior to the start of any job, removing those vessels without any jobs and
     * replacing them with <code>Ship</code>s containing jobs. This prevents jobless ships from
     * hogging piers for the duration of the session, as was the case with the former implementation
     * that started jobs directed from the <code>World.process</code> method.
     *
     * @return void
     */
    private void startAllJobs() {
        for (SeaPort port : this.world.getPorts()) {
            // Only needs to be run once to clear off any initially moored ships without jobs
            for (Dock dock : port.getDocks()) { // For each dock...
                if (dock.getShip().getJobs().isEmpty()) { // If dock's ship has no jobs
                    this.jobsStatusTextArea.append("Unmooring " + dock.getShip().getName()
                        + " from " + dock.getName() + " in " + port.getName() + "\n");
                    dock.setShip(null); // And replace it

                    while (!port.getQue().isEmpty()) { // While the port has a queue...
                        Ship newShip = port.getQue().remove(0); // Grab a waiting ship,
                        if (!newShip.getJobs().isEmpty()) { // If it has jobs
                            dock.setShip(newShip); // Set that ship as the new moored vessel
                            this.jobsStatusTextArea.append("Mooring " + dock.getShip().getName()
                                + " at " + dock.getName() + " in " + port.getName() + "\n");
                            break;
                        }
                    }
                }
                // Set the initial job-possessing ships' respective docks to be handed off to others
                dock.getShip().setDock(dock);
            }

            /**
             * From here on, we run through the individual <code>Ship</code>s listings, ignoring
             * those without any <code>Job</code>s. For those with jobs, we create and add their
             * individual job <code>JPanel</code>s to the associated GUI panel, define the status
             * log <code>JTextArea</code>s, and finally begin the job <code>Thread</code> itself.
             * <br />
             * <br />
             * This way of starting jobs is far superior to the initialization of the thread from
             * within the <code>Job</code> constructor, as it negates the possibility (with which
             * the author has had to contend) of <code>Job</code>s believing themselves to be the
             * only jobs attached to a <code>Ship</code>. Furthermore, it conforms to MVC best
             * practices in minimizing the amount of <code>Swing</code>-based GUI code from being
             * handled by non-<code>SeaPortProgram</code> classes within the package.
             */
            for (Ship ship : port.getShips()) { // For all ships in the port, moored and queued
                if (!ship.getJobs().isEmpty()) { // If it has jobs
                    for (Job job : ship.getJobs()) { // For each job in ships with jobs
                        this.jobsScrollPanePanel.add(job.getJobAsPanel());
                        this.jobsScrollPanePanel.revalidate();
                        this.jobsScrollPanePanel.repaint();

                        job.setStatusLog(this.jobsStatusTextArea);
                        job.setWorkerLog(this.jobsPoolTextArea);
                        job.startJob(); // Begin the thread itself
                    }
                }
            }
        }
    }

    /**
     * This is probably not the proper way to go about doing this, but it appears that if the user
     * has had enough of a particular world, in order to properly load a new one, all extant
     * <code>Job</code> <code>JPanel</code>s have to be cleared and each thread must be naturally
     * ended before the initialization of a new world. In order to naturally end a world's threads,
     * we use a hard stop to return straight out of the run method rather than attempt a softer
     * stop in the form of setting <code>Job.isFinished</code>, as doing so results in each canceled
     * job still submitting its status updates to the log, even after new world initialization.
     *
     * @return void
     */
    private void clearAllJobs() {
        this.jobsScrollPanePanel.removeAll();           // Remove all job panels
        this.world.getAllThings().forEach((thing) -> {  // Better than getPorts>getShips>getJobs?
            if (thing instanceof Job) {                 // Only jobs, etc.
                ((Job) thing).endJob();                 // Set Job.isEndex to true for all
            }
        });
    }

    /**
     * Method simply displays an error popup <code>JOptonPane</code> window to notify the user of
     * an error in input or somesuch.
     *
     * @param message The <code>String</code> to be used as the body of the popup
     * @return void
     */
    private void displayErrorPopup(String message) {
        JOptionPane.showMessageDialog(this.mainFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Main method; initiates new <code>SeaPortProgram</code> object and invokes
     * <code>constructGUI</code> method. Only <code>public</code> method in the program.
     *
     * @param args
     * @return void
     */
    public static void main(String[] args) {
        SeaPortProgram newCollection = new SeaPortProgram();
        newCollection.constructGUI();
    }
}