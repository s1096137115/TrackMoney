package com.creation.maxting.trackmoney.activity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

/**
 * Created by MAX on 2017/8/8.
 */

public class test {

    public void tt(){
        int[] a = {1,3};
    }

    Gson gson = new GsonBuilder().create();
        String json = gson . toJson ("ddd");
        Map<String, String> result = new Gson().fromJson(json, Map.class);
}
