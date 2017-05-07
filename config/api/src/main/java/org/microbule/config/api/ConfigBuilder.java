package org.microbule.config.api;

public interface ConfigBuilder {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    Config build();

    ConfigBuilder withCustom(Config custom);

    ConfigBuilder withPath(String... path);
}
