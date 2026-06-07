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


import walkingkooka.watch.ValueChangeWatchers;

import java.util.Optional;

/**
 * A collection of {@link StoreWatcher}. Note the event is only fired to watchers if the old and new values
 * are different.
 */
public final class StoreWatchers<V> implements StoreWatcher<V> {

    public static <V> StoreWatchers<V> empty() {
        return new StoreWatchers<>();
    }

    public Runnable add(final StoreWatcher<V> watcher) {
        return this.watchers.add(watcher);
    }

    public Runnable addOnce(final StoreWatcher<V> watcher) {
        return this.watchers.addOnce(watcher);
    }

    /**
     * Note the event is only fired if the old and new values are different.
     */
    @Override
    public void onValueChange(final Optional<V> oldValue,
                              final Optional<V> newValue) {
        this.watchers.onValueChange(oldValue, newValue);
    }

    private final ValueChangeWatchers<V> watchers = ValueChangeWatchers.empty();

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.watchers.toString();
    }
}
