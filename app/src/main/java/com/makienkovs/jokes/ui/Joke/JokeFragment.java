package com.makienkovs.jokes.ui.Joke;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.makienkovs.jokes.R;
import com.makienkovs.jokes.network.Downloader;
import com.makienkovs.jokes.network.Value;

import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class JokeFragment extends Fragment {

    ListView listView;
    private ArrayList<Value> posts;
    public static Observer<String> observer;
    com.makienkovs.jokes.ui.Adapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_joke, container, false);
        init(root);
        observer = getObserver();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        Observable<String> observable = Observable.just("download");
        observable.subscribe(observer);
    }

    private void init(View root) {
        EditText count = root.findViewById(R.id.editTextCount);
        Button reload = root.findViewById(R.id.reload);
        reload.setOnClickListener(v-> {
            String countString = count.getText().toString();
            if (countString.isEmpty()) {
                Toast.makeText(getContext(), getString(R.string.enterValidCount), Toast.LENGTH_SHORT).show();
            } else {
                Downloader.getInstance().download(Integer.parseInt(count.getText().toString()));
                count.setText("");
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
        listView = root.findViewById(R.id.list);
    }

    private Observer<String> getObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                posts = Downloader.getInstance().getPosts();
                adapter = new com.makienkovs.jokes.ui.Adapter(posts);
            }

            @Override
            public void onNext(String s) {
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
//                Toast.makeText(getContext(), getString(R.string.jokesDownloaded), Toast.LENGTH_SHORT).show();
            }
        };
    }
}