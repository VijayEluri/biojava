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

package org.biojava.bio.seq.io;

import java.io.*;
import java.util.*;
import java.net.*;

import org.biojava.utils.*;
import org.biojava.bio.*;
import org.biojava.bio.symbol.*;
import org.biojava.bio.seq.*;
import org.biojava.bio.seq.io.*;

/**
 * Format object representing FASTA files. These files are almost pure
 * sequence data. The only `sequence property' reported by this parser
 * is PROPERTY_DESCRIPTIONLINE, which is the contents of the
 * sequence's description line (the line starting with a '>'
 * character). Normally, the first word of this is a sequence ID. If
 * you wish it to be interpreted as such, you should use
 * FastaDescriptionLineParser as a SeqIO filter.
 *
 * @author Thomas Down
 * @author Matthew Pocock
 * @author Greg Cox
 */

public class FastaFormat implements SequenceFormat,
                                    Serializable,
                                    org.biojava.utils.ParseErrorListener,
                                    org.biojava.utils.ParseErrorSource
{
  
    static {
        Set validFormats = new HashSet();
        validFormats.add("Fasta");

        SequenceFormat.FORMATS.put(FastaFormat.class.getName(),
                                   validFormats);
    }

  /**
    * Constant string which is the property key used to notify
    * listeners of the description lines of FASTA sequences.
    */
    
  public final static String PROPERTY_DESCRIPTIONLINE = "description_line";
  
  private Vector mListeners = new Vector();

  /**
   * The line width for output.
   */
  private int lineWidth = 60;

  /**
   * Retrive the current line width.
   *
   * @return the line width
   */
  public int getLineWidth() {
    return lineWidth;
  }

  /**
   * Set the line width.
   * <p>
   * When writing, the lines of sequence will never be longer than the line
   * width.
   *
   * @param width the new line width
   */

  public void setLineWidth(int width) {
    this.lineWidth = width;
  }

  public boolean readSequence(
    BufferedReader reader,
		SymbolTokenization symParser,
		SeqIOListener siol
  )	throws
    IllegalSymbolException,
    IOException,
    ParseException
  {
    String line = reader.readLine();
    if (line == null) {
      throw new IOException("Premature stream end");
    }
    if (!line.startsWith(">")) {
      throw new IOException("Stream does not appear to contain FASTA formatted data: " + line);
    }
    
    siol.startSequence();
    
    String description = line.substring(1).trim();
    siol.addSequenceProperty(PROPERTY_DESCRIPTIONLINE, description);
    
    String name = new java.util.StringTokenizer(description).nextToken();
    siol.setName(name);
    
    boolean seenEOF = readSequenceData(reader, symParser, siol);
    siol.endSequence();
    
    return !seenEOF;
  }

  private boolean readSequenceData(
    BufferedReader r,
    SymbolTokenization parser,
    SeqIOListener listener
  ) throws
    IOException,
    IllegalSymbolException
  {
    char[] cache = new char[512];
    boolean reachedEnd = false, seenEOF = false;
    StreamParser sparser = parser.parseStream(listener);
    
    while (!reachedEnd) {
      r.mark(cache.length);
      int bytesRead = r.read(cache, 0, cache.length);
      if (bytesRead < 0) {
        reachedEnd = seenEOF = true;
      } else {
        int parseStart = 0;
        int parseEnd = 0;
        while (!reachedEnd && parseStart < bytesRead && cache[parseStart] != '>') {
          parseEnd = parseStart;
          
          while (parseEnd < bytesRead &&
            cache[parseEnd] != '\n' &&
            cache[parseEnd] != '\r'
          ) {
            ++parseEnd;
          }
          
          sparser.characters(cache, parseStart, parseEnd - parseStart);
          
          parseStart = parseEnd + 1;
          while (parseStart < bytesRead &&
            cache[parseStart] == '\n' &&
            cache[parseStart] == '\r')
            {
              ++parseStart;
            }
        }
        if (parseStart < bytesRead && cache[parseStart] == '>') {
          try {
            r.reset();
          } catch (IOException ioe) {
            throw new IOException(
              "Can't reset: " +
              ioe.getMessage() +
              " parseStart=" + parseStart +
              " bytesRead=" + bytesRead
            );
          }
          if (r.skip(parseStart) != parseStart) {
            throw new IOException("Couldn't reset to start of next sequence");
          }
          reachedEnd = true;
        }
      }
    }
    
    sparser.close();
    return seenEOF;
  }

    /**
     * Return a suitable description line for a Sequence. If the
     * sequence's annotation bundle contains PROPERTY_DESCRIPTIONLINE,
     * this is used verbatim.  Otherwise, the sequence's name is used.
     */

    protected String describeSequence(Sequence seq) {
	String description = null;
	try {
	    description = seq.getAnnotation().getProperty(PROPERTY_DESCRIPTIONLINE).toString();
	} catch (NoSuchElementException ex) {
	    description = seq.getName();
	}
	return description;
    }

    public void writeSequence(Sequence seq, PrintStream os)
	throws IOException
    {
	os.print(">");
	os.println(describeSequence(seq));

	//  int length = seq.length();
//    	for(int i = 1; i <= length; i++) {
//    	    os.write(seq.symbolAt(i).getToken());
//    	    if( (i % lineWidth) == 0)
//    		os.println();
//    	}
//    	if( (length % lineWidth) != 0)
//    	    os.println();

	for(int pos = 1; pos <= seq.length(); pos += lineWidth) {
	    int end = Math.min(pos + lineWidth - 1, seq.length());
	    os.println(seq.subStr(pos, end));
	}
    }

    public void writeSequence(Sequence seq, String format, PrintStream os)
        throws IOException {
        String requestedFormat = new String(format);
        boolean          found = false;

        String [] formats = (String []) getFormats().toArray(new String[0]);

        for (int i = 0; i < formats.length; i++) {
            found = found || (formats[i].compareTo(format) == 0);
        }

        if (! found)
            throw new IOException("Failed to write: an invalid file format '"
                                  + format
                                  + "' was requested");

        writeSequence(seq, os);
    }

    public Set getFormats()
    {
        return (Set) SequenceFormat.FORMATS.get(this.getClass().getName());
    }

    public String getDefaultFormat()
    {
        return "Fasta";
    }

    /**
     * Adds a parse error listener to the list of listeners if it isn't already
     * included.
     *
     * @param theListener Listener to be added.
     */
    public synchronized void addParseErrorListener(ParseErrorListener theListener)
    {
        if(mListeners.contains(theListener) == false)
        {
            mListeners.addElement(theListener);
        }
    }

    /**
     * Removes a parse error listener from the list of listeners if it is
     * included.
     *
     * @param theListener Listener to be removed.
     */
    public synchronized void removeParseErrorListener(
            ParseErrorListener theListener)
    {
        if(mListeners.contains(theListener) == true)
        {
            mListeners.removeElement(theListener);
        }
    }

    /**
     * This method determines the behaviour when a bad line is processed.
     * Some options are to log the error, throw an exception, ignore it
     * completely, or pass the event through.
     * <p>
     * This method should be overwritten when different behavior is desired.
     *
     * @param theEvent The event that contains the bad line and token.
     */
    public void BadLineParsed(org.biojava.utils.ParseErrorEvent theEvent)
    {
        notifyParseErrorEvent(theEvent);
    }

    // Protected methods
    /**
     * Passes the event on to all the listeners registered for ParseErrorEvents.
     *
     * @param theEvent The event to be handed to the listeners.
     */
    protected void notifyParseErrorEvent(ParseErrorEvent theEvent)
    {
        Vector listeners;
        synchronized(this)
        {
            listeners = (Vector)mListeners.clone();
        }

        for (int index = 0; index < listeners.size(); index++)
        {
            ParseErrorListener client = (ParseErrorListener)listeners.elementAt(index);
            client.BadLineParsed(theEvent);
        }
    }
}
