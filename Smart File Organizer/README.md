## Smart File Organizer 📂
A CLI-based file organizer that automatically sorts files based on **type, creation date, or user-defined rules**. <br>
This project will later expand into a **GUI version** using JavaFX.

### 📌 Features Implemented
- ✅ **File Type Sorting:** Automatically moves files into categorized folders (`Images`, `Documents`, etc.).
- ✅ **Date-Based Organization:** Sorts files into subfolders by modification/creation date (`2025-03`).
- ✅ **Duplicate File Detection:** Identifies duplicate files and offers options (`delete`, `rename`, `move`).
- ✅ **Logging & Reports:** Generates logs of all actions performed.
- ✅ **Undo Functionality:** In case files are moved incorrectly, allow users to revert actions.
- ✅ **User-Defined Rules:** Custom rules for file organization (e.g., regex-based naming patterns).

## 🔹 Future Enhancements

- **File Preview Feature (GUI):** Allows users to preview files before moving them.
- **Drag-and-Drop Functionality (GUI):** Users can drop folders/files into the application for processing.
- **Multi-threaded Performance Optimization:** Faster processing for large directories.

## 🚀 How to Run

- 1️⃣ Clone the repository
- 2️⃣ Navigate to the Smart File Organizer directory
- 3️⃣ Compile and run Main.java
- 4️⃣ Enter the directory path when prompted
- 5️⃣ Choose an organization method (by type, date, or custom rules)

## 📜 User-Defined Rules Guide
The **Smart File Organizer** allows users to define their own sorting rules using a `rules.txt` file. This file should
be placed in the **project directory** (`Smart File Organizer/rules.txt`) and follow this format:

`<regex_pattern> -> <Category_Name>`

### Example Rules + Explanation
`Invoice_.* -> Invoices` <br>
- Any file that **starts with** "invoice_" (e.g., `Invoice_2024.pdf`, `Invoice_March.xlsx`) will be moved to the `Invoices` folder.

`.*_report\.pdf -> Reports` <br>
- Any `.pdf` file that **ends with** `_report.pdf` (e.g., `sales_report.pdf`, `weekly_report.pdf`) will be moved to the `Reports` folder.

`.*\.(zip|rar|7z) -> Archives` <br>
- Any file with an **extension** `.zip`, `.rar`, or `.7z` will be moved to the `Archives` folder.

### How to Use Custom Rules
- Modify or add new rules in `rules.txt` in the **Smart File Organizer project folder.**
- Run the program and choose any sorting method (`type`, `date`).
- The program will **automatically check user-defined rules first** before applying default sorting.
- The files that match a rule will be moved **according to the custom rules**, while others follow the selected sorting method.