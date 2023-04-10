package com.ruslan.getjasondata;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;


public class Bot {
    public void setMsg(OkHttpClient client, String message) throws IOException {
        final String BOT_TOKEN = "2069463279:AAFrQUDyxGPYaTfDwTl4mtiOEBTZP-RzNhI";
        final String CHAT_ID = "552974553";
        String url = String.format("https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s", BOT_TOKEN, CHAT_ID, message);
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).execute();
    }
}
