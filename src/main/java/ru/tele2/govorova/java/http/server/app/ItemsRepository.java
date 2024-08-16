package ru.tele2.govorova.java.http.server.app;

import java.math.BigDecimal;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ItemsRepository {
    private List<Item> items;
    private static final Logger logger = LogManager.getLogger(ItemsRepository.class.getName());


    public ItemsRepository() {
        this.items = new ArrayList<>(Arrays.asList(
                new Item(1L, "Milk", BigDecimal.valueOf(80)),
                new Item(2L, "Bread", BigDecimal.valueOf(32)),
                new Item(3L, "Cheese", BigDecimal.valueOf(320))
        ));
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public Item add(Item item) {
        Long newId = items.stream().mapToLong(Item::getId).max().orElse(0L) + 1L;
        item.setId(newId);
        items.add(item);
        return item;
    }

    public Boolean delete(Long id) {
        for (Item item : items) {
            if (Objects.equals(item.getId(), id)) {
                items.remove(item);
                logger.debug("Item with id = {} was deleted", id);
                return true;
            }
        }
        logger.debug("Item with id = {} was not deleted", id);
        return false;
    }
}
