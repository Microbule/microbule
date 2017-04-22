package org.microbule.gson.spi;

import com.google.gson.GsonBuilder;

public interface GsonCustomizer {
    void customize(GsonBuilder builder);
}
