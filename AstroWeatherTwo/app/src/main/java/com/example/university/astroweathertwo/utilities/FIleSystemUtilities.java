package com.example.university.astroweathertwo.utilities;

import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class FIleSystemUtilities {

    // data/data/packagename ;)
    public static void serializeJsonToTheFile(Context context, String fileName, JSONObject jsonObject) throws FileNotFoundException, IOException {
        FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(jsonObject.toString());
        os.close();
        fos.close();
    }

    public static JSONObject deserializeJsonFromTheFile(Context context, String fileName) throws IOException, ClassNotFoundException, JSONException {
        FileInputStream fis = context.openFileInput(fileName);
        ObjectInputStream is = new ObjectInputStream(fis);
        String jsonObjectString = (String)is.readObject();
        JSONObject jsonObject = new JSONObject(jsonObjectString);
        is.close();
        fis.close();

        return jsonObject;
    }
}
