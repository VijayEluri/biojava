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


package org.acedb;

import java.net.URL;

/**
 * A node within the tree data structure.
 *
 * @author Matthew Pocock
 * @author Thomas Down
 */
public interface AceNode extends AceSet {
  /**
   * The name of this node.
   */
  String getName();
  
  /**
   * The type of this node.
   */
  AceType getType();
  
  /**
   * A url that can be used to retrieve this node.
   */
  URL toURL();
}

