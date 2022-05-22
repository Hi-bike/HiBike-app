package com.roundG0929.hibike.activities.map_route;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.overlay.Marker;
import com.roundG0929.hibike.R;
import com.roundG0929.hibike.api.information.InformationApi;
import com.roundG0929.hibike.api.information.dto.DangerInformation_detail;
import com.roundG0929.hibike.api.information.requestBody.Danger_infoBody;
import com.roundG0929.hibike.api.map_route.navermap.NaverApiInterface;
import com.roundG0929.hibike.api.server.ApiInterface;
import com.roundG0929.hibike.api.server.RetrofitClient;
import com.roundG0929.hibike.api.server.dto.DeleteDanger;
import com.roundG0929.hibike.api.server.dto.GetDangerOne;
import com.roundG0929.hibike.api.server.dto.ShiftDanger;
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

    public interface IsDeleteListener {
        void isDelete(boolean isDelete, int index);
    }

    private IsDeleteListener mIsDeleteListener;


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
        int index = bundle.getInt("index");
        String userId = bundle.getString("userId");
        double[] dangerLocation = bundle.getDoubleArray("dangerLocation");
        double[] myLocation = bundle.getDoubleArray("myLocation");



        titleText = view.findViewById(R.id.titleText);
        locationText = view.findViewById(R.id.locationText);
        dateText = view.findViewById(R.id.dateText);
        userNickname = view.findViewById(R.id.userNickname);
        contentText = view.findViewById(R.id.contentText);
        dangerDelete = view.findViewById(R.id.dangerDelete);
        informationImage = view.findViewById(R.id.informationImage);

        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        imageApi = new ImageApi();

        if (dangerId < 0) {
            Danger_infoBody dangerInfoBody = new Danger_infoBody();
            dangerInfoBody.setLatitude(dangerLocation[0]);
            dangerInfoBody.setLongitude(dangerLocation[1]);

            new InformationApi().getDangetInformationDetail(dangerInfoBody).enqueue(new Callback<DangerInformation_detail>() {
                @Override
                public void onResponse(Call<DangerInformation_detail> call, Response<DangerInformation_detail> response) {
                    if (response.isSuccessful()) {
                        DangerInformation_detail.Result result = response.body().result;
                        titleText.setText(result.getTitle());
                        locationText.setText(result.getRegion() + " " + result.getRegion_detail());
                        String[] times = result.getTime().split(" ");
                        dateText.setText(times[2] + " " + times[1] + " " + times[0]);
                        userNickname.setText(result.getNickname());
                        contentText.setText(result.getContents());
                        imageApi.setImageOnImageView(getActivity(), informationImage, imageApi.getDangerImageUrl(result.getImage()));
                    }
                }
                @Override
                public void onFailure(Call<DangerInformation_detail> call, Throwable t) {

                }
            });
        } else {
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
                        imageApi.setImageOnImageView(getActivity(), informationImage, imageApi.getDangerImageUrl(result.getImage()));
                    }
                }
                @Override
                public void onFailure(Call<GetDangerOne> call, Throwable t) {}
            });
        }



        dangerDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteDanger deleteDanger = new DeleteDanger();
                deleteDanger.setMyLatitude(myLocation[0]);
                deleteDanger.setLongitude(myLocation[1]);
                deleteDanger.setLatitude(dangerLocation[0]);
                deleteDanger.setLongitude(dangerLocation[1]);
                deleteDanger.setUserId(userId);
                api.deleteDanger(deleteDanger).enqueue(new Callback<DeleteDanger>() {
                    @Override
                    public void onResponse(Call<DeleteDanger> call, Response<DeleteDanger> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getContext(), "위험요소와 너무 멀리있거나 지나온 기록이 없습니다.", Toast.LENGTH_SHORT).show();
                            dangerDelete.setEnabled(true);
                        } else {
                            mIsDeleteListener.isDelete(true, index);
                            dangerDelete.setEnabled(false);
                            dismiss();
                        }
                    }
                    @Override
                    public void onFailure(Call<DeleteDanger> call, Throwable t) {}
                });
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mIsDeleteListener = (IsDeleteListener)getActivity();
        }
        catch (ClassCastException e) {
            Log.e("IsDeleteListener", "onAttach: ClassCastException: "
                    + e.getMessage());
        }
    }
}