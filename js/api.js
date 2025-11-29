/**
 * API Configuration and Utility Functions
 * Shared functions for backend communication
 */

const API_BASE_URL = 'http://localhost:8080';

/**
 * Get authentication headers
 */
function getAuthHeaders() {
    const token = localStorage.getItem('token');
    const headers = {
        'Content-Type': 'application/json'
    };
    if (token) {
        headers['Authorization'] = 'Bearer ' + token;
    }
    return headers;
}

/**
 * Check if user is authenticated
 */
function isAuthenticated() {
    return localStorage.getItem('token') !== null;
}

/**
 * Redirect to login if not authenticated
 */
function requireAuth() {
    if (!isAuthenticated()) {
        window.location.href = 'index.html';
        return false;
    }
    return true;
}

/**
 * Handle API response
 */
async function handleResponse(response) {
    const data = await response.json();
    
    if (!response.ok) {
        throw new Error(data.message || 'An error occurred');
    }
    
    return data;
}

/**
 * Make API request
 */
async function apiRequest(endpoint, method, body) {
    const url = API_BASE_URL + endpoint;
    const options = {
        method: method,
        headers: getAuthHeaders()
    };
    
    if (body && (method === 'POST' || method === 'PUT')) {
        options.body = JSON.stringify(body);
    }
    
    try {
        const response = await fetch(url, options);
        return await handleResponse(response);
    } catch (error) {
        throw error;
    }
}

