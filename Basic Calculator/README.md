## Basic Calculator 🧮
A simple command-line calculator built in Java that supports basic operations, 
history tracking, and input validation.

### 📌 Features Implemented
- ✅ **Basic Arithmetic:** Addition (+), Subtraction (-), Multiplication (*), Division (/).
- ✅ **Advanced Operations:** Exponentiation (^), Modulo (%), Square Root (sqrt).
- ✅ **More Math Functions:**
  - **Logarithm:** `log(x)`.
  - **Factorial:** `x!`.
  - **Trigonometric Functions:** `sin(x)`, `cos(x)`, `tan(x)`.
  - **Exponential:** `e^x`.
- ✅ **Full Expression Evaluation:** Supports multiple operations in a single input (e.g., `2 + 3 * 5 / (4 - 1)`)
- ✅ **Calculation History:** View previous calculations and clear history.
- ✅ **Input Validation:** Handles non-numeric inputs and division by zero, and syntax errors.
- ✅ **Continuous Calculation Mode:**
  - Use the latest result as the first operand for new calculations.
  - Option to reset stored results (`clear` command).
  - Prevents divide-by-zero errors and invalid operations.
- ✅ **Undo Last Calculation:** Remove the last entry from history if needed.
- ✅ **Save & Load History:** Calculations are stored in a `history.txt` file and persists across sessions.

## 🔹 Future Enhancements

- 🚀 **Parentheses Handling in Expressions:** Enable full support for expressions with nested operations for advanced calculations.
- 🚀 **Statistical Functions:** Add statistical operations to make the calculator useful for data analysis and engineering.
- 🚀 **Expression History Recall:** Instead of just viewing past calculations, allow users to reuse old expressions directly.
- 🚀 **Graphing Support (for GUI version):** If a graphical interface is added in the future, implement function plotting (e.g., `y = sin(x)`).

## 🚀 How to Run

- 1️⃣ Clone the repository  
- 2️⃣ Navigate to the `Basic Calculator` directory  
- 3️⃣ Compile and run `BasicCalculator.java`  
- 4️⃣ Enter a mathematical expression or use commands (`history`, `clear history`, `clear`, `undo`, `exit`)  