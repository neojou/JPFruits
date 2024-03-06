package com.nj.jpfruits;

import android.app.Application;
import android.util.Log;

import androidx.databinding.ObservableBoolean;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;


public class FruitDataViewModel {
    private static final String TAG="JPFruits:FruitDataViewModel";

    Application app;
    int fruits_file_id;

    boolean is_loaded;
    ArrayList<String> fruits_array;
    ArrayList<String> jpfruits_array;
    HashMap<String, String> eng_to_jp_name_map;
    HashMap<String, String> jp_to_eng_name_map;



    public FruitDataViewModel(Application application, int fid) {
        app = application;
        fruits_file_id = fid;
        is_loaded = false;
        fruits_array = null;
        jpfruits_array = null;
        eng_to_jp_name_map = null;
        jp_to_eng_name_map = null;
    }

    public int get_fruit_array_size() {
        if (!is_loaded || fruits_array == null) return 0;
        return fruits_array.size();
    }

    public void shuffle() {
        if (fruits_array == null) return;
        Collections.shuffle(fruits_array);
    }

    public String get_fruit_engname_by_array_pos(int pos) {
        if (!is_loaded) return null;
        if (fruits_array == null) return null;
        if (pos < 0 || pos >= fruits_array.size()) return null;
        return fruits_array.get(pos);
    }

    public String get_fruit_jpname_by_engname(String eng_name) {
        if (!is_loaded) return null;
        if (eng_to_jp_name_map == null) return null;
        return eng_to_jp_name_map.get(eng_name);
    }

    public ArrayList<String> copy_jpfruits_array() {
        if (!is_loaded) return null;
        if (jpfruits_array == null) return null;
        return (ArrayList<String>)jpfruits_array.clone();
    }

    public void load_data() {
        if (is_loaded) return;

        InputStream is;
        Log.d(TAG, "load_data()");

        /* questions.txt */
        is = app.getResources().openRawResource(fruits_file_id);
        FruitParser fp = new FruitParser(is);
        ArrayList<FruitName> fruits_name_array = new ArrayList<>();
        FruitName fn;
        while ((fn = fp.getFruitName()) != null) {
            fruits_name_array.add(fn);
        }
        fp.close();

        //FruitName.log_dump(fa);

        fruits_array = new ArrayList<>();
        jpfruits_array = new ArrayList<>();
        eng_to_jp_name_map = new HashMap<>();
        jp_to_eng_name_map = new HashMap<>();

        for (int i = 0; i < fruits_name_array.size(); i++) {
            fn = fruits_name_array.get(i);
            fruits_array.add(fn.eng_name);
            jpfruits_array.add(fn.jp_name);
            eng_to_jp_name_map.put(fn.eng_name, fn.jp_name);
            jp_to_eng_name_map.put(fn.jp_name, fn.eng_name);
        }

        is_loaded = true;
    }
}
