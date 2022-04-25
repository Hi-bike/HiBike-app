package com.roundG0929.hibike.activities.riding_record;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.ArrayList;

public class RidingRecordActivity extends AppCompatActivity {
    private ListView listView;
    private ridingListViewAdapter adapter;
    private boolean lastItemVisibleFlag = false;
    private int page = 0;                           // 페이징변수. 초기 값은 0 이다.
    private final int OFFSET = 15;                  // 한 페이지마다 로드할 데이터 갯수.
    private ProgressBar progressBar;                // 데이터 로딩중을 표시할 프로그레스바
    private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수

    // ui
    TextView tv_riding_id, tv_route, tv_riding_info, tv_starting_point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riding_record);

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
                if (!listView.canScrollVertically(1) && mLockListView == false) {
                    progressBar.setVisibility(View.VISIBLE);
                    getItems();
                }
             }
             @Override
             public void onScroll(AbsListView absListView, int i, int i1, int i2) {}
         });
    }

    public void getItems() {
        //TODO: api 호출해서 데이터 처리
        mLockListView = true;

        for (int i = page; i < page+OFFSET; i++) {
            adapter.addItem(new RidingRecord(i,"04-26","02:13", "64km/h","10555m", "출발지", "도착지"));
        }
        page += OFFSET;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                mLockListView = false;
            }
        },800);
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

            tv_riding_id.setText(ridingRecord.getRidingId()+"");
            tv_route.setText(ridingRecord.getStarting_point()+ " > " + ridingRecord.getEnd_point());
            tv_riding_info.setText(
                    ridingRecord.getCreateTime()+"/ "
                            +ridingRecord.getDistance()+"/ "
                            +ridingRecord.getAveSpeed()+"/ "
                            +ridingRecord.getRidingTime());
            //TODO: 데이터 받아와서 처리
            tv_starting_point.setText("시작 지점");

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, ridingRecord.getRidingId()+"", Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;
        }
    }

}