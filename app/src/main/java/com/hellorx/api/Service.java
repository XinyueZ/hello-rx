package com.hellorx.api;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by xzhao on 31.01.17.
 */
public interface Service {
	@GET("greeting")
	Observable<Hello> greeting();


}
