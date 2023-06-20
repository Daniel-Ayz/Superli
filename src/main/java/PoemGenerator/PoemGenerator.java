package PoemGenerator;

import HumanResources.ServiceLayer.Response;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PoemGenerator {

    public static Response<String > getPoem(String emotion, String name, String role) {
        try {
            String prompt = getUserPrompt(emotion, name, role);
            String poem = generatePoem(prompt);
            return new Response<>(poem, true);
        } catch (IOException e) {
            e.printStackTrace();
            return new Response<>(null, false);
        }
    }

    private static String getUserPrompt(String emotion, String name, String role) throws IOException {
        return "generate a few lines short motivating poem about a person named " + name + " who is a " + role + " at a grocery store and hes emotional state is " + emotion;
    }

    private static String generatePoem(String prompt) throws IOException {
        String apiKey = "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\n"; // Replace with your actual ChatGPT API key
        String apiUrl = "https://api.openai.com/v1/engines/text-davinci-001/completions";

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + apiKey);
        connection.setDoOutput(true);

        String inputData = "{\"prompt\": \"" + prompt + "\", \"max_tokens\": 50}";
        byte[] inputBytes = inputData.getBytes(StandardCharsets.UTF_8);
        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(inputBytes, 0, inputBytes.length);
        }

        StringBuilder response = new StringBuilder();
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        } else {
            System.out.println("Request failed with response code: " + connection.getResponseCode());
        }
        connection.disconnect();

        // Parse the response JSON and extract the generated poem
        String poem = parsePoemFromResponse(response.toString());
        return poem;
    }


    private static String parsePoemFromResponse(String response) {
        // Parse the JSON response to extract the generated poem
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        JsonArray choices = jsonObject.getAsJsonArray("choices");
        System.out.println(response);
        if (choices != null && choices.size() > 0) {
            JsonObject poemObject = choices.get(0).getAsJsonObject();
            String poem = poemObject.get("text").getAsString();

            // Remove any leading/trailing whitespace and quotation marks
            poem = poem.trim().replaceAll("^\"|\"$", "");

            // Split the poem into lines
            String[] lines = poem.split("\n");

//            // Ensure the poem has exactly 5 lines
//            if (lines.length == 5) {
//                return poem;
//            }
            return poem;
        }

        // If the response doesn't contain a valid poem, return an error message
        return "Failed to generate a poem. Please try again.";
    }

}
