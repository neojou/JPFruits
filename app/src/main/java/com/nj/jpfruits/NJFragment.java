package com.nj.jpfruits;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class NJFragment extends Fragment {
    private static final String TAG="JPFruits:NJFragment";
    protected ExecutorService executor;
    protected int layout_id;

    protected Handler uiThread = new Handler(Looper.getMainLooper());
    protected void doOnUiCode(Runnable ui_code) {
        uiThread.post(ui_code);
    }

    public NJFragment(int layout_id)
    {
        super(layout_id);
        this.layout_id = layout_id;
    }

    abstract protected void async_func();
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> async_func());
        executor.shutdown();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
