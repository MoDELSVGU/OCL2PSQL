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

package org.vgu.dm2schema;

import static org.vgu.dm2schema.dm.MySQLConstraint.AUTO_INCREMENT;
import static org.vgu.dm2schema.dm.MySQLConstraint.NOT_NULL;
import static org.vgu.dm2schema.dm.MySQLConstraint.PRIMARY_KEY;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.vgu.dm2schema.dm.Association;
import org.vgu.dm2schema.dm.Attribute;
import org.vgu.dm2schema.dm.Constraint;
import org.vgu.dm2schema.dm.DataModel;
import org.vgu.dm2schema.dm.End;
import org.vgu.dm2schema.dm.Entity;
import org.vgu.dm2schema.dm.Invariant;
import org.vgu.dm2schema.sql.CreateDatabase;
import org.vgu.dm2schema.sql.CreateInvariantFunction;
import org.vgu.dm2schema.sql.CreateInvariantTrigger;
import org.vgu.dm2schema.sql.DropDatabase;
import org.vgu.dm2schema.sql.DropFunction;
import org.vgu.dm2schema.sql.DropTrigger;
import org.vgu.dm2schema.sql.Function;
import org.vgu.dm2schema.sql.Trigger;
import org.vgu.dm2schema.sql.TriggerAction;
import org.vgu.ocl2psql.main.OCL2PSQL_2;
import org.vgu.ocl2psql.ocl.roberts.exception.OclParseException;

import net.sf.jsqlparser.schema.Database;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.UseStatement;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.alter.AlterExpression;
import net.sf.jsqlparser.statement.alter.AlterOperation;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

public class DM2Schema {

    public static void main(String[] args) throws Exception {
        File dataModelFile = new File(
            "src/main/resources/genSQL/uni_pof_dm.json");
        File SQLschemaFile = new File("src/main/resources/genSQL/pof.sql");
        String databaseName = "unipof";

        DataModel dataModel = new DataModel(new JSONParser()
            .parse(new FileReader(dataModelFile.getAbsolutePath())));
        FileWriter fileWriter = new FileWriter(SQLschemaFile);

        List<String> schema = new ArrayList<String>();

        List<String> DBStatements = generateDBStatements(databaseName);
        schema.addAll(DBStatements);

        List<Statement> entityStatements = generateEntityStatements(dataModel);
        schema.addAll(entityStatements.stream().map(Statement::toString)
            .collect(Collectors.toList()));

        List<Statement> associationStatements = generateAssociationStatements(
            dataModel);
        schema.addAll(associationStatements.stream().map(Statement::toString)
            .collect(Collectors.toList()));

//        List<String> invariantFunctions = generateInvariantFunctions(dataModel);
//        schema.addAll(invariantFunctions);
        
//        List<String> invariantTriggers = generateInvariantTriggers(dataModel);
//        schema.addAll(invariantTriggers);

        schema.forEach(statement -> {
            try {
                fileWriter.write(SQLStatementHelper.transform(statement));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        fileWriter.flush();
        fileWriter.close();
    }

    private static List<String> generateInvariantTriggers(DataModel dataModel) {
        //TODO: A proof-of-concept
        List<String> invariantTriggers = new ArrayList<String>();
        List<Invariant> invariants = dataModel.getInvariantsFlatten();
        for (Invariant invariant : invariants) {
            Function function = new Function(invariant.getLabel());
            Trigger trigger = new Trigger(invariant.getLabel());
            trigger.setAction(TriggerAction.INSERT);
            trigger.setAfter(true);
            trigger.setTable(new Table("Program"));
            DropTrigger dropTrigger = new DropTrigger();
            dropTrigger.setIfExists(true);
            dropTrigger.setTrigger(trigger);
            CreateInvariantTrigger invariantTrigger = new CreateInvariantTrigger();
            invariantTrigger.setDelimiter("//");
            invariantTrigger.setTrigger(trigger);
            invariantTrigger.setFunction(function);
            invariantTriggers.add(dropTrigger.toString());
            invariantTriggers.add(invariantTrigger.toString());
        }
        return invariantTriggers;
    }

    private static List<String> generateInvariantFunctions(DataModel dataModel)
        throws OclParseException, ParseException, IOException {
        List<String> invariantFunctions = new ArrayList<String>();
        List<Invariant> invariants = dataModel.getInvariantsFlatten();
        for (Invariant invariant : invariants) {
            Function function = new Function(invariant.getLabel());
            DropFunction dropFunction = new DropFunction();
            dropFunction.setIfExists(true);
            dropFunction.setFunction(function);
            CreateInvariantFunction invariantFunction = new CreateInvariantFunction();
            invariantFunction.setDelimiter("//");
            invariantFunction.setFunction(function);
            invariantFunction
                .setSqlInvariant(translateToSQL(dataModel, invariant.getOcl()));
            invariantFunctions.add(dropFunction.toString());
            invariantFunctions.add(invariantFunction.toString());
        }
        return invariantFunctions;
    }

    private static String translateToSQL(DataModel dataModel, String ocl)
        throws OclParseException, ParseException, IOException {
        OCL2PSQL_2 ocl2sql = new OCL2PSQL_2();
        ocl2sql.setDescriptionMode(false);
        ocl2sql.setDataModel(dataModel);
        return ocl2sql.mapOCLStringToSQLString(ocl);
    }

    private static List<Statement> generateAssociationStatements(
        DataModel dataModel) {
        return createAssociationStatements(dataModel.getAssociations());
    }

    private static List<Statement> generateEntityStatements(
        DataModel dataModel) {
        return createTablesStatements(dataModel.getEntities());
    }

    private static List<String> generateDBStatements(String databaseName) {
        List<String> dbstatements = new ArrayList<String>();
        Database database = new Database(databaseName);

        DropDatabase dropDatabase = new DropDatabase();
        dropDatabase.setName(database);
        dropDatabase.setIfExists(true);
        dbstatements.add(dropDatabase.toString());

        CreateDatabase createDatabase = new CreateDatabase();
        createDatabase.setDatabase(database);
        dbstatements.add(createDatabase.toString());

        UseStatement useDatabase = new UseStatement(database.getDatabaseName());
        dbstatements.add(useDatabase.toString());
        return dbstatements;
    }

    private static List<Statement> createAssociationStatements(
        Set<Association> associations) {
        List<Statement> associationTables = new ArrayList<Statement>();
        for (Association association : associations) {
            if (association.isManyToMany())
                associationTables.addAll(createAssociationTable(association));
            else if (association.isManyToOne()) {
                associationTables.addAll(createReferences(association));
            } else if (association.isOneToOne()) {
                associationTables
                    .addAll(createBothSidesReferences(association));
            }
        }
        return associationTables;
    }

    private static List<Statement> createBothSidesReferences(
        Association association) {
        End leftEnd = association.getLeft();
        End rightEnd = association.getRight();

        Table leftTable = new Table(leftEnd.getTargetClass());
        List<Statement> foreignReferences = new ArrayList<Statement>();
        Alter foreignLeftKeys = new Alter();
        foreignReferences.add(foreignLeftKeys);
        foreignLeftKeys.setTable(leftTable);
        AlterExpression addLeftColumnExpression = new AlterExpression();
        AlterExpression referenceLeftExpression = new AlterExpression();
        foreignLeftKeys.setAlterExpressions(
            Arrays.asList(addLeftColumnExpression, referenceLeftExpression));
        addLeftColumnExpression.setOperation(AlterOperation.ADD);
        ColDataType referenceLeftColumnDataType = new ColDataType();
        referenceLeftColumnDataType.setDataType("INT");
        referenceLeftColumnDataType.setArgumentsStringList(Arrays.asList("11"));
        addLeftColumnExpression.addColDataType(leftEnd.getOpp(),
            referenceLeftColumnDataType);
        referenceLeftExpression.setOperation(AlterOperation.ADD);
        referenceLeftExpression.setFkColumns(Arrays.asList(leftEnd.getOpp()));
        referenceLeftExpression.setFkSourceColumns(
            Arrays.asList(String.format("%1$s_id", leftEnd.getCurrentClass())));
        referenceLeftExpression.setFkSourceTable(leftEnd.getCurrentClass());
        Alter foreignRightKeys = new Alter();
        foreignReferences.add(foreignRightKeys);

        Table rightTable = new Table(rightEnd.getTargetClass());
        foreignRightKeys.setTable(rightTable);
        AlterExpression addRightColumnExpression = new AlterExpression();
        AlterExpression referenceRightExpression = new AlterExpression();
        foreignRightKeys.setAlterExpressions(
            Arrays.asList(addRightColumnExpression, referenceRightExpression));
        addRightColumnExpression.setOperation(AlterOperation.ADD);
        ColDataType referenceRightColumnDataType = new ColDataType();
        referenceRightColumnDataType.setDataType("INT");
        referenceRightColumnDataType
            .setArgumentsStringList(Arrays.asList("11"));
        addRightColumnExpression.addColDataType(rightEnd.getOpp(),
            referenceRightColumnDataType);
        referenceRightExpression.setOperation(AlterOperation.ADD);
        referenceRightExpression.setFkColumns(Arrays.asList(rightEnd.getOpp()));
        referenceRightExpression.setFkSourceColumns(Arrays
            .asList(String.format("%1$s_id", rightEnd.getCurrentClass())));
        referenceRightExpression.setFkSourceTable(rightEnd.getCurrentClass());

        return foreignReferences;
    }

    private static List<Statement> createReferences(Association association) {
        End manyEnd = association.getManyEnd();
        Table table = new Table(manyEnd.getTargetClass());
        List<Statement> foreignReferences = new ArrayList<Statement>();
        Alter foreignKeys = new Alter();
        foreignReferences.add(foreignKeys);
        foreignKeys.setTable(table);
        AlterExpression addColumnExpression = new AlterExpression();
        AlterExpression referenceExpression = new AlterExpression();
        foreignKeys.setAlterExpressions(
            Arrays.asList(addColumnExpression, referenceExpression));
        addColumnExpression.setOperation(AlterOperation.ADD);
        ColDataType referenceColumnDataType = new ColDataType();
        referenceColumnDataType.setDataType("INT");
        referenceColumnDataType.setArgumentsStringList(Arrays.asList("11"));
        addColumnExpression.addColDataType(manyEnd.getOpp(),
            referenceColumnDataType);
        referenceExpression.setOperation(AlterOperation.ADD);
        referenceExpression.setFkColumns(Arrays.asList(manyEnd.getOpp()));
        referenceExpression.setFkSourceColumns(
            Arrays.asList(String.format("%1$s_id", manyEnd.getCurrentClass())));
        referenceExpression.setFkSourceTable(manyEnd.getCurrentClass());
        return foreignReferences;
    }

    private static List<Statement> createAssociationTable(
        Association association) {
        List<Statement> createAssocTableStatements = new ArrayList<Statement>();
        CreateTable createAssociation = new CreateTable();
        createAssocTableStatements.add(createAssociation);
        // Get association name
        Table table = new Table(association.getName());
        createAssociation.setTable(table);
        // Get association-ends
        List<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();
        createAssociation.setColumnDefinitions(columns);
        // Add association-ends
        ColumnDefinition leftColumn = createAssociationColumn(
            association.getLeftEnd());
        columns.add(leftColumn);
        ColumnDefinition rightColumn = createAssociationColumn(
            association.getRightEnd());
        columns.add(rightColumn);

        Alter foreignKeys = new Alter();
        createAssocTableStatements.add(foreignKeys);
        foreignKeys.setTable(table);
        AlterExpression leftExpression = new AlterExpression();
        AlterExpression rightExpression = new AlterExpression();
        foreignKeys.setAlterExpressions(
            Arrays.asList(leftExpression, rightExpression));

        leftExpression.setOperation(AlterOperation.ADD);
        leftExpression.setFkColumns(Arrays.asList(association.getLeftEnd()));
        leftExpression.setFkSourceColumns(Arrays
            .asList(String.format("%1$s_id", association.getLeftEntityName())));
        leftExpression.setFkSourceTable(association.getLeftEntityName());

        rightExpression.setOperation(AlterOperation.ADD);
        rightExpression.setFkColumns(Arrays.asList(association.getRightEnd()));
        rightExpression.setFkSourceColumns(Arrays.asList(
            String.format("%1$s_id", association.getRightEntityName())));
        rightExpression.setFkSourceTable(association.getRightEntityName());
        return createAssocTableStatements;
    }

    private static ColumnDefinition createAssociationColumn(String name) {
        ColumnDefinition column = new ColumnDefinition();
        column.setColumnName(name);
        // Set column type
        ColDataType colDataType = new ColDataType();
        colDataType.setDataType("INT");
        colDataType.setArgumentsStringList(Arrays.asList("11"));
        column.setColDataType(colDataType);
        return column;
    }

    private static List<Statement> createTablesStatements(
        Map<String, Entity> entities) {
        List<Statement> tables = new ArrayList<Statement>();
        for (Map.Entry<String, Entity> entry : entities.entrySet()) {
            Entity entity = entry.getValue();

            CreateTable createTable = new CreateTable();
            tables.add(createTable);
            // Set table name
            Table table = new Table(entity.getName());
            createTable.setTable(table);
            // Set columns
            List<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();
            createTable.setColumnDefinitions(columns);
            // Add id_column
            ColumnDefinition idColumn = createIdentityColumn(table);
            columns.add(idColumn);
            // Add other attributes
            for (Attribute attribute : entity.getAttributes()) {
                ColumnDefinition column = createAttributeColumn(attribute);
                columns.add(column);
            }
        }
        return tables;
    }

    private static ColumnDefinition createAttributeColumn(Attribute attribute) {
        ColumnDefinition column = new ColumnDefinition();
        column.setColumnName(attribute.getName());
        // Set column type
        ColDataType colDataType = new ColDataType();
        colDataType
            .setDataType(DM2SQLTypeConversion.convert(attribute.getType()));
        colDataType.setArgumentsStringList(
            DM2SQLTypeConversion.addArgument(attribute.getType()));
        // Set column constraints
        List<String> constraints = new ArrayList<String>();
        for (Constraint constraint : attribute.getConstraints()) {
            constraints.add(constraint.getConstraint());
        }
        column.setColumnSpecStrings(constraints);
        column.setColDataType(colDataType);
        return column;
    }

    private static ColumnDefinition createIdentityColumn(Table table) {
        ColumnDefinition idColumn = new ColumnDefinition();
        idColumn.setColumnName(String.format("%1$s_id", table.getName()));
        ColDataType idDataType = new ColDataType();
        idDataType.setDataType("INT");
        idDataType.setArgumentsStringList(Arrays.asList("11"));
        idColumn.setColDataType(idDataType);
        idColumn.setColumnSpecStrings(
            Arrays.asList(NOT_NULL, AUTO_INCREMENT, PRIMARY_KEY));
        return idColumn;
    }

}
