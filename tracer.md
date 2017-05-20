# Tracer

Microbule will generate a the following random UUID header values automatically:

- **Microbule-Trace-ID**: a unique value corresponding to a "transaction."  If the header is found on the request, the
existing value will be used.
- **Microbule-Request-ID**: a unique value corresponding to the request itself.  This is generated for each and every
request.

A typical response will contain the following headers:

 ```
 Microbule-Request-ID=[3a5bf571-99fd-4c6b-b79e-7ff0a28e9350]
 Microbule-Trace-ID=[da3e59f4-8aff-4a89-bc30-7ad310fecf63]
 ```
