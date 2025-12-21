package com.fic.proyecto_desarrollo_movil.ui.theme;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {

    private static final String BASE_URL = "http://10.0.2.2/parkmanager/api/";
    private static final String TAG = "NetworkUtils";

    public static String sendPostRequest(String endpoint, String jsonInput) {
        HttpURLConnection connection = null;
        try {
            String fullEndpoint = endpoint;
            URL url = new URL(BASE_URL + fullEndpoint);
            connection = (HttpURLConnection) url.openConnection();

            Log.d(TAG, "Conectando a: " + url.toString());
            Log.d(TAG, "JSON enviado: " + jsonInput);

            // Configurar la conexión HTTP
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            // Escribir los datos JSON en el cuerpo de la solicitud
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
                Log.d(TAG, "JSON escrito en el output stream");
            }

            // Obtener el código de respuesta del servidor
            int responseCode = connection.getResponseCode();
            Log.d(TAG, "📥 Código de respuesta: " + responseCode + " para " + endpoint);

            // Leer la respuesta del servidor
            BufferedReader in;
            if (responseCode >= 200 && responseCode < 300) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String responseString = response.toString();
            Log.d(TAG, "Respuesta del servidor: " + responseString);

            return responseString;

        } catch (Exception e) {
            Log.e(TAG, "Error in sendPostRequest: " + e.getMessage());
            e.printStackTrace();
            return "{\"success\":false,\"message\":\"Error de conexión: " + e.getMessage() + "\"}";
        } finally {
            if (connection != null) {
                connection.disconnect();
                Log.d(TAG, "Conexión cerrada");
            }
        }
    }
    public static String get(String urlString) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();

            Log.d(TAG, "Conectando (GET) a: " + urlString);

            // Configurar la conexión para GET
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            // Obtener el código de respuesta
            int responseCode = connection.getResponseCode();
            Log.d(TAG, "📥 Código de respuesta GET: " + responseCode);

            // Leer la respuesta (Similar a tu método POST)
            BufferedReader in;
            if (responseCode >= 200 && responseCode < 300) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String responseString = response.toString();
            Log.d(TAG, "Respuesta del servidor (GET): " + responseString);

            return responseString;

        } catch (Exception e) {
            Log.e(TAG, "Error in getRequest: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
                Log.d(TAG, "Conexión GET cerrada");
            }
        }
    }
}