package com.roundG0929.hibike.activities.auth;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roundG0929.hibike.R;

public class MyBoardFragment extends Fragment {
    public static MyBoardFragment newInstance(int number) {
        MyBoardFragment myBoardFragment = new MyBoardFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("number", number);
        myBoardFragment.setArguments(bundle);
        return myBoardFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return LayoutInflater.from(inflater.getContext()).inflate(R.layout.fragment_my_board, container,false);
    }
}