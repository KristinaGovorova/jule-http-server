package ru.tele2.govorova.java.http.server.processors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.tele2.govorova.java.http.server.BadRequestException;
import ru.tele2.govorova.java.http.server.HttpRequest;
import ru.tele2.govorova.java.http.server.app.ItemsRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class DeleteItemProcessor implements RequestProcessor {
    private static final Logger logger = LogManager.getLogger(DeleteItemProcessor.class.getName());
    private ItemsRepository itemsRepository;

    public DeleteItemProcessor(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Override
    public void execute(HttpRequest request, OutputStream out) throws IOException {
        if (!request.containsParameter("id")) {
            logger.error("Parameter Id is missing in URI");
            throw new BadRequestException("Parameter Id is missing in URI");
        }
        Long idToDelete;
        try {
            idToDelete = Long.parseLong(request.getParameter("id"));
        } catch (NumberFormatException e){
            logger.error("Wrong type for idToDelete");
            throw new NumberFormatException("Wrong type for idToDelete");
        }
        if (itemsRepository.delete(idToDelete)) {
            String response = "" +
                    "HTTP/1.1 204 No Content\r\n" +
                    "\r\n";
            out.write(response.getBytes(StandardCharsets.UTF_8));
            logger.info("Element with id {} successfully deleted", idToDelete);

        } else {
            String response = "" +
                    "HTTP/1.1 204 No Content\r\n" +
                    "\r\n";
            out.write(response.getBytes(StandardCharsets.UTF_8));
            logger.info("Element with id {} is not exist", idToDelete);
        }
    }
}
