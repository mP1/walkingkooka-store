/*
 * Copyright 2029 Miroslav Pokorny (github.com/mP2)
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

package walkingkooka.store.sample;

import walkingkooka.Cast;
import walkingkooka.HasId;
import walkingkooka.ToStringBuilder;
import walkingkooka.store.Store;
import walkingkooka.store.Stores;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class Sample {

    public static void main(final String[] args) {
        final Store<String, TestUser> store = Stores.treeMap(String.CASE_INSENSITIVE_ORDER, Sample::idSetter);

        final String id1 = "user1";
        final String email1 = "email1@example.com";
        store.save(new TestUser(Optional.of(id1), email1));

        final String id2 = "user2";
        final String email2 = "email2@example.com";
        store.save(new TestUser(Optional.of(id2), email2));

        assertEquals(2, store.count());
        assertEquals(email1, store.loadOrFail(id1).email);
    }

    private static TestUser idSetter(final String id, final TestUser user) {
        return new TestUser(Optional.of(id), user.email);
    }

    static class TestUser implements HasId<Optional<String>> {

        private TestUser(final Optional<String> id, final String email) {
            super();
            this.id = id;
            this.email = email;
        }

        @Override
        public Optional<String> id() {
            return this.id;
        }

        final Optional<String> id;
        final String email;

        @Override
        public int hashCode() {
            return this.id.hashCode();
        }

        public boolean equals(final Object other) {
            return this == other || other instanceof TestUser && this.equals0(Cast.to(other));
        }

        private boolean equals0(final TestUser other) {
            return this.id.equals(other.id) && this.email.equals(other.email);
        }

        @Override
        public String toString() {
            return ToStringBuilder.empty()
                    .value(this.id)
                    .value(this.email)
                    .build();
        }
    }
}
