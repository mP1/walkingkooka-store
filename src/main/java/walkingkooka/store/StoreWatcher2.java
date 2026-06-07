package walkingkooka.store;

import walkingkooka.watch.ValueChangeWatcher2;

/**
 * A {@link StoreWatcher2} that routes each event to add/remove/update.
 */
public interface StoreWatcher2<V> extends StoreWatcher<V>,
    ValueChangeWatcher2<V> {

}
