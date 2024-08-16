package ru.tele2.govorova.java.http.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class HttpRequest {
    private String rawRequest;
    private String uri;
    private HttpMethod method;
    private Map<String, String> parameters = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private String body;

    private static final Logger logger = LogManager.getLogger(HttpRequest.class.getName());

    public String getBody() {
        return body;
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public boolean containsParameter(String key) {
        return parameters.containsKey(key);
    }


    public String getRoutingKey() {
        return method + " " + uri;
    }

    public String getUri() {
        return uri;
    }

    public HttpRequest(String rawRequest) {
        this.rawRequest = rawRequest;
        this.parse();
    }

    private void parse() {
        int startIndex = rawRequest.indexOf(' ');
        int endIndex = rawRequest.indexOf(' ', startIndex + 1);
        this.uri = rawRequest.substring(startIndex + 1, endIndex);
        this.method = HttpMethod.valueOf(rawRequest.substring(0, startIndex));
        parameters = new HashMap<>();

        if (uri.contains("?")) {
            String[] elements = uri.split("[?]");
            uri = elements[0];
            String[] keysValues = elements[1].split("&");
            for (String o : keysValues) {
                String[] keyValue = o.split("=");
                parameters.put(keyValue[0], keyValue[1]);
            }
        }

        int headerStart = rawRequest.indexOf("\r\n") + 2;
        int headerEnd = rawRequest.indexOf("\r\n\r\n");
        String headerPart = rawRequest.substring(headerStart, headerEnd);
        String[] headerArr = headerPart.split("\r\n");

        for (String line : headerArr) {
            int index = line.indexOf(":");
            if (index != -1) {
                String headerName = line.substring(0, index).trim();
                String headerValue = line.substring(index + 1).trim();
                headers.put(headerName, headerValue);
            }
        }

        if (method == HttpMethod.POST) {
            this.body = rawRequest.substring(
                    rawRequest.indexOf("\r\n\r\n") + 4
            );
            logger.info("Parsed body: " + this.body);
        }
    }

    public void printInfo() {
        logger.info("uri: " + uri);
        logger.info("method: " + method);
        logger.info("body: " + body);
        logger.info("Headers:" + headers);
    }
}
