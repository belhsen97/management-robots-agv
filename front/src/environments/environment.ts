export const environment = {
  production: false,
  apiUrl: 'http://localhost:8089/management-robot',
  mqttClientConfig: {
    hostname: 'localhost',
    port: 8083,
    path: '/mqtt',
    // keepalive: 60,      // Keep Alive interval in seconds (e.g., 60 seconds)
    reconnectPeriod: 4000,    // 0 Disable automatic reconnection by the way default value is 4000
    //  clean: true, // Retain session
    //connectTimeout: 4000, // Timeout period
    // Authentication information
    clientId: 'front',
    username: 'admin',
    password: 'admin',
    protocol: 'ws' as 'ws',  //protocol: 'ws'
    connectOnCreate: true,
  }
};
