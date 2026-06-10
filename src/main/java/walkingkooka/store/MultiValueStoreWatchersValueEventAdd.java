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

/**
 * The event holding parameters for {@link MultiValueStoreWatcher#onValueAdded(Object, Object)}.
 */
final class MultiValueStoreWatchersValueEventAdd<K, V> extends MultiValueStoreWatchersValueEvent<K, V> {

    static <K, V> MultiValueStoreWatchersValueEventAdd<K, V> with(final K id,
                                                                  final V value) {
        return new MultiValueStoreWatchersValueEventAdd<>(
            Objects.requireNonNull(id, "id"),
            Objects.requireNonNull(value, "value")
        );
    }

    private MultiValueStoreWatchersValueEventAdd(final K id,
                                                 final V value) {
        super(id, value);
    }

    // Consumer<MultiValueStoreWatcher>.....................................................................................

    @Override
    public void accept(final MultiValueStoreWatcher<K, V> watcher) {
        watcher.onValueAdded(
            this.id,
            this.value
        );
    }
}
