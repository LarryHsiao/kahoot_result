package com.larryhsiao.kahoot_result.items;

import com.google.gson.JsonObject;

public class JsonItem implements Item {
    private final JsonObject jsonObject;

    public JsonItem(JsonObject jsonObject) {this.jsonObject = jsonObject;}

    @Override
    public String name() {
        try {
            return jsonObject.get("giftName").getAsString();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public int checkCode() {
        try {
            return jsonObject.get("checkCode").getAsInt();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public String userName() {
        try {
            return jsonObject.get("cMoneyName").getAsString();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public String userId() {
        try {
            return jsonObject.get("cMoneyId").getAsString();
        } catch (Exception e) {
            return "";
        }
    }
}
