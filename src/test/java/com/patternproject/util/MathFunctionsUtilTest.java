package com.patternproject.util;

import lombok.val;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Koliadin Nikita
 */
public class MathFunctionsUtilTest {

    @Test
    public void shouldBeCorrectListOfMathFunctions() {
        val expected = new ArrayList<>(Arrays.asList(
                "abs", "acos", "asin", "atan", "cbrt", "ceil", "cos", "cosh", "exp", "expm1",
                "floor", "getExponent", "log", "log10", "log1p", "nextDown", "nextUp", "rint",
                "round", "signum", "sin", "sinh", "sqrt", "tan", "tanh", "toDegrees", "toRadians", "ulp")
        );

        assertThat(MathFunctionsUtil.MATH_FUNCTIONS).isNotNull().hasSize(28).isEqualTo(expected);
    }
}
