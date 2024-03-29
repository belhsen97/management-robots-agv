export const environment = {  production: false,
    apiUrl :'http://localhost:8089/management-robot-avg',
    mqttClientConfig:{
        hostname: 'localhost',
        port: 8083,
        path: '/mqtt',
        clean: true, // Retain session
        connectTimeout: 4000, // Timeout period
        reconnectPeriod: 4000, // Reconnect period
        // Authentication information
        clientId: 'front',
        username: 'test',
        password: 'test',
        //protocol: 'ws'
        protocol: 'ws' as 'ws',
        //connectOnCreate: false,
      }
};
