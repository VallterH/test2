package com.applestore.servlet;

import com.applestore.servlet.ProductServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.servlet.http.*;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductServletTest {

    private ProductServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter writer;

    @BeforeEach
    void setup() throws Exception {
        servlet = new ProductServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
    }

    @Test
    void shouldReturnUnauthorizedWhenNoAuth() throws Exception {
        servlet.doGet(request, response);
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void shouldReturnJsonWhenAuthorized() throws Exception {
        // Авторизация "по-настоящему" (чтобы не трогать private)
        String credentials = java.util.Base64.getEncoder()
                .encodeToString("Admin@example.com:admin123".getBytes());
        when(request.getHeader("Authorization")).thenReturn("Basic " + credentials);

        servlet.doGet(request, response);
        String result = writer.toString();

        // Проверяем, что ответ не пустой
        assertFalse(result.isEmpty(), "Ответ сервлета не должен быть пустым");
    }
}
