package com.nj.jpfruits;

import android.app.Application;
import android.util.Log;

import androidx.databinding.ObservableBoolean;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.List;
import java.util.Random;


public class FruitDataViewModel {
    private static final String TAG="JPFruits:FruitDataViewModel";

    Application app;
    int fruits_file_id;


    public FruitDataViewModel(Application application, int fid) {
        app = application;
        fruits_file_id = fid;
    }


    public void load_data() {
        InputStream is;
        Log.d(TAG, "load_data()");

        /* questions.txt */
        is = app.getResources().openRawResource(fruits_file_id);
        FruitParser fp = new FruitParser(is);
        ArrayList<FruitName> fa = new ArrayList<FruitName>();
        FruitName fn;
        while ((fn = fp.getFruitName()) != null) {
            fa.add(fn);
        };
        //FruitName.log_dump(fa);
        fp.close();

    }
}
