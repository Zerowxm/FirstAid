package wxm.com.firstaid.module;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Zero on 12/19/2016.
 */

public class District {
    String name;
    String adcode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public void c(String json){
        List<District> districts;
        try {
            JSONObject object=new JSONObject(json);
            districts=new Gson().fromJson(object.getJSONArray("districts").toString(),
                    new TypeToken<List<District>>() {
                    }.getType());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "District{" +
                "name='" + name + '\'' +
                ", adcode='" + adcode + '\'' +
                '}';
    }
}
