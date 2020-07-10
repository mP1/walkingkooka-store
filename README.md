[![Build Status](https://travis-ci.com/mP1/walkingkooka-store.svg?branch=master)](https://travis-ci.com/mP1/walkingkooka-store.svg?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/mP1/walkingkooka-store/badge.svg?branch=master)](https://coveralls.io/repos/github/mP1/walkingkooka-store?branch=master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/mP1/walkingkooka-store.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-store/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/mP1/walkingkooka-store.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-store/alerts/)



A store may be used to persist entities. A `TreeMap` memory store is provided for testing purposes.

```java
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
```