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
//import org.biojava.utils.stax.*;
import org.xml.sax.*;

/**
 * sci_property
 *
 * @author Hanning Ni    Doubletwist Inc
*/
public class AGAVESciPropertyPropHandler extends StAXPropertyHandler{


   public static final StAXHandlerFactory AGAVE_SCI_PROPERTY_PROP_HANDLER_FACTORY
    = new StAXHandlerFactory() {
    public StAXContentHandler getHandler(StAXFeatureHandler staxenv) {
      return new AGAVESciPropertyPropHandler(staxenv);
    }
   };

   private AGAVEProperty xprop ;
   private String prop_type;
   private String data_type ;
   private String value ;
    AGAVESciPropertyPropHandler(StAXFeatureHandler staxenv) {
    // execute superclass method to setup environment
    super(staxenv);
    setHandlerCharacteristics("sci_property", true);
  }

  public void startElementHandler(
                String nsURI,
                String localName,
                String qName,
                Attributes attrs)
         throws SAXException
  {
      prop_type = attrs.getValue( "prop_type" ) ;
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
              if( ob instanceof AGAVERelatedAnnotPropHandler )
              {
                  ( (AGAVERelatedAnnotPropHandler) ob ).addProperty(
                      new AGAVEProperty(AGAVEProperty.SCI_PROPERTY, prop_type, data_type, value)) ;
                   return ;
              }
           }
       }

  }


}
