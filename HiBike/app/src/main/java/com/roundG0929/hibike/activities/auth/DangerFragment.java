package com.roundG0929.hibike.activities.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roundG0929.hibike.R;


public class DangerFragment extends Fragment {

    public static DangerFragment newInstance(int number) {
        DangerFragment dangerFragment = new DangerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("number", number);
        dangerFragment.setArguments(bundle);
        return dangerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            int num = getArguments().getInt("number");
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return LayoutInflater.from(inflater.getContext()).inflate(R.layout.fragment_danger, container,false);
    }
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View v=inflater.inflate(R.layout.fragment_danger,container,false);
//        return v;
//    }
}