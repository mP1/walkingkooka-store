/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.store;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class MultiValueStoreWatchersTest implements ClassTesting<MultiValueStoreWatchers<Void, Void>> {

    @Test
    public void testAddValueWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> MultiValueStoreWatchers.empty()
                .add(null)
        );
    }

    @Test
    public void testAddValueOnceWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> MultiValueStoreWatchers.empty()
                .addOnce(null)
        );
    }

    private final static String ID = "Id111";

    private final static Integer VALUE = 111;

    @Test
    public void testOnValueAdded() {
        this.fired = false;

        final MultiValueStoreWatchers<String, Integer> watchers = MultiValueStoreWatchers.empty();
        watchers.add(
            new MultiValueStoreWatcher<>() {
                @Override
                public void onValueAdded(final String id,
                                         final Integer value) {
                    checkEquals(ID, id);
                    checkEquals(VALUE, value);
                    MultiValueStoreWatchersTest.this.fired = true;
                }

                @Override
                public void onValueRemoved(final String id,
                                           final Integer value) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void onValueChange(final Optional<Integer> oldValue,
                                          final Optional<Integer> newValue) {
                    throw new UnsupportedOperationException();
                }
            }
        );
        watchers.onValueAdded(
            ID,
            VALUE
        );

        this.checkEquals(
            true,
            this.fired
        );
    }

    @Test
    public void testOnValueRemoved() {
        this.fired = false;

        final MultiValueStoreWatchers<String, Integer> watchers = MultiValueStoreWatchers.empty();
        watchers.add(
            new MultiValueStoreWatcher<>() {
                @Override
                public void onValueAdded(final String id,
                                         final Integer value) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void onValueRemoved(final String id,
                                           final Integer value) {
                    checkEquals(ID, id);
                    checkEquals(VALUE, value);
                    MultiValueStoreWatchersTest.this.fired = true;
                }

                @Override
                public void onValueChange(final Optional<Integer> oldValue,
                                          final Optional<Integer> newValue) {
                    throw new UnsupportedOperationException();
                }
            }
        );
        watchers.onValueRemoved(
            ID,
            VALUE
        );

        this.checkEquals(
            true,
            this.fired
        );
    }

    private boolean fired;

    // Class............................................................................................................

    @Override
    public Class<MultiValueStoreWatchers<Void, Void>> type() {
        return Cast.to(MultiValueStoreWatchers.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
