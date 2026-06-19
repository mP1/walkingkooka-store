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

import java.util.Objects;
import java.util.Optional;

/**
 * An adapter between {@link StoreWatcher} and {@link MultiValueStoreWatcher}.
 */
final class MultiValueStoreStoreWatcher<K, V> implements MultiValueStoreWatcher<K, V> {

    static <K, V> MultiValueStoreStoreWatcher<K, V> with(final StoreWatcher<V> watcher) {
        return new MultiValueStoreStoreWatcher<>(
            Objects.requireNonNull(watcher, "watcher")
        );
    }

    private MultiValueStoreStoreWatcher(final StoreWatcher<V> watcher) {
        super();
        this.watcher = watcher;
    }

    // MultiValueStoreWatcher...........................................................................................

    @Override
    public void onValueAdded(final K id,
                             final V value) {
        // IGNORE
    }

    @Override
    public void onValueRemoved(final K id,
                               final V value) {
        // IGNORE
    }

    @Override
    public void onValueChange(final Optional<V> oldValue,
                              final Optional<V> newValue) {
        this.watcher.onValueChange(
            oldValue,
            newValue
        );
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.watcher.toString();
    }

    private final StoreWatcher<V> watcher;
}
