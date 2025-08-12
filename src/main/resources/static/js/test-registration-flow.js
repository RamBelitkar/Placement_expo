// Simple test to verify the registration flow
async function testRegistrationFlow() {
    const testData = {
        firstName: 'Test',
        lastName: 'User',
        phone: '+1234567890',
        department: 'Computer Science',
        currentYear: 3,
        currentCgpa: 8.5,
        backlogs: 0
    };

    try {
        // Test registration
        const registerResponse = await fetch('/api/v1/profile-simple/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-Appwrite-User-Id': 'test-user-simple',
                'X-Appwrite-User-Email': 'simple@test.com'
            },
            body: JSON.stringify(testData)
        });
        
        const registerResult = await registerResponse.json();
        console.log('Registration result:', registerResult);
        
        if (!registerResult.success) {
            throw new Error('Registration failed: ' + registerResult.message);
        }
        
        // Test dashboard
        const dashboardResponse = await fetch('/api/v1/profile-simple/dashboard', {
            method: 'GET',
            headers: {
                'X-Appwrite-User-Id': 'test-user-simple',
                'X-Appwrite-User-Email': 'simple@test.com'
            }
        });
        
        const dashboardResult = await dashboardResponse.json();
        console.log('Dashboard result:', dashboardResult);
        
        if (!dashboardResult.success) {
            throw new Error('Dashboard failed: ' + dashboardResult.message);
        }
        
        return {
            registration: registerResult,
            dashboard: dashboardResult
        };
        
    } catch (error) {
        console.error('Test flow error:', error);
        throw error;
    }
}

// Test immediately
console.log('Testing registration flow...');
testRegistrationFlow()
    .then(result => {
        console.log('✅ Registration flow test successful!');
        console.log('Results:', result);
    })
    .catch(error => {
        console.error('❌ Registration flow test failed:', error);
    });
