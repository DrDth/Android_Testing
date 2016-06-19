package com.example.user.android_test_dev;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final TextView textView = (TextView) findViewById(R.id.rss_feed);

        new Thread() {
            @Override
            public void run() {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL("http://www.androidpit.com/feed/main.xml");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    for (int count; (count = in.read(buffer)) != -1; ) {
                        baos.write(buffer, 0, count);
                    }

                    final String rssFeed = new String(baos.toByteArray(), "UTF-8");
                    runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    textView.setText(rssFeed);
                                }
                            });

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        }.start();


    }
}