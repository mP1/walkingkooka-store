/*
 * Copyright 2024 Miroslav Pokorny (github.com/mP1)
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
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class StoreWatcher2Test implements ClassTesting2<StoreWatcher2<Locale>> {

    // onValueChange....................................................................................................

    @Test
    public void testOnValueChangeWithNullOldValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> new FakeStoreWatcher2()
                .onValueChange(
                    null,
                    Optional.empty()
                )
        );
    }

    @Test
    public void testOnValueChangeWithNullNewValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> new FakeStoreWatcher2()
                .onValueChange(
                    Optional.empty(),
                    null
                )
        );
    }

    // onValueChangeAdd.................................................................................................

    @Test
    public void testOnValueChangeAdd() {
        this.fired = false;

        final Locale value = Locale.ENGLISH;

        new FakeStoreWatcher2() {
            @Override
            public void onValueChangeAdd(final Locale nv) {
                checkEquals(value, nv, "newValue");

                StoreWatcher2Test.this.fired = true;
            }
        }.onValueChange(
            Optional.empty(),
            Optional.of(value)
        );

        this.checkEquals(
            true,
            this.fired
        );
    }

    // onValueChangeRemove..............................................................................................

    @Test
    public void testOnValueChangeRemove() {
        this.fired = false;

        final Locale value = Locale.ENGLISH;

        new FakeStoreWatcher2() {

            @Override
            public void onValueChangeRemove(final Locale ov) {
                checkEquals(value, ov, "oldValue");

                StoreWatcher2Test.this.fired = true;
            }

        }.onValueChange(
            Optional.of(value),
            Optional.empty()
        );

        this.checkEquals(
            true,
            this.fired
        );
    }

    // onValueChangeUpdate..............................................................................................

    @Test
    public void testOnValueChangeUpdate() {
        this.fired = false;

        final Locale oldValue = Locale.ENGLISH;
        final Locale newValue = Locale.FRENCH;

        new FakeStoreWatcher2() {

            @Override
            public void onValueChangeReplace(final Locale ov,
                                             final Locale nv) {
                checkEquals(oldValue, ov, "oldValue");
                checkEquals(newValue, nv, "newValue");

                StoreWatcher2Test.this.fired = true;
            }
        }.onValueChange(
            Optional.of(oldValue),
            Optional.of(newValue)
        );

        this.checkEquals(
            true,
            this.fired
        );
    }

    private boolean fired;

    static class FakeStoreWatcher2 implements StoreWatcher2<Locale> {

        @Override
        public void onValueChangeAdd(final Locale nv) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void onValueChangeRemove(final Locale ov) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void onValueChangeReplace(final Locale ov,
                                         final Locale nv) {
            throw new UnsupportedOperationException();
        }
    }

    // class............................................................................................................

    @Override
    public Class<StoreWatcher2<Locale>> type() {
        return Cast.to(StoreWatcher2.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
