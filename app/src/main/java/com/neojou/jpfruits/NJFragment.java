package com.neojou.jpfruits;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class NJFragment extends Fragment {
    private static final String TAG="JPFruits:NJFragment";

    public NJFragment(int layout_id)
    {
        super(layout_id);
    }

    abstract protected void async_func();
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(this::async_func);
        executor.shutdown();
        Log.d(TAG, "onViewCreated() is finished");
    }
}