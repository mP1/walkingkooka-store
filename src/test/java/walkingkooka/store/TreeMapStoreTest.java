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
import walkingkooka.Cast;
import walkingkooka.reflect.TypeNameTesting;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TreeMapStoreTest implements StoreTesting<TreeMapStore<TestUserId, TestUser>, TestUserId, TestUser>,
        TypeNameTesting<TreeMapStore<TestUserId, TestUser>> {

    @Test
    public void testWithNullIdComparatorFails() {
        this.withFails(null, this::idSetter);
    }

    @Test
    public void testWithNullIdSetterFails() {
        this.withFails(Comparator.naturalOrder(), null);
    }

    private void withFails(final Comparator<TestUserId> idComparator,
                           final BiFunction<TestUserId, TestUser, TestUser> idSetter) {
        assertThrows(NullPointerException.class, () -> {
            TreeMapStore.with(idComparator, idSetter);
        });
    }

    @Test
    public void testLoad1() {
        this.loadAndCheck(this.createNotEmptyStore(), this.user2().id().get(), this.user2());
    }

    @Test
    public void testLoad2() {
        this.loadAndCheck(this.createNotEmptyStore(), this.user3().id().get(), this.user3());
    }

    @Test
    public void testSaveWithId() {
        final TreeMapStore<TestUserId, TestUser> store = this.createNotEmptyStore();

        final TestUser saved = TestUser.with(Optional.of(TestUserId.with(2)), "saved@example.com");
        store.save(saved);

        this.loadAndCheck(store, saved.id().get(), saved);
    }

    @Test
    public void testSaveReplaces() {
        final TreeMapStore<TestUserId, TestUser> store = this.createNotEmptyStore();

        final TestUser replace = TestUser.with(this.user3().id(), "replaced@example.com");
        store.save(replace);

        this.loadAndCheck(store, replace.id().get(), replace);
    }

    @Test
    public void testSaveWithoutIdStoreEmpty() {
        final TreeMapStore<TestUserId, TestUser> store = this.createStore();
        this.countAndCheck(store, 0);

        final String email = "saved@example.com";

        final TestUser saved = store.save(TestUser.with(Optional.empty(), email));
        this.checkEquals(TestUser.with(Optional.of(TestUserId.with(1)), email), saved, "id");

        this.loadAndCheck(store, saved.id().get(), saved);
        this.countAndCheck(store, 1);
    }

    @Test
    public void testSaveWithoutId() {
        final TreeMapStore<TestUserId, TestUser> store = this.createNotEmptyStore();
        this.countAndCheck(store, 3);

        final String email = "saved@example.com";

        final TestUser saved = store.save(TestUser.with(Optional.empty(), email));
        this.checkEquals(TestUser.with(Optional.of(TestUserId.with(334)), email), saved, "id");

        this.loadAndCheck(store, saved.id().get(), saved);
        this.countAndCheck(store, 4);
    }

    @Test
    public void testSaveWithoutId2() {
        final TreeMapStore<TestUserId, TestUser> store = this.createNotEmptyStore();
        this.countAndCheck(store, 3);

        final String email = "saved1@example.com";

        final TestUser saved1 = store.save(TestUser.with(Optional.empty(), email));
        this.checkEquals(TestUser.with(Optional.of(TestUserId.with(334)), email), saved1, "id");

        final String email2 = "saved2@example.com";

        final TestUser saved2 = store.save(TestUser.with(Optional.empty(), email2));
        this.checkEquals(TestUser.with(Optional.of(TestUserId.with(335)), email2), saved2, "id");

        this.loadAndCheck(store, saved2.id().get(), saved2);

        this.countAndCheck(store, 5);
    }

    @Test
    public void testDelete() {
        final TreeMapStore<TestUserId, TestUser> store = this.createNotEmptyStore();

        final TestUser user1 = this.user1();
        store.delete(user1.id().get());

        this.loadFailCheck(store, user1.id().get());
    }

    @Test
    public void testCount() {
        this.countAndCheck(this.createNotEmptyStore(), 3);
    }

    @Test
    public void testCountAfterSave() {
        final TreeMapStore<TestUserId, TestUser> store = this.createNotEmptyStore();

        store.save(TestUser.with(Optional.of(TestUserId.with(999)), "saved@example.com"));

        this.countAndCheck(store, 3 + 1);
    }

    @Test
    public final void testIds() {
        final TreeMapStore<TestUserId, TestUser> store = this.createStore();

        final TestUser a = this.user1();
        final TestUser b = this.user2();
        final TestUser c = this.user3();

        store.save(a);
        store.save(b);
        store.save(c);

        this.idsAndCheck2(store, 0, 3, a.id(), b.id(), c.id());
    }

    @Test
    public final void testIdsWindow() {
        final TreeMapStore<TestUserId, TestUser> store = this.createStore();

        final TestUser a = this.user1();
        final TestUser b = this.user2();
        final TestUser c = this.user3();
        final TestUser d = this.user4();

        store.save(a);
        store.save(b);
        store.save(c);
        store.save(d);

        this.idsAndCheck2(store, 1, 2, b.id(), c.id());
    }

    @Test
    public final void testValues() {
        final TreeMapStore<TestUserId, TestUser> store = this.createStore();

        final TestUser a = this.user1();
        final TestUser b = this.user2();
        final TestUser c = this.user3();

        store.save(a);
        store.save(b);
        store.save(c);

        this.valuesAndCheck(store, a.id().get(), 3, a, b, c);
    }

    @Test
    public final void testValuesWindow() {
        final TreeMapStore<TestUserId, TestUser> store = this.createStore();

        final TestUser a = this.user1();
        final TestUser b = this.user2();
        final TestUser c = this.user3();
        final TestUser d = this.user4();

        store.save(a);
        store.save(b);
        store.save(c);
        store.save(d);

        this.valuesAndCheck(store, b.id().get(), 2, b, c);
    }

    @Test
    public void testToString() {
        final TreeMapStore<TestUserId, TestUser> store = createNotEmptyStore();
        this.toStringAndCheck(store, store.idToValue.values().toString());
    }

    // helpers..........................................................................................................

    private TestUser user1() {
        return this.user(1, "user1@example.com");
    }

    private TestUser user2() {
        return this.user(2, "user2@example.com");
    }

    private TestUser user3() {
        return this.user(333, "user3@example.com");
    }

    private TestUser user4() {
        return this.user(444, "user4@example.com");
    }

    private TestUser user(final int value, final String email) {
        return TestUser.with(Optional.of(TestUserId.with(value)), email);
    }

    // StoreTesting..........................................................................................

    @Override
    public TreeMapStore<TestUserId, TestUser> createStore() {
        return TreeMapStore.with(Comparator.naturalOrder(), this::idSetter);
    }

    final TestUser idSetter(final TestUserId id, final TestUser user) {
        return TestUser.with(Optional.of(TestUserId.with(null == id ? 1 : id.value + 1)), user.email);
    }

    private TreeMapStore<TestUserId, TestUser> createNotEmptyStore() {
        final TreeMapStore<TestUserId, TestUser> store = this.createStore();

        Arrays.asList(user1(), user2(), user3())
                .stream()
                .forEach(u -> {
                    store.idToValue.put(u.id().get(), u);
                });

        return store;
    }

    @Override
    public TestUserId id() {
        return this.value().id().get();
    }

    @Override
    public TestUser value() {
        return this.user1();
    }

    private void idsAndCheck2(final TreeMapStore<TestUserId, TestUser> store,
                              final int from,
                              final int to,
                              final Optional<TestUserId>... ids) {
        this.idsAndCheck(store,
                from,
                to,
                Arrays.asList(ids).stream().map(i -> i.get()).collect(Collectors.toSet()));
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<TreeMapStore<TestUserId, TestUser>> type() {
        return Cast.to(TreeMapStore.class);
    }

    // TypeNameTesting..................................................................................................

    @Override
    public String typeNamePrefix() {
        return TreeMap.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return Store.class.getSimpleName();
    }
}
