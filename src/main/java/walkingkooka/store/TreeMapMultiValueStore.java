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

import walkingkooka.collect.list.ImmutableList;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.ImmutableSet;
import walkingkooka.collect.set.Sets;

import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A {@link Store} that shares a {@link TreeMap} and automatically allocates an ID if saving a value without an ID.
 * This store is intended to be decorated sharing the map.
 */
final class TreeMapMultiValueStore<K, V> implements MultiValueStore<K, V> {

    /**
     * Factory that creates a new {@link TreeMapMultiValueStore}.
     */
    static <K, V> TreeMapMultiValueStore<K, V> with(final Comparator<K> idComparator) {
        Objects.requireNonNull(idComparator, "idComparator");

        return new TreeMapMultiValueStore<>(idComparator);
    }

    /**
     * Private ctor
     */
    private TreeMapMultiValueStore(final Comparator<K> idComparator) {
        super();

        this.idToValues = Maps.sorted(idComparator);
    }

    // Store............................................................................................................

    @Override
    public void delete(final K id) {
        Objects.requireNonNull(id, "id");

        final Set<V> deleted = this.idToValues.remove(id);
        if (null != deleted) {
            for (final V deletedValue : deleted) {
                this.watchers.onValueRemoved(
                    id,
                    deletedValue
                );
            }
        }
    }

    @Override
    public int count() {
        return this.idToValues.size();
    }

    @Override
    public Set<K> ids(final int offset,
                      final int count) {
        Store.checkOffsetAndCount(
            offset,
            count
        );

        return this.idToValues.keySet()
            .stream()
            .skip(offset)
            .limit(count)
            .collect(
                ImmutableSet.collector()
            );
    }

    @Override
    public List<V> values(final int offset,
                          final int count) {
        Store.checkOffsetAndCount(
            offset,
            count
        );

        return this.idToValues.values()
            .stream()
            .flatMap(Set::stream)
            .skip(offset)
            .limit(count)
            .collect(ImmutableList.collector());
    }

    @Override
    public List<V> between(final K from,
                           final K to) {
        Store.checkBetween(from, to);

        final SortedMap<K, Set<V>> idToValues = this.idToValues;
        final SortedMap<K, Set<V>> subMap = idToValues.tailMap(from);
        final Comparator<? super K> idComparator = idToValues.comparator();

        final List<V> values = Lists.array();
        for (final Entry<K, Set<V>> keyAndValues : subMap.entrySet()) {
            if (idComparator.compare(keyAndValues.getKey(), to) > 0) {
                break;
            }
            values.addAll(
                keyAndValues.getValue()
            );
        }

        return values;
    }

    // MultiValueStore..................................................................................................

    @Override
    public void add(final K id,
                    final V value) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(value, "value");

        final SortedMap<K, Set<V>> idToValues = this.idToValues;
        Set<V> values = idToValues.get(id);
        if (null == values) {
            values = Sets.ordered(); // makes ordering of values more predictable., better for tests
            idToValues.put(
                id,
                values
            );
        }
        if (values.add(value)) {
            this.watchers.onValueAdded(
                id,
                value
            );
        }
        ;
    }

    @Override
    public void remove(final K id,
                       final V value) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(value, "value");

        final SortedMap<K, Set<V>> idToValues = this.idToValues;
        Set<V> values = idToValues.get(id);
        if (null != values) {
            if (values.remove(value)) {
                if (values.isEmpty()) {
                    idToValues.remove(id);
                }

                this.watchers.onValueRemoved(
                    id,
                    value
                );
            }
        }
    }

    @Override
    public List<V> findValuesById(final K id,
                                  final int offset,
                                  final int count) {
        Objects.requireNonNull(id, "id");
        Store.checkOffsetAndCount(
            offset,
            count
        );

        final Set<V> values = this.idToValues.get(id);
        return null == values ?
            Lists.empty() :
            values.stream()
                .skip(offset)
                .limit(count)
                .collect(
                    ImmutableList.collector()
                );
    }

    @Override
    public List<K> findIdsByValue(final V value,
                                  final int offset,
                                  final int count) {
        Objects.requireNonNull(value, "value");
        Store.checkOffsetAndCount(
            offset,
            count
        );

        return this.idToValues.entrySet()
            .stream()
            .filter(
                (Entry<K, Set<V>> idAndValue) -> idAndValue.getValue().contains(value)
            ).map(
                Entry::getKey
            ).skip(offset)
            .limit(count)
            .collect(
                ImmutableList.collector()
            );
    }

    // @VisibleForTesting
    final SortedMap<K, Set<V>> idToValues;

    @Override
    public Runnable addStoreWatcher(final MultiValueStoreWatcher<K, V> watcher) {
        return this.watchers.add(watcher);
    }

    private final MultiValueStoreWatchers<K, V> watchers = MultiValueStoreWatchers.empty();

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.idToValues.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            (other instanceof TreeMapMultiValueStore &&
                this.equals0((TreeMapMultiValueStore<?, ?>) other));
    }

    private boolean equals0(final TreeMapMultiValueStore<?, ?> other) {
        return this.idToValues.equals(other.idToValues);
    }

    @Override
    public String toString() {
        return this.idToValues.values().toString();
    }
}
