package br.carroroubado.app;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.carroroubado.api.PlacaDao;

public class RegistroPlacaRoubadaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Content-Type", "text/html; charset=utf-8");
        try (InputStream htmlStream = getClass().getResourceAsStream("/html/registroPlaca.html")) {
            byte[] buffer = new byte[8192];
            int n;
            while ((n = htmlStream.read(buffer)) != -1) {
                resp.getOutputStream().write(buffer, 0, n);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String placa = req.getParameter("placa").replace("-", "");
        String localizacao = req.getParameter("localizacao");
        PlacaDao placaDao = new PlacaDao();
        try {
            if (!placaDao.isPlacaRoubada(placa, localizacao)) {
                placaDao.inserir(placa, localizacao);
                resp.sendRedirect("/carros/registro/?incluido=true");
            } else {
                resp.sendRedirect("/carros/registro/?existente=true");
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }
}
