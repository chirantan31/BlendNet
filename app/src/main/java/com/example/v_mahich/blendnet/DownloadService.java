package com.example.v_mahich.blendnet;

import android.app.Activity;
import android.app.IntentService;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;

public class DownloadService extends IntentService {

    private int result = Activity.RESULT_CANCELED;
    public static final String URL = "urlpath";
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "filepath";
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "service receiver";
    public static SharedPreferences.Editor editor;
    Set<String> requests;
    String fileName;
    public DownloadService() {
        super("DownloadService");
    }

    // Will be called asynchronously by OS.
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("BLENDNET","Trying to Download");

        String serverAddress = "http://" + intent.getStringExtra("HUB_ADDRESS") + ":3000/";
        SharedPreferences sharedPreferences = getSharedPreferences("BLENDNET", Context.MODE_PRIVATE);
        requests = sharedPreferences.getStringSet("REQUESTS",null);
        editor = sharedPreferences.edit();
        if(requests == null) return;
        String[] requestArray = requests.toArray(new String[requests.size()]);
        //String[] requestArray = {"testVid1.mp4", "testVid2.mp4","TestVid3.mp4"};
        Log.e("BLENDNET","Array String: " + requestArray.length);
        RetrofitInterface retrofitInterface = RetrofitBuilder.createService(serverAddress);

        List<Call<ResponseBody>> calls = new ArrayList<Call<ResponseBody>>();

       for (final String fileName :
                requestArray) {

     //fileName = "TestVid3.mp4";
            retrofitInterface.downloadFile(fileName).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "server contacted and has file");
                        Log.d(TAG, response.body().toString().substring(0,50));

                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {
                                boolean writtenToDisk = writeResponseBodyToDisk(response.body(),fileName);

                                Log.d(TAG, "file download was a success? " + writtenToDisk);
                                requests.remove(fileName);
                                editor.putStringSet("REQUESTS",requests);
                                editor.commit();
                                return null;
                            }
                        }.execute();
                    }
                    else {
                        Log.d(TAG, "server contact failed");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "error");
                }
            });

        }


        publishResults("", result);
    }



    private boolean writeResponseBodyToDisk(ResponseBody body,String fileName) {
        try {
            // todo change the file location/name according to your needs
            File testVid1 = new File(getExternalFilesDir(null) + File.separator + fileName);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(testVid1);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private void publishResults(String outputPath, int result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(FILEPATH, outputPath);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }
}