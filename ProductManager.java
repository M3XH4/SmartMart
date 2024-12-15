import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public abstract class ProductManager implements Serializable, GlobalInterface {
    @Serial
    private static final long serialVersionUID = 1L;

    private ArrayList<Product> products;

    public ProductManager() {
        setProducts(new ArrayList<>());
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) throws IOException {
        getProducts().add(product);
        FileManager.saveInventory(getProducts());
    }
    public void removeProduct(Product product) throws IOException {
        getProducts().remove(product);
        FileManager.saveInventory(getProducts());
    }

    public void updateProductID(Product product, String newValue) throws IOException {
        String tempID = (newValue.isBlank()) ? product.getId() : newValue;
        product.setId(tempID);
        FileManager.saveInventory(getProducts());
    }
    public void updateProductName(Product product, String newValue) throws IOException {
        String tempName = (newValue.isBlank()) ? product.getName() : newValue;
        product.setName(tempName);
        FileManager.saveInventory(getProducts());
    }
    public void updateProductPrice(Product product, double newValue) throws IOException {
        double tempPrice = (newValue < 0) ? product.getPrice() : newValue;
        product.setPrice(tempPrice);
        FileManager.saveInventory(getProducts());
    }
    public void updateProductStock(Product product, int newValue) throws IOException {
        int tempStock = (newValue < 0) ? product.getQuantity() : newValue;
        product.setQuantity(tempStock);
        FileManager.saveInventory(getProducts());
    }
    public void updateProductThreshold(Product product, int newValue) throws IOException {
        int tempThreshold = (newValue < 0) ? product.getLowThreshold() : newValue;
        product.setLowThreshold(tempThreshold);
        FileManager.saveInventory(getProducts());
    }
    public Product findProductName(String name) {
        for (Product product: getProducts()) {
            if (product.getName().equalsIgnoreCase(name)) {
                return product;
            }
        }
        return null;
    }
    public Product findProductID(String id) {
        for (Product product: getProducts()) {
            if (product.getId().equalsIgnoreCase(id)) {
                return product;
            }
        }
        return null;
    }

    public ArrayList<Product> findProductsByName(String name) {
        return (ArrayList<Product>) getProducts().stream().filter(product -> product.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
    }

    public abstract void display();


}

class InventoryManager extends ProductManager {

    @Override
    public void display() {
        int idPercentage = 10;
        int namePercentage = 40;
        int stockPercentage = 15;
        int statusPercentage = 20;
        int pricePercentage = 15;

        int idWidth = (width * idPercentage / 100) - 1;
        int nameWidth = (width * namePercentage / 100) - 3;
        int stockWidth = (width * stockPercentage / 100) - 5;
        int statusWidth = (width * statusPercentage / 100 ) - 2;
        int priceWidth = (width * pricePercentage / 100) - 3;
        String header = String.format(
                SECONDARY_COMBO + "| %-" + idWidth + "s | %-" + nameWidth + "s | %-" + stockWidth + "s | %-" + statusWidth + "s | %-" + priceWidth + "s |" + RESET,
                "ID", "Name", "Stock", "Status", "Price"
        );
        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
        System.out.println(header);
        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
        for (Product product : getProducts()) {
            System.out.println(String.format(
                SECONDARY_COMBO + "| %-" + idWidth + "s | %-" + nameWidth + "s | %-" + stockWidth + "s |" + RESET + product.getStockStatus().getColorScheme() + " %-" + statusWidth + "s "+ RESET + SECONDARY_COMBO + "| %-" + priceWidth + "s |" + RESET,
                product.getId(), product.getName(), product.getQuantity(), product.getStockStatus().getMessage(), ("PHP " + product.getPrice())
            ));
        }
        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
    }
}

class CartManager extends ProductManager {
    private InventoryManager inventoryManager;
    private ArrayList<Product> shoppingCart;
    private HashMap<Product, Integer> salesEngine;

    public CartManager(InventoryManager inventoryManager) {
        setInventoryManager(inventoryManager);
        setShoppingCart(new ArrayList<>());
        setSalesEngine(new HashMap<>());
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public ArrayList<Product> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ArrayList<Product> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public void setInventoryManager(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }
    public HashMap<Product, Integer> getSalesEngine() {
        return salesEngine;
    }

    public void setSalesEngine(HashMap<Product, Integer> salesEngine) {
        this.salesEngine = salesEngine;
    }

    public void addProductToCart(Product item) throws InterruptedException, IOException {
        if (getInventoryManager().getProducts().contains(item)) {
            Product product = inventoryManager.findProductID(item.getId());

            for (Product cartProduct: getShoppingCart()) {
                if (cartProduct.equals(item)) {
                    int availableStock = getInventoryManager().findProductID(product.getId()).getQuantity();
                    if (cartProduct.getQuantity() < availableStock) {
                        cartProduct.setQuantity(cartProduct.getQuantity() + 1);
                    } else {
                        GlobalInterface.centerString(WRONG_COMBO, "ERROR! Product Is Out Of Stock. Please Try Again...");
                        System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                        Thread.sleep(2000);
                    }
                    return;
                }
            }
            int availableStock = getInventoryManager().findProductID(product.getId()).getQuantity();
            if (availableStock > 0) {
                getShoppingCart().add(new Product(product.getId(), product.getName(), 1, product.getPrice(), product.getLowThreshold()));
            } else {
                GlobalInterface.centerString(WRONG_COMBO, "ERROR! Product Is Out Of Stock. Please Try Again...");
                System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                Thread.sleep(2000);
            }
        } else {
            GlobalInterface.centerString(WRONG_COMBO, "ERROR! Product Not Found. Please Try Again...");
            System.out.print(RESET + MOVE_UP + CLEAR_LINE);
            return;
        }
    }
    public void updateCartQuantity(Product item, int amount) throws InterruptedException {
        for (Product cartProduct : getShoppingCart()) {
            int currentQuantity = cartProduct.getQuantity();
            int newQuantity = currentQuantity + amount;

            int availableStock = getInventoryManager().findProductID(item.getId()).getQuantity();
            if (newQuantity <= availableStock) {
                if (newQuantity >= 1) {
                    cartProduct.setQuantity(newQuantity);
                    cartProduct.setPrice(newQuantity * cartProduct.getPrice());
                } else {
                    GlobalInterface.centerString(MAYBE_COMBO, "WARNING! Quantity Cannot Be Less Than 1. Please Try Again...");
                    Thread.sleep(2000);
                    System.out.print(RESET + MOVE_UP + CLEAR_LINE);
                }
            } else {
                GlobalInterface.centerString(MAYBE_COMBO, "WARNING! Requested Quantity Exceeds Available Stock. Please Try Again...");
                Thread.sleep(2000);
                System.out.print(RESET + MOVE_UP + CLEAR_LINE);
            }
            break;
        }

    }

    public void removeProductFromCart(Product product) {
        getShoppingCart().remove(product);
    }

    public void clearCart() {
        shoppingCart.clear();
    }
    @Override
    public void display() {
        int namePercentage = 50;
        int quantityPercentage = 25;
        int pricePercentage = 25;

        int nameWidth = (width * namePercentage / 100) - 5;
        int quantityWidth = (width * quantityPercentage / 100) - 2;
        int priceWidth = (width * pricePercentage / 100) - 2;
        String header = String.format(
                SECONDARY_COMBO + "| %-" + nameWidth + "s | %-" + quantityWidth + "s | %-" + priceWidth + "s  |" + RESET,
                "Name", "Quantity", "Price"
        );
        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
        System.out.println(header);
        GlobalInterface.printHorizontalLine(SECONDARY_COMBO);
        if (getShoppingCart().isEmpty()) {
            System.out.println(String.format(
                    SECONDARY_COMBO + "| %-" + nameWidth + "s | %-" + quantityWidth + "s | %-" + priceWidth + "s  |" + RESET,
                    "", "", ""
            ));
        } else {
            for (Product product : getShoppingCart()) {
                System.out.println(String.format(
                        SECONDARY_COMBO + "| %-" + nameWidth + "s | %-" + quantityWidth + "s | %-" + priceWidth + "s  |" + RESET,
                        product.getName(), product.getQuantity(), ("PHP " + product.getPrice())
                ));
            }
        }
    }
}