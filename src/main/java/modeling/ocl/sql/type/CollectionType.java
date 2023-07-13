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


package modeling.ocl.sql.type;

public class CollectionType<E extends Type> extends Type {
    private static final String COLLECTION = "Col";
    private E elementType;

    public void setElementType(E elementType) {
        this.elementType = elementType;
    }
    
    public Type getElementType() {
        return elementType;
    }
    
    @Override
    public String getTypeName() {
        return String.format("%s(%s)", COLLECTION, elementType.getTypeName());
    }
}
