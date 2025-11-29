/**
 * Withdraw Page JavaScript
 * Handles withdrawal transactions and balance validation
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

        // Display available balance
        displayBalance();

        const withdrawForm = document.getElementById('withdrawForm');
        const errorMessage = document.getElementById('errorMessage');
        const successMessage = document.getElementById('successMessage');
        const amountInput = document.getElementById('amount');

        // Handle form submission
        withdrawForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            // Hide previous messages
            hideMessages();

            // Get withdrawal amount
            const amount = parseFloat(amountInput.value.trim());

            // Validate amount
            if (!amount || amount <= 0) {
                showError('Please enter a valid positive amount');
                return;
            }

            // Get current balance
            const currentBalance = parseFloat(localStorage.getItem('balance') || '0');

            // Validate sufficient balance
            if (amount > currentBalance) {
                showError(`Insufficient balance. Available: Rs ${currentBalance.toFixed(2)}`);
                return;
            }

            // Process withdrawal
            processWithdrawal(amount);
        });
    });

    /**
     * Display current available balance
     */
    function displayBalance() {
        const balanceDisplay = document.getElementById('availableBalance');
        const balance = parseFloat(localStorage.getItem('balance') || '0');
        balanceDisplay.textContent = `Rs ${balance.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
    }

    /**
     * Process withdrawal transaction
     * @param {number} amount - Amount to withdraw
     */
    function processWithdrawal(amount) {
        // Get current balance
        let currentBalance = parseFloat(localStorage.getItem('balance') || '0');
        
        // Update balance
        currentBalance -= amount;
        localStorage.setItem('balance', currentBalance.toString());

        // Create transaction record
        const transaction = {
            type: 'Withdraw',
            amount: amount,
            timestamp: new Date().toISOString(),
            description: `Withdrew Rs ${amount.toFixed(2)}`
        };

        // Add transaction to history
        addTransaction(transaction);

        // Show success message
        showSuccess(`Successfully withdrew Rs ${amount.toFixed(2)}. Remaining balance: Rs ${currentBalance.toFixed(2)}`);
        
        // Clear form
        document.getElementById('amount').value = '';

        // Update displayed balance
        displayBalance();

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

