module your.module.name {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.json;
    exports com.products;
    exports com.products.Cart;
    exports com.products.Order;
    exports com.products.Product;
    exports com.products.File;
    exports com.products.Report;
}

