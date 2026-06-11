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
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class TreeMapMultiValueStoreTest implements MultiValueStoreTesting<TreeMapMultiValueStore<String, Integer>, String, Integer> {

    private final static String ID1 = "Id111";

    private final static String ID2 = "Id222";

    private final static String ID3 = "Id333";

    private final static Integer VALUE1 = 111;

    private final static Integer VALUE2 = 222;

    private final static Integer VALUE3 = 333;

    // add..............................................................................................................

    @Test
    public void testAddValue() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);

        this.idToValuesAndCheck(
            store,
            Maps.of(
                ID1,
                Sets.of(VALUE1)
            )
        );
    }

    @Test
    public void testAddValueWithMultiValueStoreWatcher() {
        this.fired = false;

        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addStoreWatcher(
            new FakeMultiValueStoreWatcher<>() {
                @Override
                public void onValueAdded(final String id,
                                         final Integer value) {
                    checkEquals(
                        ID1,
                        id
                    );
                    checkEquals(
                        VALUE1,
                        value
                    );
                    TreeMapMultiValueStoreTest.this.fired = true;
                }
            }
        );
        store.addValue(ID1, VALUE1);

        this.idToValuesAndCheck(
            store,
            Maps.of(
                ID1,
                Sets.of(VALUE1)
            )
        );

        this.checkEquals(
            true,
            this.fired
        );
    }

    @Test
    public void testAddValueSameTwice() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1.toLowerCase(), VALUE1);

        store.addStoreWatcher(new FakeMultiValueStoreWatcher<>());

        store.addValue(ID1.toUpperCase(), VALUE1);

        this.idToValuesAndCheck(
            store,
            Maps.of(
                ID1,
                Sets.of(VALUE1)
            )
        );
    }

    @Test
    public void testAddValueTwiceDifferentIds() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID2, VALUE2);

        this.idToValuesAndCheck(
            store,
            Maps.of(
                ID1,
                Sets.of(VALUE1),
                ID2,
                Sets.of(VALUE2)
            )
        );
    }

    @Test
    public void testAddValueTwiceDifferentIds2() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID1, VALUE2);

        this.idToValuesAndCheck(
            store,
            Maps.of(
                ID1,
                Sets.of(
                    VALUE1,
                    VALUE2
                )
            )
        );
    }

    // remove...........................................................................................................

    @Test
    public void testRemoveUnknown() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.remove(ID1, VALUE1);

        this.idToValuesAndCheck(
            store,
            Maps.empty()
        );
    }

    @Test
    public void testRemoveUnknown2() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.remove(ID2, VALUE1);

        this.idToValuesAndCheck(
            store,
            Maps.of(
                ID1,
                Sets.of(VALUE1)
            )
        );
    }

    @Test
    public void testRemove() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.remove(ID1, VALUE1);

        this.idToValuesAndCheck(
            store,
            Maps.empty()
        );
    }

    @Test
    public void testRemoveWithMultiValueStoreWatcher() {
        this.fired = false;

        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);

        store.addStoreWatcher(
            new FakeMultiValueStoreWatcher<>() {
                @Override
                public void onValueRemoved(final String id,
                                           final Integer value) {
                    checkEquals(
                        ID1,
                        id
                    );
                    checkEquals(
                        VALUE1,
                        value
                    );
                    TreeMapMultiValueStoreTest.this.fired = true;
                }
            }
        );

        store.remove(ID1, VALUE1);

        this.idToValuesAndCheck(
            store,
            Maps.empty()
        );

        this.checkEquals(
            true,
            this.fired
        );
    }

    @Test
    public void testRemove2() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID1, VALUE2);

        store.remove(ID1, VALUE1);

        this.idToValuesAndCheck(
            store,
            Maps.of(
                ID1,
                Sets.of(VALUE2)
            )
        );
    }

    @Test
    public void testRemoveTwiceDifferentIds() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID2, VALUE2);

        store.remove(ID1, VALUE1);
        store.remove(ID2, VALUE2);

        this.idToValuesAndCheck(
            store,
            Maps.empty()
        );
    }

    // delete...........................................................................................................

    @Test
    public void testDeleteUnknown() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.delete(ID1);

        this.idToValuesAndCheck(
            store,
            Maps.empty()
        );
    }

    @Test
    public void testDelete() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.delete(ID1);

        this.idToValuesAndCheck(
            store,
            Maps.empty()
        );
    }

    @Test
    public void testDeleteWithMultiValueStoreWatcher() {
        final Map<String, Set<Integer>> deleted = Maps.sorted();

        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);

        store.addStoreWatcher(
            new FakeMultiValueStoreWatcher<>() {
                @Override
                public void onValueRemoved(final String id,
                                           final Integer value) {
                    Set<Integer> values = deleted.get(id);
                    if (values == null) {
                        values = new HashSet<>();
                        deleted.put(id, values);
                    }
                    values.add(value);
                }
            }
        );

        store.delete(ID1);

        this.idToValuesAndCheck(
            store,
            Maps.empty()
        );

        this.checkEquals(
            Maps.of(
                ID1,
                Sets.of(VALUE1)
            ),
            deleted
        );
    }

    @Test
    public void testDeleteWithMultiValueStoreWatcher2() {
        final Map<String, Set<Integer>> deleted = Maps.sorted();

        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID1, VALUE2);
        store.addValue(ID2, VALUE2);

        store.addStoreWatcher(
            new FakeMultiValueStoreWatcher<>() {
                @Override
                public void onValueRemoved(final String id,
                                           final Integer value) {
                    Set<Integer> values = deleted.get(id);
                    if (values == null) {
                        values = new HashSet<>();
                        deleted.put(id, values);
                    }
                    values.add(value);
                }
            }
        );

        store.delete(ID1);

        this.idToValuesAndCheck(
            store,
            Maps.of(
                ID2,
                Sets.of(VALUE2)
            )
        );

        this.checkEquals(
            Maps.of(
                ID1,
                Sets.of(
                    VALUE1,
                    VALUE2
                )
            ),
            deleted
        );
    }

    // count............................................................................................................

    @Test
    public void testCount() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID2, VALUE2);

        this.countAndCheck(
            store,
            2
        );
    }

    @Test
    public void testCountMultiValues() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID1, VALUE2);
        store.addValue(ID2, VALUE2);

        this.countAndCheck(
            store,
            2
        );
    }

    // ids..............................................................................................................

    @Test
    public void testIds() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID2, VALUE2);

        this.idsAndCheck(
            store,
            0,
            3,
            ID1,
            ID2
        );
    }

    @Test
    public void testIdsWithOffset() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID2, VALUE2);

        this.idsAndCheck(
            store,
            1,
            3,
            ID2
        );
    }

    @Test
    public void testIdsWithSize() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID2, VALUE2);
        store.addValue(ID3, VALUE3);

        this.idsAndCheck(
            store,
            0,
            2,
            ID1,
            ID2
        );
    }

    @Test
    public void testIdsWithOffsetAndSize() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID2, VALUE2);
        store.addValue(ID3, VALUE3);

        this.idsAndCheck(
            store,
            1,
            1,
            ID2
        );
    }

    @Test
    public void testIdsWithMultiValueAndOffsetAndSize() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID1, VALUE2);
        store.addValue(ID2, VALUE2);
        store.addValue(ID3, VALUE3);

        this.idsAndCheck(
            store,
            1,
            1,
            ID2
        );
    }

    // values...........................................................................................................

    @Test
    public void testValues() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID2, VALUE2);

        this.valuesAndCheck(
            store,
            0,
            3,
            VALUE1,
            VALUE2
        );
    }

    @Test
    public void testValues2() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID1, VALUE2);

        this.valuesAndCheck(
            store,
            0,
            3,
            VALUE1,
            VALUE2
        );
    }

    @Test
    public void testValuesWithOffset() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID2, VALUE2);

        this.valuesAndCheck(
            store,
            1,
            3,
            VALUE2
        );
    }

    @Test
    public void testValuesWithSize() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID2, VALUE2);
        store.addValue(ID3, VALUE3);

        this.valuesAndCheck(
            store,
            0,
            2,
            VALUE1,
            VALUE2
        );
    }

    @Test
    public void testValuesWithOffsetAndSize() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID2, VALUE2);
        store.addValue(ID3, VALUE3);

        this.valuesAndCheck(
            store,
            1,
            1,
            VALUE2
        );
    }

    @Test
    public void testValuesWithMultiValueAndOffsetAndSize() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID1, VALUE2);
        store.addValue(ID2, VALUE2);
        store.addValue(ID3, VALUE3);

        this.valuesAndCheck(
            store,
            1,
            1,
            VALUE2
        );
    }

    // between..........................................................................................................

    @Test
    public void testBetween() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID2, VALUE2);

        this.betweenAndCheck(
            store,
            ID1,
            ID1,
            VALUE1
        );
    }

    @Test
    public void testBetween2() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID2, VALUE2);
        store.addValue(ID3, VALUE3);

        this.betweenAndCheck(
            store,
            ID1,
            ID2,
            VALUE1,
            VALUE2
        );
    }

    @Test
    public void testBetween3() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID2, VALUE2);
        store.addValue(ID3, VALUE3);

        this.betweenAndCheck(
            store,
            ID2,
            ID2,
            VALUE2
        );
    }

    // findValuesById...................................................................................................

    @Test
    public void testFindValuesById() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID2, VALUE2);
        store.addValue(ID3, VALUE3);

        this.findValuesByIdAndCheck(
            store,
            ID2,
            0,
            2,
            VALUE2
        );
    }

    @Test
    public void testFindValuesByIdWithOffset() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID2, VALUE1);
        store.addValue(ID2, VALUE2);
        store.addValue(ID2, VALUE3);
        store.addValue(ID3, VALUE3);

        this.findValuesByIdAndCheck(
            store,
            ID2,
            1,
            99,
            VALUE2,
            VALUE3
        );
    }

    @Test
    public void testFindValuesByIdWithSize() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID2, VALUE1);
        store.addValue(ID2, VALUE2);
        store.addValue(ID2, VALUE3);
        store.addValue(ID3, VALUE3);

        this.findValuesByIdAndCheck(
            store,
            ID2,
            0,
            1,
            VALUE1
        );
    }

    @Test
    public void testFindValuesByIdWithOffsetAndSize() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID2, VALUE1);
        store.addValue(ID2, VALUE2);
        store.addValue(ID2, VALUE3);
        store.addValue(ID3, VALUE3);

        this.findValuesByIdAndCheck(
            store,
            ID2,
            1,
            1,
            VALUE2
        );
    }

    // findIdsByValue...................................................................................................

    @Test
    public void testFindIdsByValue() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE1);
        store.addValue(ID2, VALUE2);
        store.addValue(ID3, VALUE3);

        this.findIdsByValueAndCheck(
            store,
            VALUE2,
            0,
            2,
            ID2
        );
    }

    @Test
    public void testFindIdsByValueWithOffset() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE2);
        store.addValue(ID2, VALUE1);
        store.addValue(ID2, VALUE2);
        store.addValue(ID2, VALUE3);
        store.addValue(ID3, VALUE2);

        this.findIdsByValueAndCheck(
            store,
            VALUE2,
            1,
            99,
            ID2,
            ID3
        );
    }

    @Test
    public void testFindIdsByValueWithSize() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE2);
        store.addValue(ID2, VALUE1);
        store.addValue(ID2, VALUE2);
        store.addValue(ID2, VALUE3);
        store.addValue(ID3, VALUE2);

        this.findIdsByValueAndCheck(
            store,
            VALUE2,
            0,
            1,
            ID1
        );
    }

    @Test
    public void testFindIdsByValueWithOffsetAndSize() {
        final TreeMapMultiValueStore<String, Integer> store = this.createStore();
        store.addValue(ID1, VALUE2);
        store.addValue(ID2, VALUE1);
        store.addValue(ID2, VALUE2);
        store.addValue(ID2, VALUE3);
        store.addValue(ID3, VALUE2);

        this.findIdsByValueAndCheck(
            store,
            VALUE2,
            1,
            1,
            ID2
        );
    }

    private boolean fired;

    private void idToValuesAndCheck(final TreeMapMultiValueStore<String, Integer> store,
                                    final Map<String, Set<Integer>> expected) {
        this.checkEquals(
            expected,
            store.idToValues,
            store::toString
        );
    }

    @Override
    public TreeMapMultiValueStore<String, Integer> createStore() {
        return TreeMapMultiValueStore.with(
            String.CASE_INSENSITIVE_ORDER
        );
    }

    @Override
    public String id() {
        return ID1;
    }

    @Override
    public Integer value() {
        return VALUE1;
    }

    @Override
    public Class<TreeMapMultiValueStore<String, Integer>> type() {
        return Cast.to(TreeMapMultiValueStore.class);
    }
}
