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
import java.util.Optional;

public interface MultiValueStoreDelegator<K, V> extends StoreDelegator<K, V>,
    MultiValueStore<K, V> {

    // Store............................................................................................................

    @Override
    default Optional<V> load(final K id) {
        return MultiValueStore.super.load(id);
    }

    @Override
    default V save(final V value) {
        return MultiValueStore.super.save(value);
    }

    @Override
    default Runnable addStoreWatcher(final StoreWatcher<V> watcher) {
        return MultiValueStore.super.addStoreWatcher(watcher);
    }

    @Override
    default Store<K, V> store() {
        return this.multiValueStore();
    }

    // MultiValueStore..................................................................................................

    @Override
    default void add(final K id,
                     final V value) {
        this.multiValueStore()
            .add(
                id,
                value
            );
    }

    @Override
    default void remove(final K id,
                        final V value) {
        this.multiValueStore()
            .remove(
                id,
                value
            );
    }

    @Override
    default List<V> findValuesById(final K id,
                                   final int offset,
                                   final int count) {
        return this.multiValueStore()
            .findValuesById(
                id,
                offset,
                count
            );
    }

    @Override
    default List<K> findIdsByValue(final V value,
                                   final int offset,
                                   final int count) {
        return this.multiValueStore()
            .findIdsByValue(
                value,
                offset,
                count
            );
    }

    @Override
    default Runnable addStoreWatcher(final MultiValueStoreWatcher<K, V> watcher) {
        return this.multiValueStore()
            .addStoreWatcher(watcher);
    }

    MultiValueStore<K, V> multiValueStore();
}
