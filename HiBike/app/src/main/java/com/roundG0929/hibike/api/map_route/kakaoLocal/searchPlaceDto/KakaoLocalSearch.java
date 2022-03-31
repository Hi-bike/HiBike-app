package com.roundG0929.hibike.api.map_route.kakaoLocal.searchPlaceDto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KakaoLocalSearch {
    @SerializedName("meta")
    Meta meta;
    @SerializedName("documents")
    List<Document> documents;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
