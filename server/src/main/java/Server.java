import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import database.Store;
import handlers.Handler;
import logging.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Server {
    private final HttpServer server;
    private final Store store;

    public Server(int port) throws IOException {
        try {
            this.server = HttpServer.create(new InetSocketAddress(port), 0);
            this.server.setExecutor(Executors.newCachedThreadPool());


            this.store = new Store();
        } catch (IOException ex) {
            Logger.error(ex, "Could not create server");
            throw ex;
        }
    }

    public Server route(String path, Handler handler) {
        try {
            this.server.createContext(path, handler);
            handler.setStore(this.store);
            Logger.debug("Added handler for " + path);
            return this;
        } catch (Exception ex) {
            Logger.warn(ex, "Could not add handler for " + path);
            return this;
        }
    }

    public void start() {
        server.start();
        Logger.info("Server running on port " + server.getAddress().getPort());
    }

    public void stop() {
        server.stop(0);
    }
}
