import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileManager {
    private static final String inventoryFilePath = getSourcePath() + "\\inventory.dat";
    private static final String employeeFilePath = getSourcePath() + "\\employee.dat";
    private static final String printerFilePath = getSourcePath() + "\\printer.txt";

    private static String getSourcePath() {
        Path currentPath = Paths.get("");
        Path srcPath = currentPath.resolve("src");
        Path binPath = srcPath.resolve("bin");
        if (!Files.exists(binPath)) {
            try {
                Files.createDirectories(binPath);
            } catch (IOException e) {
                System.out.print(FontManager.ERROR_COMBO + "ERROR! Cannot Create Bin Folder...");
            }
        }
        return binPath.toAbsolutePath().toString();
    }

    public static void createFile() throws IOException {
        File inventoryFile = new File(inventoryFilePath);
        File employeeFile = new File(employeeFilePath);
        File printerFile = new File(printerFilePath);

        if (!inventoryFile.exists()) {
            inventoryFile.createNewFile();
        }

        if (!employeeFile.exists()) {
            employeeFile.createNewFile();
        }
        if (!printerFile.exists()) {
            printerFile.createNewFile();
        }

    }

    public static void savePrinter(int printer) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(printerFilePath));
        writer.write("Printer: " + printer);
        writer.close();
    }

    public static int loadPrinter() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(printerFilePath));
        String line;
        int tempPrinterCode = 0;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("Printer: ")) {
                String printerCode = line.substring(9);
                tempPrinterCode = Integer.parseInt(printerCode);
            }
        }
        return tempPrinterCode;
    }

    public static void saveInventory(ArrayList<Product> products) throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(inventoryFilePath));
        outputStream.writeObject(products);
        outputStream.close();
    }

    public static ArrayList<Product> loadInventory() {
        ArrayList<Product> tempProducts;

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(inventoryFilePath))) {
            tempProducts = (ArrayList<Product>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
        return tempProducts;
    }

    public static void saveEmployees(ArrayList<Employee> employees) throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(employeeFilePath));
        outputStream.writeObject(employees);
        outputStream.close();
    }

    public static ArrayList<Employee> loadEmployees() {
        ArrayList<Employee> tempEmployees;

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(employeeFilePath))) {
            tempEmployees = (ArrayList<Employee>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
        return tempEmployees;
    }
}
