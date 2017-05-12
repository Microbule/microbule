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

package org.microbule.test.core;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.ServerSocket;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class MicrobuleTestCase extends Assert {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Rule
    public final ExpectedException exception = ExpectedException.none();

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    protected void assertIsUtilsClass(Class<?> clazz) throws Exception {
        assertTrue(Modifier.isFinal(clazz.getModifiers()));
        assertEquals(1, clazz.getDeclaredConstructors().length);
        final Constructor<?> ctor = clazz.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(ctor.getModifiers()));
        ctor.setAccessible(true);
        ctor.newInstance();
    }

    protected void expectException(Class<? extends Exception> type, String msg, Object... params) {
        exception.expect(type);
        exception.expectMessage(String.format(msg, params));
    }

    protected int findFreePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }
}
