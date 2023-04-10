package com.ruslan.getjasondata;

import okhttp3.*;
import org.json.JSONArray;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    private static final OkHttpClient client = new OkHttpClient();
    private static final Bot bot = new Bot();

    public static void main(String[] args) throws IOException {
        ArrayList<String[]> parameters = getParameters();
        boolean allUpdated = true;
        for (String[] parameter : parameters) {
            if (!getData(parameter)) {
                allUpdated = false;
                break;
            }
            System.out.println(true);
        }
        if (allUpdated) {
            bot.setMsg(client, "Обновився👌, 😎.");
        } else {
            bot.setMsg(client, "🤚Не обновився🤬.");
        }
    }

    private static ArrayList<String[]> getParameters() {
        final HttpRequestParameters hrp = new HttpRequestParameters();
        final String[][] strArray = {
                {hrp.url_vb, hrp.courtId_vb, hrp.urlValue_vb, hrp.name_vb},
                {hrp.url_pr, hrp.courtId_pr, hrp.urlValue_pr, hrp.name_pr},
                {hrp.url_ug, hrp.courtId_ug, hrp.urlValue_ug, hrp.name_ug},
                {hrp.url_zka, hrp.courtId_zka, hrp.urlValue_zka, hrp.name_zka}
        };
        return new ArrayList<>(Arrays.asList(strArray));
    }

    private static boolean getData(String[] parameters) throws IOException {
        if (checkWebsite(parameters[2])) {
            final String application = "application/x-www-form-urlencoded; charset=UTF-8";
            final String referer = "Referer";
            final String x_Requested = "X-Requested-With";
            final String xMLHttpRequest = "XMLHttpRequest";

            final MediaType mediaType = MediaType.parse(application);
            final Request request = new Request.Builder()
                    .url(parameters[0])
                    .post(RequestBody.create(parameters[1], mediaType))
                    .addHeader(referer, parameters[2])
                    .addHeader(x_Requested, xMLHttpRequest)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                if (response.body() != null) {
                    String responseBody = response.body().string();
                    JSONArray json = new JSONArray(responseBody);
                    Path logFile = Paths.get(parameters[3]);
                    try (BufferedWriter writer = Files.newBufferedWriter(logFile, StandardCharsets.UTF_8)) {
                        writer.write(json.toString(1));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean checkWebsite(String url) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            return false;
        }
    }
}