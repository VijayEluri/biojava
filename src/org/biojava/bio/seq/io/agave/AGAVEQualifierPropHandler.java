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
import org.biojava.bio.symbol.*;
import org.biojava.utils.*;
import org.xml.sax.*;

/**
 * 
 * @author Hanning Ni    Doubletwist Inc
 */
public class AGAVEQualifierPropHandler extends StAXPropertyHandler {


   public static final StAXHandlerFactory AGAVE_QUALIFIER_PROP_HANDLER_FACTORY
    = new StAXHandlerFactory() {
    public StAXContentHandler getHandler(StAXFeatureHandler staxenv) {
      return new AGAVEQualifierPropHandler(staxenv);
    }
   };

   private AGAVEProperty xprop ;
   private String prop_type;
   private String data_type ;
   private String value ;
    AGAVEQualifierPropHandler(StAXFeatureHandler staxenv) {
    // execute superclass method to setup environment
    super(staxenv);
    setHandlerCharacteristics("qualifier", true);
  }

  public void startElementHandler(
                String nsURI,
                String localName,
                String qName,
                Attributes attrs)
         throws SAXException
  {
      prop_type = attrs.getValue( "qualifier_type" ) ;
      data_type = attrs.getValue( "data_type" )  ;
  }
   public void characters(char[] ch, int start, int length)
        throws SAXException
  {
      value = new String(ch) ;
  }
  public void endElementHandler(
                String nsURI,
                String localName,
                String qName,
                StAXContentHandler handler)
                throws SAXException
  {
       int currLevel = staxenv.getLevel();
       if (currLevel >=1) {
           ListIterator li = staxenv.getHandlerStackIterator(currLevel);
           while( li.hasPrevious() )
          {
              Object ob =   li.previous() ;
              if( ob instanceof AGAVESeqFeatureHandler )
              {
                  ( (AGAVESeqFeatureHandler) ob ).addProperty(
                      new AGAVEProperty(AGAVEProperty.QUALIFIER, prop_type, data_type, value)) ;
                   return ;
              }
           }
       }

  }


}
