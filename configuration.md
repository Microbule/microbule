# Configuration

Microbule includes a pluggable configuration framework.  Whenever configuration data is requested, each registered *ConfigProvider* is consulted.  The following *ConfigProviders* are included: 

#### System Providers
* System Properties - Java system properties
* Environment Variables - environment variables

#### Container Providers
* Spring - Spring's Environment
* OSGi - a ManagedServiceFactory which receives configuration data from ConfigAdmin

#### Remote Providers
* Consul - retrieves configuration data from Consul
* Etcd - retrieves configuration data from Etcd 

## Search Order
The *ConfigProviders* are ordered according to their *priority*, which defaults to:

* System Properties
* Environment Variables
* Container Providers
* Remote Providers
