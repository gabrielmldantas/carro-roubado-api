package br.carroroubado.app;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import br.carroroubado.api.Placa;
import br.carroroubado.api.PlacaDao;
import br.carroroubado.aws.AwsClientBuilder;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectTextRequest;
import software.amazon.awssdk.services.rekognition.model.DetectTextResponse;
import software.amazon.awssdk.services.rekognition.model.Image;

public class VerificarPlacaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Content-Type", "text/html; charset=utf-8");
        try (InputStream htmlStream = getClass().getResourceAsStream("/html/verificarPlaca.html")) {
            byte[] buffer = new byte[8192];
            int n;
            while ((n = htmlStream.read(buffer)) != -1) {
                resp.getOutputStream().write(buffer, 0, n);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part partPlaca = req.getPart("placa");
        if ("image/png".equals(partPlaca.getContentType()) || "image/jpeg".equals(partPlaca.getContentType())) {
            byte[] imagem = new byte[Long.valueOf(partPlaca.getSize()).intValue()];
            partPlaca.getInputStream().read(imagem);

            RekognitionClient client = AwsClientBuilder.buildClient();
            DetectTextResponse response = client.detectText(DetectTextRequest.builder()
                    .image(Image.builder().bytes(SdkBytes.fromByteArray(imagem)).build())
                    .build());

            Placa placa = Placa.fromRekognitionResponse(response);
            try {
                placa.setRoubado(new PlacaDao().isPlacaRoubada(placa.getNumeracao(), placa.getLocalizacao()));
            } catch (SQLException e) {
                throw new IOException(e);
            }

            resp.sendRedirect(String.format("/carros/verificar/?numeroPlaca=%s&localizacao=%s&roubada=%s",
                    placa.getNumeracao(), placa.getLocalizacao(), String.valueOf(placa.isRoubado())));
        } else {
            resp.sendRedirect("/carros/verificar/?erro=formatoInvalido");
        }
    }
}
