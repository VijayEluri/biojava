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
 
package org.biojava.bio.program.gff;

import java.io.*;
import java.util.*;

import org.biojava.utils.*;
import org.biojava.bio.*;
import org.biojava.bio.symbol.*;
import org.biojava.bio.seq.*;

/**
 * A set of entries and comments as a representation of a GFF file.
 * <P>
 * This is an intermediate stoorage solution for GFF stuff. It lets you
 * collect together an arbitrary set of GFF records and comments, and then
 * do something with them later.
 *
 * @author Matthew Pocock
 */
public class GFFEntrySet {
  /**
   * All of the lines - comments & records
   */
  private List lines;
  
  /**
   * Make an empty <span class="type">GFFEntrySet</span>.
   */
  public GFFEntrySet() {
    lines = new ArrayList();
  }
  
  /**
   * Loop over all lines in the set.
   * <P>
   * The <span class="type">Iterator</span>
   * will return <span class="type">String</span> and <span class="type">
   * GFFRecord</span> objects in the order that they were added to this set.
   * It is your responsibility to check the type of
   * <span class="method">hasNext</span> before casting it.
   */
  public Iterator lineIterator() {
    return lines.iterator();
  }
  
  /**
   * Add a comment to the end of this set.
   * <P>
   * This should be the text of the comment, without the leading
   * '<code>#</code>'.
   *
   * @param comment a <span class="type">String</span> giving the comment
   */
  public void add(String comment) {
    lines.add(comment);
  }
  
  /**
   * Add a <span class="type">GFFRecord</span> to the end of this set.
   *
   * @param comment a <span class="type">GFFRecord</span> to append
   */
  public void add(GFFRecord record) {
    lines.add(record);
  }
  
  /**
   * Return how many lines are in this set.
   *
   * @return the size
   */
  public int size() {
    return lines.size();
  }
  
  /**
   * Get an annotator that can add GFF features to a
   * <span class="type">Sequence</span> using the features in this
   * <span class="type">GFFEntrySet</span>.  The SequenceAnnotator
   * returned by this method currently adds new features to an
   * existing sequence (assuming it implements MutableFeatureHolder).
   *
   * <p>Sequences are only annotated if their getName() method returns
   * a name equal to the sequence name field of one or more records
   * in this GFFEntrySet.</p>
   *
   * @return an <span class="type">SequenceAnnotator</span> that adds GFF features
   */
  public SequenceAnnotator getAnnotator() {
    return new SequenceAnnotator() {
      public Sequence annotate(Sequence seq) throws BioException, ChangeVetoException {
        Feature.Template plain = new Feature.Template();
        StrandedFeature.Template stranded = new StrandedFeature.Template();
        plain.annotation = Annotation.EMPTY_ANNOTATION;
        stranded.annotation = Annotation.EMPTY_ANNOTATION;
        for(Iterator i = lineIterator(); i.hasNext(); ) {
          Object o = i.next();
          if(o instanceof GFFRecord) {
            GFFRecord rec = (GFFRecord) o;
            if(rec.getSeqName().equals(seq.getName())) {
              if(rec.getStrand() == StrandedFeature.UNKNOWN) {
                plain.location = new RangeLocation(rec.getStart(), rec.getEnd());
                plain.type = rec.getFeature();
                plain.source = rec.getSource();
                seq.createFeature(plain);
              } else {
                stranded.location = new RangeLocation(rec.getStart(), rec.getEnd());
                stranded.type = rec.getFeature();
                stranded.source = rec.getSource();
                stranded.strand = rec.getStrand();
                seq.createFeature(stranded);
              }
            }
          }
        }
        return seq;
      }
    };
  }
  
  /**
   * Filter this entry set into another set.
   *
   * @param filter  the <span class="type">GFFRecordFilter</span> to filter with
   * @return  a new <span class="type">GFFEntrySet</span> containing only the
   *          items filtered in by the filter
   */
  public GFFEntrySet filter(GFFRecordFilter filter) {
    GFFEntrySet accepted = new GFFEntrySet();
    for(Iterator i = lineIterator(); i.hasNext(); ) {
      Object o = i.next();
      if(o instanceof GFFRecord) {
        GFFRecord record = (GFFRecord) o;
        if(filter.accept(record)) {
          accepted.add(record);
        }
      }
    }
    
    return accepted;
  }
  
  /**
   * Get the <span class="type">GFFDocumentHandler</span> for adding to this
   * set.
   *
   * @return  a <span class="type">GFFDocumentHandler</span> that adds everything
   *          that it recieves to this set
   */
  public GFFDocumentHandler getAddHandler() {
    return new EntrySetBuilder();
  }
  
  /**
   * The type of object returned by <span class="method">getAddHandler</span>.
   *
   * @author Matthew Pocock
   * @author Thomas Down
   */
  private class EntrySetBuilder implements GFFDocumentHandler {
    public void startDocument(String locator) {}
    public void endDocument()   {}
  
    public void commentLine(String comment) {
      lines.add(comment);
    }
    
    public void recordLine(GFFRecord record) {
      lines.add(record);
    }
  }
}
