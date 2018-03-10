### Object Oriented and Concurrent Programming, SeaPort Project 3 ###

#### Overview ####

The four-part SeaPort Project Series served as the primary means of concept enforcement in CMSC 335, Object Oriented and Current Programming. At its most basic level, the program employs the fundamental principles of object oriented programming and multithreading concurrency to chart the movements of hypothetical ships in a set of sea ports, processing their jobs in real time with the application of included dock workers and moving the vessels about from queue to dock to harbor as their jobs are completed.

Over the course of five projects, including the four required and an alternate Project 4, the author expanded a hand-drawn GUI constructed with Swing that provides users with a comprehensive overview of the world and its contents, in addition to a number of secondary utility features. Users are permitted access to a search function for any and all objects in the world, as well as a sorting option that lists objects by a number of user-selected parameters such as weight, draft, etc. Furthermore, each of the jobs in question is interactive, with the user given the ability to suspend, cancel, or complete ongoing jobs as desired.

#### About Project 3 ####

Project 3 is concerned with the specifics of the `Job` class and the related display of all job objects in the GUI. In line with the focus on concurrent multithreading programming, each job class instance possesses its own related thread which can only gain access to a limited number of dock workers if its associated ship is docked and enough workers of a certain occupation are available. The GUI was subsequently revamped from the previous three-panel design to include a new panel for all currently running jobs, in addition to a pair of status logs denoting the jobs being run and workers currently employed. Progress is displayed via a `JProgressBar`.

Included are all files, documentation (notated as `Eissen_Project3.pdf`), and the project requirements rubrics (notated as `SeaPort_Project3_Rubric.pdf`).