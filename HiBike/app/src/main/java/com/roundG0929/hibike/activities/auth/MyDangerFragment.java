package com.roundG0929.hibike.activities.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.roundG0929.hibike.R;
import com.roundG0929.hibike.api.server.ApiInterface;
import com.roundG0929.hibike.api.server.RetrofitClient;
import com.roundG0929.hibike.api.server.dto.PostMyDanger;
import com.roundG0929.hibike.api.server.dto.ShiftDanger;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyDangerFragment extends Fragment{
    ListView listView;
    ListViewAdapter adapter;
    ProgressBar progressbar;
    ApiInterface api;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String id; //user id
    int page = 0; //데이터 page
    final int OFFSET = 15; // 서버에서 가져올 위험요소 개수
    boolean mLockListView = false; //중복 요청 제한 lock
    boolean isLast = false; //등록된 위험요소 마지막
    int count = 1;


    public static MyDangerFragment newInstance(int number) {
        MyDangerFragment myDangerFragment = new MyDangerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("number", number);
        myDangerFragment.setArguments(bundle);
        return myDangerFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        pref = getContext().getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();
        id = pref.getString("id", "");

        api = RetrofitClient.getRetrofit().create(ApiInterface.class);

        View view = inflater.inflate(R.layout.fragment_my_danger, container, false);
        listView = view.findViewById(R.id.listview_danger);
        progressbar = view.findViewById(R.id.progressbar);

        adapter = new ListViewAdapter();

        progressbar.setVisibility(View.GONE);

        getItems();

        listView.setAdapter(adapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
//                 if (!listView.canScrollVertically(-1)) {최상단}
                if (!listView.canScrollVertically(1) && mLockListView == false && isLast == false) { //화면 밑 and lock 존재
                    progressbar.setVisibility(View.VISIBLE);
                    getItems();
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {}
        });

        return view;
    }

    public void getItems() {
        mLockListView = true; // lock 획득

        api.getMyDanger(id, page).enqueue(new Callback<PostMyDanger>() {
            @Override
            public void onResponse(Call<PostMyDanger> call, Response<PostMyDanger> response) {
                if (response.isSuccessful()) {

                    ArrayList<PostMyDanger.Result> results = response.body().getResults();

                    if (results.size() > 0) {
                        if (results.get(0).getCount() > -1) {
                            count = results.get(0).getCount();
                        }
                        for (PostMyDanger.Result result : results) {

                            String[] timeList = result.getTime().split(" ");
                            result.setTime(timeList[3]+" "+timeList[2]+" "+timeList[1]);

                            String[] regionList = result.getRegion().split(" ");
                            result.setRegion(regionList[0]+" "+regionList[1]+" "+regionList[2]);

                            result.setCount(count--);

                            adapter.addItem(result);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        isLast = true;
                    }

                } else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<PostMyDanger> call, Throwable t) {
                Log.e("getMyDanger", t.toString());
            }
        });
        page += OFFSET;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressbar.setVisibility(View.GONE);
                mLockListView = false; // lock 반납
            }
        },600);
    }


    public class ListViewAdapter extends BaseAdapter {
        ArrayList<PostMyDanger.Result> items = new ArrayList<>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(PostMyDanger.Result item) {
            items.add(item);
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final Context context = viewGroup.getContext();
            final PostMyDanger.Result item = items.get(i);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_danger, viewGroup, false);

            TextView dangerId = view.findViewById(R.id.dangerId);
            TextView myDangerTitle = view.findViewById(R.id.myDangerTitle);
            TextView myDangerRegion = view.findViewById(R.id.myDangerRegion);
            TextView myDangerTime = view.findViewById(R.id.myDangerTime);
            TextView myDangerDelete = view.findViewById(R.id.myDangerDelete);

            dangerId.setText(String.valueOf(item.getCount()));
            myDangerTitle.setText(item.getTitle());
            myDangerRegion.setText(item.getRegion());
            myDangerTime.setText(item.getTime());

            if (item.getIsDelete().equals("N")) {
                myDangerDelete.setText("삭제");
            } else {
                myDangerDelete.setBackground(ContextCompat.getDrawable(context, R.drawable.button_rounded_withdraw));
                myDangerDelete.setTextColor(Color.DKGRAY);
                myDangerDelete.setText("복구");
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MyDangerOne.class);
                    intent.putExtra("dangerId", item.getDangerId());
                    startActivity(intent);
                }
            });

            ShiftDanger shiftDanger = new ShiftDanger();
            shiftDanger.setDangerId(item.getDangerId());

            myDangerDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   api.shiftMyDanger(shiftDanger).enqueue(new Callback<ShiftDanger>() {
                       @Override
                       public void onResponse(Call<ShiftDanger> call, Response<ShiftDanger> response) {
                           if (response.isSuccessful()) {
                               Log.v("danger id", item.getDangerId() + "");

                               if (item.getIsDelete().equals("N")) {
                                   item.setIsDelete("Y");
                                   myDangerDelete.setBackground(ContextCompat.getDrawable(context, R.drawable.button_rounded_withdraw));
                                   myDangerDelete.setTextColor(Color.DKGRAY);
                                   myDangerDelete.setText("복구");

                               } else {
                                   item.setIsDelete("N");
                                   myDangerDelete.setBackground(ContextCompat.getDrawable(context, R.drawable.button_rounded_delete));
                                   myDangerDelete.setTextColor(Color.RED);
                                   myDangerDelete.setText("삭제");
                               }
                               adapter.notifyDataSetChanged();
                           } else {
                               Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                           }
                       }

                       @Override
                       public void onFailure(Call<ShiftDanger> call, Throwable t) {
                           Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
                       }
                   });
                }
            });

            return view;
        }
    }


}