import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import servlets.VerbServlet;

public class JettyServer {

    public static void main(String[] args) throws Exception {
        // Создание контекста Spring
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(AppConfig.class);
        context.refresh();

        // Настройка Jetty сервера
        Server server = new Server(8080);

        // Создание контекста сервлетов
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        server.setHandler(contextHandler);

        // Регистрация Spring DispatcherServlet
        ServletHolder servletHolder = new ServletHolder(new DispatcherServlet(context));
        contextHandler.addServlet(servletHolder, "/api/*");

        // Регистрация вашего VerbServlet
        ServletHolder verbServletHolder = new ServletHolder(new VerbServlet());
        contextHandler.addServlet(verbServletHolder, "/verbs");

        // Запуск сервера
        server.start();
        server.join();
    }
}
