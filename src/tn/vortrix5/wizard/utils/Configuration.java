package tn.vortrix5.wizard.utils;

import com.google.gson.stream.JsonReader;
import org.json.JSONObject;
import tn.vortrix5.wizard.json.JSONReader;
import tn.vortrix5.wizard.json.JSONWriter;

import java.io.File;
import java.io.IOException;

public class Configuration {
    public final JSONObject object;

    private final File file;
    public Configuration(String path) throws IOException {
        this.file = new File(path);
        if(file.exists())
            this.object = new JSONReader(file).toJSONObject();
              else
                object = new JSONObject();
    }

    public String getString(String key, String defaultValue)
    {
        if(!object.has(key))
            object.put(key, defaultValue);
        return object.getString(key);
    }
    public int getInt(String key, int defaultValue)
    {
        if(!object.has(key))
            object.put(key, defaultValue);
        return object.getInt(key);
    }
    public void save()
    {
        try(JSONWriter writer = new JSONWriter(file)){
            writer.write(this.object);
            writer.flush();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
