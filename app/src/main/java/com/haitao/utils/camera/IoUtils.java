package com.haitao.utils.camera;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by pxl(彭小利) on 2015/9/29.
 */
public class IoUtils {
    /**
     * 关闭流
     *
     * @param stream 可关闭的流
     */
    public static void closeStream(Closeable stream) {
        try {
            if (stream != null)
                stream.close();
        } catch (IOException e) {

        }
    }

}
