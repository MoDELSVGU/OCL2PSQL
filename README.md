# OCL2PSQL

The Object Constraint Language (OCL) is a textual, declarative language typically used as part of the UML standard for specifying constraints and queries on models.

OCL2PSQL implements a novel mapping
from OCL to pure SQL that deals with
(possibly nested) iterator expressions,
without resorting to imperative features of SQL.

OCL2PSQL generates SQL queries that can be efficiently
executed on mid- and large-size SQL databases.

## Introduction

This web service is intended for readers of our manuscripts:

* Mapping OCL into SQL: Challenges and Opportunities Ahead
* OCL2PSQL: An OCL-to-SQL Code-Generator for Model-Driven Engineering

Please, be aware that OCL2PSQL is an on-going research project.
In particular, OCL2PSQL does **NOT** cover yet the full OCL language. 

Moreover, for the time being, OCL expressions should adjust to the following syntactical conventions:

* Use single quotation marks for string-literals. E.g., `p.Person:name = 'string'`.
* **allInstances-expressions**. `<class>.allInstances()` should be written `<class>::allInstances()`. E.g., `Car::allInstances()`.
* **dot-expressions**. `<var>.<attr>` should be written `<var>.<class>:<attr>`, where `<class>` is the class of `<attr>`. E.g., `p.Person:name`.
* Similarly, `<var>.<assoc-end>` should be written `<var>.<class>:<assoc-end>`, where `<class>` is the source-class of `<assoc-end>`. E.g., `c.Car:owners`.

This table represents a detailed overview of supported Object Constraint Language (OCL) operators in our implementation `OCL2PSQL`. It consists of several categories, in which contains 2 columns: the operator (on the left) and the syntax example (on the right).

##### Class operations
|||
|--------------------------|---|
|`allInstances`  |  *class*.`allInstances()` |
|`attributes`  |  *var*.`att` |
|`association ends`  |  *var*.`assoc` |

##### Constants operations
|||
|--------------------------|---|
|`Boolean Literal`  |  `{TRUE, FALSE}`|
|`Integer Literal`  |  `{..., -2, 1, 0, 1, 2, ...}`|
|`String Literal`  |  `'string'`|

##### Boolean operations
|||
|--------------------------|---|
|`not`  |  `NOT` *bool-expr* |
|`and`, `or`| *bool-expr* `logic-operator` *bool-expr'*|
| =, <>, >, <, &#8805;, &#8804;  |  *bool-expr* `compare-operator` *bool-expr'*|

##### Iterative operations
|||
|--------------------------|---|
|`collect`  |  *source-expr* -> `collect`(*var* &#124; *body-expr*) |
|`forAll`  |  *source-expr* -> `forAll`(*var* &#124; *bool-expr*) |
|`exists`  |  *source-expr* -> `exists`(*var* &#124; *bool-expr*) |
|`select`  |  *source-expr* -> `select`(*var* &#124; *bool-expr*) |
|`reject`  |  *source-expr* -> `reject`(*var* &#124; *bool-expr*) |
|`size`  |  *source-expr* -> `size()`|
|`isEmpty`  |  *source-expr* -> `isEmpty()`|
|`notEmpty`  |  *source-expr* -> `notEmpty()`|
|`isUnique`  |  *source-expr* -> `isUnique()`|
|`flatten`[<sup>1</sup>]  |  *source-expr* -> `flatten()`|

##### Bag operations
|||
|--------------------------|---|
|`asSet`  |  *bag-expr* -> `asSet()`|

###### [<sup>1</sup>] For the time being, this can operate *ONLY* after the `collect` operation
[<sup>1</sup>]:#-For

# Quick Guideline

An easy way to use `OCL2PSQL` library is 

```java
OCL2PSQL ocl2psql = new OCL2PSQL();
String filePath = "/absolute/path/of/contextual/data/model";
ocl2psql.setPlainUMLContextFromFile(filePath);
ocl2psql.setDescriptionMode(true);

String oclExpression = "Car::allInstances()->collect(c.Car:owners->isUnique())";
String finalStatement = ocl2psql.mapToString(oclExpression);
```
###### The contextual data model definition can be found in the manuscript above.
###### Description mode add OCL sub-expressions as comments to generated SQL statements. Its default value is false.

For the interested individuals, we provide one sample scenario where the *contextual model* of the OCL expressions can be translated by the model `CarOwnership` [(See here)](http://researcher-paper.ap-southeast-1.elasticbeanstalk.com/model.html) and the generated SQL expressions are *solely* intended for the database schema CarDB [(See here)](http://researcher-paper.ap-southeast-1.elasticbeanstalk.com/schema.html). 
