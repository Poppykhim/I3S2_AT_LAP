package lab02;

public class Calculator {
    private boolean debugMode = false;
    private java.util.List<String> history = new java.util.ArrayList<>();

    public Calculator() {}

    public Calculator(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public java.util.List<String> getHistory() {
        return history;
    }

    public double add(double a, double b) {
        if (debugMode) {
            history.add("add(" + a + ", " + b + ")");
        }
        double result = a + b;
        if (Double.isInfinite(result)) {
            throw new ArithmeticException("Double overflow occurred during addition");
        }
        return result;
    }

    public double subtract(double a, double b) {
        if (debugMode) {
            history.add("subtract(" + a + ", " + b + ")");
        }
        double result = a - b;
        if (Double.isInfinite(result)) {
            throw new ArithmeticException("Double overflow occurred during subtraction");
        }
        return result;
    }

    public double multiply(double a, double b) {
        if (debugMode) {
            history.add("multiply(" + a + ", " + b + ")");
        }
        double result = a * b;
        if (Double.isInfinite(result)) {
            throw new ArithmeticException("Double overflow occurred during multiplication");
        }
        return result;
    }

    public double divide(double a, double b) {
        if (debugMode) {
            history.add("divide(" + a + ", " + b + ")");
        }
        if (b == 0.0) {
            throw new ArithmeticException("Division by zero");
        }
        return a / b;
    }

    public double squareRoot(double a) {
        if (debugMode) {
            history.add("squareRoot(" + a + ")");
        }
        if (a < 0) {
            throw new IllegalArgumentException("Cannot calculate square root of a negative number");
        }
        return Math.sqrt(a);
    }

    public double calculate(double a, double b, String operator) {
        if (operator == null) {
            throw new IllegalArgumentException("Operator cannot be null");
        }
        String op = operator.trim();
        if (op.isEmpty()) {
            throw new IllegalArgumentException("Operator cannot be empty");
        }

        switch (op) {
            case "+":
                return add(a, b);
            case "-":
                return subtract(a, b);
            case "*":
                return multiply(a, b);
            case "/":
                return divide(a, b);
            default:
                throw new UnsupportedOperationException("Unsupported operator: " + op);
        }
    }
}
