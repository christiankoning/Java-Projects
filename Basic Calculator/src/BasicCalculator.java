import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BasicCalculator {
    private static List<String> history = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an operator: +, -, *, /, ^, %, sqrt, history, clear history, or type 'exit' to quit:");

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
        System.out.println("Enter the first number:");
        double num1 = scanner.nextDouble();
        System.out.println("Enter the second number:");
        double num2 = scanner.nextDouble();
        scanner.nextLine();
        return new double[]{num1, num2};
    }

    private static double getSingleNumber(Scanner scanner) {
        System.out.println("Enter the first number:");
        double num = scanner.nextDouble();
        scanner.nextLine();
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
        System.out.println("Result: " + result);
        history.add(operation + " = " + result);
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
            return 0;
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
            return 0;
        } else {
            return Math.sqrt(a);
        }
    }
}
