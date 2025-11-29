/**
 * Dashboard Page JavaScript
 * Displays user information, balance, and handles navigation
 */

(function() {
    'use strict';

    // Wait for DOM to be fully loaded
    document.addEventListener('DOMContentLoaded', function() {
        // Check if user is logged in
        if (localStorage.getItem('loggedIn') !== 'true') {
            window.location.href = 'index.html';
            return;
        }

        // Display user information
        displayUserInfo();
        
        // Display balance
        displayBalance();

        // Handle logout
        const logoutBtn = document.getElementById('logoutBtn');
        logoutBtn.addEventListener('click', function(e) {
            e.preventDefault();
            logout();
        });
    });

    /**
     * Display current user's username
     */
    function displayUserInfo() {
        const usernameDisplay = document.getElementById('usernameDisplay');
        const currentUser = localStorage.getItem('currentUser') || 'User';
        usernameDisplay.textContent = currentUser;
    }

    /**
     * Display current account balance
     */
    function displayBalance() {
        const balanceDisplay = document.getElementById('balanceDisplay');
        const balance = parseFloat(localStorage.getItem('balance') || '0');
        balanceDisplay.textContent = `Rs ${balance.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
    }

    /**
     * Handle user logout
     * Clears session and redirects to login page
     */
    function logout() {
        if (confirm('Are you sure you want to logout?')) {
            localStorage.setItem('loggedIn', 'false');
            localStorage.setItem('currentUser', '');
            window.location.href = 'index.html';
        }
    }
})();

