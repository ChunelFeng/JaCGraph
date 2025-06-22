package cgraph.jacgraph.utils;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * @author Choo.lch
 * @date 2025/6/2 20:40
 * @Description:
 */
public class DateUtils {

    public static String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    public static String toLocalString(String format){
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 定义时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

        // 格式化当前时间
        return now.format(formatter);
    }
}
