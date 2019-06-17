package com.example.maps

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Html
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.map.HTTPDataHandler
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import android.view.inputmethod.EditorInfo



class MainActivity : AppCompatActivity() {

    var httpDataHandler : HTTPDataHandler = HTTPDataHandler()
    var place : Place? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    fun onEditorAction(textView: TextView, actionId: Int, keyEvent: KeyEvent) {
        if (keyEvent.hashCode() == KeyEvent.KEYCODE_ENTER){
            btnPesquisarLugaresClick(textView)
        }
    }


     fun btnPesquisarLugaresClick(view: View){

         //deve-se verificar se está no formato de (lat, lon)
//        val string = "hello@world.com"
//
//val index = string.indexOf('@')
//
//val domain: String? = if (index == -1) null else string.substring(index + 1)

         val pesquisa = txtPesquisarLugares.text.toString()
         val index = pesquisa.indexOf(",")
         var reverse = false
         var lat : String? = null
         var lon : String? = null

         if (index >= 0){
             lat = pesquisa.substring(0,index)
             lon = pesquisa.substring(index + 1, pesquisa.length)
             Log.d("lat", lat)
             Log.d("lon", lon)
             if (lat != null && lon != null){
                 reverse = true
             }
         }

        Log.d("reverse",  "" + reverse)
         val thread = Thread {
             var pesquisa : String
             if (reverse){
//                 https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=-25&lon=-49
                 pesquisa = "https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=" + lat + "&lon=" + lon +
                         "&email=rvarjao@gmail.com"
             }else{
                 pesquisa =
                     "https://nominatim.openstreetmap.org/search/" + txtPesquisarLugares.text + "?format=json&addressdetails=1&limit=1"
             }
             Log.d("PESQUISA", pesquisa)
            //https://nominatim.openstreetmap.org/search/<query>?<params>
            //https://nominatim.openstreetmap.org/search.php?q=curitiba&polygon_geojson=1&viewbox=
            val resultado = httpDataHandler.GetHTTPData(pesquisa.toString())
            place = Place(resultado)
            runOnUiThread({
                if (place != null) {
                    //Apresenta resultado na tela
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        textview.setText(Html.fromHtml(place!!.html(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        textview.setText(Html.fromHtml(place!!.html()));
                    }
                } else {
                    textview.setText("Local não encontrado")
                }
            })

            //Detalhes
            if (place != null && place!!.place_id != null) {
                val thread_details = Thread {
                    //https://nominatim.openstreetmap.org/details.php?osmtype=W&osmid=38210407&format=json
                    var pesquisa = "https://nominatim.openstreetmap.org/details.php?place_id=" +
                            place!!.place_id + "&format=json&email=rvarjao@gmail.com"
                    //https://nominatim.openstreetmap.org/search.php?q=curitiba&polygon_geojson=1&viewbox=
                    val resultado = httpDataHandler.GetHTTPData(pesquisa.toString())
                    runOnUiThread({
                        textviewDetalhes.setText(resultado)
                    })
                }
                thread_details.start()
            } else {
                textviewDetalhes.setText("Local não encontrado")
            }
        }
        thread.start()
    }



}
