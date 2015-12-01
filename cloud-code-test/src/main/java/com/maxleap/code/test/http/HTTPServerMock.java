package com.maxleap.code.test.http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by stream.
 */
public class HTTPServerMock {

  public static void main(String[] args) throws Exception {
    //初始化cloudcode main

    Server server = new Server(8081);
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);

    context.addServlet(new ServletHolder(new HelloServlet()), "/*");
    context.addServlet(new ServletHolder(new HelloServlet("Buongiorno Mondo")), "/it/*");
    context.addServlet(new ServletHolder(new HelloServlet("Bonjour le Monde")), "/fr/*");

    server.start();
    server.join();
  }

  private static class HelloServlet extends HttpServlet {
    private String greeting = "Hello World";

    public HelloServlet() {
    }

    public HelloServlet(String greeting) {
      this.greeting = greeting;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("text/html");
      response.setStatus(HttpServletResponse.SC_OK);
      response.getWriter().println("<h1>" + greeting + "</h1>");
      //接收到请求后直接调用相关 function handler
    }
  }

}
