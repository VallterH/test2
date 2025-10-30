package com.applestore;

import com.applestore.dao.productdao;
import com.applestore.model.Product;
import com.google.gson.Gson;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        productdao dao = new productdao();
        List<Product> products = dao.getAllProducts();

        Gson gson = new Gson();
        String json = gson.toJson(products);

        System.out.println("Список товаров в JSON:");
        System.out.println(json);
    }
}