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
import walkingkooka.watch.Watchers;

import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * A {@link Store} that shares a {@link TreeMap} and automatically allocates an ID if saving a value without an ID.
 * This store is intended to be decorated sharing the map.
 */
final class TreeMapStore<K extends Comparable<K>, V extends HasId<Optional<K>>> implements Store<K, V> {

    /**
     * Factory that creates a new {@link TreeMapStore}.
     */
    static <K extends Comparable<K>, V extends HasId<Optional<K>>> TreeMapStore<K, V> with(final Comparator<K> idComparator,
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

    private V update(final K id, final V value) {
        if (false == value.equals(this.idToValue.put(id, value))) {
            this.saveWatchers.accept(value);
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
        idToValue.put(valueWithId.id().get(), valueWithId);
        this.saveWatchers.accept(valueWithId);
        return valueWithId;
    }

    /**
     * Accepts the current highest ID or null (when the store is empty) and value combining the two into a new value.
     */
    private final BiFunction<K, V, V> idSetter;

    @Override
    public Runnable addSaveWatcher(final Consumer<V> saved) {
        return this.saveWatchers.add(saved);
    }

    private final Watchers<V> saveWatchers = Watchers.create();

    @Override
    public void delete(final K id) {
        Objects.requireNonNull(id, "id");

        if (null != this.idToValue.remove(id)) {
            this.deleteWatchers.accept(id);
        }
    }

    @Override
    public Runnable addDeleteWatcher(final Consumer<K> deleted) {
        return this.deleteWatchers.add(deleted);
    }

    private final Watchers<K> deleteWatchers = Watchers.create();

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

        final SortedMap<K, V> subMap = this.idToValue.tailMap(from);

        final List<V> values = Lists.array();
        for (final Entry<K, V> keyAndValue : subMap.entrySet()) {
            if (keyAndValue.getKey().compareTo(to) > 0) {
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
    public String toString() {
        return this.idToValue.values().toString();
    }
}
