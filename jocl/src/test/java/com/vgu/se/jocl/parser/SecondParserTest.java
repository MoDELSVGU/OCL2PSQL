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


package com.vgu.se.jocl.parser;

import java.io.File;
import java.io.FileReader;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.vgu.se.jocl.env.Environment;

@Ignore
public class SecondParserTest {
    
    public Environment env = null;

    @Before
    public void setUp() throws Exception {
        env = new Environment();
        env.setPlainUMLContext(new FileReader(new File("./src/test/resources/test_context.json").getAbsoluteFile()));
    }

    @Test(expected = Exception.class) //TODO: a specific exception
    public void throwingException() {
        env.setOclExpStr(null);
        env.parse();
    }
    
    @Test
    public void runningTest1() {
        env.setOclExpStr("true");
        env.parse();
    }
    
    @Test
    public void runningTest2() {
        env.setOclExpStr("True");
        env.parse();
    }
    
    @Test
    public void runningTest3() {
        env.setOclExpStr("'String'");
        env.parse();
    }
    
    @Test
    public void runningTest4() {
        env.setOclExpStr("1");
        env.parse();
    }
    
    @Test
    public void runningTest5() {
        env.setOclExpStr("A.allInstances()");
        env.parse();
    }
    
    @Test
    public void runningTest6() {
        env.setOclExpStr("A.allInstances()->isEmpty()");
        env.parse();
    }

    @Test
    public void runningTest7() {
        env.setOclExpStr("A.allInstances()->collect(c|c.a)");
        env.parse();
    }
    
    @Test
    public void runningTest8() {
        env.setOclExpStr("A.allInstances()->collect(c|c.cb");
        env.parse();
    }
    
    @Test
    public void runningTest9() {
        env.setOclExpStr("A.allInstances()->size() = 2");
        env.parse();
    }
    
    @Test
    public void runningTest10() {
        env.setOclExpStr("1 = 1");
        env.parse();
    }
    
    @Test
    public void runningTest11() {
        env.setOclExpStr("A.allInstances()->collect(a|a.cb->collect(b|b.b))");
        env.parse();
    }
    
    @Test
    public void runningTest12() {
        env.setOclExpStr("A.allInstances()->collect(a|a.cb->collect(b|b.b))->size()");
        env.parse();
    }
}
