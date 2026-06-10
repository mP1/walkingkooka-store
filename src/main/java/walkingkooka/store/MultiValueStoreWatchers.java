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


import walkingkooka.watch.Watchers;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A collection of {@link MultiValueStoreWatcher}. Note the event is only fired to watchers if the old and new values
 * are different.
 */
public final class MultiValueStoreWatchers<K, V> implements MultiValueStoreWatcher<K, V> {

    public static <K, V> MultiValueStoreWatchers<K, V> empty() {
        return new MultiValueStoreWatchers<>();
    }

    private MultiValueStoreWatchers() {
        super();
    }

    public Runnable add(final MultiValueStoreWatcher<K, V> watcher) {
        Objects.requireNonNull(watcher, "watcher");

        return this.watchers.add(
            (e) -> e.accept(watcher)
        );
    }

    public Runnable addOnce(final MultiValueStoreWatcher<K, V> watcher) {
        Objects.requireNonNull(watcher, "watcher");

        return this.watchers.add(
            (e) -> e.accept(watcher)
        );
    }

    // MultiValueStoreWatcher...........................................................................................

    @Override
    public void onValueAdded(final K id,
                             final V value) {
        this.watchers.accept(
            MultiValueStoreWatchersValueEvent.add(
                id,
                value
            )
        );
    }

    @Override
    public void onValueRemoved(final K id,
                               final V value) {
        this.watchers.accept(
            MultiValueStoreWatchersValueEvent.remove(
                id,
                value
            )
        );
    }

    private final Watchers<Consumer<MultiValueStoreWatcher<K, V>>> watchers = Watchers.empty();

    /**
     * Note the event is only fired if the old and new values are different.
     */
    @Override
    public void onValueChange(final Optional<V> oldValue,
                              final Optional<V> newValue) {
        throw new UnsupportedOperationException();
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.watchers.toString();
    }
}
