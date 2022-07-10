package com.sleticalboy.tinker.fake;

/**
 * Created on 19-4-5.
 *
 * @author leebin
 */
public final class Bug {

  public String bug() {
    int i = 10;
    int j = 1;
    return "" + i / j;
  }
}
