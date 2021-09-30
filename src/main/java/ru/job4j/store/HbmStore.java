package ru.job4j.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.model.Filter;
import ru.job4j.model.Item;
import ru.job4j.model.Priority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class HbmStore implements Store {

    private static final Logger LOG = LoggerFactory.getLogger(HbmStore.class.getName());

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();

    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private HbmStore() {
    }

    private static final class Lazy {
        private static final Store INST = new HbmStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Priority> findAllPriorities() {
        List<Priority> list = new ArrayList<>();
        String hql = "FROM ru.job4j.model.Priority ORDER BY value DESC";
        try {
            list = executeTransaction(session -> session.createQuery(hql).list());
        } catch (Exception e) {
            LOG.error("Database query failed. FIND ALL PRIORITIES", e);
        }
        return list;
    }

    @Override
    public Priority findHighestPriority() {
        Object result = null;
        String hql = "FROM ru.job4j.model.Priority ORDER BY value";
        try {
            result = executeTransaction(session -> session.createQuery(hql)
                    .setMaxResults(1)
                    .uniqueResult());
        } catch (Exception e) {
            LOG.error("Database query failed. FIND HIGHEST PRIORITY", e);
        }
        return result == null ? null : (Priority) result;
    }

    @Override
    public Collection<Item> findAllItems() {
        List<Item> list = new ArrayList<>();
        String hql = "from ru.job4j.model.Item ORDER BY created";
        try {
            list = executeTransaction(session -> session.createQuery(hql).list());
        } catch (Exception e) {
            LOG.error("Database query failed. FIND ALL ITEMS", e);
        }
        return list;
    }

    @Override
    public Collection<Item> findItemsByDone(boolean done) {
        List<Item> list = new ArrayList<>();
        String hql = "from ru.job4j.model.Item WHERE done = :pDone ORDER BY created";
        try {
            list = executeTransaction(session -> session.createQuery(hql)
                    .setParameter("pDone", done)
                    .list());
        } catch (Exception e) {
            LOG.error("Database query failed. FIND ITEMS BY DONE", e);
        }
        return list;
    }

    @Override
    public Collection<Item> findItemsByPriority(Priority priority) {
        List<Item> list = new ArrayList<>();
        String hql = "from ru.job4j.model.Item WHERE priority = :pPriority ORDER BY created";
        try {
            list = executeTransaction(session -> session.createQuery(hql)
                    .setParameter("pPriority", priority)
                    .list());
        } catch (Exception e) {
            LOG.error("Database query failed. FIND ITEMS BY DONE", e);
        }
        return list;
    }

    @Override
    public void saveItem(Item item) {
        try {
            executeTransaction(session -> {
                if (item.getId() == 0) {
                    session.save(item);
                } else {
                    session.update(item);
                }
                return true;
            });
        } catch (Exception e) {
            LOG.error("Database query failed. SAVE ITEM", e);
        }
    }

    @Override
    public void deleteItem(Integer id) {
        try {
            executeTransaction(session -> {
                Item item = new Item();
                item.setId(id);
                session.delete(item);
                return true;
            });
        } catch (Exception e) {
            LOG.error("Database query failed. DELETE ITEM", e);
        }
    }

    @Override
    public Collection<Filter> findAllFilters() {
        List<Filter> list = new ArrayList<>();
        String hql = "FROM ru.job4j.model.Filter ORDER BY id";
        try {
            list = executeTransaction(session -> session.createQuery(hql).list());
        } catch (Exception e) {
            LOG.error("Database query failed. FIND ALL FILTERS", e);
        }
        return list;
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    private <T> T executeTransaction(Function<Session, T> f) {
        T result;
        Session session = sf.openSession();
        session.beginTransaction();
        result = f.apply(session);
        session.getTransaction().commit();
        session.close();
        return result;
    }
}
