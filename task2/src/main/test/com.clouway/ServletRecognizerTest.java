package com.clouway;

import org.junit.Before;
import org.junit.Test;


import java.io.ByteArrayOutputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by com.clouway on 16.05.16.
 */
public class ServletRecognizerTest {

  private FakeRequest fakeRequest;
  private FakeResponse fakeResponse;
  private RecognizerServlet recognizerServlet;


  @Before
  public void setUp(){
    recognizerServlet=new RecognizerServlet();
    fakeRequest=new FakeRequest();
    fakeResponse = new FakeResponse();
  }

  @Test
  public void checkRequest() throws Exception {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();

    fakeRequest.setHeader("Referer","http://localhost:8080/pages/abv.html");
    fakeResponse.setOutputStream(out);
    recognizerServlet.doGet(fakeRequest,fakeResponse);

    String expected = out.toString();

    assertThat(expected.contains("http://localhost:8080/pages/abv.html"),is(true));
  }
}
