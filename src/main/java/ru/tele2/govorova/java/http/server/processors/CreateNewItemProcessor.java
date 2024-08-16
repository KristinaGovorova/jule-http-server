package ru.tele2.govorova.java.http.server.processors;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.JsonParseException;
import ru.tele2.govorova.java.http.server.BadRequestException;
import ru.tele2.govorova.java.http.server.HttpRequest;
import ru.tele2.govorova.java.http.server.app.Item;
import ru.tele2.govorova.java.http.server.app.ItemsRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CreateNewItemProcessor implements RequestProcessor {
    private ItemsRepository itemsRepository;
    private static final Logger logger = LogManager.getLogger(CreateNewItemProcessor.class.getName());

    public CreateNewItemProcessor(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Override
    public void execute(HttpRequest request, OutputStream out) throws IOException {
        try {
            Gson gson = new Gson();
            Item item = itemsRepository.add(gson.fromJson(request.getBody(), Item.class));
            String itemJson = gson.toJson(item);
            String response = "" +
                    "HTTP/1.1 201 Created\r\n" +
                    "Content-Type: application/json\r\n" +
                    "\r\n" +
                    itemJson;
            out.write(response.getBytes(StandardCharsets.UTF_8));
        } catch (JsonParseException e) {
            logger.error("Wrong JSON format", e);
            throw new BadRequestException("Некорректный формат входящего JSON объекта");
        }
    }
}
