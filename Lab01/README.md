# I3S2 Automation Testing Labs

**Student Name:** Virak Rith  
**Major:** Software Engineering (Year 3)  
**Institution:** Institute of Technology of Cambodia (ITC)

This repository contains the automation testing laboratory exercises for Semester 2, 2026.

---

## 🚀 Project Structure

The repository is organized into subfolders for each laboratory session. Each lab is a standalone Java project managed via a central GitHub Actions workflow.

- **.github/workflows/**: Contains YAML files for CI/CD automation.
- **Lab01/**: Introduction to JUnit 5 and Automated API Testing.

---

## 🧪 Lab 01: API Authentication Testing

### Overview

In this lab, I configured a Java testing environment using **JUnit 5 (Jupiter)** without a heavy build tool like Maven or Gradle. The goal was to test a Login API endpoint.

### Key Learnings

- Setting up the **JUnit Platform Standalone Console** (v1.13.0-M3).
- Using Java's native `HttpClient` to send POST requests.
- Automating tests using **GitHub Actions**.

### How to Run Locally

1. **Navigate to the lab folder:**
   ```powershell
   cd Lab01
   ```
2. **Compile the code:**
   ```powershell
   javac -d bin -cp "lib/*;src" src/lab01/*.java test/lab01/*.java
   ```
3. **Execute the tests:**
   ```powershell
   java -jar lib/junit-platform-console-standalone-1.13.0-M3.jar --class-path bin --scan-class-path
   ```
