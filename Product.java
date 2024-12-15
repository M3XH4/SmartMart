import java.io.Serial;
import java.io.Serializable;

public class Product implements Serializable, FontManager {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private int quantity;
    private double price;
    private double marketPrice;
    private int lowThreshold;

    public enum Status {
        WELL_STOCKED("Well-Stocked", PERFECT_COMBO),
        STOCK_RUNNING_LOW("Stock Running Low", WARNING_COMBO),
        OUT_OF_STOCK("Out of Stock", ERROR_COMBO);

        private final String message;
        private final String colorScheme;

        Status(String message, String colorScheme) {
            this.message = message;
            this.colorScheme = colorScheme;
        }

        public String getMessage() {
            return this.message;
        }

        public String getColorScheme() {
            return this.colorScheme;
        }
    }

    public Product(String id, String name, int quantity, double price, int lowThreshold) {
        setId(id);
        setName(name);
        setQuantity(quantity);
        setPrice(price);
        setLowThreshold(lowThreshold);
    }

    public Product(Product product) {
        setId(product.getId());
        setName(product.getName());
        setQuantity(product.getQuantity());
        setPrice(product.getPrice());
        setLowThreshold(product.getLowThreshold());
    }

    public Status getStockStatus() {
        if (quantity <= 0) {
            return Status.OUT_OF_STOCK;
        } else if (quantity <= lowThreshold) {
            return Status.STOCK_RUNNING_LOW;
        } else {
            return Status.WELL_STOCKED;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getLowThreshold() {
        return lowThreshold;
    }
    public void setLowThreshold(int lowThreshold) {
        this.lowThreshold = lowThreshold;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Product product = (Product) obj;
        return id.equals(product.id); // Compare based on unique ID
    }

    @Override
    public int hashCode() {
        return id.hashCode(); // Generate hash based on unique ID
    }
}

