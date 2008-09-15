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
 * created at Sep 14, 2008
 */
package org.biojava.bio.program.sax;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import junit.framework.TestCase;

import org.biojava.bio.program.ssbind.BlastLikeSearchBuilder;
import org.biojava.bio.program.ssbind.SeqSimilarityAdapter;
import org.biojava.bio.search.SearchContentHandler;
import org.biojava.bio.search.SeqSimilaritySearchHit;
import org.biojava.bio.search.SeqSimilaritySearchResult;
import org.biojava.bio.seq.db.DummySequenceDB;
import org.biojava.bio.seq.db.DummySequenceDBInstallation;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/** a class to test Blast parsing
 * 
 * @author Travis Banks
 *
 */
public class BlastTest extends TestCase {


	public void testBLASTP2_2_11() throws Exception
	{
		String filename = "files/org/biojava/bio/program/sax/blastp-2.2.11.txt.gz";
		parseBlastFile(filename, 2, 2, 2);
	}

	public void testBLASTP2_2_15() throws Exception
	{
		String filename = "files/org/biojava/bio/program/sax/blastp-2.2.15.txt.gz";
		parseBlastFile(filename, 2, 2, 2);
	}

	public void testTBLASTN2_2_18() throws Exception
	{
		String filename = "files/org/biojava/bio/program/sax/tblastn-2.2.18.txt.gz"
			;
		parseBlastFile(filename, 2, 10, 2);
	}

	public void testSingleBLASTHit2_2_15() throws Exception
	{
		String filename = "files/org/biojava/bio/program/sax/single-blastp-2.2.15.txt.gz";
			parseBlastFile(filename, 2, 2, 1);
	}

	private void parseBlastFile(String filename, int numberOfReports, int numberOfHits, int numberOfHsps) throws IOException, SAXException {
		String resName = filename;
		InputStream resStream = new GZIPInputStream( getClass().getClassLoader().getResourceAsStream(
				resName));
		assert resStream != null
		: "Resource " + resName + " could not be located";
		InputSource is = new InputSource(resStream);
		
		
		BlastLikeSAXParser parser = new BlastLikeSAXParser();
		parser.setModeStrict();
		SeqSimilarityAdapter adapter = new SeqSimilarityAdapter();
		parser.setContentHandler(adapter);
		List<SeqSimilaritySearchResult> results = new ArrayList<SeqSimilaritySearchResult>();
		SearchContentHandler builder = new BlastLikeSearchBuilder(results,
				new DummySequenceDB("queries"), new DummySequenceDBInstallation());
		adapter.setSearchContentHandler(builder);
		parser.parse(is);

		// check that there are the correct numbers of queries
		assertEquals(numberOfReports, results.size());

		// check that the number of hits is correct
		for(SeqSimilaritySearchResult sssr: results) {
			assertEquals(numberOfHits,sssr.getHits().size());
		}
		// check that the number of hsps is correct
		for(SeqSimilaritySearchResult sssr: results) {
			for(Object o:sssr.getHits()) {
				SeqSimilaritySearchHit hit=(SeqSimilaritySearchHit)o;
				assertEquals(numberOfHsps,hit.getSubHits().size());
			}
		}
	}
}
