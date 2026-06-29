package lab02;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class CalculatorTest {
    private Calculator calc;

    @BeforeEach
    public void setUp() {
        calc = new Calculator();
    }

    // ==========================================
    // BLACKBOX TESTS
    // ==========================================

    @Test
    public void testMathematicalCorrectness() {
        // Test basic addition
        assertEquals(5.0, calc.add(2.0, 3.0), 0.0001);
        assertEquals(-1.0, calc.add(2.0, -3.0), 0.0001);

        // Test basic subtraction
        assertEquals(-1.0, calc.subtract(2.0, 3.0), 0.0001);

        // Test basic multiplication
        assertEquals(6.0, calc.multiply(2.0, 3.0), 0.0001);
        assertEquals(0.0, calc.multiply(2.0, 0.0), 0.0001);

        // Test basic division
        assertEquals(2.0, calc.divide(6.0, 3.0), 0.0001);
        assertEquals(-2.5, calc.divide(5.0, -2.0), 0.0001);

        // Test basic square root
        assertEquals(3.0, calc.squareRoot(9.0), 0.0001);
        assertEquals(0.0, calc.squareRoot(0.0), 0.0001);
    }

    @Test
    public void testDivisionByZero() {
        // Error handling test
        assertThrows(ArithmeticException.class, () -> {
            calc.divide(5.0, 0.0);
        });
    }

    @Test
    public void testSquareRootNegative() {
        // Input validation test
        assertThrows(IllegalArgumentException.class, () -> {
            calc.squareRoot(-1.0);
        });
    }

    @Test
    public void testOverflowHandling() {
        // Edge cases
        assertThrows(ArithmeticException.class, () -> {
            calc.add(Double.MAX_VALUE, Double.MAX_VALUE);
        });

        assertThrows(ArithmeticException.class, () -> {
            calc.subtract(-Double.MAX_VALUE, Double.MAX_VALUE);
        });

        assertThrows(ArithmeticException.class, () -> {
            calc.multiply(Double.MAX_VALUE, 2.0);
        });
    }

    // ==========================================
    // WHITEBOX TESTS
    // ==========================================

    @Test
    public void testCalculateBranchCoverage() {
        // Operator switch branch coverage
        assertEquals(10.0, calc.calculate(7.0, 3.0, "+"), 0.0001);
        assertEquals(4.0, calc.calculate(7.0, 3.0, "-"), 0.0001);
        assertEquals(21.0, calc.calculate(7.0, 3.0, "*"), 0.0001);
        assertEquals(2.0, calc.calculate(6.0, 3.0, "/"), 0.0001);

        // Whitespace trimming coverage
        assertEquals(10.0, calc.calculate(7.0, 3.0, " + "), 0.0001);
    }

    @Test
    public void testCalculateExceptionPaths() {
        // Null operator path
        assertThrows(IllegalArgumentException.class, () -> {
            calc.calculate(2.0, 3.0, null);
        });

        // Empty operator path
        assertThrows(IllegalArgumentException.class, () -> {
            calc.calculate(2.0, 3.0, "   ");
        });

        // Unsupported operator path
        assertThrows(UnsupportedOperationException.class, () -> {
            calc.calculate(2.0, 3.0, "%");
        });
    }

    @Test
    public void testDebugModeTesting() {
        // Test with debug mode = false (default)
        calc.add(2.0, 3.0);
        assertTrue(calc.getHistory().isEmpty());

        // Test with debug mode = true
        calc.setDebugMode(true);
        assertTrue(calc.isDebugMode());
        
        calc.add(5.0, 5.0);
        calc.subtract(10.0, 4.0);
        calc.multiply(2.0, 3.0);
        calc.divide(8.0, 2.0);
        calc.squareRoot(16.0);

        List<String> logs = calc.getHistory();
        assertEquals(5, logs.size());
        assertEquals("add(5.0, 5.0)", logs.get(0));
        assertEquals("subtract(10.0, 4.0)", logs.get(1));
        assertEquals("multiply(2.0, 3.0)", logs.get(2));
        assertEquals("divide(8.0, 2.0)", logs.get(3));
        assertEquals("squareRoot(16.0)", logs.get(4));
    }
}
