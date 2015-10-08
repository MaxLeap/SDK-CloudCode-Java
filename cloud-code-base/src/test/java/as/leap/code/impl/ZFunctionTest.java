package as.leap.code.impl;

import as.leap.code.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by stream.
 */
public class ZFunctionTest {

  private DefineFunction functions;
  private String functionName = "functionName";

  @Before
  public void before() {
    functions = new DefineFunction();
  }

  @Test
  public void functionWithoutParams() {
    functions.define(functionName, new LASHandler<Request, Response<String>>() {
      @Override
      public Response<String> handle(Request request) {
        Response<String> response = new LASResponse<String>(String.class);
        response.setResult("world.");
        return response;
      }
    });
    Request request = new LASRequest(null,null);
    //invoke defined function.
    Response response = functions.getHandler(functionName).handle(request);
    Assert.assertTrue(response.succeeded());
    Assert.assertEquals("world.", response.getResult());
  }

  @Test
  public void functionWithUserPrincical(){
    functions.define(functionName, new LASHandler<Request, Response>() {
      @Override
      public Response handle(Request request) {
        UserPrincipal userPrincipal = request.getUserPrincipal();
        System.out.println(userPrincipal);
        Response<String> response = new LASResponse<String>(String.class);
        response.setResult("world.");
        return response;
      }
    });
    UserPrincipal userPrincipal = new UserPrincipal();
    userPrincipal.setAppId("appId");
    userPrincipal.setIdentityType(IdentityType.MASTER_KEY);
    userPrincipal.setKey("masterKey");
    Response response = functions.getHandler(functionName).handle(new LASRequest(null, userPrincipal));
    Assert.assertTrue(response.succeeded());
    Assert.assertEquals("world.", response.getResult());
  }

  @Test
  public void functionWithFail() {
    functions.define(functionName, new LASHandler<Request, Response>() {
      @Override
      public Response handle(Request request) {
        Response<String> response = new LASResponse<String>(String.class);
        response.setError("fail");
        return response;
      }
    });

    Request request = new LASRequest(null,null);
    //invoke defined function.
    Response response = functions.getHandler(functionName).handle(request);
    Assert.assertFalse(response.succeeded());
    Assert.assertEquals("fail", response.getError());

  }

  @Test
  public void functionWithInteger() {
    functions.define(functionName, new LASHandler<Request, Response<Integer>>() {
      @Override
      public Response<Integer> handle(Request request) {
        int value = request.parameter(int.class);
        Assert.assertEquals(100, value);
        Response<Integer> response = new LASResponse<Integer>(int.class);
        response.setResult(101);
        return response;
      }
    });
    final Request request = new LASRequest("100",null);
    //invoke defined function.
    Response response = functions.getHandler(functionName).handle(request);
    Assert.assertTrue(response.succeeded());
    Assert.assertEquals(101, response.getResult());
  }

  @Test
  public void functionsWithPOJOs() throws Exception {
    Book queryBook = new Book();
    queryBook.setId(100);
    queryBook.setName("name");
    queryBook.setAuthor("stream");

    functions.define(functionName, new LASHandler<Request, Response<List<Book>>>() {
      @Override
      public Response<List<Book>> handle(Request request) {
        Book book = request.parameter(Book.class);
        Assert.assertEquals(book.getId(), 100);
        Assert.assertEquals(book.getName(), "name");
        Assert.assertEquals(book.getAuthor(), "stream");

        List<Book> books = new ArrayList<Book>();
        books.add(book);
        Response<List<Book>> response = new LASResponse<List<Book>>(Book.class, true);
        response.setResult(books);
        return response;
      }
    });

    Map<String, Object> bookJson = new HashMap<String, Object>();
    bookJson.put("id", 100);
    bookJson.put("name", "name");
    bookJson.put("author", "stream");

    String bookJsonStr = LASJsonParser.asJson(bookJson);
    Request request = new LASRequest(bookJsonStr,null);
    //invoke defined function.
    Response response = functions.getHandler(functionName).handle(request);
    String responseJsonStr = LASJsonParser.asJson(response.getResult());
    List<Book> bookList = LASJsonParser.asObject(responseJsonStr, ((LASResponse) response).getResultType());
    Assert.assertEquals(1, bookList.size());
    Book book = bookList.get(0);
    Assert.assertEquals(book.getId(), 100);
    Assert.assertEquals(book.getName(), "name");
    Assert.assertEquals(book.getAuthor(), "stream");
  }

  /**
   * Java POJO
   * should implements interface of serializable.
   */
  private static class Book implements Serializable {
    private int id;
    private String name;
    private String author;

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getAuthor() {
      return author;
    }

    public void setAuthor(String author) {
      this.author = author;
    }
  }


}
