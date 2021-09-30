package ru.job4j.store;

import ru.job4j.model.Filter;
import ru.job4j.model.Item;
import ru.job4j.model.Priority;

import java.util.Collection;

public interface Store extends AutoCloseable {
    Collection<Priority> findAllPriorities();
    Priority findHighestPriority();
    Collection<Item> findAllItems();
    Collection<Item> findItemsByDone(boolean done);
    Collection<Item> findItemsByPriority(Priority priority);
    void saveItem(Item item);
    void deleteItem(Integer id);
    Collection<Filter> findAllFilters();
}
