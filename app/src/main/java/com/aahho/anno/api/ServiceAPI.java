package com.aahho.anno.api;

import com.aahho.anno.model.NotificationRequest;
import com.aahho.anno.model.NotificationResponse;
import com.aahho.anno.model.UploadResponse;

import okhttp3.MultipartBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by souvikdas on 27/10/17.
 */

public interface ServiceAPI {

    @Multipart
    @POST("/service/s3/upload")
    Call<UploadResponse> uploadImage(@Part MultipartBody.Part file);

    @POST("/anno/user/notify")
    Call<NotificationResponse> sendNotification(@Body NotificationRequest notificationRequest);
}
