package com.kessi.allstatussaver.utils

import android.app.Activity
import android.widget.Toast
import androidx.annotation.Keep
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.kessi.allstatussaver.model.insta.ModelEdNode
import com.kessi.allstatussaver.model.insta.ModelGetEdgetoNode
import com.kessi.allstatussaver.model.insta.ModelInstagramResponse
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.net.URI

object InstaDownload {


    var Urlwi: String? = ""

    @Keep
    fun startInstaDownload(Url: String, activity: Activity) {

        Utils.displayLoader(activity)


        try {

            val uri = URI(Url)
            Urlwi = URI(
                uri.scheme,
                uri.authority,
                uri.path,
                null,
                uri.fragment
            ).toString()


        } catch (ex: java.lang.Exception) {
            try {
                Utils.dismissLoader()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return
        }

        var urlwithoutlettersqp: String? = Urlwi

        if (urlwithoutlettersqp!!.contains("/reel/")) {
            urlwithoutlettersqp = urlwithoutlettersqp.replace("/reel/", "/p/")
        }

        if (urlwithoutlettersqp.contains("/tv/")) {
            urlwithoutlettersqp = urlwithoutlettersqp.replace("/tv/", "/p/")
        }

        val urlwithoutlettersqp_noa: String = urlwithoutlettersqp

        urlwithoutlettersqp = "$urlwithoutlettersqp?__a=1&__d=dis"

        if (!(activity).isFinishing) {


            try {


                downloadInstagramImageOrVideoResponseOkhttp(
                    urlwithoutlettersqp_noa, activity
                )

            } catch (e: java.lang.Exception) {
                try {

                    Utils.dismissLoader()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                e.printStackTrace()
            }

        }

    }

    var myPhotoUrlIs: String? = null
    var myVideoUrlIs: String? = null

    @Keep
    fun downloadInstagramImageOrVideoResponseOkhttp(URL: String?, activity: Activity) {

//TODO check
//        Unirest.config()
//            .socketTimeout(500)
//            .connectTimeout(1000)
//            .concurrency(10, 5)
//            .proxy(Proxy("https://proxy"))
//            .setDefaultHeader("Accept", "application/json")
//            .followRedirects(false)
//            .enableCookieManagement(false)
//            .addInterceptor(MyCustomInterceptor())

        object : Thread() {
            override fun run() {


                try {

                    val cookieJar: ClearableCookieJar = PersistentCookieJar(
                        SetCookieCache(),
                        SharedPrefsCookiePersistor(activity)
                    )

                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    // init OkHttpClient
                    val client: OkHttpClient = OkHttpClient.Builder()
                        .cookieJar(cookieJar)
                        .addInterceptor(logging)
                        .build()

                    val request: Request = Request.Builder()
                        .url("$URL?__a=1&__d=dis")
                        .method("GET", null)
                        .build()
                    val response = client.newCall(request).execute()

                    val ressd = response.body!!.string()
                    var code = response.code
                    if (!ressd.contains("shortcode_media")) {
                        code = 400
                    }
                    if (code == 200) {


                        try {


                            val listType =
                                object : TypeToken<ModelInstagramResponse?>() {}.type
                            val modelInstagramResponse: ModelInstagramResponse? =
                                GsonBuilder().create()
                                    .fromJson<ModelInstagramResponse>(
                                        ressd,
                                        listType
                                    )


                            if (modelInstagramResponse != null) {


                                if (modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children != null) {
                                    val modelGetEdgetoNode: ModelGetEdgetoNode =
                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.edge_sidecar_to_children

                                    val modelEdNodeArrayList: List<ModelEdNode> =
                                        modelGetEdgetoNode.modelEdNodes
                                    for (i in 0 until modelEdNodeArrayList.size) {
                                        if (modelEdNodeArrayList[i].modelNode.isIs_video) {
                                            myVideoUrlIs =
                                                modelEdNodeArrayList[i].modelNode.video_url


                                            val timeStamp =
                                                System.currentTimeMillis().toString()
                                            val file = "Insta_$timeStamp"
                                            val ext = "mp4"
                                            val fileName = "$file.$ext"

                                            Utils.downloader(
                                                activity,
                                                myVideoUrlIs?.replace("\"", ""),
                                                Utils.instaDirPath,
                                                fileName
                                            )

                                            try {

                                                Utils.dismissLoader()
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }


                                            myVideoUrlIs = ""
                                        } else {
                                            myPhotoUrlIs =
                                                modelEdNodeArrayList[i].modelNode.display_resources[modelEdNodeArrayList[i].modelNode.display_resources.size - 1].src


                                            val timeStamp =
                                                System.currentTimeMillis().toString()
                                            val file = "Insta_$timeStamp"
                                            val ext = "png"
                                            val fileName = "$file.$ext"

                                            Utils.downloader(
                                                activity,
                                                myPhotoUrlIs?.replace("\"", ""),
                                                Utils.instaDirPath,
                                                fileName
                                            )
                                            myPhotoUrlIs = ""
                                            try {

                                                Utils.dismissLoader()
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }

                                        }
                                    }
                                } else {
                                    val isVideo =
                                        modelInstagramResponse.modelGraphshortcode.shortcode_media.isIs_video
                                    if (isVideo) {
                                        myVideoUrlIs =
                                            modelInstagramResponse.modelGraphshortcode.shortcode_media.video_url


                                        val timeStamp = System.currentTimeMillis().toString()
                                        val file = "Insta_$timeStamp"
                                        val ext = "mp4"
                                        val fileName = "$file.$ext"

                                        Utils.downloader(
                                            activity,
                                            myVideoUrlIs?.replace("\"", ""),
                                            Utils.instaDirPath,
                                            fileName
                                        )

                                        try {

                                            Utils.dismissLoader()
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                        myVideoUrlIs = ""
                                    } else {
                                        myPhotoUrlIs =
                                            modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources[modelInstagramResponse.modelGraphshortcode.shortcode_media.display_resources.size - 1].src

                                        val timeStamp = System.currentTimeMillis().toString()
                                        val file = "Insta_$timeStamp"
                                        val ext = "png"
                                        val fileName = "$file.$ext"

                                        Utils.downloader(
                                            activity,
                                            myPhotoUrlIs?.replace("\"", ""),
                                            Utils.instaDirPath,
                                            fileName
                                        )
                                        try {
                                            Utils.dismissLoader()
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                        myPhotoUrlIs = ""
                                    }
                                }


                            } else {
                                Toast.makeText(
                                    activity,
                                    "error",
                                    Toast.LENGTH_SHORT
                                ).show()

                                try {
                                    Utils.dismissLoader()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                            }


                        } catch (e: Exception) {
//
                            Utils.dismissLoader()
                            activity.runOnUiThread(Runnable {
                                Toast.makeText(
                                    activity,
                                    "Media not available for download",
                                    Toast.LENGTH_SHORT
                                ).show()
                            })
//
                        }

                    } else {
                        Utils.dismissLoader()
                        activity.runOnUiThread(Runnable {
                            Toast.makeText(
                                activity,
                                "Media not available for download",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                    }


                } catch (e: Throwable) {
                    e.printStackTrace()
                    println("The request has failed " + e.message)
                    Utils.dismissLoader()
                    activity.runOnUiThread(Runnable {
                        Toast.makeText(
                            activity,
                            "Media not available for download",
                            Toast.LENGTH_SHORT
                        ).show()
                    })

                }
            }
        }.start()
    }


}