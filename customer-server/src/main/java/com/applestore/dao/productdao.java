package com.applestore.dao;

import com.applestore.model.Product;
import com.applestore.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class productdao {

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, name, description, price, image_url FROM products";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getString("image_url")
                );
                products.add(p);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении товаров: " + e.getMessage());
        }

        return products;
    }
}
