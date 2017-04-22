package org.microbule.config.consul;

import com.google.gson.annotations.SerializedName;

public class ConsulNode {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @SerializedName("Key")
    private final String key;

    @SerializedName("Value")
    private final String value;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public ConsulNode(String key, String value) {
        this.key = key;
        this.value = value;
    }

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
