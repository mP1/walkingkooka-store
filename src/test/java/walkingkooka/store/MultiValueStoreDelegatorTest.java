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

import walkingkooka.HasId;
import walkingkooka.store.MultiValueStoreDelegatorTest.TestMultiValueStoreDelegator;
import walkingkooka.store.MultiValueStoreDelegatorTest.TestValue;

import java.util.Optional;

public final class MultiValueStoreDelegatorTest implements MultiValueStoreTesting<TestMultiValueStoreDelegator, String, TestValue> {

    @Override
    public void testAddStoreWatcherAndDelete() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testAddStoreWatcherAndSave() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testAddStoreWatcherAndSaveTwiceFiresOnce() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTestNaming() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TestMultiValueStoreDelegator createStore() {
        return new TestMultiValueStoreDelegator();
    }

    @Override
    public String id() {
        return "Id111";
    }

    @Override
    public TestValue value() {
        return new TestValue();
    }

    @Override
    public Class<TestMultiValueStoreDelegator> type() {
        return TestMultiValueStoreDelegator.class;
    }

    final static class TestMultiValueStoreDelegator implements MultiValueStoreDelegator<String, TestValue> {

        private TestMultiValueStoreDelegator() {
            super();
        }

        @Override
        public MultiValueStore<String, TestValue> multiValueStore() {
            return MultiValueStores.treeMap(String.CASE_INSENSITIVE_ORDER);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    final static class TestValue implements HasId<Optional<String>> {

        @Override
        public Optional<String> id() {
            return Optional.of("Id111");
        }
    }
}
