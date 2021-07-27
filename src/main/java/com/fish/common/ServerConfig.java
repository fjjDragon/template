package com.fish.common;

import com.fish.dao.redis.RedisDao;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

/**
 * @author: fjjdragon
 * @date: 2021-07-23 17:47
 */
@Slf4j
public class ServerConfig {

    private static ServerConfig instance = null;

    public static ServerConfig getInstance() {
        if (null == instance) {
            instance = new ServerConfig();
        }
        return instance;
    }

    private Map<String, Object> properties = null;

    private ServerConfig() {

    }

    public boolean initConfig() {
        if (null != properties)
            return true;
        try {
            File dumpFile = new File(RedisDao.class.getClassLoader().getResource("config.yaml").getPath());
            properties = new Yaml().load(new FileInputStream(dumpFile));

            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }

    }

    public Map<String, Object> getConfigProperties() {
        return properties;
    }
}