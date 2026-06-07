package walkingkooka.store;

import walkingkooka.watch.ValueChangeWatcher2;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link StoreWatcher2} that routes each event to add/remove/update.
 */
public interface StoreWatcher2<V> extends StoreWatcher<V>,
    ValueChangeWatcher2<V> {

}
