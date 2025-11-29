/**
 * Withdraw Page JavaScript
 * Handles withdrawal transactions and balance validation
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

        // Load and display available balance
        loadBalance();

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

            // Process withdrawal via backend API
            processWithdrawal(amount);
        });
    });

    /**
     * Load balance from backend
     */
    async function loadBalance() {
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

            if (response.ok && data.success && data.data) {
                displayBalance(data.data.balance);
                localStorage.setItem('balance', data.data.balance.toString());
            } else {
                // Fallback to localStorage
                displayBalance(parseFloat(localStorage.getItem('balance') || '0'));
            }
        } catch (error) {
            // Fallback to localStorage
            displayBalance(parseFloat(localStorage.getItem('balance') || '0'));
        }
    }

    /**
     * Display current available balance
     * @param {number} balance - Balance to display
     */
    function displayBalance(balance) {
        const balanceDisplay = document.getElementById('availableBalance');
        const balanceValue = parseFloat(balance || '0');
        balanceDisplay.textContent = `Rs ${balanceValue.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
    }

    /**
     * Process withdrawal transaction via backend API
     * @param {number} amount - Amount to withdraw
     */
    async function processWithdrawal(amount) {
        try {
            const token = localStorage.getItem('token');
            
            const response = await fetch('http://localhost:8080/withdraw', {
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
                showError(data.message || 'Withdrawal failed. Please try again.');
                return;
            }

            // Withdrawal successful
            if (data.success && data.data) {
                // Update local balance
                localStorage.setItem('balance', data.data.newBalance.toString());
                
                // Show success message
                showSuccess(data.data.message || `Successfully withdrew Rs ${amount.toFixed(2)}. Remaining balance: Rs ${data.data.newBalance.toFixed(2)}`);
                
                // Clear form
                document.getElementById('amount').value = '';

                // Update displayed balance
                displayBalance(data.data.newBalance);

                // Redirect to dashboard after 2 seconds
                setTimeout(function() {
                    window.location.href = 'dashboard.html';
                }, 2000);
            } else {
                showError('Withdrawal failed. Please try again.');
            }
        } catch (error) {
            showError('Connection error. Please make sure the backend server is running.');
            console.error('Withdrawal error:', error);
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

