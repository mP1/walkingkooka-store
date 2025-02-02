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
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.StandardThrowableTesting;

import java.util.Optional;

public final class MissingStoreExceptionTest implements StandardThrowableTesting<MissingStoreException> {

    @Test
    public void testNewHasNotFoundText() {
        this.newAndCheck(
            new MissingStoreException(HAS_NOT_FOUND_TEXT),
            NOT_FOUND_MESSAGE,
            Optional.of(HAS_NOT_FOUND_TEXT),
            null // cause
        );
    }

    @Test
    public void testNewHasNotFoundTextAndCause() {
        final RuntimeException cause = new RuntimeException("Cause 123");

        this.newAndCheck(
            new MissingStoreException(
                HAS_NOT_FOUND_TEXT,
                cause
            ),
            NOT_FOUND_MESSAGE,
            Optional.of(HAS_NOT_FOUND_TEXT),
            cause // cause
        );
    }

    private final static String ID = "Id999";

    @Test
    public void testNewMessageAndValue() {
        this.newAndCheck(
            new MissingStoreException(
                MESSAGE,
                Optional.of(ID)
            ),
            MESSAGE,
            Optional.of(ID),
            null // cause
        );
    }

    @Test
    public void testNewMessageAndValueAndCause() {
        final RuntimeException cause = new RuntimeException("Cause 123");

        this.newAndCheck(
            new MissingStoreException(
                MESSAGE,
                Optional.of(ID),
                cause
            ),
            MESSAGE,
            Optional.of(ID),
            cause // cause
        );
    }

    private void newAndCheck(final MissingStoreException exception,
                             final String message,
                             final Optional<?> value,
                             final Throwable cause) {
        this.checkEquals(
            message,
            exception.getMessage(),
            "message"
        );

        this.checkEquals(
            value,
            exception.value(),
            "value"
        );

        this.checkEquals(
            cause,
            exception.getCause(),
            "cause"
        );
    }

    private final static String NOT_FOUND_MESSAGE = "Not found message 123";

    private final static TestHasNotFoundText HAS_NOT_FOUND_TEXT = new TestHasNotFoundText();

    static class TestHasNotFoundText implements HasNotFoundText {

        TestHasNotFoundText() {
            super();
        }

        @Override
        public String notFoundText() {
            return NOT_FOUND_MESSAGE;
        }
    }

    @Override
    public MissingStoreException createThrowable(final String message) {
        return new MissingStoreException(message);
    }

    @Override
    public MissingStoreException createThrowable(final String message, final Throwable cause) {
        return new MissingStoreException(message, cause);
    }

    // class............................................................................................................

    @Override
    public Class<MissingStoreException> type() {
        return MissingStoreException.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
