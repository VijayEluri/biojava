/*                    BioJava development code
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

package org.biojava.bio.seq.genomic;

import org.biojava.bio.seq.*;

/**
 * This is a feature that sits within a DNA sequence, and represents a region
 * that can be thought of as RNA. This may be a transcript, or an exon, or an
 * EST hit.
 *
 * @author Matthew Pocock
 * @since 1.1
 */
public interface RNAFeature extends StrandedFeature {
  /**
   * Retrieve the Sequence that represents the RNA associated with this feature.
   *
   * @return Sequence the associated RNA
   */
  public Sequence getRNA();
  
  /**
   * Template class for parameterizing the creation of a new RNAFeature.
   *
   * @author Matthew Pocock
   */
  public class Template extends StrandedFeature.Template {
    /**
     * The associated RNA. If this is null, then the RNA should be
     * auto-generated by translating the region within this feature. If it is
     * supplied, then firstly the alphabet should be checked to ensure that it
     * is RNA, and secondly, basic checks should be made about its length.
     */
    public Sequence rna;
  }
}
