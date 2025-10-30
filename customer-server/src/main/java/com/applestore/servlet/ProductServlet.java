package com.applestore.servlet;

import com.applestore.dao.productdao;
import com.applestore.model.Product;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class ProductServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");

        productdao dao = new productdao();
        List<Product> products = dao.getAllProducts();

        String json = new Gson().toJson(products);
        resp.getWriter().write(json);
    }
}
