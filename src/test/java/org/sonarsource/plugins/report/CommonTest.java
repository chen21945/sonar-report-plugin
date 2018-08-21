package org.sonarsource.plugins.report;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CommonTest {

    @Test
    public void arrayTest() {
        String[] strs = {"Bug", "Vul"};
        System.out.println(Arrays.stream(strs).collect(Collectors.joining(",")));
        System.out.println(StringUtils.join(strs,","));
    }

}
