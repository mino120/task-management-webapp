# Scala Task Management System

A simple and efficient task management web application built with the Play Framework and Scala. This application allows users to create boards, organize tasks into lists, and track progress across different stages of work.

![Scala Task Management](/public/images/screenshot.png)

## Features

- **User Authentication:** Secure login and registration system
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

## Setup Instructions

### Prerequisites

- JDK 11 or higher
- SBT (Scala Build Tool)
- Git

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/task-management-webapp.git
   cd task-management-webapp
   ```

2. Run the application:
   ```bash
   sbt run
   ```

3. Open your browser and navigate to:
   ```
   http://localhost:9000
   ```

4. Login with the demo account:
   - Email: demo@example.com
   - Password: password
   
   Or register a new account.

## Authentication System

The application implements a session-based authentication system:

- **Registration:** Users can create new accounts with email and password
- **Login/Logout:** Session management for authenticated users
- **Access Control:** Protected routes require authentication
- **User-Specific Data:** Boards and tasks are associated with specific users

For demo purposes, the app uses in-memory storage. In a production environment, you would:
- Connect to a database
- Implement password hashing
- Add email verification
- Add more security features

## Usage Guide

### Creating a Board

1. Log in to your account
2. Click "New Board" in the navigation menu
3. Enter a name and optional description for your board
4. Click "Create Board"

### Adding Tasks

1. Open a board
2. Find the list where you want to add a task (To-Do, Doing, Done)
3. Click "Add Task" button
4. Enter a title and optional description
5. Click "Save"

### Tracking Progress

1. Move tasks between lists to track their progress
2. Tasks in "To-Do" haven't been started
3. Tasks in "Doing" are in progress
4. Tasks in "Done" are completed

## Future Enhancements

- Task due dates and priorities
- User profile management
- File attachments for tasks
- Team collaboration features
- Email notifications
- Drag-and-drop interface

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Play Framework team for the excellent web framework
- Bootstrap team for the responsive design framework
- All contributors to this project

---

© 2025 Scala Task Management System