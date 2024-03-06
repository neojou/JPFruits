package com.nj.jpfruits;

import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

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
