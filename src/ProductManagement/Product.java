package ProductManagement;

public class Product {

    private String productID;
    private String productName;
    private String manufacturerName;
    private String supplierName;
    private int quantity;
    private float price;

    public Product(String productId, String productName, String manufacturerName, String supplierName, int quantity, float price) {
        this.productID = productId;
        this.productName = productName;
        this.manufacturerName = manufacturerName;
        this.supplierName = supplierName;
        this.quantity = quantity;
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String lineRepresentation()
    {
        StringBuilder line = new StringBuilder();
        line.append(productID);
        line.append(",");
        line.append(productName);
        line.append(",");
        line.append(manufacturerName);
        line.append(",");
        line.append(supplierName);
        line.append(",");
        line.append(quantity);
        line.append(",");
        line.append(price);


        return line.toString();
    }

    public String getSearchKey()
    {

        return this.productID;
    }
}
