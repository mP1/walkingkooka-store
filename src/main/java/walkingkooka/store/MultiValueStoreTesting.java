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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface MultiValueStoreTesting<S extends MultiValueStore<K, V>, K, V> extends StoreTesting<S, K, V> {

    // addValue.........................................................................................................

    @Test
    default void testAddValueNullIdFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStore()
                .addValue(
                    null,
                    this.value()
                )
        );
    }

    @Test
    default void testAddValueNullValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStore()
                .addValue(
                    this.id(),
                    null
                )
        );
    }

    // removeValue......................................................................................................

    @Test
    default void testRemoveValueNullIdFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStore()
                .removeValue(
                    null,
                    this.value()
                )
        );
    }

    @Test
    default void testRemoveValueNullValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStore()
                .removeValue(
                    this.id(),
                    null
                )
        );
    }

    // removeByValue....................................................................................................

    @Test
    default void testRemoveByValueNullValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStore()
                .removeByValue(
                    null
                )
        );
    }

    // findValues.......................................................................................................

    @Test
    default void testFindValuesByIdWithNullIdFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStore()
                .findValuesById(
                    null,
                    0,
                    0
                )
        );
    }

    @Test
    default void testFindValuesByIdWithInvalidOffsetFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createStore()
                .findValuesById(
                    this.id(),
                    -1,
                    0
                )
        );
    }

    @Test
    default void testFindValuesByIdWithInvalidCountFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createStore()
                .findValuesById(
                    this.id(),
                    0,
                    -1
                )
        );
    }

    @Test
    default void testFindValuesByIdWithOffsetZeroAndCountZero() {
        this.findValuesByIdAndCheck2(
            this.createStore(),
            this.id(),
            0,
            0
        );
    }

    default <KK, VV> void findValuesByIdAndCheck(final MultiValueStore<KK, VV> store,
                                                 final KK id,
                                                 final VV... values) {
        this.findValuesByIdAndCheck(
            store,
            id,
            Lists.of(values)
        );
    }

    default <KK, VV> void findValuesByIdAndCheck(final MultiValueStore<KK, VV> store,
                                                 final KK id,
                                                 final List<VV> values) {
        this.findValuesByIdAndCheck2(
            store,
            id,
            0,
            Integer.MAX_VALUE,
            values
        );
    }

    default <KK, VV> void findValuesByIdAndCheck2(final MultiValueStore<KK, VV> store,
                                                  final KK id,
                                                  final int offset,
                                                  final int count,
                                                  final VV... values) {
        this.findValuesByIdAndCheck2(
            store,
            id,
            offset,
            count,
            Lists.of(values)
        );
    }

    default <KK, VV> void findValuesByIdAndCheck2(final MultiValueStore<KK, VV> store,
                                                  final KK id,
                                                  final int offset,
                                                  final int count,
                                                  final List<VV> values) {
        this.checkEquals(
            values,
            store.findValuesById(
                id,
                offset,
                count
            )
        );
    }

    // findIdsByValue...................................................................................................

    @Test
    default void testFindIdsByValueWithNullValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStore()
                .findIdsByValue(
                    null,
                    0,
                    0
                )
        );
    }

    @Test
    default void testFindIdsByValueWithInvalidOffsetFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createStore()
                .findIdsByValue(
                    this.value(),
                    -1,
                    0
                )
        );
    }

    @Test
    default void testFindIdsByValueWithInvalidCountFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createStore()
                .findIdsByValue(
                    this.value(),
                    0,
                    -1
                )
        );
    }

    @Test
    default void testFindIdsByValueWithOffsetZeroAndCountZero() {
        this.findIdsByValueAndCheck(
            this.createStore(),
            this.value(),
            0,
            0
        );
    }

    default <KK, VV> void findIdsByValueAndCheck(final MultiValueStore<KK, VV> store,
                                                 final VV value,
                                                 final KK... ids) {
        this.findIdsByValueAndCheck(
            store,
            value,
            Lists.of(ids)
        );
    }

    default <KK, VV> void findIdsByValueAndCheck(final MultiValueStore<KK, VV> store,
                                                 final VV value,
                                                 final List<KK> ids) {
        this.findIdsByValueAndCheck(
            store,
            value,
            0,
            Integer.MAX_VALUE,
            ids
        );
    }

    default <KK, VV> void findIdsByValueAndCheck(final MultiValueStore<KK, VV> store,
                                                 final VV value,
                                                 final int offset,
                                                 final int count,
                                                 final KK... ids) {
        this.findIdsByValueAndCheck(
            store,
            value,
            offset,
            count,
            Lists.of(ids)
        );
    }

    default <KK, VV> void findIdsByValueAndCheck(final MultiValueStore<KK, VV> store,
                                                 final VV value,
                                                 final int offset,
                                                 final int count,
                                                 final List<KK> ids) {
        this.checkEquals(
            ids,
            store.findIdsByValue(
                value,
                offset,
                count
            ),
            () -> "findIds "
        );
    }

    // addStoreWatcherOnce..............................................................................................

    default void testAddStoreWatcherOnceWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStore()
                .addStoreWatcherOnce(
                    (MultiValueStoreWatcher<K, V>) null
                )
        );
    }

    // Store............................................................................................................

    @Override
    default void testAddStoreWatcherAndDelete() {
        throw new UnsupportedOperationException();
    }

    @Override
    default void testAddStoreWatcherAndSave() {
        throw new UnsupportedOperationException();
    }

    @Override
    default void testAddStoreWatcherAndSaveTwiceFiresOnce() {
        throw new UnsupportedOperationException();
    }
}
