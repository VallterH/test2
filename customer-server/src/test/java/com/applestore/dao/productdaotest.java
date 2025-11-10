package com.applestore.dao;

import com.applestore.dao.productdao;
import com.applestore.model.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ProductDaoTest {

    static productdao dao;

    @BeforeAll
    static void setup() {
        dao = new productdao();
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = dao.getAllProducts();
        assertNotNull(products, "Список товаров не должен быть null");
        assertTrue(products.size() > 0, "В базе должен быть хотя бы один товар");
    }

    @Test
    void testProductFields() {
        List<Product> products = dao.getAllProducts();
        Product first = products.get(0);
        assertNotNull(first.getName(), "Название продукта не должно быть null");
        assertTrue(first.getPrice() > 0, "Цена должна быть больше 0");
    }
}
