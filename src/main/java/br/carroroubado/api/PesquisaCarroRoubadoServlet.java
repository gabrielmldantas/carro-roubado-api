package br.carroroubado.api;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import br.carroroubado.aws.AwsClientBuilder;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectTextRequest;
import software.amazon.awssdk.services.rekognition.model.DetectTextResponse;
import software.amazon.awssdk.services.rekognition.model.Image;

public class PesquisaCarroRoubadoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        try {
            resp.addHeader("Content-Type", "application/json");
            ObjetoRequisicao objetoRequisicao = null;
            try (Reader reader = new InputStreamReader(req.getInputStream())) {
                objetoRequisicao = gson.fromJson(reader, ObjetoRequisicao.class);
            }
            RekognitionClient client = AwsClientBuilder.buildClient();
            DetectTextResponse response = client.detectText(DetectTextRequest.builder()
                    .image(Image.builder().bytes(SdkBytes.fromByteArray(Base64.getDecoder()
                    		.decode(objetoRequisicao.getBuffer()))).build())
                    .build());

            Placa placa = Placa.fromRekognitionResponse(response);
            placa.setRoubado(new PlacaDao().isPlacaRoubada(placa.getNumeracao(), placa.getLocalizacao()));
            System.out.println(gson.toJson(placa));
            resp.getWriter().print(gson.toJson(placa));
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("excecao", new JsonPrimitive(e.getClass().getCanonicalName()));
            jsonObject.add("mensagem", new JsonPrimitive(e.getMessage() != null ? e.getMessage() : ""));
            resp.getWriter().print(gson.toJson(jsonObject));
        }
    }
}
