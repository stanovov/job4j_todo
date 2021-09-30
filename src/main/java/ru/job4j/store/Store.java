package ru.job4j.store;

import ru.job4j.model.Filter;
import ru.job4j.model.Item;
import ru.job4j.model.Priority;
import ru.job4j.model.User;

import java.util.Collection;

public interface Store extends AutoCloseable {
    Collection<Priority> findAllPriorities();
    Priority findHighestPriority();
    Collection<Item> findAllItems(User user);
    Collection<Item> findItemsByDone(User user, boolean done);
    Collection<Item> findItemsByPriority(User user, Priority priority);
    void saveItem(Item item);
    void deleteItem(Integer id);
    Collection<Filter> findAllFilters();
    User findUserByUsername(String username);
    void saveUser(User user);
}
