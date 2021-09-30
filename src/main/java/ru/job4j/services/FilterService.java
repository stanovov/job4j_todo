package ru.job4j.services;

import ru.job4j.model.Item;
import ru.job4j.model.Priority;
import ru.job4j.model.User;
import ru.job4j.store.HbmStore;
import ru.job4j.store.Store;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public class FilterService {

    private final Map<Integer, Function<User, Collection<Item>>> filters;

    private final Store store = HbmStore.instOf();

    private final Priority highPriority = store.findHighestPriority();

    private FilterService() {
        filters = Map.of(
                1, (user) -> store.findAllItems(user),
                2, (user) -> store.findItemsByDone(user, true),
                3, (user) -> store.findItemsByDone(user, false),
                4, (user) -> store.findItemsByPriority(user, highPriority)
        );
    }

    private static final class Lazy {
        private static final FilterService INST = new FilterService();
    }


    public static FilterService instOf() {
        return FilterService.Lazy.INST;
    }


    public Collection<Item> getItemsByFilterId(int filterId, int userId) {
        User user = new User();
        user.setId(userId);
        Function<User, Collection<Item>> supplier = filters.get(filterId);
        if (supplier == null) {
            throw new IllegalArgumentException("Filters up to and including ID 4 are currently supported.");
        }
        return supplier.apply(user);
    }
}
