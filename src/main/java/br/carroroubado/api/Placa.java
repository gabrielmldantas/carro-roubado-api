package br.carroroubado.api;

import software.amazon.awssdk.services.rekognition.model.DetectTextResponse;
import software.amazon.awssdk.services.rekognition.model.TextDetection;

public class Placa {

	private String numeracao;
	private String localizacao;
	private boolean roubado;

	public String getNumeracao() {
		return numeracao;
	}

	public void setNumeracao(String numeracao) {
		this.numeracao = numeracao;
	}

	public String getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}

	public boolean isRoubado() {
		return roubado;
	}

	public void setRoubado(boolean roubado) {
		this.roubado = roubado;
	}

	public static Placa fromRekognitionResponse(DetectTextResponse response) {
		Placa placa = new Placa();
		for (TextDetection textDetection : response.textDetections()) {
			String[] parts = textDetection.detectedText().split("-");
			if (parts.length == 1) {
				String part = parts[0].trim();
				if (part.equals("BRASIL")) {
					placa.setLocalizacao(part);
				} else {
					placa.setNumeracao(part);
				}
			} else {
				String firstPart = parts[0].trim();
				if (firstPart.length() == 2) {
					placa.setLocalizacao(firstPart);
				} else {
					placa.setNumeracao(firstPart + parts[1].trim());
				}
			}
		}
		return placa;
	}
}
