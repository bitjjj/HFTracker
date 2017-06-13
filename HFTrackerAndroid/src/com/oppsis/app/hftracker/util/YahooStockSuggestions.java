/*
 The MIT License

 Copyright (c) 2013 Nitesh Patel http://niteshpatel.github.io/ministocks

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */

package com.oppsis.app.hftracker.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YahooStockSuggestions {

    private static final String BASE_URL =
            "http://d.yimg.com/autoc.finance.yahoo.com/autoc?callback=YAHOO.Finance.SymbolSuggest.ssCallback&query=";
    private static final Pattern PATTERN_RESPONSE =
            Pattern
                    .compile("YAHOO\\.Finance\\.SymbolSuggest\\.ssCallback\\((\\{.*?\\})\\)");

    /**
     * Get Symbol Suggestions *
     */
    public static List<Map<String, String>> getSuggestions(String query) {
        List<Map<String, String>> suggestions =
                new ArrayList<Map<String, String>>();

        String response;
        try {
            String url = BASE_URL + URLEncoder.encode(query, "UTF-8");
            response = getURLData(url);

        } catch (UnsupportedEncodingException e1) {
            response = null;
        }

        // Return if empty response
        if (response == null || response.equals("")) {
            return suggestions;
        }

        Matcher m = PATTERN_RESPONSE.matcher(response);
        if (m.find()) {
            response = m.group(1);
            try {
                JSONArray jsonA =
                        new JSONObject(response)
                                .getJSONObject("ResultSet")
                                .getJSONArray("Result");

                for (int i = 0; i < jsonA.length(); i++) {

                    Map<String, String> suggestion =
                            new HashMap<String, String>();

                    JSONObject jsonO = jsonA.getJSONObject(i);
                    suggestion.put("symbol", jsonO.getString("symbol"));
                    suggestion.put("name", jsonO.getString("name"));
                    suggestions.add(suggestion);
                }
                return suggestions;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return suggestions;
    }
    
    private static String getURLData(String url) {

        // Ensure we always request some data
        if (!url.contains("INDU"))
            url = url.replace("&s=", "&s=INDU+");

        // Grab the data from the source
        String response = null;
        try {
            // Set connection timeout and socket timeout
            URLConnection connection = new URL(url).openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(60000);
            InputStream stream = connection.getInputStream();

            // Read information out of input stream
            BufferedReader r = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null)
                builder.append(line).append("\n");
            response = builder.toString();

        } catch (MalformedURLException ignored) {
        } catch (IOException ignored) {
        }
        return response;
    }
    
    public static void main(String[] args) {
    	getSuggestions("A");
	}
}
