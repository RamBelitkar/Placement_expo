// Browser compatibility polyfills - BALANCED MODE
// This file provides polyfills for browser APIs that may not be available in all browsers
// Non-aggressive approach to avoid interfering with authentication flow

(function() {
    'use strict';

    // Basic crypto polyfill - only if needed
    function createCryptoPolyfill() {
        const generateUUID = function() {
            return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
                const r = Math.random() * 16 | 0;
                const v = c === 'x' ? r : (r & 0x3 | 0x8);
                return v.toString(16);
            });
        };

        const getRandomValues = function(array) {
            for (let i = 0; i < array.length; i++) {
                array[i] = Math.floor(Math.random() * 256);
            }
            return array;
        };

        return {
            randomUUID: generateUUID,
            getRandomValues: getRandomValues
        };
    }

    // Only add crypto polyfills if they don't exist
    if (typeof window !== 'undefined') {
        // Ensure crypto exists
        if (!window.crypto) {
            window.crypto = createCryptoPolyfill();
        } else {
            // Add missing methods only
            if (!window.crypto.randomUUID) {
                window.crypto.randomUUID = createCryptoPolyfill().randomUUID;
            }
            if (!window.crypto.getRandomValues) {
                window.crypto.getRandomValues = createCryptoPolyfill().getRandomValues;
            }
        }

        // Patch global crypto only if it doesn't exist
        if (typeof crypto === 'undefined') {
            window.crypto = window.crypto || createCryptoPolyfill();
        } else if (!crypto.randomUUID) {
            crypto.randomUUID = window.crypto.randomUUID;
        }
    }

    // Minimal error suppression - only for known extension errors
    const originalError = window.onerror;
    window.onerror = function(message, filename, lineno, colno, error) {
        // Only suppress specific extension errors that we know are safe to ignore
        if (filename && (
            filename.includes('extension://') ||
            filename.includes('moz-extension://') ||
            filename.includes('safari-extension://')
        ) && message.includes('crypto.randomUUID')) {
            console.warn('[POLYFILLS] Suppressed extension crypto error:', message);
            return true;
        }

        // Let all other errors through normally
        if (originalError) {
            return originalError.apply(this, arguments);
        }
        
        return false;
    };

    // Basic promise rejection handling - only for crypto-related issues
    const originalRejectionHandler = window.onunhandledrejection;
    window.onunhandledrejection = function(event) {
        if (event.reason && event.reason.message && 
            event.reason.message.includes('crypto.randomUUID') &&
            event.reason.stack && event.reason.stack.includes('extension://')) {
            console.warn('[POLYFILLS] Suppressed extension crypto promise rejection');
            event.preventDefault();
            return true;
        }

        // Let other promise rejections through normally
        if (originalRejectionHandler) {
            return originalRejectionHandler.apply(this, arguments);
        }
        
        return false;
    };

    console.log('[POLYFILLS] Basic browser compatibility polyfills loaded successfully');
    console.log('[POLYFILLS] crypto.randomUUID available:', typeof window.crypto.randomUUID === 'function');
})();

// Additional immediate execution to ensure it runs ASAP
if (typeof window !== 'undefined' && !window.crypto) {
    window.crypto = {};
}
if (typeof window !== 'undefined' && !window.crypto.randomUUID) {
    window.crypto.randomUUID = function() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            const r = Math.random() * 16 | 0;
            const v = c === 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    };
}
