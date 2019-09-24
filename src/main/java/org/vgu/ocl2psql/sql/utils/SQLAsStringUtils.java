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

@author: ngpbh
***************************************************************************/


package org.vgu.ocl2psql.sql.utils;

import java.util.regex.Pattern;

public class SQLAsStringUtils {
    private final static String INDENT = "    ";
    private final static String BEGIN_SUB_EXP_PATTERN = "^\\/\\*\\*\\*\\sBEGIN:\\s[ -~]+\\s\\*\\*\\*\\/$";
    private final static String END_SUB_EXP_PATTERN = "^\\/\\*\\*\\*\\sEND:\\s[ -~]+\\s\\*\\*\\*\\/$";
    
    public static String applyIndent(String SQLString) {
        int indentRate = -1;
        String indentedSQLString = "";
        String[] lines = SQLString.split("\n");
        for(String line : lines) {
            String lineAfterIndent;
            if(beginNewSubExpression(line)) {
                lineAfterIndent = getIndent(++indentRate).concat(line);
            }
            else if(endCurrentSubExpression(line)) {
                lineAfterIndent = getIndent(indentRate--).concat(line);
            }
            else {
                lineAfterIndent = getIndent(indentRate).concat(line);
            }
            indentedSQLString = indentedSQLString.concat(lineAfterIndent).concat("\r\n");
        }
        return indentedSQLString;
    }
    
    private static boolean endCurrentSubExpression(String line) {
        Pattern endPattern = Pattern.compile(END_SUB_EXP_PATTERN);
        return endPattern.matcher(line).matches();
    }

    private static boolean beginNewSubExpression(String line) {
        Pattern beginPattern = Pattern.compile(BEGIN_SUB_EXP_PATTERN);
        return beginPattern.matcher(line).matches();
    }

    private static String getIndent(int indentRate) {
        String indent = "";
        for(int i = 0; i < indentRate; i++) {
            indent = indent.concat(INDENT);
        }
        return indent;
    }
}
