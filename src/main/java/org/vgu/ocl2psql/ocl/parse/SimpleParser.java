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


package org.vgu.ocl2psql.ocl.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.vgu.ocl2psql.ocl.context.OclContext;
import org.vgu.ocl2psql.ocl.exception.OclParseException;
import org.vgu.ocl2psql.ocl.expressions.BooleanLiteralExp;
import org.vgu.ocl2psql.ocl.expressions.CollectionItem;
import org.vgu.ocl2psql.ocl.expressions.CollectionKind;
import org.vgu.ocl2psql.ocl.expressions.CollectionLiteralExp;
import org.vgu.ocl2psql.ocl.expressions.EnumLiteralExp;
import org.vgu.ocl2psql.ocl.expressions.IfExp;
import org.vgu.ocl2psql.ocl.expressions.IntegerLiteralExp;
import org.vgu.ocl2psql.ocl.expressions.IterateExp;
import org.vgu.ocl2psql.ocl.expressions.IteratorExp;
import org.vgu.ocl2psql.ocl.expressions.IteratorKind;
import org.vgu.ocl2psql.ocl.expressions.LetExp;
import org.vgu.ocl2psql.ocl.expressions.NullLiteralExp;
import org.vgu.ocl2psql.ocl.expressions.OclExpression;
import org.vgu.ocl2psql.ocl.expressions.OperationCallExp;
import org.vgu.ocl2psql.ocl.expressions.PropertyCallExp;
import org.vgu.ocl2psql.ocl.expressions.RealLiteralExp;
import org.vgu.ocl2psql.ocl.expressions.StringLiteralExp;
import org.vgu.ocl2psql.ocl.expressions.TypeExp;
import org.vgu.ocl2psql.ocl.expressions.Variable;
import org.vgu.ocl2psql.ocl.expressions.VariableExp;

public class SimpleParser {

    private static String trimBothSides(String input) {
        while (input.startsWith(" "))
            input = input.substring(1);
        return input.trim();
    }

    private final static Pattern[] operators = new Pattern[] { Pattern.compile("(.*)(?<!\\w)(implies)(?!\\w)(.*)"),
            Pattern.compile("(.*)(?<!\\w)(xor|or|and)(?!\\w)(.*)"), Pattern.compile("(.*)((?<!\\>|\\<)\\=|\\<\\>)(.*)"),
            Pattern.compile("(.*)(\\<(?!\\>|\\=)|(?<!\\<|\\-)\\>(?!\\=)|\\<\\=|\\>\\=)(.*)"), null,
            Pattern.compile("(.*)(\\+|\\-(?!\\>))(.*)"), Pattern.compile("(.*)(\\*|\\/)(.*)"),
            Pattern.compile("(.*)(?<!\\w)(not)(?!\\w)(.*)"), Pattern.compile("(.*)(\\.|\\-\\>)(.*)") };

    private static final Pattern VARIABLE_DECL_PATTERN = Pattern
            .compile("^(\\w+)( ?\\: ?([\\w:\\(\\)]+))? ?(= ?(.*))?$");

    private static OclExpression parse(String operator, String source, String body, List<String> bracketStack,
            List<String> stringStack, OclContext context) throws OclParseException {
        // It's Hoang. I added this if to handle the not operator!
        if ("not".equals(operator) && body != null && body.length() > 0) {
            return new OperationCallExp(null, operator, parse(body, bracketStack, stringStack, context));
        }
        if (source != null && source.length() > 0 && body != null && body.length() > 0) {
            if (".".equals(operator)) {
                // must check, if it is a property or an operation call
                if (body.matches("(\\w*)\\(\\d+\\)")) {
                    String[] arguments = replace(body.replaceFirst("\\w*(\\(\\d+\\))", "$1"), bracketStack).split(",");
                    if (arguments.length == 1 && "".equals(arguments[0]))
                        arguments = new String[0];
                    OclExpression[] argumentExps = new OclExpression[arguments.length];
                    for (int i = 0; i < arguments.length; i++) {
                        argumentExps[i] = parse(arguments[i], bracketStack, stringStack, context);
                    }
                    return new OperationCallExp(parse(source, bracketStack, stringStack, context),
                            body.replaceFirst("(\\w*)\\(\\d+\\)", "$1"), argumentExps);
                } else {
                    return new PropertyCallExp(parse(source, bracketStack, stringStack, context), body);
                }
            } else if ("->".equals(operator)) {
                String name = body.replaceFirst("(\\w+)\\(\\d+\\)", "$1");
                String innerExpression = replace(body.replaceFirst("\\w+(\\(\\d+\\))", "$1"), bracketStack);
                if ("iterate".equals(name)) {
                    if (innerExpression.matches("^.*;.*\\|.*$")) {
                        String iterator = innerExpression.replaceFirst("^(.*);(.*)\\|(.*)$", "$1");
                        String accumulator = innerExpression.replaceFirst("^(.*);(.*)\\|(.*)$", "$2");
                        innerExpression = innerExpression.replaceFirst("^(.*);(.*)\\|(.*)$", "$3");
                        return new IterateExp(parse(source, bracketStack, stringStack, context),
                                parseVariable(iterator, bracketStack, stringStack, context),
                                parseVariable(accumulator, bracketStack, stringStack, context),
                                innerExpression.length() == 0 ? null
                                        : parse(innerExpression, bracketStack, stringStack, context));
                    } else {
                        throw new OclParseException(
                                "Iterate must define an accumulator and an iterator! " + innerExpression);
                    }
                } else {
                    if (IteratorKind.valueOf(name) == null)
                        throw new OclParseException("Unknown Iterator: " + name);
                    String iterator;
                    if (innerExpression.matches("^.*\\|.*$")) {
                        iterator = innerExpression.replaceFirst("^(.*)\\|.*$", "$1");
                        innerExpression = innerExpression.replaceFirst("^.*\\|(.*)$", "$1");
                    } else {
                        iterator = "iterator";
                    }
                    return new IteratorExp(parse(source, bracketStack, stringStack, context),
                            IteratorKind.valueOf(name), parseVariable(iterator, bracketStack, stringStack, context),
                            innerExpression.length() == 0 ? null
                                    : parse(innerExpression, bracketStack, stringStack, context));
                }
            } else {
                return new OperationCallExp(parse(source, bracketStack, stringStack, context), operator,
                        parse(body, bracketStack, stringStack, context));
            }
        }
        throw new OclParseException("Cannot parse: " + operator);
    }

    private static OclExpression check(Pattern pattern, String input, List<String> bracketStack,
            List<String> stringStack, OclContext context) throws OclParseException {
        if (pattern != null) {
            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                return parse(matcher.group(2), matcher.group(1), matcher.group(3), bracketStack, stringStack, context);
            } else {
                return null;
            }
        } else {
            if (input.startsWith("if") && input.endsWith("endif")) {
                int thenIndex = findCorresponding(input, 0, input.length(), "if", "then");
                if (thenIndex > 2) {
                    int elseIndex = findCorresponding(input, thenIndex, input.length(), "then", "else");
                    if (elseIndex > thenIndex + 4) {
                        // if with else
                        int endIndex = findCorresponding(input, elseIndex, input.length(), "else", "endif");
                        if (endIndex > elseIndex + 4) {
                            OclExpression conditionExp = parse(input.substring(2, thenIndex), bracketStack, stringStack,
                                    context);
                            OclExpression thenExp = parse(input.substring(thenIndex + 4, elseIndex), bracketStack,
                                    stringStack, context);
                            OclExpression elseExp = parse(input.substring(elseIndex + 4, endIndex), bracketStack,
                                    stringStack, context);
                            return new IfExp(conditionExp, thenExp, elseExp);
                        } else {
                            throw new OclParseException("Found if without corresponding endif in: " + input);
                        }
                    } else {
                        // if without else
                        int endIndex = findCorresponding(input, thenIndex, input.length(), "then", "endif");
                        if (endIndex > thenIndex + 4) {
                            OclExpression conditionExp = parse(input.substring(2, thenIndex), bracketStack, stringStack,
                                    context);
                            OclExpression thenExp = parse(input.substring(thenIndex + 4, endIndex), bracketStack,
                                    stringStack, context);
                            return new IfExp(conditionExp, thenExp);
                        } else {
                            throw new OclParseException("Found if without corresponding endif in: " + input);
                        }
                    }
                } else {
                    throw new OclParseException("Found if without corresponding then in: " + input);
                }
            } else if (input.matches("^let(?!\\w).*")) {
                String letExpression = input.replaceAll("^let(?!\\w)(.*)(?<!\\w)in(?!\\w)(.*)$", "$1");
                String resultExpression = input.replaceAll("^let(?!\\w)(.*)(?<!\\w)in(?!\\w)(.*)$", "$2");
                return new LetExp(parse(resultExpression, bracketStack, stringStack, context),
                        parseVariable(letExpression, bracketStack, stringStack, context));
            }
            return null;
        }
    }

    private static Variable parseVariable(String input, List<String> bracketStack, List<String> stringStack,
            OclContext context) throws OclParseException {
        input = trimBothSides(input);
        if (input.matches("\\(\\d+\\)")) {
            input = replace(input, bracketStack);
        }
        Matcher m = VARIABLE_DECL_PATTERN.matcher(input);
        if (m.find()) {
            String name = m.group(1);
            String type = m.group(3);
            String init = m.group(5);
            if (name == null)
                throw new OclParseException("not a valid variable declaration - no name declared in: " + input);
            return new Variable(name, type != null ? new TypeExp(type) : null,
                    init != null ? parse(init, bracketStack, stringStack, context) : null);
        } else {
            throw new OclParseException("cannot parse variable declaration: " + input);
        }
    }

    private static String replace(String input, List<String> bracketStack) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '(' && i + 3 <= input.length()) {
                int index = Integer.parseInt(input.substring(i + 1, input.indexOf(')', i)));
                result.append(bracketStack.get(index));
                i = input.indexOf(')', i);
            } else {
                result.append(input.charAt(i));
            }
        }
        return result.toString();
    }

    private static OclExpression parse(String input, List<String> bracketStack, List<String> stringStack,
            OclContext context) throws OclParseException {
        input = trimBothSides(input);
        if (input.matches("\\(\\d+\\)")) {
            input = replace(input, bracketStack);
        }
        for (Pattern pattern : operators) {
            OclExpression expression = check(pattern, input, bracketStack, stringStack, context);
            if (expression != null)
                return expression;
        }
        if (input.matches("^\\d+(\\.\\d+)?$")) {
            // it is a NumberLiteral
            if (input.matches("^\\d+\\.\\d+")) {
                // it is a RealLiteral
                return new RealLiteralExp(Double.parseDouble(input));
            } else {
                // it is an IntegerLiteral
                return new IntegerLiteralExp(Integer.parseInt(input));
            }
        } else if (input.matches("^\\[\\d+\\]$")) {
            int index = Integer.parseInt(input.replaceFirst("^\\[(\\d+)\\]$", "$1"));
            return new StringLiteralExp(stringStack.get(index));
        } else if ("true".equals(input) || "false".equals(input)) {
            // it is a BooleanLiteral
            return new BooleanLiteralExp(Boolean.parseBoolean(input));
        } else if (input.length() > 0) {
            // check if it is a type
            if (input.matches("\\w+(\\:\\:\\w+)+(\\w*)\\(\\d+\\)")) {
                String className = input.substring(0, input.lastIndexOf("::"));
                String body = input.substring(input.lastIndexOf("::") + 2);
                String[] arguments = replace(body.replaceFirst("\\w*(\\(\\d+\\))", "$1"), bracketStack).split(",");
                if (arguments.length == 1 && "".equals(arguments[0]))
                    arguments = new String[0];
                OclExpression[] argumentExps = new OclExpression[arguments.length];
                for (int i = 0; i < arguments.length; i++) {
                    argumentExps[i] = parse(arguments[i], bracketStack, stringStack, context);
                }
                return new OperationCallExp(new TypeExp(className), body.replaceFirst("(\\w*)\\(\\d+\\)", "$1"),
                        argumentExps);
            } else if (input.matches("\\w+(\\:\\:\\w+)+")) {
                String className = input.substring(0, input.lastIndexOf("::"));
                String literal = input.substring(input.lastIndexOf("::") + 2);
                if (context.isEnum(className))
                    return new EnumLiteralExp(new TypeExp(context.getFullName(className)), literal);
                if (context.isType(className))
                    return new PropertyCallExp(new TypeExp(className), literal);
                return new TypeExp(context.getFullName(input));
            } else if (input.matches("(?<!\\w)String(?!\\w)")) {
                return new TypeExp(input);
            } else if (input.matches("(?<!\\w)Boolean(?!\\w)")) {
                return new TypeExp(input);
            } else if (input.matches("(?<!\\w)Integer(?!\\w)")) {
                return new TypeExp(input);
            } else if (input.matches("(?<!\\w)Real(?!\\w)")) {
                return new TypeExp(input);
            } else if (input.matches("(?<!\\w)OclVoid(?!\\w)")) {
                return new TypeExp(input);
            } else if (input.matches("(?<!\\w)OclAny(?!\\w)")) {
                return new TypeExp(input);
            } else if (input.matches("(?<!\\w)OclUndefined(?!\\w)")) {
                return new NullLiteralExp();
            } else if (input.matches("(?<!\\w)(Set|Sequence|Bag|OrderedSet)\\((.*)\\)")) {
                return new TypeExp(input.replaceAll("(?<!\\w)(Set|Sequence|Bag|OrderedSet)\\((.*)\\)", "$1"));
            } else if (input.matches("(?<!\\w)(Set|Sequence|Bag|OrderedSet)\\{(.*)\\}")) {
                String type = input.replaceAll("(?<!\\w)(Set|Sequence|Bag|OrderedSet)\\{(.*)\\}", "$1");
                String[] literals = input.replaceAll("(?<!\\w)(Set|Sequence|Bag|OrderedSet)\\{(.*)\\}", "$2")
                        .split(",");
                CollectionItem[] parts = new CollectionItem[literals.length];
                for (int i = 0; i < literals.length; i++) {
                    parts[i] = new CollectionItem(parse(literals[i], bracketStack, stringStack, context));
                }
                return new CollectionLiteralExp(CollectionKind.valueOf(type), parts);
            } else if ("self".equals(input)) {
                return new VariableExp(new Variable("self"));
            } else {
                // try to find that class first...
                if (context.isType(input))
                    return new TypeExp(context.getFullName(input));
                // if not found, state it as a Variable...
                return new VariableExp(new Variable(input));
            }
        } else {
            throw new OclParseException("cannot parse an empty expression!");
        }
    }

    private static int findCorrespondingBracket(String input, int begin) {
        int level = 0;
        for (int i = begin; i < input.length(); i++) {
            if (input.charAt(i) == '(')
                level++;
            else if (input.charAt(i) == ')') {
                level--;
                if (level == 0)
                    return i;
            }
        }
        return -1;
    }

    private static int findCorresponding(String input, int begin, int end, String beginString, String endString) {
        int level = 0;
        for (int i = begin; i < end; i++) {
            if (input.substring(i).matches("^" + beginString + "(?!\\w).*")) {
                if (i == begin || !Character.isLetterOrDigit(input.charAt(i - 1)))
                    level++;
            } else if (input.substring(i).matches("^" + endString + "(?!\\w).*"))
                if (i == begin || !Character.isLetterOrDigit(input.charAt(i - 1)))
                    if (i + endString.length() == end
                            || !Character.isLetterOrDigit(input.charAt(i + endString.length()))) {
                        level--;
                        if (level == 0)
                            return i;
                    }
        }
        return -1;
    }

    private static String pushBrackets(String input, List<String> bracketStack) throws OclParseException {
        int firstBracket = input.indexOf('(');
        if (firstBracket >= 0) {
            StringBuilder result = new StringBuilder();
            result.append(input.substring(0, firstBracket));
            int lastBracket = findCorrespondingBracket(input, firstBracket);
            if (lastBracket < 0) {
                throw new OclParseException("no matching closing bracket found: " + input.substring(firstBracket));
            }
            String inner = input.substring(firstBracket + 1, lastBracket);
            bracketStack.add(pushBrackets(inner, bracketStack));
            result.append("(").append(bracketStack.size() - 1).append(")");
            String after = input.substring(lastBracket + 1);
            result.append(pushBrackets(after, bracketStack));
            return result.toString();
        } else {
            if (input.contains(")"))
                throw new OclParseException("illegal closing bracket found: " + input);
            return input;
        }
    }

    private static String pushStrings(String input, List<String> stringStack) throws OclParseException {
        int firstBracket = input.indexOf('\'');
        if (firstBracket >= 0) {
            StringBuilder result = new StringBuilder();
            result.append(input.substring(0, firstBracket));
            int lastBracket = input.indexOf('\'', firstBracket + 1);
            if (lastBracket < 0) {
                throw new OclParseException("end of line before String end: " + input.substring(firstBracket));
            }
            String inner = input.substring(firstBracket + 1, lastBracket);
            stringStack.add(inner);
            result.append("[").append(stringStack.size() - 1).append("]");
            String after = input.substring(lastBracket + 1);
            result.append(pushStrings(after, stringStack));
            return result.toString();
        } else {
            if (input.contains("'"))
                throw new OclParseException("illegal count of ' found: " + input);
            return input;
        }
    }

    private static String eliminateComments(String input) {
        // ocl multiline comments are surrounded with /* and */ and are not
        // nested
        input = input.replaceAll("(?s)\\/\\*[^\\*(?=\\/)]*\\*\\/", "");
        // ocl single line comments starts with -- end ends at eol
        StringBuilder builder = new StringBuilder("");
        for (String line : input.split("\\n")) {
            if (line.matches("(.*)(\\-\\-)(.*)")) {
                builder.append(line.replace("(.*)(\\-\\-)(.*)", "$1"));
                builder.append("\n");
            } else {
                builder.append(line);
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    public static OclExpression parse(String ocl, OclContext context) throws OclParseException {
        ArrayList<String> stringStack = new ArrayList<String>();
        ArrayList<String> bracketStack = new ArrayList<String>();
        ocl = ocl.replaceAll("(?<!\\w)let(?!\\w)( *)", "let (");
        ocl = ocl.replaceAll("( *)(?<!\\w)in(?!\\w)( *)", ") in (");
        int i = -1;
        do {
            i = ocl.indexOf(") in (", i + 1);
            if (i >= 0) {
                int j = findCorrespondingBracket(ocl, i + 5);
                if (j == -1)
                    ocl += ')';
                else
                    ocl = ocl.substring(0, j) + ')' + ocl.substring(j);
            }
        } while (i != -1);
        ocl = ocl.replaceAll("(?<!\\w)if(?!\\w)( *)", "if (");
        ocl = ocl.replaceAll("( *)(?<!\\w)then(?!\\w)( *)", ") then (");
        ocl = ocl.replaceAll("( *)(?<!\\w)else(?!\\w)( *)", ") else (");
        ocl = ocl.replaceAll("( *)(?<!\\w)endif(?!\\w)", ") endif");
        String encoded = pushBrackets(pushStrings(eliminateComments(ocl), stringStack), bracketStack);
        return parse(encoded, bracketStack, stringStack, context);
    }

}
