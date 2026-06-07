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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;

import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * A {@link Store} that shares a {@link TreeMap} and automatically allocates an ID if saving a value without an ID.
 * This store is intended to be decorated sharing the map.
 */
final class TreeMapStore<K, V extends HasId<Optional<K>>> implements Store<K, V> {

    /**
     * Factory that creates a new {@link TreeMapStore}.
     */
    static <K, V extends HasId<Optional<K>>> TreeMapStore<K, V> with(final Comparator<K> idComparator,
                                                                     final BiFunction<K, V, V> idSetter) {
        Objects.requireNonNull(idComparator, "idComparator");
        Objects.requireNonNull(idSetter, "idSetter");

        return new TreeMapStore<>(idComparator, idSetter);
    }

    /**
     * Private ctor
     */
    private TreeMapStore(final Comparator<K> idComparator, final BiFunction<K, V, V> idSetter) {
        super();
        this.idToValue = Maps.sorted(idComparator);
        this.idSetter = idSetter;
    }

    @Override
    public Optional<V> load(final K id) {
        Objects.requireNonNull(id, "id");

        return Optional.ofNullable(this.idToValue.get(id));
    }

    @Override
    public V save(final V value) {
        Objects.requireNonNull(value, "value");

        final K id = value.id().orElse(null);
        return null != id ?
            this.update(id, value) :
            this.saveNew(value);
    }

    private V update(final K id,
                     final V value) {
        final V previous = this.idToValue.put(id, value);
        if (false == value.equals(previous)) {
            this.watchers.onValueChange(
                Optional.ofNullable(previous),
                Optional.of(value)
            );
        }
        return value;
    }

    // no attempt to avoid clashes etc.
    private V saveNew(final V value) {
        final SortedMap<K, V> idToValue = this.idToValue;
        final K max = idToValue.isEmpty() ?
            null :
            idToValue.lastKey();

        final V valueWithId = this.idSetter.apply(max, value);
        idToValue.put(
            valueWithId.id()
                .get(),
            valueWithId
        );
        this.watchers.onValueChange(
            Optional.of(valueWithId),
            Optional.empty()
        );
        return valueWithId;
    }

    /**
     * Accepts the current highest ID or null (when the store is empty) and value combining the two into a new value.
     */
    private final BiFunction<K, V, V> idSetter;

    @Override
    public void delete(final K id) {
        Objects.requireNonNull(id, "id");

        final V deleted = this.idToValue.remove(id);
        if (null != deleted) {
            this.watchers.onValueChange(
                Optional.of(deleted),
                Optional.empty()
            );
        }
    }

    @Override
    public int count() {
        return this.idToValue.size();
    }

    @Override
    public Set<K> ids(final int offset,
                      final int count) {
        Store.checkOffsetAndCount(offset, count);

        return this.idToValue.keySet()
            .stream()
            .skip(offset)
            .limit(count)
            .collect(Collectors.toCollection(Sets::ordered));
    }

    @Override
    public List<V> values(final int offset,
                          final int count) {
        Store.checkOffsetAndCount(offset, count);

        return this.idToValue.entrySet()
            .stream()
            .skip(offset)
            .limit(count)
            .map(Entry::getValue)
            .collect(Collectors.toCollection(Lists::array));
    }

    @Override
    public List<V> between(final K from, final K to) {
        Store.checkBetween(from, to);

        final SortedMap<K, V> idToValue = this.idToValue;
        final SortedMap<K, V> subMap = idToValue.tailMap(from);
        final Comparator<? super K> idComparator = idToValue.comparator();

        final List<V> values = Lists.array();
        for (final Entry<K, V> keyAndValue : subMap.entrySet()) {
            if (idComparator.compare(keyAndValue.getKey(), to) > 0) {
                break;
            }
            values.add(
                keyAndValue.getValue()
            );
        }

        return values;
    }

    /**
     * A {@link TreeMap} sorted by ID from lowest to highest.
     */
    // VisibleForTesting
    final SortedMap<K, V> idToValue;

    @Override
    public Runnable addStoreWatcher(final StoreWatcher<V> watcher) {
        return this.watchers.add(watcher);
    }

    private final StoreWatchers<V> watchers = StoreWatchers.empty();

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.idToValue.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            (other instanceof TreeMapStore &&
                this.equals0((TreeMapStore<?, ?>) other));
    }

    private boolean equals0(final TreeMapStore<?, ?> other) {
        return this.idToValue.equals(other.idToValue);
    }

    @Override
    public String toString() {
        return this.idToValue.values().toString();
    }
}
