/*
 *                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 */

package org.biojava.bio.seq.io;

import java.io.*;
import org.biojava.bio.seq.*;
import org.biojava.bio.symbol.*;

/**
 * Base-class for listeners that pass filtered events onto another listener.
 *
 * @author Matthew Pocock
 * @since 1.1
 */
public class SeqIOFilter implements SeqIOListener {
  /**
   * Delegate that will recieve all of the forwarded event.
   */
  private SeqIOListener delegate;
  
  /**
   * Create a new SeqIOFilter that will forward events on to another listener.
   *
   * @param delegate  the SeqIOListener to wrap
   */
  public SeqIOFilter(SeqIOListener delegate) {
    this.delegate = delegate;
  }
  
  /**
   * Retrieve the delegate that is wrapped.
   *
   * @return the SeqIOListener delegate
   */
  public SeqIOListener getDelegate() {
    return delegate;
  }
  
  public void startSequence() {
    delegate.startSequence();
  }
  
  public void endSequence() {
    delegate.endSequence();
  }
  
  public void setName(String name) {
    delegate.setName(name);
  }
  
  public void setURI(String uri) {
    delegate.setURI(uri);
  }
  
  public void addSymbols(Alphabet alpha, Symbol[] syms, int start, int length)
  throws IllegalAlphabetException {
    delegate.addSymbols(alpha, syms, start, length);
  }
  
  public void addSequenceProperty(String key, Object value) {
    delegate.addSequenceProperty(key, value);
  }
  
  public void startFeature(Feature.Template templ) {
    delegate.startFeature(templ);
  }
  
  public void endFeature() {
    delegate.endFeature();
  }
  
  public void addFeatureProperty(String key, Object value) {
    delegate.addFeatureProperty(key, value);
  }
}
