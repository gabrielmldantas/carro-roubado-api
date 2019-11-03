package br.carroroubado.aws;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;

public class AwsClientBuilder {

	public static RekognitionClient buildClient() {
		return RekognitionClient.builder()
				.region(Region.US_EAST_1)
				.build();
	}
}
