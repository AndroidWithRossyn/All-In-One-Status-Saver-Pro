package com.kessi.allstatussaver.utils;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

public class KMoSupport {

    public static void KMojDownload(Context context, String mvUrl) {
        Utils.displayLoader(context);
        Log.e("onResponse: ", "Moj url0 " + mvUrl);

        try {
            mvUrl = mvUrl.substring(mvUrl.lastIndexOf("/") + 1);

            if (mvUrl.contains("?referrer=share")) {
                mvUrl = mvUrl.substring(0, mvUrl.indexOf("?"));
            }

            JSONObject bodyObject = new JSONObject("{\"appVersion\":83,\"bn\":\"broker1\",\"client\":\"android\",\"deviceId\":\"ebb088d29e7287b1\",\"message\":{\"adData\":{\"adsShown\":0,\"firstFeed\":false},\"deviceInfoKey\":\"OSyQoHJLJ4NsXPLyQePkAICh3Q0ih0bveFwm1KEV+vReMuldqo+mSyMjdhb4EeryKxk1ctAbYaDH\\\\nTI+PMRPZVYH5pBccAm7OT2uz69vmD/wPqGuSgWV2aVNMdM75DMb8NZn1JU2b1bo/oKs80baklsvx\\\\n1X7jrFPL6M5EDTdPDhs=\\\\n\",\"deviceInfoPayload\":\"M6g+6j6irhFT/H6MsQ/n/tEhCl7Z5QgtVfNKU8M90zTJHxqljm2263UkjRR9bRXAjmQFXXOTXJ25\\\\nOHRjV7L5Lw+tUCONoYfyUEzADihWfAiUgXJEcKePfZONbdXXuwGgOPeD0k4iSvI7JdzroRCScKXd\\\\n41CkmXFayPaRL9aqgAgs6kSoIncCWBU2gEXiX1lgPVvdmUzCZ+yi2hFA+uFOmv1MJ6dcFKKcpBM6\\\\nHSPIrGV+YtTyfd8nElx0kyUbE4xmjOuMrctkjnJkd2tMdxB8qOFKeYrcLzy4LZJNXyUmzs29XSE+\\\\nhsrMZib8fFPJhJZIyGCWqfWiURut4Bg5HxYhYhg3ejPxFjNyXxS3Ja+/pA+A0olt5Uia7ync/Gui\\\\n58tlDQ4SKPthCzGa1tCVN+2y/PW30+LM79t0ltJ/YrNZivQx4eEnszlM9nwmIuj5z5LPniQghA6x\\\\nrfQ8IqVUZfiitXj/Fr7UjKg1cs/Ajj8g4u/KooRvVkg9tMwWePtJFqrkk1+DU4cylnSEG3XHgfer\\\\nslrzj5NNZessMEi+4Nz0O2D+b8Y+RjqN6HqpwZPDHhZwjz0Iuj2nhZLgu1bgNJev5BwxAr8akDWv\\\\nvKsibrJS9auQOYVzbYZFdKMiBnh+WHq0qO2aW1akYWCha3ZsSOtsnyPnFC+1PnMbBv+FiuJmPMXg\\\\nSODFoRIXfxgA/qaiKBipS+kIyfaPxn6O1i6MOwejVuQiWdAPTO132Spx0cFtdyj2hX6wAMe21cSy\\\\n8rs3KQxiz+cq7Rfwzsx4wiaMryFunfwUwnauGwTFOW98D5j6oO8=\\\\n\",\"lang\":\"Hindi\",\"playEvents\":[{\"authorId\":\"18326559001\",\"networkBitrate\":1900000,\"initialBufferPercentage\":100,\"isRepost\":false,\"sg\":false,\"meta\":\"NotifPostId\",\"md\":\"Stream\",\"percentage\":24.68405,\"p\":\"91484006\",\"radio\":\"wifi\",\"r\":\"deeplink_VideoPlayer\",\"repeatCount\":0,\"timeSpent\":9633,\"duration\":15,\"videoStartTime\":3916,\"t\":1602255552820,\"clientType\":\"Android\",\"i\":79,\"appV\":83,\"sessionId\":\"72137847101_8863b3f5-ad2d-4d59-aa7c-cf1fb9ef32ea\"},{\"authorId\":\"73625124001\",\"networkBitrate\":1900000,\"initialBufferPercentage\":100,\"isRepost\":false,\"sg\":false,\"meta\":\"list2\",\"md\":\"Stream\",\"percentage\":17.766666,\"p\":\"21594412\",\"radio\":\"wifi\",\"r\":\"First Launch_VideoPlayer\",\"repeatCount\":0,\"tagId\":\"0\",\"tagName\":\"\",\"timeSpent\":31870,\"duration\":17,\"videoStartTime\":23509,\"t\":1602218215942,\"clientType\":\"Android\",\"i\":79,\"appV\":83,\"sessionId\":\"72137847101_db67c0c9-a267-4cec-a3c3-4c0fa4ea16e1\"}],\"r\":\"VideoFeed\"},\"passCode\":\"9e32d6145bfe53d14a0c\",\"resTopic\":\"response/user_72137847101_9e32d6145bfe53d14a0c\",\"userId\":\"72137847101\"}");


            AndroidNetworking.post("https://moj-apis.sharechat.com/videoFeed?postId=" + mvUrl + "&firstFetch=true")
                    .addHeaders("X-SHARECHAT-USERID", "72137847101")
                    .addHeaders("X-SHARECHAT-SECRET", "9e32d6145bfe53d14a0c")
                    .addHeaders("APP-VERSION", "83")
                    .addHeaders("PACKAGE-NAME", "in.mohalla.video")
                    .addHeaders("DEVICE-ID", "ebb088d29e7287b1")
                    .addHeaders("CLIENT-TYPE", "Android:")
                    .addHeaders("Content-Type", "application/json; charset=UTF-8")
                    .addHeaders("Host", "moj-apis.sharechat.com")
                    .addHeaders("Connection", "Keep-Alive:")
                    .addHeaders("User-Agent", "okhttp/3.12.12app-version:83")
                    .addHeaders("cache-control", "no-cache")
                    .addHeaders("client-type", "Android")
                    .addHeaders("connection", "Keep-Alive")
                    .addHeaders("content-type", "application/json;charset=UTF-8")
                    .addHeaders("device-id", "ebb088d29e7287b1")
                    .addHeaders("host", "moj-apis.sharechat.com")
                    .addHeaders("package-name", "in.mohalla.video")
                    .addHeaders("postman-token", "37d59a7c-f247-3b70-ab3c-1dedf4079852")
                    .addHeaders("user-agent", "okhttp/3.12.12")
                    .addHeaders("x-sharechat-secret", "9e32d6145bfe53d14a0c")
                    .addHeaders("x-sharechat-userid", "72137847101")
                    .addJSONObjectBody(bodyObject)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            String mUrl;
                            try {

                                JSONObject jsonObject = new JSONObject(response.toString());

                                mUrl = jsonObject.getJSONObject("payload")
                                        .getJSONArray("d")
                                        .getJSONObject(0)
                                        .getJSONArray("bandwidthParsedVideos")
                                        .getJSONObject(3)
                                        .getString("url");

                                Utils.dismissLoader();

                                String timeStamp = String.valueOf(System.currentTimeMillis());
                                String file = "Moj_Video" + "_" + timeStamp;
                                String ext = "mp4";
                                String fileName = file + "." + ext;

                                Utils.downloader(context, mUrl, Utils.mojDirPath, fileName);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Utils.dismissLoader();
                            }


                        }

                        @Override/**/
                        public void onError(ANError error) {
                            Utils.dismissLoader();
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            Utils.dismissLoader();
        }
    }

}
