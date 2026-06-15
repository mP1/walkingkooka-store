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

import walkingkooka.HasNotFoundText;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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

    /**
     * Creates a {@link MissingStoreException} using the id if possible as the value parameter.
     */
    default MissingStoreException notFound(final Object id) {
        Objects.requireNonNull(id, "id");

        return id instanceof HasNotFoundText ?
            new MissingStoreException(
                (HasNotFoundText) id
            ) :
            new MissingStoreException("Unable to find id: " + id);
    }

    /**
     * Saves or updates a value.
     */
    V save(final V value);

    /**
     * Deletes a single value by id.
     */
    void delete(final K id);

    /**
     * Returns the total number of records in the store.
     */
    int count();

    /**
     * Returns a view of all ids between the positional range.
     */
    Set<K> ids(final int offset,
               final int count);

    /**
     * Returns the first id or an {@link Optional#empty()}.
     */
    default Optional<K> firstId() {
        final Set<K> first = this.ids(0, 1);
        return first.stream().findFirst();
    }

    /**
     * Returns a view of all values between the given range.
     */
    List<V> values(final int offset,
                   final int count);

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
        return this.values(
            0,
            Integer.MAX_VALUE
        );
    }

    /**
     * Useful parameter checking for both {@link #ids}
     */
    static void checkOffsetAndCount(final int offset,
                                    final int count) {
        if (offset < 0) {
            throw new IllegalArgumentException("Invalid offset " + offset + " < 0");
        }
        checkCount(count);
    }

    /**
     * Checks the count is greater than or equal to zero.
     */
    static int checkCount(final int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Invalid count " + count + " < 0");
        }
        return count;
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

    /**
     * Adds a {@link StoreWatcher}
     */
    Runnable addStoreWatcher(final StoreWatcher<V> watcher);

    /**
     * Adds a {@link StoreWatcher}
     */
    Runnable addStoreWatcherOnce(final StoreWatcher<V> watcher);

}
