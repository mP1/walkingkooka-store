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

import walkingkooka.HasId;
import walkingkooka.reflect.PublicStaticHelper;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Contains many factory methods for a variety of {@link Store} implementations.
 */
public final class Stores implements PublicStaticHelper {

    /**
     * {@see FakeStore}
     */
    public static Store fake() {
        return new FakeStore();
    }

    /**
     * {@see TreeMapStore}
     */
    public static <K extends Comparable<K>, V extends HasId<Optional<K>>> Store<K, V> treeMap(final Comparator<K> idComparator,
                                                                                              final BiFunction<K, V, V> idSetter) {
        return TreeMapStore.with(idComparator, idSetter);
    }

    /**
     * Stop creation
     */
    private Stores() {
        throw new UnsupportedOperationException();
    }
}
