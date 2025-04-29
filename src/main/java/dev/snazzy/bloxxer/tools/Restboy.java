package dev.snazzy.bloxxer.tools;

import com.sun.net.httpserver.HttpServer;
import dev.snazzy.bloxxer.rest.AuthenticatedHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Restboy {
    private final HttpServer httpServer;

    public Restboy(int port) throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

        this.httpServer.setExecutor(Executors.newFixedThreadPool(4));
    }

    public void start() {
        // Start the server
       this.httpServer.start();
    }

    public void stop() {
        this.httpServer.stop(0);
    }

    public HttpServer getHttpServer() {
        return this.httpServer;
    }

    public void createContext(String path, AuthenticatedHandler restHandler) {
        this.httpServer.createContext(path, restHandler);
    }
}
