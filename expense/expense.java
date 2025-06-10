package Harshith;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ExpenseTracker {

    static class Transaction {
        enum Type { INCOME, EXPENSE }

        private Type type;
        private String category;
        private double amount;
        private LocalDate date;

        public Transaction(Type type, String category, double amount, LocalDate date) {
            this.type = type;
            this.category = category;
            this.amount = amount;
            this.date = date;
        }

        public Type getType() {
            return type;
        }

        public String getCategory() {
            return category;
        }

        public double getAmount() {
            return amount;
        }

        public LocalDate getDate() {
            return date;
        }

        @Override
        public String toString() {
            return type + "," + category + "," + amount + "," + date;
        }

        public static Transaction fromString(String line) {
            String[] parts = line.split(",");
            return new Transaction(
                Type.valueOf(parts[0].trim()),
                parts[1].trim(),
                Double.parseDouble(parts[2].trim()),
                LocalDate.parse(parts[3].trim())
            );
        }
    }

    static class TransactionManager {
        private List<Transaction> transactions = new ArrayList<>();

        public void addTransaction(Transaction t) {
            transactions.add(t);
        }

        public void loadFromFile(String filename) {
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = br.readLine()) != null) {
                    Transaction t = Transaction.fromString(line);
                    transactions.add(t);
                }
                System.out.println("Data loaded successfully.");
            } catch (IOException e) {
                System.out.println("Error loading from file: " + e.getMessage());
            }
        }

        public void saveToFile(String filename) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
                for (Transaction t : transactions) {
                    pw.println(t);
                }
                System.out.println("Data saved successfully.");
            } catch (IOException e) {
                System.out.println("Error saving to file: " + e.getMessage());
            }
        }

        public void showMonthlySummary(int year, int month) {
            double income = 0, expense = 0;

            for (Transaction t : transactions) {
                if (t.getDate().getYear() == year && t.getDate().getMonthValue() == month) {
                    if (t.getType() == Transaction.Type.INCOME) {
                        income += t.getAmount();
                    } else {
                        expense += t.getAmount();
                    }
                }
            }

            System.out.println("\n--- Monthly Summary for " + year + "-" + String.format("%02d", month) + " ---");
            System.out.printf("Total Income: $%.2f\n", income);
            System.out.printf("Total Expenses: $%.2f\n", expense);
            System.out.printf("Net Savings: $%.2f\n", income - expense);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TransactionManager manager = new TransactionManager();
        String fileName = "data.csv";  // default file

        while (true) {
            System.out.println("\n--- Expense Tracker Menu ---");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Monthly Summary");
            System.out.println("4. Load Data from File");
            System.out.println("5. Save Data to File");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (option) {
                case 1:
                    System.out.print("Enter income category (e.g., Salary, Business): ");
                    String incomeCategory = scanner.nextLine();

                    System.out.print("Enter amount: ");
                    double incomeAmount = scanner.nextDouble();

                    System.out.print("Enter year (e.g., 2025): ");
                    int incomeYear = scanner.nextInt();

                    System.out.print("Enter month (1-12): ");
                    int incomeMonth = scanner.nextInt();

                    scanner.nextLine(); // consume newline
                    manager.addTransaction(new Transaction(
                        Transaction.Type.INCOME,
                        incomeCategory,
                        incomeAmount,
                        LocalDate.of(incomeYear, incomeMonth, 1)
                    ));
                    System.out.println("Income recorded.");
                    break;

                case 2:
                    System.out.print("Enter expense category (e.g., Food, Rent, Travel): ");
                    String expenseCategory = scanner.nextLine();

                    System.out.print("Enter amount: ");
                    double expenseAmount = scanner.nextDouble();

                    System.out.print("Enter year (e.g., 2025): ");
                    int expenseYear = scanner.nextInt();

                    System.out.print("Enter month (1-12): ");
                    int expenseMonth = scanner.nextInt();

                    scanner.nextLine(); // consume newline
                    manager.addTransaction(new Transaction(
                        Transaction.Type.EXPENSE,
                        expenseCategory,
                        expenseAmount,
                        LocalDate.of(expenseYear, expenseMonth, 1)
                    ));
                    System.out.println("Expense recorded.");
                    break;

                case 3:
                    System.out.print("Enter year (e.g., 2025): ");
                    int year = scanner.nextInt();
                    System.out.print("Enter month (1-12): ");
                    int month = scanner.nextInt();
                    manager.showMonthlySummary(year, month);
                    break;

                case 4:
                    System.out.print("Enter filename to load (e.g., data.csv): ");
                    String loadFile = scanner.nextLine();
                    manager.loadFromFile(loadFile);
                    break;

                case 5:
                    System.out.print("Enter filename to save (e.g., data.csv): ");
                    String saveFile = scanner.nextLine();
                    manager.saveToFile(saveFile);
                    break;

                case 6:
                    System.out.println("Exiting. Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
