/*
 * BioJava development code
 * 
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 * 
 * http://www.gnu.org/copyleft/lesser.html
 * 
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 * 
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 * 
 * http://www.biojava.org
 */

package org.biojava.utils;

import java.util.*;

/**
 *  Interface for objects which listen to ChangeEvents. 
 *
 * @author     Thomas Down
 * @author     Matthew Pocock
 * @created    September 29, 2000 
 * @since      1.1 
 */

public interface ChangeListener extends EventListener {
  /**
   *  
   * Conventience implementation which vetoes every change of which it is
   * notified.
 You could add this to an object directly to stop it changing
   * in any way, or alternatively you could add it for a specific ChangeType
   * to stop that single item form altering.
   */
  final static ChangeListener ALWAYS_VETO = new AlwaysVetoListener();


  /**
   * Called before a change takes place. 
   * <P>
   * This is your chance to stop the change by throwing a ChangeVetoException.
   * This method does not indicate that the change will definitely take place.
   *
   * @param  cev                      
   * An event encapsulting the change which is about 
   * to take place.
   * @exception  ChangeVetoException  Description of Exception 
   * @throws                          
   * ChangeVetoException if the receiver does not wish 
   * this change to occur at this
   * time.
   */

  void preChange(ChangeEvent cev) throws ChangeVetoException;


  /**
   *  Called when a change has just taken place.
   *
   * @param  cev  
   * An event encapulating the change which has 
   * occured.
   */

  void postChange(ChangeEvent cev);


  /**
   *  An implementation that always vetoes everything. 
   *
   * @author     Thomas Down
   * @created    September 29, 2000 
   * @since      1.1 
   */

  static class AlwaysVetoListener implements ChangeListener {
    /**
     *  Private construtor to stop people pissing about.
     */
    protected AlwaysVetoListener() {
    }


    public void preChange(ChangeEvent cev) throws ChangeVetoException {
      throw new ChangeVetoException(
        cev,
        "This object sas been locked"
      );
    }


    public void postChange(ChangeEvent cev) {
      throw new NestedError(
        new ChangeVetoException(
          cev, "This object sas been locked"
        ),
        "Assertion failure: A locked object has been modified"
      );
    }
  }
}

