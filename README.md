
# Disease Simulation Project
# Authors : Ashmit Agrawal & Pallav Regmi


## Overview
This project simulates the spread of a disease through a population where agents can be humans, animals, or any entity capable of disease transmission. The simulation explores various states such as vulnerable, sick, immune, and dead. A unique feature of this simulation is the "Ghost Mode," where some of the deceased agents become ghosts for a specified time, haunting and potentially "killing" nearby agents.


## Features
- **Simulation of disease dynamics** with agents transitioning through different states based on disease exposure and recovery.
- **Emoji Status Indicators**: Each agent's current state is visually represented by emojis, enhancing the user interface and making the simulation more intuitive and are as following:
Vulnerable(👤), Incubating(🦠) , Sick(😷), Immune(🍏), Ghost(👻), Dead(🪦).
- **Configurable Simulation Parameters**: Users can adjust various parameters like exposure distance, incubation period, sickness duration, and initial agent states through a configuration file.

## Additional Feature
- **Ghost Mode**: Deceased agents can turn into ghosts, introducing an additional layer of interaction and consequences within the simulation. The unique feature of this simulation, "Ghost Mode," where some of the deceased agents become ghosts for a specified time, haunting and potentially "killing" nearby agents.

## Configuration
Users can customize the simulation via the GUI or a configuration file.
**Vulnerable Status: Can get the virus and get into incubation period.
**Incubation Period: Time before symptoms appear.
**Sickness Time: Duration an agent remains sick before recovering or dying.
**Recovery Probability: Likelihood of an agent recovering from the disease.
**Initial Sick/Immune Count: Number of initially sick or immune agents.
**Agent Speed: Determines how fast agents move in the simulation area.
**Ghost Chance of an agent turning into a ghost after death.
**Ghost Time: Time after which a ghost will perish if it doesn't interact with other agents.


##Running the Program
Run the DiseaseMainGUI which is the main class amongst the 9 classes
Put in the name of the test file (sample_config.txt, test1.txt,test2.txt, test3.txt) in the Configuration File tab which is on the top left of the GUI display.
Click the submit button.
Finally, click the Start button.
You can see the status of the population/agent displayed by emojis on the canvas. Meanings of the emojis are on the top of the displayed GUI.
You may also change the Exposure Distance, Incubation Period, Sickness Time, Recovery Probability, Initial Sick, Initial Immune, Ghost Chance, Ghost Time and Reanimated Life Time. 


## Usage
- The GUI displays the simulation with different colored emojis representing the agents' states.
- Control the simulation through the GUI to start, pause, or reset the simulation as needed.
- Modify the `sample_config.txt` file to change simulation parameters and explore different scenarios.


### Design and Architecture of DiseaseMainGUI

The interface uses a `BorderPane` layout, organizing content into regions for displaying the simulation, controls, health status indicators, and a message log.

- **Canvas:** Displays the simulation grid where agents move and change states.
- **Control Panel:** Contains inputs like buttons and dropdowns for adjusting simulation parameters.
- **Health States Bar:** Shows different health statuses with emojis.
- **Message Log:** Logs events during the simulation on the right.
- **Parameter Adjustment:** Users can modify disease parameters in real-time through the control panel.
- **Animation and Updates:** Uses JavaFX `AnimationTimer` to update the simulation dynamically.
- **Event Handling:** Manages user interactions and file inputs for configuration.
- **External Configuration:** Supports loading initial settings from a file, allowing flexible setup for various scenarios.
- **Scalability:** Designed to be scalable, facilitating the addition of more complex behaviors or simulation environments.

This architecture provides a comprehensive tool for visualizing disease dynamics, offering interactive controls and detailed graphical representations.



## **Roles for each members of the project**

### Pallav Regmi: Core Simulation Logic
- **Designed the agent behaviors and state transitions** (Vulnerable, Sick, Immune, Dead and Ghost).
- **Implemented the simulation's core logic**, by including disease spread mechanisms and agent interactions.
- **Handled concurrency and synchronization** by managing agents running on separate threads safely.

## Ashmit Agrawal: GUI and Configuration
- **Developed the GUI** to display the simulation, showing agents in different states to provide user control over the simulation (start, restart etc.).
- **Managed the configuration file** loading and parsing, applying settings to the simulation.
- **Implemented additional feature**, which is ghost mode and unique way of agent movement.

## Collaborative Efforts:
- **Integration and Testing**: Both members worked together to integrate the core logic with the GUI, ensuring the simulation runs as intended and is accurately represented visually.
- **Documentation**: Jointly documented the project, detailing setup, operation, and features.
- **Feedback and Refinement**: Shared feedback on each others' work for improvement and refinement, addressing any bugs or performance issues found during testing.


