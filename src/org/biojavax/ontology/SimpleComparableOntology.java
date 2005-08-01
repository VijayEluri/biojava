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
 * SimpleComparableOntology.java
 *
 * Created on June 16, 2005, 2:30 PM
 */

package org.biojavax.ontology;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.biojava.ontology.AlreadyExistsException;
import org.biojava.ontology.DefaultOps;
import org.biojava.ontology.OntologyOps;
import org.biojava.ontology.Term;
import org.biojava.ontology.Triple;
import org.biojava.ontology.Variable;
import org.biojava.utils.AbstractChangeable;
import org.biojava.utils.ChangeEvent;
import org.biojava.utils.ChangeSupport;
import org.biojava.utils.ChangeVetoException;

/**
 * Represents an ontology that can be compared to other ontologies.
 * Equality is based on name alone.
 * @author Richard Holland
 * @author Mark Schreiber
 */
public class SimpleComparableOntology extends AbstractChangeable implements ComparableOntology {
    
    private String name;
    private String description;
    private Map terms = new HashMap();
    private Set triples = new HashSet();
    private OntologyOps ops;
    
    
    /**
     * Creates a new instance of SimpleComparableOntology
     * @param name the name of the ontology.
     */
    public SimpleComparableOntology(String name) {
        if (name==null) throw new IllegalArgumentException("Name cannot be null");
        this.name = name;
        this.description = null;
        this.ops = new DefaultOps() {
            public Set getRemoteTerms() {
                return Collections.EMPTY_SET;
            }
        };
    }
    
    // Hibernate requirement - not for public use.
    protected SimpleComparableOntology() {}
    
    /**
     * {@inheritDoc}
     */
    public int compareTo(Object o) {
        ComparableOntology them = (ComparableOntology)o;
        return this.getName().compareTo(them.getName());
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if (obj==null || !(obj instanceof ComparableOntology)) return false;
        ComparableOntology them = (ComparableOntology)obj;
        return this.getName().equals(them.getName());
    }
    
    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        int hash = 17;
        return 31*hash + this.getName().hashCode();
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString() {
        return this.getName();
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean containsTerm(String name) {
        return this.terms.containsKey(name);
    }
    
    /**
     * {@inheritDoc}
     */
    public Term getTerm(String s) throws NoSuchElementException {
        if (!this.terms.containsKey(s)) throw new NoSuchElementException("Ontology does not have term "+s);
        return (ComparableTerm)this.terms.get(s);
    }
    
    /**
     * {@inheritDoc}
     */
    public Term createTerm(String name, String description, Object[] synonyms) throws AlreadyExistsException, ChangeVetoException, IllegalArgumentException {
        if (name==null) throw new IllegalArgumentException("Name cannot be null");
        if (this.terms.containsKey(name)) throw new AlreadyExistsException("Ontology already has term");
        ComparableTerm ct = new SimpleComparableTerm(this,name,description,synonyms);
        if(!this.hasListeners(ComparableOntology.TERM)) {
            this.terms.put(name,ct);
        } else {
            ChangeEvent ce = new ChangeEvent(
                    this,
                    ComparableOntology.TERM,
                    ct,
                    this.terms.get(name)
                    );
            ChangeSupport cs = this.getChangeSupport(ComparableOntology.TERM);
            synchronized(cs) {
                cs.firePreChangeEvent(ce);
                this.terms.put(name,ct);
                cs.firePostChangeEvent(ce);
            }
        }
        return ct;
    }
    
    /**
     * {@inheritDoc}
     * This particular instance however ignores all the above as BioJavaX has no concept
     * of remote terms. Instead, it makes a copy of the imported term and returns a
     * pointer to it (or a pointer to the existing copy if one exists). Thus the term
     * becomes a part of this ontology instead of a pointer to another ontology.
     */
    public Term importTerm(Term t, String localName) throws ChangeVetoException, IllegalArgumentException {
        if (localName==null) localName=t.getName();
        if (localName==null) throw new IllegalArgumentException("Name cannot be null");
        if (this.terms.containsKey(localName)) return (ComparableTerm)this.terms.get(localName);
        ComparableTerm ct = new SimpleComparableTerm(this,localName,t.getDescription(),t.getSynonyms());
        if(!this.hasListeners(ComparableOntology.TERM)) {
            this.terms.put(localName,ct);
        } else {
            ChangeEvent ce = new ChangeEvent(
                    this,
                    ComparableOntology.TERM,
                    ct,
                    this.terms.get(localName)
                    );
            ChangeSupport cs = this.getChangeSupport(ComparableOntology.TERM);
            synchronized(cs) {
                cs.firePreChangeEvent(ce);
                this.terms.put(localName,ct);
                cs.firePostChangeEvent(ce);
            }
        }
        return ct;
    }
    
    /**
     * {@inheritDoc}
     */
    public Triple createTriple(Term subject, Term object, Term predicate, String name, String description) throws AlreadyExistsException, ChangeVetoException {
        if (this.containsTriple(subject,object,predicate)) throw new AlreadyExistsException("Ontology already has triple");
        if (!(subject instanceof ComparableTerm)) throw new IllegalArgumentException("Subject must be a ComparableTerm");
        if (!(object instanceof ComparableTerm)) throw new IllegalArgumentException("Object must be a ComparableTerm");
        if (!(predicate instanceof ComparableTerm)) throw new IllegalArgumentException("Predicate must be a ComparableTerm");
        ComparableTriple ct = new SimpleComparableTriple(this,(ComparableTerm)subject,(ComparableTerm)object,(ComparableTerm)predicate);
        if (!this.triples.contains(ct)) {
            if(!this.hasListeners(ComparableOntology.TRIPLE)) {
                this.triples.add(ct);
            } else {
                ChangeEvent ce = new ChangeEvent(
                        this,
                        ComparableOntology.TRIPLE,
                        ct,
                        null
                        );
                ChangeSupport cs = this.getChangeSupport(ComparableOntology.TRIPLE);
                synchronized(cs) {
                    cs.firePreChangeEvent(ce);
                    this.triples.add(ct);
                    cs.firePostChangeEvent(ce);
                }
            }
        }
        return ct;
    }
    
    /**
     * {@inheritDoc}
     */
    public void deleteTerm(Term t) throws ChangeVetoException {
        for (Iterator i = this.triples.iterator(); i.hasNext();) {
            ComparableTriple ct = (ComparableTriple)i.next();
            if (ct.equals(t) || ct.getSubject().equals(t) || ct.getObject().equals(t) || ct.getPredicate().equals(t)) {
                if(!this.hasListeners(ComparableOntology.TRIPLE)) {
                    i.remove();
                } else {
                    ChangeEvent ce = new ChangeEvent(
                            this,
                            ComparableOntology.TRIPLE,
                            null,
                            ct
                            );
                    ChangeSupport cs = this.getChangeSupport(ComparableOntology.TRIPLE);
                    synchronized(cs) {
                        cs.firePreChangeEvent(ce);
                        i.remove();
                        cs.firePostChangeEvent(ce);
                    }
                }
            }
        }
        if(!this.hasListeners(ComparableOntology.TERM)) {
            if (t instanceof Triple) this.triples.remove(t);
            else this.terms.remove(t.getName());
        } else {
            ChangeEvent ce = new ChangeEvent(
                    this,
                    ComparableOntology.TERM,
                    null,
                    t
                    );
            ChangeSupport cs = this.getChangeSupport(ComparableOntology.TERM);
            synchronized(cs) {
                cs.firePreChangeEvent(ce);
                if (t instanceof Triple) this.triples.remove(t);
                else this.terms.remove(t.getName());
                cs.firePostChangeEvent(ce);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Set getTriples(Term subject, Term object, Term predicate) {
        Set results = new HashSet();
        for (Iterator i = this.triples.iterator(); i.hasNext();) {
            ComparableTriple ct = (ComparableTriple)i.next();
            if ((subject==null || ct.getSubject().equals(subject)) &&
                    (object==null || ct.getObject().equals(object)) &&
                    (predicate==null || ct.getPredicate().equals(predicate))) results.add(ct);
        }
        return results;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setTripleSet(Set triples) throws ChangeVetoException {
        this.triples.clear();
        for (Iterator i = triples.iterator(); i.hasNext();) {
            Object o = i.next();
            if (!(o instanceof ComparableTriple)) throw new ChangeVetoException("Can only add ComparableTriples to ComparableOntology");
            ComparableTriple t = (ComparableTriple)o;
            this.triples.add(t);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Set getTripleSet() { return Collections.unmodifiableSet(this.triples); }
    
    /**
     * {@inheritDoc}
     */
    public Set getTerms() { return new HashSet(this.terms.values()); }
    
    /**
     * {@inheritDoc}
     */
    public void setTermSet(Set terms) throws ChangeVetoException {
        this.terms.clear();
        for (Iterator i = terms.iterator(); i.hasNext();) {
            Object o = i.next();
            if (!(o instanceof ComparableTerm)) throw new ChangeVetoException("Can only add ComparableTerms to ComparableOntology");
            ComparableTerm t = (ComparableTerm)o;
            this.terms.put(t.getName(),t);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Set getTermSet() { return Collections.unmodifiableSet(this.getTerms()); }
        
    /**
     * {@inheritDoc}
     */
    public boolean containsTriple(Term subject, Term object, Term predicate) {
        for (Iterator i = this.triples.iterator(); i.hasNext();) {
            ComparableTriple ct = (ComparableTriple)i.next();
            if (ct.getSubject().equals(subject) &&
                    ct.getObject().equals(object) &&
                    ct.getPredicate().equals(predicate)) return true;
        }
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    public Term createTerm(String name, String description) throws AlreadyExistsException, ChangeVetoException, IllegalArgumentException {
        return this.createTerm(name,description,null);
    }
    
    /**
     * {@inheritDoc}
     * NOT IMPLEMENTED
     */
    public Variable createVariable(String name, String description) throws AlreadyExistsException, ChangeVetoException, IllegalArgumentException {
        throw new ChangeVetoException("BioSQL doesn't know what these are so we cowardly refuse to know too.");
    }
    
    /**
     * {@inheritDoc}
     */
    public String getDescription() {  return this.description; }
    
    /**
     * {@inheritDoc}
     */
    public void setDescription(String description) throws ChangeVetoException {
        if(!this.hasListeners(ComparableOntology.DESCRIPTION)) {
            this.description = description;
        } else {
            ChangeEvent ce = new ChangeEvent(
                    this,
                    ComparableOntology.DESCRIPTION,
                    description,
                    this.description
                    );
            ChangeSupport cs = this.getChangeSupport(ComparableOntology.DESCRIPTION);
            synchronized(cs) {
                cs.firePreChangeEvent(ce);
                this.description = description;
                cs.firePostChangeEvent(ce);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public String getName() { return this.name; }
    
    // Hibernate requirement - not for public use.
    private void setName(String name) { this.name = name; }
    
    /**
     * {@inheritDoc}
     */
    public OntologyOps getOps() { return this.ops; }
    
    // Hibernate requirement - not for public use.
    private Long id;
    
    // Hibernate requirement - not for public use.
    private Long getId() { return this.id; }
    
    
    // Hibernate requirement - not for public use.
    private void setId(Long id) { this.id = id; }
    
}

