package handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import database.Store;
import http.HttpMethod;
import http.HttpResponse;
import logging.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Handler implements HttpHandler {

    protected HttpMethod method;
    protected Map<String, String> params;
    protected HttpExchange exchange;

    private Store store;

    public void setStore(Store store) {
        this.store = store;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        method = HttpMethod.valueOf(exchange.getRequestMethod());

        Logger.debug("Incoming " + method + " " + exchange.getRequestURI());

        params = extractQuery(exchange.getRequestURI());
        this.exchange = exchange;

        try {
            String handler = getClass().getSimpleName();
            Logger.debug("Handling request with " + handler);

            HttpResponse response = switch (method) {
                case POST -> post();
                case PUT -> put();
                case DELETE -> delete();
                default -> get();
            };

            reply(response.code(), response.body());
        } catch (Exception ex) {
            Logger.warn(ex, "Exception while handling request");
            reply(500, ex.getMessage());
        }
    }

    abstract protected HttpResponse get() throws IOException;
    abstract protected HttpResponse post() throws IOException;
    abstract protected HttpResponse put() throws IOException;
    abstract protected HttpResponse delete() throws IOException;

    protected Store getStore() {
        return store;
    }

    protected String getParameter(String name) {
        return params.get(name);
    }

    protected HttpResponse ok(String body) {
        return new HttpResponse(200, body);
    }

    protected HttpResponse ok(Object object) {
        try {
            String json = new ObjectMapper().writeValueAsString(object);
            return ok(json);
        } catch (Exception ex) {
            Logger.warn(ex, "Exception while serializing object");
            return error(ex.getMessage());
        }
    }

    protected HttpResponse conflict(String body) {
        return new HttpResponse(400, body);
    }

    protected HttpResponse conflict(Object object) {
        try {
            String json = new ObjectMapper().writeValueAsString(object);
            return conflict(json);
        } catch (Exception ex) {
            Logger.warn(ex, "Exception while serializing object");
            return error(ex.getMessage());
        }
    }

    protected HttpResponse error(String body) {
        return new HttpResponse(500, body);
    }

    protected HttpResponse error(Object object) {
        try {
            String json = new ObjectMapper().writeValueAsString(object);
            return error(json);
        } catch (Exception ex) {
            Logger.warn(ex, "Exception while serializing object");
            return error(ex.getMessage());
        }
    }

    private void reply(int status, String response) throws IOException {
        exchange.sendResponseHeaders(status, response.length());
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseBody().write(response.getBytes());
        exchange.getResponseBody().close();
    }


    private Map<String, String> extractQuery(URI url) {
        Map<String, String> query_pairs = new LinkedHashMap<>();
        String query = url.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8), URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8));
        }
        return query_pairs;
    }
}
