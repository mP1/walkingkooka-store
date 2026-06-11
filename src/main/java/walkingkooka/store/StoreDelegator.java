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
import java.util.Set;

public interface StoreDelegator<K, V> extends Store<K, V> {

    @Override
    default Optional<V> load(final K id) {
        return this.store()
            .load(id);
    }

    @Override
    default V save(final V value) {
        return this.store()
            .save(value);
    }

    @Override
    default void delete(final K id) {
        this.store()
            .delete(id);
    }

    @Override
    default int count() {
        return this.store()
            .count();
    }

    @Override
    default Set<K> ids(final int offset,
                       final int count) {
        return this.store()
            .ids(
                offset,
                count
            );
    }

    @Override
    default List<V> values(final int offset,
                           final int count) {
        return this.store()
            .values(
                offset,
                count
            );
    }

    @Override
    default List<V> between(final K from,
                            final K to) {
        return this.store()
            .between(
                from,
                to
            );
    }

    @Override
    default Runnable addStoreWatcher(final StoreWatcher<V> watcher) {
        return this.store()
            .addStoreWatcher(watcher);
    }

    Store<K, V> store();
}
