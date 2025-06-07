package cn.chengzhiya.mhdflibrary.manager;

public interface LoggerManager {
    /**
     * 打印日志
     *
     * @param string 日志消息
     */
    default void log(String string) {
        System.out.println(string);
    }
}
