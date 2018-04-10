package com.woodcock.citygang.classes;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by user on 16.07.2016.
 */
public class TimeViewer {

    private final OkHttpClient client = new OkHttpClient();
    private String[] time;
    private String[] date=new String[3];
    boolean flagStart;

    public void run() throws Exception {
        Request request = new Request.Builder()
                .url("http://www.timeapi.org/utc/now")
                .build();


        flagStart=false;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                time=getTime(responseHeaders.value(0));

                flagStart=true;
//                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
//                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                }
//
//                System.out.println(response.body().string());

            }
        });

    }

    private String[] getTime(String str)
    {
        String[] infos=str.split(" ");
        date[0]=infos[1];
        date[1]=infos[2];
        date[2]=infos[3];
        return infos[4].split(":");
    }

    public String[] Time()
    {
        return time;
    }

    public String[] Date()
    {
        return date;
    }

    public Boolean getFlagStart()
    {return flagStart;}
}
