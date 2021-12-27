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


package com.vgu.se.jocl.expressions;

import com.vgu.se.jocl.visit.ParserVisitor;

public class IterateExp extends LoopExp {

    @Override
    public void accept(ParserVisitor parserVisitor) {
        parserVisitor.visit(this);
    }

//    private CollectionType<Classifier> source;
//    private OclExp body;

    // has to be the Type of the result of the evaluation on body;

    // v stands for Variable ; v has to have the same type with source;
    // ( source.getClass() ) v;
//    private Variable v; 
//    public void setSource( CollectionType<Classifier> source ) {
//        this.source = source;
//    }
    
   
}



