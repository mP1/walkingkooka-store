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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface StoreTesting<S extends Store<K, V>, K, V> extends ClassTesting2<S>,
    ToStringTesting<S> {

    @Test
    default void testLoadNullIdFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStore()
                .load(null)
        );
    }

    @Test
    default void testSaveNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStore().save(null)
        );
    }

    @Test
    default void testAddSaveWatcherNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createStore().addSaveWatcher(null)
        );
    }

    @Test
    default void testAddSaveWatcherAndSave() {
        final V value = this.value();

        final S store = this.createStore();

        final List<V> fired = Lists.array();
        store.addSaveWatcher((s) -> fired.add(s));

        final V saved = store.save(value);

        this.checkEquals(
            Lists.of(saved),
            fired,
            "fired values"
        );
    }

    @Test
    default void testAddSaveWatcherAndSaveTwiceFiresOnce() {
        final V value = this.value();

        final S store = this.createStore();

        final List<V> fired = Lists.array();
        store.addSaveWatcher((s) -> fired.add(s));

        final V saved = store.save(value);
        store.save(value);

        this.checkEquals(
            Lists.of(saved),
            fired,
            "fired values"
        );
    }

    @Test
    default void testAddSaveWatcherAndRemove() {
        this.createStore().addSaveWatcher((v) -> {
        }).run();
    }

    @Test
    default void testDeleteNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createStore().delete(null);
        });
    }

    @Test
    default void testAddDeleteWatcherNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createStore().addDeleteWatcher(null);
        });
    }

    @Test
    default void testAddDeleteWatcherAndRemove() {
        this.createStore().addDeleteWatcher((k) -> {
        }).run();
    }

    @Test
    default void testAddDeleteWatcherAndDelete() {
        final V value = this.value();

        final S store = this.createStore();

        final List<K> fired = Lists.array();
        store.addDeleteWatcher((d) -> fired.add(d));

        store.save(value);

        final K id = this.id();
        store.delete(id);

        this.checkEquals(
            Lists.of(id),
            fired,
            "fired values"
        );
    }

    @Test
    default void testIdsInvalidFromFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createStore().ids(-1, 0)
        );
    }

    @Test
    default void testIdsInvalidCountFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createStore().ids(0, -1)
        );
    }

    @Test
    default void testIdsOffset0AndCountZero() {
        this.idsAndCheck(
            this.createStore(),
            0,
            0
        );
    }

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

    @Test
    default void testFirstIdWhenEmpty() {
        this.checkEquals(Optional.empty(),
            this.createStore().firstId());
    }

    @Test
    default void testFirstValueWhenEmpty() {
        this.checkEquals(Optional.empty(),
            this.createStore().firstValue());
    }

    @Test
    default void testAllWhenEmpty() {
        this.checkEquals(
            Lists.empty(),
            this.createStore().all()
        );
    }

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

    S createStore();

    K id();

    V value();

    default <KK, VV> void loadAndCheck(final Store<KK, VV> store,
                                       final KK id,
                                       final VV value) {
        this.checkEquals(
            Optional.of(value),
            store.load(id),
            () -> " store load " + id
        );
    }

    default void loadFailCheck(final K id) {
        this.loadFailCheck(this.createStore(), id);
    }

    default <KK, VV> void loadFailCheck(final Store<KK, VV> store,
                                        final KK id) {
        final Optional<VV> value = store.load(id);
        this.checkEquals(
            Optional.empty(),
            value,
            () -> "Expected id " + id + " to fail"
        );
    }

    default void countAndCheck(final Store<?, ?> store,
                               final int count) {
        this.checkEquals(count, store.count(), () -> "Wrong count " + store);
    }

    default <KK> void idsAndCheck(final Store<KK, ?> store,
                                  final int from,
                                  final int to,
                                  final KK... ids) {
        this.idsAndCheck(
            store,
            from,
            to,
            Sets.of(ids)
        );
    }

    default <KK> void idsAndCheck(final Store<KK, ?> store,
                                  final int from,
                                  final int to,
                                  final Set<KK> ids) {
        this.checkEquals(ids,
            store.ids(from, to),
            "ids from " + from + " count=" + to);
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
            "values from " + from + " to " + to);
    }

    @Override
    default JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
