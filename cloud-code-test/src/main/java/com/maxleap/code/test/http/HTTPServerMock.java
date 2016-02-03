package com.maxleap.code.test.http;

import com.maxleap.code.Response;
import com.maxleap.code.test.framework.TestCloudCode;
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

  public HTTPServerMock(TestCloudCode testCloudCode) throws Exception {
    this(8085, testCloudCode);
  }

  public HTTPServerMock(int port, TestCloudCode testCloudCode) throws Exception {
    Server server = new Server(port);
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);

    context.addServlet(new ServletHolder(new FunctionServlet(testCloudCode)), "/functions/*");
    context.addServlet(new ServletHolder(new JobServlet(testCloudCode)), "/jobs/*");

    server.start();
    System.out.println("http server have startup on port " + port);
    server.join();
  }

  private class FunctionServlet extends HttpServlet {
    private TestCloudCode testCloudCode;

    public FunctionServlet(TestCloudCode testCloudCode) {
      this.testCloudCode = testCloudCode;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
      StringBuilder sb = new StringBuilder();
      String s;
      while ((s = req.getReader().readLine()) != null) sb.append(s);
      response.setCharacterEncoding("UTF-8");
      response.setStatus(HttpServletResponse.SC_OK);
      String name = req.getPathInfo().split("/")[1];
      Response result = testCloudCode.runFunction(name, sb.toString());
      if (result.succeeded()) response.getWriter().println(result.getResult());
      else {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(400);
        response.getWriter().println("{\"errorCode\":109998,\"errorMessage\":\""+result.getError()+"\"}");
      }
    }
  }

  private class JobServlet extends HttpServlet {
    private TestCloudCode testCloudCode;

    public JobServlet(TestCloudCode testCloudCode) {
      this.testCloudCode = testCloudCode;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
      StringBuilder sb = new StringBuilder();
      String s;
      while ((s = req.getReader().readLine()) != null) sb.append(s);

      response.setStatus(HttpServletResponse.SC_OK);
      String name = req.getPathInfo().split("/")[1];

      testCloudCode.runJob(name, sb.toString());
    }
  }

}
