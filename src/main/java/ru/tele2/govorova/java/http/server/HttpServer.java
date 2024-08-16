package ru.tele2.govorova.java.http.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private int port;
    private Dispatcher dispatcher;
    private ExecutorService executorService;
    private ThreadLocal<byte[]> requestBuffer;
    private int DEFAULT_BUFFER_SIZE = 8192;
    private static final Logger logger = LogManager.getLogger(HttpServer.class);

    public HttpServer(int port) {
        this.port = port;
        this.dispatcher = new Dispatcher();
    }

    public void start() {
        executorService = Executors.newFixedThreadPool(5);
        requestBuffer = ThreadLocal.withInitial(() -> new byte[DEFAULT_BUFFER_SIZE]);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Сервер запущен на порту: ", port);
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.execute(() -> executeRequest(socket));
            }
        } catch (IOException e) {
            logger.error("Wrong server action.", e);
        } finally {
            executorService.shutdown();
        }
    }

    private void executeRequest(Socket socket) {
        try {
            byte[] buffer = requestBuffer.get();
            int n = socket.getInputStream().read(buffer);
            if (n > 0) {
                String rawRequest = new String(buffer, 0, n);
                HttpRequest request = new HttpRequest(rawRequest);
                request.printInfo();
                dispatcher.execute(request, socket.getOutputStream());
                socket.getOutputStream().flush();
            }
        } catch (Exception e) {
            logger.error("Error while processing request.", e);
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                logger.error("Socket close error", e);
            }
        }
    }
}
