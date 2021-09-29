package ru.job4j.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.model.Item;
import ru.job4j.model.Priority;
import ru.job4j.store.HbmStore;
import ru.job4j.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

public class ItemsServlet extends HttpServlet {

    private static final Gson GSON = new GsonBuilder().create();

    private final Priority highPriority = HbmStore.instOf().findHighestPriority();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");
        OutputStream output = resp.getOutputStream();
        int filterId = Integer.parseInt(req.getParameter("filter_id"));
        Store store = HbmStore.instOf();
        Collection<Item> items = List.of();
        if (filterId == 1) {
            items = store.findAllItems();
        } else if (filterId == 2) {
            items = store.findItemsByDone(true);
        } else if (filterId == 3) {
            items = store.findItemsByDone(false);
        } else if (filterId == 4) {
            items = store.findItemsByPriority(highPriority);
        }
        String json = GSON.toJson(items);
        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                resp.getOutputStream(), StandardCharsets.UTF_8));
        Item item = GSON.fromJson(req.getReader(), Item.class);
        HbmStore.instOf().saveItem(item);
        if (item.getId() == 0) {
            writer.print("409 Conflict");
            writer.flush();
            return;
        }
        writer.print("200 OK");
        writer.flush();
    }
}
