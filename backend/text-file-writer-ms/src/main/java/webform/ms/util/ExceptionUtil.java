package webform.ms.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ExceptionUtil {
    public static String getFromProp(String path, String key, String defMsg){
        ResourceBundle bundle = null;
        try {
            bundle = ResourceBundle.getBundle(path);
        } catch (MissingResourceException e) {
//            System.out.println("PropertiesFile [" + path + "] not found.");
//            e.printStackTrace();
            return defMsg;
        }
        String value = null;
        try {
            value = bundle.getString(key);
        } catch (MissingResourceException e) {
//            System.out.println("Key [" + key + "] not found.");
//            e.printStackTrace();
            return defMsg;
        }
        return value;
    }
}
