# Java Mini Projects Showcase

This repository contains **three Java mini-projects** designed to demonstrate Java skills in file handling, collections, object serialization, and problem-solving.  
Each project runs on the command line and stores data locally so that it persists between runs.

---

## 1. Smart Password Generator & Analyzer

**Description:**  
A security tool that can generate strong, random passwords and analyze the strength of any given password.  
It uses rules for complexity and provides feedback for improvement.

**Features:**
- Generate random passwords based on:
  - Length
  - Uppercase/lowercase letters
  - Numbers
  - Special characters
- Analyze a password’s strength:
  - Length score
  - Character variety score
  - Common password check
- Save generated passwords in an **encrypted file**.

**Run Instructions:**
```bash
javac PasswordTool.java
java PasswordTool
