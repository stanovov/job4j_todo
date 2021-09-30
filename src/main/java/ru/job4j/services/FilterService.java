package ru.job4j.services;

import ru.job4j.model.Item;
import ru.job4j.model.Priority;
import ru.job4j.store.HbmStore;
import ru.job4j.store.Store;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

public class FilterService {

    private final Map<Integer, Supplier<Collection<Item>>> filters;

    private final Store store = HbmStore.instOf();

    private final Priority highPriority = store.findHighestPriority();

    private FilterService() {
        filters = Map.of(
                1, () -> store.findAllItems(),
                2, () -> store.findItemsByDone(true),
                3, () -> store.findItemsByDone(false),
                4, () -> store.findItemsByPriority(highPriority)
        );
    }

    private static final class Lazy {
        private static final FilterService INST = new FilterService();
    }


    public static FilterService instOf() {
        return FilterService.Lazy.INST;
    }


    public Collection<Item> getItemsByFilterId(int filterId) {
        Supplier<Collection<Item>> supplier = filters.get(filterId);
        if (supplier == null) {
            throw new IllegalArgumentException("Filters up to and including ID 4 are currently supported.");
        }
        return supplier.get();
    }
}
