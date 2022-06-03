package edu.seb.chaos.now.http.util;

import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class AssaultUtils {

    public AssaultUtils() {}

    public InputStream fromByteArrayToInputStream(byte[] bytes) throws IOException {
        return ByteSource.wrap(bytes).openStream();
    }

    public byte[] fromInputStreamToByteArray(InputStream stream) throws IOException {
        return ByteStreams.toByteArray(stream);
    }

    public String fromByteArrayToString(byte[] bytes) {
        return new String(bytes);
    }

    public String fromInputStreamToString(InputStream stream) throws IOException {
        return fromByteArrayToString(fromInputStreamToByteArray(stream));
    }

    public InputStream fromStringToInputStream(String string) throws IOException {
        return CharSource.wrap(string).asByteSource(StandardCharsets.UTF_8).openStream();
    }

    /**
     * Creates new "wrapper" on top of old ClientHttpResponse object and replaces the Body with the provided
     * InputStream.
     * @param response old object to be wrapped.
     * @param inputStream new body to be added on wrapper.
     * @return Wrapped ClientHttpResponse object.
     */
    public ClientHttpResponse createNewResponse(ClientHttpResponse response, InputStream inputStream) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return response.getStatusCode();
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return response.getRawStatusCode();
            }

            @Override
            public String getStatusText() throws IOException {
                return response.getStatusText();
            }

            @Override
            public void close() {
                try {
                    this.getBody().close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public InputStream getBody() throws IOException {
                return inputStream;
            }

            @Override
            public HttpHeaders getHeaders() {
                return response.getHeaders();
            }
        };
    }
}
