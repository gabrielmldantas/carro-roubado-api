package br.carroroubado.api;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

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
		System.out.println("");
		Gson gson = new Gson();
		ObjetoRequisicao objetoRequisicao = null;
		try (Reader reader = new InputStreamReader(req.getInputStream())) {
			objetoRequisicao = gson.fromJson(reader, ObjetoRequisicao.class);
		}
		RekognitionClient client = AwsClientBuilder.buildClient();
		DetectTextResponse response = client.detectText(DetectTextRequest.builder()
				.image(Image.builder().bytes(SdkBytes.fromByteArray(objetoRequisicao.getBuffer())).build())
				.build());

		Placa placa = Placa.fromRekognitionResponse(response);

		try (Connection connection = DatabaseManager.getConnection()) {
			String sql = "select count(*) from placa where placa = ? and localizacao = ?";
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setString(1, placa.getNumeracao());
				ps.setString(2, placa.getLocalizacao());
				try (ResultSet rs = ps.executeQuery()) {
					placa.setRoubado(rs.next());
					resp.addHeader("Content-Type", "application/json");
					System.out.println(gson.toJson(placa));
					resp.getWriter().println(gson.toJson(placa));
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
