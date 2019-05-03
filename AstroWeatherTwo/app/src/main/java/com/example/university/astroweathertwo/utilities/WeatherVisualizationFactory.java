package com.example.university.astroweathertwo.utilities;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WeatherVisualizationFactory {
    private static Map<Integer, String> weatherCodeImageNameHash = new HashMap<>();

    static {
        for(int i = 0 ; i < 47; i++)
            weatherCodeImageNameHash.put(i, i + ".png");

        weatherCodeImageNameHash.put(3200, "na.png");
    }

    public static void setBackgroundForImageView(int conditionsCode, ImageView imageView, Activity activity) throws IOException {
        String fileName = weatherCodeImageNameHash.get(conditionsCode) != null ? weatherCodeImageNameHash.get(conditionsCode) : "na.png";
        imageView.setBackground(Drawable.createFromStream(activity.getAssets().open(fileName), "na.png"));
    }
}
