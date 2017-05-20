package org.microbule.gson.util;

import java.io.IOException;
import java.util.function.Function;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class StringValueAdapter<T> extends NullSafeTypeAdapter<T> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Function<T, String> formatter;
    private final Function<String, T> parser;

//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    public static <T> TypeAdapter<T> toStringAdapter(Function<String, T> parser) {
        return new StringValueAdapter<>(Object::toString, parser);
    }

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public StringValueAdapter(Function<T, String> formatter, Function<String, T> parser) {
        this.formatter = formatter;
        this.parser = parser;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    protected final T readNonNull(JsonReader in) throws IOException {
        return this.parser.apply(in.nextString());
    }

    protected final void writeNonNull(JsonWriter out, T value) throws IOException {
        out.value(formatter.apply(value));
    }
}
