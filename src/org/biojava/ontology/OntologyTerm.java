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

package org.biojava.bio.ontology; 
 
import java.util.*;
import org.biojava.utils.*;
import org.biojava.bio.*;

/**
 * A term in an ontology which identifies another ontology.
 *
 * <p>
 * This Term type has an associated ontology. It is meant to represent that
 * ontology so that you can reason over them. For example, you could add
 * information to an Ontology containing an OntologyTerm stating how the
 * OntologyTerm's Ontology relates to other entities. This allows
 * classifications of Ontologies to be built. You could say that GO is a
 * biological ontoloogy, as is SO or perhaps declar something about the source
 * of the information.
 * </p>
 *
 * @author Thomas Down
 * @author Matthew Pocock
 * @since 1.4
 */

public interface OntologyTerm extends Term {
    /**
     * Get the remote ontology referenced by this term
     */
    
    public Ontology getOntology();
    
    /**
     * Simple in-memory implementation of a remote ontology term.
     *
     * @for.developer This can be used to implement Ontology.importTerm
     */
    
    public final static class Impl
    extends AbstractChangeable
    implements OntologyTerm, java.io.Serializable {
        private final Ontology ontology;
        private final Ontology target;
        private transient ChangeForwarder forwarder;
        
        public Impl(Ontology ontology, Ontology target) {
            if (ontology == null) {
                throw new NullPointerException("The ontology may not be null");
            }
            if (target == null) {
                throw new NullPointerException("The targetted ontology may not be null");
            }
            this.ontology = ontology;
            this.target = target;
        }
        
        public String getName() {
            return target.getName();
        }
        
        public String getDescription() {
            return target.getDescription();
        }
        
        public Ontology getOntology() {
            return ontology;
        }
        
        public Ontology getTargetOntology() {
            return target;
        }
        
        public String toString() {
            return "Remote ontology: " + getName();
        }
        
        public Annotation getAnnotation() {
            return Annotation.EMPTY_ANNOTATION;
        }
        
        public ChangeSupport getChangeSupport(ChangeType ct) {
            ChangeSupport cs = super.getChangeSupport(ct);
            forwarder = new ChangeForwarder(this, cs) {
                protected ChangeEvent generateEvent(ChangeEvent cev) {
                    return new ChangeEvent(
                        getSource(),
                        ChangeType.UNKNOWN,
                        target,
                        null,
                        cev
                    );
                }
            } ;
            target.addChangeListener(forwarder, ChangeType.UNKNOWN);
            return cs;
        }
    }
}
