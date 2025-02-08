package com.neojou.jpfruits;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@startuml
package com.neojou.jpfruits {
    class JParser {
        - static final String TAG = "JPFruit:JParser"
                - final InputStream is
        - InputStreamReader is_reader
        - BufferedReader br
        - boolean isCommented
        + JParser(InputStream is)
        + String readLine()
        + void close()
    }

    JParser o-- InputStream
    JParser o-- InputStreamReader
    JParser o-- BufferedReader
    JParser o-- Log
    JParser o-- StandardCharsets
}
@enduml


public class JParser {
    private static final String TAG="JPFruit:JParser";

    final protected InputStream is;
    protected InputStreamReader is_reader = null;
    protected BufferedReader br = null;

    boolean isCommented;

    public JParser(InputStream is) {
        this.is = is;
        isCommented = false;
        try {
            is_reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            br = new BufferedReader(is_reader);
        } catch (Exception e) {
            Log.e(TAG, "JParser() failed Exception" + e);
        }
    }

    public String readLine() {
        String line = null;

        try {
            while ((line = br.readLine()) != null) {
                //Log.v(TAG, "line="+line);
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.startsWith("//")) continue;
                if (line.startsWith("/*")) {
                    isCommented = true;
                    continue;
                }
                if (line.startsWith("*/")) {
                    isCommented = false;
                    continue;
                }
                if (isCommented)
                    continue;
                break;
            }
        } catch (Exception e) {
            Log.e(TAG, "readLine() failed Exception" + e);
        }

        return line;
    }

    public void close() {
        try {
            if (br != null)
                br.close();
            if (is_reader != null)
                is_reader.close();
            if (is != null)
                is.close();
        } catch (Exception e) {
            Log.e(TAG, "close() failed");
        }
    }
}