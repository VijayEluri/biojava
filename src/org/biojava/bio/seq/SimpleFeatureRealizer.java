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
import java.io.*;
import java.lang.reflect.*;
import org.biojava.bio.*;
import org.biojava.utils.*;

/**
 * FeatureRealizer which uses a lookup table to map template classes
 * to implementations.  Optionally, this implementation can fall back
 * on another FeatureRealizer if it fails.
 *
 * <p>
 * When searching for a Feature implementation to match a specific
 * Feature.Template class, the following search order is used:
 * <ol><li>Mappings added to this SimpleFeatureRealizer, in reverse
 * order of addition.</li>
 * <li>Any mappings known to the fallback realizer, if one is installed.</li>
 * <li>If no mapping can be found, a BioException is thrown.</li></ol>
 * </p>
 *
 * @author Thomas Down
 */

public class SimpleFeatureRealizer implements FeatureRealizer, Serializable {
    /**
     * Default implementation of FeatureRealizer, which wraps simple
     * implementations of Feature and StrandedFeature.  This is the
     * default FeatureRealizer used by SimpleSequence and ViewSequence,
     * and may also be used by others.  When building new FeatureRealizers,
     * you may wish to use this as a `fallback' realizer, and benefit from
     * the Feature and StrandedFeature implementations.
     */

    public final static FeatureRealizer DEFAULT;

    static {
	SimpleFeatureRealizer d  = new SimpleFeatureRealizer() {
	    public Object writeReplace() {
		try {
		    return new StaticMemberPlaceHolder(SimpleFeatureRealizer.class.getField("DEFAULT"));
		} catch (NoSuchFieldException ex) {
		    throw new BioError(ex);
		}
	    }
	} ;

	try {
	    d.addImplementation(Feature.Template.class, 
				SimpleFeature.class);
	    d.addImplementation(StrandedFeature.Template.class,
				SimpleStrandedFeature.class);
	} catch (BioException ex) {
	    throw new BioError(ex, "Couldn't initialize default FeatureRealizer");
	}

	DEFAULT = d;
    }

    private List templateToImpl;
    private FeatureRealizer fallBack;

    {
	templateToImpl = new ArrayList();
    }

    public SimpleFeatureRealizer() {
	fallBack = null;
    }

    public SimpleFeatureRealizer(FeatureRealizer fallBack) {
	this.fallBack = fallBack;
    }

    /**
     * Install a new mapping from a class of Feature.Template to
     * a class of Feature implementations.  The implementation
     * class MUST have a public constructor of the form
     * (Sequence, Feature.Template).
     *
     * <p>A newly added implementation takes precendence over
     * any existing implementations if a template can be realized
     * by more than one implementation.</p>
     * 
     * @param template The class of templates to implement.
     * @param impl A class of Feature which can be used to implement these templates.
     */

    public void addImplementation(Class template, Class impl) 
        throws BioException
    {
	TemplateImpl ti = new TemplateImpl(template, impl);
	templateToImpl.add(0, ti);
    }

    public Feature realizeFeature(Sequence seq, Feature.Template temp)
	throws BioException 
    {
	for (Iterator i = templateToImpl.iterator(); i.hasNext(); ) {
	    TemplateImpl ti = (TemplateImpl) i.next();
	    if (ti.accept(temp))
		return ti.realize(seq, temp);
	}

	if (fallBack != null)
	    return fallBack.realizeFeature(seq, temp);

	throw new BioException("Couldn't find realized implementation for template of class " + 
			       temp.getClass().getName());
    }

    private static class TemplateImpl {
	private Class template;
	private Constructor cons;

	private TemplateImpl(Class template, Class impl) 
	    throws BioException
	{
	    Class[] signature = new Class[2];
	    signature[0] = Sequence.class;
	    signature[1] = template;
	    this.template = template;

	    try {
		this.cons = impl.getConstructor(signature);
	    } catch (NoSuchMethodException ex) {
		throw new BioException(ex, "Class " + impl.getName() + " does not have suitable constructor");
	    }
	}

	public boolean accept(Feature.Template temp) {
	    return template.isInstance(temp);
	}

	public Feature realize(Sequence seq, Feature.Template temp) 
	    throws BioException
	{
	    Object[] consArgs = new Object[2];
	    consArgs[0] = seq;
	    consArgs[1] = temp;
	    try {
		return (Feature) cons.newInstance(consArgs);
	    } catch (Exception ex) {
		throw new BioException(ex, "Couldn't realize feature");
	    }
	}
    }
}
