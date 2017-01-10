package wxm.com.firstaid.util;

/**
 * Created by Zero on 12/19/2016.
 */

public class Utils {
    public static String getSex(int sex){
        if (sex==0){
            return "女";
        }else {
            return "男";
        }
    }
    public static int getSexIndex(String sex){
        if ("男".equals(sex)){
            return 1;
        }else {
            return 0;
        }
    }
}
