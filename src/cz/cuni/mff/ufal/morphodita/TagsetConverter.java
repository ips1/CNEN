/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.7
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package cz.cuni.mff.ufal.morphodita;

public class TagsetConverter {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected TagsetConverter(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(TagsetConverter obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        morphodita_javaJNI.delete_TagsetConverter(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void convert(TaggedLemma tagged_lemma) {
    morphodita_javaJNI.TagsetConverter_convert(swigCPtr, this, TaggedLemma.getCPtr(tagged_lemma), tagged_lemma);
  }

  public void convertAnalyzed(TaggedLemmas tagged_lemmas) {
    morphodita_javaJNI.TagsetConverter_convertAnalyzed(swigCPtr, this, TaggedLemmas.getCPtr(tagged_lemmas), tagged_lemmas);
  }

  public void convertGenerated(TaggedLemmasForms forms) {
    morphodita_javaJNI.TagsetConverter_convertGenerated(swigCPtr, this, TaggedLemmasForms.getCPtr(forms), forms);
  }

  public static TagsetConverter newIdentityConverter() {
    long cPtr = morphodita_javaJNI.TagsetConverter_newIdentityConverter();
    return (cPtr == 0) ? null : new TagsetConverter(cPtr, true);
  }

  public static TagsetConverter newPdtToConll2009Converter() {
    long cPtr = morphodita_javaJNI.TagsetConverter_newPdtToConll2009Converter();
    return (cPtr == 0) ? null : new TagsetConverter(cPtr, true);
  }

}