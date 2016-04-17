package io.github.djxy.permissionManager.network;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Samuel on 2016-04-03.
 */
public class HttpRequest {

    private static final Pattern UUID_PATTERN = Pattern.compile("\\w{32}");

    public static UUID getPlayerUUIDFromName(String name) throws Exception {
        String link = "https://api.mojang.com/users/profiles/minecraft/"+name+"?at="+(System.currentTimeMillis() / 1000);

        URL url = new URL(link);
        HttpsURLConnection con = (HttpsURLConnection)url.openConnection();

        if(con.getResponseCode() == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Matcher matcher = UUID_PATTERN.matcher(response);

            if(matcher.find())
                return convertMojangUUID(matcher.group());
        }

        return null;
    }

    private static UUID convertMojangUUID(String mojangUUID){
        return UUID.fromString(mojangUUID.substring(0, 8)+"-"+mojangUUID.substring(8, 12)+"-"+mojangUUID.substring(12, 16)+"-"+mojangUUID.substring(16, 20)+"-"+mojangUUID.substring(20, 32));
    }

}
