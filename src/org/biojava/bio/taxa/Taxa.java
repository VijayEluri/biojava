package org.biojava.bio.taxa;

import java.util.*;

import org.biojava.utils.*;
import org.biojava.bio.*;

/**
 * A taxa within a classification.
 * <P>
 * Taxa may be 'leaf' nodes specifying species, or 'internal' nodes specifying
 * kingdoms and the like.
 *
 * @author Matthew Pocock
 */
public interface Taxa extends Annotatable {
  /**
   * Change type to indicate that the common name of this Taxa is changing.
   */
  public final static ChangeType CHANGE_COMMON_NAME = new ChangeType(
    "Common name change",
    Taxa.class,
    "CHANGE_COMMON_NAME"
  );
  
  /**
   * Change type to indicate that the scientific name of this Taxa is changing.
   */
  public final static ChangeType CHANGE_SCIENTIFIC_NAME = new ChangeType(
    "Scientific name change",
    Taxa.class,
    "CHANGE_SCIENTIFIC_NAME"
  );

  /**
   * The common name of the Taxa.
   * <P>
   * This is the normal name used in common speach, such as 'human'.
   *
   * @return a String representing this taxa's common name
   */
  public String getCommonName();
  
  /**
   * Set the new common name of this Taxa.
   *
   * @param commonName  the new common name
   * @throws ChangeVetoException if the name can't be changed at this time
   */
  public void setCommonName(String commonName)
  throws ChangeVetoException;
  
  /**
   * The scientific name of this species.
   * <P>
   * This will be the portion of the scientific classification pertaining to
   * just this node within the classifictaion. It will be something like
   * 'homo sapien' or 'archaeal group 2', rather than the full classification
   * list.
   */
  public String getScientificName();

  /**
   * Change the scientific name of this species.
   *
   * @param scientificName  the new scientific name
   * @throws ChangeVetoException if the scientific name can't be changed at this
   *         time
   */
  public void setScientificName(String scientificName)
  throws ChangeVetoException;
  
  /**
   * The parent of this Taxa.
   * <P>
   * Taxas live within a tree data-structure, so every taxa has a single parent
   * except for the root type. This has the null parent.
   *
   * @return the parent Taxa, or null if this is the root type.
   */
  public Taxa getParent();
  
  /**
   * The children of this Taxa.
   * <P>
   * Taxas live within a tree data-structure, so every taxa has zero or more
   * children. In the case of zero children, the empty set is returned.
   * <P>
   * ? read-only ? dynamicaly updated with taxa object ? copy of data ?
   *
   * @return the Set (possibly empty) of all child Taxa
   */
  public Set getChildren();
  
  /**
   * Two taxa are equal if they have equivalent children, common and
   * scientific names.
   *
   * <P>
   * Two different implementations of Taxa should be able to apropreately
   * trans-class equality. The parent of a Taxa is not considered in testing
   * equality as this potentialy leads to combinatorial problems checking whole
   * taxa hierachies against one another.
   * </P>
   *
   * @param o  the object to check
   * @return true if o is a Taxa instance and has the same properties as this
   */
  public boolean equals(Object o);
  
  /**
   * The hash-code of a Taxa is equal to the hash-code of it's scientific name.
   */
  public int hashCode();
}
