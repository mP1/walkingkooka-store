package walkingkooka.store;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link StoreWatcher} that routes each event to add/remove/update.
 */
public interface StoreWatcher2<V> extends StoreWatcher<V> {

    @Override
    default void onStoreValueChange(final Optional<V> oldValue,
                                    final Optional<V> newValue) {
        Objects.requireNonNull(oldValue, "oldValue");
        Objects.requireNonNull(newValue, "newValue");

        final boolean oldEmpty = oldValue.isEmpty();
        final boolean newEmpty = newValue.isEmpty();

        if (oldEmpty) {
            this.onStoreValueAdd(
                newValue.get()
            );
        } else {
            if (newEmpty) {
                this.onStoreValueRemove(
                    oldValue.get()
                );
            } else {
                this.onStoreValueUpdate(
                    oldValue.get(),
                    newValue.get()
                );
            }
        }
    }

    void onStoreValueAdd(final V newValue);

    void onStoreValueRemove(final V oldValue);

    void onStoreValueUpdate(final V oldValue,
                            final V newValue);
}
