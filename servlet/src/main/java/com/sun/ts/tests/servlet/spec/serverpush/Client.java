/*
 * Copyright (c) 2017, 2019 Oracle and/or its affiliates and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.ts.tests.servlet.spec.serverpush;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.WebUtil;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;
import org.apache.commons.codec.binary.Base64;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.Authenticator;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.InetAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class Client extends AbstractUrlClient {

  @BeforeEach
  public void setupServletName() throws Exception {
    setServletName("TestServlet");
  }

  /**
   * Deployment for the test
   */
  @Deployment(testable = false)
  // TOFIX
  // here http2 setup
  // @TargetsContainer()
  public static WebArchive getTestArchive() throws Exception {
    return ShrinkWrap.create(WebArchive.class, "servlet_spec_serverpush_web.war")
            .setWebXML(Client.class.getResource("servlet_spec_serverpush_web.xml"));
  }  
  

  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String USERNAME = "authuser";

  private static final String PASSWORD = "authpassword";

  private String requestURI = null;

  private String hostname;

  private int portnum;

  private WebUtil.Response response = null;

  private String authUsername = "javajoe";

  private String authPassword = "javajoe";

  private CookieManager cm = new CookieManager();

  /*
   * @class.setup_props: webServerHost; webServerPort; authuser; authpassword;
   *
   */
  // TOFIX
  public void setup(String[] args, Properties p) throws Exception {
    boolean pass = true;

    try {
      authUsername = p.getProperty(USERNAME);
      authPassword = p.getProperty(PASSWORD);
      hostname = p.getProperty(WEBSERVERHOSTPROP);
      if (hostname == null || hostname.equals("")) {
        pass = false;
      }
      try {
        portnum = Integer.parseInt(p.getProperty(WEBSERVERPORTPROP));
      } catch (Exception e) {
        pass = false;
      }
    } catch (Exception e) {
      throw new Exception("setup failed:", e);
    }

    if (!pass) {
      TestUtil.logErr(
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Exception("setup failed:");
    }

    logger.debug("hostname:port:{}:{}", hostname, portnum);
    logMsg("setup ok");
  }

  public void cleanup() throws Exception {
    TestUtil.logTrace("cleanup");
  }

  /*
   * @testName: serverPushTest
   * 
   * @assertion_ids: N/A;
   * 
   * @test_Strategy: Verify server push can work correctly.
   */
  @Test
  public void serverPushTest() throws Exception {
    requestURI = "http://" + hostname + ":" + portnum  + getContextRoot()
        + "/TestServlet";
    Map<String, String> headers = new HashMap<>();
    headers.put("foo", "bar");
    List<HttpResponse<String>> responses = sendRequest(headers, null, null);
    verfiyResponses(responses,
        new String[] { "hello", "INDEX from index.html" });
  }

  /*
   * @testName: getNullPushBuilderTest
   * 
   * @assertion_ids: N/A;
   * 
   * @test_Strategy: Verify the returned PushBuilder Object is null if the
   * current connection does not support server push.
   */
  @Test
  public void getNullPushBuilderTest() throws Exception {
    try {
      requestURI = getContextRoot() + "/TestServlet";
      TestUtil.logMsg("Sending request \"" + requestURI + "\"");

      response = WebUtil.sendRequest("GET", InetAddress.getByName(hostname),
          portnum, getRequest(requestURI), null, null);

      TestUtil.logMsg("response.statusToken:" + response.statusToken);
      TestUtil.logMsg("response.content:" + response.content);

      // Check that the page was found (no error).
      if (response.isError()) {
        TestUtil.logErr("Could not find " + requestURI);
        throw new Exception("getNullPushBuilderTest failed.");
      }

      if (response.content.indexOf("Get Null PushBuilder") < 0) {
        throw new Exception("getNullPushBuilderTest failed.");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("getNullPushBuilderTest failed: ", e);
    }
  }

  /*
   * @testName: serverPushInitTest
   * 
   * @assertion_ids: N/A;
   * 
   * @test_Strategy: Verify PushBuilder is initialized correctly.
   */
  @Test
  public void serverPushInitTest() throws Exception {
    requestURI = "http://" + hostname + ":" + portnum + getContextRoot() +
         "/TestServlet2";
    Map<String, String> headers = new HashMap<>();
    headers.put("foo", "bar");
    headers.put("If-Match", "*");
    headers.put("Range", "bytes=100-");
    String authString = authUsername + ":" + authPassword;
    logMsg("auth string: " + authString);
    byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
    String authStringEnc = new String(authEncBytes);

    CookieManager cm = new CookieManager();
    headers.put("Authorization", "Basic " + authStringEnc);
    headers.put("Referer", requestURI + "/test");

    List<HttpResponse<String>> responses = sendRequest(headers, null, cm);
    if (responses.size() != 1)
      throw new Exception("Test fail");
    String sessionid = responses.get(0)
            .headers()
            .allValues("set-cookie")
            .stream()
            .filter(value -> value.contains("JSESSIONID="))
            .findFirst()
            .orElse(null);

    if (sessionid == null) {
      throw new Exception("Test fail: new session ID should be used as the "
          + "PushBuilder's session ID.");
    }

    sessionid = sessionid
        .substring(sessionid.indexOf("JSESSIONID=") + "JSESSIONID=".length());
    if (sessionid.indexOf(";") > 0) {
      sessionid = sessionid.substring(0, sessionid.indexOf(";"));
    } else if (sessionid.indexOf(".") > 0) {
      sessionid = sessionid.substring(0, sessionid.indexOf("."));
    }

    logMsg("Sessionid in cookie: " + sessionid);

    String response = responses.get(0).body();

    StringTokenizer token = new StringTokenizer(response, "\n");
    String newSessionId = "";
    while (token.hasMoreTokens()) {
      String tmp = token.nextToken();
      if (tmp.startsWith("JSESSIONID:")) {
        newSessionId = tmp.substring("JSESSIONID:".length()).trim();
        break;
      }
    }

    if (!sessionid.contains(newSessionId) && !newSessionId.contains(sessionid)) {
      throw new Exception("Test fail: new session ID should be used as the "
          + "PushBuilder's session ID.");
    }

    if (!response.contains("Return new instance:true")) {
      throw new Exception("Test fail: each call to newPushBuilder() should "
          + "create a new instance");
    }

    if (!response.contains("Method:GET")) {
      throw new Exception("Test fail: The method of PushBuilder should be "
          + "initialized to \"GET\"");
    }

    if (!response.contains("foo=bar")) {
      throw new Exception("Test fail: The existing request headers of the current "
          + "HttpServletRequest should be added to the builder");
    }

    if (response.contains("if-match")) {
      throw new Exception(
          "Test fail: Conditional headers should NOT be added to the builder");
    }

    if (response.contains("range")) {
      throw new Exception(
          "Test fail: Range headers should NOT be added to the builder");
    }

    if (!response.contains("authorization")) {
      throw new Exception(
          "Test fail: Authorization headers should be added to the builder");
    }

    if (!response.contains("referer=" + requestURI)) {
      throw new Exception(
          "Test fail: Referer headers should be set to " + requestURI);
    }
  }

  /*
   * @testName: serverPushSessionTest
   * 
   * @assertion_ids: N/A;
   * 
   * @test_Strategy: Verify PushBuilder with session works as expected.
   */
  @Test
  public void serverPushSessionTest() throws Exception {
    try {
      requestURI = getContextRoot() + "/TestServlet3?generateSession=true";
      TestUtil.logMsg("Sending request \"" + requestURI + "\"");

      response = WebUtil.sendRequest("GET", InetAddress.getByName(hostname),
          portnum, getRequest(requestURI), null, null);
      TestUtil.logMsg("The new sessionid is :" + response.content);

      // Check that the page was found (no error).
      if (response.isError()) {
        TestUtil.logErr("Could not find " + requestURI);
        throw new Exception("serverPushSessionTest failed.");
      }

      requestURI = "http://" + hostname + ":" + portnum + getContextRoot()
          + "/TestServlet3;jsessionid=" + response.content.trim();
      TestUtil.logMsg("Sending request \"" + requestURI + "\"");
      List<HttpResponse<String>> responses = sendRequest(new HashMap<>(), null,
          null);
      String responseStr = responses.get(0).body();

      TestUtil.logMsg("The test result :" + responseStr);
      if (responseStr.indexOf("Test success") < 0) {
        throw new Exception("serverPushSessionTest failed.");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("serverPushSessionTest failed: ", e);
    }
  }

  /*
   * @testName: serverPushCookieTest
   * 
   * @assertion_ids: N/A;
   * 
   * @test_Strategy: Verify PushBuilder with cookie works as expected.
   */
  @Test
  public void serverPushCookieTest() throws Exception {
    requestURI = "http://" + hostname + ":" + portnum + getContextRoot() +
        "/TestServlet4";
    Map<String, String> headers = new HashMap<>();
    headers.put("foo", "bar");
    CookieManager cm = new CookieManager();
    List<HttpResponse<String>> responses = sendRequest(headers, null, cm);
    verfiyResponses(responses,
        new String[] { "add cookies [foo,bar] [baz,qux] to response",
            "INDEX from index.html" });
    boolean cookieExisted = false;
    String pbCookies = "";
    try {
      for (HttpResponse<String> r : responses) {
        if (r.body().indexOf("Cookie header in PushBuilder: ") >= 0) {
          cookieExisted = true;
          pbCookies = r.body()
              .substring(r.body().indexOf("Cookie header in PushBuilder: "));
          break;
        }
      }
      if (!cookieExisted) {
        throw new Exception("Wrong Responses");
      }

      if (pbCookies.indexOf("foo") < 0 || pbCookies.indexOf("bar") < 0) {
        throw new Exception(
            "The Cookie header 'foo=bar' should be added to the PushBuilder.");
      }

      if (pbCookies.indexOf("baz") >= 0 || pbCookies.indexOf("qux") >= 0) {
        throw new Exception(
            "the maxAge for Cookie 'baz=qux' is <= 0, it should be removed from the builder");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("serverPushSessionTest failed: ", e);
    }
  }

  /*
   * @testName: serverPushSessionTest2
   * 
   * @assertion_ids: N/A;
   * 
   * @test_Strategy: Verify PushBuilder with Session works as expected.
   */
  @Test
  public void serverPushSessionTest2() throws Exception {
    requestURI = "http://" + hostname + ":" + portnum + getContextRoot()
        + "/TestServlet5";
    Map<String, String> headers = new HashMap<>();
    CookieManager cm = new CookieManager();
    cm.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
    List<HttpResponse<String>> responses = sendRequest(headers, null, cm);
    boolean pass = false;

    try {
      List<HttpCookie> cookies = cm.getCookieStore().get(new URI(
          "http://" + hostname + ":" + portnum + getContextRoot() + "/index.html"));
      for (HttpCookie cookie : cookies) {
        if ("JSESSIONID".equals(cookie.getName())) {
          pass = true;
        }
      }

      if (!pass) {
        for (HttpResponse<String> response : responses) {
          if (response.uri().toString().indexOf("index.html;jsessionid") > 0) {
            pass = true;
          }
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("serverPushSessionTest failed: ", e);
    }

    if (!pass) {
      throw new Exception(
          "If the builder has a session ID, then the pushed request should "
              + "include the session ID either as a Cookie or as a URI parameter as appropriate");
    }
  }

  /*
   * @testName: serverPushMiscTest
   * 
   * @assertion_ids: N/A;
   * 
   * @test_Strategy: Verify some methods of PushBuilder works as expected.
   */
  @Test
  public void serverPushMiscTest() throws Exception {
    requestURI = "http://" + hostname + ":" + portnum + getContextRoot()
        + "/TestServlet6";
    Map<String, String> headers = new HashMap<>();
    headers.put("foo", "bar");
    headers.put("baz", "qux");
    List<HttpResponse<String>> responses = sendRequest(headers, null, null);
    HttpResponse<String> pushResp = null;
    HttpRequest pushReq = null;

    for (HttpResponse<String> response : responses) {
      if (response.uri().toString().indexOf("index.html") >= 0) {
        pushResp = response;
        pushReq = response.request();
      }
    }

    if (pushResp == null)
      throw new Exception("can not get push response");

    logMsg(
        "expected header: h1=v1, foo=v2; expected querysting: querystring=1&querystring=2");
    Map<String, List<String>> pushHeaders = pushReq.headers().map();
    logMsg("Current push request header: " + pushHeaders);
    if (!(pushHeaders.get("h1") != null
        && pushHeaders.get("h1").get(0).equals("v1"))) {
      throw new Exception("test fail: could not find header h1=v1");
    }

    if (!(pushHeaders.get("foo") != null
        && pushHeaders.get("foo").get(0).equals("v2"))) {
      throw new Exception("test fail: could not find header foo=v2");
    }

    if (pushHeaders.get("baz") != null) {
      throw new Exception("test fail");
    }

    logMsg("Current query string of the push request is "
        + pushReq.uri().getQuery());
    if (pushReq.uri().getQuery() == null || pushReq.uri().getQuery()
        .indexOf("querystring=1&querystring=2") < 0) {
      throw new Exception(
          "test fail: could not find correct querystring \"querystring=1&querystring=2\"");
    }
  }

  /*
   * @testName: serverPushNegtiveTest
   * 
   * @assertion_ids: N/A;
   * 
   * @test_Strategy: Verify some methods of PushBuilder works as expected.
   */
  @Test
  public void serverPushNegtiveTest() throws Exception {
    requestURI = "http://" + hostname + ":" + portnum + getContextRoot()
        + "/TestServlet7";
    Map<String, String> headers = new HashMap<>();

    List<HttpResponse<String>> responses = sendRequest(headers, null, null);
    HttpResponse<String> servletResp = null;
    for (HttpResponse<String> response : responses) {
      if (response.uri().toString().indexOf("TestServlet7") >= 0) {
        servletResp = response;
      }
    }

    if (servletResp == null)
      throw new Exception("can not get servlet response");
    if (servletResp.body().indexOf("test passed") < 0) {
      throw new Exception("test fail");
    }
  }

  private List<HttpResponse<String>> sendRequest(Map<String, String> headers,
      Authenticator auth, CookieManager cm) throws Exception {
    HttpClient.Builder builder = HttpClient.newBuilder();
    if (auth != null)
      builder.authenticator(auth);
    if (cm != null)
      builder.cookieHandler(cm);

    HttpClient client = builder.version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.ALWAYS)
        .executor(Executors.newFixedThreadPool(4)).build();
    ;

    List<HttpResponse<String>> responses = new CopyOnWriteArrayList<>();

    try {
      // GET
      HttpRequest.Builder requestBuilder = HttpRequest
          .newBuilder(new URI(requestURI)).version(HttpClient.Version.HTTP_2);
      for (Map.Entry<String, String> e : headers.entrySet()) {
        requestBuilder.setHeader(e.getKey(), e.getValue());
      }

      client.sendAsync(requestBuilder.build(), HttpResponse.BodyHandlers.ofString(), pushPromiseHandler())
              .thenAccept(pageResponse -> {
                responses.add(pageResponse);
              }).completeOnTimeout(null, 1, TimeUnit.MINUTES); //timeout configurable??

    } catch (Exception e) {
      throw new Exception("Test fail", e);
    }
    return responses;
  }

  private static HttpResponse.PushPromiseHandler<String> pushPromiseHandler() {
    return (HttpRequest initiatingRequest, HttpRequest pushPromiseRequest, Function<HttpResponse.BodyHandler<String>,
          CompletableFuture<HttpResponse<String>>> acceptor) -> acceptor.apply(HttpResponse.BodyHandlers.ofString());
  }

  private void printResponse(HttpResponse<String> response) {
    logMsg("ResponseURI:     " + response.uri());
    logMsg("ResponseBody:     " + response.body());
    logMsg("HTTP-Version: " + response.version());
    logMsg("Statuscode:   " + response.statusCode());
    logMsg("Header:");
    response.headers().map().forEach(
        (header, values) -> logMsg("  " + header + " = " + values.stream()
            .map(String::trim).reduce(String::concat).orElse("hallo")));
  }

  private void verfiyResponses(List<HttpResponse<String>> responses,
      String[] expectedResponses) throws Exception {
    if (responses.size() == 0) {
      throw new Exception("No Responses, expected responses are "
          + Arrays.toString(expectedResponses));
    }

    if (responses.size() != expectedResponses.length) {
      throw new Exception("Wrong Responses, expected responses are "
          + Arrays.toString(expectedResponses));
    }

    for (String s : expectedResponses) {
      boolean found = false;
      for (HttpResponse<String> r : responses) {
        logMsg(r.body());
        if (r.body().indexOf(s) >= 0) {
          found = true;
          break;
        }
      }
      if (!found) {
        throw new Exception("Wrong Responses, expected responses are "
            + Arrays.toString(expectedResponses));
      }
    }
  }
}
