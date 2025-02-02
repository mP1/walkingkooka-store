package test;

import com.google.gwt.junit.client.GWTTestCase;
import walkingkooka.store.Stores;

@walkingkooka.j2cl.locale.LocaleAware
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
