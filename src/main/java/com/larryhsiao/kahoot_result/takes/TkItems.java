package com.larryhsiao.kahoot_result.takes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.larryhsiao.kahoot_result.items.Item;
import com.larryhsiao.kahoot_result.items.JsonItem;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rs.RsVelocity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TkItems implements Take {
    @Override
    public Response act(Request req) throws IOException {
        final URL url = new URL("");
        final URLConnection conn = url.openConnection();
        final List<Item> items = new ArrayList<>();
        try (InputStream inputStream = conn.getInputStream()) {
            final JsonArray jsonArray = JsonParser.parseReader(
                new InputStreamReader(inputStream)
            ).getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                final Item item = new JsonItem(jsonElement.getAsJsonObject());
                items.add(item);
            }
        }
        items.sort(Comparator.comparingInt(Item::checkCode));
        final String res = responseHtml(items);
        return new RsVelocity(
            getClass().getResource("/item.html.vm"),
            new RsVelocity.Pair("result", res)
        );
    }

    private String responseHtml(List<Item> items) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (Item item : items) {
            stringBuilder.append(item.checkCode())
                .append(" ")
                .append(item.name())
                .append(" ")
                .append(item.userName())
                .append(" ")
                .append(item.userId());
            stringBuilder.append("</br>");
        }
        return stringBuilder.toString();
    }
}

