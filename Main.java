import java.io.*;
import java.util.*;

public class Main implements GlobalInterface {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner input = new Scanner(System.in);
        FrameManager frameManager = new FrameManager();
        InventoryManager inventoryManager = new InventoryManager();
        EmployeeManager employeeManager = new EmployeeManager();
        FileManager.createFile();

        ArrayList<Product> loadProducts = FileManager.loadInventory();
        if (loadProducts != null && !loadProducts.isEmpty()) {
            inventoryManager.setProducts(loadProducts);
        } else {
            initializeInventory(inventoryManager);
        }

        ArrayList<Employee> loadEmployees = FileManager.loadEmployees();
        if (loadEmployees != null && !loadEmployees.isEmpty()) {
            employeeManager.setEmployees(loadEmployees);
        } else {
            initializeEmployee(employeeManager);
        }

        PrintManager.printer = FileManager.loadPrinter();

        CartManager cartManager = new CartManager(inventoryManager);
        BarcodeServer barcodeServer = new BarcodeServer(inventoryManager, cartManager);
        barcodeServer.startServer();

        while (true) {
            GlobalInterface.clearScreen();
            Employee employee = frameManager.loginFrame(employeeManager);
            GlobalInterface.clearScreen();
            if (employee instanceof Cashier) {
                do {
                    frameManager.titleFrame();
                    frameManager.posForm(employee, cartManager);
                    cartManager.display();
                    frameManager.posFormOptions();
                    String posSelection = response(" Which Option Would You Like To Do: ");
                    GlobalInterface.clearScreen();
                    if (posSelection.equals("ADD")) {
                        do {
                            boolean continueOption = false;
                            Product tempProduct = null;
                            
                            frameManager.titleFrame();
                            frameManager.posForm(employee, cartManager);
                            cartManager.display();
                            GlobalInterface.printHorizontalLine(SECONDARY_COMBO);

                            if (BarcodeServer.getScannedBarcode() != null) {
                                String scannedBarcode = BarcodeServer.getScannedBarcode();
                                BarcodeServer.clearScannedBarcode();
                                for (Product product : inventoryManager.getProducts()) {
                                    if (product.getId().equals(scannedBarcode)) {
                                        tempProduct = product;
                                        continueOption = true;
                                        break;
                                    }
                                }
                                if (continueOption) {
                                    cartManager.addProductToCart(tempProduct);
                                    GlobalInterface.clearScreen();
                                    break;
                                } else {
                                    System.out.print(RESET + MOVE_UP + CLEAR_LINE); 
                                    GlobalInterface.centerString(ERROR_COMBO, "ERROR! PRODUCT NOT FOUND. PLEASE TRY AGAIN.");
                                    GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
                                    Thread.sleep(1000);
                                    GlobalInterface.clearScreen();
                                }
                            } else {
                                String optionChoice = response(" Enter The ID Or Name Of The Product Or Back To Go Back To Selection: ");
                                if (optionChoice.equals("BACK")) {
                                    GlobalInterface.clearScreen();
                                    break;
                                }

                                for (Product product : inventoryManager.getProducts()) {
                                    if (product.getName().toUpperCase().equals(optionChoice) || product.getId().equals(optionChoice)) {
                                        tempProduct = product;
                                        continueOption = true;
                                        break;
                                    }
                                }

                                if (continueOption) {
                                    cartManager.addProductToCart(tempProduct);
                                    GlobalInterface.clearScreen();
                                    break;
                                } else {
                                    System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                    GlobalInterface.centerString(ERROR_COMBO, "ERROR! PRODUCT NOT FOUND. PLEASE TRY AGAIN.");
                                    GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
                                    Thread.sleep(1000);
                                    GlobalInterface.clearScreen();
                                }
                            }
                        } while (true);
                    } else if (posSelection.equals("EDIT")) {
                        if (cartManager.getShoppingCart().isEmpty()) {
                            frameManager.titleFrame();
                            frameManager.posForm(employee, cartManager);
                            cartManager.display();
                            GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
                            GlobalInterface.centerString(MAYBE_COMBO, "WARNING! Cart Is Still Empty. Add Some Products First...");
                            GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
                            Thread.sleep(1000);
                            GlobalInterface.clearScreen();
                        } else {
                            if (BarcodeServer.getScannedBarcode() != null) {
                                frameManager.titleFrame();
                                frameManager.posForm(employee, cartManager);
                                cartManager.display();
                                GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
                                boolean continueOption = false;
                                Product tempProduct = null;
                                String scannedBarcode = BarcodeServer.getScannedBarcode();
                                BarcodeServer.clearScannedBarcode();

                                for (Product product : cartManager.getShoppingCart()) {
                                    if (product.getId().equals(scannedBarcode)) {
                                        continueOption = true;
                                        tempProduct = product;
                                        break;
                                    }
                                }

                                if (continueOption) {
                                    do {
                                        String newQuantityStr = response("Enter The To Be Added Quantity For " + tempProduct.getName() + ": ");
                                        try {
                                            int newQuantity = Integer.parseInt(newQuantityStr);
                                            cartManager.updateCartQuantity(tempProduct, newQuantity);
                                            GlobalInterface.clearScreen();
                                            break;
                                        } catch (NumberFormatException e) {
                                            System.out.print(RESET + MOVE_UP + CLEAR_LINE); 
                                            System.out.print(RESET + MOVE_UP + CLEAR_LINE); 
                                            GlobalInterface.centerString(MAYBE_COMBO, "WARNING! Invalid Quantity Entered. Please Try Again...");
                                            Thread.sleep(1000);
                                            System.out.print(RESET + MOVE_UP + CLEAR_LINE); 
                                        }
                                    } while (true);
                                } else {
                                    System.out.print(RESET + MOVE_UP + CLEAR_LINE); 
                                    GlobalInterface.centerString(ERROR_COMBO, "ERROR! PRODUCT NOT FOUND. PLEASE TRY AGAIN.");
                                    GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
                                    Thread.sleep(1000);
                                    GlobalInterface.clearScreen();
                                }
                            } else {
                                do {
                                    boolean continueOption = false;
                                    boolean endLoop = false;
                                    Product tempProduct = null;

                                    frameManager.titleFrame();
                                    frameManager.posForm(employee, cartManager);
                                    cartManager.display();
                                    GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
                                    String editChoice = response("Enter The ID Or Name Of The Product In The Cart Or Back To Go Back To Selection: ");
                                    if (editChoice.equals("BACK")) {
                                        GlobalInterface.clearScreen();
                                        break;
                                    }

                                    for (Product product : cartManager.getShoppingCart()) {
                                        if (product.getName().equalsIgnoreCase(editChoice) || product.getId().equals(editChoice)) {
                                            continueOption = true;
                                            tempProduct = product;
                                            break;
                                        }
                                    }

                                    if (continueOption) {
                                        do {
                                            String newQuantityStr = response("Enter The To Be Added Quantity For " + tempProduct.getName() + ": ");
                                            try {
                                                int newQuantity = Integer.parseInt(newQuantityStr);
                                                cartManager.updateCartQuantity(tempProduct, newQuantity);
                                                endLoop = true;
                                                break;
                                            } catch (NumberFormatException e) {
                                                GlobalInterface.centerString(MAYBE_COMBO, "WARNING! Invalid Quantity Entered. Please Try Again...");
                                            }
                                        } while (true);
                                    } else {
                                        System.out.print(RESET + MOVE_UP + CLEAR_LINE); 
                                        GlobalInterface.centerString(ERROR_COMBO, "ERROR! PRODUCT NOT FOUND. PLEASE TRY AGAIN.");
                                        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
                                        Thread.sleep(1000);
                                        GlobalInterface.clearScreen();
                                    }
                                    if (endLoop) {
                                        GlobalInterface.clearScreen();
                                        break;
                                    }
                                } while (true);
                            }
                        }
                    } else if (posSelection.equals("REMOVE")) {
                        if (cartManager.getShoppingCart().isEmpty()) {
                            frameManager.titleFrame();
                            frameManager.posForm(employee, cartManager);
                            cartManager.display();
                            GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
                            GlobalInterface.centerString(MAYBE_COMBO, "WARNING! Cart Is Still Empty. Add Some Products First...");
                            GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
                            Thread.sleep(1000);
                            GlobalInterface.clearScreen();
                        } else {
                            if (BarcodeServer.getScannedBarcode() != null) {
                                frameManager.titleFrame();
                                frameManager.posForm(employee, cartManager);
                                cartManager.display();
                                GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
                                String scannedBarcode = BarcodeServer.getScannedBarcode();
                                BarcodeServer.clearScannedBarcode();
                                for (Product product : inventoryManager.getProducts()) {
                                    if (product.getId().equals(scannedBarcode)) {
                                        cartManager.removeProductFromCart(product);
                                        GlobalInterface.clearScreen();
                                        break;
                                    } else {
                                        System.out.print(RESET + MOVE_UP + CLEAR_LINE); 
                                        GlobalInterface.centerString(ERROR_COMBO, "ERROR! PRODUCT NOT FOUND. PLEASE TRY AGAIN.");
                                        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
                                        Thread.sleep(1000);
                                        GlobalInterface.clearScreen();
                                    }
                                }
                            } else {
                                do {
                                    boolean endLoop = false;
                                    frameManager.titleFrame();
                                    frameManager.posForm(employee, cartManager);
                                    cartManager.display();
                                    GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
                                    String removeChoice = response("Enter The ID Or Name Of The Product In The Cart Or Back To Go Back To Selection: ");

                                    for (Product product : cartManager.getShoppingCart()) {
                                        if (product.getName().equalsIgnoreCase(removeChoice) || product.getId().equals(removeChoice)) {
                                            cartManager.removeProductFromCart(product);
                                            endLoop = true;
                                            break;
                                        } else {
                                            System.out.print(RESET + MOVE_UP + CLEAR_LINE); 
                                            GlobalInterface.centerString(ERROR_COMBO, "ERROR! PRODUCT NOT FOUND. PLEASE TRY AGAIN.");
                                            GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
                                            Thread.sleep(1000);
                                            GlobalInterface.clearScreen();
                                        }
                                    }
                                    if (endLoop) {
                                        GlobalInterface.clearScreen();
                                        break;
                                    }
                                } while (true);
                            }
                        }
                    } else if (posSelection.equals("CHECKOUT")) {
                        if (cartManager.getShoppingCart().isEmpty()) {
                            frameManager.titleFrame();
                            frameManager.posForm(employee, cartManager);
                            cartManager.display();
                            GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
                            GlobalInterface.centerString(MAYBE_COMBO, "WARNING! Cart Is Still Empty. Add Some Products First...");
                            GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
                            Thread.sleep(1000);
                            GlobalInterface.clearScreen();
                        } else {
                            do {
                                frameManager.titleFrame();
                                frameManager.posForm(employee, cartManager);
                                cartManager.display();
                                GlobalInterface.printHorizontalLine(SECONDARY_COMBO);

                                double totalAmount = 0;
                                
                                for (Product product : cartManager.getShoppingCart()) {
                                    totalAmount += product.getPrice();
                                }
                                
                                try {
                                    double payment = responseDouble(" Enter Payment: PHP ");
                                    if (payment >= totalAmount) {
                                        double change = payment - totalAmount;

                                        for (Product product : cartManager.getShoppingCart()) {
                                            int purchasedQuantity = product.getQuantity();
                                            inventoryManager.updateProductStock(product, purchasedQuantity);
                                        }
                                        System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                        GlobalInterface.centerString(PRIMARY_COMBO, "Checkout Successful! Total: PHP " + totalAmount);
                                        GlobalInterface.centerString(PRIMARY_COMBO, "PRINTING RECEIPT. PLEASE WAIT...");
                                        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
                                        PrintManager.print(cartManager.getShoppingCart(), employee, totalAmount, payment, change);
                                        cartManager.clearCart();
                                        Thread.sleep(2000);
                                        GlobalInterface.clearScreen();
                                        break;
                                    } else {
                                        GlobalInterface.centerString(WARNING_COMBO, "WARNING! Payment Is Insuffecient. Please Try Again.");
                                        Thread.sleep(2000);
                                        GlobalInterface.clearScreen();
                                        continue;
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                    GlobalInterface.centerString(WARNING_COMBO, "WARNING! Enter An Valid Amount. PLease Try Again.");
                                    Thread.sleep(1000);
                                    GlobalInterface.clearScreen();
                                    continue;
                                }
                                
                            } while (true);
                        }
                    } else if (posSelection.equals("EXIT")) {
                        Thread.sleep(2000);
                        GlobalInterface.clearScreen();
                        break;
                    }
                } while(true);
            } else if (employee instanceof Manager) {
                do {
                    frameManager.titleFrame();
                    frameManager.adminMenuFrame();
                    String adminChoice = response(" Which Option Would You Like To Do: ");
                    GlobalInterface.clearScreen();
                    if (adminChoice.equals("INVENTORY")) {
                        do {
                            frameManager.titleFrame();
                            frameManager.inventoryTitleFrame();
                            inventoryManager.display();
                            frameManager.inventoryOptionFrame();
                            String inventorySelection = response(" Which Option Would You Like To Do: ");
                            GlobalInterface.clearScreen();
                            if (inventorySelection.equals("ADD")) {
                                do {
                                    frameManager.inventoryAddForm(inventoryManager);
                                    boolean initBreak = false;
                                    String id = "";
                                    String name = "";
                                    double price = 0.0;
                                    int stock = 0;
                                    int threshold = 0;
                                    Product checkProduct = null;

                                    //ID
                                    id = responseSecondary("ID: ", 178);

                                    if (id.equalsIgnoreCase("CANCEL")) {
                                        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        Thread.sleep(2000);
                                        GlobalInterface.clearScreen();
                                        break;
                                    }
                                    System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                    GlobalInterface.centerString(SECONDARY_COMBO, "ID: " + GlobalInterface.putSpaceGaps(40, id));
                                    checkProduct = inventoryManager.findProductID(id);
                                    if (checkProduct != null) {
                                        GlobalInterface.centerString(WRONG_COMBO, "PRODUCT ALREADY EXISTS. PLEASE TRY AGAIN...");
                                        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        Thread.sleep(2000);
                                        GlobalInterface.clearScreen();
                                        checkProduct = null;
                                        continue;
                                    }
                                    //NAME
                                    System.out.print(GlobalInterface.printPaddingLeft(SECONDARY_COMBO, width - 174) + "NAME: ");
                                    name = input.nextLine();

                                    if (name.equalsIgnoreCase("CANCEL")) {
                                        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        Thread.sleep(2000);
                                        GlobalInterface.clearScreen();
                                        break;
                                    }

                                    System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                    GlobalInterface.centerString(SECONDARY_COMBO, "NAME: " + GlobalInterface.putSpaceGaps(42, name));
                                    checkProduct = inventoryManager.findProductName(name);
                                    if (checkProduct != null) {
                                        GlobalInterface.centerString(WRONG_COMBO, "PRODUCT ALREADY EXISTS. PLEASE TRY AGAIN...");
                                        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        Thread.sleep(2000);
                                        checkProduct = null;
                                        GlobalInterface.clearScreen();
                                        continue;
                                    }
                                    //PRICE
                                    do {
                                        try {
                                            System.out.print(GlobalInterface.printPaddingLeft(SECONDARY_COMBO, width - 172) + "PRICE: PHP ");
                                            String tempValue = input.nextLine();

                                            if (tempValue.equalsIgnoreCase("CANCEL")) {
                                                initBreak = true;
                                                break;
                                            }
                                            price = Double.parseDouble(tempValue);

                                            if (price <= 0) {
                                                System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                                GlobalInterface.centerString(MAYBE_COMBO,"WARNING! Don't Enter A Negative Amount. Please Try Again...");
                                                Thread.sleep(1000);
                                                System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                                continue;
                                            }
                                            System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                            GlobalInterface.centerString(SECONDARY_COMBO, "PRICE: " + GlobalInterface.putSpaceGaps(44, ("PHP "+ price)));
                                            break;
                                        } catch (NumberFormatException e) {
                                            System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                            GlobalInterface.centerString(MAYBE_COMBO, "WARNING! PLEASE ENTER THE RIGHT VALUE. PLEASE TRY AGAIN...");
                                            Thread.sleep(1000);
                                            System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                        }
                                    } while (true);
                                    if (initBreak) {
                                        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        Thread.sleep(2000);
                                        GlobalInterface.clearScreen();
                                        break;
                                    }
                                    //STOCK
                                    do {
                                        try {
                                            System.out.print(GlobalInterface.printPaddingLeft(SECONDARY_COMBO, width - 172) + "STOCK: ");
                                            String tempValue = input.nextLine();

                                            if (tempValue.equalsIgnoreCase("CANCEL")) {
                                                initBreak = true;
                                                break;
                                            }
                                            stock = Integer.parseInt(tempValue);

                                            if (stock <= 0) {
                                                System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                                GlobalInterface.centerString(MAYBE_COMBO,"WARNING! Don't Enter A Negative Amount. Please Try Again...");
                                                Thread.sleep(1000);
                                                System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                                continue;
                                            }
                                            System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                            GlobalInterface.centerString(SECONDARY_COMBO, "STOCK: " + GlobalInterface.putSpaceGaps(44, String.valueOf(stock)));
                                            break;
                                        } catch (NumberFormatException e) {
                                            System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                            GlobalInterface.centerString(MAYBE_COMBO, "WARNING! PLEASE ENTER THE RIGHT VALUE. PLEASE TRY AGAIN...");
                                            Thread.sleep(1000);
                                            System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                        }
                                    } while (true);
                                    if (initBreak) {
                                        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        Thread.sleep(2000);
                                        GlobalInterface.clearScreen();
                                        break;
                                    }
                                    //LOW THRESHOLD
                                    do {
                                        try {
                                            System.out.print(GlobalInterface.printPaddingLeft(SECONDARY_COMBO, width - 156) + "LOW THRESHOLD: ");
                                            String tempValue = input.nextLine();

                                            if (tempValue.equalsIgnoreCase("CANCEL")) {
                                                initBreak = true;
                                                break;
                                            }
                                            threshold = Integer.parseInt(tempValue);

                                            if (threshold <= 0) {
                                                System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                                GlobalInterface.centerString(MAYBE_COMBO,"WARNING! Don't Enter A Negative Amount. Please Try Again...");
                                                Thread.sleep(1000);
                                                System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                                continue;
                                            }
                                            System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                            GlobalInterface.centerString(SECONDARY_COMBO, "LOW THRESHOLD: " + GlobalInterface.putSpaceGaps(52, String.valueOf(threshold)));
                                            break;
                                        } catch (NumberFormatException e) {
                                            System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                            GlobalInterface.centerString(MAYBE_COMBO, "WARNING! PLEASE ENTER THE RIGHT VALUE. PLEASE TRY AGAIN...");
                                            Thread.sleep(1000);
                                            System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                        }
                                    } while (true);
                                    if (initBreak) {
                                        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        Thread.sleep(2000);
                                        GlobalInterface.clearScreen();
                                        break;
                                    }
                                    inventoryManager.addProduct(new Product(id, name, stock, price, threshold));
                                    GlobalInterface.centerString(CORRECT_COMBO, "SUCCESSFULLY ADDED PRODUCT { " + inventoryManager.getProducts().get(inventoryManager.getProducts().size() - 1).getName() + " }");
                                    GlobalInterface.centerString(CORRECT_COMBO, "GOING BACK TO INVENTORY SELECTION. PLEASE WAIT...");
                                    GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                    GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                    GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                    GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                    GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                    Thread.sleep(2000);
                                    GlobalInterface.clearScreen();
                                    break;
                                } while (true);
                            } else if (inventorySelection.equals("DELETE")) {
                                boolean errorEncounter = false;
                                do {
                                    boolean continueOption = false;
                                    Product tempProduct = null;
                                    frameManager.titleFrame();
                                    frameManager.inventoryTitleFrame();
                                    inventoryManager.display();
                                    if (errorEncounter) {
                                        GlobalInterface.centerString(MAYBE_COMBO, "WARNING! Please Enter The Correct Product. Please Try Again...");
                                        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
                                        errorEncounter = false;
                                    }
                                    String optionChoice = response(" Enter The ID Or Name Of The Product Or Back To Go Back To Selection: ");
                                    GlobalInterface.clearScreen();
                                    if (optionChoice.equals("BACK")) {
                                        GlobalInterface.clearScreen();
                                        break;
                                    }

                                    for (Product product : inventoryManager.getProducts()) {
                                        if (product.getName().toUpperCase().equals(optionChoice) || product.getId().equals(optionChoice)) {
                                            tempProduct = product;
                                            continueOption = true;
                                        }
                                    }
                                    if (continueOption) {
                                        frameManager.titleFrame();
                                        inventoryManager.display();
                                        frameManager.inventoryDeleteFrom(tempProduct);
                                        String optionSelection = response(" Which Option Would You Like To Do: ");
                                        if (optionSelection.equals("DELETE")) {
                                            System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                            GlobalInterface.centerString(CORRECT_COMBO, "Successfully Deleted The Product { " + tempProduct.getName() + " }");
                                            inventoryManager.removeProduct(tempProduct);
                                            GlobalInterface.centerString(CORRECT_COMBO, "Going Back To Selection. Please Wait A Moment");
                                            GlobalInterface.printHorizontalLine(SECONDARY_COMBO);

                                            Thread.sleep(2000);
                                            GlobalInterface.clearScreen();
                                            break;
                                        } else if (optionSelection.equals("BACK")) {
                                            Thread.sleep(2000);
                                            GlobalInterface.clearScreen();
                                            break;
                                        }
                                    } else {
                                        errorEncounter = true;
                                        continue;
                                    }
                                } while (true);
                            } else if (inventorySelection.equals("EDIT")) {
                                boolean errorEncounter = false;
                                do {
                                    boolean continueOption = false;
                                    Product tempProduct = null;
                                    frameManager.titleFrame();
                                    frameManager.inventoryTitleFrame();
                                    inventoryManager.display();
                                    if (errorEncounter) {
                                        GlobalInterface.centerString(MAYBE_COMBO, "WARNING! Please Enter The Correct Product. Please Try Again...");
                                        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
                                        errorEncounter = false;
                                    }

                                    String optionChoice = response(" Enter The ID Or Name Of The Product Or Back To Go Back To Selection: ");
                                    GlobalInterface.clearScreen();
                                    if (optionChoice.equals("BACK")) {
                                        GlobalInterface.clearScreen();
                                        break;
                                    }

                                    for (Product product : inventoryManager.getProducts()) {
                                        if (product.getName().toUpperCase().equals(optionChoice) || product.getId().equals(optionChoice)) {
                                            tempProduct = product;
                                            continueOption = true;
                                        }
                                    }
                                    String changeMessage = "";
                                    if (continueOption) {
                                        do {
                                            frameManager.titleFrame();
                                            inventoryManager.display();
                                            frameManager.inventoryEditForm(tempProduct, changeMessage);
                                            String optionSelection = response(" Which Option Would You Like To Do: ");
                                            GlobalInterface.clearScreen();
                                            if (optionSelection.equals("ID")) {
                                                do {
                                                    String newValue = responseDefault( " Enter The New Value Of The ID { " + tempProduct.getId() +" } Or Leave Blank To Not Change: ").trim();
                                                    if (newValue.isBlank()) {
                                                        GlobalInterface.clearScreen();
                                                        break;
                                                    }
                                                    Product checkProduct = inventoryManager.findProductID(newValue);
                                                    if (checkProduct != null) {
                                                        GlobalInterface.centerString(WRONG_COMBO, "PRODUCT ID ALREADY EXISTS. PLEASE TRY AGAIN...");
                                                        Thread.sleep(2000);                                                    
                                                        continue;
                                                    }
                                                    changeMessage = changeMessage(tempProduct, "ID", tempProduct.getId(), newValue);
                                                    inventoryManager.updateProductID(tempProduct, newValue);
                                                    Thread.sleep(2000);
                                                    GlobalInterface.clearScreen();   
                                                    break;
                                                } while (true);
                                            } else if (optionSelection.equals("NAME")) {
                                                do {
                                                    String newValue = responseDefault( " Enter The New Value Of The Name { " + tempProduct.getName() +" } Or Leave Blank To Not Change: ").trim();
                                                    if (newValue.isBlank()) {
                                                        GlobalInterface.clearScreen();
                                                        break;
                                                    }
                                                    Product checkProduct = inventoryManager.findProductID(newValue);
                                                    if (checkProduct != null) {
                                                        GlobalInterface.centerString(WRONG_COMBO, "PRODUCT NAME ALREADY EXISTS. PLEASE TRY AGAIN...");
                                                        Thread.sleep(2000);                                                  
                                                        continue;
                                                    }
                                                    changeMessage = changeMessage(tempProduct, "Name", tempProduct.getName(), newValue);
                                                    inventoryManager.updateProductName(tempProduct, newValue);
                                                    Thread.sleep(2000);
                                                    GlobalInterface.clearScreen();   
                                                    break;
                                                } while (true);
                                            } else if (optionSelection.equals("STOCK")) {
                                                do {
                                                    try {
                                                        String newValue = responseDefault( " Enter The New Value Of The Stock { " + tempProduct.getQuantity() +" } Or Leave Blank To Not Change: ").trim();

                                                        if (newValue.isBlank()) {
                                                            GlobalInterface.clearScreen();
                                                            break;
                                                        }

                                                        int newStock = Integer.parseInt(newValue);

                                                        if (newStock <= 0) {
                                                            GlobalInterface.centerString(MAYBE_COMBO,"WARNING! Don't Enter A Negative Amount. Please Try Again...");
                                                            Thread.sleep(2000);
                                                            continue;
                                                        }

                                                        changeMessage = changeMessage(tempProduct, "Stock", String.valueOf(tempProduct.getQuantity()), String.valueOf(newStock));
                                                        inventoryManager.updateProductStock(tempProduct, newStock);
                                                        Thread.sleep(2000);
                                                        GlobalInterface.clearScreen();  
                                                        break;
                                                    } catch (NumberFormatException e) {
                                                        GlobalInterface.centerString(MAYBE_COMBO,"WARNING! Please Enter An Number. Please Try Again...");
                                                        Thread.sleep(2000);
                                                    }
                                                } while (true);
                                            } else if (optionSelection.equals("PRICE")) {
                                                do {
                                                    try {
                                                        String newValue = responseDefault( " Enter The New Value Of The Price { PHP " + tempProduct.getPrice() +" } Or Leave Blank To Not Change: ").trim();

                                                        if (newValue.isBlank()) {
                                                            GlobalInterface.clearScreen();
                                                            break;
                                                        }

                                                        double newPrice = Double.parseDouble(newValue);

                                                        if (newPrice <= 0) {
                                                            GlobalInterface.centerString(MAYBE_COMBO,"WARNING! Don't Enter A Negative Amount. Please Try Again...");
                                                            Thread.sleep(2000);
                                                            continue;
                                                        }
                                                        changeMessage = changeMessage(tempProduct, "Price", String.valueOf(tempProduct.getPrice()), String.valueOf(newPrice));
                                                        inventoryManager.updateProductPrice(tempProduct, newPrice);
                                                        Thread.sleep(2000);
                                                        GlobalInterface.clearScreen();  
                                                        break;
                                                    } catch (NumberFormatException e) {
                                                        GlobalInterface.centerString(MAYBE_COMBO,"WARNING! Please Enter An Number. Please Try Again...");
                                                        Thread.sleep(2000);
                                                    }
                                                } while (true);
                                            } else if (optionSelection.equals("THRESHOLD")) {
                                                do {
                                                    try {
                                                        String newValue = responseDefault( " Enter The New Value Of The Low Threshold { " + tempProduct.getLowThreshold() +" } Or Leave Blank To Not Change: ").trim();

                                                        if (newValue.isBlank()) {
                                                            GlobalInterface.clearScreen();
                                                            break;
                                                        }

                                                        int newThreshold = Integer.parseInt(newValue);

                                                        if (newThreshold <= 0) {
                                                            GlobalInterface.centerString(MAYBE_COMBO,"WARNING! Don't Enter A Negative Amount. Please Try Again...");
                                                            Thread.sleep(2000);
                                                            continue;
                                                        }
                                                        changeMessage = changeMessage(tempProduct, "Low Threshold", String.valueOf(tempProduct.getLowThreshold()), String.valueOf(newThreshold));
                                                        inventoryManager.updateProductThreshold(tempProduct, newThreshold);
                                                        Thread.sleep(2000);
                                                        GlobalInterface.clearScreen();  
                                                        break;
                                                    } catch (NumberFormatException e) {
                                                        GlobalInterface.centerString(MAYBE_COMBO,"WARNING! Please Enter An Number. Please Try Again...");
                                                        Thread.sleep(2000);
                                                    }
                                                } while (true);
                                            } else if (optionSelection.equals("CHANGE")) {
                                                GlobalInterface.clearScreen();
                                                break;
                                            }
                                        } while (true);
                                    } else {
                                        errorEncounter = true;
                                        continue;
                                    }
                                } while (true);
                            } else if (inventorySelection.equals("BACK")) {
                                break;
                            }
                        } while (true);
                    } else if (adminChoice.equals("SALES")) {
                        System.out.println();
                    } else if (adminChoice.equals("ACCOUNTS")){
                        do {
                            frameManager.titleFrame();
                            employeeManager.display();
                            frameManager.accountsOptionFrame();
                            String accountSelection = response(" Which Option Would You Like To Do: ");
                            GlobalInterface.clearScreen();
                            if (accountSelection.equals("ADD")) {
                                do {
                                    frameManager.titleFrame();
                                    employeeManager.display();
                                    frameManager.accountAddForm();
                                    boolean initBreak = false;
                                    String accountType = "";
                                    String name = "";
                                    String username = "";
                                    String password = "";
                                    Employee checkAccount = null;
                                    //ACCOUNT TYPE
                                    do {
                                        accountType = responseSecondary("ACCOUNT TYPE (C - FOR CASHIER/ M - FOR MANAGER): ", 88);
                                        if (accountType.equalsIgnoreCase("M") || accountType.equalsIgnoreCase("C")) {
                                            System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                            GlobalInterface.centerString(SECONDARY_COMBO, "ACCOUNT TYPE: " + GlobalInterface.putSpaceGaps(50, accountType));
                                            break;
                                        } else if (accountType.equalsIgnoreCase("CANCEL")) {
                                            initBreak = true;
                                            break;
                                        } else {
                                            System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                            GlobalInterface.centerString(MAYBE_COMBO, "PLEASE ENTER THE RIGHT VALUE. PLEASE TRY AGAIN...");
                                            Thread.sleep(1000);
                                            System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                            continue;
                                        }
                                    } while (true);

                                    if (initBreak) {
                                        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        Thread.sleep(2000);
                                        GlobalInterface.clearScreen();
                                        break;
                                    }

                                    //NAME
                                    System.out.print(GlobalInterface.printPaddingLeft(SECONDARY_COMBO, width - 174) + "NAME: ");
                                    name = input.nextLine();

                                    if (name.equalsIgnoreCase("CANCEL")) {
                                        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        Thread.sleep(2000);
                                        GlobalInterface.clearScreen();
                                        break;
                                    }
                                    System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                    GlobalInterface.centerString(SECONDARY_COMBO, "NAME: " + GlobalInterface.putSpaceGaps(42, name));
                                    //USERNAME
                                    System.out.print(GlobalInterface.printPaddingLeft(SECONDARY_COMBO, width - 166) + "USERNAME: ");
                                    username = input.nextLine();

                                    if (username.equalsIgnoreCase("CANCEL")) {
                                        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        Thread.sleep(2000);
                                        GlobalInterface.clearScreen();
                                        break;
                                    }
                                    
                                    System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                    GlobalInterface.centerString(SECONDARY_COMBO, "USERNAME: " + GlobalInterface.putSpaceGaps(46, username));
                                    checkAccount = employeeManager.findAccountUsername(username);
                                    if (checkAccount != null) {
                                        GlobalInterface.centerString(WRONG_COMBO, "ACCOUNT ALREADY EXISTS. PLEASE TRY AGAIN...");
                                        checkAccount = null;
                                        continue;
                                    }

                                    //PASSWORD
                                    do {
                                        System.out.print(GlobalInterface.printPaddingLeft(SECONDARY_COMBO, width - 166) + "PASSWORD: ");
                                        password = input.nextLine();

                                        if (password.equalsIgnoreCase("CANCEL")) {
                                            initBreak = true;
                                            break;
                                        }

                                        System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                        GlobalInterface.centerString(SECONDARY_COMBO, "PASSWORD: " + GlobalInterface.putSpaceGaps(46, password));

                                        System.out.print(GlobalInterface.printPaddingLeft(SECONDARY_COMBO, width - 150) + "CONFIRM PASSWORD: ");
                                        String c_password = input.nextLine();
                                        if (c_password.equalsIgnoreCase("CANCEL")) {
                                            initBreak = true;
                                            break;
                                        }

                                        System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                        GlobalInterface.centerString(SECONDARY_COMBO, "CONFIRM PASSWORD: " + GlobalInterface.putSpaceGaps(54, c_password));

                                        if (password.equals(c_password)) {
                                            break;
                                        } else {
                                            System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                            System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                            GlobalInterface.centerString(WRONG_COMBO, "PASSWORD DOES NOT MATCH. PLEASE TRY AGAIN...");
                                            Thread.sleep(1000);
                                            System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                        }
                                    } while (true);
                                    if (initBreak) {
                                        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                        GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                        Thread.sleep(2000);
                                        GlobalInterface.clearScreen();
                                        break;
                                    }

                                    if (accountType.equals("M")) {
                                        employeeManager.addEmployee(new Manager(name, username, password));
                                    } else if (accountType.equals("C")) {
                                        employeeManager.addEmployee(new Cashier(name, username, password));
                                    }
                                    GlobalInterface.centerString(CORRECT_COMBO, "SUCCESSFULLY ADDED ACCOUNT { " +  employeeManager.getEmployees().get(employeeManager.getEmployees().size() - 1).getName() + " }");
                                    GlobalInterface.centerString(CORRECT_COMBO, "GOING BACK TO ACCOUNT SELECTION. PLEASE WAIT...");
                                    GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                    GlobalInterface.printBackgroundColor(SECONDARY_COMBO);
                                    GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                    GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                    GlobalInterface.printBackgroundColor(PRIMARY_COMBO);
                                    Thread.sleep(2000);
                                    GlobalInterface.clearScreen();
                                    break;
                                } while (true);
                            } else if (accountSelection.equals("DELETE")) {
                                boolean errorEncounter = false;
                                do {
                                    boolean initBreak = false;
                                    boolean continueOption = false;
                                    Employee tempAccount = null;
                                    frameManager.titleFrame();
                                    employeeManager.display();
                                    if (errorEncounter) {
                                        GlobalInterface.centerString(WARNING_COMBO, "WARNING! Please Enter The Correct Account Username. Please Try Again...");
                                        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
                                        errorEncounter = false;
                                    }
                                    String optionChoice = response(" Enter The Username Of The Account Or Back To Go Back To Selection: ");
                                    GlobalInterface.clearScreen();
                                    if (optionChoice.equals("BACK")) {
                                        GlobalInterface.clearScreen();
                                        break;
                                    }

                                    for (Employee account : employeeManager.getEmployees()) {
                                        if (account.getUsername().toUpperCase().equals(optionChoice)) {
                                            tempAccount = account;
                                            continueOption = true;
                                        }
                                    }
                                    if (continueOption) {
                                        do {
                                            frameManager.titleFrame();
                                            employeeManager.display();
                                            frameManager.accountDeleteFrom(tempAccount);
                                            String optionSelection = response(" Which Option Would You Like To Do: ");
                                            if (optionSelection.equals("DELETE")) {
                                                System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                                                GlobalInterface.centerString(CORRECT_COMBO, "Successfully Deleted The Product { " + tempAccount.getName() + " : " + tempAccount.getUsername() + " }");
                                                employeeManager.removeEmployee(tempAccount);
                                                GlobalInterface.centerString(CORRECT_COMBO, "Going Back To Selection. Please Wait A Moment");
                                                GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
                                                Thread.sleep(2000);
                                                GlobalInterface.clearScreen();
                                                initBreak = true;
                                                break;
                                            } else if (optionSelection.equals("BACK")) {
                                                Thread.sleep(1000);
                                                GlobalInterface.clearScreen();
                                                initBreak = true;
                                                break;
                                            }
                                        } while (true);
                                        if (initBreak) {
                                            break;
                                        }
                                    } else {
                                        errorEncounter = true;
                                        continue;
                                    }
                                } while (true);
                            } else if (accountSelection.equals("BACK")) {
                                GlobalInterface.clearScreen();
                                break;
                            }
                        } while (true);
                    } else if (adminChoice.equals("PRINTER")) {
                        boolean errorEncounter = false;
                        boolean trueForm = false;
                        do{
                            frameManager.titleFrame();
                            frameManager.printerFrame();
                            if (errorEncounter) {
                                GlobalInterface.centerString(WARNING_COMBO, " WARNING! Please Input The Right Code. Please Try Again...");
                                GlobalInterface.printHorizontalLine(PRIMARY_COMBO);
                                errorEncounter = false;
                            }
                            if (trueForm) {
                                GlobalInterface.centerString(PRIMARY_COMBO, " Successfully Changed The Printer.");
                                GlobalInterface.printHorizontalLine(PRIMARY_COMBO);
                                trueForm = false;
                                errorEncounter = false;
                            }
                            String printerChoice = response(" Type In The Number of A Printer To Change Current Printer Or Type In Back To Go Back To Admin Page: ");

                            if (printerChoice.equals("BACK")) {
                                GlobalInterface.clearScreen();
                                break;
                            }
                            try {
                                int printerCode = (Integer.parseInt(printerChoice)) - 1;
                                if (printerCode >= 0 && printerCode < PrintManager.printServices.length) {
                                    PrintManager.printer = printerCode;
                                    FileManager.savePrinter(printerCode);
                                    errorEncounter = false;
                                    trueForm = true;
                                    GlobalInterface.clearScreen();
                                } else {
                                    GlobalInterface.clearScreen();
                                    throw new NumberFormatException();
                                }
                            } catch (NumberFormatException e) {
                                errorEncounter = true; 
                                GlobalInterface.clearScreen();
                            }
                        } while (true);
                    } else if (adminChoice.equals("LOGOUT")) {
                        Thread.sleep(2000);
                        GlobalInterface.clearScreen();
                        break;
                    }
                } while (true);
            }
        }
    }
    private static String response(String text) {
        Scanner input = new Scanner(System.in);
        System.out.print(RESPONSE_COMBO + text + FontManager.RESET);
        String response = input.nextLine().toUpperCase();
        System.out.println(MOVE_UP + CLEAR_LINE + RESPONSE_COMBO + text + response + " ".repeat(width - (text.length() + response.length())) + RESET);
        return response;
    }
    private static double responseDouble(String text) {
        Scanner input = new Scanner(System.in);
        System.out.print(RESPONSE_COMBO + text + FontManager.RESET);
        double response = input.nextDouble();
        System.out.println(MOVE_UP + CLEAR_LINE + RESPONSE_COMBO + text + response + " ".repeat(width - (text.length() + String.valueOf(response).length())) + RESET);
        return response;
    }
    private static String responseSecondary(String text, int counter) {
        Scanner input = new Scanner(System.in);
        System.out.print(GlobalInterface.printPaddingLeft(SECONDARY_COMBO, width - counter) + text);
        String response = input.nextLine().toUpperCase();
        System.out.println(MOVE_UP + CLEAR_LINE + SECONDARY_COMBO + text + response + " ".repeat(width - (text.length() + response.length())) + RESET);
        return response;
    }
    private static String responseDefault(String text) {
        Scanner input = new Scanner(System.in);
        System.out.print(RESPONSE_COMBO + text + FontManager.RESET);
        String response = input.nextLine().trim();
        System.out.println(MOVE_UP + CLEAR_LINE + RESPONSE_COMBO + text + response + " ".repeat(width - (text.length() + response.length())) + RESET);
        return response;
    }
    private static String changeMessage(Product product, String category, String oldValue, String newValue) {
        if (category.equals("Price")) {
            return String.format("The %s Of The Product { %s } Which Has The Old Value Of { PHP %s } Is Changed To %s", category, product.getName(), oldValue, newValue);
        } else {
            return String.format("The %s Of The Product { %s } Which Has The Old Value Of { %s } Is Changed To %s", category, product.getName(), oldValue, newValue);
        }
    }
    private static void initializeInventory(InventoryManager inventory) throws IOException, InterruptedException {
        inventory.addProduct(new Product("4800361425308", "KokoKrunchChocoRingz", 50, 10.00, 15));
        inventory.addProduct(new Product("4800361413480", "Milo", 20, 8.00, 30));
        inventory.addProduct(new Product("4807770272554", "LuckyMePancitCantonSweet&Spicy", 0, 12.00, 30));
        FileManager.saveInventory(inventory.getProducts());
    }
    private static void initializeEmployee(EmployeeManager employeeManager) throws IOException {
        employeeManager.addEmployee(new Manager("Admin", "admin", "admin123"));
        employeeManager.addEmployee(new Cashier("Todd", "todd@cashier", "cash99"));
        FileManager.saveEmployees(employeeManager.getEmployees());
    }
}
