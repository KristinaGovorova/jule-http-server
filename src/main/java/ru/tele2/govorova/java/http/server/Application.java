package ru.tele2.govorova.java.http.server;

public class Application {
    public static void main(String[] args) {
        new HttpServer(8189).start();
    }
}