from flask import Blueprint, render_template, request, redirect, url_for, flash, session

auth_bp = Blueprint('auth', __name__)

# Hardcoded credentials
USER_CREDENTIALS = {
    'username': 'admin',
    'password': '1234'
}

@auth_bp.route('/login', methods=["POST", "GET"])
def login():
    if request.method == "POST":
        username = request.form.get('username')
        password = request.form.get('password')

        if username == USER_CREDENTIALS['username'] and password == USER_CREDENTIALS['password']:
            session['user'] = username
            flash('Login successful', 'success')
            return redirect(url_for('tasks.view_tasks'))  # Redirect to tasks page
        else:
            flash('Invalid username or password', 'danger')
            return redirect(url_for('auth.login'))  # Redirect back to login

    return render_template('login.html')  # Show login form

@auth_bp.route('/logout')
def logout():
    session.pop('user', None)
    flash('Logged out successfully!', 'info')
    return redirect(url_for('auth.login'))
