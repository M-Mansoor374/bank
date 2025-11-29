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
        if (localStorage.getItem('token')) {
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

            // Call backend API
            loginUser(username, password);
        });

        /**
         * Login user via backend API
         * @param {string} username - Username
         * @param {string} password - Password
         */
        async function loginUser(username, password) {
            try {
                showError(''); // Clear previous errors
                
                const response = await fetch('http://localhost:8080/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        username: username,
                        password: password
                    })
                });

                const data = await response.json();

                if (!response.ok) {
                    showError(data.message || 'Invalid username or password. Please try again.');
                    passwordInput.value = '';
                    return;
                }

                // Login successful - store token and user info
                if (data.success && data.data) {
                    localStorage.setItem('token', data.data.token);
                    localStorage.setItem('currentUser', data.data.username);
                    localStorage.setItem('accountId', data.data.accountId);
                    localStorage.setItem('balance', data.data.balance.toString());
                    
                    // Redirect to dashboard
                    window.location.href = 'dashboard.html';
                } else {
                    showError('Login failed. Please try again.');
                    passwordInput.value = '';
                }
            } catch (error) {
                showError('Connection error. Please make sure the backend server is running.');
                console.error('Login error:', error);
            }
        }

        /**
         * Display error message
         * @param {string} message - Error message to display
         */
        function showError(message) {
            if (message) {
                errorMessage.textContent = message;
                errorMessage.classList.add('show');
                
                // Hide error message after 5 seconds
                setTimeout(function() {
                    errorMessage.classList.remove('show');
                }, 5000);
            } else {
                errorMessage.classList.remove('show');
            }
        }
    });
})();

