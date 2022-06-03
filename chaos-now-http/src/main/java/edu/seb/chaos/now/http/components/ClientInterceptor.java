package edu.seb.chaos.now.http.components;

import edu.seb.chaos.now.http.util.AssaultUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.stereotype.Component;

import java.io.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientInterceptor implements ClientHttpRequestInterceptor {

    private final AssaultUtils assaultUtils;
    private final Assault assault;


    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        boolean hasExceptionOccured = false;
        ClientHttpResponse response = execution.execute(request, body);
        InputStream inputStream = response.getBody();
        String newBody = null;
        try {
            newBody = this.assault.attack(this.assaultUtils.fromInputStreamToString(inputStream));
        } catch (IOException e) {
            log.error("Chaos-Now-HTTP couldn't modify Body.");
            hasExceptionOccured = true;
        }
        if (!hasExceptionOccured && newBody != null) {
            ClientHttpResponse newResponse = this.assaultUtils.createNewResponse(response, this.assaultUtils.fromStringToInputStream(newBody));
            inputStream.close();
            return newResponse;
        }
        return response;
    }
}
