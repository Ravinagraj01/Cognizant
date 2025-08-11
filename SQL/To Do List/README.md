# Flask To-Do List App

A simple, user-friendly To-Do List web application built with Flask and SQLite. This app allows users to add, view, update, and clear tasks, with a basic login system.

## Features
- User authentication (login/logout)
- Add new tasks
- View all tasks
- Change task status (Pending → Working → Done → Pending)
- Clear all tasks
- Flash messages for feedback
- Responsive, modern UI

## Folder Structure
```
app/
  __init__.py
  models/
    models.py
  routes/
    auth.py
    tasks.py
  static/
    css/style.css
  templates/
    base.html
    login.html
    register.html
    task.html
instance/
  todo.db
run.py
```

## Getting Started

### Prerequisites
- Python 3.7+
- `pip` (Python package manager)

### Installation
1. **Clone the repository:**
   ```bash
   git clone <repo-url>
   cd <project-directory>
   ```
2. **Create a virtual environment (optional but recommended):**
   ```bash
   python -m venv venv
   source venv/bin/activate  # On Windows: venv\Scripts\activate
   ```
3. **Install dependencies:**
   ```bash
   pip install flask flask_sqlalchemy
   ```
4. **Run the app:**
   ```bash
   python run.py
   ```
5. **Open your browser:**
   Visit [http://127.0.0.1:5000/](http://127.0.0.1:5000/)

## Usage
- **Login:** Use the default credentials:
  - Username: `admin`
  - Password: `1234`
- **Add Tasks:** Enter a task and click 'Add'.
- **Change Status:** Click 'Next' to cycle through task statuses.
- **Clear Tasks:** Click 'Clear all tasks' to remove all tasks.
- **Logout:** Use the navigation bar.

## Requirements
- Flask
- Flask-SQLAlchemy

## Customization
- To change login credentials, edit `app/routes/auth.py`.
- To modify the database, see `app/models/models.py`.

## License
MIT License
