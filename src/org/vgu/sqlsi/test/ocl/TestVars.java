package org.vgu.sqlsi.test.ocl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.vgu.sqlsi.ocl.expressions.VariableUtils;

public class TestVars {
    
    public static void main(String[] args) {
        List<Pair> suite = new ArrayList<Pair>();
        suite.add(new Pair("Car::allInstances()", new ArrayList<String>()));
        suite.add(new Pair("true", new ArrayList<String>()));
        suite.add(new Pair("Car::allInstances()->collect(c|c.Car:color)", new ArrayList<String>()));
        suite.add(new Pair("Car::allInstances()->exists(c|c.Car:color='black')", new ArrayList<String>()));
    
//        for (Pair item : suite) {
//            String ocl = item.getOcl();
//            List<String> freeVars = item.getVars();
//            VariableUtils.FVars(selectBody)
//        }
    }
}
