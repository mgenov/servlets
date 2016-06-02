package com.clouway.core;

import javax.servlet.http.Cookie;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 06.06.16.
 */
public interface CookieFinder {
  Cookie find(Cookie[] cookies);
}
