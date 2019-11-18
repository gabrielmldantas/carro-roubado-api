package br.carroroubado.api;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import software.amazon.awssdk.services.rekognition.model.DetectTextResponse;
import software.amazon.awssdk.services.rekognition.model.TextDetection;
import software.amazon.awssdk.services.rekognition.model.TextTypes;

public class Placa {

    private static final Pattern REGEX_DIGITO = Pattern.compile(".*\\d.*");
    private static final Set<String> SIGLAS = new HashSet<>();

    static {
        SIGLAS.add("AC");
        SIGLAS.add("AL");
        SIGLAS.add("AP");
        SIGLAS.add("AM");
        SIGLAS.add("BA");
        SIGLAS.add("CE");
        SIGLAS.add("DF");
        SIGLAS.add("ES");
        SIGLAS.add("GO");
        SIGLAS.add("MA");
        SIGLAS.add("MT");
        SIGLAS.add("MS");
        SIGLAS.add("MG");
        SIGLAS.add("PA");
        SIGLAS.add("PB");
        SIGLAS.add("PR");
        SIGLAS.add("PE");
        SIGLAS.add("PI");
        SIGLAS.add("RJ");
        SIGLAS.add("RN");
        SIGLAS.add("RS");
        SIGLAS.add("RO");
        SIGLAS.add("RR");
        SIGLAS.add("SC");
        SIGLAS.add("SP");
        SIGLAS.add("SE");
        SIGLAS.add("TO");
    }

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
        Map<String, Float> numeracoesByConfidence = new HashMap<>();
        Map<String, Float> localizacoesByConfidence = new HashMap<>();
        Placa placa = new Placa();

        for (TextDetection textDetection : response.textDetections()) {
            if (textDetection.type() == TextTypes.WORD) {
                if (REGEX_DIGITO.matcher(textDetection.detectedText()).matches()) {
                    numeracoesByConfidence.put(textDetection.detectedText().replaceAll("\\W", ""), textDetection.confidence());
                } else if (textDetection.detectedText().length() == 2 && isSigla(textDetection.detectedText())) {
                    localizacoesByConfidence.put(textDetection.detectedText(), textDetection.confidence());
                } else {
                    String sigla = textDetection.detectedText().split("\\W")[0];
                    if (isSigla(sigla)) {
                        localizacoesByConfidence.put(sigla, textDetection.confidence());
                    }
                }
            }
        }

        Comparator<Entry<String, Float>> confidenceComparator = (entry1, entry2) -> entry1.getValue().compareTo(entry2.getValue());
        placa.setNumeracao(numeracoesByConfidence.entrySet().stream().max(confidenceComparator).get().getKey());
        placa.setLocalizacao(localizacoesByConfidence.entrySet().stream().max(confidenceComparator).
                map(entry -> entry.getKey()).orElse("BR"));
        return placa;
    }

    private static boolean isSigla(String sigla) {
        return SIGLAS.contains(sigla);
    }
}
