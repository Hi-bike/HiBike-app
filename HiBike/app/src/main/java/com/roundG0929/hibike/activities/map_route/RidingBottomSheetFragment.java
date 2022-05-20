package com.roundG0929.hibike.activities.map_route;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.roundG0929.hibike.R;
import com.roundG0929.hibike.api.map_route.navermap.NaverApiInterface;
import com.roundG0929.hibike.api.server.ApiInterface;
import com.roundG0929.hibike.api.server.RetrofitClient;
import com.roundG0929.hibike.api.server.dto.GetDangerOne;
import com.roundG0929.hibike.api.server.fuction.ImageApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RidingBottomSheetFragment extends BottomSheetDialogFragment {
    ApiInterface api;
    ImageApi imageApi;

    TextView titleText, locationText, dateText, userNickname, contentText;
    TextView dangerDelete;
    ImageView informationImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_riding_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        int dangerId = bundle.getInt("dangerId");

        titleText = view.findViewById(R.id.titleText);
        locationText = view.findViewById(R.id.locationText);
        dateText = view.findViewById(R.id.dateText);
        userNickname = view.findViewById(R.id.userNickname);
        contentText = view.findViewById(R.id.contentText);
        dangerDelete = view.findViewById(R.id.dangerDelete);
        informationImage = view.findViewById(R.id.informationImage);

        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        imageApi = new ImageApi();

        api.getDangerOne(dangerId).enqueue(new Callback<GetDangerOne>() {
            @Override
            public void onResponse(Call<GetDangerOne> call, Response<GetDangerOne> response) {
                if (response.isSuccessful()) {
                    GetDangerOne.Result result = response.body().getResult();
                    titleText.setText(result.getTitle());
                    locationText.setText(result.getRegion() + " " + result.getRegionDetail());
                    String[] times = result.getTime().split(" ");
                    dateText.setText(times[2] + " " + times[1] + " " + times[0]);
                    userNickname.setText(result.getNickname());
                    contentText.setText(result.getContents());
                    imageApi.getImage(informationImage, imageApi.getDangerImageUrl(result.getImage()));
                }
            }
            @Override
            public void onFailure(Call<GetDangerOne> call, Throwable t) {
            }
        });

    }
}