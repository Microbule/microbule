# Connection/Receive Timeouts

Microbule will automatically set connection and receive timeouts (10 sec and 30 sec by default) on all generated client
proxies.  You can customize the timeout values using configuration properties when you create the proxy.  For example,
you can set the "receiveTimeout" property to a value (in milliseconds) to be used by all calls for the proxy.  
Alternatively, you can customize per-method (we use the method name as the key) timeouts by setting the
"foo.connectionTimeout" value, which would override the connection timeout value for the "foo" method
only.
