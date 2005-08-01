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

/*
 * SimpleBioEntryRelationship.java
 *
 * Created on June 16, 2005, 2:07 PM
 */

package org.biojavax.bio;

import org.biojava.utils.AbstractChangeable;
import org.biojava.utils.ChangeEvent;
import org.biojava.utils.ChangeSupport;
import org.biojava.utils.ChangeVetoException;
import org.biojavax.ontology.ComparableTerm;

/**
 * Represents a relationship between two bioentries that is described by a term.
 * Equality is the combination of unique subject, object and term.
 * @author Richard Holland
 * @author Mark Schreiber
 */
public class SimpleBioEntryRelationship extends AbstractChangeable implements BioEntryRelationship {
    
    private BioEntry object;
    private BioEntry subject;
    private ComparableTerm term;
    private int rank;
    
    /**
     * Creates a new instance of SimpleBioEntryRelationship
     * @param object The object bioentry.
     * @param subject The subject bioentry.
     * @param term The relationship term.
     */
    
    public SimpleBioEntryRelationship(BioEntry object, BioEntry subject, ComparableTerm term, int rank) {
        if (object==null) throw new IllegalArgumentException("Object cannot be null");
        if (subject==null) throw new IllegalArgumentException("Subject cannot be null");
        if (term==null) throw new IllegalArgumentException("Term cannot be null");
        if (object.equals(subject)) throw new IllegalArgumentException("Object cannot be the same as the subject");
        this.object = object;
        this.subject = subject;
        this.term = term;
        this.rank = rank;
    }
    
    // Hibernate requirement - not for public use.
    protected SimpleBioEntryRelationship() {}
    
    /**
     * {@inheritDoc}
     */
    public void setRank(int rank) throws ChangeVetoException {
        if(!this.hasListeners(BioEntryRelationship.RANK)) {
            this.rank = rank;
        } else {
            ChangeEvent ce = new ChangeEvent(
                    this,
                    BioEntryRelationship.RANK,
                    Integer.valueOf(rank),
                    Integer.valueOf(this.rank)
                    );
            ChangeSupport cs = this.getChangeSupport(BioEntryRelationship.RANK);
            synchronized(cs) {
                cs.firePreChangeEvent(ce);
                this.rank = rank;
                cs.firePostChangeEvent(ce);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public int getRank() { return this.rank; }
    
    /**
     * {@inheritDoc}
     */
    public BioEntry getObject() { return this.object; }
    
    // Hibernate requirement - not for public use.
    private void setObject(BioEntry object) { this.object = object; }
    
    /**
     * {@inheritDoc}
     */
    public BioEntry getSubject() { return this.subject; }
    
    // Hibernate requirement - not for public use.
    private void setSubject(BioEntry subject) { this.subject = subject; }
    
    /**
     * {@inheritDoc}
     */
    public ComparableTerm getTerm() { return this.term; }
    
    // Hibernate requirement - not for public use.
    private void setTerm(ComparableTerm term) { this.term = term; }
    
    /**
     * {@inheritDoc}
     */
    public int compareTo(Object o) {
        BioEntryRelationship them = (BioEntryRelationship)o;
        if (!this.getObject().equals(them.getObject())) return this.getObject().compareTo(them.getObject());
        if (!this.getSubject().equals(them.getSubject())) return this.getSubject().compareTo(them.getSubject());
        return this.getTerm().compareTo(them.getTerm());
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj==null || !(obj instanceof BioEntryRelationship)) return false;
        else {
            BioEntryRelationship them = (BioEntryRelationship)obj;
            return (this.getObject().equals(them.getObject()) &&
                    this.getSubject().equals(them.getSubject()) &&
                    this.getTerm().equals(them.getTerm()));
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        int code = 17;
        code = code*37 + this.getObject().hashCode();
        code = code*37 + this.getSubject().hashCode();
        code = code*37 + this.getTerm().hashCode();
        return code;
    }
    
    /**
     * {@inheritDoc}
     * Form is <code>this.getTerm()+"("+this.getSubject()+","+this.getObject()+")";<code>
     */
    public String toString() { return this.getTerm()+"("+this.getSubject()+","+this.getObject()+")"; }
    
    // Hibernate requirement - not for public use.
    private Long id;
    
    // Hibernate requirement - not for public use.
    private Long getId() { return this.id; }
    
    // Hibernate requirement - not for public use.
    private void setId(Long id) { this.id = id; }
}

