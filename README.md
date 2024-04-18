## Overview
This disease simulation game allows to simulate the spread of a disease among a population of agents. Each agent has behaviors such as moving, getting infected, recovering, having a ghost control you and death. The simulation includes customizable parameters and displays the progression through a graphical user interface.


## Setup
Clone or download the repository: Ensure all the provided Java files are placed in the same directory.
Open your IDE: Import the project into your Java IDE.
Ensure all files are in the correct package: If the files are not grouped in a package, create a new package and move all files into it.

## Configuration
* Agent Parameters: Customize agent behaviors through the AgentParameters.java file, where you can set parameters like incubation period, sickness length, and recovery probability.
* Layout and Management: Adjust layout and agent management settings in AgentLayout.java and AgentManager.java for different simulation dynamics.
* Running the Simulation
* Compile the project: Build the project using your IDE’s build functionality.
* Run the game: Execute the main method in the PlagueGUI.java file to start the simulation.
* Interact with the GUI: The GUI allows you to observe the simulation in real-time and provides controls to     adjust the simulation parameters on the fly.

# How To Play



### **Roles for each members of the project**

### Pallav Regmi: Core Simulation Logic
- **Designs the agent behaviors and state transitions** (Vulnerable, Sick, Immune, Dead).
- **Implements the simulation's core logic**, including disease spread mechanisms and agent interactions.
- **Handles concurrency and synchronization** to manage agents running on separate threads safely.

### Ashmit Agrawal: GUI and Configuration
- **Develops the GUI** to display the simulation, showing agents in different states and providing user control over the simulation (start, stop, restart).
- **Manages the configuration file** loading and parsing, applying settings to the simulation.
- **Implements additional features**, such as simulation history, plotting disease progression, or agent movement.

### Collaborative Efforts:
- **Integration and Testing**: Both members work together to integrate the core logic with the GUI, ensuring the simulation runs as intended and is accurately represented visually.
- **Documentation**: Jointly document the project, detailing setup, operation, and features.
- **Feedback and Refinement**: Share feedback on each other’s work for improvement and refinement, addressing any bugs or performance issues found during testing.





