package org.microbule.gson.util;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public abstract class NullSafeTypeAdapter<T> extends TypeAdapter<T> {
//----------------------------------------------------------------------------------------------------------------------
// Abstract Methods
//----------------------------------------------------------------------------------------------------------------------

    protected abstract T readNonNull(JsonReader var1) throws IOException;

    protected abstract void writeNonNull(JsonWriter var1, T var2) throws IOException;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public final T read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            return this.readNonNull(in);
        }
    }

    public final void write(JsonWriter out, T value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            this.writeNonNull(out, value);
        }
    }
}
