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
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface StoreTesting<S extends Store<K, V>, K, V> extends TreePrintableTesting,
    ClassTesting2<S>,
    ToStringTesting<S> {

    // load.............................................................................................................

    @Test
    default void testLoadNullIdFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStore()
                .load(null)
        );
    }

    default <KK, VV> void loadAndCheck(final Store<KK, VV> store,
                                       final KK id) {
        this.loadAndCheck(
            store,
            id,
            Optional.empty()
        );
    }

    default <KK, VV> void loadAndCheck(final Store<KK, VV> store,
                                       final KK id,
                                       final VV value) {
        this.loadAndCheck(
            store,
            id,
            Optional.of(value)
        );
    }

    default <KK, VV> void loadAndCheck(final Store<KK, VV> store,
                                       final KK id,
                                       final Optional<VV> value) {
        this.checkEquals(
            value,
            store.load(id),
            () -> " store load " + id
        );
    }

    // save.............................................................................................................

    @Test
    default void testSaveNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStore().save(null)
        );
    }

    // delete...........................................................................................................

    @Test
    default void testDeleteNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createStore().delete(null);
        });
    }

    // ids..............................................................................................................

    @Test
    default void testIdsWithInvalidOffsetFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createStore().ids(-1, 0)
        );
    }

    @Test
    default void testIdsWithInvalidCountFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createStore().ids(0, -1)
        );
    }

    @Test
    default void testIdsWithOffsetZeroAndCountZero() {
        this.idsAndCheck(
            this.createStore(),
            0,
            0
        );
    }

    default <KK> void idsAndCheck(final Store<KK, ?> store,
                                  final int offset,
                                  final int count,
                                  final KK... ids) {
        this.idsAndCheck(
            store,
            offset,
            count,
            Sets.of(ids)
        );
    }

    default <KK> void idsAndCheck(final Store<KK, ?> store,
                                  final int offset,
                                  final int count,
                                  final Set<KK> ids) {
        this.checkEquals(
            ids,
            store.ids(
                offset,
                count
            ),
            "ids offset " + offset + " count=" + count);
    }

    // values...........................................................................................................

    @Test
    default void testValuesNegativeOffsetFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createStore().
                values(
                    -1,
                    0
                )
        );
    }

    @Test
    default void testValuesInvalidCountFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createStore()
                .values(
                    0,
                    -1
                )
        );
    }

    @Test
    default void testValueZeroCount() {
        this.valuesAndCheck(
            this.createStore(),
            0, // from
            0 // count
        );
    }

    default <VV> void valuesAndCheck(final Store<?, VV> store,
                                     final int from,
                                     final int count,
                                     final VV... values) {
        this.valuesAndCheck(
            store,
            from,
            count,
            Lists.of(values)
        );
    }

    default <VV> void valuesAndCheck(final Store<?, VV> store,
                                     final int from,
                                     final int count,
                                     final List<VV> values) {
        this.checkEquals(
            values,
            store.values(from, count),
            "values from " + from + " count=" + count
        );
    }

    // first............................................................................................................

    @Test
    default void testFirstIdWhenEmpty() {
        this.checkEquals(
            Optional.empty(),
            this.createStore()
                .firstId()
        );
    }

    @Test
    default void testFirstValueWhenEmpty() {
        this.checkEquals(
            Optional.empty(),
            this.createStore()
                .firstValue()
        );
    }

    // all..............................................................................................................

    @Test
    default void testAllWhenEmpty() {
        this.allAndCheck(
            this.createStore()
        );
    }

    default <KK, VV> void allAndCheck(final Store<KK, VV> store,
                                      final VV... values) {
        this.allAndCheck(
            store,
            Lists.of(values)
        );
    }

    default <KK, VV> void allAndCheck(final Store<KK, VV> store,
                                      final List<VV> values) {
        this.checkEquals(
            values,
            store.all(),
            store::toString
        );
    }

    // between..........................................................................................................

    @Test
    default void testBetweenNullFromFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStore().between(
                null,
                this.id()
            )
        );
    }

    @Test
    default void testBetweenNullToFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStore().between(
                this.id(),
                null
            )
        );
    }

    default <KK, VV> void betweenAndCheck(final Store<KK, VV> store,
                                          final KK from,
                                          final KK to,
                                          final VV... values) {
        this.betweenAndCheck(
            store,
            from,
            to,
            Lists.of(values)
        );
    }

    default <KK, VV> void betweenAndCheck(final Store<KK, VV> store,
                                          final KK from,
                                          final KK to,
                                          final List<VV> values) {
        this.checkEquals(
            values,
            store.between(from, to),
            "values from " + from + " to " + to
        );
    }

    // count............................................................................................................

    default void countAndCheck(final Store<?, ?> store,
                               final int count) {
        this.checkEquals(count, store.count(), () -> "Wrong count " + store);
    }

    S createStore();

    K id();

    V value();

    // addStoreWatcher..................................................................................................

    @Test
    default void testAddStoreWatcherWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStore().addStoreWatcher(null)
        );
    }

    @Test
    default void testAddStoreWatcherAndSave() {
        final V value = this.value();

        final S store = this.createStore();

        final List<V> fired = Lists.array();
        store.addStoreWatcher(
            (ov, nv) ->
                fired.add(
                    nv.orElse(null)
                )
        );

        final V saved = store.save(value);

        this.checkEquals(
            Lists.of(saved),
            fired,
            "fired values"
        );
    }

    @Test
    default void testAddStoreWatcherAndSaveTwiceFiresOnce() {
        final V value = this.value();

        final S store = this.createStore();

        final List<V> fired = Lists.array();
        store.addStoreWatcher(
            (ov, nv) -> fired.add(
                nv.orElse(null)
            )
        );

        final V saved = store.save(value);
        store.save(value);

        this.checkEquals(
            Lists.of(saved),
            fired,
            "fired values"
        );
    }

    @Test
    default void testAddStoreWatcherAndDelete() {
        final V value = this.value();

        final S store = this.createStore();

        final List<V> fired = Lists.array();
        store.addStoreWatcher(
            (ov, nv) -> fired.add(
                nv.orElse(null)
            )
        );

        store.save(value);

        final K id = this.id();
        store.delete(id);

        this.checkEquals(
            Lists.of(
                value,
                null
            ),
            fired,
            "fired values"
        );
    }

    // addStoreWatcherOnce..............................................................................................

    @Test
    default void testAddStoreWatcherOnceWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStore()
                .addStoreWatcherOnce(null)
        );
    }

    @Test
    default void testAddStoreWatcherOnceAndSave() {
        final V value = this.value();

        final S store = this.createStore();

        final List<V> fired = Lists.array();
        store.addStoreWatcherOnce(
            (ov, nv) ->
                fired.add(
                    nv.orElse(null)
                )
        );

        final V saved = store.save(value);

        this.checkEquals(
            Lists.of(saved),
            fired,
            "fired values"
        );
    }

    @Test
    default void testAddStoreWatcherOnceAndDelete() {
        final V value = this.value();

        final S store = this.createStore();

        final List<V> fired = Lists.array();
        store.addStoreWatcherOnce(
            (ov, nv) -> fired.add(
                nv.orElse(null)
            )
        );

        store.save(value);

        final K id = this.id();
        store.delete(id);

        this.checkEquals(
            Lists.of(
                value
            ),
            fired,
            "fired values"
        );
    }

    // class............................................................................................................

    @Override
    default JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
