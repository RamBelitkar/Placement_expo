// Initialize Appwrite client
const client = new Appwrite.Client();

// Configure client with current origin
const currentOrigin = window.location.origin;
console.log('Current origin:', currentOrigin);

client
    .setEndpoint('https://nyc.cloud.appwrite.io/v1')
    .setProject('6894d20300386b659e32');

// Add CORS and authentication headers
client.headers = {
    ...client.headers,
    'X-Appwrite-Response-Format': '1.0.0',
    'X-Fallback-Cookies': 'true',
    'Origin': currentOrigin
};

// Initialize Appwrite account with full scope
const account = new Appwrite.Account(client);
