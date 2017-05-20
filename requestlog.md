# Request Logging

Microbule will automatically log (at INFO level) all request BEGIN/END events along with timings.  For example:

```
 2016-11-21 10:57:01,568  INFO RequestLogFilter - BEGIN GET /hello/Microbule
 2016-11-21 10:57:01,583  INFO RequestLogFilter - END   GET /hello/Microbule - 200 OK (0.001 sec)
 ```
