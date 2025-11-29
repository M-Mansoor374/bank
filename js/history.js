/**
 * Transaction History Page JavaScript
 * Displays all transaction history from backend API
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

        // Load and display transactions from backend
        loadTransactions();
    });

    /**
     * Load transactions from backend API
     */
    async function loadTransactions() {
        try {
            const token = localStorage.getItem('token');
            
            const response = await fetch('http://localhost:8080/transactions', {
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
                throw new Error(data.message || 'Failed to load transactions');
            }

            // Display transactions
            if (data.success && data.data && data.data.transactions) {
                displayTransactions(data.data.transactions);
            } else {
                displayTransactions([]);
            }
        } catch (error) {
            console.error('Transaction history error:', error);
            // Show empty state on error
            displayTransactions([]);
        }
    }

    /**
     * Display all transactions in a table
     * @param {Array} transactions - Array of transaction objects
     */
    function displayTransactions(transactions) {
        const transactionsBody = document.getElementById('transactionsBody');
        const emptyState = document.getElementById('emptyState');
        const transactionsContainer = document.getElementById('transactionsContainer');

        // Check if there are any transactions
        if (!transactions || transactions.length === 0) {
            transactionsContainer.style.display = 'none';
            emptyState.style.display = 'block';
            return;
        }

        // Show transactions table
        transactionsContainer.style.display = 'block';
        emptyState.style.display = 'none';

        // Clear existing rows
        transactionsBody.innerHTML = '';

        // Sort transactions by timestamp (newest first)
        const sortedTransactions = transactions.sort(function(a, b) {
            return new Date(b.timestamp) - new Date(a.timestamp);
        });

        // Create table rows for each transaction
        sortedTransactions.forEach(function(transaction) {
            const row = createTransactionRow(transaction);
            transactionsBody.appendChild(row);
        });
    }

    /**
     * Create a table row for a transaction
     * @param {Object} transaction - Transaction object
     * @returns {HTMLElement} Table row element
     */
    function createTransactionRow(transaction) {
        const row = document.createElement('tr');

        // Transaction type
        const typeCell = document.createElement('td');
        typeCell.className = 'transaction-type';
        typeCell.textContent = transaction.type || 'N/A';
        row.appendChild(typeCell);

        // Transaction amount
        const amountCell = document.createElement('td');
        const amount = parseFloat(transaction.amount || 0);
        amountCell.className = 'transaction-amount';
        
        // Add class based on transaction type
        if (transaction.type === 'Deposit') {
            amountCell.classList.add('deposit');
            amountCell.textContent = `+Rs ${amount.toFixed(2)}`;
        } else if (transaction.type === 'Withdraw') {
            amountCell.classList.add('withdraw');
            amountCell.textContent = `-Rs ${amount.toFixed(2)}`;
        } else {
            amountCell.textContent = `Rs ${amount.toFixed(2)}`;
        }
        row.appendChild(amountCell);

        // Transaction description
        const descCell = document.createElement('td');
        descCell.textContent = transaction.description || 'N/A';
        row.appendChild(descCell);

        // Transaction timestamp
        const timestampCell = document.createElement('td');
        timestampCell.className = 'transaction-timestamp';
        const date = new Date(transaction.timestamp);
        const formattedDate = formatDateTime(date);
        timestampCell.textContent = formattedDate;
        row.appendChild(timestampCell);

        return row;
    }

    /**
     * Format date and time for display
     * @param {Date} date - Date object
     * @returns {string} Formatted date string
     */
    function formatDateTime(date) {
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = date.getFullYear();
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');

        return `${day}/${month}/${year} ${hours}:${minutes}:${seconds}`;
    }
})();

