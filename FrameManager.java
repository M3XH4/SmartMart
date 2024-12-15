import java.util.*;
import java.text.SimpleDateFormat;

public class FrameManager implements GlobalInterface {
    Scanner input;

    public FrameManager() {
        input = new Scanner(System.in);
    }
    public void titleFrame() {
        ArrayList<String> title = new ArrayList<>(Arrays.asList(
                "███████ ███    ███  █████  ██████  ████████ ███    ███  █████  ██████  ████████",
                "██      ████  ████ ██   ██ ██   ██    ██    ████  ████ ██   ██ ██   ██    ██   ",
                "███████ ██ ████ ██ ███████ ██████     ██    ██ ████ ██ ███████ ██████     ██   ",
                "     ██ ██  ██  ██ ██   ██ ██   ██    ██    ██  ██  ██ ██   ██ ██   ██    ██   ",
                "███████ ██      ██ ██   ██ ██   ██    ██    ██      ██ ██   ██ ██   ██    ██   "
        ));

        GlobalInterface.printBackgroundColor(PRIMARY_COMBO, width);
        for (String t : title) {
            GlobalInterface.centerString(PRIMARY_COMBO, t);
        }
        System.out.println(PRIMARY_COMBO + " ".repeat(width) + RESET);
    }

    public Employee loginFrame(EmployeeManager employeeManager) throws InterruptedException {
        do {
            GlobalInterface.clearScreen();
            boolean wrongAccount = false;
            titleFrame();
            GlobalInterface.printBackgroundColor(BACKGROUND_WHITE_BRIGHT);
            GlobalInterface.printBackgroundColor(BACKGROUND_WHITE_BRIGHT);
            GlobalInterface.printBackgroundColor(BACKGROUND_WHITE_BRIGHT);
            GlobalInterface.printBackgroundColor(BACKGROUND_WHITE_BRIGHT);
    
            System.out.print(GlobalInterface.printPaddingLeft(SECONDARY_COMBO, width - 176) + "ENTER USERNAME: ");
            String username = input.nextLine();
            System.out.print(RESET + MOVE_UP + CLEAR_LINE);
            GlobalInterface.centerString(SECONDARY_COMBO, "ENTER USERNAME: " + GlobalInterface.putSpaceGaps(30, username));
    
            System.out.print(GlobalInterface.printPaddingLeft(SECONDARY_COMBO, width - 176) + "ENTER PASSWORD: ");
            String password = input.nextLine();
            System.out.print(RESET + MOVE_UP + CLEAR_LINE);
            GlobalInterface.centerString(SECONDARY_COMBO, "ENTER PASSWORD: " + GlobalInterface.putSpaceGaps(30, password));
    
            for (Employee employee : employeeManager.getEmployees()) {
                if (employee.getUsername().equals(username) && employee.getPassword().equals(password)) {
                    GlobalInterface.centerString(CORRECT_COMBO, "SUCCESSFULLY LOGIN. PLEASE WAIT...");
                    GlobalInterface.printBackgroundColor(BACKGROUND_WHITE_BRIGHT);
                    GlobalInterface.printBackgroundColor(BACKGROUND_WHITE_BRIGHT);
                    GlobalInterface.printBackgroundColor(BACKGROUND_WHITE_BRIGHT);
                    GlobalInterface.printBackgroundColor(BACKGROUND_WHITE_BRIGHT);
                    GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                    GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                    GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                    Thread.sleep(2000);
                    if (employee instanceof Manager manager) {
                        return manager;
                    } else if (employee instanceof Cashier cashier) {
                        return cashier;
                    }
                } else {
                    wrongAccount = true;
                }
            }
    
            if (wrongAccount) {
                GlobalInterface.centerString(WRONG_COMBO, "WRONG CREDENTIALS. PLEASE TRY AGAIN...");
            }
    
            GlobalInterface.printBackgroundColor(BACKGROUND_WHITE_BRIGHT);
            GlobalInterface.printBackgroundColor(BACKGROUND_WHITE_BRIGHT);
            GlobalInterface.printBackgroundColor(BACKGROUND_WHITE_BRIGHT);
            GlobalInterface.printBackgroundColor(BACKGROUND_WHITE_BRIGHT);
            GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
            GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
            GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
            Thread.sleep(2000);
            return null;
        } while (true);
    }

    public void posForm(Employee employee, CartManager cartManager) {
        ArrayList<String> title = new ArrayList<>(Arrays.asList(
           "█▀█ █▀█ ▀█▀ █▀█ ▀█▀     █▀█ █▀▀     █▀▀ █▀█ █   █▀▀",
           "█▀▀ █ █  █  █ █  █  ▄▄▄ █ █ █▀▀ ▄▄▄ ▀▀█ █▀█ █   █▀▀",
           "█   █▄█ ▄█▄ █ █  █      █▄█ █       ▄▄█ █ █ █▄▄ █▄▄"
        ));
        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
        for (String t : title) {
            GlobalInterface.centerString(SECONDARY_COMBO, t);
        }
        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
        GlobalInterface.centerStringLine(SECONDARY_COMBO, "DETAILS");
        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
        //DATE
        Date now = new Date();
        SimpleDateFormat formatterDate = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = formatterDate.format(now);
        //COLUMN
        int column1Percentage = 60;
        int column2Percentage = 40;
        int column1Width = (width * column1Percentage / 100) - 6;
        int column2Width = (width * column2Percentage / 100) - 1;

        double totalAmount = 0;
        for (Product product : cartManager.getShoppingCart()) {
            totalAmount += product.getPrice();
        }

        System.out.printf(SECONDARY_COMBO + "| %-" + column1Width + "s | %-" + column2Width + "s  |" + RESET + "%n",
                ("CASHIER: " + employee.getName()), ("TOTAL COST: PHP " + totalAmount)
        );
        System.out.printf(SECONDARY_COMBO + "| %-" + column1Width + "s | %-" + column2Width + "s  |" + RESET + "%n",
                ("DATETIME: " + formattedDate), ""
        );
    }
    public void posFormOptions() {
        int columnPercentage = 20;
        int columnWidth = (width * columnPercentage / 100) - 3;

        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
        System.out.printf(SECONDARY_COMBO + "| %s | %s | %s | %s | %s   |" + RESET + "%n",
                GlobalInterface.centerColumnString("ADD - Adds Product To Cart", columnWidth), GlobalInterface.centerColumnString("EDIT - Edits Quantity Of Product", columnWidth),
                GlobalInterface.centerColumnString("REMOVE - Removes Product To Cart", columnWidth), GlobalInterface.centerColumnString("CHECKOUT - Checkouts The Cart", columnWidth),
                GlobalInterface.centerColumnString("EXIT - Exits System", columnWidth)
        );
        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
    }
    public void adminMenuFrame() {
        ArrayList<String> title = new ArrayList<>(Arrays.asList(
                "█▄█ █▀▀ █▀█ █ █",
                "█ █ █▀▀ █ █ █ █",
                "█ █ █▄▄ █ █ █▄█"
        ));
        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
        for (String t : title) {
            GlobalInterface.centerString(SECONDARY_COMBO, t);
        }
        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
        GlobalInterface.centerString(SECONDARY_COMBO, GlobalInterface.putSpaceGaps(40, "INVENTORY   - Proceed To Inventory Page"));
        //GlobalInterface.centerString(SECONDARY_COMBO, GlobalInterface.putSpaceGaps(40, "SALES       - Proceed To Sales Page"));
        GlobalInterface.centerString(SECONDARY_COMBO, GlobalInterface.putSpaceGaps(40, "ACCOUNTS    - Proceed To Accounts Page"));
        GlobalInterface.centerString(SECONDARY_COMBO, GlobalInterface.putSpaceGaps(40, "PRINTER     - Edit Printer Settings"));
        GlobalInterface.centerString(SECONDARY_COMBO, GlobalInterface.putSpaceGaps(40, "LOGOUT      - Go Back To Login Form"));
        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
    }

    public void inventoryTitleFrame() {
        ArrayList<String> title = new ArrayList<>(Arrays.asList(
           "▀█▀ █▀█ █ █ █▀▀ █▀█ ▀█▀ █▀█ █▀▄ █ █",
           " █  █ █ ▀▄▀ █▀▀ █ █  █  █ █ █▀▄  █ ",
           "▄█▄ █ █  █  █▄▄ █ █  █  █▄█ █ █  █ "
        ));

        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
        for (String t : title) {
            GlobalInterface.centerString(SECONDARY_COMBO, t);
        }
    }
    public void inventoryOptionFrame() {
        int columnPercentage = 25;
        int columnWidth = (width * columnPercentage / 100) - 3;

        System.out.printf(SECONDARY_COMBO + "| %s | %s | %s |  %s  |" + RESET + "%n",
                GlobalInterface.centerColumnString("ADD - Inserts Product", columnWidth), GlobalInterface.centerColumnString("DELETE - Removes Product", columnWidth),
                GlobalInterface.centerColumnString("EDIT - Modifies Product", columnWidth), GlobalInterface.centerColumnString("BACK - Go Back To Menu", columnWidth)
        );

        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
    }
    public void inventoryEditForm(Product product, String changeMessage) {
        ArrayList<String> title = new ArrayList<>(Arrays.asList(
            "█▀▀ █▀▄ ▀█▀ ▀█▀   █▀█ █▀▄ █▀█ █▀▄ █ █ █▀▀ ▀█▀",
            "█▀▀ █ █  █   █    █▀▀ █▀▄ █ █ █ █ █ █ █    █ ",
            "▀▀▀ ▀▀  ▀▀▀  ▀    ▀   ▀ ▀ ▀▀▀ ▀▀  ▀▀▀ ▀▀▀  ▀ "
        ));
        for (String t : title) {
            GlobalInterface.centerString(SECONDARY_COMBO, t);
        }
        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
        GlobalInterface.centerString(SECONDARY_COMBO, "SELECTED PRODUCT:  " + product.getName());
        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
        GlobalInterface.centerString(SECONDARY_COMBO, GlobalInterface.putSpaceGaps(36, "ID          - Edit ID"));
        GlobalInterface.centerString(SECONDARY_COMBO, GlobalInterface.putSpaceGaps(36, "NAME        - Edit Name"));
        GlobalInterface.centerString(SECONDARY_COMBO, GlobalInterface.putSpaceGaps(36, "STOCK       - Edit Stock"));
        GlobalInterface.centerString(SECONDARY_COMBO, GlobalInterface.putSpaceGaps(36, "PRICE       - Edit Price"));
        GlobalInterface.centerString(SECONDARY_COMBO, GlobalInterface.putSpaceGaps(36, "THRESHOLD   - Edit Low Threshold"));
        GlobalInterface.centerString(SECONDARY_COMBO, GlobalInterface.putSpaceGaps(36, "CHANGE      - Change Product "));
        if (!changeMessage.isBlank()) {
            GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
            GlobalInterface.centerString(SECONDARY_COMBO, changeMessage);
            GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
        } else {
            GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
            GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
            GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
        }
        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
    }
    public void inventoryDeleteFrom(Product product) {
        ArrayList<String> title = new ArrayList<>(Arrays.asList(
            "█▀▄ █▀▀ █   █▀▀ ▀█▀ █▀▀   █▀█ █▀▄ █▀█ █▀▄ █ █ █▀▀ ▀█▀",
            "█ █ █▀▀ █   █▀▀  █  █▀▀   █▀▀ █▀▄ █ █ █ █ █ █ █    █ ",
            "▀▀  ▀▀▀ ▀▀▀ ▀▀▀  ▀  ▀▀▀   ▀   ▀ ▀ ▀▀▀ ▀▀  ▀▀▀ ▀▀▀  ▀ "
        ));
        int columnPercentage = 50;
        int columnWidth = (width * columnPercentage / 100) - 3;

        for (String t : title) {
            GlobalInterface.centerString(SECONDARY_COMBO, t);
        }
        GlobalInterface.printHorizontalLine(SECONDARY_COMBO); 
        GlobalInterface.centerString(SECONDARY_COMBO, "SELECTED PRODUCT:  " + product.getName());

        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
        System.out.printf(SECONDARY_COMBO + "| %s | %s |" + RESET + "%n",
                GlobalInterface.centerColumnString("DELETE - Permanently Delete Product", columnWidth), GlobalInterface.centerColumnString("BACK - Back To Selection", columnWidth)
        );
        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
    }
    public void inventoryAddForm(InventoryManager inventoryManager) {
        ArrayList<String> title = new ArrayList<>(Arrays.asList(
            "█▀█ █▀▄ █▀▄   █▀█ █▀▄ █▀█ █▀▄ █ █ █▀▀ ▀█▀", 
            "█▀█ █ █ █ █   █▀▀ █▀▄ █ █ █ █ █ █ █    █ ",
            "▀ ▀ ▀▀  ▀▀    ▀   ▀ ▀ ▀▀▀ ▀▀  ▀▀▀ ▀▀▀  ▀ "
        ));
        titleFrame();
        inventoryTitleFrame();
        inventoryManager.display();
        for (String t : title) {
            GlobalInterface.centerString(SECONDARY_COMBO, t);
        }
        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
        GlobalInterface.centerString(SECONDARY_COMBO, "PLEASE INPUT THE RIGHT VALUE IN FOLLOWING QUESTIONS. YOU CAN TYPE CANCEL TO GO BACK TO SELECTION");
        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
    }
    public void accountsOptionFrame() {
        int column1Percentage = 40;
        int column2Percentage = 20;
        int column1Width = (width * column1Percentage / 100) - 5;
        int column2Width = (width * column2Percentage / 100);

        System.out.printf(SECONDARY_COMBO + "| %s | %s | %s  |" + RESET + "%n",
                GlobalInterface.centerColumnString("ADD - Adds Account", column1Width), GlobalInterface.centerColumnString("DELETE - Deletes Account", column1Width),
                GlobalInterface.centerColumnString("BACK - Go Back To Menu", column2Width)
        );

        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
    }
    public void accountsTitleFrame() {
        ArrayList<String> title = new ArrayList<>(Arrays.asList(
           "█▀█ █▀▀ █▀▀ █▀█ █ █ █▀█ ▀█▀ █▀▀",
           "█▀█ █   █   █ █ █ █ █ █  █  ▀▀█",
           "█ █ █▄▄ █▄▄ █▄█ █▄█ █ █  █  ▄▄█"
        ));
        for (String t : title) {
            GlobalInterface.centerString(SECONDARY_COMBO, t);
        }
    }
    public void accountDeleteFrom(Employee employee) {
        ArrayList<String> title = new ArrayList<>(Arrays.asList(
            "█▀▄ █▀▀ █   █▀▀ ▀█▀ █▀▀   █▀█ █▀▀ █▀▀ █▀█ █ █ █▀█ ▀█▀",
            "█ █ █▀▀ █   █▀▀  █  █▀▀   █▀█ █   █   █ █ █ █ █ █  █ ",
            "▀▀  ▀▀▀ ▀▀▀ ▀▀▀  ▀  ▀▀▀   ▀ ▀ ▀▀▀ ▀▀▀ ▀▀▀ ▀▀▀ ▀ ▀  ▀ "
        ));
        int columnPercentage = 50;
        int columnWidth = (width * columnPercentage / 100) - 3;

        for (String t : title) {
            GlobalInterface.centerString(SECONDARY_COMBO, t);
        }
        GlobalInterface.printHorizontalLine(SECONDARY_COMBO); 
        GlobalInterface.centerString(SECONDARY_COMBO, "SELECTED ACCOUNT:  " + employee.getName() + " : " + employee.getUsername());

        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
        System.out.printf(SECONDARY_COMBO + "| %s | %s |" + RESET + "%n",
                GlobalInterface.centerColumnString("DELETE - Permanently Delete Account", columnWidth), GlobalInterface.centerColumnString("BACK - Back To Selection", columnWidth)
        );
        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
    }
    public void accountAddForm() {
        ArrayList<String> title = new ArrayList<>(Arrays.asList(
            "█▀█ █▀▄ █▀▄   █▀█ █▀▀ █▀▀ █▀█ █ █ █▀█ ▀█▀",
            "█▀█ █ █ █ █   █▀█ █   █   █ █ █ █ █ █  █ ",
            "▀ ▀ ▀▀  ▀▀    ▀ ▀ ▀▀▀ ▀▀▀ ▀▀▀ ▀▀▀ ▀ ▀  ▀ "
        ));

        for (String t : title) {
            GlobalInterface.centerString(SECONDARY_COMBO, t);
        }
        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
        GlobalInterface.centerString(SECONDARY_COMBO, "PLEASE INPUT THE RIGHT VALUE IN FOLLOWING QUESTIONS. YOU CAN TYPE CANCEL TO GO BACK TO SELECTION");
        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
    }
    public void printerFrame() {
        ArrayList<String> title = new ArrayList<>(Arrays.asList(
           "█▀█ █▀▄ ▀█▀ █▀█ ▀█▀ █▀▀ █▀▄",
           "█▀▀ █▀▄  █  █ █  █  █▀▀ █▀▄",
           "█   █ █ ▄█▄ █ █  █  █▄▄ █ █"
        ));
        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
        for (String t : title) {
            GlobalInterface.centerString(SECONDARY_COMBO, t);
        }
        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
        PrintManager.displayPrinters();
        PrintManager.displayCurrentPrinter();
    }
}
