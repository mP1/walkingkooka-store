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

import walkingkooka.Value;
import walkingkooka.net.header.HasStatus;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;

import java.util.Objects;
import java.util.Optional;

/**
 * This exception is thrown when a load fails, recording a message and possibly the ID itself.
 */
public class MissingStoreException extends StoreException
    implements Value<Optional<?>>,
    HasStatus {

    private static final long serialVersionUID = 1L;

    protected MissingStoreException() {
        this((String) "");
    }

    public MissingStoreException(final String message) {
        this(
            message,
            Optional.empty() // value
        );
    }

    public MissingStoreException(final String message,
                                 final Throwable cause) {
        this(
            message,
            Optional.empty(), // value
            cause
        );
    }

    public MissingStoreException(final HasNotFoundText id) {
        this(
            id.notFoundText(),
            Optional.of(id) // value
        );
    }

    public MissingStoreException(final HasNotFoundText id,
                                 final Throwable cause) {
        this(
            id.notFoundText(),
            Optional.of(id),
            cause
        );
    }

    public MissingStoreException(final String message,
                                 final Optional<?> value) {
        super(
            message
        );
        this.value = Objects.requireNonNull(value, "value");
    }

    public MissingStoreException(final String message,
                                 final Optional<?> value,
                                 final Throwable cause) {
        super(
            message,
            cause
        );
        this.value = Objects.requireNonNull(value, "value");
    }

    // Value............................................................................................................

    @Override
    public Optional<?> value() {
        return this.value;
    }

    private final Optional<?> value;

    // HasStatus........................................................................................................

    @Override
    public Optional<HttpStatus> status() {
        return Optional.of(
            HttpStatusCode.NO_CONTENT.setMessage(
                HttpStatus.firstLineOfText(
                    this.getMessage()
                )
            )
        );
    }
}
