package com.example.map

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.ProtocolException
import java.net.URL



class HTTPDataHandler constructor() {

    fun GetHTTPData(requestURL: String):String{
        Log.e("GetHTTPData","GetHTTPData")
        val url : URL
        var response = ""
        try{
            url = URL(requestURL)
            val conn : HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.readTimeout = 15000
            conn.connectTimeout = 15000
            conn.requestMethod = "GET"

            conn.setRequestProperty("Content-type", "application/x-www-form-urlencode")
            conn.doOutput = true
            val responseCode = conn.responseCode
            Log.e("responseCode",conn.responseMessage)
            if (responseCode == HttpURLConnection.HTTP_OK){
                Log.e("HTTP_OK","HTTP_OK")
                val br = BufferedReader(InputStreamReader(conn.inputStream))

                val iterator = br.lines()
                iterator.forEach(){
                    response += it
                }
                br.close()

            }else{
                response = ""
            }

        }catch (e : ProtocolException){
            e.printStackTrace()
        }catch (e : MalformedURLException){
            e.printStackTrace()
        }catch (e: IOException){
            e.printStackTrace()
        }

        return response
    }

}

