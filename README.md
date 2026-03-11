# 🧠 Wumpus World Application

A classic AI problem brought to life — an intelligent agent navigating a dangerous cave filled with hazards, using **Naïve Bayes** probabilistic reasoning, logical inference, and decision-making to survive and find the gold.

---

## 🗺️ Overview

The Wumpus World is a well-known environment in Artificial Intelligence, introduced in Russell & Norvig's *Artificial Intelligence: A Modern Approach*. This project implements a fully functional Wumpus World simulation with an AI agent capable of:

- **Perceiving** its environment through sensory inputs (stench, breeze, glitter, bump, scream)
- **Reasoning** about the location of hazards using Naïve Bayes inference
- **Making decisions** to safely navigate the cave, avoid the Wumpus and pits, and retrieve the gold

---

## 🎮 Game Rules

| Element    | Description                                                  |
|------------|--------------------------------------------------------------|
| 🟡 Gold    | The objective — find it and climb out to win                 |
| 👹 Wumpus  | A deadly monster; adjacent squares emit a **stench**        |
| 🕳️ Pit     | A bottomless pit; adjacent squares feel a **breeze**        |
| 🏹 Arrow   | One arrow available to shoot and kill the Wumpus             |
| 🚪 Exit    | Bottom-left corner `[1,1]` — escape with the gold to win    |

The agent dies if it steps into a pit or the Wumpus's square (unless the Wumpus is dead).

---

## 🧩 Features

- Full Wumpus World environment with configurable grid maps
- AI agent powered by **Naïve Bayes** for probabilistic hazard detection
- Logical reasoning engine for safe and risky move classification
- Multiple pre-built maps loaded from `maps.txt`
- Configurable environment settings via `config.txt`
- Graphical interface with assets in the `gfx/` directory

---

## 🗂️ Project Structure

```
├── src/                   # Java source files
│   ├── Agent.java         # AI agent logic — perception, reasoning, decisions
│   ├── World.java         # Wumpus World environment simulation
│   ├── NaiveBayes.java    # Naïve Bayes classifier for hazard inference
│   └── ...
├── build/                 # Compiled class files
├── gfx/                   # Graphics and UI assets
├── nbproject/             # NetBeans project configuration
├── maps.txt               # Predefined cave map layouts
├── config.txt             # Environment configuration settings
├── build.xml              # Ant build script
├── manifest.mf            # JAR manifest file
└── README.md
```

---

## ⚙️ Installation & Setup

### Prerequisites

- Java JDK 8+
- Apache Ant (for building) **or** NetBeans IDE

### Clone the Repository

```bash
git clone https://github.com/Bharath-Mbnsv/Wumpus-World-Application.git
cd Wumpus-World-Application
```

### Build with Ant

```bash
ant build
```

### Run the Application

```bash
ant run
```

Or run the compiled JAR directly:

```bash
java -jar dist/WumpusWorld.jar
```

### Open in NetBeans

Simply open the project folder in **NetBeans IDE** — it will auto-detect the `nbproject/` configuration and you can build/run from the IDE.

---

## 🤖 AI Agent — How It Thinks

The agent operates in a **Perceive → Reason → Act** loop:

```
┌─────────────┐     ┌──────────────────┐     ┌────────────────┐
│   Perceive  │────▶│  Naïve Bayes +   │────▶│  Choose Action │
│  (sensors)  │     │  Logic Reasoning │     │  (move/shoot)  │
└─────────────┘     └──────────────────┘     └────────────────┘
```

1. **Perception** — The agent reads sensory inputs from its current cell.
2. **Naïve Bayes Inference** — Computes the probability of a Wumpus or pit being in adjacent cells based on accumulated sensory evidence.
3. **Logical Reasoning** — Marks cells as safe, risky, or confirmed hazards.
4. **Decision** — Moves to the safest known cell, shoots if the Wumpus location is highly probable, or grabs the gold when found.

---

## 🗺️ Map Configuration

Maps are defined in `maps.txt` and the environment can be tuned via `config.txt`. Each map specifies grid size, starting position, gold location, Wumpus position, and pit locations.

Example map format:

```
4 4          # Grid size (rows x cols)
1 1          # Agent start position
3 3          # Gold position
1 3          # Wumpus position
2 2 3 1      # Pit positions (pairs)
```

---

## 🔮 Future Improvements

- Add a **STRIPS/PDDL-based** planner for more advanced logical reasoning
- Implement a **fully observable** mode for comparison with the partial-observability agent
- Extend to larger, randomly generated maps
- Add difficulty levels with multiple Wumpuses or more pits
- Build a web-based front end for browser play

---

## 📚 References

- Russell, S. & Norvig, P. — *Artificial Intelligence: A Modern Approach* (Chapter 7 — Wumpus World)
- Naïve Bayes Classification — probabilistic inference under uncertainty

---

## 🤝 Contributing

Contributions are welcome! To get started:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add your feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request
