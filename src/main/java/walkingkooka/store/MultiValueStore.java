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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link Store} that supports more than one value for a particular parent key.}
 */
public interface MultiValueStore<K, V> extends Store<K, V> {

    void addValue(final K id,
                  final V value);

    void removeValue(final K id,
                     final V value);

    List<V> findValuesById(final K id,
                           final int offset,
                           final int count);

    List<K> findIdsByValue(final V value,
                           final int offset,
                           final int count);

    /**
     * Adds a {@link MultiValueStoreWatcher}
     */
    Runnable addStoreWatcher(final MultiValueStoreWatcher<K, V> watcher);

    // Store............................................................................................................

    @Override
    default Optional<V> load(final K id) {
        Objects.requireNonNull(id, "id");
        throw new UnsupportedOperationException();
    }

    @Override
    default V save(final V value) {
        Objects.requireNonNull(value, "value");
        throw new UnsupportedOperationException();
    }

    /**
     * Count the number of values not ids.
     */
    @Override
    int count();

    @Override
    default Runnable addStoreWatcher(final StoreWatcher<V> watcher) {
        Objects.requireNonNull(watcher, "watcher");

        return this.addStoreWatcher(
            new MultiValueStoreWatcher<>() {
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
                    watcher.onValueChange(
                        oldValue,
                        newValue
                    );
                }

                // Object................................................................................................

                @Override
                public String toString() {
                    return watcher.toString();
                }
            }
        );
    }
}
