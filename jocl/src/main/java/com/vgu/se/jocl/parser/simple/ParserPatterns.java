/**************************************************************************
Copyright 2019 Vietnamese-German-University

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

@author: thian
***************************************************************************/

package com.vgu.se.jocl.parser.simple;

import java.util.regex.Pattern;

public class ParserPatterns {

    // Helper patterns
    /**
     * <p>
     * 1st Capturing Group {@code ('(.*?)')}<br/>
     * {@code '} matches the * character {@code '} literally (case
     * sensitive)
     * </p>
     * <p>
     * 2nd Capturing Group {@code (.*?)} {@code .*?} matches any
     * character (except for line terminators) {@code *?}
     * <emp>Quantifier</emp>: Matches between zero and unlimited times,
     * as few times as possible, expanding as needed (lazy)
     * </p>
     */
    public static final String STRING_LITERAL_STR = "('(.*?)')";

    /**
     * <p>
     * 1st Capturing Group (\\(([^()]*?)\\)) <br />
     * {@code \\(} matches the character {@code (} literally (case
     * sensitive)
     * </p>
     * <p>
     * 2nd Capturing Group ([^()]*?) <br />
     * Match a single character not present in the list below
     * {@code [^()]*?}<br />
     * {@code ?} Quantifier — Matches between zero and unlimited times,
     * as few times as possible, expanding as needed (lazy) {@code ()}
     * matches a single character in the list () (case sensitive)
     * </p>
     * <p>
     * {@code \)} matches the character {@code )} literally (case
     * sensitive)
     * </p>
     */
    public static final String PARENTHESIS_LITERAL_STR = "(\\(([^()]*?)\\))";
    
    // OPERATION STRING PATTERNS 
    /**
     * 
     */
    public static final String IMPLIES_OP_STR = "(.*)\\b(implies)\\b(.*)";
    public static final String XOR_OP_STR = "(.*)\\b(xor)\\b(.*)";
    public static final String OR_OP_STR = "(.*)\\b(or)\\b(.*)";
    public static final String AND_OP_STR = "(.*)\\b(and)\\b(.*)";
    public static final String EQUALITY_OP_STR = "(.*)((?<!\\<|\\>)\\=|\\<\\>)(.*)";
    public static final String COMPARISON_OP_STR = "(.*)(\\<(?!\\>|\\=)|(?<!\\<|\\-)\\>(?!\\=)|\\<\\=|\\>\\=)(.*)";
    public static final String ADD_OR_SUBTRACT_OP_STR = "(.*)(\\+|\\-(?!\\>))(.*)";
    public static final String MULTIPLY_OR_DIV_OP_STR = "(.*)(\\*|\\/)(.*)";
//    public static final String NOT_OP_STR = "(.*)\\b(not)\\b(\\(?\\w+\\)?.*)";
    public static final String NOT_OP_STR = "(.*)\\b(not)\\b(.*)";
    public static final String NOT_OR_NEGATIVE_OP_STR = "(.*)(\\bnot\\b|-(?=\\d++(\\.\\d+)?))\\b(.*)";
    public static final String DOT_OR_ARROW_OP_STR = "(.*)(\\.|\\-\\>)(.*)";
//    public static final String SQL_FUNCTION = "@SQL_\\w+\\(?\\w+\\)?";
    public static final String SQL_FUNCTION = "@SQL\\(.*\\)";

    // Pattern from String
    public static final Pattern IMPLIES_OP_PATT = Pattern.compile(IMPLIES_OP_STR);
    public static final Pattern XOR_OP_PATT = Pattern.compile(XOR_OP_STR);
    public static final Pattern OR_OP_PATT = Pattern.compile(OR_OP_STR);
    public static final Pattern AND_OP_PATT = Pattern.compile(AND_OP_STR);
    public static final Pattern EQUALITY_OP_PATT = Pattern.compile(EQUALITY_OP_STR);
    public static final Pattern COMPARISON_OP_PATT = Pattern.compile(COMPARISON_OP_STR);
    public static final Pattern ADD_OR_SUBTRACT_OP_PATT = Pattern.compile(ADD_OR_SUBTRACT_OP_STR);
    public static final Pattern MULTIPLY_OR_DIV_OP_PATT = Pattern.compile(MULTIPLY_OR_DIV_OP_STR);
    public static final Pattern NOT_OP_PATT = Pattern.compile(NOT_OP_STR);
    public static final Pattern NOT_OR_NEGATIVE_OP_PATT = Pattern.compile(NOT_OR_NEGATIVE_OP_STR);
    public static final Pattern DOT_OR_ARROW_OP_PATT = Pattern.compile(DOT_OR_ARROW_OP_STR);
    public static final Pattern SQL_FUNCTION_PATT = Pattern.compile(SQL_FUNCTION);
    
    // VARIABLE DECLARATION PATTERN
    public static final String VARIABLE_DECL_STR = "(\\w.*)\\|(\\w.*)";

}
