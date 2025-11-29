/**
 * Login Page JavaScript
 * Handles user authentication and form validation
 */

(function() {
    'use strict';

    // Wait for DOM to be fully loaded
    document.addEventListener('DOMContentLoaded', function() {
        const loginForm = document.getElementById('loginForm');
        const errorMessage = document.getElementById('errorMessage');
        const usernameInput = document.getElementById('username');
        const passwordInput = document.getElementById('password');

        // Check if user is already logged in
        if (localStorage.getItem('loggedIn') === 'true') {
            window.location.href = 'dashboard.html';
            return;
        }

        // Handle form submission
        loginForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            // Get form values
            const username = usernameInput.value.trim();
            const password = passwordInput.value.trim();

            // Validate form fields
            if (!username || !password) {
                showError('Please fill in all fields');
                return;
            }

            // Get stored credentials
            const storedCredentials = JSON.parse(localStorage.getItem('userCredentials') || '{}');
            
            // Validate credentials
            if (username === storedCredentials.username && password === storedCredentials.password) {
                // Login successful
                localStorage.setItem('loggedIn', 'true');
                localStorage.setItem('currentUser', username);
                
                // Redirect to dashboard
                window.location.href = 'dashboard.html';
            } else {
                // Login failed
                showError('Invalid username or password. Please try again.');
                passwordInput.value = '';
            }
        });

        /**
         * Display error message
         * @param {string} message - Error message to display
         */
        function showError(message) {
            errorMessage.textContent = message;
            errorMessage.classList.add('show');
            
            // Hide error message after 5 seconds
            setTimeout(function() {
                errorMessage.classList.remove('show');
            }, 5000);
        }
    });
})();

