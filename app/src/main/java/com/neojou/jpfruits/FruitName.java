package com.neojou.jpfruits;

import android.util.Log;

import java.util.List;

@startuml
package com.neojou.jpfruits {
    class FruitName {
        - static final String TAG = "JPFruits:FruitName"
                + String eng_name
        + String jp_name
        + void log_dump()
        + static void log_dump(List<FruitName> fl)
    }

    FruitName o-- Log
    FruitName o-- List
}
@enduml

public class FruitName {
    private static final String TAG="JPFruits:FruitName";

    public String eng_name;
    public String jp_name;

    public void log_dump() {
        Log.d(TAG, "eng_name : " + eng_name);
        Log.d(TAG, "jp_name : " + jp_name);
    }

    public static void log_dump(List<FruitName> fl) {
        int sz;

        if (fl == null) return;
        sz = fl.size();
        Log.d(TAG, "Fruit Name List size =" + sz);
        for (int i = 0; i < sz; i++) {
            FruitName fn = fl.get(i);
            fn.log_dump();
        }
    }
}