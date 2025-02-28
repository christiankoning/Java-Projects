import java.util.Scanner;

public class BasicCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an operator: +, -, *, /, or type 'exit' to quit:");

            String operator = scanner.nextLine();

            if (operator.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the calculator...");
                break;
            }

            double result = 0;

            switch (operator) {
                case "+":
                    double[] additionNumbers = getNumbers(scanner);
                    result = CalculatorOperations.add(additionNumbers[0], additionNumbers[1]);
                    showResult(result);
                    break;
                case "-":
                    double[] subtractionNumbers = getNumbers(scanner);
                    result = CalculatorOperations.subtract(subtractionNumbers[0], subtractionNumbers[1]);
                    showResult(result);
                    break;
                case "*":
                    double[] multiplicationNumbers = getNumbers(scanner);
                    result = CalculatorOperations.multiply(multiplicationNumbers[0], multiplicationNumbers[1]);
                    showResult(result);
                    break;
                case "/":
                    double[] divisionNumbers = getNumbers(scanner);
                    result = CalculatorOperations.divide(divisionNumbers[0], divisionNumbers[1]);
                    showResult(result);
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

    private static void showResult(double result) {
        System.out.println("Result: " + result);
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
}
