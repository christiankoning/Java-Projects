import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BasicCalculator {
    private static List<String> history = new ArrayList<>();
    private static double previousResult = Double.NaN;
    private static HistoryManager historyManager = new HistoryManager();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an operator: +, -, *, /, ^, %, sqrt, history, clear history, clear, undo, or type 'exit' to quit:");

            String operator = scanner.nextLine();

            if (operator.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the calculator...");
                break;
            }

            if (operator.equalsIgnoreCase("history")) {
                showHistory();
                continue;
            }

            if (operator.equalsIgnoreCase("clear history")) {
                history.clear();
                System.out.println("Calculation history cleared.");
                continue;
            }

            if (operator.equalsIgnoreCase("clear")) {
                previousResult = Double.NaN;
                System.out.println("Previous result cleared.");
                continue;
            }

            if (operator.equalsIgnoreCase("undo")) {
                if (!history.isEmpty()) {
                   history.remove(history.size() - 1);
                   System.out.println("Last calculation undone.");
                } else {
                    System.out.println("No calculations to undo.");
                }
                continue;
            }

            double result;

            switch (operator) {
                case "+":
                    double[] additionNumbers = getNumbers(scanner);
                    result = CalculatorOperations.add(additionNumbers[0], additionNumbers[1]);
                    showResult(result, additionNumbers[0] + " + " + additionNumbers[1]);
                    break;
                case "-":
                    double[] subtractionNumbers = getNumbers(scanner);
                    result = CalculatorOperations.subtract(subtractionNumbers[0], subtractionNumbers[1]);
                    showResult(result, subtractionNumbers[0] + " - " + subtractionNumbers[1]);
                    break;
                case "*":
                    double[] multiplicationNumbers = getNumbers(scanner);
                    result = CalculatorOperations.multiply(multiplicationNumbers[0], multiplicationNumbers[1]);
                    showResult(result, multiplicationNumbers[0] + " * " + multiplicationNumbers[1]);
                    break;
                case "/":
                    double[] divisionNumbers = getNumbers(scanner);
                    result = CalculatorOperations.divide(divisionNumbers[0], divisionNumbers[1]);
                    showResult(result, divisionNumbers[0] + " / " + divisionNumbers[1]);
                    break;
                case "^":
                    double[] powerNumbers = getNumbers(scanner);
                    result = CalculatorOperations.exponentiation(powerNumbers[0], powerNumbers[1]);
                    showResult(result, powerNumbers[0] + " ^ " + powerNumbers[1]);
                    break;
                case "%":
                    double[] modulusNumbers = getNumbers(scanner);
                    result = CalculatorOperations.modulo(modulusNumbers[0], modulusNumbers[1]);
                    showResult(result, modulusNumbers[0] + " % " + modulusNumbers[1]);
                    break;
                case "sqrt":
                    double sqrtNumber = getSingleNumber(scanner);
                    result = CalculatorOperations.squareRoot(sqrtNumber);
                    showResult(result, "√" + sqrtNumber);
                    break;
                default:
                    System.out.println("Invalid operator, try again.");
            }

        }
    }

    private static double[] getNumbers(Scanner scanner) {
        double num1 = 0, num2 = 0;
        boolean validInput = false;

        if (!Double.isNaN(previousResult)) {
            System.out.println("Use previous result? (yes/no)");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("yes")) {
                while (!validInput) {
                    try {
                        num1 = previousResult;
                        System.out.println("Enter the second number:");
                        num2 = Double.parseDouble(scanner.nextLine());
                        return new double[]{num1, num2};
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter numbers only.");
                    }
                }
            }
        }

        while (!validInput) {
            try {
                System.out.println("Enter the first number:");
                num1 = Double.parseDouble(scanner.nextLine());

                System.out.println("Enter the second number:");
                num2 = Double.parseDouble(scanner.nextLine());

                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter numbers only.");
            }
        }
        return new double[]{num1, num2};
    }

    private static double getSingleNumber(Scanner scanner) {
        double num = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.println("Enter a number:");
                num = Double.parseDouble(scanner.nextLine());
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return num;
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
        if (Double.isNaN(result) || result == Double.POSITIVE_INFINITY || result == Double.NEGATIVE_INFINITY) {
            System.out.println("Calculation failed, no result stored.");
        } else {
            System.out.println("Result: " + result);
            history.add(operation + " = " + result);
            previousResult = result;
            historyManager.saveHistoryToFile(history);
        }
    }
}
class CalculatorOperations {

    public static double add(double a, double b) {
        return a + b;
    }

    public static double subtract(double a, double b) {
        return a - b;
    }

    public static double multiply(double a, double b) {
        return a * b;
    }

    public static double divide(double a, double b) {
        if(b == 0) {
            System.out.println("Error: Cannot divide by zero");
            return Double.NaN;
        } else {
            return a / b;
        }
    }

    public static double exponentiation(double a, double b) {
        return Math.pow(a, b);
    }

    public static double modulo(double a, double b) {
        return a % b;
    }

    public static double squareRoot(double a) {
        if (a < 0) {
            System.out.println("Error: Cannot take the square root of a negative number.");
            return Double.NaN;
        } else {
            return Math.sqrt(a);
        }
    }
}

class HistoryManager {

    public void saveHistoryToFile(List<String> history) {
        try (FileWriter writer = new FileWriter("Basic Calculator/history.txt")) {
            for (String entry : history) {
                writer.write(entry + "\n");
            }
            System.out.println("History saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving history: " + e.getMessage());
        }
    }

    public void loadHistoryFromFile() {

    }
}
