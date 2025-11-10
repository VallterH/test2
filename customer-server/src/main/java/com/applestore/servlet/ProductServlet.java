package com.applestore.servlet;

import com.applestore.dao.productdao;
import com.applestore.model.Product;
import com.applestore.util.DatabaseManager;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.List;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private final productdao dao = new productdao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {


        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            resp.setHeader("WWW-Authenticate", "Basic realm=\"AppleStore\"");
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }


        String base64Credentials = authHeader.substring("Basic ".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        String[] values = credentials.split(":", 2);

        if (values.length != 2) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String email = values[0];
        String password = values[1];


        if (!isValidUser(email, password)) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Неверный логин или пароль");
            return;
        }

        // Если всё ок — отправляем JSON
        resp.setContentType("application/json; charset=UTF-8");
        List<Product> products = dao.getAllProducts();
        String json = new Gson().toJson(products);
        resp.getWriter().write(json);
    }


    private boolean isValidUser(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
