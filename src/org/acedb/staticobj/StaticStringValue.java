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


package org.acedb.staticobj;

import org.acedb.*;
import java.net.*;

/**
 * @author Thomas Down
 */

public class StaticStringValue extends StaticAceNode
        implements StringValue 
{
    private String val;

    public StaticStringValue(String val, AceNode parent) {
	super(val, parent);
	this.val = val;
    }

    public String toString() {
	return val;
    }

    public AceType getType() {
	return AceType.STRING;
    }
}
