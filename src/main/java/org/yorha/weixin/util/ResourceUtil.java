package org.yorha.weixin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Description
 * @Author aria
 * @Date 18-11-25
 * @Project_name weixin
 */
public class ResourceUtil {

    /*
     * @Description:获解析esources中的配置文件
     * @Param: [fileName, propertyName]
     * @return: java.lang.String
     * @Author:  aria
     * @Date: 下午10:14
     */
    public static String getProperty(String fileName,String propertyName) throws IOException {
        String path=Thread.currentThread().getContextClassLoader().getResource("/").getPath();
        InputStream in = new FileInputStream(new File(path+fileName+".properties"));
        Properties properties = new Properties();
        properties.load(in);
        return properties.getProperty(propertyName);
    }

}
