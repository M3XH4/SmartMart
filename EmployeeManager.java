import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class EmployeeManager implements GlobalInterface, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private ArrayList<Employee> employees;

    public EmployeeManager() {
        setEmployees(new ArrayList<>());
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<Employee> employees) {
        this.employees = employees;
    }

    public void addEmployee(Employee employee) throws IOException {
        if (employee instanceof Manager manager) {
            getEmployees().add(manager);
        } else if (employee instanceof Cashier cashier) {
            getEmployees().add(cashier);
        }
        FileManager.saveEmployees(getEmployees());
    }
    public void removeEmployee(Employee account) throws IOException {
        getEmployees().remove(account);
        FileManager.saveEmployees(getEmployees());
    }
    public Employee findAccountName(String name) {
        for (Employee account: getEmployees()) {
            if (account.getName().equalsIgnoreCase(name)) {
                return account;
            }
        }
        return null;
    }
    public Employee findAccountUsername(String username) {
        for (Employee account: getEmployees()) {
            if (account.getUsername().equalsIgnoreCase(username)) {
                return account;
            }
        }
        return null;
    }
    public ArrayList<Manager> getManagers() {
        ArrayList<Manager> tempManagers = new ArrayList<>();
        for (Employee employee : getEmployees()) {
            if (employee instanceof Manager manager) {
                tempManagers.add(manager);
            }
        }
        return tempManagers;
    }

    public ArrayList<Cashier> getCashiers() {
        ArrayList<Cashier> tempCashiers = new ArrayList<>();
        for (Employee employee : getEmployees()) {
            if (employee instanceof Cashier cashier) {
                tempCashiers.add(cashier);
            }
        }
        return tempCashiers;
    }

    public void display() {
        int namePercentage = 20;
        int usernamePercentage = 40;
        int passwordPercentage = 40;

        int nameWidth = (width * namePercentage / 100) + 1;
        int usernameWidth = (width * usernamePercentage / 100) - 5;
        int passwordWidth = (width * passwordPercentage / 100) - 5;
        String header = String.format(
                SECONDARY_COMBO + "| %-" + nameWidth + "s | %-" + usernameWidth + "s | %-" + passwordWidth + "s |" + RESET,
                "Name", "Username", "Password"
        );

        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
        System.out.println(header);
        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
        for (Employee employee : getEmployees()) {
            System.out.println(String.format(
                    SECONDARY_COMBO + "| %-" + nameWidth + "s | %-" + usernameWidth + "s | %-" + passwordWidth + "s |" + RESET,
                    employee.getName(), employee.getUsername(), employee.getPassword().replaceAll(".", "*")
            ));
        }
        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
    }
}
