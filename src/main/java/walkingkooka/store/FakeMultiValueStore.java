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

public class FakeMultiValueStore<K, V> extends FakeStore<K, V> implements MultiValueStore<K, V> {

    public FakeMultiValueStore() {
        super();
    }

    // MultiValueStore..................................................................................................

    @Override
    public void addValue(final K id,
                         final V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(final K id,
                       final V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<V> findValuesById(final K id,
                                  final int offset,
                                  final int count) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<K> findIdsByValue(final V value,
                                  final int offset,
                                  final int count) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addStoreWatcher(final MultiValueStoreWatcher<K, V> watcher) {
        throw new UnsupportedOperationException();
    }
}
