/*
 * Copyright (c) 2017 The Microbule Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.microbule.gson.api;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public interface GsonService {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    /**
     * Appends an object's JSON value to the supplied {@link Appendable} object.
     *
     * @param src    the object
     * @param writer the appendable
     * @param <A>    the appendable type
     * @return the appendable
     */
    <A extends Appendable> A append(Object src, A writer);

    /**
     * Appends an {@link JsonElement}'s JSON value to the supplied {@link Appendable} object.
     *
     * @param json   the element
     * @param writer the appendable
     * @param <A>    the appendable type
     * @return the appendable
     */
    <A extends Appendable> A append(JsonElement json, A writer);

    /**
     * Appends an object's JSON value to the supplied {@link Appendable} object.
     *
     * @param src    the object
     * @param type   the object's type (to be used in the case of generics)
     * @param writer the appendable
     * @param <A>    the appendable type
     * @return the appendable
     */
    <A extends Appendable> A append(Object src, Type type, A writer);
    /**
     * Allows access to the {@link Gson} instance in order to create some object.  Do NOT maintain a reference to the
     * {@link Gson} object!
     *
     * @param function a function
     * @param <T>      the type to return
     * @return the value created by the function
     */
    <T> T createWithGson(Function<Gson, T> function);

    /**
     * Allows access to the {@link Gson} instance.  Do NOT maintain a reference to the supplied {@link Gson} object!
     *
     * @param consumer a consumer
     */
    void doWithGson(Consumer<Gson> consumer);

    /**
     * Parses an object from the supplied {@link Reader}.
     *
     * @param json the reader containing the JSON
     * @param type the generic type
     * @param <T>  the object type to return (uses type inference)
     * @return the parsed object
     */
    <T> T parse(Reader json, Type type);

    /**
     * Parses an object from the supplied {@link JsonElement}.
     *
     * @param json the JSON element
     * @param type the type
     * @param <T>  the object type
     * @return the parsed object
     */
    <T> T parse(JsonElement json, Class<T> type);

    /**
     * Parses an object from the supplied {@link JsonElement}.
     *
     * @param json the JSON element
     * @param type the generic type
     * @param <T>  the object type (uses type inference)
     * @return the parsed object
     */
    <T> T parse(JsonElement json, Type type);

    /**
     * Parses an object from the supplied JSON string.
     *
     * @param json the JSON string
     * @param type the type
     * @param <T>  the object type
     * @return the parsed object
     */
    <T> T parse(String json, Class<T> type);

    /**
     * Parses an object from the supplied JSON string.
     *
     * @param json the JSON string
     * @param type the generic type
     * @param <T>  the object type
     * @return the parsed object
     */
    <T> T parse(String json, Type type);

    /**
     * Parses an object from the supplied {@link Reader}.
     *
     * @param json the reader containing the JSON
     * @param type the type
     * @param <T>  the object type to return
     * @return the parsed object
     */
    <T> T parse(Reader json, Class<T> type);

    /**
     * Transforms an input object into a {@link JsonElement}.
     * @param src the source object
     * @param <T> the JSON element type (uses type inference)
     * @return the parsed JSON element
     */
    <T extends JsonElement> T toJson(Object src);

    /**
     * Transforms an input object into a {@link JsonElement}.
     * @param src the source object
     * @param type the source object's generic type
     * @param <T> the JSON element type (uses type inference)
     * @return the parsed JSON element
     */
    <T extends JsonElement> T toJson(Object src, Type type);

    /**
     * Transforms an object into its JSON string representation.
     * @param src the source object
     * @return the JSON string
     */
    String toJsonString(Object src);

    /**
     * Transforms a {@link JsonElement} into its JSON string representation.
     * @param json the JSON element
     * @return the JSON string
     */
    String toJsonString(JsonElement json);

    /**
     * Transforms an object into its JSON string representation.
     * @param src the source object
     * @param type the source object's generic type
     * @return the JSON string
     */
    String toJsonString(Object src, Type type);
}
