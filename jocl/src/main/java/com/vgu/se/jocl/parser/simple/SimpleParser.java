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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.vgu.dm2schema.dm.DataModel;
import org.vgu.dm2schema.dm.DmUtils;

import com.vgu.se.jocl.exception.OclParserException;
import com.vgu.se.jocl.expressions.AssociationClassCallExp;
import com.vgu.se.jocl.expressions.BooleanLiteralExp;
import com.vgu.se.jocl.expressions.Expression;
import com.vgu.se.jocl.expressions.IntegerLiteralExp;
import com.vgu.se.jocl.expressions.IteratorExp;
import com.vgu.se.jocl.expressions.IteratorKind;
import com.vgu.se.jocl.expressions.M2MAssociationClassCallExp;
import com.vgu.se.jocl.expressions.M2OAssociationClassCallExp;
import com.vgu.se.jocl.expressions.NullLiteralExp;
import com.vgu.se.jocl.expressions.O2OAssociationClassCallExp;
import com.vgu.se.jocl.expressions.OclExp;
import com.vgu.se.jocl.expressions.Operation;
import com.vgu.se.jocl.expressions.OperationCallExp;
import com.vgu.se.jocl.expressions.PropertyCallExp;
import com.vgu.se.jocl.expressions.RealLiteralExp;
import com.vgu.se.jocl.expressions.StringLiteralExp;
import com.vgu.se.jocl.expressions.TypeExp;
import com.vgu.se.jocl.expressions.Variable;
import com.vgu.se.jocl.expressions.VariableExp;
import com.vgu.se.jocl.expressions.sql.LiteralParam;
import com.vgu.se.jocl.expressions.sql.SqlFunctionExp;
import com.vgu.se.jocl.expressions.sql.functions.SqlFnCurdate;
import com.vgu.se.jocl.expressions.sql.functions.SqlFnTimestampdiff;
import com.vgu.se.jocl.parser.interfaces.Parser;
import com.vgu.se.jocl.types.Type;

public class SimpleParser implements Parser {

    private List<String> stringArray = new ArrayList<String>();
    private List<String> parenthesisArray = new ArrayList<String>();
    private Stack<Variable> variableStack = new Stack<Variable>();
    private Set<Variable> adhocContextualSet = new HashSet<>();

    private DataModel dm;

    private void houseCleanup() {
        this.stringArray.clear();
        this.parenthesisArray.clear();
        this.variableStack.clear();
    }

    @Override
    public void putAdhocContextualSet(Variable v) {
        this.adhocContextualSet.remove(v);
        this.adhocContextualSet.add(v);
    }

    /**
     * This parse an OCL Expression string to OclExp Java Object given a
     * 
     * UML Context file.
     * 
     * @param ctx
     * @param oclExpStr
     * @return OclExp
     */
    public Expression parse(String ocl, DataModel dm) {
        houseCleanup();
        this.dm = dm;

        String encOcl = encode(ocl);

        return parseOclExp(encOcl, dm);
    }

    private Expression parseOclExp(String ocl, DataModel dm) {

        if (Pattern.matches("\\(\\d+\\)", ocl)) {
            ocl = decode(ocl, true).replaceAll("^\\((.*)\\)$", "$1");
        }

        Expression oclExp = parseCallExp(ocl, dm);

        if (oclExp != null) {
            oclExp.setOclStr(decode(ocl));
            return oclExp;
        }

        Expression litExp = parseLiteralExp(ocl, dm);
        litExp.setOclStr(decode(ocl));

        return litExp;
    }

    private Expression parseCallExp(String ocl, DataModel dm) {

//        Implementing the operators patterns from lowest precedence
        Pattern[] patterns = { ParserPatterns.IMPLIES_OP_PATT,
            ParserPatterns.XOR_OP_PATT, ParserPatterns.OR_OP_PATT,
            ParserPatterns.AND_OP_PATT, ParserPatterns.EQUALITY_OP_PATT,
            ParserPatterns.COMPARISON_OP_PATT,
//                ParserPatterns.ADD_OR_SUBTRACT_OP_PATT,
//                ParserPatterns.MULTIPLY_OR_DIV_OP_PATT,
//                ParserPatterns.NOT_OR_NEGATIVE_OP_PATT,
            ParserPatterns.NOT_OP_PATT };

        Matcher m;
        for (Pattern p : patterns) {
            m = p.matcher(ocl);
            if (m.find()) {
                return parseOperationCallExp(m, ocl, dm);
            }
        }

//        Check the DOT or ARROW pattern
        m = ParserPatterns.DOT_OR_ARROW_OP_PATT.matcher(ocl);
        if (m.find()) {
            return parseCallExp(m, ocl, dm);
        }

        return null;
    }

    private OclExp parseOperationCallExp(Matcher m, String ocl, DataModel dm) {

        String source = trim(m.group(1));
        String operator = trim(m.group(2));
        String body = trim(m.group(3));

        Expression sourceExp = null;

        if (!operator.equals("not")) {
            sourceExp = getExp(source);
        }

        Expression bodyExp = getExp(body);

        Type type = getOperationExpType(operator, sourceExp, bodyExp);

        OperationCallExp opCallExp = new OperationCallExp(sourceExp,
            new Operation(operator), Arrays.asList(bodyExp));
        opCallExp.setType(type);

        return opCallExp;
    }

    private boolean isSqlFunction(String oclString) {
        return Pattern.matches(ParserPatterns.SQL_FUNCTION, oclString);
    }

    private Expression getExp(String exp) {
        if (isSqlFunction(exp)) {
//            return new SqlFunctionExp(exp);
            return parseSqlFunctionExp(exp);
        }

        return parseOclExp(exp, dm);
    }
    
    private SqlFunctionExp parseSqlFunctionExp(String exp) {
        exp = decode(exp).trim();
        exp = exp.replaceAll("\\@SQL\\((.*)\\)", "$1");
        
        String fnName = exp.replaceAll("(\\w+)\\(.*\\)", "$1");

        // No parameter
        String fnContent = exp.replaceAll("\\w+\\((.*)\\)", "$1").trim();
        if (fnContent.length() == 0) {
            switch(fnName.toUpperCase()) {
            case "CURDATE":
                // CURDATE()
                return new SqlFnCurdate(fnName);
            default :
                return null;
            }
        }

        String[] params = fnContent.split(",");
        List<Expression> paramList = new ArrayList<Expression>();
        List<LiteralParam> literalParamList = new ArrayList<>();
        // TIMESTAMPDIFF(year, e.date, curedate())
        for (int i = 0; i < params.length; i++) {
            params[i] = params[i].trim();
            if (Pattern.matches("^\\w+$", params[i])) {
                literalParamList.add(new LiteralParam(params[i]));
            } else if (Pattern.matches("(.*)\\.(.*)", params[i])) {
                Matcher m = ParserPatterns.DOT_OR_ARROW_OP_PATT.matcher(params[i]);
                if (m.find()) {
                    paramList.add(parseDotCase(m, params[i], dm));
                }
            } else {
                paramList.add(parseSqlFunctionExp(params[i]));
            }
        }
        
        switch(fnName.toUpperCase()) {
        case "TIMESTAMPDIFF":
            return new SqlFnTimestampdiff(fnName, paramList, literalParamList);
        default :
            return null;
        }
    }
    
    private Expression parseCallExp(Matcher m, String ocl, DataModel dm) {

        String operator = m.group(2);

        switch (operator) {
        case ".":
            return parseDotCase(m, ocl, dm);
        default: // "->"
            return parseArrowCase(m, ocl, dm);
        }
    }

    private Expression parseDotCase(Matcher m, String ocl, DataModel dm) {

        String left = trim(m.group(1));
        String right = trim(m.group(3));
        String operation = right.replaceFirst("(\\w*)\\(\\d+\\)", "$1");
        String operationPatt = "(\\w*)(\\((\\d+)\\))";
        Matcher mRight = Pattern.compile(operationPatt).matcher(right);
        Type type = new Type();

        if (mRight.find()) {
//            Used for operation defined in Classifier with paramenters
            Expression leftExp = parseOclExp(left, dm);

            String[] arguments = this.parenthesisArray
                .get(Integer.valueOf(trim(mRight.group(3)))).split(",");

            if (arguments.length == 1 & "".equals(arguments[0])) {
                arguments = new String[0];
            }

            Expression[] argumentExps = new Expression[arguments.length];
            for (int i = 0; i < arguments.length; i++) {
//                argumentExps[i] = parse(arguments[i], dm);
                argumentExps[i] = parseOclExp(arguments[i], dm);
            }

            type = getOperationExpType(operation, leftExp, argumentExps);

            OperationCallExp opCallExp = new OperationCallExp(leftExp,
                new Operation(operation), Arrays.asList(argumentExps));
            opCallExp.setType(type);

            return opCallExp;

        } else {
            if (Pattern.matches("(.*)\\.(.*)$", left)) {
                checkNavigationSource(left, dm);
            }
            
            Expression src = parseOclExp(left, dm);
            String srcType = src.getType().getReferredType();

            if ("Unknown".equals(srcType)) {
                throw new OclParserException(
                    "Cannot parse " + srcType + " type");
            }

            Expression dotOpCall = null;

            if (DmUtils.isPropertyOfClass(dm, srcType, right)) {
                dotOpCall = new PropertyCallExp(src, right);
                type = new Type(DmUtils.getAttributeType(dm, srcType, right));
                dotOpCall.setType(type);

            } else if (DmUtils.isAssociationEndOfClass(dm, srcType, right)) {
                if (DmUtils.isAssocM2M(dm, srcType, right)) {
                    dotOpCall = parseM2MAssociationCallExp(dm, right, src,
                        srcType);
                } else if (DmUtils.isAssocM2O(dm, srcType, right)) {
                    dotOpCall = parseM2OAssociationCallExp(dm, right, src,
                        srcType);
                } else /* association is one to one */ {
                    dotOpCall = parseO2OAssociationCallExp(dm, right, src,
                        srcType);
                }

            } else {
                throw new OclParserException("Cannot parse " + srcType);
            }

            return dotOpCall;
        }
    }
    
    private void checkNavigationSource(String ocl, DataModel dm) {
        Matcher m = Pattern.compile("(.*)(\\.)(.*)").matcher(ocl);
        
        while(m.find()) {
            String collection = m.group(3);
            
            Expression src = parseOclExp(m.group(1), dm);
            String srcType = src.getType().getReferredType();

            if (!DmUtils.isEndMultOne(dm, srcType, collection)) {
                throw new OclParserException(
                        "Cannot use dot to navigate on collection.");
            }
        }
    }

    private Expression parseO2OAssociationCallExp(DataModel dm,
        String referredEnd, Expression src, String srcType) {
        Type type;
        Expression dotOpCall;
        dotOpCall = new O2OAssociationClassCallExp(src, referredEnd);

        String associationTableName = DmUtils.getAssociationName(dm, srcType,
            referredEnd);

        ((AssociationClassCallExp) dotOpCall)
            .setAssociation(associationTableName);

        ((AssociationClassCallExp) dotOpCall).setOppositeAssociationEnd(
            DmUtils.getAssociationEndSourceClassName(dm, srcType, referredEnd));

        String referredClassName = DmUtils.getAssociationEndTargetClassName(dm,
            srcType, referredEnd);

        ((AssociationClassCallExp) dotOpCall)
            .setReferredAssociationEndType(new Type(referredClassName));

        ((AssociationClassCallExp) dotOpCall)
            .setOppositeAssociationEndType(new Type(srcType));

        type = new Type(referredClassName);
        dotOpCall.setType(type);

        return dotOpCall;
    }

    private Expression parseM2OAssociationCallExp(DataModel dm,
        String referredEnd, Expression src, String srcType) {
        Type type;
        Expression dotOpCall;
        dotOpCall = new M2OAssociationClassCallExp(src, referredEnd);

        String associationTableName = DmUtils.getAssociationName(dm, srcType,
            referredEnd);

        ((AssociationClassCallExp) dotOpCall)
            .setAssociation(associationTableName);

        ((AssociationClassCallExp) dotOpCall).setOppositeAssociationEnd(
            DmUtils.getAssociationEndSourceClassName(dm, srcType, referredEnd));

        String referredClassName = DmUtils.getAssociationEndTargetClassName(dm,
            srcType, referredEnd);

        ((AssociationClassCallExp) dotOpCall)
            .setReferredAssociationEndType(new Type(referredClassName));

        ((AssociationClassCallExp) dotOpCall)
            .setOppositeAssociationEndType(new Type(srcType));

        if (DmUtils.isEndMultMany(dm, srcType, referredEnd)) {
            type = new Type("Col(" + referredClassName + ")");
        } else /* else, this end's mult is one */ {
            type = new Type(referredClassName);
        }

        dotOpCall.setType(type);
        return dotOpCall;
    }

    private Expression parseM2MAssociationCallExp(DataModel dm,
        String referredEnd, Expression src, String srcType) {
        Type type;
        Expression dotOpCall;
        dotOpCall = new M2MAssociationClassCallExp(src, referredEnd);

        String associationTableName = DmUtils.getAssociationName(dm, srcType,
            referredEnd);

        ((AssociationClassCallExp) dotOpCall)
            .setAssociation(associationTableName);

        ((AssociationClassCallExp) dotOpCall).setOppositeAssociationEnd(
            DmUtils.getAssociationEndSourceClassName(dm, srcType, referredEnd));

        String referredClassName = DmUtils.getAssociationEndTargetClassName(dm,
            srcType, referredEnd);

        ((AssociationClassCallExp) dotOpCall)
            .setReferredAssociationEndType(new Type(referredClassName));

        ((AssociationClassCallExp) dotOpCall)
            .setOppositeAssociationEndType(new Type(srcType));

        type = new Type("Col(" + referredClassName + ")");
        dotOpCall.setType(type);
        return dotOpCall;
    }

    private Expression parseArrowCase(Matcher m, String ocl, DataModel dm) {

        String source = trim(m.group(1));
        String body = trim(replace(m.group(3)));
        String kind = trim(m.group(3).replaceFirst("(\\w*)\\(\\d+\\)", "$1"));

        if (IteratorKind.valueOf(kind) == null) {
            throw new OclParserException("Invalid iterator kind!");
        }

        Expression sourceExp = parseOclExp(source, dm);

        String iterator = "iterator";
        String iteratorDeclRx = "^(.*)\\|(.*)$";
        Variable variable = new Variable(iterator, new Type());

        if (body.matches(iteratorDeclRx)) {
            iterator = trim(body.replaceFirst(iteratorDeclRx, "$1"));

            String sourceType = sourceExp.getType().getReferredType()
                .replaceFirst("Col\\((\\w+)\\)", "$1");

            Type varType = new Type(sourceType);
            variable = new Variable(sourceExp, iterator, varType);

            if (this.variableStack.contains(variable)) {
                throw new OclParserException(iterator + " already existed!");
            }

            if (this.adhocContextualSet.contains(variable)) {
                throw new OclParserException(
                    iterator + " is defined as free variable!");
            }

            body = trim(body.replaceFirst(iteratorDeclRx, "$2"));

            this.variableStack.push(variable);
//            System.out.println("String : " + body + " --> Stack : "
//                    + variableStack);
        }

        Expression bodyExp = "".equals(body) ? null : parseOclExp(body, dm);

        if (!iterator.equals("iterator")) {
            this.variableStack.pop();
        }

        Expression iteratorExp = new IteratorExp(sourceExp, kind, variable,
            bodyExp);
        Type type = getIteratorExpType(sourceExp, kind, bodyExp);
        iteratorExp.setType(type);

        return iteratorExp;
    }

    private Expression parseLiteralExp(String input, DataModel dm) {
        /**
         * Any character between two single quote '' E.g.: "'Lorem ipsum
         *  123|, !@#$%^&*('"
         * 
         */
        final String STRING_LITERAL_STR = "\\s*\\w*\\{(\\d+)*\\}";
        /**
         * Start ONLY with a word true OR false WITHOUT quote and
         * case-insensitive (?i) <- inline flag E.g.:"true", "false", "True",
         * "False", "TRUE", "FALSE" Note : there is NO single quote
         */
        final String BOOLEAN_LITERAL_STR = "(?i)(^\\btrue\\b|\\bfalse\\b$)";
        /**
         * String contains ONLY digit WITH an optional minus sign (-) denoting
         * negativity; this patterns allows, too, the decimal number by adding
         * optional group (.\\d+)?. This also allows JAVA legal way of writing
         * number using underscore (_) E.g.: "12334", "-12345", "123_3445",
         * "12_234.4893"
         */
        final String NUMERIC_LITERAL_STR = "^-?(\\d+(_\\d+)*(.\\d+)?)(?<!_)$";
        /**
         * This is strictly a natural number. If this string is given a number
         * that is greater than what an Integer can hold, the parser will throw
         * an Error.
         */
        final String INTEGER_LITERAL_STR = "^-?\\d+(_\\d+)*(?<!_)$";
        /**
         * This is strictly a real number. If this string is given a number that
         * is greater than what a Double can hold, the parser will throw an
         * Error.
         */
        final String REAL_LITERAL_STR = "^-?(\\d+(_\\d+)*.\\d+)(?<!_)$";

        /*
         * temporary for SQL literal
         */
        final String SQL_LITERAL_STR = "\\@SQL\\((.*)\\)";

        if (input.matches(NUMERIC_LITERAL_STR)) {

            if (input.matches(INTEGER_LITERAL_STR)) {

                IntegerLiteralExp intLitExp = new IntegerLiteralExp(
                    Integer.valueOf(input));
                intLitExp.setType(new Type("Integer"));
                return intLitExp;
            } else if (input.matches(REAL_LITERAL_STR)) {

                RealLiteralExp realLitExp = new RealLiteralExp(
                    Double.valueOf(input));
                realLitExp.setType(new Type("Real"));
                return realLitExp;
            }

        } else if (input.matches(STRING_LITERAL_STR)) {
            input = input.replaceFirst(STRING_LITERAL_STR, "$1");
            input = this.stringArray.get(Integer.valueOf(input));

            StringLiteralExp strLitExp = new StringLiteralExp(input);
            strLitExp.setType(new Type("String"));

            return strLitExp;

        } else if (input.matches(BOOLEAN_LITERAL_STR)) {

            BooleanLiteralExp boolLitExp = new BooleanLiteralExp(
                Boolean.valueOf(input));
            boolLitExp.setType(new Type("Boolean"));

            return boolLitExp;

        } else if (Pattern.matches(SQL_LITERAL_STR, input)) {

            return parseSqlFunctionExp(input);

        } else if (input.length() > 0) {

            // Check if it's a class
            if (DmUtils.isClass(dm, input)) {
                TypeExp type = new TypeExp(input);
                type.setType(new Type(input));

                return type;
            } else {
                for (int i = 0; i < this.variableStack.size(); i++) {
                    if (this.variableStack.get(i).getName().equals(input)) {
                        return new VariableExp(this.variableStack.get(i));
                    }
                }

                for (Variable v : this.adhocContextualSet) {
                    if (v.getName().equals(input)) {
                        return new VariableExp(v);
                    }
                }
            }
        } else {
//            throw new OclParserException(
//                input + "\n======\n" + "Invalid OCL Literal Expression!");
            return null;
        }

        return new NullLiteralExp();
    }

    private String encode(String ocl) {

        String encOcl = extractString(ocl);
        encOcl = extractParenthesis(encOcl);

        return encOcl;
    }

    private String decode(String encOcl) {
        return decode(encOcl, false);
    }

    private String decode(String encOcl, boolean shallow) {

        String decOcl = String.copyValueOf(encOcl.toCharArray());

        Pattern p = Pattern.compile("((.*)\\()(\\d+)(\\)(.*))");
        Pattern s = Pattern.compile("(.*)(\\{(\\d+)\\})(.*)");

        Matcher mP = p.matcher(decOcl);
        if (shallow) {
            if (mP.find()) {
                String content = this.parenthesisArray
                    .get(Integer.parseInt(mP.group(3)));
                decOcl = decOcl.replaceFirst(p.pattern(),
                    "$1" + content + "$4");
                mP = p.matcher(decOcl);
            }
        } else {
            while (mP.find()) {
                String content = this.parenthesisArray
                    .get(Integer.parseInt(mP.group(3)));
                decOcl = decOcl.replaceFirst(p.pattern(),
                    "$1" + content + "$4");
                mP = p.matcher(decOcl);
            }
        }

        Matcher mS = s.matcher(decOcl);
        while (mS.find()) {
            String content = this.stringArray
                .get(Integer.parseInt(mS.group(3)));
            decOcl = decOcl.replaceFirst(s.pattern(),
                "$1" + "'" + content + "'" + "$4");
            mS = s.matcher(decOcl);
        }

        return decOcl;
    }

    private String extractString(String encOcl) {

        Pattern pattern = Pattern.compile(ParserPatterns.STRING_LITERAL_STR);
        Matcher m = pattern.matcher(encOcl);
        String openBracket = "{";
        String closeBracket = "}";
        int level = 0;

        while (m.find()) {
            String content = m.group(1);
            content = content.replaceAll("^'|'$", "");
            this.stringArray.add(trim(content));
            encOcl = m.replaceFirst(openBracket + level + closeBracket);
            m = pattern.matcher(encOcl);
            level++;
        }

        return encOcl;
    }

    private String extractParenthesis(String encOcl) {

        Pattern pattern = Pattern
            .compile(ParserPatterns.PARENTHESIS_LITERAL_STR);
        Matcher m = pattern.matcher(encOcl);
        String openBracket = "[";
        String closeBracket = "]";
        int level = 0;

        while (m.find()) {
            String content = m.group(1);
            content = content.replaceAll("^\\(|\\)$", "");

            // **********************************************
            // in case of `... operation (a+b)...` being parsed to
            // `... operation [1] ...`
            content = content.replaceAll("\\[", "(");
            content = content.replaceAll("\\]", ")");
            // **********************************************

            this.parenthesisArray.add(trim(content));
            encOcl = m.replaceFirst(openBracket + level + closeBracket);
            m = pattern.matcher(encOcl);
            level++;
        }

        encOcl = encOcl.replaceAll("\\[", "(");
        encOcl = encOcl.replaceAll("\\]", ")");

        return encOcl;
    }

    private String replace(String input) {

        String output = String.copyValueOf(input.toCharArray());
        Pattern p = Pattern.compile("((\\w+)\\()(\\d+)(\\)(.*))");
        Matcher m = p.matcher(output);

        if (m.find()) {
            String content = this.parenthesisArray
                .get(Integer.valueOf(m.group(3)));

            if ("".equals(content)) {
                return content;
            }

            output = output.replaceFirst(p.pattern(), content);
        }

        return output;
    }

    private String trim(String input) {

        String spacePatt = "^(\\s)*|(\\s)*$";
        Matcher m = Pattern.compile(spacePatt).matcher(input);
        if (m.find()) {
            return input.replaceAll(spacePatt, "");
        }

        return input;
    }

    private Type getOperationExpType(String operationName, Expression leftExp,
        Expression... exps) {

        String leftExpType = "";

        if (leftExp instanceof OclExp) {
            leftExpType = leftExp.getType().getReferredType();
        }

        Type opType = new Type();
        switch (operationName) {
        case "allInstances":
            opType = new Type("Col(" + leftExpType + ")");
            return opType;
        case "not":
        case "=":
        case "<>":
        case "<":
        case ">":
        case ">=":
        case "<=":
        case "and":
        case "or":
        case "oclIsUndefined":
        case "oclIsKindOf":
        case "oclIsTypeOf":
            opType = new Type("Boolean");
            return opType;
        case "oclAsType":
//            String argType = exps[0].getType().getReferredType();
//            if (!UMLContextUtils.isSuperClassOf(this.dm, leftExpType,
//                    argType)) {
//                throw new OclParserException("\n======\n"
//                        + "Cannot perform casting!");
//            }
//            opType = new Type(argType);
//            return opType;
            throw new OclParserException(
                "\n======\n" + operationName + " not supported!");
        default:
            return opType;
        }
    }

    private Type getIteratorExpType(Expression source, String kind,
        Expression body) {
        Type type = new Type("Invalid");

        switch (IteratorKind.valueOf(kind)) {
        case any:
        case at:
        case first:
        case last:
        case sum:
            return new Type(source.getType().getReferredType()
                .replaceFirst("^Col\\((\\w+)\\)$", "$1"));

        case asBag:
        case asOrderedSet:
        case asSequence:
        case asSet:
        case excluding:
        case flatten:
        case including:
        case reject:
        case select:
        case sortedBy:
        case union:
            return new Type(source.getType().getReferredType());

        case excludes:
        case excludesAll:
        case exists:
        case forAll:
        case includes:
        case includesAll:
        case isEmpty:
        case isUnique:
        case notEmpty:
            return new Type("Boolean");

        case count:
        case indexOf:
        case one:
        case size:
            return new Type("Integer");
        case collect:
            return new Type("Col(" + body.getType().getReferredType() + ")");
        default:
            return type;
        }
    }

    @Override
    public Expression parse(String ocl, Object dm) {
        // TODO Auto-generated method stub
        return null;
    }
}
