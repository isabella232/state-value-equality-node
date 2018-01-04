package com.luna.authentication;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class MatcherTest {

    private Matcher matcher = new Matcher();

    private final static Set ONE_TWO_THREE = new HashSet<>(asList("one", "two", "three"));
    private final static List THREE_TWO_ONE = new LinkedList<>(asList("three", "two", "one"));

    @DataProvider (name = "matchAllData")
    Object[][] testMatchAllData() {
        return new Object[][] {
                { singleton("data"), "data", true },
                { singleton("data"), "invalid", false },
                { singleton("data"), singleton("data"), true },
                { "data", "data", true },
                { "data", "invalid", false },
                { null, singleton("data"), false },
                { null, null, true },
                { ONE_TWO_THREE, THREE_TWO_ONE, true},
                { "one", ONE_TWO_THREE, false },
                { "two", ONE_TWO_THREE, false }
        };
    }

    @Test (dataProvider = "matchAllData")
    public void testMatchAll(Object one, Object two, boolean result) {
        assertThat(matcher.matchAll(one, two)).isEqualTo(result);
    }

    @DataProvider (name = "matchFirstData")
    Object[][] testMatchFirstData() {
        return new Object[][] {
                { singleton("data"), "data", true },
                { singleton("data"), "invalid", false },
                { singleton("data"), singleton("data"), true },
                { "data", "data", true },
                { "data", "invalid", false },
                { null, singleton("data"), false },
                { null, null, true },
                { ONE_TWO_THREE, THREE_TWO_ONE, false},
                { "one", ONE_TWO_THREE, true },
                { "two", ONE_TWO_THREE, false }
        };
    }

    @Test (dataProvider = "matchFirstData")
    public void testMatchFirst(Object one, Object two, boolean result) {
        assertThat(matcher.matchFirst(one, two)).isEqualTo(result);
    }

    @DataProvider (name = "matchAnyData")
    Object[][] testMatchAnyData() {
        return new Object[][] {
                { singleton("data"), "data", true },
                { singleton("data"), "invalid", false },
                { singleton("data"), singleton("data"), true },
                { "data", "data", true },
                { "data", "invalid", false },
                { null, singleton("data"), false },
                { null, null, true },
                { ONE_TWO_THREE, THREE_TWO_ONE, true},
                { "one", ONE_TWO_THREE, true },
                { "two", ONE_TWO_THREE, true }
        };
    }

    @Test (dataProvider = "matchAnyData")
    public void testMatchAny(Object one, Object two, boolean result) {
        assertThat(matcher.matchAny(one, two)).isEqualTo(result);
    }

    @DataProvider (name = "matchExactData")
    Object[][] testMatchExactData() {
        return new Object[][] {
                { singleton("data"), "data", false },
                { singleton("data"), "invalid", false },
                { singleton("data"), singleton("data"), true },
                { "data", "data", true },
                { "data", "invalid", false },
                { null, singleton("data"), false },
                { null, null, true },
                { ONE_TWO_THREE, THREE_TWO_ONE, false },
                { "one", ONE_TWO_THREE, false },
                { "two", ONE_TWO_THREE, false }
        };
    }

    @Test (dataProvider = "matchExactData")
    public void testMatchExact(Object one, Object two, boolean result) {
        assertThat(matcher.matchExact(one, two)).isEqualTo(result);
    }

}
