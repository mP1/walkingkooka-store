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

final class TestUserId implements Comparable<TestUserId> {

    static TestUserId with(final int value) {
        return new TestUserId(value);
    }

    private TestUserId(final int value) {
        super();
        this.value = value;
    }

    final int value;

    @Override
    public int hashCode() {
        return this.value;
    }

    public boolean equals(final Object other) {
        return this == other || other instanceof TestUserId && this.equals0(Cast.to(other));
    }

    private boolean equals0(final TestUserId other) {
        return this.value == other.value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    @Override
    public int compareTo(final TestUserId other) {
        return this.value - other.value;
    }
}
