/**
 * Dashboard Page JavaScript
 * Displays user information, balance, and handles navigation
 */

(function() {
    'use strict';

    // Wait for DOM to be fully loaded
    document.addEventListener('DOMContentLoaded', function() {
        // Check if user is logged in
        if (!localStorage.getItem('token')) {
            window.location.href = 'index.html';
            return;
        }

        // Load dashboard data from backend
        loadDashboardData();

        // Handle logout
        const logoutBtn = document.getElementById('logoutBtn');
        logoutBtn.addEventListener('click', function(e) {
            e.preventDefault();
            logout();
        });
    });

    /**
     * Load dashboard data from backend API
     */
    async function loadDashboardData() {
        try {
            const token = localStorage.getItem('token');
            
            const response = await fetch('http://localhost:8080/dashboard', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token
                }
            });

            const data = await response.json();

            if (!response.ok) {
                if (response.status === 401) {
                    // Token invalid, redirect to login
                    localStorage.removeItem('token');
                    window.location.href = 'index.html';
                    return;
                }
                throw new Error(data.message || 'Failed to load dashboard data');
            }

            // Display dashboard data
            if (data.success && data.data) {
                displayUserInfo(data.data.username);
                displayBalance(data.data.balance);
            }
        } catch (error) {
            console.error('Dashboard error:', error);
            // Fallback to localStorage if API fails
            displayUserInfo(localStorage.getItem('currentUser') || 'User');
            displayBalance(parseFloat(localStorage.getItem('balance') || '0'));
        }
    }

    /**
     * Display current user's username
     * @param {string} username - Username to display
     */
    function displayUserInfo(username) {
        const usernameDisplay = document.getElementById('usernameDisplay');
        usernameDisplay.textContent = username || 'User';
    }

    /**
     * Display current account balance
     * @param {number} balance - Balance to display
     */
    function displayBalance(balance) {
        const balanceDisplay = document.getElementById('balanceDisplay');
        const balanceValue = parseFloat(balance || '0');
        balanceDisplay.textContent = `Rs ${balanceValue.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
    }

    /**
     * Handle user logout
     * Clears session and redirects to login page
     */
    function logout() {
        if (confirm('Are you sure you want to logout?')) {
            localStorage.removeItem('token');
            localStorage.removeItem('currentUser');
            localStorage.removeItem('accountId');
            localStorage.removeItem('balance');
            window.location.href = 'index.html';
        }
    }
})();

