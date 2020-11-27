package koye.lib.common.utils;

import com.google.common.base.CaseFormat;

public interface StringUtils {

    static String toLowerFirstSymbol(String s) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, s);
    }
}
