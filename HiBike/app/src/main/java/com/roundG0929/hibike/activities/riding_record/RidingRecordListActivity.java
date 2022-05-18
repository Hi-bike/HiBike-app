package com.roundG0929.hibike.activities.riding_record;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.roundG0929.hibike.HibikeUtils;
import com.roundG0929.hibike.R;
import com.roundG0929.hibike.api.server.ApiInterface;
import com.roundG0929.hibike.api.server.RetrofitClient;
import com.roundG0929.hibike.api.server.dto.GetRidingAll;
import com.roundG0929.hibike.api.server.dto.GetRidingTotal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RidingRecordListActivity extends AppCompatActivity {
    private ListView listView;
    private ridingListViewAdapter adapter;
    private int page = 0;                           // 페이징변수. 초기 값은 0.
    private final int OFFSET = 15;                  // 한 페이지마다 로드할 데이터 갯수.
    private ProgressBar progressBar;                // 데이터 로딩중을 표시할 프로그레스바
    private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수
    ApiInterface api;

    String id; //유저 아이디
    boolean isLast = false;
    int count; //기록 개수

    // ui
    TextView tv_riding_id, tv_route, tv_riding_info, tv_starting_point, tv_unique_id, tv_riding_total;
    ImageView ivRidingBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riding_record_list);

        //api 연결
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        //user id
        id = pref.getString("id", "");
        getTotalInfo();

        tv_riding_total = findViewById(R.id.tv_riding_total);

        ivRidingBack = findViewById(R.id.iv_riding_back);
        ivRidingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.listview_riding);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        adapter = new ridingListViewAdapter();

        listView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);

        getItems();

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
             @Override
             public void onScrollStateChanged(AbsListView absListView, int i) {
//                 if (!listView.canScrollVertically(-1)) {최상단}
                if (!listView.canScrollVertically(1) && mLockListView == false && isLast == false) { //화면 밑 and lock 존재
                    progressBar.setVisibility(View.VISIBLE);
                    getItems();
                }
             }
             @Override
             public void onScroll(AbsListView absListView, int i, int i1, int i2) {}
         });
    }

    public void getItems() {
        mLockListView = true; // lock 획득
        api.getRidingAll(id, page).enqueue(new Callback<GetRidingAll>() {
            @Override
            public void onResponse(Call<GetRidingAll> call, Response<GetRidingAll> response) {
                ArrayList<Object> records = (ArrayList<Object>) response.body().getResult();
                if (records.size() > 0){
                    for (Object record : records) {
                        String json = HibikeUtils.objectToJson(record);
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            String createTime = jsonObject.getString("create_time").substring(0,10);
                            String distance = jsonObject.getString("distance")+"m";
                            String aveSpeed = jsonObject.getString("ave_speed")+"km/h";
                            String[] ridingTime = jsonObject.getString("riding_time").split(" : ");
                            String time = ridingTime[0] + "분" + ridingTime[1] + "초";
                            String startingPoint = jsonObject.getString("starting_region");
                            String endPoint = jsonObject.getString("end_region");
                            String uniqueId = jsonObject.getString("unique_id");
                            int count = jsonObject.getInt("count");

                            adapter.addItem(new RidingRecord(count, createTime, time, aveSpeed, distance, startingPoint, endPoint, uniqueId));
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            Log.e("JSONException", e.toString());
                        }
                    }

                } else {
                    isLast = true;
                }
            }
            @Override
            public void onFailure(Call<GetRidingAll> call, Throwable t) {
                Log.e("on failure", t.toString());
            }
        });

        page += OFFSET;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                mLockListView = false; // lock 반납
            }
        },600);
    }

    public void getTotalInfo(){
        api.getRidingTotal(id).enqueue(new Callback<GetRidingTotal>() {
            @Override
            public void onResponse(Call<GetRidingTotal> call, Response<GetRidingTotal> response) {
                if (response.isSuccessful()) {
                    String totalDistance = response.body().getTotalDistance();
                    int distance = (int) Math.round(Double.parseDouble(totalDistance));
                    String[] totalTime = response.body().getTotalTime().split(" : ");
                    String time="";
                    try {
                        time = totalTime[0] + "분 " + totalTime[1] + "초";
                        tv_riding_total.setText("총 거리: " + distance +"m  "+"  총 시간: " + time);
                    } catch (Exception e) {
                        tv_riding_total.setText("총 거리: 0m"+"  "+"총 시간: 0분 : 0초");
                    }

//                    count = response.body().getCount();

                } else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<GetRidingTotal> call, Throwable t) {}
        });
    }
    public class ridingListViewAdapter extends BaseAdapter {
        ArrayList<RidingRecord> records = new ArrayList<RidingRecord>();

        @Override
        public int getCount(){
            return records.size();
        }

        @Override
        public RidingRecord getItem(int position) {
            return records.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addItem(RidingRecord item) {
            records.add(item);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            final Context context = viewGroup.getContext();
            final RidingRecord ridingRecord = records.get(position);

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_riding, viewGroup, false);
            } else {
                View view = new View(context);
                view = (View) convertView;
            }

            tv_riding_id = (TextView) convertView.findViewById(R.id.tv_riding_id);
            tv_route = (TextView) convertView.findViewById(R.id.tv_route);
            tv_riding_info = (TextView) convertView.findViewById(R.id.tv_riding_info);
            tv_starting_point = (TextView) convertView.findViewById(R.id.tv_starting_point);
            tv_unique_id = (TextView) convertView.findViewById(R.id.tv_unique_id);

            tv_riding_id.setText(ridingRecord.getRidingId()+"");
            tv_route.setText(ridingRecord.getStarting_point()+ " > " + ridingRecord.getEnd_point());
            tv_riding_info.setText(
                    ridingRecord.getCreateTime()+" | "
                            +ridingRecord.getDistance()+" | "
                            +ridingRecord.getAveSpeed()+" | "
                            +ridingRecord.getRidingTime());
            String[] startPoint = ridingRecord.getStarting_point().split(" ");
            tv_starting_point.setText(startPoint[0]+" "+startPoint[1]+" "+startPoint[2]);
            tv_unique_id.setText(ridingRecord.getUniqueId());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // uniqueId 넘기기
                    Intent intent = new Intent(getApplicationContext(), RidingRecordActivity.class);
                    intent.putExtra("uniqueId", ridingRecord.getUniqueId());
                    intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }



}