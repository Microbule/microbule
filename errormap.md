# Error Mapping

Microbule will inject its own JAX-RS ExceptionMappers into the services by default.  These ExceptionMappers will use
the ErrorMapperService to create responses when an exception happens.  The default format of the error response will be
a plain text response with newline-separated error messages.  You can override the default format by providing an
ErrorResponseStrategy.
