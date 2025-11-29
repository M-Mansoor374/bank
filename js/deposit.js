/**
 * Deposit Page JavaScript
 * Handles deposit transactions and balance updates
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

            // Process deposit
            processDeposit(amount);
        });
    });

    /**
     * Process deposit transaction
     * @param {number} amount - Amount to deposit
     */
    function processDeposit(amount) {
        // Get current balance
        let currentBalance = parseFloat(localStorage.getItem('balance') || '0');
        
        // Update balance
        currentBalance += amount;
        localStorage.setItem('balance', currentBalance.toString());

        // Create transaction record
        const transaction = {
            type: 'Deposit',
            amount: amount,
            timestamp: new Date().toISOString(),
            description: `Deposited Rs ${amount.toFixed(2)}`
        };

        // Add transaction to history
        addTransaction(transaction);

        // Show success message
        showSuccess(`Successfully deposited Rs ${amount.toFixed(2)}. New balance: Rs ${currentBalance.toFixed(2)}`);
        
        // Clear form
        document.getElementById('amount').value = '';

        // Redirect to dashboard after 2 seconds
        setTimeout(function() {
            window.location.href = 'dashboard.html';
        }, 2000);
    }

    /**
     * Add transaction to history
     * @param {Object} transaction - Transaction object
     */
    function addTransaction(transaction) {
        let transactions = JSON.parse(localStorage.getItem('transactions') || '[]');
        transactions.push(transaction);
        localStorage.setItem('transactions', JSON.stringify(transactions));
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

