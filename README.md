# Scala Task Management System

A simple and efficient task management web application built with the Play Framework and Scala. This application allows users to create boards, organize tasks into lists, and track progress across different stages of work.

## Features

- **Board Management:** Create, view, and manage multiple project boards
- **Task Organization:** Create tasks and organize them into customizable lists
- **Kanban-Style Interface:** Track progress by moving tasks between "To-Do", "Doing", and "Done" lists
- **Responsive Design:** Works on desktop and mobile devices

## Technologies Used

- **Backend:** 
  - Scala 2.13.x
  - Play Framework 3.0.7
  - SBT (Scala Build Tool)
- **Frontend:** 
  - HTML, CSS, JavaScript
  - Bootstrap 4.5.2
  - Font Awesome 5.15.3
  - jQuery 3.5.1

---

## Setup Instructions (Mac & Windows)

### Prerequisites

- **Java JDK 11 or higher**  
  [Download Adoptium JDK](https://adoptium.net/temurin/releases/?version=11)
- **SBT (Scala Build Tool)**  
  [Download SBT](https://www.scala-sbt.org/download.html)
- **Git**  
  [Download Git](https://git-scm.com/downloads)

---

### 1. Clone the Repository

```sh
git clone https://github.com/your-username/task-management-webapp.git
cd task-management-webapp

2. Install Java & SBT
On Mac:

Install Homebrew if not already installed.
Then run:
brew install temurin
brew install sbt
Or use the official installers from the links above.
On Windows:

Download and install:
Adoptium JDK 11+ MSI
SBT Windows Installer
Restart your computer after installation to update your PATH.
3. Verify Installation
Open a new terminal (Mac: Terminal, Windows: Command Prompt or PowerShell):

java -version     # Should print java version 11 or higher
sbt sbtVersion    # Should print sbt version
4. Run the Application
In your project directory, run:

sbt run
This will compile the project and start the server.

5. Open in Browser
Visit:

http://localhost:9000
You will see the welcome page.

Usage Guide

From the welcome page, explore features to create boards, add tasks, and track progress.
Troubleshooting

Java or SBT not found: Ensure both Java and SBT are correctly installed and added to your PATH.
Port conflicts: If port 9000 is in use, change the Play Framework port in conf/application.conf or stop the conflicting service.
Future Enhancements

Task due dates and priorities
User profile management
File attachments for tasks
Team collaboration features
Email notifications
Drag-and-drop interface
