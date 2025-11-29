/**
 * Splash Screen JavaScript
 * Shows splash screen for 3 seconds, then displays main content
 */

(function() {
    'use strict';

    document.addEventListener('DOMContentLoaded', function() {
        const splashScreen = document.getElementById('splashScreen');
        const mainContent = document.getElementById('mainContent');

        // Hide splash screen and show main content after 3 seconds
        setTimeout(function() {
            splashScreen.style.display = 'none';
            mainContent.style.display = 'block';
            
            // Add fade-in animation to main content
            mainContent.style.animation = 'fadeIn 0.5s ease-in';
        }, 4000); // 4 seconds
    });
})();

