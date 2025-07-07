package cn.chengzhiya.mhdflibrary.manager;

import cn.chengzhiya.mhdflibrary.exception.DownloadException;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public final class HttpManager {
    private Proxy proxy = Proxy.NO_PROXY;
    private int timeout = (int) TimeUnit.SECONDS.toMillis(20);

    /**
     * 通过URL地址打开URL连接
     *
     * @param url URL地址
     * @return URL连接
     */
    @SneakyThrows
    public URLConnection openConnection(String url) {
        URLConnection connection = new URL(url).openConnection(getProxy());
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);

        return connection;
    }

    /**
     * 通过URL连接下载文件
     *
     * @param connection URL连接
     * @return 文件数据
     */
    public byte[] downloadFile(URLConnection connection) throws DownloadException {
        try {
            try (InputStream in = connection.getInputStream()) {
                byte[] bytes = in.readAllBytes();
                if (bytes.length == 0) {
                    throw new DownloadException("无可下载文件");
                }
                return bytes;
            }
        } catch (Exception e) {
            throw new DownloadException(e);
        }
    }

    /**
     * 通过URL地址下载文件
     *
     * @param url URL地址
     * @return 文件数据
     */
    public byte[] downloadFile(String url) throws DownloadException {
        return downloadFile(openConnection(url));
    }

    /**
     * 通过URL连接下载并保存文件
     *
     * @param connection URL连接
     * @param savePath   保存目录
     */
    public void downloadFile(URLConnection connection, Path savePath) throws DownloadException {
        try {
            Files.write(savePath, downloadFile(connection));
        } catch (IOException e) {
            if (savePath.toFile().exists()) {
                savePath.toFile().delete();
            }
            throw new DownloadException(e);
        }
    }

    /**
     * 通过URL地址下载并保存文件
     *
     * @param url      URL地址
     * @param savePath 保存目录
     */
    public void downloadFile(String url, Path savePath) throws DownloadException {
        downloadFile(openConnection(url), savePath);
    }
}
