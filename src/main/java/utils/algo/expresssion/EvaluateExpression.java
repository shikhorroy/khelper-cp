package utils.algo.expresssion;

/**
 * Algo: Shunting Yard by Edgar Dijkstra.
 * Complexity: O(n) time and memory, n = expression length.
 * <p>
 * Supports non-negative integer literals, binary + - * /, unary + and - (for example "-3", "2*-3", "-(1+2)"),
 * parentheses and whitespace. Arithmetic is int arithmetic: / truncates toward zero and overflow wraps around.
 * A malformed expression (unknown character, missing operand, unbalanced parentheses, empty input) throws
 * IllegalArgumentException; a literal above Integer.MAX_VALUE throws NumberFormatException.
 * </p>
 * <p>
 * Description:
 * 1. While there are still tokens to be read in,
 *    1.1 Get the next token.
 *    1.2 If the token is:
 *        1.2.1 A number: push it onto the value stack.
 *        1.2.2 A variable: get its value, and push onto the value stack.
 *        1.2.3 A left parenthesis: push it onto the operator stack.
 *        1.2.4 A right parenthesis:
 *          1 While the thing on top of the operator stack is not a left parenthesis,
 *              1 Pop the operator from the operator stack.
 *              2 Pop the value stack twice, getting two operands.
 *              3 Apply the operator to the operands, in the correct order.
 *              4 Push the result onto the value stack.
 *          2 Pop the left parenthesis from the operator stack, and discard it.
 *        1.2.5 An operator (call it thisOp):
 *          1 While the operator stack is not empty, and the top thing on the
 *            operator stack has the same or greater precedence as thisOp,
 *            1 Pop the operator from the operator stack.
 *            2 Pop the value stack twice, getting two operands.
 *            3 Apply the operator to the operands, in the correct order.
 *            4 Push the result onto the value stack.
 *          2 Push thisOp onto the operator stack.
 * 2. While the operator stack is not empty,
 *     1 Pop the operator from the operator stack.
 *     2 Pop the value stack twice, getting two operands.
 *     3 Apply the operator to the operands, in the correct order.
 *     4 Push the result onto the value stack.
 * 3. At this point the operator stack should be empty, and the value
 *    stack should have only one value in it, which is the final result.
 * </p>
 */
public class EvaluateExpression {

    //~ Operator-stack marker for unary minus. It binds tighter than * and /.
    private static final char NEGATE = 'n';

    public static int evaluate(String expression) {
        char[] tokens = expression.toCharArray();

        int[] values = new int[tokens.length];
        char[] ops = new char[tokens.length];
        int valueTop = 0, opTop = 0;
        //~ True when the next token must start an operand: a number, '(' or a unary sign.
        boolean expectOperand = true;

        for (int i = 0; i < tokens.length; i++) {
            char c = tokens[i];
            if (Character.isWhitespace(c)) continue;

            if (c >= '0' && c <= '9') {
                if (!expectOperand) throw malformed(expression, i);
                long number = 0;
                while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9') {
                    number = number * 10 + (tokens[i++] - '0');
                    if (number > Integer.MAX_VALUE) {
                        throw new NumberFormatException("Number too large in: " + expression);
                    }
                }
                i--;
                values[valueTop++] = (int) number;
                expectOperand = false;
            } else if (c == '(') {
                if (!expectOperand) throw malformed(expression, i);
                ops[opTop++] = c;
            } else if (c == ')') {
                if (expectOperand) throw malformed(expression, i);
                while (opTop > 0 && ops[opTop - 1] != '(') valueTop = apply(ops[--opTop], values, valueTop);
                if (opTop == 0) throw malformed(expression, i);
                opTop--;
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                if (expectOperand) {
                    if (c == '*' || c == '/') throw malformed(expression, i);
                    if (c == '-') ops[opTop++] = NEGATE;
                    continue;
                }
                while (opTop > 0 && hasPrecedence(c, ops[opTop - 1])) valueTop = apply(ops[--opTop], values, valueTop);
                ops[opTop++] = c;
                expectOperand = true;
            } else {
                throw malformed(expression, i);
            }
        }
        if (expectOperand) throw malformed(expression, tokens.length);

        while (opTop > 0) {
            char op = ops[--opTop];
            if (op == '(') throw new IllegalArgumentException("Unclosed '(' in: " + expression);
            valueTop = apply(op, values, valueTop);
        }
        return values[0];
    }

    /**
     * True when the operator op2 on top of the stack must be applied before pushing op1, that is when op2 has the
     * same or a higher precedence (all binary operators are left-associative). Parentheses never qualify.
     */
    public static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') return false;
        return precedence(op2) >= precedence(op1);
    }

    private static int precedence(char op) {
        if (op == NEGATE) return 3;
        if (op == '*' || op == '/') return 2;
        if (op == '+' || op == '-') return 1;
        return 0;
    }

    /**
     * Applies a binary operator: returns a op b. Note the argument order: b (right operand) comes first.
     *
     * @throws UnsupportedOperationException on division by zero.
     * @throws IllegalArgumentException      on an unknown operator.
     */
    public static int applyOp(char op, int b, int a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) throw new UnsupportedOperationException("Cannot divide by zero");
                return a / b;
        }
        throw new IllegalArgumentException("Unknown operator: " + op);
    }

    /**
     * Pops the operands of op, pushes the result and returns the new value-stack size.
     */
    private static int apply(char op, int[] values, int top) {
        if (op == NEGATE) {
            values[top - 1] = -values[top - 1];
            return top;
        }
        int b = values[--top];
        int a = values[--top];
        values[top++] = applyOp(op, b, a);
        return top;
    }

    private static IllegalArgumentException malformed(String expression, int position) {
        return new IllegalArgumentException("Malformed expression at index " + position + ": " + expression);
    }
}
