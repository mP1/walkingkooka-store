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

import walkingkooka.collect.list.Lists;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/**
 * A store that holds a value with an id (K).
 */
public interface Store<K, V> {

    /**
     * Fetches the value using the reference.
     */
    Optional<V> load(final K id);

    /**
     * Fetches the value with the id or throws a {@link StoreException}.
     */
    default V loadOrFail(final K id) {
        final Optional<V> value = this.load(id);
        if (false == value.isPresent()) {
            throw this.notFound(id);
        }
        return value.get();
    }

    default LoadStoreException notFound(final Object id) {
        return new LoadStoreException("Unable to find id: " + id);
    }

    /**
     * Saves or updates a value.
     */
    V save(final V value);

    /**
     * Adds a watcher that will receive values after a save.<br>
     * Note the watcher will only be fired when new or different values are saved. Saving the same value twice in succession
     * should only fire the first time, ignoring the second because it is identical.
     */
    Runnable addSaveWatcher(final Consumer<V> saved);

    /**
     * Deletes a single value by id.
     */
    void delete(final K id);

    /**
     * Adds a watcher that will receive values after a deleted.
     */
    Runnable addDeleteWatcher(final Consumer<K> deleted);

    /**
     * Returns the total number of records in the store.
     */
    int count();

    /**
     * Returns a view of all ids between the positional range.
     */
    Set<K> ids(final int from, final int count);

    /**
     * Returns the first id or an {@link Optional#empty()}.
     */
    default Optional<K> firstId() {
        final Set<K> first = this.ids(0, 1);
        return first.stream().findFirst();
    }

    /**
     * Returns a view of all values between the range of ids.
     */
    List<V> values(final K from, final int count);

    /**
     * Fetches the first value if one is present.
     */
    default Optional<V> firstValue() {
        return this.firstId()
                .flatMap(this::load);
    }

    /**
     * Returns all values in this store.
     */
    default List<V> all() {
        return this.firstId()
                .map((i) -> this.values(i, Integer.MAX_VALUE))
                .orElse(Lists.empty());
    }

    /**
     * Useful parameter checking for both {@link #ids}
     */
    static void checkFromAndTo(final int from, final int count) {
        if (from < 0) {
            throw new IllegalArgumentException("From " + from + " < 0");
        }
        checkCount(count);
    }

    /**
     * Useful parameter checking for both {@link #ids}
     */
    static <K> void checkFromAndToIds(final K from, final int count) {
        Objects.requireNonNull(from, "from");
        checkCount(count);
    }

    /**
     * Checks the count is greater than or equal to zero.
     */
    static void checkCount(final int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Invalid count " + count + " < 0");
        }
    }

    /**
     * Returns all the values between the from and to which are both inclusive.
     */
    List<V> between(final K from, final K to);

    /**
     * Useful parameter checking
     */
    static <K> void checkBetween(final K from, final K to) {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
    }
}
