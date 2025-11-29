/**
 * Deposit Page JavaScript
 * Handles deposit transactions and balance updates
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

        const depositForm = document.getElementById('depositForm');
        const errorMessage = document.getElementById('errorMessage');
        const successMessage = document.getElementById('successMessage');
        const amountInput = document.getElementById('amount');

        // Handle form submission
        depositForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            // Hide previous messages
            hideMessages();

            // Get deposit amount
            const amount = parseFloat(amountInput.value.trim());

            // Validate amount
            if (!amount || amount <= 0) {
                showError('Please enter a valid positive amount');
                return;
            }

            // Process deposit via backend API
            processDeposit(amount);
        });
    });

    /**
     * Process deposit transaction via backend API
     * @param {number} amount - Amount to deposit
     */
    async function processDeposit(amount) {
        try {
            const token = localStorage.getItem('token');
            
            const response = await fetch('http://localhost:8080/deposit', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token
                },
                body: JSON.stringify({
                    amount: amount.toString()
                })
            });

            const data = await response.json();

            if (!response.ok) {
                if (response.status === 401) {
                    // Token invalid, redirect to login
                    localStorage.removeItem('token');
                    window.location.href = 'index.html';
                    return;
                }
                showError(data.message || 'Deposit failed. Please try again.');
                return;
            }

            // Deposit successful
            if (data.success && data.data) {
                // Update local balance
                localStorage.setItem('balance', data.data.newBalance.toString());
                
                // Show success message
                showSuccess(data.data.message || `Successfully deposited Rs ${amount.toFixed(2)}. New balance: Rs ${data.data.newBalance.toFixed(2)}`);
                
                // Clear form
                document.getElementById('amount').value = '';

                // Redirect to dashboard after 2 seconds
                setTimeout(function() {
                    window.location.href = 'dashboard.html';
                }, 2000);
            } else {
                showError('Deposit failed. Please try again.');
            }
        } catch (error) {
            showError('Connection error. Please make sure the backend server is running.');
            console.error('Deposit error:', error);
        }
    }

    /**
     * Display error message
     * @param {string} message - Error message to display
     */
    function showError(message) {
        const errorMessage = document.getElementById('errorMessage');
        errorMessage.textContent = message;
        errorMessage.classList.add('show');
    }

    /**
     * Display success message
     * @param {string} message - Success message to display
     */
    function showSuccess(message) {
        const successMessage = document.getElementById('successMessage');
        successMessage.textContent = message;
        successMessage.classList.add('show');
    }

    /**
     * Hide all messages
     */
    function hideMessages() {
        document.getElementById('errorMessage').classList.remove('show');
        document.getElementById('successMessage').classList.remove('show');
    }
})();

