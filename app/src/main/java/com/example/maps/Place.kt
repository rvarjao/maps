package com.example.maps

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class Place constructor(string : String){

    var display_name : String? = "";
    var place_id : String? = ""
    var lat : Float = 0F
    var lon : Float = 0F
    var address : JSONObject? = null
    var city : String? = ""
    var state : String? = ""
    var country  : String? = ""
    var country_code : String? = ""
    var details : String? = ""

    init {
        Log.d("init string", string)
        try{
            var jsonObject : JSONObject? = null
            var jsonArray : JSONArray?

            if (string.startsWith("{")){
                jsonObject = JSONObject(string)
            }else if(string.startsWith("[")){
                jsonArray = JSONArray(string)
                jsonObject = jsonArray.getJSONObject(0)
            }

            place_id = jsonObject!!.getString("place_id")
            display_name = jsonObject.getString("display_name")
            lat = jsonObject.getString("lat").toFloat()
            lon = jsonObject.getString("lon").toFloat()
            address = jsonObject.getJSONObject("address")
            if (address != null){
                city = address?.getString("city")
                state = address?.getString("state")
                country = address?.getString("country")
                country_code = address?.getString("country_code")
            }
        }catch (e : Exception){
            e.printStackTrace()
        }

//        2019-06-16 14:18:46.954 25731-25760/com.example.maps
//        I/RESULTADO: [{"place_id":198243138,"licence":"Data © OpenStreetMap contributors,
//        ODbL 1.0. https://osm.org/copyright",
//        "osm_type":"relation",
//        "osm_id":297514,
//        "boundingbox":["-25.6450101","-25.3467009","-49.38914","-49.1843182"],
//        "lat":"-25.4295963","lon":"-49.2712724",
//        "display_name":"Curitiba, Microrregião de Curitiba, RMC, Mesorregião Metropolitana de Curitiba, PR, Região Sul, Brasil",
//        "class":"place",
//        "type":"city",
//        "importance":0.728433827268013,
//        "icon":"https://nominatim.openstreetmap.org/images/mapicons/poi_place_city.p.20.png",
//        "address":{"city":"Curitiba","county":"Microrregião de Curitiba","state_district":"Mesorregião Metropolitana de Curitiba","state":"PR","country":"Brasil","country_code":"br"}}]


    }

    fun html() : String{
        var html = """<strong>Nome: </strong> %s<br>
                <strong>Localização: </strong>(%.5f, %.5f)<br>
                    <strong>Cidade: </strong>%s<br>
                    <strong>Estado: </strong>%s<br>
                    <strong>País: </strong>%s<br>
                    <strong>Código de País: </strong>%s<br>
                """.format(display_name, lat, lon, city, state, country, country_code)
        ;
        return html
    }



}