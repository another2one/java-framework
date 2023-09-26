package com.framework;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author lizhi
 */
public class ReServletResponse extends HttpServletResponseWrapper {

    private ByteArrayOutputStream output = new ByteArrayOutputStream();

    public ReServletResponse(HttpServletResponse response) {
        super(response);
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            public boolean isReady() {
                return true;
            }

            public void setWriteListener(WriteListener listener) {
            }

            // 实际写入ByteArrayOutputStream:
            public void write(int b) throws IOException {
                output.write(b);
            }
        };
    }

    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(output, false, StandardCharsets.UTF_8);
    }

    public byte[] getContent() {
        return output.toByteArray();
    }
}
