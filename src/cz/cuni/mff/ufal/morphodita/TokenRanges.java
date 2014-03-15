/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.7
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package cz.cuni.mff.ufal.morphodita;

public class TokenRanges {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected TokenRanges(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(TokenRanges obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        morphodita_javaJNI.delete_TokenRanges(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public TokenRanges() {
    this(morphodita_javaJNI.new_TokenRanges__SWIG_0(), true);
  }

  public TokenRanges(long n) {
    this(morphodita_javaJNI.new_TokenRanges__SWIG_1(n), true);
  }

  public long size() {
    return morphodita_javaJNI.TokenRanges_size(swigCPtr, this);
  }

  public long capacity() {
    return morphodita_javaJNI.TokenRanges_capacity(swigCPtr, this);
  }

  public void reserve(long n) {
    morphodita_javaJNI.TokenRanges_reserve(swigCPtr, this, n);
  }

  public boolean isEmpty() {
    return morphodita_javaJNI.TokenRanges_isEmpty(swigCPtr, this);
  }

  public void clear() {
    morphodita_javaJNI.TokenRanges_clear(swigCPtr, this);
  }

  public void add(TokenRange x) {
    morphodita_javaJNI.TokenRanges_add(swigCPtr, this, TokenRange.getCPtr(x), x);
  }

  public TokenRange get(int i) {
    return new TokenRange(morphodita_javaJNI.TokenRanges_get(swigCPtr, this, i), false);
  }

  public void set(int i, TokenRange val) {
    morphodita_javaJNI.TokenRanges_set(swigCPtr, this, i, TokenRange.getCPtr(val), val);
  }

}
