package com.huawei.datasight.molap.partition.reader;

/**
 Copyright 2005 Bytecode Pty Ltd.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.huawei.unibi.molap.constants.MolapCommonConstants;

/**
 * A very simple CSV parser released under a commercial-friendly license.
 * This just implements splitting a single line into fields.
 *
 * @author Glen Smith
 * @author Rainer Pruy
 */
public class CSVParser {

    /**
     * The default separator to use if none is supplied to the constructor.
     */
    public static final char DEFAULT_SEPARATOR = ',';
    /**
     * The average size of a line read by openCSV (used for setting the size of StringBuilders).
     */
    public static final int INITIAL_READ_SIZE = 128;
    /**
     * The default quote character to use if none is supplied to the
     * constructor.
     */
    public static final char DEFAULT_QUOTE_CHARACTER = '"';
    /**
     * The default escape character to use if none is supplied to the
     * constructor.
     */
    public static final char DEFAULT_ESCAPE_CHARACTER = '\\';
    /**
     * The default strict quote behavior to use if none is supplied to the
     * constructor.
     */
    public static final boolean DEFAULT_STRICT_QUOTES = false;
    /**
     * The default leading whitespace behavior to use if none is supplied to the
     * constructor.
     */
    public static final boolean DEFAULT_IGNORE_LEADING_WHITESPACE = true;
    /**
     * If the quote character is set to null then there is no quote character.
     */
    public static final boolean DEFAULT_IGNORE_QUOTATIONS = false;
    /**
     * This is the "null" character - if a value is set to this then it is ignored.
     */
    public static final char NULL_CHARACTER = '\0';
    /**
     * This is the character that the CSVParser will treat as the separator.
     */
    private final char separator;
    /**
     * This is the character that the CSVParser will treat as the quotation character.
     */
    private final char quotechar;
    /**
     * This is the character that the CSVParser will treat as the escape character.
     */
    private final char escape;
    /**
     * Determines if the field is between quotes (true) or between separators (false).
     */
    private final boolean strictQuotes;
    /**
     * Ignore any leading white space at the start of the field.
     */
    private final boolean ignoreLeadingWhiteSpace;
    /**
     * Skip over quotation characters when parsing.
     */
    private final boolean ignoreQuotations;
    private boolean ignoreEscapeChar;
    private String pending;
    private boolean inField;
    private int charCountInsideQuote;

    public void setPending(String pending)
    {
        this.pending = pending;
    }
    
    /**
     * Constructs CSVParser using a comma for the separator.
     */
    public CSVParser() {
        this(DEFAULT_SEPARATOR, DEFAULT_QUOTE_CHARACTER, DEFAULT_ESCAPE_CHARACTER);
    }

    /**
     * Constructs CSVParser with supplied separator.
     *
     * @param separator the delimiter to use for separating entries.
     */
    public CSVParser(char separator) {
        this(separator, DEFAULT_QUOTE_CHARACTER, DEFAULT_ESCAPE_CHARACTER);
    }


    /**
     * Constructs CSVParser with supplied separator and quote char.
     *
     * @param separator the delimiter to use for separating entries
     * @param quotechar the character to use for quoted elements
     */
    public CSVParser(char separator, char quotechar) {
        this(separator, quotechar, DEFAULT_ESCAPE_CHARACTER);
    }

    /**
     * Constructs CSVReader with supplied separator and quote char.
     *
     * @param separator the delimiter to use for separating entries
     * @param quotechar the character to use for quoted elements
     * @param escape    the character to use for escaping a separator or quote
     */
    public CSVParser(char separator, char quotechar, char escape) {
        this(separator, quotechar, escape, DEFAULT_STRICT_QUOTES);
    }

    /**
     * Constructs CSVParser with supplied separator and quote char.
     * Allows setting the "strict quotes" flag
     *
     * @param separator    the delimiter to use for separating entries
     * @param quotechar    the character to use for quoted elements
     * @param escape       the character to use for escaping a separator or quote
     * @param strictQuotes if true, characters outside the quotes are ignored
     */
    public CSVParser(char separator, char quotechar, char escape, boolean strictQuotes) {
        this(separator, quotechar, escape, strictQuotes, DEFAULT_IGNORE_LEADING_WHITESPACE);
    }

    /**
     * Constructs CSVParser with supplied separator and quote char.
     * Allows setting the "strict quotes" and "ignore leading whitespace" flags
     *
     * @param separator               the delimiter to use for separating entries
     * @param quotechar               the character to use for quoted elements
     * @param escape                  the character to use for escaping a separator or quote
     * @param strictQuotes            if true, characters outside the quotes are ignored
     * @param ignoreLeadingWhiteSpace if true, white space in front of a quote in a field is ignored
     */
    public CSVParser(char separator, char quotechar, char escape, boolean strictQuotes, boolean ignoreLeadingWhiteSpace) {
        this(separator, quotechar, escape, strictQuotes, ignoreLeadingWhiteSpace, DEFAULT_IGNORE_QUOTATIONS);
    }

    /**
     * Constructs CSVParser with supplied separator and quote char.
     * Allows setting the "strict quotes" and "ignore leading whitespace" flags
     *
     * @param separator               the delimiter to use for separating entries
     * @param quotechar               the character to use for quoted elements
     * @param escape                  the character to use for escaping a separator or quote
     * @param strictQuotes            if true, characters outside the quotes are ignored
     * @param ignoreLeadingWhiteSpace if true, white space in front of a quote in a field is ignored
     * @param ignoreQuotations        if true, treat quotations like any other character.
     */
    public CSVParser(char separator, char quotechar, char escape, boolean strictQuotes, boolean ignoreLeadingWhiteSpace,
                     boolean ignoreQuotations) {
        if (anyCharactersAreTheSame(separator, quotechar, escape)) {
            throw new UnsupportedOperationException("The separator, quote, and escape characters must be different!");
        }
        if (separator == NULL_CHARACTER) {
            throw new UnsupportedOperationException("The separator character must be defined!");
        }
        this.separator = separator;
        this.quotechar = quotechar;
        this.escape = escape;
        this.strictQuotes = strictQuotes;
        this.ignoreLeadingWhiteSpace = ignoreLeadingWhiteSpace;
        this.ignoreQuotations = ignoreQuotations;
    }

    /**
     * Constructs CSVParser with supplied separator and quote char.
     * Allows setting the "strict quotes" and "ignore leading whitespace" flags
     *
     * @param separator               the delimiter to use for separating entries
     * @param quotechar               the character to use for quoted elements
     * @param escape                  the character to use for escaping a separator or quote
     * @param strictQuotes            if true, characters outside the quotes are ignored
     * @param ignoreLeadingWhiteSpace if true, white space in front of a quote in a field is ignored
     * @param ignoreQuotations        if true, treat quotations like any other character.
     */
    public CSVParser(char separator, char quotechar, char escape, boolean strictQuotes, boolean ignoreLeadingWhiteSpace,
                     boolean ignoreQuotations, boolean ignoreEscape) {
        this(separator, quotechar, escape, strictQuotes, ignoreLeadingWhiteSpace, ignoreQuotations);
        this.ignoreEscapeChar = ignoreEscape;
    }
    
    /**
     * @return The default separator for this parser.
     */
    public char getSeparator() {
        return separator;
    }

    /**
     * @return The default quotation character for this parser.
     */
    public char getQuotechar() {
        return quotechar;
    }

    /**
     * @return The default escape character for this parser.
     */
    public char getEscape() {
        return escape;
    }

    /**
     * @return The default strictQuotes setting for this parser.
     */
    public boolean isStrictQuotes() {
        return strictQuotes;
    }

    /**
     * @return The default ignoreLeadingWhiteSpace setting for this parser.
     */
    public boolean isIgnoreLeadingWhiteSpace() {
        return ignoreLeadingWhiteSpace;
    }

    /**
     * @return the default ignoreQuotation setting for this parser.
     */
    public boolean isIgnoreQuotations() {
        return ignoreQuotations;
    }

    /**
     * checks to see if any two of the three characters are the same.  This is because in openCSV the
     * separator, quote, and escape characters must the different.
     *
     * @param separator the defined separator character
     * @param quotechar the defined quotation cahracter
     * @param escape    the defined escape character
     * @return true if any two of the three are the same.
     */
    private boolean anyCharactersAreTheSame(char separator, char quotechar, char escape) {
        return isSameCharacter(separator, quotechar) || isSameCharacter(separator, escape) || isSameCharacter(quotechar, escape);
    }

    /**
     * checks that the two characters are the same and are not the defined NULL_CHARACTER.
     * @param c1 first character
     * @param c2 second character
     * @return true if both characters are the same and are not the defined NULL_CHARACTER
     */
    private boolean isSameCharacter(char c1, char c2) {
        return c1 != NULL_CHARACTER && c1 == c2;
    }

    /**
     * @return true if something was left over from last call(s)
     */
    public boolean isPending() {
        return pending != null;
    }

    /**
     * Parses an incoming String and returns an array of elements.  This method is used when the
     * data spans multiple lines.
     *
     * @param nextLine current line to be processed
     * @return the comma-tokenized list of elements, or null if nextLine is null
     * @throws IOException if bad things happen during the read
     */
    public String[] parseLineMulti(String nextLine) throws IOException {
        return parseLine(nextLine, true);
    }

    /**
     * Parses an incoming String and returns an array of elements.  This method is used when all data is contained
     * in a single line.
     *
     * @param nextLine Line to be parsed.
     * @return the comma-tokenized list of elements, or null if nextLine is null
     * @throws IOException if bad things happen during the read
     */
    public String[] parseLine(String nextLine) throws IOException {
        return parseLine(nextLine, false);
    }

    /**
     * Parses an incoming String and returns an array of elements.
     *
     * @param nextLine the string to parse
     * @param multi Does it take multiple lines to form a single record.
     * @return the comma-tokenized list of elements, or null if nextLine is null
     * @throws IOException if bad things happen during the read
     */
    private String[] parseLine(String nextLine, boolean multi) throws IOException 
    {

        if (!multi && pending != null) {
            pending = null;
        }

        if (nextLine == null) {
            if (pending != null) {
                String s = pending;
                pending = null;
                return new String[]{s};
            } else {
                return null;
            }
        }

        List<String> tokensOnThisLine = new ArrayList<>(MolapCommonConstants.DEFAULT_COLLECTION_SIZE);      
        //CHECKSTYLE:OFF    Approval No:Approval-V1R2C10_010
        StringBuilder sb = new StringBuilder(INITIAL_READ_SIZE);
        
        boolean inQuotes = false;
        // CHECKSTYLE:ON
        if (pending != null) {
            sb.append(pending);
            pending = null;
            inQuotes = !this.ignoreQuotations;//true;
        }
        inQuotes = checkForQuotes(nextLine, tokensOnThisLine, sb, inQuotes);
        // line is done - check status
        if ((inQuotes && !ignoreQuotations)) {
            if (multi) {
                // continuing a quoted section, re-append newline
                sb.append('\n');
                charCountInsideQuote = sb.length() - (sb.indexOf("\"")  + 1);
                if(charCountInsideQuote >= 10000) //max char count to wait till the quote terminates
                {
//                    System.out.println("Unterminated quoted field after 10000 characters in CSV Multi Line : " + sb.toString());
                    throw new IOException("Un-terminated quoted field after 10000 characters");
                }
                pending = sb.toString();
                sb = null; // this partial content is not to be added to field list yet
            } else {
                throw new IOException("Un-terminated quoted field at end of CSV line");
            }
        } else {
            inField = false;
            charCountInsideQuote = 0;
        }

        if (sb != null) {
            tokensOnThisLine.add(sb.toString());
        }
        return tokensOnThisLine.toArray(new String[tokensOnThisLine.size()]);

    }

	/**
	 * @param nextLine
	 * @param tokensOnThisLine
	 * @param sb
	 * @param inQuotes
	 * @return
	 */
	private boolean checkForQuotes(String nextLine,
			List<String> tokensOnThisLine, StringBuilder sb, boolean inQuotes) {
		for (int i = 0; i < nextLine.length(); i++) {

            char c = nextLine.charAt(i);
            //Naresh 00902756 : Modified to skip escape character check
            if (!this.ignoreEscapeChar && c == this.escape) {
                if (isNextCharacterEscapable(nextLine, inQuotes(inQuotes), i)) {
                    i = appendNextCharacterAndAdvanceLoop(nextLine, sb, i);
                }
            } else if (c == quotechar) {
                if (isNextCharacterEscapedQuote(nextLine, inQuotes(inQuotes), i)) {
                    i = appendNextCharacterAndAdvanceLoop(nextLine, sb, i);
                } else {
                    inQuotes = checkForStrictQuotes(nextLine, sb, inQuotes, i,
							c);
                }
                inField = !inField;
            } else if (c == separator && !(inQuotes && !ignoreQuotations)) {
                tokensOnThisLine.add(sb.toString());
                sb.setLength(0);
                inField = false;
            } else {
                if (!strictQuotes || (inQuotes && !ignoreQuotations)) {
                    sb.append(c);
                    inField = true;
                }
            }
        }
		return inQuotes;
	}

	/**
	 * @param nextLine
	 * @param sb
	 * @param inQuotes
	 * @param i
	 * @param c
	 * @return
	 */
	private boolean checkForStrictQuotes(String nextLine, StringBuilder sb,
			boolean inQuotes, int i, char c) {
		inQuotes = !inQuotes;

		// the tricky case of an embedded quote in the middle: a,bc"d"ef,g
		if (!strictQuotes) {
		    if (i > 2 //not on the beginning of the line
		            && nextLine.charAt(i - 1) != this.separator //not at the beginning of an escape sequence
		            && nextLine.length() > (i + 1) &&
		            nextLine.charAt(i + 1) != this.separator //not at the	end of an escape sequence
		            ) {

		        if (ignoreLeadingWhiteSpace && sb.length() > 0 && isAllWhiteSpace(sb)) {
		            sb.setLength(0);
		        } else {
		            sb.append(c);
		        }

		    }
		}
		return inQuotes;
	}

    /**
     * Appends the next character in the line to the stringbuffer.
     *
     * @param line - line to process
     * @param sb   - contains the processed character
     * @param i    - current position in the line.
     * @return new position in the line.
     */
    private int appendNextCharacterAndAdvanceLoop(String line, StringBuilder sb, int i) {
        sb.append(line.charAt(i + 1));
        i++;
        return i;
    }

    /**
     * Determines if we can process as if we were in quotes.
     *
     * @param inQuotes - are we currently in quotes.
     * @return - true if we should process as if we are inside quotes.
     */
    private boolean inQuotes(boolean inQuotes) {
        return (inQuotes && !ignoreQuotations) || inField;
    }

    /**
     * Checks to see if the character after the index is a quotation character.
     *
     * precondition: the current character is a quote or an escape
     *
     * @param nextLine the current line
     * @param inQuotes true if the current context is quoted
     * @param i        current index in line
     * @return true if the following character is a quote
     */
    private boolean isNextCharacterEscapedQuote(String nextLine, boolean inQuotes, int i) {
        return inQuotes  // we are in quotes, therefore there can be escaped quotes in here.
                && nextLine.length() > (i + 1)  // there is indeed another character to check.
                && isCharacterQuoteCharacter(nextLine.charAt(i + 1));
    }

    /**
     * Checks to see if the passed in character is the defined quotation character.
     *
     * @param c source character
     * @return true if c is the defined quotation character
     */
    private boolean isCharacterQuoteCharacter(char c) {
        return c == quotechar;
    }

    /**
     * checks to see if the character is the defined escape character.
     *
     * @param c source character
     * @return true if the character is the defined escape character
     */
    private boolean isCharacterEscapeCharacter(char c) {
        return c == escape;
    }

    /**
     * Checks to see if the character passed in could be escapable.  Escapable characters for openCSV are the
     * quotation character or the escape character.
     *
     * @param c source character
     * @return true if the character could be escapable.
     */
    private boolean isCharacterEscapable(char c) {
        return isCharacterQuoteCharacter(c) || isCharacterEscapeCharacter(c);
    }

    /**
     * Checks to see if the character after the current index in a String is an escapable character.
     * Meaning the next character is either a quotation character or the escape char and you are inside
     * quotes.
     *
     * precondition: the current character is an escape
     *
     * @param nextLine the current line
     * @param inQuotes true if the current context is quoted
     * @param i        current index in line
     * @return true if the following character is a quote
     */
    protected boolean isNextCharacterEscapable(String nextLine, boolean inQuotes, int i) {
        return inQuotes  // we are in quotes, therefore there can be escaped quotes in here.
                && nextLine.length() > (i + 1)  // there is indeed another character to check.
                && isCharacterEscapable(nextLine.charAt(i + 1));
    }

    /**
     * Checks if every element is the character sequence is whitespace.
     *
     * precondition: sb.length() is greater than 0
     *
     * @param sb A sequence of characters to examine
     * @return true if every character in the sequence is whitespace
     */
    protected boolean isAllWhiteSpace(CharSequence sb) {
        return StringUtils.isWhitespace(sb);
    }
}
