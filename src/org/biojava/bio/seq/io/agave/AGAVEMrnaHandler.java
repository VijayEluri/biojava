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
package org.biojava.bio.seq.io.agave;
import java.util.*;
import org.biojava.bio.*;
import org.biojava.bio.seq.*;
import org.biojava.bio.seq.io.*;
import org.biojava.bio.seq.genomic.Gene;
import org.biojava.bio.symbol.*;
import org.biojava.bio.program.das.*;
import org.biojava.utils.*;
//import org.biojava.utils.stax.*;
import org.xml.sax.*;

/**
 *
 * Handles the AGAVE &lt;mrna&gt; element
 *
 * @author Hanning Ni    Doubletwist Inc
 */
public class AGAVEMrnaHandler
               extends StAXFeatureHandler

{
  public static final StAXHandlerFactory AGAVE_MRNA_HANDLER_FACTORY
    = new StAXHandlerFactory() {
    public StAXContentHandler getHandler(StAXFeatureHandler staxenv) {
      return new AGAVEMrnaHandler(staxenv);
    }
  };


  AGAVEMrnaHandler(StAXFeatureHandler staxenv) {
    // setup up environment stuff
    super( staxenv );
    featureListener = staxenv.featureListener;
    setHandlerCharacteristics("mrna", true);

         super.addHandler(new ElementRecognizer.ByLocalName("bio_sequence"),
         AGAVEBioSequenceHandler.AGAVE_BIO_SEQUENCE_HANDLER_FACTORY);
  }


   public void startElementHandler(
                String nsURI,
                String localName,
                String qName,
                Attributes attrs)
         throws SAXException
  {
      featureTemplate.type = "mRNA" ;
  }


  /**
  protected Feature.Template createTemplate() {
    // create Gene Template for this
    StrandedFeature.Template st = new StrandedFeature.Template();

    // assume feature set to describe a transcript
    st.type = "mrna";
    st.strand = StrandedFeature.UNKNOWN;
    // set up annotation bundle
    st.annotation = new SmallAnnotation();
    st.location = new  Location.EmptyLocation();
    if( staxenv != null )
        staxenv. subFeatures .add( this ) ;

    return st;
  }**/

}
