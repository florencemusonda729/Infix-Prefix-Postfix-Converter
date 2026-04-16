import java.util.*;

public class InfixConverter {

    // Check if a character is an operator
    private static boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^';
    }

    // Return precedence of an operator
    private static int precedence(char op) {
        switch (op) {
            case '+': case '-': return 1;
            case '*': case '/': return 2;
            case '^': return 3;
            default: return 0;
        }
    }

    // Return associativity
    private static boolean isLeftAssociative(char op) {
        return op != '^';
    }

    // VALIDATION METHOD
    public static boolean isValidInfix(String infix) {
        Stack<Character> stack = new Stack<>();
        int operands = 0;
        int operators = 0;

        char prev = '\0';

        for (int i = 0; i < infix.length(); i++) {
            char ch = infix.charAt(i);

            if (Character.isLetterOrDigit(ch)) {
                operands++;

                if (i > 0 && Character.isLetterOrDigit(prev)) {
                    return false;
                }
            }

            else if (ch == '(') {
                stack.push(ch);
            }

            else if (ch == ')') {
                if (stack.isEmpty()) return false;
                stack.pop();
            }

            else if (isOperator(ch)) {
                operators++;

                if (i == 0) return false;
                if (i == infix.length() - 1) return false;
                if (isOperator(prev)) return false;
                if (prev == '(') return false;
            }

            else {
                return false;
            }

            prev = ch;
        }

        if (!stack.isEmpty()) return false;
        if (operands != operators + 1) return false;

        return true;
    }

    // Convert infix to postfix
    public static String infixToPostfix(String infix) {
        StringBuilder output = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (char ch : infix.toCharArray()) {

            if (Character.isLetterOrDigit(ch)) {
                output.append(ch);
            }

            else if (ch == '(') {
                stack.push(ch);
            }

            else if (ch == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    output.append(stack.pop());
                }
                stack.pop();
            }

            else if (isOperator(ch)) {
                while (!stack.isEmpty() && isOperator(stack.peek()) &&
                        ((isLeftAssociative(ch) && precedence(stack.peek()) >= precedence(ch)) ||
                         (!isLeftAssociative(ch) && precedence(stack.peek()) > precedence(ch)))) {
                    output.append(stack.pop());
                }
                stack.push(ch);
            }
        }

        while (!stack.isEmpty()) {
            output.append(stack.pop());
        }

        return output.toString();
    }

    // Convert infix to prefix
    public static String infixToPrefix(String infix) {
        StringBuilder reversed = new StringBuilder(infix).reverse();

        for (int i = 0; i < reversed.length(); i++) {
            char c = reversed.charAt(i);
            if (c == '(') reversed.setCharAt(i, ')');
            else if (c == ')') reversed.setCharAt(i, '(');
        }

        String postfix = infixToPostfix(reversed.toString());

        // ✅ FIXED LINE HERE
        return new StringBuilder(postfix).reverse().toString();
    }

    // MAIN METHOD WITH LOOP
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("\nEnter infix expression (or type 'exit' to quit): ");
            String infix = scanner.nextLine().replaceAll("\\s+", "");

            if (infix.equalsIgnoreCase("exit")) {
                System.out.println("Program terminated.");
                break;
            }

            if (!isValidInfix(infix)) {
                System.out.println("Invalid Infix Expression!");
            } else {
                System.out.println("Postfix: " + infixToPostfix(infix));
                System.out.println("Prefix : " + infixToPrefix(infix));
            }
        }

        scanner.close();
    }
};