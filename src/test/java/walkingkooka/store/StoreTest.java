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

public final class StoreTest implements HasNotFoundTextTesting,
    ClassTesting<Store<?, ?>> {

    // notFound.........................................................................................................

    @Test
    public void testNotFoundIdNotImplementingHasNotFoundText() {
        this.notFoundAndCheck(
            new FakeStore<Long, Long>() {

            },
            123,
            "Unable to find id: 123"
        );
    }

    @Test
    public void testNotFoundIdImplementingHasNotFoundText() {
        final String message = "Custom has not found text 123";

        this.notFoundAndCheck(
            new FakeStore<HasNotFoundText, Long>() {

            },
            new HasNotFoundText() {
                @Override
                public String notFoundText() {
                    return message;
                }
            },
            message
        );
    }

    private void notFoundAndCheck(final Store<?, ?> store,
                                  final Object id,
                                  final String expected) {
        this.checkEquals(
            expected,
            new FakeStore<HasNotFoundText, Long>() {

            }.notFound(id)
                .getMessage()
        );
    }

    // class............................................................................................................

    @Override
    public Class<Store<?, ?>> type() {
        return Cast.to(Store.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
