package version1.com.tangcheng.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by tc on 2016/4/23.
 */
public class DownLoadUtil {
    public static void download(String urlstr,String path){
        try {
            URL url = new URL(urlstr);
            URLConnection conn = url.openConnection();
            FileUtils.copyInputStreamToFile(conn.getInputStream(),new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
