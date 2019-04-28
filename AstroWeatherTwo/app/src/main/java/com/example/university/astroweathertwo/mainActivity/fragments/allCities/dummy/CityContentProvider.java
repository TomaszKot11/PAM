package com.example.university.astroweathertwo.mainActivity.fragments.allCities.dummy;

import android.content.Context;
import com.example.university.astroweathertwo.utilities.database.SQLiteDatabaseHelper;
import com.example.university.astroweathertwo.utilities.database.entities.City;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: make dynamic loading when the scroll view is scrolled
public class CityContentProvider {
    public static List<City> ITEMS;

    public static void loadCitiesFromDatabase(Context context) {
        SQLiteDatabaseHelper sqLiteDatabaseHelper = SQLiteDatabaseHelper.getInstance(context);

        ITEMS = sqLiteDatabaseHelper.allCities();
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }
}
