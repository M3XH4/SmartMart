import java.io.Serial;
import java.io.Serializable;

public class Employee implements GlobalInterface, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String username;
    private String password;

    public Employee(String name, String username, String password) {
        setName(name);
        setUsername(username);
        setPassword(password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
class Manager extends Employee {
    public Manager(String name, String username, String password) {
        super(name, username, password);
    }
}
class Cashier extends Employee {
    public Cashier(String name, String username, String password) {
        super(name, username, password);
    }
}