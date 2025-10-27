package com.applestore;

import com.applestore.dao.productdao;
import com.applestore.model.Product;

public class Main {
    public static void main(String[] args) {
        productdao dao = new productdao();
        for (Product p : dao.getAllProducts()) {
            System.out.println(p);
        }
    }
}

