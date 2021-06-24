package com.fish.netty.http;

import lombok.extern.slf4j.Slf4j;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UriDecoder {
    private Map<String, String> paramsMap = new HashMap<String, String>();
    private String path[];
    private String ip;

    public void praser(String url) {
        int index = url.indexOf('?');
        int jjj = url.indexOf("/");
        if (index < 0) {
            if (jjj >= 0) {
                setPath(url.substring(jjj + 1));
            }
            return;
        }
        setPath(url.substring(jjj + 1, index));



        index++;
        String tempurl = url.substring(index);
        int endIndex;
        int startIndex;
        index = 0;
        while (true) {
            endIndex = tempurl.indexOf('=', index);
            if (endIndex < 0) {
                break;
            }
            startIndex = tempurl.indexOf('&', endIndex);
            if (startIndex < 0) {
                paramsMap.put(tempurl.substring(index, endIndex), tempurl.substring(endIndex + 1));
                break;
            }
            paramsMap.put(tempurl.substring(index, endIndex), tempurl.substring(endIndex + 1, startIndex));
            index = startIndex + 1;
        }
    }

    public void prasePostParam(String str) {
        int endIndex;
        int startIndex;
        int index = 0;
        if (str == null)
            return;
        while (true) {
            endIndex = str.indexOf('=', index);
            if (endIndex < 0) {
                break;
            }
            startIndex = str.indexOf('&', endIndex);
            if (startIndex < 0) {
                paramsMap.put(str.substring(index, endIndex), str.substring(endIndex + 1));
                break;
            }
            paramsMap.put(str.substring(index, endIndex), str.substring(endIndex + 1, startIndex));
            index = startIndex + 1;
        }
    }

    public String getValue(String key) {
        return paramsMap.get(key);
    }

    public boolean containsKey(String key) {
        return paramsMap.containsKey(key);
    }

    public String getString(String key) {
        return paramsMap.get(key);
    }

    public String getString(String key, String defaultString) {
        if (!paramsMap.containsKey(key) || paramsMap.get(key) == null || paramsMap.get(key).length() == 0)
            return defaultString;
        return paramsMap.get(key);
    }

    public String getStringUrlDecoder(String key) {
        if (!paramsMap.containsKey(key) || paramsMap.get(key) == null || paramsMap.get(key).length() == 0)
            return null;
        try {
            return URLDecoder.decode(paramsMap.get(key), "UTF-8");
        } catch (Exception e) {

        }
        return null;
    }

    public String getStringUrlDecoder(String key, String defalut) {
        if (!paramsMap.containsKey(key) || paramsMap.get(key) == null || paramsMap.get(key).length() == 0)
            return defalut;
        try {
            return URLDecoder.decode(paramsMap.get(key), "UTF-8");
        } catch (Exception e) {

        }
        return defalut;
    }

    public int getInt(String key) {
        try {
            return Integer.valueOf(paramsMap.get(key));
        } catch (Exception e) {
            return -1;
        }

    }

    public int getInt(String key, int df) {
        try {
            return Integer.valueOf(paramsMap.get(key));
        } catch (Exception e) {
            return df;
        }

    }

    public long getLong(String key) {
        try {
            return Long.valueOf(paramsMap.get(key));
        } catch (Exception e) {
            return -1l;
        }

    }

    public long getLong(String key, long df) {
        try {
            return Long.valueOf(paramsMap.get(key));
        } catch (Exception e) {
            return df;
        }

    }

    public void setIpFromXForwardFor(String temp) {
        if (temp != null && temp.length() > 0) {
            String tttt[] = temp.split(",");
            ip = tttt[0].replaceAll(" ", "");
        }
    }

    public String getPath(int i) {
        if (path.length <= i)
            return null;
        return path[i];
    }

    public void setPath(String path) {
        if (path == null || path.length() == 0) {
            this.path = new String[]{"/"};
        } else {
            this.path = path.split("/");
        }
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Map<String, String> getParamsMap() {
        return paramsMap;
    }
}
