package com.mou.complex;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_PRIVATE;

public class Utillities {
    public static final String RECOVERY_FILE_NAME = "recovery.tmp";
    public static final String SETTINGS_FILE_NAME = "settings.config";

    public static boolean saveFile(Context context, String data, String fileName) {
        try {
            FileOutputStream fOut = context.openFileOutput(fileName, MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(data);
            osw.flush();
            osw.close();
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return false;
    }

    public static String loadFile(Context context, String fileName) {
        StringBuilder builder = new StringBuilder();
        FileInputStream fIn;
        try {
            File file = new File(context.getFilesDir(), fileName);
            if (!file.exists()) return null;
            fIn = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;
            while ((line = bufferedReader.readLine()) != null) {

                builder.append(line).append("\n");
            }
            if (builder.length() > 0) return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean isFileExists(Context context, String fileName) {
        return new File(context.getFilesDir(), fileName).exists();
    }

}
