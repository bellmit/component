package com.lyloou.component.file.ali;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {
    private static final int DEFAULT_BUFFER_SIZE = 4096;

    private IOUtils() {
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        byte[] var4;
        try {
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

            while (true) {
                int n;
                if (-1 == (n = input.read(buffer))) {
                    var4 = output.toByteArray();
                    break;
                }

                output.write(buffer, 0, n);
            }
        } catch (Throwable var6) {
            try {
                output.close();
            } catch (Throwable var5) {
                var6.addSuppressed(var5);
            }

            throw var6;
        }

        output.close();
        return var4;
    }
}
