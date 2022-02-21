package com.roundG0929.hibike.avtivities.board;

import androidx.appcompat.app.AppCompatActivity;
import com.roundG0929.hibike.R;
import android.os.Bundle;
import android.widget.ListView;

public class ListViewActivity extends AppCompatActivity {

    private ListView listview ;
    private ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        adapter = new ListViewAdapter();

        // 리스트뷰 객체 생성 및 Adapter 설정
        listview = (ListView) findViewById(R.id.list_view);
        listview.setAdapter(adapter);
        adapter.addItem(R.drawable.icons_profile,"테스트1","관리자1");
        adapter.addItem(R.drawable.icons_profile,"테스트2","관리자2");
        adapter.addItem(R.drawable.icons_profile,"테스트3","관리자3");
        adapter.addItem(R.drawable.icons_profile,"테스트4","관리자4");
        adapter.addItem(R.drawable.icons_profile,"테스트5","관리자5");
        // 리스트 뷰 아이템 추가.

    }
}