package br.com.alura.screenmatch.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ConsultaAPITranslated {

    public static String obterTraducao(String texto) {
        try {
            // Codifica texto para URL
            String textoEncode = URLEncoder.encode(texto, StandardCharsets.UTF_8);

            // Monta URL GET para MyMemory API (en = inglês, pt = português)
            String urlStr = "https://api.mymemory.translated.net/get?q=" + textoEncode + "&langpair=en|pt";

            URL url = new URL(urlStr);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setRequestProperty("Accept", "application/json");

            int status = conexao.getResponseCode();
            if (status != 200) {
                throw new RuntimeException("Erro HTTP " + status + ": " + conexao.getResponseMessage());
            }

            // Lê resposta JSON
            BufferedReader br = new BufferedReader(new InputStreamReader(conexao.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder resposta = new StringBuilder();
            String linha;
            while ((linha = br.readLine()) != null) {
                resposta.append(linha.trim());
            }
            br.close();

            String json = resposta.toString();

            // Extrai texto traduzido do JSON manualmente
            String chave = "\"translatedText\":\"";
            int inicio = json.indexOf(chave) + chave.length();
            int fim = json.indexOf("\"", inicio);

            if (inicio < chave.length() || fim < 0) {
                return "Erro: não foi possível extrair a tradução";
            }

            String traducao = json.substring(inicio, fim);
            return traducao;

        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao traduzir: " + e.getMessage();
        }
    }
}
