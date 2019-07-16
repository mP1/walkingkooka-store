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

import walkingkooka.Cast;
import walkingkooka.HasId;
import walkingkooka.ToStringBuilder;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.test.HashCodeEqualsDefined;

import java.util.Optional;

final class TestUser implements HasId<Optional<TestUserId>>, HashCodeEqualsDefined {

    public static TestUser with(final Optional<TestUserId> id, final EmailAddress email) {
        return new TestUser(id, email);
    }

    private TestUser(final Optional<TestUserId> id, final EmailAddress email) {
        super();
        this.id = id;
        this.email = email;
    }

    @Override
    public Optional<TestUserId> id() {
        return this.id;
    }

    final Optional<TestUserId> id;
    final EmailAddress email;

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
