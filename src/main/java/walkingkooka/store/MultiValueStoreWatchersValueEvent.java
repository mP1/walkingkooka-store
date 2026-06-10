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

import walkingkooka.ToStringBuilder;
import walkingkooka.UsesToStringBuilder;

import java.util.function.Consumer;

/**
 * The event payload used by {@link MultiValueStoreWatchers}.
 */
abstract class MultiValueStoreWatchersValueEvent<K, V> implements Consumer<MultiValueStoreWatcher<K, V>>,
    UsesToStringBuilder {

    static <K, V> MultiValueStoreWatchersValueEventAdd<K, V> add(final K id,
                                                                 final V value) {
        return MultiValueStoreWatchersValueEventAdd.with(
            id,
            value
        );
    }

    static <K, V> MultiValueStoreWatchersValueEvent<K, V> remove(final K id,
                                                                 final V value) {
        return MultiValueStoreWatchersValueEventRemove.with(
            id,
            value
        );
    }

    MultiValueStoreWatchersValueEvent(final K id,
                                      final V value) {
        super();
        this.id = id;
        this.value = value;
    }

    // Consumer<MultiValueStoreWatcher>.................................................................................

    final K id;
    final V value;

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return ToStringBuilder.buildFrom(this);
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    public final void buildToString(final ToStringBuilder b) {
        b.value(this.id);
        b.value(this.value);
    }
}
