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

import org.apache.regexp.*;

import org.biojava.bio.*;
import org.biojava.bio.seq.*;
import org.biojava.bio.symbol.*;
import org.biojava.bio.seq.db.*;
import org.biojava.utils.*;

/**
 * A set of convenience methods for handling common file formats.
 *
 * @author Thomas Down
 * @author Mark Schreiber
 * @author Nimesh Singh
 * @author Matthew Pocock
 * @author Keith James
 * @since 1.1
 */
public final class SeqIOTools  {
    private static SequenceBuilderFactory _emblBuilderFactory;
    private static SequenceBuilderFactory _genbankBuilderFactory;
    private static SequenceBuilderFactory _genpeptBuilderFactory;
    private static SequenceBuilderFactory _swissprotBuilderFactory;
    private static SequenceBuilderFactory _fastaBuilderFactory;

    /**
     * This can't be instantiated.
     */
    private SeqIOTools() {
    }

    /**
     * Get a default SequenceBuilderFactory for handling EMBL
     * files.
     */
    public static SequenceBuilderFactory getEmblBuilderFactory() {
        if (_emblBuilderFactory == null) {
            _emblBuilderFactory =
                new EmblProcessor.Factory(SmartSequenceBuilder.FACTORY);
        }
        return _emblBuilderFactory;
    }

    /**
     * Iterate over the sequences in an EMBL-format stream.
     */
    public static SequenceIterator readEmbl(BufferedReader br) {
        return new StreamReader(br,
                                new EmblLikeFormat(),
                                getDNAParser(),
                                getEmblBuilderFactory());
    }

    /**
     * Iterate over the sequences in an EMBL-format stream, but for RNA.
     */
    public static SequenceIterator readEmblRNA(BufferedReader br) {
        return new StreamReader(br,
                                new EmblLikeFormat(),
                                getRNAParser(),
                                getEmblBuilderFactory());
    }

    /**
     * Iterate over the sequences in an EMBL-format stream.
     */
    public static SequenceIterator readEmblNucleotide(BufferedReader br) {
        return new StreamReader(br,
                                new EmblLikeFormat(),
                                getNucleotideParser(),
                                getEmblBuilderFactory());
    }

    /**
     * Get a default SequenceBuilderFactory for handling GenBank
     * files.
     */
    public static SequenceBuilderFactory getGenbankBuilderFactory() {
        if (_genbankBuilderFactory == null) {
            _genbankBuilderFactory =
                new GenbankProcessor.Factory(SmartSequenceBuilder.FACTORY);
        }
        return _genbankBuilderFactory;
    }

    /**
     * Iterate over the sequences in an GenBank-format stream.
     */
    public static SequenceIterator readGenbank(BufferedReader br) {
        return new StreamReader(br,
                                new GenbankFormat(),
                                getDNAParser(),
                                getGenbankBuilderFactory());
    }

    /**
    * Get a default SequenceBuilderFactory for handling Genpept
    * files.
    */
    public static SequenceBuilderFactory getGenpeptBuilderFactory() {
        if (_genpeptBuilderFactory == null) {
            _genpeptBuilderFactory =
                new GenbankProcessor.Factory(SmartSequenceBuilder.FACTORY);
        }
        return _genpeptBuilderFactory;
    }

    /**
    * Iterate over the sequences in an Genpept-format stream.
    */
    public static SequenceIterator readGenpept(BufferedReader br) {
        return new StreamReader(br,
                                new GenbankFormat(),
                                getProteinParser(),
                                getGenpeptBuilderFactory());
    }

    /**
     * Get a default SequenceBuilderFactory for handling Swissprot
     * files.
     */
    public static SequenceBuilderFactory getSwissprotBuilderFactory() {
        if (_swissprotBuilderFactory == null) {
            _swissprotBuilderFactory =
                new SwissprotProcessor.Factory(SmartSequenceBuilder.FACTORY);
        }
        return _swissprotBuilderFactory;
    }

    /**
     * Iterate over the sequences in an Swissprot-format stream.
     */
    public static SequenceIterator readSwissprot(BufferedReader br) {
        return new StreamReader(br,
                                new EmblLikeFormat(),
                                getProteinParser(),
                                getSwissprotBuilderFactory());
    }

    /**
     * Get a default SequenceBuilderFactory for handling FASTA
     * files.
     */
    public static SequenceBuilderFactory getFastaBuilderFactory() {
        if (_fastaBuilderFactory == null) {
            _fastaBuilderFactory =
                new FastaDescriptionLineParser.Factory(SmartSequenceBuilder.FACTORY);
        }
        return _fastaBuilderFactory;
    }

    /**
     * Iterate over the sequences in an FASTA-format stream of DNA sequences.
     */
    public static SequenceIterator readFastaDNA(BufferedReader br) {
        return new StreamReader(br,
                                new FastaFormat(),
                                getDNAParser(),
                                getFastaBuilderFactory());
    }

    /**
     * Iterate over the sequences in an FASTA-format stream of RNA sequences.
     */
    public static SequenceIterator readFastaRNA(BufferedReader br) {
        return new StreamReader(br,
                                new FastaFormat(),
                                getRNAParser(),
                                getFastaBuilderFactory());
    }

    /**
     * Iterate over the sequences in an FASTA-format stream of Protein sequences.
     */
    public static SequenceIterator readFastaProtein(BufferedReader br) {
        return new StreamReader(br,
                                new FastaFormat(),
                                getProteinParser(),
                                getFastaBuilderFactory());
    }

    /**
     * Create a sequence database from a fasta file provided as an
     * input stream.  Note this somewhat duplicates functionality in
     * the readFastaDNA and readFastaProtein methods but uses a stream
     * rather than a reader and returns a SequenceDB rather than a
     * SequenceIterator. If the returned DB is likely to be large then
     * the above mentioned methods should be used.
     *
     * @throws BioException if problems occur during reading of the
     * stream.
     * @since 1.2
     */
    public static SequenceDB readFasta(InputStream seqFile, Alphabet alpha)
        throws BioException {
        HashSequenceDB db = new HashSequenceDB(IDMaker.byName);
        SequenceBuilderFactory sbFact =
            new FastaDescriptionLineParser.Factory(SmartSequenceBuilder.FACTORY);
        FastaFormat fFormat = new FastaFormat();
        for (SequenceIterator seqI = new StreamReader(seqFile,
                                                      fFormat,
                                                      alpha.getTokenization("token"),
                                                      sbFact);seqI.hasNext();) {
            Sequence seq = seqI.nextSequence();
            try {
                db.addSequence(seq);
            } catch (ChangeVetoException cve) {
                throw new NestedError(cve,"Could not successfully add sequence "
                                      + seq.getName()
                                      + " to sequence database");
            }
        }
        return db;
    }

    /**
     * Write a sequenceDB to an output stream in fasta format.
     *
     * @throws IOException if there was an error while writing.
     * @since 1.2
     */
    public static void writeFasta(OutputStream os, SequenceDB db)
        throws IOException {
        StreamWriter sw = new StreamWriter(os,new FastaFormat());
        sw.writeStream(db.sequenceIterator());
    }

    /**
     * Writes sequences from a SequenceIterator to an OutputStream in
     * Fasta Format.  This makes for a useful format filter where a
     * StreamReader can be sent to the StreamWriter after formatting.
     *
     * @throws IOException if there was an error while writing.
     * @since 1.2
     */
    public static void writeFasta(OutputStream os, SequenceIterator in)
        throws IOException {
        StreamWriter sw = new StreamWriter(os,new FastaFormat());
        sw.writeStream(in);
    }

    /**
     * Writes a single Sequence to an OutputStream in Fasta format.
     *
     * @param os  the OutputStream.
     * @param seq  the Sequence.
     * @throws IOException if there was an error while writing.
     */
    public static void writeFasta(OutputStream os, Sequence seq)
        throws IOException {
        writeFasta(os, new SingleSeqIterator(seq));
    }

    /**
     * Writes a stream of Sequences to an OutputStream in EMBL format.
     *
     * @param os the OutputStream.
     * @param in a SequenceIterator.
     * @exception IOException if there was an error while writing.
     */
    public static void writeEmbl(OutputStream os, SequenceIterator in)
        throws IOException {
        StreamWriter sw = new StreamWriter(os, new EmblLikeFormat());
        sw.writeStream(in);
    }

    /**
     * Writes a single Sequence to an OutputStream in EMBL format.
     *
     * @param os  the OutputStream.
     * @param seq  the Sequence.
     * @throws IOException if there was an error while writing.
     */
    public static void writeEmbl(OutputStream os, Sequence seq) throws IOException {
        writeEmbl(os, new SingleSeqIterator(seq));
    }

    /**
     * Writes a stream of Sequences to an OutputStream in SwissProt
     * format.
     *
     * @param os the OutputStream.
     * @param in a SequenceIterator.
     * @exception IOException if there was an error while writing.
     */
    public static void writeSwissprot(OutputStream os, SequenceIterator in)
        throws IOException, BioException {
        SequenceFormat former = new EmblLikeFormat();
        PrintStream ps = new PrintStream(os);
        while (in.hasNext()) {
            former.writeSequence(in.nextSequence(), ps);
        }
    }

    /**
     * Writes a single Sequence to an OutputStream in SwissProt format.
     *
     * @param os  the OutputStream.
     * @param seq  the Sequence.
     * @throws IOException if there was an error while writing.
     */
    public static void writeSwissprot(OutputStream os, Sequence seq)
        throws IOException, BioException {
        writeSwissprot(os, new SingleSeqIterator(seq));
    }

    /**
     * Writes a stream of Sequences to an OutputStream in Genpept
     * format.
     *
     * @param os the OutputStream.
     * @param in a SequenceIterator.
     * @exception IOException if there was an error while writing.
     */
    public static void writeGenpept(OutputStream os, SequenceIterator in)
        throws IOException, BioException {
        SequenceFormat former = new GenbankFormat();
        PrintStream ps = new PrintStream(os);
        while (in.hasNext()) {
            former.writeSequence(in.nextSequence(), ps);
        }
    }

    /**
     * Writes a single Sequence to an OutputStream in Genpept format.
     *
     * @param os  the OutputStream.
     * @param seq  the Sequence.
     * @throws IOException if there was an error while writing.
     */
    public static void writeGenpept(OutputStream os, Sequence seq)
    throws IOException, BioException {
      writeGenpept(os, new SingleSeqIterator(seq));
    }

    /**
     * Writes a stream of Sequences to an OutputStream in Genbank
     * format.
     *
     * @param os the OutputStream.
     * @param in a SequenceIterator.
     * @exception IOException if there was an error while writing.
     */
    public static void writeGenbank(OutputStream os, SequenceIterator in)
        throws IOException {
        StreamWriter sw = new StreamWriter(os, new GenbankFormat());
        sw.writeStream(in);
    }

    /**
     * Writes a single Sequence to an OutputStream in Genbank format.
     *
     * @param os  the OutputStream.
     * @param seq  the Sequence.
     * @throws IOException if there was an error while writing.
     */
    public static void writeGenbank(OutputStream os, Sequence seq)
        throws IOException {
        writeGenbank(os, new SingleSeqIterator(seq));
    }

   /**
     * <code>identifyFormat</code> performs a case-insensitive mapping
     * of a pair of common sequence format name (such as 'embl',
     * 'genbank' or 'fasta') and alphabet name (such as 'dna', 'rna',
     * 'protein', 'aa') to an integer. The value returned will be one
     * of the public static final fields in
     * <code>SeqIOConstants</code>, or a bitwise-or combination of
     * them. The method will reject known illegal combinations of
     * format and alphabet (such as swissprot + dna) by throwing an
     * <code>IllegalArgumentException</code>. It will return the
     * <code>SeqIOConstants.UNKNOWN</code> value when either format or
     * alphabet are unknown.
     *
     * @param formatName a <code>String</code>.
     * @param alphabetName a <code>String</code>.
     *
     * @return an <code>int</code>.
     */
    public static int identifyFormat(String formatName, String alphabetName) {
        int format, alpha;
        if (formatName.equalsIgnoreCase("raw")) {
            format = SeqIOConstants.RAW;
        }
        else if (formatName.equalsIgnoreCase("fasta")) {
            format = SeqIOConstants.FASTA;
        }
        else if (formatName.equalsIgnoreCase("nbrf")) {
            format = SeqIOConstants.NBRF;
        }
        else if (formatName.equalsIgnoreCase("ig")) {
            format = SeqIOConstants.IG;
        }
        else if (formatName.equalsIgnoreCase("embl")) {
            format = SeqIOConstants.EMBL;
        }
        else if (formatName.equalsIgnoreCase("swissprot") ||
                 formatName.equalsIgnoreCase("swiss")) {
            if (alphabetName.equalsIgnoreCase("aa") ||
                alphabetName.equalsIgnoreCase("protein")) {
                return SeqIOConstants.SWISSPROT;
            } else {
                throw new IllegalArgumentException("Illegal format and alphabet "
                                                   + "combination "
                                                   + formatName
                                                   + " + "
                                                   + alphabetName);
            }
        } else if (formatName.equalsIgnoreCase("genbank")) {
            format = SeqIOConstants.GENBANK;
        } else if (formatName.equalsIgnoreCase("genpept")) {
            if (alphabetName.equalsIgnoreCase("aa") ||
                alphabetName.equalsIgnoreCase("protein")) {
                return SeqIOConstants.GENPEPT;
            } else {
                throw new IllegalArgumentException("Illegal format and alphabet "
                                                   + "combination "
                                                   + formatName
                                                   + " + "
                                                   + alphabetName);
            }
        } else if (formatName.equalsIgnoreCase("refseq")) {
            format = SeqIOConstants.REFSEQ;
        } else if (formatName.equalsIgnoreCase("gcg")) {
            format = SeqIOConstants.GCG;
        } else if (formatName.equalsIgnoreCase("gff")) {
            format = SeqIOConstants.GFF;
        }
        else if (formatName.equalsIgnoreCase("pdb")) {
            if (alphabetName.equalsIgnoreCase("aa") ||
                alphabetName.equalsIgnoreCase("protein")) {
                return SeqIOConstants.PDB;
            } else {
                throw new IllegalArgumentException("Illegal format and alphabet "
                                                   + "combination "
                                                   + formatName
                                                   + " + "
                                                   + alphabetName);
            }
        } else if (formatName.equalsIgnoreCase("phred")) {
            if (alphabetName.equalsIgnoreCase("dna")) {
                return SeqIOConstants.PHRED;
            } else {
                throw new IllegalArgumentException("Illegal format and alphabet "
                                                   + "combination "
                                                   + formatName
                                                   + " + "
                                                   + alphabetName);
            }
        } else {
            return SeqIOConstants.UNKNOWN;
        }

        if (alphabetName.equalsIgnoreCase("dna")) {
            alpha = SeqIOConstants.DNA;
        } else if (alphabetName.equalsIgnoreCase("rna")) {
            alpha = SeqIOConstants.RNA;
        } else if (alphabetName.equalsIgnoreCase("aa") ||
                 alphabetName.equalsIgnoreCase("protein")) {
            alpha = SeqIOConstants.AA;
        } else {
            return SeqIOConstants.UNKNOWN;
        }

        return (format | alpha);
    }

    /**
     * <code>getSequenceFormat</code> accepts a value which represents
     * a sequence format and returns the relevant
     * <code>SequenceFormat</code> object.
     *
     * @param identifier an <code>int</code> which represents a binary
     * value with bits set according to the scheme described in
     * <code>SeqIOConstants</code>.
     *
     * @return a <code>SequenceFormat</code>.
     *
     * @exception BioException if an error occurs.
     */
    public static SequenceFormat getSequenceFormat(int identifier)
        throws BioException {

        // Mask the sequence format bytes
        int alphaType = identifier & (~ 0xffff);
        if (alphaType == 0)
            throw new IllegalArgumentException("No alphabet was set in the identifier");

        // Mask alphabet bytes
        int formatType = identifier & (~ 0xffff0000);
        if (formatType == 0)
            throw new IllegalArgumentException("No format was set in the identifier");

        switch (identifier) {
            case SeqIOConstants.FASTA_DNA:
            case SeqIOConstants.FASTA_RNA:
            case SeqIOConstants.FASTA_AA:
                return new FastaFormat();
            case SeqIOConstants.EMBL_DNA:
            case SeqIOConstants.EMBL_RNA:
                return new EmblLikeFormat();
            case SeqIOConstants.GENBANK_DNA:
            case SeqIOConstants.GENBANK_RNA:
                return new GenbankFormat();
            case SeqIOConstants.SWISSPROT:
                return new EmblLikeFormat();
            default:
                throw new BioException("No SequenceFormat available for "
                                       + "format/alphabet identifier '"
                                       + identifier
                                       + "'");
        }
    }

    /**
     * <code>getBuilderFactory</code> accepts a value which represents
     * a sequence format and returns the relevant
     * <code>SequenceBuilderFactory</code> object.
     *
     * @param identifier an <code>int</code> which represents a binary
     * value with bits set according to the scheme described in
     * <code>SeqIOConstants</code>.
     *
     * @return a <code>SequenceBuilderFactory</code>.
     *
     * @exception BioException if an error occurs.
     */
    public static SequenceBuilderFactory getBuilderFactory(int identifier)
        throws BioException {

        // Mask the sequence format bytes
        int alphaType = identifier & (~ 0xffff);
        if (alphaType == 0)
            throw new IllegalArgumentException("No alphabet was set in the identifier");

        // Mask alphabet bytes
        int formatType = identifier & (~ 0xffff0000);
        if (formatType == 0)
            throw new IllegalArgumentException("No format was set in the identifier");

        switch (identifier) {
            case SeqIOConstants.FASTA_DNA:
            case SeqIOConstants.FASTA_RNA:
            case SeqIOConstants.FASTA_AA:
                return getFastaBuilderFactory();
            case SeqIOConstants.EMBL_DNA:
                    return getEmblBuilderFactory();
            case SeqIOConstants.GENBANK_DNA:
                return getGenbankBuilderFactory();
            case SeqIOConstants.SWISSPROT:
                return getSwissprotBuilderFactory();
            case SeqIOConstants.GENPEPT:
                return getGenpeptBuilderFactory();
            default:
                throw new BioException("No SequenceBuilderFactory available for "
                                       + "format/alphabet identifier '"
                                       + identifier
                                       + "'");
        }
    }

    /**
     * <code>getAlphabet</code> accepts a value which represents a
     * sequence format and returns the relevant
     * <code>FiniteAlphabet</code> object.
     *
     * @param identifier an <code>int</code> which represents a binary
     * value with bits set according to the scheme described in
     * <code>SeqIOConstants</code>.
     *
     * @return a <code>FiniteAlphabet</code>.
     *
     * @exception BioException if an error occurs.
     */
    public static FiniteAlphabet getAlphabet(int identifier)
        throws BioException {

        // Mask the sequence format bytes
        int alphaType = identifier & (~ 0xffff);
        if (alphaType == 0)
            throw new IllegalArgumentException("No alphabet was set in the identifier");

        switch (alphaType) {
            case SeqIOConstants.DNA:
                return DNATools.getDNA();
            case SeqIOConstants.RNA:
                return RNATools.getRNA();
            case SeqIOConstants.AA:
                return ProteinTools.getAlphabet();
            default:
                throw new BioException("No FiniteAlphabet available for "
                                       + "alphabet identifier '"
                                       + identifier
                                       + "'");
        }
    }

    /**
     * The following methods provide an alternate interface for
     * reading and writing sequences and alignments. (Nimesh Singh).
     *
     */

    /**
     * Attempts to guess the filetype of a file given the name.  For
     * use with the functions below that take an int fileType as a
     * parameter.
     *
     * @deprecated because there is no standard file naming convention
     * and guessing by file name is inherantly error prone and bad.
     */
    public static int guessFileType(File seqFile)
        throws IOException, FileNotFoundException {
        //First tries by matching an extension
        String fileName = seqFile.getName();
        try {
            if ((new RE(".*\\u002eem.*")).match(fileName)) {
                return SeqIOConstants.EMBL;
            }
            else if ((new RE(".*\\u002edat.*")).match(fileName)) {
                return SeqIOConstants.EMBL;
            }
            else if ((new RE(".*\\u002egb.*")).match(fileName)) {
                return SeqIOConstants.GENBANK;
            }
            else if ((new RE(".*\\u002esp.*")).match(fileName)) {
                return SeqIOConstants.SWISSPROT;
            }
            else if ((new RE(".*\\u002egp.*")).match(fileName)) {
                return SeqIOConstants.GENPEPT;
            }
            else if ((new RE(".*\\u002efa.*")).match(fileName)) {
                return guessFastaType(seqFile);
            }
            else if ((new RE(".*\\u002emsf.*")).match(fileName)) {
                return guessMsfType(seqFile);
            }
        } catch (RESyntaxException e) {
            throw new BioError(e, "Internal error in SeqIOTools");
        }

        //Reads the file to guess based on content
        BufferedReader br = new BufferedReader(new FileReader(seqFile));
        String line1 = br.readLine();
        String line2 = br.readLine();
        br.close();

        if (line1.startsWith(">")) {
            return guessFastaType(seqFile);
        }
        else if (line1.startsWith("PileUp")) {
            return guessMsfType(seqFile);
        }
        else if (line1.startsWith("!!AA_MULTIPLE_ALIGNMENT")) {
            return AlignIOConstants.MSF_AA;
        }
        else if (line1.startsWith("!!NA_MULTIPLE_ALIGNMENT")) {
            return AlignIOConstants.MSF_DNA;
        }
        else if (line1.startsWith("ID")) {
            for (int i = 0; i < line1.length(); i++) {
                if (Character.toUpperCase(line1.charAt(i)) == 'P' &&
                    Character.toUpperCase(line1.charAt(i+1)) == 'R' &&
                    Character.toUpperCase(line1.charAt(i+2)) == 'T') {
                    return SeqIOConstants.SWISSPROT;
                }
            }
            return SeqIOConstants.EMBL_DNA;
        }
        else if (line1.toUpperCase().startsWith("LOCUS")) {
            for (int i = 0; i < line1.length(); i++) {
                if (Character.toUpperCase(line1.charAt(i)) == 'A' &&
                    Character.toUpperCase(line1.charAt(i+1)) == 'A') {
                    return SeqIOConstants.GENPEPT;
                }
            }
            return SeqIOConstants.GENBANK_DNA;
        }
        else if (line1.length() >= 45 &&
                 line1.substring(19, 45).equalsIgnoreCase("GENETIC SEQUENCE DATA BANK")) {
            return guessGenType(fileName);
        }
        else {
            return SeqIOConstants.UNKNOWN;
        }
    }

    /**
     * Attempts to retrieve the most appropriate
     * <code>SequenceBuilder</code> object for some combination of
     * <code>Alphabet</code> and <code>SequenceFormat</code>
     *
     * @param format currently supports <code>FastaFormat</code>,
     * <code>GenbankFormat</code>, <code>EmblLikeFormat</code>
     * @param alpha currently only supports the DNA and Protein
     * alphabets
     *
     * @return the <code>SequenceBuilderFactory</code>
     *
     * @throws BioException if the combination of alpha and format is
     * unrecognized.
     *
     * @deprecated as this essentially duplicates the operation
     * available in the method <code>identifyBuilderFactory</code>.
     */
    public static SequenceBuilderFactory formatToFactory(SequenceFormat format,
                                                         Alphabet alpha)
        throws BioException {

        if ((format instanceof FastaFormat) &&
           (alpha == DNATools.getDNA() ||
            alpha == ProteinTools.getAlphabet())) {

            return getFastaBuilderFactory();
        }
        else if (format instanceof GenbankFormat &&
                alpha == DNATools.getDNA()) {

            return getGenbankBuilderFactory();
        }
        else if (format instanceof GenbankFormat &&
                 alpha == ProteinTools.getAlphabet()) {
            return getGenpeptBuilderFactory();
        }
        else if (format instanceof EmblLikeFormat &&
                 alpha == DNATools.getDNA()){
            return getEmblBuilderFactory();
        }
        else if (format instanceof EmblLikeFormat &&
                 alpha == ProteinTools.getAlphabet()) {
            return getSwissprotBuilderFactory();
        }
        else {
            throw new BioException("Unknown combination of"
                                   + " Alphabet and Format");
      }
    }

    /**
     * Reads a file and returns the corresponding Biojava object. You
     * need to cast it as an Alignment or a SequenceIterator as
     * appropriate.
     */
    public static Object fileToBiojava(int fileType, BufferedReader br)
        throws BioException {

        // Mask the sequence format bytes
        int alphaType = fileType & (~ 0xffff);
        if (alphaType == 0)
            throw new IllegalArgumentException("No alphabet was set in the identifier");

        // Mask alphabet bytes
        int formatType = fileType & (~ 0xffff0000);
        if (formatType == 0)
            throw new IllegalArgumentException("No format was set in the identifier");

        switch (fileType) {
            case AlignIOConstants.MSF_DNA:
            case AlignIOConstants.MSF_AA:
            case AlignIOConstants.FASTA_DNA:
            case AlignIOConstants.FASTA_AA:
                return fileToAlign(fileType, br);
            case SeqIOConstants.FASTA_DNA:
            case SeqIOConstants.FASTA_AA:
            case SeqIOConstants.EMBL_DNA:
            case SeqIOConstants.GENBANK_DNA:
            case SeqIOConstants.SWISSPROT:
            case SeqIOConstants.GENPEPT:
                return fileToSeq(fileType, br);
            default:
                throw new BioException("Unknown file type '"
                                       + fileType
                                       + "'");
        }
    }

    /**
     * Converts a Biojava object to the given filetype.
     */
    public static void biojavaToFile(int fileType, OutputStream os,
                                     Object biojava)
        throws BioException, IOException, IllegalSymbolException {
        switch (fileType) {
            case AlignIOConstants.MSF_DNA:
            case AlignIOConstants.MSF_AA:
            case AlignIOConstants.FASTA_DNA:
            case AlignIOConstants.FASTA_AA:
                alignToFile(fileType, os, (Alignment) biojava);
                break;
            case SeqIOConstants.FASTA_DNA:
            case SeqIOConstants.FASTA_AA:
            case SeqIOConstants.EMBL_DNA:
            case SeqIOConstants.GENBANK_DNA:
            case SeqIOConstants.SWISSPROT:
            case SeqIOConstants.GENPEPT:
                seqToFile(fileType, os, (SequenceIterator) biojava);
                break;
            default:
                throw new BioException("Unknown file type '"
                                       + fileType
                                       + "'");
        }
    }

    /**
     * Helper function for guessFileName.
     */
    private static int guessFastaType(File seqFile)
        throws IOException, FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(seqFile));
        String line = br.readLine();
        line = br.readLine();
        br.close();
        for (int i = 0; i < line.length(); i++) {
            if (Character.toUpperCase(line.charAt(i)) == 'F' ||
                Character.toUpperCase(line.charAt(i)) == 'L' ||
                Character.toUpperCase(line.charAt(i)) == 'I' ||
                Character.toUpperCase(line.charAt(i)) == 'P' ||
                Character.toUpperCase(line.charAt(i)) == 'Q' ||
                Character.toUpperCase(line.charAt(i)) == 'E') {
                return SeqIOConstants.FASTA_AA;
            }
        }

        return SeqIOConstants.FASTA_DNA;
    }

    private static SymbolTokenization getDNAParser() {
        try {
            return DNATools.getDNA().getTokenization("token");
        } catch (BioException ex) {
            throw new BioError(ex, "Assertion failing:"
                               + " Couldn't get DNA token parser");
        }
    }

    private static SymbolTokenization getRNAParser() {
        try {
            return RNATools.getRNA().getTokenization("token");
        } catch (BioException ex) {
            throw new BioError(ex, "Assertion failing:"
                               + " Couldn't get RNA token parser");
        }
    }

    private static SymbolTokenization getNucleotideParser() {
        try {
            return NucleotideTools.getNucleotide().getTokenization("token");
        } catch (BioException ex) {
            throw new BioError(ex, "Assertion failing:"
                               + " Couldn't get nucleotide token parser");
        }
    }

    private static SymbolTokenization getProteinParser() {
        try {
            return ProteinTools.getTAlphabet().getTokenization("token");
        } catch (BioException ex) {
            throw new BioError(ex, "Assertion failing:"
                               + " Couldn't get PROTEIN token parser");
        }
    }

    /**
     * Helper function for guessFileName.
     */
    private static int guessMsfType(File seqFile)
        throws IOException, FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(seqFile));
        String line = br.readLine();
        if (line.startsWith("!!NA_MULTIPLE_ALIGNMENT")) {
            return AlignIOConstants.MSF_DNA;
        }
        else if (line.startsWith("!!AA_MULTIPLE_ALIGNMENT")) {
            return AlignIOConstants.MSF_AA;
        }
        else {
            while (line.indexOf("Type: ") == -1) {
                line = br.readLine();
            }
            br.close();
            int typeIndex = line.indexOf("Type: ") + 6;
            if (line.substring(typeIndex).startsWith("N")) {
                return AlignIOConstants.MSF_DNA;
            }
            else if (line.substring(typeIndex).startsWith("P")) {
                return AlignIOConstants.MSF_AA;
            }
            else {
                return AlignIOConstants.UNKNOWN;
            }
        }
    }

    /**
     * Helper function for guessFileName.
     */
    private static int guessGenType(String fileName)
        throws IOException, FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = br.readLine();
        while (line.indexOf("LOCUS") == -1) {
            line = br.readLine();
        }
        br.close();
        for (int i = 0; i < line.length(); i++) {
            if (Character.toUpperCase(line.charAt(i)) == 'A' &&
                Character.toUpperCase(line.charAt(i+1)) == 'A') {
                    return SeqIOConstants.GENPEPT;
            }
        }
        return SeqIOConstants.GENBANK;
    }

    /**
     * Converts a file to an Biojava alignment.
     */
    private static Alignment fileToAlign(int fileType, BufferedReader br)
        throws BioException {
        switch(fileType) {
            case AlignIOConstants.MSF_DNA:
            case AlignIOConstants.MSF_AA:
                return (new MSFAlignmentFormat()).read(br);
            case AlignIOConstants.FASTA_DNA:
            case AlignIOConstants.FASTA_AA:
                return (new FastaAlignmentFormat()).read(br);
            default:
                throw new BioException("Unknown file type '"
                                       + fileType
                                       + "'");
        }
    }

    /**
     * Converts a file to a Biojava sequence.
     */
    private static SequenceIterator fileToSeq(int fileType,
                                              BufferedReader br)
        throws BioException {
        switch (fileType) {
            case SeqIOConstants.FASTA_DNA:
                return SeqIOTools.readFastaDNA(br);
            case SeqIOConstants.FASTA_AA:
                return SeqIOTools.readFastaProtein(br);
            case SeqIOConstants.EMBL_DNA:
                return SeqIOTools.readEmbl(br);
            case SeqIOConstants.GENBANK_DNA:
                return SeqIOTools.readGenbank(br);
            case SeqIOConstants.SWISSPROT:
                return SeqIOTools.readSwissprot(br);
            case SeqIOConstants.GENPEPT:
                return SeqIOTools.readGenpept(br);
            default:
                throw new BioException("Unknown file type '"
                                       + fileType
                                       + "'");
        }
    }

    /**
     * Converts a Biojava alignment to the given filetype.
     */
    private static void alignToFile(int fileType, OutputStream os,
                                    Alignment align)
        throws BioException, IllegalSymbolException {
        switch(fileType) {
            case AlignIOConstants.MSF_DNA:
                (new MSFAlignmentFormat()).writeDna(os, align);
                break;
            case AlignIOConstants.MSF_AA:
                (new MSFAlignmentFormat()).writeProtein(os, align);
                break;
            case AlignIOConstants.FASTA_DNA:
                (new FastaAlignmentFormat()).writeDna(os, align);
                break;
            case AlignIOConstants.FASTA_AA:
                (new FastaAlignmentFormat()).writeProtein(os, align);
                break;
            default:
                throw new BioException("Unknown file type '"
                                       + fileType
                                       + "'");
        }
    }

    /**
     * Converts a Biojava sequence to the given filetype.
     */
    private static void seqToFile(int fileType, OutputStream os,
                                  SequenceIterator seq)
        throws IOException, BioException {
        switch (fileType) {
            case SeqIOConstants.FASTA_DNA:
            case SeqIOConstants.FASTA_AA:
                SeqIOTools.writeFasta(os, seq);
                break;
            case SeqIOConstants.EMBL_DNA:
                SeqIOTools.writeEmbl(os, seq);
                break;
            case SeqIOConstants.SWISSPROT:
                SeqIOTools.writeSwissprot(os, seq);
                break;
            case SeqIOConstants.GENBANK_DNA:
                SeqIOTools.writeGenbank(os, seq);
                break;
            case SeqIOConstants.GENPEPT:
                SeqIOTools.writeGenpept(os, seq);
                break;
            default:
                throw new BioException("Unknown file type '"
                                       + fileType
                                       + "'");
        }
    }

    private static final class SingleSeqIterator
        implements SequenceIterator {
        private Sequence seq;
        SingleSeqIterator(Sequence seq) {
            this.seq = seq;
        }

        public boolean hasNext() {
            return seq != null;
        }

        public Sequence nextSequence() {
            Sequence seq = this.seq;
            this.seq = null;
            return seq;
        }
    }
}
