// Initialize Appwrite client
const client = new Appwrite.Client();
client
    .setEndpoint('https://nyc.cloud.appwrite.io/v1')
    .setProject('6894d20300386b659e32');

// Initialize Appwrite account
const account = new Appwrite.Account(client);
