package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        URL urlObj = new URL("http://universities.hipolabs.com/search?country=Brazil");
        HttpURLConnection conexao = (HttpURLConnection)urlObj.openConnection();
        conexao.setRequestMethod("GET");
        int responseCode = conexao.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in .readLine()) != null) {
                response.append(inputLine);
            } in .close();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.toString());
            List<Universidade> universidades = new ArrayList<>();

            Iterator<JsonNode> iterator = jsonNode.elements();
            while (iterator.hasNext()) {
                JsonNode universityNode = iterator.next();
                String name = universityNode.get("name").asText();
                String url = universityNode.get("web_pages").get(0).asText();
                Universidade universidade = new Universidade(name, url);
                universidades.add(universidade);
            }

            for (Universidade universidade : universidades) {
                System.out.println(universidade.toString());
            }
        } else {
            System.out.println("GET request did not work");
        }
    }
}