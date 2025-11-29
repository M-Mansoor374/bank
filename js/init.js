/**
 * Initialization Script
 * Sets up default user credentials, initial balance, and transaction history
 * Runs on page load to ensure localStorage is properly initialized
 */

(function() {
    'use strict';

    // Initialize default user credentials if not already set
    if (!localStorage.getItem('userCredentials')) {
        const credentials = {
            username: 'user@gmail.com',
            password: '12345'
        };
        localStorage.setItem('userCredentials', JSON.stringify(credentials));
    }

    // Initialize balance if not already set
    if (!localStorage.getItem('balance')) {
        localStorage.setItem('balance', '10000'); // Initial balance: Rs 10,000
    }

    // Initialize transaction history if not already set
    if (!localStorage.getItem('transactions')) {
        const initialTransactions = [
            {
                type: 'Initial',
                amount: 10000,
                timestamp: new Date().toISOString(),
                description: 'Account opened with initial balance'
            }
        ];
        localStorage.setItem('transactions', JSON.stringify(initialTransactions));
    }

    // Initialize session state if not already set
    if (!localStorage.getItem('loggedIn')) {
        localStorage.setItem('loggedIn', 'false');
    }

    // Initialize current user if not already set
    if (!localStorage.getItem('currentUser')) {
        localStorage.setItem('currentUser', '');
    }
})();

