package org.vgu.sqlsi.ocl.expressions;

import java.util.ArrayList;
import java.util.List;

import org.vgu.sqlsi.main.Pair;


public class SeqVarUtils {
    public static Pair<List<String>, OclExpression> SeqVar(OclExpression source, String posString) throws RuntimeException {
        List<Integer> pos = new ArrayList<Integer>();
        String[] posStrings = posString.split("\\.");
        for(String s : posStrings) {
            pos.add(Integer.valueOf(s));
        }
        return SeqVarAux(new ArrayList<String>(), source, pos);
    }

    private static Pair<List<String>, OclExpression> SeqVarAux(List<String> arrayList, 
            OclExpression source, 
            List<Integer> pos) throws RuntimeException {
        if(pos.isEmpty()) {
            return new Pair<List<String>, OclExpression>(arrayList, source) ;
        }
        int currentPos = fetchCurrentPos(pos);
        if(source instanceof OperationCallExp) {
            OperationCallExp operationCallExp = (OperationCallExp) source;
            if("allInstances".equals(operationCallExp.getName())) {
                checkInvalidPos(pos);
                checkInvalidRightSubExpression(currentPos);
                return SeqVarAux(arrayList, operationCallExp.getSource(), pos);
            }
            if("oclIsUndefined".equals(operationCallExp.getName())) {
                checkInvalidPos(pos);
                checkInvalidRightSubExpression(currentPos);
                return SeqVarAux(arrayList, operationCallExp.getSource(), pos);
            }
            if("=".equals(operationCallExp.getName())) {
                if(currentPos == 0)
                    return SeqVarAux(arrayList, operationCallExp.getSource(), pos);
                return SeqVarAux(arrayList, operationCallExp.getArguments().get(0), pos);
            }
        }
        if(source instanceof TypeExp) {
            checkInvalidPos(pos);
            return new Pair<List<String>, OclExpression>(arrayList, source) ;
        }
        if(source instanceof IteratorExp) {
            IteratorExp iteratorExp = (IteratorExp) source;
            switch (iteratorExp.kind) {
            case size:
            case flatten:
                checkInvalidRightSubExpression(currentPos);
                return SeqVarAux(arrayList, iteratorExp.getSource(), pos);
            case select:
            case forAll:
            case exists:
            case collect:
                if(currentPos == 0) return SeqVarAux(arrayList, iteratorExp.getSource(), pos);
                if(pos.isEmpty()) throw new RuntimeException("invalid subexpression");
                currentPos = fetchCurrentPos(pos);
                checkInvalidLeftSubExpression(currentPos);
                arrayList.add(iteratorExp.getIterator().getName());
                return SeqVarAux(arrayList, iteratorExp.getBody(), pos);
            default:
                throw new RuntimeException("unsupported operator");
            }
        }
        if(source instanceof PropertyCallExp) {
            checkInvalidPos(pos);
            checkInvalidRightSubExpression(currentPos);
            return new Pair<List<String>, OclExpression>(arrayList, source);
        }
        throw new RuntimeException("unsupported operator");
    }

    private static void checkInvalidRightSubExpression(int currentPos) throws RuntimeException {
        if(currentPos == 1) throw new RuntimeException("invalid subexpression");
    }
    
    private static void checkInvalidLeftSubExpression(int currentPos) throws RuntimeException {
        if(currentPos == 0) throw new RuntimeException("invalid subexpression");
    }

    private static int fetchCurrentPos(List<Integer> pos) {
        int currentPos = pos.get(0);
        pos.remove(0);
        return currentPos;
    }

    private static void checkInvalidPos(List<Integer> pos) throws RuntimeException {
        if(!pos.isEmpty()) {
            throw new RuntimeException("There is something wrong with the position");
        }
    }
}
