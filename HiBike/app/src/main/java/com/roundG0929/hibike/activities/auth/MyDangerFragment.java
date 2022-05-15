package com.roundG0929.hibike.activities.auth;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.roundG0929.hibike.R;

import java.util.ArrayList;


public class MyDangerFragment extends Fragment{
    ListView listView;
    ListViewAdapter listViewAdapter;

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

        View view = inflater.inflate(R.layout.fragment_my_danger, container, false);
        listView = view.findViewById(R.id.listview_danger);

        listViewAdapter = new ListViewAdapter();

        listViewAdapter.addItem(new MyDanger(1,"asdfasdfasdf", "region", "time", "Y"));
        listViewAdapter.addItem(new MyDanger(2,"title", "region", "time", "Y"));
        listViewAdapter.addItem(new MyDanger(3,"title", "region", "time", "Y"));
        listViewAdapter.addItem(new MyDanger(4,"title", "region", "time", "Y"));
        listViewAdapter.addItem(new MyDanger(5,"title", "region", "time", "Y"));
        listViewAdapter.addItem(new MyDanger(6,"title", "region", "time", "Y"));
        listViewAdapter.addItem(new MyDanger(7,"title", "region", "time", "Y"));
        listViewAdapter.addItem(new MyDanger(8,"title", "region", "time", "Y"));
        listViewAdapter.addItem(new MyDanger(9,"title", "region", "time", "Y"));
        listViewAdapter.addItem(new MyDanger(10,"title", "region", "time", "Y"));

        listView.setAdapter(listViewAdapter);
        return view;
    }


    public class ListViewAdapter extends BaseAdapter {
        ArrayList<MyDanger> items = new ArrayList<>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(MyDanger item) {
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
            final MyDanger item = items.get(i);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_danger, viewGroup, false);

            TextView dangerId = view.findViewById(R.id.dangerId);
            TextView myDangerTitle = view.findViewById(R.id.myDangerTitle);
            TextView myDangerRegion = view.findViewById(R.id.myDangerRegion);
            TextView myDangerTime = view.findViewById(R.id.myDangerTime);
            TextView myDangerDelete = view.findViewById(R.id.myDangerDelete);

            dangerId.setText(String.valueOf(item.getDangerId()));
            myDangerTitle.setText(item.getTitle());
            myDangerRegion.setText(item.getRegion());
            myDangerTime.setText(item.getTime());
            myDangerDelete.setText(item.getIsDelete());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                }
            });

            return view;
        }
    }


}