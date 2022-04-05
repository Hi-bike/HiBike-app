package com.roundG0929.hibike.api.map_route.kakaoLocal.searchPlaceDto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Meta {
    @SerializedName("pageable_count")
    @Expose
    int pageable_count;
    @SerializedName("total_count")
    @Expose
    int total_count;
    @SerializedName("is_end")
    @Expose
    boolean is_end;

    public int getPageable_count() {
        return pageable_count;
    }

    public void setPageable_count(int pageable_count) {
        this.pageable_count = pageable_count;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public boolean isIs_end() {
        return is_end;
    }

    public void setIs_end(boolean is_end) {
        this.is_end = is_end;
    }
}
