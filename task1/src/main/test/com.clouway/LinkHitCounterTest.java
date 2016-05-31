package com.clouway;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 31.05.16.
 */
public class LinkHitCounterTest {
  private FakeRequest fakeRequest;
  private FakeResponse fakeResponse;
  private FakeHttpSession fakeHttpSession;
  private LinkHitCounter linkHitCounter;


  @Before
  public void setUp() {
    fakeHttpSession = new FakeHttpSession();
    fakeRequest = new FakeRequest(fakeHttpSession);
    fakeResponse = new FakeResponse();
    linkHitCounter = new LinkHitCounter();
  }

  @Test
  public void hitFirstLinkOnce() throws Exception {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();

    fakeResponse.setOutputStream(out);
    linkHitCounter.doGet(fakeRequest, fakeResponse);

    fakeRequest.setParameter("link", "first");
    linkHitCounter.doGet(fakeRequest, fakeResponse);

    String expected = out.toString();
    assertThat(expected, containsString("You have accessed this link 1 times."));
  }

  @Test
  public void hitFirstLinkTwice() throws Exception {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();

    fakeResponse.setOutputStream(out);
    linkHitCounter.doGet(fakeRequest, fakeResponse);

    fakeRequest.setParameter("link", "first");
    linkHitCounter.doGet(fakeRequest, fakeResponse);

    fakeRequest.setParameter("link", "first");
    linkHitCounter.doGet(fakeRequest, fakeResponse);

    String expected = out.toString();
    assertThat(expected, containsString("You have accessed this link 2 times."));
  }

  @Test
  public void hitEachLinkDifferentTimes() throws Exception {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();

    fakeResponse.setOutputStream(out);
    linkHitCounter.doGet(fakeRequest, fakeResponse);

    fakeRequest.setParameter("link", "first");
    linkHitCounter.doGet(fakeRequest, fakeResponse);

    fakeRequest.setParameter("link", "second");
    linkHitCounter.doGet(fakeRequest, fakeResponse);

    fakeRequest.setParameter("link", "second");
    linkHitCounter.doGet(fakeRequest, fakeResponse);

    fakeRequest.setParameter("link", "third");
    linkHitCounter.doGet(fakeRequest, fakeResponse);

    fakeRequest.setParameter("link", "third");
    linkHitCounter.doGet(fakeRequest, fakeResponse);

    fakeRequest.setParameter("link", "third");
    linkHitCounter.doGet(fakeRequest, fakeResponse);

    String expected = out.toString();
    assertThat(expected, containsString("You have accessed this link 1 times."));
    assertThat(expected, containsString("You have accessed this link 2 times."));
    assertThat(expected, containsString("You have accessed this link 3 times."));
  }
}
