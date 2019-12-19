package koye.lib.expression;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum OPTION {

    IGNORE_CASE,
    NEGATIVE;

    public static OPTION[] union(OPTION[]... optionArr) {
        Set<OPTION> optionSet = new HashSet<>();
        for (OPTION[] options : optionArr) {
            optionSet.addAll(Arrays.asList(options));
        }
        return optionSet.toArray(new OPTION[0]);
    }

    public static OPTION[] exclude(OPTION[] availableOptions, OPTION[] excludeOptions) {
        Set<OPTION> optionSet = new HashSet<>(Arrays.asList(availableOptions));
        optionSet.removeAll(Arrays.asList(excludeOptions));
        return optionSet.toArray(new OPTION[0]);
    }

}
