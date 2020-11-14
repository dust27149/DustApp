package com.dust.app.retrofit.api;


import com.dust.app.bean.Geocode;
import com.dust.app.retrofit.response.TianDiTuResponse;

import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface TianDiTuService {

    @GET("geocoder?")
    Observable<TianDiTuResponse<Geocode>> getGeocode(@QueryMap Map<String, String> params);

}
