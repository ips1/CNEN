/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.7
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package cz.cuni.mff.ufal.morphodita;

public class TokenRange {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected TokenRange(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(TokenRange obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        morphodita_javaJNI.delete_TokenRange(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setStart(long value) {
    morphodita_javaJNI.TokenRange_start_set(swigCPtr, this, value);
  }

  public long getStart() {
    return morphodita_javaJNI.TokenRange_start_get(swigCPtr, this);
  }

  public void setLength(long value) {
    morphodita_javaJNI.TokenRange_length_set(swigCPtr, this, value);
  }

  public long getLength() {
    return morphodita_javaJNI.TokenRange_length_get(swigCPtr, this);
  }

  public TokenRange() {
    this(morphodita_javaJNI.new_TokenRange(), true);
  }

}
