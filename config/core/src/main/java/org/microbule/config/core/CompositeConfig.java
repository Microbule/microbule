package org.microbule.config.core;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.microbule.config.api.Config;

public class CompositeConfig implements Config {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final List<Config> members;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public CompositeConfig(Config... members) {
        this(Arrays.asList(members));
    }

    public CompositeConfig(List<Config> members) {
        this.members = members;
    }

//----------------------------------------------------------------------------------------------------------------------
// Config Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Config group(String keyPrefix) {
        return new CompositeConfig(members.stream().map(config -> config.group(keyPrefix)).collect(Collectors.toList()));
    }

    @Override
    public Optional<String> value(String key) {
        return members.stream()
                .map(member -> member.value(key))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}
