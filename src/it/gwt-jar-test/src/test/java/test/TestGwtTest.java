package test;

import com.google.gwt.junit.client.GWTTestCase;
import walkingkooka.store.Stores;

public class TestGwtTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "test.Test";
    }

    public void testAssertEquals() {
        assertEquals(
            1,
            1
        );
    }

    public void testStoresFake() {
        Stores.fake();
    }
}
