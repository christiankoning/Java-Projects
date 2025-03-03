import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

public class BasicCalculator {
    private static HistoryManager historyManager = new HistoryManager();
    private static List<String> history = historyManager.loadHistoryFromFile();
    private static double previousResult = Double.NaN;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.println("Enter a mathematical expression (e.g., 5 + 3 * 2) or a command (history, clear history, clear, undo, exit):");

            String operator = scanner.nextLine().trim();

            if(operator.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the calculator...");
                break;
            }

            if(operator.equalsIgnoreCase("history")) {
                showHistory();
                continue;
            }

            if(operator.equalsIgnoreCase("clear history")) {
                history.clear();
                System.out.println("Calculation history cleared.");
                continue;
            }

            if(operator.equalsIgnoreCase("clear")) {
                previousResult = Double.NaN;
                System.out.println("Previous result cleared.");
                continue;
            }

            if(operator.equalsIgnoreCase("undo")) {
                if(!history.isEmpty()) {
                   history.remove(history.size() - 1);
                   System.out.println("Last calculation undone.");
                } else {
                    System.out.println("No calculations to undo.");
                }
                continue;
            }
            performCalculation(operator);
        }
    }

    private static void showHistory() {
        if(history.isEmpty()) {
            System.out.println("No calculations yet.");
        } else {
            System.out.println("Calculation history:");
            for (String entry : history) {
                System.out.println(entry);
            }
        }
    }

    private static void showResult(double result, String operation) {
        if(Double.isNaN(result) || result == Double.POSITIVE_INFINITY || result == Double.NEGATIVE_INFINITY) {
            System.out.println("Calculation failed, no result stored.");
        } else {
            System.out.println("Result: " + result);
            history.add(operation + " = " + result);
            previousResult = result;
            historyManager.saveHistoryToFile(history);
        }
    }

    private static void performCalculation(String input) {
        if (!Double.isNaN(previousResult)) {
            input = input.replace("ans", String.valueOf(previousResult));
        }

        double result = ExpressionEvaluator.evaluateExpression(input);

        if(!Double.isNaN(result)) {
            showResult(result, input);
        } else {
            System.out.println("Invalid expression. Please enter a valid mathematical expression.");
        }
    }
}

class HistoryManager {
    private static final String HISTORY_FILE_PATH = "Basic Calculator/history.txt";
    public void saveHistoryToFile(List<String> history) {
        try(FileWriter writer = new FileWriter(HISTORY_FILE_PATH)) {
            for (String entry : history) {
                writer.write(entry + "\n");
            }
            System.out.println("History saved successfully.");
        } catch(IOException e) {
            System.out.println("Error saving history: " + e.getMessage());
        }
    }

    public List<String> loadHistoryFromFile() {
        List<String> loadedHistory = new ArrayList<>();
        File file = new File(HISTORY_FILE_PATH);

        if(!file.exists()) {
            System.out.println("No previous history found.");
            return loadedHistory;
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(HISTORY_FILE_PATH))) {
            String line;
            while((line = reader.readLine()) != null) {
                loadedHistory.add(line);
            }
            System.out.println("History loaded succesfully.");
        } catch(IOException e) {
            System.out.println("Error loading history: " + e.getMessage());
        }
        return loadedHistory;
    }
}

class ExpressionEvaluator {
    public static double evaluateExpression(String expression) {
        try {
            return evaluatePostfix(convertToPostfix(expression));
        } catch (Exception e) {
            System.out.println("Invalid expression. Please try again.");
            return Double.NaN;
        }
    }

    private static String convertToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        StringTokenizer tokens = new StringTokenizer(infix, "+-*/^%()", true);
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken().trim();
            if (token.isEmpty()) continue;

            if (isNumber(token)) {
                postfix.append(token).append(" ");
            } else if (token.equals("(")) {
                stack.push('(');
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop()).append(" ");
                }
                stack.pop();
            } else {
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(token.charAt(0))) {
                    postfix.append(stack.pop()).append(" ");
                }
                stack.push(token.charAt(0));
            }
        }

        while (!stack.isEmpty()) {
            postfix.append(stack.pop()).append(" ");
        }
        return postfix.toString();
    }

    private static double evaluatePostfix(String postfix) {
        Stack<Double> stack = new Stack<>();
        StringTokenizer tokens = new StringTokenizer(postfix);

        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else {
                double b = stack.pop();
                double a = stack.pop();
                switch (token.charAt(0)) {
                    case '+': stack.push(a + b); break;
                    case '-': stack.push(a - b); break;
                    case '*': stack.push(a * b); break;
                    case '/': stack.push(a / b); break;
                    case '^': stack.push(Math.pow(a, b)); break;
                    case '%': stack.push(a % b); break;
                }
            }
        }
        return stack.pop();
    }

    private static boolean isNumber(String token) {
        return token.matches("-?\\d+(\\.\\d+)?");
    }

    private static int precedence(char operator) {
        return switch (operator) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            case '^' -> 3;
            default -> 0;
        };
    }
}
