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

package org.biojava.bio.seq;

import java.util.*;

import org.biojava.utils.*;
import org.biojava.bio.*;

/**
 * An abstract implementation of FeatureHolder.
 *
 * This provides the filter method, but who wants to code that more than
 * once? It also has support for the ChangeEvents.
 *
 * @author Matthew Pocock
 */
public abstract class AbstractFeatureHolder implements FeatureHolder {
  protected ChangeSupport changeSupport = null;
  
  protected void generateChangeSupport(ChangeType changeType) {
    if(changeSupport == null) {
      changeSupport = new ChangeSupport();
    }
  }
  
  public void addChangeListener(ChangeListener cl) {
    generateChangeSupport(null);

    synchronized(changeSupport) {
      changeSupport.addChangeListener(cl);
    }
  }
  
  public void addChangeListener(ChangeListener cl, ChangeType ct) {
    generateChangeSupport(ct);

    synchronized(changeSupport) {
      changeSupport.addChangeListener(cl, ct);
    }
  }
  
  public void removeChangeListener(ChangeListener cl) {
    if(changeSupport != null) {
      synchronized(changeSupport) {
        changeSupport.removeChangeListener(cl);
      }
    }
  }
  
  public void removeChangeListener(ChangeListener cl, ChangeType ct) {
    if(changeSupport != null) {
      synchronized(changeSupport) {
        changeSupport.removeChangeListener(cl, ct);
      }
    }
  }  
  
  public FeatureHolder filter(FeatureFilter ff, boolean recurse) {
    SimpleFeatureHolder res = new SimpleFeatureHolder();
    for(Iterator f = features(); f.hasNext();) {
      Feature feat = (Feature) f.next();
      if(ff.accept(feat)) {
        try {
          res.addFeature(feat);
        } catch (ChangeVetoException cve) {
          throw new BioError(
            "Assertion failed: Couldn't add a feature to my new FeatureHolder"
          );
        }
      }
      if(recurse) {
        FeatureHolder r = feat.filter(ff, recurse);
        for(Iterator rf = r.features(); rf.hasNext();) {
          try {
            res.addFeature((Feature) rf.next());
          } catch (ChangeVetoException cve) {
            throw new BioError(
              cve,
              "Assertion failure: Should be able to manipulate this FeatureHolder"
            );
          }
        }
      }
    }
    return res;
  }

  public Feature createFeature(Feature.Template temp)
  throws BioException, ChangeVetoException {
    throw new ChangeVetoException(
      "This FeatureHolder does not support creation of new Features."
    );
  }

  public void removeFeature(Feature f)
  throws ChangeVetoException {
    throw new ChangeVetoException(
      "This FeatureHolder does not support removal of Features."
    );
  }
}
