package net.minesucht.managers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class Fetcher {

	private static Fetcher instance;
	public Fetcher(){
		instance = this;
	}
	
	
	public GameProfile queryGameProfile(String skinURL) {
        GameProfile newSkinProfile = new GameProfile(UUID.randomUUID(), null);
        try {
            String json = queryURL(skinURL);
            JSONParser parser = new JSONParser();
            JSONObject apijson = (JSONObject) parser.parse(json);
            JSONArray properties = (JSONArray) apijson.get("properties");
            Iterator propertiesIter = properties.iterator();
            JSONObject test = (JSONObject) parser.parse(propertiesIter.next().toString());
            JSONObject texture = (JSONObject) parser.parse(new String(org.apache.commons.codec.binary.Base64.decodeBase64(test.get("value").toString())));
            JSONObject textureSkin = (JSONObject) texture.get("textures");
            JSONObject textureSkinUrl = (JSONObject) textureSkin.get("SKIN");
            String url = textureSkinUrl.get("url").toString();
            newSkinProfile.getProperties().put("textures", new Property("textures", Base64Coder.encodeString("{textures:{SKIN:{url:\"" + url + "\"}}}")));
        } catch (Exception e) {
            e.printStackTrace();
            newSkinProfile.getProperties().put("textures", new Property("textures", Base64Coder.encodeString("{textures:{SKIN:{url:\"http://assets.mojang.com/SkinTemplates/steve.png\"}}}")));
        }
        return newSkinProfile;
    }
	
	public String queryURL(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
    
    public static Fetcher getInstance(){
    	return instance;
    }
}
