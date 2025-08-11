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

---

## 2. Intelligent File Organizer

**Description:** 
A local file management utility that scans a directory and organizes files into categorized folders based on their extensions.
Categories can be customized via a JSON configuration file.

**Features:**
    Auto-detect and move files into:
        Images
        Videos
        Documents
        Music
        Code files

    Configurable categories via config.json.

    Generates a log of all moved files.

    Works recursively for subfolders.

**Run Instructions:**
```bash
javac FileOrganizer.java
java FileOrganizer

## 3. Offline URL Shortener

**Description:**
A simple URL shortener that works offline by creating short random codes for long URLs.
Mappings are stored locally in a serialized file and can be retrieved later.

**Features:**
    Generate unique short codes (6 characters).
    Retrieve the original URL from a short code.
    Track how many times a code has been used.
    Delete URL mappings.
    Persistent storage using binary file serialization.

**Run Instructions:**
```bash
javac OfflineURLShortener.java
java OfflineURLShortener
