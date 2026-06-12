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

import walkingkooka.reflect.PublicStaticHelper;

import java.util.Comparator;

/**
 * Contains many factory methods for a variety of {@link MultiValueStore} implementations.
 */
public final class MultiValueStores implements PublicStaticHelper {

    /**
     * {@see FakeMultiValueStore}
     */
    public static <K, V> Store<K, V> fake() {
        return new FakeMultiValueStore<>();
    }

    /**
     * {@see TreeMapMultiValueStore}
     */
    public static <K, V> MultiValueStore<K, V> treeMap(final Comparator<K> idComparator) {
        return TreeMapMultiValueStore.with(idComparator);
    }

    /**
     * Stop creation
     */
    private MultiValueStores() {
        throw new UnsupportedOperationException();
    }
}
