package koye.lib.common.utils;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @org.junit.jupiter.api.Test
    void toLowerFirstSymbol() {

        String s = "StringUtilsTest";
        String res = StringUtils.toLowerFirstSymbol(s);
        assertEquals(res, "stringUtilsTest");
    }
}