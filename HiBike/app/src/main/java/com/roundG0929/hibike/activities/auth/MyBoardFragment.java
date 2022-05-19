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
import com.roundG0929.hibike.activities.board.ViewContentsActivity;
import com.roundG0929.hibike.api.server.ApiInterface;
import com.roundG0929.hibike.api.server.RetrofitClient;
import com.roundG0929.hibike.api.server.dto.DeleteMyBoard;
import com.roundG0929.hibike.api.server.dto.PostMyBoard;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBoardFragment extends Fragment {
    ListView listView;
    ListViewAdapter adapter;
    ProgressBar progressbar;
    ApiInterface api;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String id; //user id
    int page = 0; //데이터 page
    final int OFFSET = 15; // 서버에서 가져올 게시물 개수
    boolean mLockListView = false; //중복 요청 제한 lock
    boolean isLast = false; //등록된 게시물 마지막
    int count = 1;

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

        pref = getContext().getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();
        id = pref.getString("id", "");

        api = RetrofitClient.getRetrofit().create(ApiInterface.class);

        View view = inflater.inflate(R.layout.fragment_my_board, container, false);
        listView = view.findViewById(R.id.listview_board);
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
        api.getMyBoard(id,page).enqueue(new Callback<PostMyBoard>() {
            @Override
            public void onResponse(Call<PostMyBoard> call, Response<PostMyBoard> response) {
                if (response.isSuccessful()) {

                    ArrayList<PostMyBoard.Result> results = response.body().getResults();

                    if (results.size() > 0) {
                        if (results.get(0).getCount() > -1) {
                            count = results.get(0).getCount();
                        }
                        for (PostMyBoard.Result result : results) {
                            Log.v("PostMyBoard result", String.valueOf(result.getTitle()));
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
            public void onFailure(Call<PostMyBoard> call, Throwable t) {
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
        ArrayList<PostMyBoard.Result> items = new ArrayList<>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(PostMyBoard.Result item) {
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
        public View getView(int position, View view, ViewGroup viewGroup) {
            final Context context = viewGroup.getContext();
            final PostMyBoard.Result item = items.get(position);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_board, viewGroup, false);

            TextView boardId = view.findViewById(R.id.boardId);
            TextView myBoardTitle = view.findViewById(R.id.myBoardTitle);
            TextView myBoardTime = view.findViewById(R.id.myBoardTime);


            boardId.setText(item.getCount()+"");
            myBoardTitle.setText(item.getTitle());

            String[] times = item.getTime().split(" ");
            myBoardTime.setText(times[3]+" "+times[2]+" "+times[1]);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ViewContentsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Title", item.getTitle());
                    bundle.putString("Content", item.getContents());
                    bundle.putInt("post_id", item.getBoardId());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

            DeleteMyBoard deleteMyBoard = new DeleteMyBoard();
            deleteMyBoard.setPostId(item.getBoardId());

            TextView myBoardDelete = view.findViewById(R.id.myBoardDelete);
            myBoardDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     api.deleteMyBoard(deleteMyBoard).enqueue(new Callback<DeleteMyBoard>() {
                         @Override
                         public void onResponse(Call<DeleteMyBoard> call, Response<DeleteMyBoard> response) {
                             if (response.isSuccessful()) {
                                 myBoardDelete.setBackground(ContextCompat.getDrawable(context, R.drawable.button_rounded_withdraw));
                                 myBoardDelete.setTextColor(Color.DKGRAY);

                                 items.remove(position);
                                 adapter.notifyDataSetChanged();
                             } else {
                                 Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                             }
                         }

                         @Override
                         public void onFailure(Call<DeleteMyBoard> call, Throwable t) {
                             Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                         }
                     });
                }
            });

            return view;
        }
    }

}