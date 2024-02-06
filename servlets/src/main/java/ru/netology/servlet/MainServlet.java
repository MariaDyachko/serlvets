package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {

    private static final String request = "/api/posts/";
    private static final String methodGet = "GET";
    private static final String methodPost = "POST";
    private static final String methodDelete = "DELETE";
    private static final String slash = "/";

    private PostController controller;

    @Override
    public void init() {
        final var context = new AnnotationConfigApplicationContext("ru.netology");
        controller = context.getBean(PostController.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            // primitive routing
            if (method.equals(methodGet) && path.equals(request)) {
                controller.all(resp);
                return;
            }
            if (method.equals(methodGet) && path.matches(request + "\\d+")) {
                // easy way
                final long id = Long.parseLong(path.substring(path.lastIndexOf(slash)+1));
                controller.getById(id, resp);
                return;
            }
            if (method.equals(methodPost) && path.equals(request)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(methodDelete) && path.matches(request + "\\d+")) {
                // easy way
                final var id = Long.parseLong(path.substring(path.lastIndexOf(slash)+1));
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}


