package com.framework;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.*;

/**
 * @author lizhi
 */
public class ReServletRequest extends HttpServletRequestWrapper {
    private byte[] body;
    public ReServletRequest(HttpServletRequest request, byte[] body) {
        super(request);
        this.body = body;
    }

    public ServletInputStream getInputStream() {
        return new ServletInputStream() {
            private int offset = 0;

            public boolean isFinished() {
                return offset >= body.length;
            }

            public boolean isReady() {
                return true;
            }

            public void setReadListener(ReadListener listener) {
            }

            public int read() {
                if (offset >= body.length) {
                    return -1;
                }
                int n = body[offset] & 0xff;
                offset++;
                return n;
            }
        };
    }

    public BufferedReader getReader() throws UnsupportedEncodingException {
        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(body), "UTF-8"));
    }
}
