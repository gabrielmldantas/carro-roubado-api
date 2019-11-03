package br.carroroubado.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
		RekognitionClient client = AwsClientBuilder.buildClient();
		DetectTextResponse response = client.detectText(DetectTextRequest.builder()
				.image(Image.builder().bytes(SdkBytes.fromInputStream(req.getInputStream())).build())
				.build());

		Map<String, Float> texts = new HashMap<>();
		response.textDetections().forEach(text -> texts.put(text.detectedText(), text.confidence()));

		resp.addHeader("Content-Type", "application/json");
		resp.getWriter().println(new Gson().toJson(texts));
	}
}
