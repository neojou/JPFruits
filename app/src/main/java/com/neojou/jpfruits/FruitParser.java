package com.neojou.jpfruits;

import android.util.Log;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@startuml
package com.neojou.jpfruits {
    class FruitParser {
        - static final String TAG = "JPFruit:FruitParser"
                + FruitParser(InputStream is)
                + FruitName getFruitName()
    }

    FruitParser ..|> JParser
    FruitParser o-- InputStream
    FruitParser o-- FruitName
    FruitParser o-- Log
    FruitParser o-- Pattern
    FruitParser o-- Matcher
}
@enduml

public class FruitParser extends JParser {
    private static final String TAG="JPFruit:FruitParser";

    public FruitParser(InputStream is) {
        super(is);
    }

    public FruitName getFruitName() {
        final FruitName fn = new FruitName();

        try {
            String str;
            final String line = readLine();
            if (line == null) return null;
            final Pattern p = Pattern.compile("(.+) (.+)");
            Matcher m = p.matcher(line);
            if (m.matches()) {
                str = m.group(1);
                if (str != null)
                    fn.eng_name = str.trim();
                str = m.group(2);
                if (str != null)
                    fn.jp_name = str.trim();
            } else {
                Log.e(TAG, "eng_name:" + fn.eng_name);
                Log.e(TAG, "jp_name:" + fn.jp_name);
                Log.e(TAG, "line :" + line + "not match");
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "getFruitName() parsing fail exception: " + e);
            return null;
        }

        return fn;
    }

}

