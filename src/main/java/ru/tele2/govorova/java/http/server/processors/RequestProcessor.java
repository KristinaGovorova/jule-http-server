package ru.tele2.govorova.java.http.server.processors;

import ru.tele2.govorova.java.http.server.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;

public interface RequestProcessor {
    void execute(HttpRequest request, OutputStream out) throws IOException;
}