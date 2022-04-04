package com.roundG0929.hibike.activities.map_route;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.roundG0929.hibike.R;
import com.roundG0929.hibike.api.map_route.kakaoLocal.searchPlaceDto.Document;

import java.util.ArrayList;

public class SearchPlaceAdapter extends RecyclerView.Adapter<SearchPlaceAdapter.ViewHolder> {
    ArrayList<Document> places = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_searchplace,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Document place = places.get(position);
        holder.setItem(place);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }
    public void addItem(Document place){
        places.add(place);
    }

    public void setItems(ArrayList<Document> items){
        this.places = items;
    }

    public Document getItem(int position){
        return places.get(position);
    }

    public void setItem(int position,Document item){
        places.set(position,item);
    }

    public void clearItem(){places.clear();}






    //뷰홀더
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView placeNameText;
        TextView placeAddressText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            placeNameText = itemView.findViewById(R.id.placeNameText);
            placeAddressText = itemView.findViewById(R.id.placeAddressText);
        }

        public void setItem(Document place){
            placeNameText.setText(place.getPlace_name());
            placeAddressText.setText(place.getAddress_name());
        }
    }
}
