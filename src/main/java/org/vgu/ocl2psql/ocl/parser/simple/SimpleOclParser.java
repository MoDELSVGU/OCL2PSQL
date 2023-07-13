/**************************************************************************
Copyright 2019-2020 Vietnamese-German-University

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

package org.vgu.ocl2psql.ocl.parser.simple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.vgu.ocl2psql.ocl.parser.exception.MappingException;
import org.vgu.ocl2psql.sql.statement.select.Join;
import org.vgu.ocl2psql.sql.statement.select.PlainSelect;
import org.vgu.ocl2psql.sql.statement.select.ResSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.Select;
import org.vgu.ocl2psql.sql.statement.select.SubSelect;
import org.vgu.ocl2psql.sql.statement.select.ValSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.VarSelectExpression;

import com.vgu.se.jocl.expressions.AssociationClassCallExp;
import com.vgu.se.jocl.expressions.BooleanLiteralExp;
import com.vgu.se.jocl.expressions.IntegerLiteralExp;
import com.vgu.se.jocl.expressions.IteratorExp;
import com.vgu.se.jocl.expressions.IteratorKind;
import com.vgu.se.jocl.expressions.LiteralExp;
import com.vgu.se.jocl.expressions.M2MAssociationClassCallExp;
import com.vgu.se.jocl.expressions.M2OAssociationClassCallExp;
import com.vgu.se.jocl.expressions.O2OAssociationClassCallExp;
import com.vgu.se.jocl.expressions.OclExp;
import com.vgu.se.jocl.expressions.OperationCallExp;
import com.vgu.se.jocl.expressions.PropertyCallExp;
import com.vgu.se.jocl.expressions.StringLiteralExp;
import com.vgu.se.jocl.expressions.TypeExp;
import com.vgu.se.jocl.expressions.Variable;
import com.vgu.se.jocl.expressions.VariableExp;
import com.vgu.se.jocl.utils.VariableUtils;
import com.vgu.se.jocl.visit.ParserVisitor;

import modeling.data.entities.DataModel;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NotExpression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.Distinct;
import net.sf.jsqlparser.statement.select.GroupByElement;

public class SimpleOclParser implements ParserVisitor {

	private Select select;
	private DataModel dataModel;

	public DataModel getDataModel() {
		return dataModel;
	}

	public void setDataModel(DataModel dm) {
		this.dataModel = dm;
	}

	public Select getSelect() {
		return this.select;
	}

	public void setSelect(Select select) {
		this.select = select;
	}

	private void addComment(com.vgu.se.jocl.expressions.Expression exp, PlainSelect plainSelect) {
		if (exp.getOclStr() != null) {
			plainSelect.setCorrespondOCLExpression(exp.getOclStr());
		}
	}

	@Override
	public void visit(IteratorExp iteratorExp) {
		PlainSelect plainSelect = null;

		switch (IteratorKind.valueOf(iteratorExp.getKind())) {
		case any:
			break;
		case asBag:
			break;
		case asOrderedSet:
			break;
		case asSequence:
			break;
		case asSet:
			plainSelect = mapAsSet(iteratorExp);
			break;
		case at:
			break;
		case collect:
			plainSelect = mapCollect(iteratorExp);
			break;
		case count:
			break;
		case excludes:
			break;
		case excludesAll:
			break;
		case excluding:
			break;
		case exists:
			plainSelect = mapExists(iteratorExp);
			break;
		case first:
			break;
		case flatten:
			plainSelect = mapFlatten(iteratorExp);
			break;
		case forAll:
			plainSelect = mapForAll(iteratorExp);
			break;
		case includes:
			break;
		case includesAll:
			break;
		case including:
			break;
		case indexOf:
			break;
		case isEmpty:
			plainSelect = mapIsEmpty(iteratorExp);
			break;
		case isUnique:
			plainSelect = mapIsUnique(iteratorExp);
			break;
		case last:
			break;
		case notEmpty:
			plainSelect = mapNotEmpty(iteratorExp);
			break;
		case one:
			break;
		case reject:
			plainSelect = mapReject(iteratorExp);
			break;
		case select:
			plainSelect = mapSelect(iteratorExp);
			break;
		case size:
			plainSelect = mapSize(iteratorExp);
			break;
		case sortedBy:
			break;
		case sum:
			break;
		case union:
			break;
		default:
			break;
		}

		addComment(iteratorExp, plainSelect);

		this.select = new Select();
		this.select.setSelectBody(plainSelect);
	}

	@Override
	public void visit(OperationCallExp operationCallExp) {
		PlainSelect plainSelect = null;

		switch (operationCallExp.getReferredOperation().getName()) {
		case "allInstances":
			plainSelect = mapAllInstances(operationCallExp);
			break;
		case "oclIsUndefined":
			plainSelect = mapOclIsUndefined(operationCallExp);
			break;
		case "oclIsKindOf":
			plainSelect = mapOclIsKindOf(operationCallExp);
			break;
		case "oclIsTypeOf":
			plainSelect = mapOclIsTypeOf(operationCallExp);
			break;
		case "oclAsType":
			plainSelect = mapOclAsType(operationCallExp);
			break;
		case "=":
		case "<>":
		case "<=":
		case ">=":
		case ">":
		case "<":
		case "and":
		case "or":
			plainSelect = mapBinary(operationCallExp);
			break;
		case "not":
			plainSelect = mapNot(operationCallExp);
			break;
		case "implies":
			plainSelect = mapImplies(operationCallExp);
			break;
		// TODO: This is a lazy attempt to map size from iteratorExp to
		// OperationCallExp.
		// Disclaimer: According to the OCL 2.4, size, isEmpty, flatten...
		// should be placed here!
		case "size":
			plainSelect = mapSize(operationCallExp);
			break;
		case "isEmpty":
			plainSelect = mapIsEmpty(operationCallExp);
			break;
		case "notEmpty":
			plainSelect = mapNotEmpty(operationCallExp);
			break;
		case "isUnique":
			plainSelect = mapIsUnique(operationCallExp);
			break;
		case "flatten":
			plainSelect = mapFlatten(operationCallExp);
			break;
		default:
			break;
		}

		addComment(operationCallExp, plainSelect);

		this.select = new Select();
		this.select.setSelectBody(plainSelect);
	}

	private PlainSelect mapFlatten(OperationCallExp exp) {
		if (!(exp.getSource() instanceof IteratorExp)) {
			throw new MappingException("Cannot flatten a source which is " + "not a collection.");
		}

		PlainSelect plainSelect = new PlainSelect();
		String tmpSourceAlias = "TEMP_src";

		IteratorExp tmpSourceExp = (IteratorExp) exp.getSource();
		tmpSourceExp.accept(this);
		Select tmpSourceMap = this.getSelect();

		SubSelect tmpSource = new SubSelect(tmpSourceMap.getSelectBody(), tmpSourceAlias);

		List<Variable> fVarsExp = VariableUtils.FVars(exp);
		boolean isEmptyFvExp = fVarsExp.isEmpty();

		if (isEmptyFvExp) {
			plainSelect.createTrueValColumn();

			ResSelectExpression res = new ResSelectExpression(new Column(Arrays.asList(tmpSourceAlias, "res")));
			plainSelect.setRes(res);

			plainSelect.setFromItem(tmpSource);

			BinaryExpression where = buildBinExp("=", new Column(Arrays.asList(tmpSourceAlias, "val")),
					new LongValue(1L));
			plainSelect.setWhere(where);

		} else {
			String tmpFlattenAlias = "TEMP_flat";
			String tmpAlias = "TEMP";

			Variable v = ((IteratorExp) exp.getSource()).getIterator();

			com.vgu.se.jocl.expressions.Expression srcOfSrcExp = ((IteratorExp) exp.getSource()).getSource();
			srcOfSrcExp.accept(this);
			Select srcOfSrcMap = this.getSelect();
			SubSelect tmpSourceOfSource = new SubSelect(srcOfSrcMap.getSelectBody(), tmpSourceAlias);

			List<Variable> sVarsSrcOfSrcExp = VariableUtils.SVars(((IteratorExp) exp.getSource()).getSource());

			boolean isVarInBody = true; // assumtion on paper

			if (isVarInBody) {
				CaseExpression valCase = new CaseExpression();

				IsNullExpression isNull = new IsNullExpression();
				isNull.setLeftExpression(new Column(Arrays.asList(tmpFlattenAlias, "val")));

				valCase.setSwitchExpression(isNull);

				WhenClause when1Then0 = new WhenClause();
				when1Then0.setWhenExpression(new LongValue(1L));
				when1Then0.setThenExpression(new LongValue(0L));

				valCase.setWhenClauses(Arrays.asList(when1Then0));

				valCase.setElseExpression(new Column(Arrays.asList(tmpFlattenAlias, "val")));

				ValSelectExpression val = new ValSelectExpression(valCase);
				plainSelect.setVal(val);

				CaseExpression resCase = new CaseExpression();

				resCase.setSwitchExpression(isNull);

				WhenClause when1ThenNull = new WhenClause();
				when1ThenNull.setWhenExpression(new LongValue(1L));
				when1ThenNull.setThenExpression(new NullValue());

				resCase.setWhenClauses(Arrays.asList(when1ThenNull));

				resCase.setElseExpression(new Column(Arrays.asList(tmpFlattenAlias, "res")));

				ResSelectExpression res = new ResSelectExpression(resCase);
				plainSelect.setRes(res);

				org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsSrcOfSrcExp, plainSelect, tmpFlattenAlias);

				plainSelect.setFromItem(tmpSourceOfSource);

				Join join = new Join();
				join.setLeft(true);

				// Right item of left join
				PlainSelect plainSelectInJoin = new PlainSelect();
				plainSelectInJoin.addSelectItems(new AllColumns());

				SubSelect tmp = new SubSelect(tmpSourceMap.getSelectBody(), tmpAlias);
				plainSelectInJoin.setFromItem(tmp);

				BinaryExpression valEq1 = buildBinExp("=", new Column(Arrays.asList(tmpAlias, "val")),
						new LongValue(1L));
				plainSelectInJoin.setWhere(valEq1);

				SubSelect tmpFlatten = new SubSelect(plainSelectInJoin, tmpFlattenAlias);
				join.setRightItem(tmpFlatten);
				// .END Right item

				List<Variable> sVarsSrcOfSrcAndV = new ArrayList<Variable>(sVarsSrcOfSrcExp);
				sVarsSrcOfSrcAndV.add(v);

				BinaryExpression onExp = null;
				for (Variable var : sVarsSrcOfSrcExp) {
					BinaryExpression refSrcEqRefFlatten = buildBinExp("=",
							new Column(Arrays.asList(tmpSourceAlias, "ref_".concat(var.getName()))),
							new Column(Arrays.asList(tmpFlattenAlias, "ref_".concat(var.getName()))));

					if (onExp == null) {
						onExp = refSrcEqRefFlatten;
					} else {
						onExp = buildBinExp("and", onExp, refSrcEqRefFlatten);
					}
				}

				join.addOnExpression(onExp);

				plainSelect.setJoins(Arrays.asList(join));
			}
		}

		return plainSelect;
	}

	private PlainSelect mapIsUnique(OperationCallExp exp) {
		PlainSelect plainSelect = new PlainSelect();

		// Begin preparation
		String tmpSrcAlias = "TEMP_SRC";

		com.vgu.se.jocl.expressions.Expression srcExp = exp.getSource();
		srcExp.accept(this);

		Select srcMap = this.getSelect();
		SubSelect tmpSrc = new SubSelect(srcMap.getSelectBody(), tmpSrcAlias);

		List<Variable> fvExp = VariableUtils.FVars(exp);
		boolean isFvExpEmpty = fvExp.isEmpty();

		// . End preparation
		plainSelect.createTrueValColumn();

		Function countRes = new Function();
		countRes.setName("COUNT");
		ExpressionList expLs = new ExpressionList(new Column(Arrays.asList(tmpSrcAlias, "res")));
		countRes.setParameters(expLs);

		Function countDistinctRes = new Function();
		countDistinctRes.setName("COUNT");
		countDistinctRes.setDistinct(true);
		expLs = new ExpressionList(new Column(Arrays.asList(tmpSrcAlias, "res")));
		countDistinctRes.setParameters(expLs);

		BinaryExpression countExp = buildBinExp("=", countRes, countDistinctRes);

		if (isFvExpEmpty) {
			ResSelectExpression res = new ResSelectExpression(countExp);
			plainSelect.setRes(res);

			plainSelect.setFromItem(tmpSrc);

		} else {
			// Begin Preparation
			List<Variable> svSrc = VariableUtils.SVars(srcExp);
			// . End Preparation

			CaseExpression resCase = new CaseExpression();

			BinaryExpression srcValEq0 = buildBinExp("=", new Column(Arrays.asList(tmpSrcAlias, "val")),
					new LongValue(0L));
			resCase.setSwitchExpression(srcValEq0);

			WhenClause whenClause = new WhenClause();
			whenClause.setWhenExpression(new LongValue(1L));
			whenClause.setThenExpression(new LongValue(1L));

			resCase.setWhenClauses(Arrays.asList(whenClause));

			resCase.setElseExpression(countExp);

			ResSelectExpression res = new ResSelectExpression(resCase);
			plainSelect.setRes(res);

			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(svSrc, plainSelect, tmpSrcAlias);

			plainSelect.setFromItem(tmpSrc);

			GroupByElement groupBy = new GroupByElement();

			List<Expression> groupByList = new ArrayList<Expression>();
			for (Variable v : svSrc) {
				groupByList.add(new Column(Arrays.asList(tmpSrcAlias, "ref_".concat(v.getName()))));
			}
			groupByList.add(new Column(Arrays.asList(tmpSrcAlias, "val")));

			groupBy.addGroupByExpressions(groupByList);

			plainSelect.setGroupByElement(groupBy);
		}

		return plainSelect;
	}

	private PlainSelect mapNotEmpty(OperationCallExp exp) {
		PlainSelect plainSelect = new PlainSelect();

		// Begin preparation

		String tmpSrcAlias = "TEMP_SRC";
		com.vgu.se.jocl.expressions.Expression srcExp = exp.getSource();
		srcExp.accept(this);

		Select srcMap = this.getSelect();
		SubSelect tmpSrc = new SubSelect(srcMap.getSelectBody(), tmpSrcAlias);

		List<Variable> fvExp = VariableUtils.FVars(exp);
		boolean isFvExpEmpty = fvExp.isEmpty();

		// . End preparation

		plainSelect.createTrueValColumn();

		if (isFvExpEmpty) {
			Function count = new Function();
			count.setName("COUNT");
			count.setAllColumns(true);

			BinaryExpression resExp = buildBinExp(">", count, new LongValue(0L));

			ResSelectExpression res = new ResSelectExpression(resExp);
			plainSelect.setRes(res);

			plainSelect.setFromItem(tmpSrc);

		} else {
			List<Variable> svSrc = VariableUtils.SVars(srcExp);

			CaseExpression resCase = new CaseExpression();

			BinaryExpression srcValEq0 = buildBinExp("=", new Column(Arrays.asList(tmpSrcAlias, "val")),
					new LongValue(0L));
			resCase.setSwitchExpression(srcValEq0);

			WhenClause whenClause = new WhenClause();
			whenClause.setWhenExpression(new LongValue(1L));
			whenClause.setThenExpression(new LongValue(0L));

			resCase.setWhenClauses(Arrays.asList(whenClause));

			resCase.setElseExpression(new LongValue(1L));

			ResSelectExpression res = new ResSelectExpression(resCase);
			plainSelect.setRes(res);

			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(svSrc, plainSelect, tmpSrcAlias);

			plainSelect.setFromItem(tmpSrc);

			GroupByElement groupBy = new GroupByElement();

			List<Expression> groupByExps = new ArrayList<Expression>();
			for (Variable v : svSrc) {
				groupByExps.add(new Column(Arrays.asList(tmpSrcAlias, "ref_".concat(v.getName()))));
			}
			groupByExps.add(new Column(Arrays.asList(tmpSrcAlias, "val")));

			groupBy.addGroupByExpressions(groupByExps);

			plainSelect.setGroupByElement(groupBy);

		}

		return plainSelect;
	}

	private PlainSelect mapIsEmpty(OperationCallExp exp) {
		PlainSelect plainSelect = new PlainSelect();

		// Begin preparation

		String tmpSrcAlias = "TEMP_SRC";
		com.vgu.se.jocl.expressions.Expression srcExp = exp.getSource();
		srcExp.accept(this);

		Select srcMap = this.getSelect();
		SubSelect tmpSrc = new SubSelect(srcMap.getSelectBody(), tmpSrcAlias);

		List<Variable> fvExp = VariableUtils.FVars(exp);
		boolean isFvExpEmpty = fvExp.isEmpty();

		// . End preparation

		plainSelect.createTrueValColumn();

		if (isFvExpEmpty) {
			Function count = new Function();
			count.setName("COUNT");
			count.setAllColumns(true);

			BinaryExpression resExp = buildBinExp("=", count, new LongValue(0L));

			ResSelectExpression res = new ResSelectExpression(resExp);
			plainSelect.setRes(res);

			plainSelect.setFromItem(tmpSrc);

		} else {
			List<Variable> svSrc = VariableUtils.SVars(srcExp);

			CaseExpression resCase = new CaseExpression();

			BinaryExpression srcValEq0 = buildBinExp("=", new Column(Arrays.asList(tmpSrcAlias, "val")),
					new LongValue(0L));
			resCase.setSwitchExpression(srcValEq0);

			WhenClause whenClause = new WhenClause();
			whenClause.setWhenExpression(new LongValue(1L));
			whenClause.setThenExpression(new LongValue(1L));

			resCase.setWhenClauses(Arrays.asList(whenClause));

			resCase.setElseExpression(new LongValue(0L));

			ResSelectExpression res = new ResSelectExpression(resCase);
			plainSelect.setRes(res);

			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(svSrc, plainSelect, tmpSrcAlias);

			plainSelect.setFromItem(tmpSrc);

			GroupByElement groupBy = new GroupByElement();

			List<Expression> groupByExps = new ArrayList<Expression>();
			for (Variable v : svSrc) {
				groupByExps.add(new Column(Arrays.asList(tmpSrcAlias, "ref_".concat(v.getName()))));
			}
			groupByExps.add(new Column(Arrays.asList(tmpSrcAlias, "val")));

			groupBy.addGroupByExpressions(groupByExps);

			plainSelect.setGroupByElement(groupBy);

		}

		return plainSelect;
	}

	private PlainSelect mapSize(OperationCallExp exp) {
		PlainSelect plainSelect = new PlainSelect();
		plainSelect.createTrueValColumn();

		exp.getSource().accept(this);
		Select source = this.getSelect();

		String tmpSourceAlias = "TEMP_src";
		SubSelect tmpSource = new SubSelect(source.getSelectBody(), tmpSourceAlias);
		plainSelect.setFromItem(tmpSource);

		Function count = new Function();
		count.setName("COUNT");
		count.setAllColumns(true);

		if (VariableUtils.FVars(exp).isEmpty()) {
			ResSelectExpression res = new ResSelectExpression(count);
			plainSelect.setRes(res);

			return plainSelect;

		} else {
			BinaryExpression isValZero = buildBinExp("=", new Column(Arrays.asList(tmpSourceAlias, "val")),
					new LongValue(0L));

			WhenClause whenCase = new WhenClause();
			whenCase.setWhenExpression(new LongValue(1L));
			whenCase.setThenExpression(new LongValue(0L));

			CaseExpression caseRes = new CaseExpression();
			caseRes.setSwitchExpression(isValZero);
			caseRes.setWhenClauses(Arrays.asList(whenCase));
			caseRes.setElseExpression(count);

			ResSelectExpression res = new ResSelectExpression(caseRes);
			plainSelect.setRes(res);

			List<Expression> groupByFields = new ArrayList<Expression>();

			List<Variable> sVars = VariableUtils.SVars(exp);
			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVars, plainSelect, tmpSourceAlias);

			for (Variable v : sVars) {
				VarSelectExpression refVar = new VarSelectExpression(v.getName());
				Expression varCol = new Column(Arrays.asList(tmpSourceAlias, refVar.getRef().getAlias().getName()));
				refVar.setRefExpression(varCol);

				groupByFields.add(varCol);
			}

			groupByFields.add(new Column(Arrays.asList(tmpSourceAlias, "val")));

			GroupByElement groupByEl = new GroupByElement();
			groupByEl.addGroupByExpressions(groupByFields);
			plainSelect.setGroupByElement(groupByEl);

			return plainSelect;
		}
	}

	@Override
	public void visit(LiteralExp literalExp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(PropertyCallExp propertyCallExp) {
		// This is the implementation follow the definition
		// in the paper. Here we introduce a "short-cut".
//        PlainSelect plainSelect = map(propertyCallExp);

		// Here is the short-cut
		PlainSelect plainSelect = mapShortcut(propertyCallExp);

		addComment(propertyCallExp, plainSelect);

		this.select = new Select();
		this.select.setSelectBody(plainSelect);
	}

	private PlainSelect mapShortcut(PropertyCallExp exp) {

		if (exp.getNavigationSource() instanceof VariableExp) {
			VariableExp variableExp = (VariableExp) exp.getNavigationSource();
			if (variableExp.getVariable().getSource() instanceof OperationCallExp) {
				OperationCallExp operationCallExp = (OperationCallExp) variableExp.getVariable().getSource();
				if (operationCallExp.getReferredOperation().getName().equals("allInstances")) {
					PlainSelect plainSelect = new PlainSelect();

					String varType = variableExp.getType().getReferredType();
					String varName = variableExp.getVariable().getName();
					varType.replaceAll("Col\\((\\w+)\\)", varType);

					plainSelect.setFromItem(new Table(varType));

					ResSelectExpression res = new ResSelectExpression(
							new Column(Arrays.asList(varType, exp.getReferredProperty())));
					plainSelect.setRes(res);

					plainSelect.createTrueValColumn();

					VarSelectExpression var = new VarSelectExpression(varName);
					var.setRefExpression(new Column(Arrays.asList(varType, varType.concat("_id"))));
					plainSelect.addVar(var);

					return plainSelect;
				}
			}
		}

		return map(exp);

	}

	@Override
	public void visit(AssociationClassCallExp associationClassCallExp) {
		// This is the implementation follow the definition
		// in the paper. Here we introduce a "short-cut".
		PlainSelect plainSelect = map(associationClassCallExp);

		// Here is the short-cut
//        PlainSelect plainSelect = mapShortcut(associationClassCallExp);

		addComment(associationClassCallExp, plainSelect);

		this.select = new Select();
		this.select.setSelectBody(plainSelect);
	}

	@SuppressWarnings("unused")
	private PlainSelect mapShortcut(AssociationClassCallExp exp) {

		if (exp.getNavigationSource() instanceof VariableExp) {
			VariableExp variableExp = (VariableExp) exp.getNavigationSource();
			if (variableExp.getVariable().getSource() instanceof OperationCallExp) {
				OperationCallExp operationCallExp = (OperationCallExp) variableExp.getVariable().getSource();
				if (operationCallExp.getReferredOperation().getName().equals("allInstances")) {

					if (exp instanceof O2OAssociationClassCallExp) {
						return mapShortcutO2O(exp, variableExp);
					} else if (exp instanceof M2OAssociationClassCallExp) {
						return mapShortcutM2O(exp, variableExp);
					} else {
						return mapShortcutM2M(exp, variableExp);
					}

				}
			}
		}

		return map(exp);

	}

	private PlainSelect mapShortcutO2O(AssociationClassCallExp exp, VariableExp variableExp) {

		PlainSelect plainSelect = new PlainSelect();

		String varName = variableExp.getVariable().getName();
		@SuppressWarnings("unused")
		String referredEndType = exp.getReferredAssociationEndType().getReferredType();
		String referredEndName = exp.getReferredAssociationEnd();
		String oppositeEndType = exp.getOppositeAssociationEndType().getReferredType();

		plainSelect.createTrueValColumn();

		ResSelectExpression res = new ResSelectExpression(new Column(Arrays.asList(oppositeEndType, referredEndName)));
		plainSelect.setRes(res);

		VarSelectExpression var = new VarSelectExpression(varName);
		var.setRefExpression(new Column(Arrays.asList(oppositeEndType, oppositeEndType.concat("_id"))));
		plainSelect.addVar(var);

		plainSelect.setFromItem(new Table(oppositeEndType));

		return plainSelect;
	}

	private PlainSelect mapShortcutM2O(AssociationClassCallExp exp, VariableExp variableExp) {

		PlainSelect plainSelect = new PlainSelect();

		String varName = variableExp.getVariable().getName();
		String referredEndType = exp.getReferredAssociationEndType().getReferredType();
		String referredEndName = exp.getReferredAssociationEnd();
		String oppositeEndType = exp.getOppositeAssociationEndType().getReferredType();
		String oppositeEndName = exp.getOppositeAssociationEnd();

		if (((M2OAssociationClassCallExp) exp).isOneEndAssociationCall()) {
			ResSelectExpression res = new ResSelectExpression(
					new Column(Arrays.asList(oppositeEndType, referredEndName)));
			plainSelect.setRes(res);

			plainSelect.createTrueValColumn();

			VarSelectExpression var = new VarSelectExpression(varName);
			var.setRefExpression(new Column(Arrays.asList(oppositeEndType, oppositeEndType.concat("_id"))));
			plainSelect.addVar(var);

			plainSelect.setFromItem(new Table(oppositeEndType));
		} else {
			CaseExpression caseVal = new CaseExpression();
			IsNullExpression isOpposNull = new IsNullExpression();
			isOpposNull.setLeftExpression(new Column(Arrays.asList(referredEndType, oppositeEndName)));
			caseVal.setSwitchExpression(isOpposNull);
			WhenClause whenClause = new WhenClause();
			whenClause.setWhenExpression(new LongValue(1L));
			whenClause.setThenExpression(new LongValue(0L));
			caseVal.setWhenClauses(Arrays.asList(whenClause));
			caseVal.setElseExpression(new LongValue(1L));
			ValSelectExpression val = new ValSelectExpression(caseVal);
			plainSelect.setVal(val);

			ResSelectExpression res = new ResSelectExpression(
					new Column(Arrays.asList(referredEndType, referredEndType.concat("_id"))));
			plainSelect.setRes(res);

			CaseExpression caseType = new CaseExpression();
			caseType.setSwitchExpression(isOpposNull);
			WhenClause typeWhenClause = new WhenClause();
			typeWhenClause.setWhenExpression(new LongValue(1L));
			typeWhenClause.setThenExpression(new StringValue("EmptyCol"));
			caseType.setWhenClauses(Arrays.asList(typeWhenClause));
			caseType.setElseExpression(new StringValue(referredEndType));

			VarSelectExpression var = new VarSelectExpression(varName);
			var.setRefExpression(new Column(Arrays.asList(oppositeEndType, oppositeEndType.concat("_id"))));
			plainSelect.addVar(var);

			plainSelect.setFromItem(new Table(referredEndType));

			Join leftJ = new Join();
			leftJ.setLeft(true);
			leftJ.setRightItem(new Table(oppositeEndType));

			BinaryExpression EqOppos = buildBinExp("=",
					new Column(Arrays.asList(oppositeEndType, oppositeEndType.concat("_id"))),
					new Column(Arrays.asList(referredEndType, oppositeEndName)));

			leftJ.addOnExpression(EqOppos);

			plainSelect.setJoins(Arrays.asList(leftJ));
		}
		return plainSelect;
	}

	private PlainSelect mapShortcutM2M(AssociationClassCallExp exp, VariableExp variableExp) {
		PlainSelect plainSelect = new PlainSelect();

		String varName = variableExp.getVariable().getName();
		String associationTableName = exp.getAssociation();
		String referredEndType = exp.getReferredAssociationEndType().getReferredType();
		String oppositeEndType = exp.getOppositeAssociationEndType().getReferredType();
		String referredEndName = exp.getReferredAssociationEnd();
		String oppositeEndName = exp.getOppositeAssociationEnd();

		plainSelect.setFromItem(new Table(oppositeEndType));

		Join leftJoin = new Join();
		leftJoin.setLeft(true);
		leftJoin.setRightItem(new Table(associationTableName));
		BinaryExpression refVEqOppos = buildBinExp("=",
				new Column(Arrays.asList(oppositeEndType, oppositeEndType.concat("_id"))),
				new Column(Arrays.asList(associationTableName, oppositeEndName)));
		leftJoin.addOnExpression(refVEqOppos);
		plainSelect.setJoins(Arrays.asList(leftJoin));

		ResSelectExpression res = new ResSelectExpression(
				new Column(Arrays.asList(associationTableName, referredEndName)));
		plainSelect.setRes(res);

		ValSelectExpression val = new ValSelectExpression();
		CaseExpression caseVal = new CaseExpression();
		IsNullExpression isOpposNull = new IsNullExpression();
		isOpposNull.setLeftExpression(new Column(Arrays.asList(associationTableName, oppositeEndName)));
		caseVal.setSwitchExpression(isOpposNull);
		WhenClause whenValClause = new WhenClause();
		whenValClause.setWhenExpression(new LongValue(1L));
		whenValClause.setThenExpression(new LongValue(0L));
		caseVal.setWhenClauses(Arrays.asList(whenValClause));
		caseVal.setElseExpression(new LongValue(1L));
		val.setExpression(caseVal);
		plainSelect.setVal(val);

		CaseExpression caseType = new CaseExpression();
		caseType.setSwitchExpression(isOpposNull);
		WhenClause whenTypeClause = new WhenClause();
		whenTypeClause.setWhenExpression(new LongValue(1L));
		whenTypeClause.setThenExpression(new StringValue("EmptyCol"));
		caseType.setWhenClauses(Arrays.asList(whenTypeClause));
		caseType.setElseExpression(new StringValue(referredEndType));

		VarSelectExpression var = new VarSelectExpression(varName);
		var.setRefExpression(new Column(Arrays.asList(oppositeEndType, oppositeEndType.concat("_id"))));
		plainSelect.addVar(var);

		return plainSelect;
	}

	@Override
	public void visit(StringLiteralExp stringLiteralExp) {
		PlainSelect plainSelect = map(stringLiteralExp);

		addComment(stringLiteralExp, plainSelect);

		this.select = new Select();
		this.select.setSelectBody(plainSelect);
	}

	@Override
	public void visit(BooleanLiteralExp booleanLiteralExp) {
		PlainSelect plainSelect = map(booleanLiteralExp);

		addComment(booleanLiteralExp, plainSelect);

		this.select = new Select();
		this.select.setSelectBody(plainSelect);
	}

	@Override
	public void visit(IntegerLiteralExp integerLiteralExp) {
		PlainSelect plainSelect = map(integerLiteralExp);

		addComment(integerLiteralExp, plainSelect);

		this.select = new Select();
		this.select.setSelectBody(plainSelect);
	}

	@Override
	public void visit(VariableExp variableExp) {
		PlainSelect plainSelect = map(variableExp);

		addComment(variableExp, plainSelect);

		this.select = new Select();
		this.select.setSelectBody(plainSelect);
	}

	@Override
	public void visit(com.vgu.se.jocl.expressions.Expression exp) {
		// TODO Auto-generated method stub

	}

	private PlainSelect map(BooleanLiteralExp exp) {
		PlainSelect plainSelect = new PlainSelect();
		plainSelect.createTrueValColumn();

		ResSelectExpression res = new ResSelectExpression(new Column(exp.getValue().toString()));
		plainSelect.setRes(res);

		return plainSelect;
	}

	private PlainSelect map(IntegerLiteralExp exp) {
		PlainSelect plainSelect = new PlainSelect();
		plainSelect.createTrueValColumn();

		ResSelectExpression res = new ResSelectExpression(new LongValue(exp.getValue().toString()));
		plainSelect.setRes(res);

		return plainSelect;
	}

	private PlainSelect map(StringLiteralExp exp) {
		PlainSelect plainSelect = new PlainSelect();
		plainSelect.createTrueValColumn();

		ResSelectExpression res = new ResSelectExpression(new StringValue(exp.getValue().toString()));
		plainSelect.setRes(res);

		return plainSelect;
	}

	private PlainSelect map(VariableExp exp) {
		PlainSelect plainSelect = new PlainSelect();

		String tmpDmnAlias = "TEMP_dmn";
		String varName = exp.getVariable().getName();

		com.vgu.se.jocl.expressions.Expression sourceExp = exp.getVariable().getSource();

		if (sourceExp == null) {
			plainSelect.createTrueValColumn();

			ResSelectExpression res = new ResSelectExpression(new Column(varName));
			plainSelect.setRes(res);

			VarSelectExpression var = new VarSelectExpression(varName);
			var.setRefExpression(new Column(varName));
			plainSelect.addVar(var);

			return plainSelect;
		}

		sourceExp.accept(this);
		Select source = this.getSelect();
		SubSelect tmpSource = new SubSelect(source.getSelectBody(), tmpDmnAlias);

		ValSelectExpression val = new ValSelectExpression(new Column(Arrays.asList(tmpDmnAlias, "val")));
		plainSelect.setVal(val);

		ResSelectExpression res = new ResSelectExpression(new Column(Arrays.asList(tmpDmnAlias, "res")));
		plainSelect.setRes(res);

		VarSelectExpression v = new VarSelectExpression(varName);
		v.setRefExpression(new Column(Arrays.asList(tmpDmnAlias, "res")));
		plainSelect.addVar(v);

		List<Variable> sVarsSrcV = VariableUtils.SVars(sourceExp);
		org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsSrcV, plainSelect, tmpDmnAlias);

		plainSelect.setFromItem(tmpSource);

		return plainSelect;
	}

	private PlainSelect map(PropertyCallExp exp) {
		boolean isSourceVarExp = exp.getNavigationSource() instanceof VariableExp;
		boolean isSourceAssociationExp = exp.getNavigationSource() instanceof AssociationClassCallExp;

		if (!(isSourceVarExp || isSourceAssociationExp)) {
			throw new MappingException(
					String.format("PropertyCallExp %s's source is neither a VariableExp or an AssociationClassCallExp",
							exp.getOclStr()));
		}

		PlainSelect plainSelect = new PlainSelect();

		// Preparation
		String tmpObjAlias = "TEMP_obj";

//        VariableExp varExp = (VariableExp) exp.getNavigationSource();
		com.vgu.se.jocl.expressions.Expression srcExp = exp.getNavigationSource();

		String srcType = srcExp.getType().getReferredType();
		srcType.replaceAll("Col\\((\\w+)\\)", srcType);

		srcExp.accept(this);
		Select vMap = this.getSelect();
		SubSelect tmpObj = new SubSelect(vMap.getSelectBody(), tmpObjAlias);

		// .END Preparation

		ValSelectExpression val = new ValSelectExpression(new Column(Arrays.asList(tmpObjAlias, "val")));
		plainSelect.setVal(val);

		ResSelectExpression res = new ResSelectExpression(
				new Column(Arrays.asList(srcType, exp.getReferredProperty())));
		plainSelect.setRes(res);

		plainSelect.setFromItem(tmpObj);

		List<Variable> sVarsV = VariableUtils.SVars(srcExp);
		org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsV, plainSelect, tmpObjAlias);

		Join leftJ = new Join();
		leftJ.setLeft(true);
		leftJ.setRightItem(new Table(srcType));
		plainSelect.setJoins(Arrays.asList(leftJ));

		String varName = VariableUtils.FVars(srcExp).get(0).getName();

		Expression onExpRefCol = null;
		if (isSourceVarExp) {
			onExpRefCol = new Column(Arrays.asList(tmpObjAlias, "ref_".concat(varName)));
		} else if (isSourceAssociationExp) {
			onExpRefCol = new Column(Arrays.asList(tmpObjAlias, "res"));
		}

		BinaryExpression refVEqId = buildBinExp("=", onExpRefCol,
				new Column(Arrays.asList(srcType, srcType.concat("_id"))));

		BinaryExpression valEq1 = buildBinExp("=", new Column(Arrays.asList(tmpObjAlias, "val")), new LongValue(1L));

		BinaryExpression onExp = buildBinExp("and", refVEqId, valEq1);
		leftJ.addOnExpression(onExp);

		return plainSelect;
	}

	private PlainSelect map(AssociationClassCallExp exp) {

//        if (!(exp.getNavigationSource() instanceof VariableExp)) {
//            throw new MappingException("Cannot parse non Variable expression");
//        } else if (exp.getNavigationSource() instanceof AssociationClassCallExp) {
//            return mapShortcut((AssociationClassCallExp) exp.getNavigationSource());
//        }

		PlainSelect plainSelect;

		if (exp instanceof M2MAssociationClassCallExp) {
			plainSelect = mapM2M(exp);
		} else if (exp instanceof M2OAssociationClassCallExp) {
			plainSelect = mapM2O(exp);
		} else {
			plainSelect = mapO2O(exp);
		}

		return plainSelect;
	}

	private PlainSelect mapO2O(AssociationClassCallExp exp) {

		PlainSelect plainSelect = new PlainSelect();

		String tmpObjAlias = "TEMP_OBJ";

		String referredEndType = exp.getReferredAssociationEndType().getReferredType();
		String referredEndIdColumn = String.format("%1$s_id", referredEndType);
		String referredEndName = exp.getReferredAssociationEnd();

		CaseExpression caseVal = new CaseExpression();
		IsNullExpression isOpposNull = new IsNullExpression();
		isOpposNull.setLeftExpression(new Column(Arrays.asList(referredEndType, referredEndIdColumn)));
		caseVal.setSwitchExpression(isOpposNull);
		WhenClause whenClause = new WhenClause();
		whenClause.setWhenExpression(new LongValue(1L));
		whenClause.setThenExpression(new LongValue(0L));
		caseVal.setWhenClauses(Arrays.asList(whenClause));
		caseVal.setElseExpression(new LongValue(1L));
		ValSelectExpression val = new ValSelectExpression(caseVal);
		plainSelect.setVal(val);

		ResSelectExpression res = new ResSelectExpression(new Column(Arrays.asList(referredEndType, referredEndName)));
		plainSelect.setRes(res);

		CaseExpression caseType = new CaseExpression();
		caseType.setSwitchExpression(isOpposNull);
		WhenClause typeWhenClause = new WhenClause();
		typeWhenClause.setWhenExpression(new LongValue(1L));
		typeWhenClause.setThenExpression(new StringValue("EmptyCol"));
		caseType.setWhenClauses(Arrays.asList(typeWhenClause));
		caseType.setElseExpression(new StringValue(referredEndType));

		com.vgu.se.jocl.expressions.Expression sourceExp = exp.getNavigationSource();

		List<Variable> sVarsV = VariableUtils.SVars(sourceExp);

		org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsV, plainSelect, tmpObjAlias);

		sourceExp.accept(this);
		Select vMap = this.getSelect();
		SubSelect tmpObj = new SubSelect(vMap.getSelectBody(), tmpObjAlias);

		plainSelect.setFromItem(tmpObj);

		Join leftJ = new Join();
		leftJ.setLeft(true);
		leftJ.setRightItem(new Table(referredEndType));

		VariableExp varExp = (VariableExp) exp.getNavigationSource();
		BinaryExpression refVEqOppos = buildBinExp("=",
				new Column(Arrays.asList(tmpObjAlias, "ref_".concat(varExp.getVariable().getName()))),
				new Column(Arrays.asList(referredEndType, referredEndIdColumn)));

		BinaryExpression valEq1 = buildBinExp("=", new Column(Arrays.asList(tmpObjAlias, "val")), new LongValue(1L));

		BinaryExpression onExp = buildBinExp("and", refVEqOppos, valEq1);
		leftJ.addOnExpression(onExp);

		plainSelect.setJoins(Arrays.asList(leftJ));

		return plainSelect;
	}

	private PlainSelect mapM2O(AssociationClassCallExp exp) {

		PlainSelect plainSelect = new PlainSelect();

		String tmpObjAlias = "TEMP_OBJ";
		String referredEndType = exp.getReferredAssociationEndType().getReferredType();
		@SuppressWarnings("unused")
		String referredEndIdColumn = String.format("%1$s_id", referredEndType);
		String referredEndName = exp.getReferredAssociationEnd();
		String oppositeEndType = exp.getOppositeAssociationEndType().getReferredType();
		String oppositeEndName = exp.getOppositeAssociationEnd();
		String oppositeEndIdColumn = String.format("%1$s_id", oppositeEndType);

		if (((M2OAssociationClassCallExp) exp).isOneEndAssociationCall()) {

			CaseExpression caseVal = new CaseExpression();
			IsNullExpression isOpposNull = new IsNullExpression();
			isOpposNull.setLeftExpression(new Column(Arrays.asList(oppositeEndType, oppositeEndIdColumn)));
			caseVal.setSwitchExpression(isOpposNull);
			WhenClause whenClause = new WhenClause();
			whenClause.setWhenExpression(new LongValue(1L));
			whenClause.setThenExpression(new LongValue(0L));
			caseVal.setWhenClauses(Arrays.asList(whenClause));
			caseVal.setElseExpression(new LongValue(1L));
			ValSelectExpression val = new ValSelectExpression(caseVal);
			plainSelect.setVal(val);

//            ResSelectExpression res = new ResSelectExpression(
//                new Column(Arrays.asList(referredEndType, referredEndName)));
			// JP's testing
			ResSelectExpression res = new ResSelectExpression(
					new Column(Arrays.asList(oppositeEndType, referredEndName)));
			plainSelect.setRes(res);

			CaseExpression caseType = new CaseExpression();
			caseType.setSwitchExpression(isOpposNull);
			WhenClause typeWhenClause = new WhenClause();
			typeWhenClause.setWhenExpression(new LongValue(1L));
			typeWhenClause.setThenExpression(new StringValue("EmptyCol"));
			caseType.setWhenClauses(Arrays.asList(typeWhenClause));
			caseType.setElseExpression(new StringValue(referredEndType));

			com.vgu.se.jocl.expressions.Expression sourceExp = exp.getNavigationSource();

			List<Variable> sVarsV = VariableUtils.SVars(sourceExp);

			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsV, plainSelect, tmpObjAlias);

			sourceExp.accept(this);
			Select vMap = this.getSelect();
			SubSelect tmpObj = new SubSelect(vMap.getSelectBody(), tmpObjAlias);

			plainSelect.setFromItem(tmpObj);

			Join leftJ = new Join();
			leftJ.setLeft(true);
			leftJ.setRightItem(new Table(oppositeEndType));

			boolean isVarExpSrc = exp.getNavigationSource() instanceof VariableExp;
			boolean isAssociationExpSrc = exp.getNavigationSource() instanceof AssociationClassCallExp;

			Expression refCol = null;
			if (isVarExpSrc) {
				refCol = new Column(Arrays.asList(tmpObjAlias,
						"ref_".concat(((VariableExp) exp.getNavigationSource()).getVariable().getName())));
			} else if (isAssociationExpSrc) {
				refCol = new Column(Arrays.asList(tmpObjAlias, "res"));
			}

			BinaryExpression refVEqOppos = buildBinExp("=", refCol,
					new Column(Arrays.asList(oppositeEndType, oppositeEndIdColumn)));

			BinaryExpression valEq1 = buildBinExp("=", new Column(Arrays.asList(tmpObjAlias, "val")),
					new LongValue(1L));

			BinaryExpression onExp = buildBinExp("and", refVEqOppos, valEq1);
			leftJ.addOnExpression(onExp);

			plainSelect.setJoins(Arrays.asList(leftJ));

		} else {
			CaseExpression caseVal = new CaseExpression();

			IsNullExpression isOpposNull = new IsNullExpression();
			isOpposNull.setLeftExpression(new Column(Arrays.asList(oppositeEndType, oppositeEndName)));
			caseVal.setSwitchExpression(isOpposNull);
			WhenClause whenClause = new WhenClause();
			whenClause.setWhenExpression(new LongValue(1L));
			whenClause.setThenExpression(new LongValue(0L));
			caseVal.setWhenClauses(Arrays.asList(whenClause));
			caseVal.setElseExpression(new LongValue(1L));
			ValSelectExpression val = new ValSelectExpression(caseVal);
			plainSelect.setVal(val);

			ResSelectExpression res = new ResSelectExpression(
					new Column(Arrays.asList(oppositeEndType, oppositeEndIdColumn)));
			plainSelect.setRes(res);

			CaseExpression caseType = new CaseExpression();
			caseType.setSwitchExpression(isOpposNull);
			WhenClause typeWhenClause = new WhenClause();
			typeWhenClause.setWhenExpression(new LongValue(1L));
			typeWhenClause.setThenExpression(new StringValue("EmptyCol"));
			caseType.setWhenClauses(Arrays.asList(typeWhenClause));
			caseType.setElseExpression(new StringValue(referredEndType));

			com.vgu.se.jocl.expressions.Expression sourceExp = exp.getNavigationSource();

			List<Variable> sVarsV = VariableUtils.SVars(sourceExp);

			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsV, plainSelect, tmpObjAlias);

			sourceExp.accept(this);
			Select vMap = this.getSelect();
			SubSelect tmpObj = new SubSelect(vMap.getSelectBody(), tmpObjAlias);

			plainSelect.setFromItem(tmpObj);

			Join leftJ = new Join();
			leftJ.setLeft(true);
			leftJ.setRightItem(new Table(oppositeEndType));

			VariableExp varExp = (VariableExp) exp.getNavigationSource();
			BinaryExpression refVEqOppos = buildBinExp("=",
					new Column(Arrays.asList(tmpObjAlias, "ref_".concat(varExp.getVariable().getName()))),
					new Column(Arrays.asList(oppositeEndType, oppositeEndName)));

			BinaryExpression valEq1 = buildBinExp("=", new Column(Arrays.asList(tmpObjAlias, "val")),
					new LongValue(1L));

			BinaryExpression onExp = buildBinExp("and", refVEqOppos, valEq1);
			leftJ.addOnExpression(onExp);

			plainSelect.setJoins(Arrays.asList(leftJ));

		}
		return plainSelect;
	}

	private PlainSelect mapM2M(AssociationClassCallExp exp) {

		PlainSelect plainSelect = new PlainSelect();

		String tmpObjAlias = "TEMP_obj";

		String associationTableName = exp.getAssociation();
		String oppositeEndName = exp.getOppositeAssociationEnd();
		String referredEndName = exp.getReferredAssociationEnd();
		String referredEndType = exp.getReferredAssociationEndType().getReferredType();

		CaseExpression caseVal = new CaseExpression();
		IsNullExpression isOpposNull = new IsNullExpression();
		isOpposNull.setLeftExpression(new Column(Arrays.asList(associationTableName, oppositeEndName)));
		caseVal.setSwitchExpression(isOpposNull);
		WhenClause whenClause = new WhenClause();
		whenClause.setWhenExpression(new LongValue(1L));
		whenClause.setThenExpression(new LongValue(0L));
		caseVal.setWhenClauses(Arrays.asList(whenClause));
		caseVal.setElseExpression(new LongValue(1L));
		ValSelectExpression val = new ValSelectExpression(caseVal);
		plainSelect.setVal(val);

		ResSelectExpression res = new ResSelectExpression(
				new Column(Arrays.asList(associationTableName, referredEndName)));
		plainSelect.setRes(res);

		CaseExpression caseType = new CaseExpression();
		caseType.setSwitchExpression(isOpposNull);
		WhenClause typeWhenClause = new WhenClause();
		typeWhenClause.setWhenExpression(new LongValue(1L));
		typeWhenClause.setThenExpression(new StringValue("EmptyCol"));
		caseType.setWhenClauses(Arrays.asList(typeWhenClause));
		String resType = referredEndType.replaceAll("Col\\((\\w+)\\)", "$1");
		caseType.setElseExpression(new StringValue(resType));

		com.vgu.se.jocl.expressions.Expression sourceExp = exp.getNavigationSource();

		List<Variable> sVarsV = VariableUtils.SVars(sourceExp);

		org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsV, plainSelect, tmpObjAlias);

		sourceExp.accept(this);
		Select vMap = this.getSelect();
		SubSelect tmpObj = new SubSelect(vMap.getSelectBody(), tmpObjAlias);

		plainSelect.setFromItem(tmpObj);

		Join leftJ = new Join();
		leftJ.setLeft(true);
		leftJ.setRightItem(new Table(associationTableName));

		VariableExp varExp = (VariableExp) exp.getNavigationSource();
		BinaryExpression refVEqOppos = buildBinExp("=",
				new Column(Arrays.asList(tmpObjAlias, "ref_".concat(varExp.getVariable().getName()))),
				new Column(Arrays.asList(associationTableName, oppositeEndName)));

		BinaryExpression valEq1 = buildBinExp("=", new Column(Arrays.asList(tmpObjAlias, "val")), new LongValue(1L));

		BinaryExpression onExp = buildBinExp("and", refVEqOppos, valEq1);
		leftJ.addOnExpression(onExp);

		plainSelect.setJoins(Arrays.asList(leftJ));

		return plainSelect;
	}

	private PlainSelect mapAllInstances(OperationCallExp exp) {
		PlainSelect plainSelect = new PlainSelect();
		plainSelect.createTrueValColumn();

		String tableName = exp.getSource().getType().getReferredType();

		ResSelectExpression res = new ResSelectExpression(new Column(tableName.concat("_id")));
		plainSelect.setRes(res);

		plainSelect.setFromItem(new Table(tableName));

		return plainSelect;
	}

	private PlainSelect mapOclIsUndefined(OperationCallExp exp) {
		PlainSelect plainSelect = new PlainSelect();

		String tmpSourceAlias = "TEMP_src";

		com.vgu.se.jocl.expressions.Expression srcExp = exp.getSource();
		srcExp.accept(this);

		Select sourceMap = this.getSelect();
		SubSelect tmpSource = new SubSelect(sourceMap.getSelectBody(), tmpSourceAlias);

		List<Variable> sVarsSrc = VariableUtils.SVars(srcExp);

		ValSelectExpression val = new ValSelectExpression(new Column(Arrays.asList(tmpSourceAlias, "val")));
		plainSelect.setVal(val);

		CaseExpression resCase = new CaseExpression();

		BinaryExpression caseSwitch = buildBinExp("=", new Column(Arrays.asList(tmpSourceAlias, "val")),
				new LongValue(0L));
		resCase.setSwitchExpression(caseSwitch);

		WhenClause when = new WhenClause();
		when.setWhenExpression(new LongValue(1L));
		when.setThenExpression(new NullValue());

		resCase.setWhenClauses(Arrays.asList(when));

		IsNullExpression isNull = new IsNullExpression();
		isNull.setLeftExpression(new Column(Arrays.asList(tmpSourceAlias, "res")));

		resCase.setElseExpression(isNull);

		ResSelectExpression res = new ResSelectExpression(resCase);
		plainSelect.setRes(res);

		org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsSrc, plainSelect, tmpSourceAlias);

		plainSelect.setFromItem(tmpSource);

		return plainSelect;
	}

	private PlainSelect mapOclIsKindOf(OperationCallExp exp) {
		PlainSelect plainSelect = new PlainSelect();

		// Begin Preparation
		com.vgu.se.jocl.expressions.Expression src = exp.getSource();
		src.accept(this);

		String tmpSrcAlias = "TMP_SOURCE";
		Select srcMap = this.getSelect();
		SubSelect tmpSrc = new SubSelect(srcMap.getSelectBody(), tmpSrcAlias);

		com.vgu.se.jocl.expressions.Expression argExp = exp.getArguments().get(0);

		String typeToCheck = "";
		if (argExp instanceof TypeExp) {
			typeToCheck = ((TypeExp) argExp).getType().getReferredType();
		} else {
			typeToCheck = ((VariableExp) exp.getArguments().get(0)).getType().getReferredType();
		}

//        boolean isKindOf = DmUtils.isSuperClassOf(this.dataModel, typeToCheck,
//            src.getType().getReferredType());

		List<Variable> sVarsSrc = VariableUtils.SVars(src);
		// . End Preparation

		ValSelectExpression val = new ValSelectExpression(new Column(Arrays.asList(tmpSrcAlias, "val")));
		plainSelect.setVal(val);

		CaseExpression resCase = new CaseExpression();

		BinaryExpression valEq0 = buildBinExp("=", new Column(Arrays.asList(tmpSrcAlias, "val")), new LongValue(0L));
		resCase.setSwitchExpression(valEq0);

		WhenClause whenClause = new WhenClause();
		whenClause.setWhenExpression(new LongValue(1L));
		whenClause.setThenExpression(new NullValue());
		resCase.setWhenClauses(Arrays.asList(whenClause));

//        resCase.setElseExpression(new LongValue(isKindOf ? 1L : 0L));
		resCase.setElseExpression(new LongValue(0L));

		ResSelectExpression res = new ResSelectExpression(resCase);
		plainSelect.setRes(res);

		plainSelect.setFromItem(tmpSrc);

		org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsSrc, plainSelect, tmpSrcAlias);

		return plainSelect;
	}

	private PlainSelect mapOclIsTypeOf(OperationCallExp exp) {
		PlainSelect plainSelect = new PlainSelect();

		// Begin Preparation
		com.vgu.se.jocl.expressions.Expression src = exp.getSource();
		src.accept(this);

		String tmpSrcAlias = "TMP_SOURCE";
		Select srcMap = this.getSelect();
		SubSelect tmpSrc = new SubSelect(srcMap.getSelectBody(), tmpSrcAlias);

		com.vgu.se.jocl.expressions.Expression argExp = exp.getArguments().get(0);

		String typeToCheck = "";
		if (argExp instanceof TypeExp) {
			typeToCheck = ((TypeExp) argExp).getType().getReferredType();
		} else {
			typeToCheck = ((VariableExp) exp.getArguments().get(0)).getType().getReferredType();
		}

		List<Variable> sVarsSrc = VariableUtils.SVars(src);
		// . End Preparation

		ValSelectExpression val = new ValSelectExpression(new Column(Arrays.asList(tmpSrcAlias, "val")));
		plainSelect.setVal(val);

		CaseExpression resCase = new CaseExpression();

		BinaryExpression valEq0 = buildBinExp("=", new Column(Arrays.asList(tmpSrcAlias, "val")), new LongValue(0L));
		resCase.setSwitchExpression(valEq0);

		WhenClause whenClause = new WhenClause();
		whenClause.setWhenExpression(new LongValue(1L));
		whenClause.setThenExpression(new NullValue());
		resCase.setWhenClauses(Arrays.asList(whenClause));

		BinaryExpression srcTypeEqTypeToCheck = buildBinExp("=", new Column(Arrays.asList(tmpSrcAlias, "type")),
				new StringValue(typeToCheck));
		resCase.setElseExpression(srcTypeEqTypeToCheck);

		ResSelectExpression res = new ResSelectExpression(resCase);
		plainSelect.setRes(res);

		org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsSrc, plainSelect, tmpSrcAlias);

		plainSelect.setFromItem(tmpSrc);

		return plainSelect;
	}

	private PlainSelect mapOclAsType(OperationCallExp exp) {
		PlainSelect plainSelect = new PlainSelect();

		// Begin Preparation
		com.vgu.se.jocl.expressions.Expression src = exp.getSource();
		src.accept(this);

		String tmpSrcAlias = "TMP_SOURCE";
		Select srcMap = this.getSelect();
		SubSelect tmpSrc = new SubSelect(srcMap.getSelectBody(), tmpSrcAlias);

		String typeCastedTo = exp.getType().getReferredType();
		boolean isTypeUnchangedOrPrimitive = typeCastedTo.equals("String") || typeCastedTo.equals("Integer")
				|| typeCastedTo.equals("Boolean")
				|| typeCastedTo.equals(((VariableExp) exp.getSource()).getType().getReferredType());

		List<Variable> sVarsSrc = VariableUtils.SVars(src);
		// . End preparation

		ValSelectExpression val = new ValSelectExpression(new Column(Arrays.asList(tmpSrcAlias, "val")));
		plainSelect.setVal(val);

		ResSelectExpression res = new ResSelectExpression(new Column(Arrays.asList(tmpSrcAlias, "res")));
		plainSelect.setRes(res);

		CaseExpression typeCase = new CaseExpression();

		BinaryExpression valEq0 = buildBinExp("=", new Column(Arrays.asList(tmpSrcAlias, "val")), new LongValue(0L));
		typeCase.setSwitchExpression(valEq0);

		WhenClause whenClause = new WhenClause();
		whenClause.setWhenExpression(new LongValue(1L));
		whenClause.setThenExpression(new NullValue());
		typeCase.setWhenClauses(Arrays.asList(whenClause));

		typeCase.setElseExpression(new StringValue(typeCastedTo));

		org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsSrc, plainSelect, tmpSrcAlias);

		plainSelect.setFromItem(tmpSrc);

		if (!isTypeUnchangedOrPrimitive) {
			Join join = new Join();
			join.setRightItem(new Table(typeCastedTo));

			BinaryExpression resEqId = buildBinExp("=", new Column(Arrays.asList(tmpSrcAlias, "res")),
					new Column(Arrays.asList(typeCastedTo, typeCastedTo.concat("_id"))));
			join.addOnExpression(resEqId);

			plainSelect.setJoins(Arrays.asList(join));
		}

		return plainSelect;
	}

	private PlainSelect mapNot(OperationCallExp exp) {
		PlainSelect plainSelect = new PlainSelect();

		com.vgu.se.jocl.expressions.Expression tmpExp = exp.getArguments().get(0);

		String tmpAlias = "TEMP";
		tmpExp.accept(this);

		Select tmpMap = this.getSelect();
		SubSelect tmp = new SubSelect(tmpMap.getSelectBody(), tmpAlias);

		List<Variable> sVarsTmp = VariableUtils.SVars(tmpExp);

		NotExpression notExp = new NotExpression(new Column(Arrays.asList(tmpAlias, "res")));
		ResSelectExpression res = new ResSelectExpression(notExp);
		plainSelect.setRes(res);

		ValSelectExpression val = new ValSelectExpression(new Column(Arrays.asList(tmpAlias, "val")));
		plainSelect.setVal(val);

		org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsTmp, plainSelect, tmpAlias);

		plainSelect.setFromItem(tmp);

		return plainSelect;
	}

	private PlainSelect mapBinary(OperationCallExp exp) {
		PlainSelect plainSelect = new PlainSelect();

		com.vgu.se.jocl.expressions.Expression leftExp = exp.getSource();
		com.vgu.se.jocl.expressions.Expression rightExp = exp.getArguments().get(0);

		String tmpLeftAlias = "TEMP_left";
		String tmpRightAlias = "TEMP_right";

		List<Variable> fVarsLeft = VariableUtils.FVars(leftExp);
		List<Variable> fVarsRight = VariableUtils.FVars(rightExp);

		boolean isEmptyFvLeft = fVarsLeft.isEmpty();
		boolean isEmptyFvRight = fVarsRight.isEmpty();

		BinaryExpression binExp = buildBinExp(exp.getReferredOperation().getName(),
				new Column(Arrays.asList(tmpLeftAlias, "res")), new Column(Arrays.asList(tmpRightAlias, "res")));

		ResSelectExpression res = new ResSelectExpression(binExp);
		plainSelect.setRes(res);

		leftExp.accept(this);
		Select leftMap = this.getSelect();
		SubSelect tmpLeft = new SubSelect(leftMap.getSelectBody(), tmpLeftAlias);

		rightExp.accept(this);
		Select rightMap = this.getSelect();
		SubSelect tmpRight = new SubSelect(rightMap.getSelectBody(), tmpRightAlias);

		// Case 1
		if (isEmptyFvLeft && isEmptyFvRight) {
			plainSelect.createTrueValColumn();

			plainSelect.setFromItem(tmpLeft);

			Join join = new Join();
			join.setRightItem(tmpRight);

			plainSelect.setJoins(Arrays.asList(join));

			return plainSelect;
		}

		// Preparation for Case 2, 3, 4
		List<Variable> sVarsLeft = VariableUtils.SVars(leftExp);
		List<Variable> sVarsRight = VariableUtils.SVars(rightExp);

		boolean isSubsetSvLeftOfSvRight = sVarsRight.containsAll(sVarsLeft);
		boolean isSubsetSvRightOfSvLeft = sVarsLeft.containsAll(sVarsRight);

		List<Variable> sVarsIntxn = new ArrayList<Variable>(sVarsLeft);
		sVarsIntxn.retainAll(sVarsRight);

		boolean isEmptySvIntxn = sVarsIntxn.isEmpty();

		CaseExpression caseVal = new CaseExpression();

		BinaryExpression leftValEq0 = buildBinExp("=", new Column(Arrays.asList(tmpLeftAlias, "val")),
				new LongValue(0L));

		BinaryExpression rightValEq0 = buildBinExp("=", new Column(Arrays.asList(tmpRightAlias, "val")),
				new LongValue(0L));

		OrExpression orExp = new OrExpression(leftValEq0, rightValEq0);
		caseVal.setSwitchExpression(orExp);

		WhenClause whenClause = new WhenClause();
		whenClause.setWhenExpression(new LongValue(1L));
		whenClause.setThenExpression(new LongValue(0L));
		caseVal.setWhenClauses(Arrays.asList(whenClause));

		caseVal.setElseExpression(new LongValue(1L));

		ValSelectExpression val = new ValSelectExpression(caseVal);
		plainSelect.setVal(val);

		Join join = new Join();

		if (!isEmptySvIntxn) {
			BinaryExpression onExp = null;
			join.setLeft(true);
			for (Variable v : sVarsIntxn) {
				VarSelectExpression var = new VarSelectExpression(v.getName());
				String refName = var.getRef().getAlias().getName();

				BinaryExpression onExp_1 = buildBinExp("=", new Column(Arrays.asList(tmpLeftAlias, refName)),
						new Column(Arrays.asList(tmpRightAlias, refName)));

				if (onExp == null) {
					onExp = onExp_1;
				} else {
					onExp = buildBinExp("and", onExp, onExp_1);
				}
			}

			join.addOnExpression(onExp);
		}

		// Case 2
		if (!isEmptyFvLeft && isSubsetSvRightOfSvLeft) {
			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsLeft, plainSelect, tmpLeftAlias);

			plainSelect.setFromItem(tmpLeft);
			join.setRightItem(tmpRight);
		}
		// case 3
		else if (!isEmptyFvRight && isSubsetSvLeftOfSvRight) {
			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsRight, plainSelect, tmpRightAlias);

			plainSelect.setFromItem(tmpRight);
			join.setRightItem(tmpLeft);
		}
		// case 4
		else if (!isEmptyFvLeft && !isEmptyFvLeft && !isSubsetSvLeftOfSvRight && !isSubsetSvRightOfSvLeft) {

			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsLeft, plainSelect, tmpLeftAlias);
			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsRight, plainSelect, tmpRightAlias);

			plainSelect.setFromItem(tmpLeft);
			join.setRightItem(tmpRight);
		}

		plainSelect.setJoins(Arrays.asList(join));

		return plainSelect;
	}

	private PlainSelect mapImplies(OperationCallExp exp) {
		PlainSelect plainSelect = new PlainSelect();

		com.vgu.se.jocl.expressions.Expression leftExp = exp.getSource();
		com.vgu.se.jocl.expressions.Expression rightExp = exp.getArguments().get(0);

		String tmpLeftAlias = "TEMP_LEFT";
		String tmpRightAlias = "TEMP_RIGHT";

		leftExp.accept(this);
		Select leftMap = this.getSelect();
		SubSelect tmpLeft = new SubSelect(leftMap.getSelectBody(), tmpLeftAlias);

		rightExp.accept(this);
		Select rightMap = this.getSelect();
		SubSelect tmpRight = new SubSelect(rightMap.getSelectBody(), tmpRightAlias);

		List<Variable> fVarsLeft = VariableUtils.FVars(leftExp);
		List<Variable> fVarsRight = VariableUtils.FVars(rightExp);

		boolean isEmptyFvLeft = fVarsLeft.isEmpty();
		boolean isEmptyFvRight = fVarsRight.isEmpty();

		CaseExpression caseImplies = new CaseExpression();

		WhenClause caseWhen = new WhenClause();
		caseWhen.setWhenExpression(new Column(Arrays.asList(tmpLeftAlias, "res")));
		caseWhen.setThenExpression(new Column(Arrays.asList(tmpRightAlias, "res")));
		caseImplies.setWhenClauses(Arrays.asList(caseWhen));

		caseImplies.setElseExpression(new LongValue(1L));

		ResSelectExpression res = new ResSelectExpression(caseImplies);
		plainSelect.setRes(res);

		// Case 1
		if (isEmptyFvLeft && isEmptyFvRight) {
			plainSelect.createTrueValColumn();

			plainSelect.setFromItem(tmpLeft);

			Join join = new Join();
			join.setRightItem(tmpRight);

			plainSelect.setJoins(Arrays.asList(join));

			return plainSelect;
		}

		// Preparation for Case 2, 3, 4
		List<Variable> sVarsLeft = VariableUtils.SVars(leftExp);
		List<Variable> sVarsRight = VariableUtils.SVars(rightExp);

		boolean isSubsetSvLeftOfSvRight = sVarsRight.containsAll(sVarsLeft);
		boolean isSubsetSvRightOfSvLeft = sVarsLeft.containsAll(sVarsRight);

		List<Variable> sVarsIntxn = new ArrayList<Variable>(sVarsLeft);
		sVarsIntxn.retainAll(sVarsRight);

		boolean isEmptySvIntxn = sVarsIntxn.isEmpty();

		CaseExpression caseVal = new CaseExpression();

		BinaryExpression leftValEq0 = buildBinExp("=", new Column(Arrays.asList(tmpLeftAlias, "val")),
				new LongValue(0L));

		BinaryExpression rightValEq0 = buildBinExp("=", new Column(Arrays.asList(tmpRightAlias, "val")),
				new LongValue(0L));

		OrExpression orExp = new OrExpression(leftValEq0, rightValEq0);
		caseVal.setSwitchExpression(orExp);

		WhenClause whenClause = new WhenClause();
		whenClause.setWhenExpression(new LongValue(1L));
		whenClause.setThenExpression(new LongValue(0L));
		caseVal.setWhenClauses(Arrays.asList(whenClause));

		caseVal.setElseExpression(new LongValue(1L));

		ValSelectExpression val = new ValSelectExpression(caseVal);
		plainSelect.setVal(val);

		Join join = new Join();

		if (!isEmptySvIntxn) {
			join.setLeft(true);

			for (Variable v : sVarsIntxn) {
				VarSelectExpression var = new VarSelectExpression(v.getName());
				String refName = var.getRef().getAlias().getName();

				BinaryExpression onExp = buildBinExp("=", new Column(Arrays.asList(tmpLeftAlias, refName)),
						new Column(Arrays.asList(tmpRightAlias, refName)));

				join.addOnExpression(onExp);
			}
		}

		// Case 2
		if (!isEmptyFvLeft && isSubsetSvRightOfSvLeft) {
			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsLeft, plainSelect, tmpLeftAlias);

			plainSelect.setFromItem(tmpLeft);
			join.setRightItem(tmpRight);
		}
		// case 3
		else if (!isEmptyFvRight && isSubsetSvLeftOfSvRight) {
			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsRight, plainSelect, tmpRightAlias);

			plainSelect.setFromItem(tmpRight);
			join.setRightItem(tmpLeft);
		}
		// case 4
		else if (!isEmptyFvLeft && !isEmptyFvLeft && !isSubsetSvLeftOfSvRight && !isSubsetSvRightOfSvLeft) {

			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsLeft, plainSelect, tmpLeftAlias);
			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsRight, plainSelect, tmpRightAlias);

			plainSelect.setFromItem(tmpLeft);
			join.setRightItem(tmpRight);
		}

		plainSelect.setJoins(Arrays.asList(join));

		return plainSelect;
	}

	private PlainSelect mapSize(IteratorExp exp) {
		PlainSelect plainSelect = new PlainSelect();
		plainSelect.createTrueValColumn();

		exp.getSource().accept(this);
		Select source = this.getSelect();

		String tmpSourceAlias = "TEMP_src";
		SubSelect tmpSource = new SubSelect(source.getSelectBody(), tmpSourceAlias);
		plainSelect.setFromItem(tmpSource);

		Function count = new Function();
		count.setName("COUNT");
		count.setAllColumns(true);

		if (VariableUtils.FVars(exp).isEmpty()) {
			ResSelectExpression res = new ResSelectExpression(count);
			plainSelect.setRes(res);

			return plainSelect;

		} else {
			BinaryExpression isValZero = buildBinExp("=", new Column(Arrays.asList(tmpSourceAlias, "val")),
					new LongValue(0L));

			WhenClause whenCase = new WhenClause();
			whenCase.setWhenExpression(new LongValue(1L));
			whenCase.setThenExpression(new LongValue(0L));

			CaseExpression caseRes = new CaseExpression();
			caseRes.setSwitchExpression(isValZero);
			caseRes.setWhenClauses(Arrays.asList(whenCase));
			caseRes.setElseExpression(count);

			ResSelectExpression res = new ResSelectExpression(caseRes);
			plainSelect.setRes(res);

			List<Expression> groupByFields = new ArrayList<Expression>();

			List<Variable> sVars = VariableUtils.SVars(exp);
			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVars, plainSelect, tmpSourceAlias);

			for (Variable v : sVars) {
				VarSelectExpression refVar = new VarSelectExpression(v.getName());
				Expression varCol = new Column(Arrays.asList(tmpSourceAlias, refVar.getRef().getAlias().getName()));
				refVar.setRefExpression(varCol);

				groupByFields.add(varCol);
			}

			groupByFields.add(new Column(Arrays.asList(tmpSourceAlias, "val")));

			GroupByElement groupByEl = new GroupByElement();
			groupByEl.addGroupByExpressions(groupByFields);
			plainSelect.setGroupByElement(groupByEl);

			return plainSelect;
		}
	}

	private PlainSelect mapAsSet(IteratorExp exp) {
		PlainSelect plainSelect = new PlainSelect();
		exp.getSource().accept(this);
		Select source = this.getSelect();

		plainSelect = (PlainSelect) source.getSelectBody();

		Distinct distinctRes = new Distinct();
		plainSelect.setDistinct(distinctRes);

		return plainSelect;

	}

	private PlainSelect mapCollect(IteratorExp exp) {
		PlainSelect plainSelect = new PlainSelect();

		String tmpBodyAlias = "TEMP_body";

		ValSelectExpression val = new ValSelectExpression(new Column(Arrays.asList(tmpBodyAlias, "val")));
		plainSelect.setVal(val);

		ResSelectExpression res = new ResSelectExpression(new Column(Arrays.asList(tmpBodyAlias, "res")));
		plainSelect.setRes(res);

		exp.getBody().accept(this);
		Select body = this.getSelect();
		SubSelect tmpBody = new SubSelect(body.getSelectBody(), tmpBodyAlias);

		List<Variable> fVarsB = VariableUtils.FVars(exp.getBody());
		Variable v = exp.getIterator();
		boolean isVarInFVarsB = fVarsB.contains(v);

		List<Variable> sVarsS = VariableUtils.SVars(exp.getSource());
		List<Variable> sVarsB = VariableUtils.SVars(exp.getBody());
		List<Variable> sVarsIntxn = new ArrayList<Variable>(sVarsB);
		sVarsIntxn.retainAll(sVarsS);

		if (isVarInFVarsB) {
			plainSelect.setFromItem(tmpBody);

		} else {
			String tmpSourceAlias = "TEMP_src";

			exp.getSource().accept(this);
			Select source = this.getSelect();
			SubSelect tmpSource = new SubSelect(source.getSelectBody(), tmpSourceAlias);

			plainSelect.setFromItem(tmpSource);

			Join join = new Join();
			join.setRightItem(tmpBody);

			BinaryExpression onExp = null;

			for (Variable var : sVarsIntxn) {
				BinaryExpression bodyEqSrc = buildBinExp("=",
						new Column(Arrays.asList(tmpSourceAlias, "ref_".concat(var.getName()))),
						new Column(Arrays.asList(tmpBodyAlias, "ref_".concat(var.getName()))));

				if (onExp == null) {
					onExp = bodyEqSrc;
				} else {
					onExp = buildBinExp("and", onExp, bodyEqSrc);
				}
			}

			join.addOnExpression(onExp);
			plainSelect.setJoins(Arrays.asList(join));
		}

		List<Variable> fVarsExp = VariableUtils.FVars(exp);
		boolean isEmptyFVarsExp = fVarsExp.isEmpty();

		if (isEmptyFVarsExp) {
			// Nothing to do

		} else {
			List<Variable> sVarsRemained = new ArrayList<Variable>(sVarsB);
			sVarsRemained.removeAll(sVarsS);
			sVarsRemained.remove(v);

			if (isVarInFVarsB) {
				org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsS, plainSelect, tmpBodyAlias);

				org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsRemained, plainSelect, tmpBodyAlias);

				plainSelect.setFromItem(tmpBody);

			} else {
				// Already handled above
			}
		}

		return plainSelect;
	}

	private PlainSelect mapIsEmpty(IteratorExp exp) {
		PlainSelect plainSelect = new PlainSelect();

		// Begin preparation

		String tmpSrcAlias = "TEMP_SRC";
		com.vgu.se.jocl.expressions.Expression srcExp = exp.getSource();
		srcExp.accept(this);

		Select srcMap = this.getSelect();
		SubSelect tmpSrc = new SubSelect(srcMap.getSelectBody(), tmpSrcAlias);

		List<Variable> fvExp = VariableUtils.FVars(exp);
		boolean isFvExpEmpty = fvExp.isEmpty();

		// . End preparation

		plainSelect.createTrueValColumn();

		if (isFvExpEmpty) {
			Function count = new Function();
			count.setName("COUNT");
			count.setAllColumns(true);

			BinaryExpression resExp = buildBinExp("=", count, new LongValue(0L));

			ResSelectExpression res = new ResSelectExpression(resExp);
			plainSelect.setRes(res);

			plainSelect.setFromItem(tmpSrc);

		} else {
			List<Variable> svSrc = VariableUtils.SVars(srcExp);

			CaseExpression resCase = new CaseExpression();

			BinaryExpression srcValEq0 = buildBinExp("=", new Column(Arrays.asList(tmpSrcAlias, "val")),
					new LongValue(0L));
			resCase.setSwitchExpression(srcValEq0);

			WhenClause whenClause = new WhenClause();
			whenClause.setWhenExpression(new LongValue(1L));
			whenClause.setThenExpression(new LongValue(1L));

			resCase.setWhenClauses(Arrays.asList(whenClause));

			resCase.setElseExpression(new LongValue(0L));

			ResSelectExpression res = new ResSelectExpression(resCase);
			plainSelect.setRes(res);

			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(svSrc, plainSelect, tmpSrcAlias);

			plainSelect.setFromItem(tmpSrc);

			GroupByElement groupBy = new GroupByElement();

			List<Expression> groupByExps = new ArrayList<Expression>();
			for (Variable v : svSrc) {
				groupByExps.add(new Column(Arrays.asList(tmpSrcAlias, "ref_".concat(v.getName()))));
			}
			groupByExps.add(new Column(Arrays.asList(tmpSrcAlias, "val")));

			groupBy.addGroupByExpressions(groupByExps);

			plainSelect.setGroupByElement(groupBy);

		}

		return plainSelect;
	}

	private PlainSelect mapNotEmpty(IteratorExp exp) {
		PlainSelect plainSelect = new PlainSelect();

		// Begin preparation

		String tmpSrcAlias = "TEMP_SRC";
		com.vgu.se.jocl.expressions.Expression srcExp = exp.getSource();
		srcExp.accept(this);

		Select srcMap = this.getSelect();
		SubSelect tmpSrc = new SubSelect(srcMap.getSelectBody(), tmpSrcAlias);

		List<Variable> fvExp = VariableUtils.FVars(exp);
		boolean isFvExpEmpty = fvExp.isEmpty();

		// . End preparation

		plainSelect.createTrueValColumn();

		if (isFvExpEmpty) {
			Function count = new Function();
			count.setName("COUNT");
			count.setAllColumns(true);

			BinaryExpression resExp = buildBinExp(">", count, new LongValue(0L));

			ResSelectExpression res = new ResSelectExpression(resExp);
			plainSelect.setRes(res);

			plainSelect.setFromItem(tmpSrc);

		} else {
			List<Variable> svSrc = VariableUtils.SVars(srcExp);

			CaseExpression resCase = new CaseExpression();

			BinaryExpression srcValEq0 = buildBinExp("=", new Column(Arrays.asList(tmpSrcAlias, "val")),
					new LongValue(0L));
			resCase.setSwitchExpression(srcValEq0);

			WhenClause whenClause = new WhenClause();
			whenClause.setWhenExpression(new LongValue(1L));
			whenClause.setThenExpression(new LongValue(0L));

			resCase.setWhenClauses(Arrays.asList(whenClause));

			resCase.setElseExpression(new LongValue(1L));

			ResSelectExpression res = new ResSelectExpression(resCase);
			plainSelect.setRes(res);

			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(svSrc, plainSelect, tmpSrcAlias);

			plainSelect.setFromItem(tmpSrc);

			GroupByElement groupBy = new GroupByElement();

			List<Expression> groupByExps = new ArrayList<Expression>();
			for (Variable v : svSrc) {
				groupByExps.add(new Column(Arrays.asList(tmpSrcAlias, "ref_".concat(v.getName()))));
			}
			groupByExps.add(new Column(Arrays.asList(tmpSrcAlias, "val")));

			groupBy.addGroupByExpressions(groupByExps);

			plainSelect.setGroupByElement(groupBy);

		}

		return plainSelect;
	}

	private PlainSelect mapIsUnique(IteratorExp exp) {
		PlainSelect plainSelect = new PlainSelect();

		// Begin preparation
		String tmpSrcAlias = "TEMP_SRC";

		com.vgu.se.jocl.expressions.Expression srcExp = exp.getSource();
		srcExp.accept(this);

		Select srcMap = this.getSelect();
		SubSelect tmpSrc = new SubSelect(srcMap.getSelectBody(), tmpSrcAlias);

		List<Variable> fvExp = VariableUtils.FVars(exp);
		boolean isFvExpEmpty = fvExp.isEmpty();

		// . End preparation
		plainSelect.createTrueValColumn();

		Function countRes = new Function();
		countRes.setName("COUNT");
		ExpressionList expLs = new ExpressionList(new Column(Arrays.asList(tmpSrcAlias, "res")));
		countRes.setParameters(expLs);

		Function countDistinctRes = new Function();
		countDistinctRes.setName("COUNT");
		countDistinctRes.setDistinct(true);
		expLs = new ExpressionList(new Column(Arrays.asList(tmpSrcAlias, "res")));
		countDistinctRes.setParameters(expLs);

		BinaryExpression countExp = buildBinExp("=", countRes, countDistinctRes);

		if (isFvExpEmpty) {
			ResSelectExpression res = new ResSelectExpression(countExp);
			plainSelect.setRes(res);

			plainSelect.setFromItem(tmpSrc);

		} else {
			// Begin Preparation
			List<Variable> svSrc = VariableUtils.SVars(srcExp);
			// . End Preparation

			CaseExpression resCase = new CaseExpression();

			BinaryExpression srcValEq0 = buildBinExp("=", new Column(Arrays.asList(tmpSrcAlias, "val")),
					new LongValue(0L));
			resCase.setSwitchExpression(srcValEq0);

			WhenClause whenClause = new WhenClause();
			whenClause.setWhenExpression(new LongValue(1L));
			whenClause.setThenExpression(new LongValue(1L));

			resCase.setWhenClauses(Arrays.asList(whenClause));

			resCase.setElseExpression(countExp);

			ResSelectExpression res = new ResSelectExpression(resCase);
			plainSelect.setRes(res);

			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(svSrc, plainSelect, tmpSrcAlias);

			plainSelect.setFromItem(tmpSrc);

			GroupByElement groupBy = new GroupByElement();

			List<Expression> groupByList = new ArrayList<Expression>();
			for (Variable v : svSrc) {
				groupByList.add(new Column(Arrays.asList(tmpSrcAlias, "ref_".concat(v.getName()))));
			}
			groupByList.add(new Column(Arrays.asList(tmpSrcAlias, "val")));

			groupBy.addGroupByExpressions(groupByList);

			plainSelect.setGroupByElement(groupBy);
		}

		return plainSelect;
	}

	private PlainSelect mapReject(IteratorExp exp) {
		PlainSelect plainSelect = new PlainSelect();

		// Beign Preparation
		String tmpBodyAlias = "TMP_BODY";
		com.vgu.se.jocl.expressions.Expression bodyExp = exp.getBody();
		bodyExp.accept(this);

		Select bodyMap = this.getSelect();
		SubSelect tmpBody = new SubSelect(bodyMap.getSelectBody(), tmpBodyAlias);

		String tmpSrcAlias = "TMP_SOURCE";
		com.vgu.se.jocl.expressions.Expression srcExp = exp.getSource();
		srcExp.accept(this);

		Select srcMap = this.getSelect();
		SubSelect tmpSrc = new SubSelect(srcMap.getSelectBody(), tmpSrcAlias);

		Variable v = exp.getIterator();

		List<Variable> fvExp = VariableUtils.FVars(exp);
		boolean isFvExpEmpty = fvExp.isEmpty();

		List<Variable> fvBody = VariableUtils.FVars(bodyExp);
		boolean isVarInFvBody = fvBody.contains(v);

		List<Variable> svBody = VariableUtils.SVars(bodyExp);
		List<Variable> svSrc = VariableUtils.SVars(srcExp);

		List<Variable> svBodyComplement = VariableUtils.getComplement(svSrc, svBody);
		List<Variable> svBodyComplementExcludeCurrentVar = VariableUtils.getComplement(svBodyComplement,
				Arrays.asList(v));

		// . End preparation

		if (isFvExpEmpty) {
			// Case 1
			ValSelectExpression val = new ValSelectExpression(new Column(Arrays.asList(tmpBodyAlias, "val")));
			plainSelect.setVal(val);

			ResSelectExpression res = new ResSelectExpression(
					new Column(Arrays.asList(tmpBodyAlias, ("ref_").concat(v.getName()))));
			plainSelect.setRes(res);

			plainSelect.setFromItem(tmpBody);

			if (!isVarInFvBody) {
				// Case 3
				Join join = new Join();

				join.setRightItem(tmpSrc);

				plainSelect.setJoins(Arrays.asList(join));
			}

			BinaryExpression resBodyEq0 = buildBinExp("=", new Column(Arrays.asList(tmpBodyAlias, "res")),
					new LongValue(0L));

			plainSelect.setWhere(resBodyEq0);

		} else {
			CaseExpression valCase = new CaseExpression();

			BinaryExpression srcValEq0 = buildBinExp("=", new Column(Arrays.asList(tmpSrcAlias, "val")),
					new LongValue(0L));

			IsNullExpression isNullBodyRefV = new IsNullExpression();
			isNullBodyRefV.setLeftExpression(new Column(Arrays.asList(tmpBodyAlias, "ref_".concat(v.getName()))));

			BinaryExpression switchCase = buildBinExp("or", srcValEq0, isNullBodyRefV);

			valCase.setSwitchExpression(switchCase);

			WhenClause valWhenClause = new WhenClause();
			valWhenClause.setWhenExpression(new LongValue(1L));
			valWhenClause.setThenExpression(new LongValue(0L));
			valCase.setWhenClauses(Arrays.asList(valWhenClause));

			valCase.setElseExpression(new LongValue(1L));

			ValSelectExpression val = new ValSelectExpression(valCase);
			plainSelect.setVal(val);

			CaseExpression resCase = new CaseExpression();
			resCase.setSwitchExpression(switchCase);

			WhenClause resWhenClause = new WhenClause();
			resWhenClause.setWhenExpression(new LongValue(1L));
			resWhenClause.setThenExpression(new NullValue());

			resCase.setWhenClauses(Arrays.asList(resWhenClause));

			resCase.setElseExpression(new Column(Arrays.asList(tmpSrcAlias, "res")));

			ResSelectExpression res = new ResSelectExpression(resCase);
			plainSelect.setRes(res);

			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(svSrc, plainSelect, tmpSrcAlias);
			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(svBodyComplementExcludeCurrentVar, plainSelect,
					tmpBodyAlias);

			plainSelect.setFromItem(tmpSrc);

			Join join = new Join();

			if (isVarInFvBody) {
				// Case 2
				join.setLeft(true);
			}
			// else { case 4 }

			PlainSelect tmpSelect = new PlainSelect();
			tmpSelect.addSelectItems(new AllColumns());

			String tmpAlias = "TMP";
			SubSelect tmp = new SubSelect(tmpBody.getSelectBody(), tmpAlias);

			tmpSelect.setFromItem(tmp);

			BinaryExpression tmpResEq0 = buildBinExp("=", new Column(Arrays.asList(tmpAlias, "res")),
					new LongValue(0L));

			tmpSelect.setWhere(tmpResEq0);

			SubSelect tmpBodyInJoin = new SubSelect(tmpSelect, tmpBodyAlias);

			join.setRightItem(tmpBodyInJoin);

			BinaryExpression onCond = buildBinExp("=", new Column(Arrays.asList(tmpSrcAlias, "res")),
					new Column(Arrays.asList(tmpBodyAlias, "ref_".concat(v.getName()))));

			for (Variable vPrime : svSrc) {
				BinaryExpression srcRefEqbodyRef = buildBinExp("=",
						new Column(Arrays.asList(tmpSrcAlias, "ref_".concat(vPrime.getName()))),
						new Column(Arrays.asList(tmpBodyAlias, "ref_".concat(vPrime.getName()))));

				onCond = buildBinExp("and", onCond, srcRefEqbodyRef);
			}

			join.addOnExpression(onCond);

			plainSelect.setJoins(Arrays.asList(join));

		}

		return plainSelect;
	}

	private PlainSelect mapSelect(IteratorExp exp) {
		PlainSelect plainSelect = new PlainSelect();

		// Preparation
		String tmpBodyAlias = "TEMP_body";

		com.vgu.se.jocl.expressions.Expression bodyExp = exp.getBody();

		bodyExp.accept(this);
		Select bodyMap = this.getSelect();
		SubSelect tmpBody = new SubSelect(bodyMap.getSelectBody(), tmpBodyAlias);

		String tmpSourceAlias = "TEMP_src";

		com.vgu.se.jocl.expressions.Expression sourceExp = exp.getSource();

		sourceExp.accept(this);
		Select sourceMap = this.getSelect();
		SubSelect tmpSource = new SubSelect(sourceMap.getSelectBody(), tmpSourceAlias);

		Variable vExp = exp.getIterator();

		List<Variable> fvExp = VariableUtils.FVars(exp);
		boolean isEmptyFvExp = fvExp.isEmpty();

		List<Variable> fvBody = VariableUtils.FVars(bodyExp);
		boolean isVarInFvBody = fvBody.contains(vExp);

		List<Variable> svBody = VariableUtils.SVars(bodyExp);
		List<Variable> svSrc = VariableUtils.SVars(sourceExp);

		List<Variable> svBodyComplement = VariableUtils.getComplement(svSrc, svBody);
		List<Variable> svBodyComplementExcludeCurrentVar = VariableUtils.getComplement(svBodyComplement,
				Arrays.asList(vExp));

		String vName = exp.getIterator().getName();

		// Case 1
		if (isEmptyFvExp) {
			Table fromTable = new Table();

			Column valCol = new Column(fromTable, "val");
			ValSelectExpression val = new ValSelectExpression(valCol);
			plainSelect.setVal(val);

			Column resCol = new Column(fromTable, "ref_".concat(vName));
			ResSelectExpression res = new ResSelectExpression(resCol);
			plainSelect.setRes(res);

			// Case 1
			if (isVarInFvBody) {

				fromTable.setName(tmpBodyAlias);

				plainSelect.setFromItem(tmpBody);

			} else {
				// Case 3
				resCol.setColumnName("res");

				fromTable.setName(tmpSourceAlias);

				plainSelect.setFromItem(tmpSource);

				Join join = new Join();

				join.setRightItem(tmpBody);

				plainSelect.setJoins(Arrays.asList(join));

			}

			BinaryExpression whereCond = buildBinExp("=", new Column(Arrays.asList(tmpBodyAlias, "res")),
					new LongValue(1L));
			plainSelect.setWhere(whereCond);

		} else { // Case 2

			// Val column
			CaseExpression valCase = new CaseExpression();

			BinaryExpression srcValEq0 = buildBinExp("=", new Column(Arrays.asList(tmpSourceAlias, "val")),
					new LongValue(0L));

			IsNullExpression bodyRefIsNull = new IsNullExpression();
			bodyRefIsNull.setLeftExpression(new Column(Arrays.asList(tmpBodyAlias, "ref_".concat(vName))));

			BinaryExpression valAndResCaseSwitch = buildBinExp("or", srcValEq0, bodyRefIsNull);

			valCase.setSwitchExpression(valAndResCaseSwitch);

			WhenClause valWhen1Then0 = new WhenClause();
			valWhen1Then0.setWhenExpression(new LongValue(1L));
			valWhen1Then0.setThenExpression(new LongValue(0L));

			valCase.setWhenClauses(Arrays.asList(valWhen1Then0));

			valCase.setElseExpression(new LongValue(1L));

			ValSelectExpression val = new ValSelectExpression(valCase);
			plainSelect.setVal(val);

			// Res column
			CaseExpression resCase = new CaseExpression();

			resCase.setSwitchExpression(valAndResCaseSwitch);

			WhenClause resWhen1ThenNull = new WhenClause();
			resWhen1ThenNull.setWhenExpression(new LongValue(1L));
			resWhen1ThenNull.setThenExpression(new NullValue());

			resCase.setWhenClauses(Arrays.asList(resWhen1ThenNull));

			resCase.setElseExpression(new Column(Arrays.asList(tmpSourceAlias, "res")));

			ResSelectExpression res = new ResSelectExpression(resCase);
			plainSelect.setRes(res);

			// Ref columns
			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(svSrc, plainSelect, tmpSourceAlias);
			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(svBodyComplementExcludeCurrentVar, plainSelect,
					tmpBodyAlias);

			plainSelect.setFromItem(tmpSource);

			Join join = new Join();

			// Case 2
			if (isVarInFvBody) {
				join.setLeft(true);

			} else {
				// Case 4
				// No need!!
			}

			// Right item of Left join
			PlainSelect tmpSelect = new PlainSelect();
			tmpSelect.addSelectItems(new AllColumns());

			String tmpSelectAlias = "TEMP";
			SubSelect tmp = new SubSelect(bodyMap.getSelectBody(), tmpSelectAlias);

			tmpSelect.setFromItem(tmp);

			BinaryExpression tmpResEq1 = buildBinExp("=", new Column(Arrays.asList(tmpSelectAlias, "res")),
					new LongValue(1L));

			tmpSelect.setWhere(tmpResEq1);

			SubSelect tmpBodyInJoin = new SubSelect(tmpSelect, tmpBodyAlias);

			join.setRightItem(tmpBodyInJoin);

			BinaryExpression onCond = buildBinExp("=", new Column(Arrays.asList(tmpSourceAlias, "res")),
					new Column(Arrays.asList(tmpBodyAlias, "ref_".concat(vName))));

			List<Variable> sVarsSrc = VariableUtils.SVars(sourceExp);

			for (Variable vPrime : sVarsSrc) {
				BinaryExpression refBinExp = buildBinExp("=",
						new Column(Arrays.asList(tmpSourceAlias, "ref_".concat(vPrime.getName()))),
						new Column(Arrays.asList(tmpBodyAlias, "ref_".concat(vPrime.getName()))));

				onCond = buildBinExp("and", onCond, refBinExp);
			}

			join.addOnExpression(onCond);

			plainSelect.setJoins(Arrays.asList(join));
		}

		return plainSelect;
	}

	private PlainSelect mapForAll(IteratorExp exp) {
		// Preparation for Case 1, 2, 3, 4
		PlainSelect plainSelect = new PlainSelect();
		plainSelect.createTrueValColumn();

		Function countAll = new Function();
		countAll.setName("COUNT");
		countAll.setAllColumns(true);

		BinaryExpression countAllEq0 = buildBinExp("=", countAll, new LongValue(0L));
		// .END preparation

		String tmpSourceAlias = "TEMP_SOURCE";
		OclExp sourceExp = (OclExp) exp.getSource();
		sourceExp.accept(this);

		Select sourceMap = this.getSelect();
		SubSelect tmpSource = new SubSelect(sourceMap.getSelectBody(), tmpSourceAlias);

		String tmpBodyAlias = "TEMP_body";
		com.vgu.se.jocl.expressions.Expression bodyExp = exp.getBody();
		bodyExp.accept(this);

		Select bodyMap = this.getSelect();
		SubSelect tmpBody = new SubSelect(bodyMap.getSelectBody(), tmpBodyAlias);

		Variable v = exp.getIterator();

		List<Variable> fVarsBody = VariableUtils.FVars(bodyExp);
		boolean isVarInFvBody = fVarsBody.contains(v);

		List<Variable> fVarsExp = VariableUtils.FVars(exp);
		boolean isEmptyFvExp = fVarsExp.isEmpty();

		if (isEmptyFvExp) {
			ResSelectExpression res = new ResSelectExpression(countAllEq0);
			plainSelect.setRes(res);

			// Case 1
			if (isVarInFvBody) {
				plainSelect.setFromItem(tmpBody);

			} else { // Case 3
				plainSelect.setFromItem(tmpSource);

				Join join = new Join();

				join.setRightItem(tmpBody);

				plainSelect.setJoins(Arrays.asList(join));
			}

			BinaryExpression where = buildBinExp("=", new Column(Arrays.asList(tmpBodyAlias, "res")),
					new LongValue(0L));

			plainSelect.setWhere(where);

		} else {
			// Preparation for Case 2, 4
			List<Variable> sVarsSrc = VariableUtils.SVars(sourceExp);
			List<Variable> sVarsBody = VariableUtils.SVars(bodyExp);
			Variable outerVar = v;

			List<Variable> sVarsComplement = new ArrayList<Variable>(sVarsBody);
			sVarsComplement.removeAll(sVarsSrc);
			sVarsComplement.remove(v);
			// .End preparation

			CaseExpression resCase = new CaseExpression();

			IsNullExpression isNullSrcRef = new IsNullExpression();
			isNullSrcRef.setLeftExpression(new Column(Arrays.asList(tmpBodyAlias, "ref_".concat(outerVar.getName()))));

			resCase.setSwitchExpression(isNullSrcRef);

			WhenClause when1Then1 = new WhenClause();
			when1Then1.setWhenExpression(new LongValue(1L));
			when1Then1.setThenExpression(new LongValue(1L));

			resCase.setWhenClauses(Arrays.asList(when1Then1));

			resCase.setElseExpression(new Column(Arrays.asList(tmpBodyAlias, "res")));

			ResSelectExpression res = new ResSelectExpression(resCase);

			plainSelect.setRes(res);

			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsSrc, plainSelect, tmpSourceAlias);
			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsComplement, plainSelect, tmpBodyAlias);

			plainSelect.setFromItem(tmpSource);

			Join join = new Join();

			// Case 2
			if (isVarInFvBody) {
				join.setLeft(true);

			} else { // Case 4
				// No need!
			}

			// Right item of left join
			PlainSelect plainSelectInJoin = new PlainSelect();

			plainSelectInJoin.createTrueValColumn();

			ResSelectExpression resInJoin = new ResSelectExpression(countAllEq0);
			plainSelectInJoin.setRes(resInJoin);

			List<Variable> sVarsBodyLessV = new ArrayList<Variable>(sVarsBody);
//            sVarsBodyLessV.remove(v);

//            System.out.println("Exp:" + exp.getOclStr() + "\nBody Exp:" + bodyExp.getOclStr() + "\n" + sVarsBody);

			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsBodyLessV, plainSelectInJoin, tmpBodyAlias);

			plainSelectInJoin.setFromItem(tmpBody);

			Function ifNull = new Function();
			ifNull.setName("IFNULL");

			ExpressionList ifNullParams = new ExpressionList(new Column(Arrays.asList(tmpBodyAlias, "res")),
					new LongValue(0L));
			ifNull.setParameters(ifNullParams);

			BinaryExpression ifNullEq0 = buildBinExp("=", ifNull, new LongValue(0L));

			BinaryExpression tmpBodyValEq1 = buildBinExp("=", new Column(Arrays.asList(tmpBodyAlias, "val")),
					new LongValue(1L));

			BinaryExpression whereCond = buildBinExp("and", ifNullEq0, tmpBodyValEq1);
			plainSelectInJoin.setWhere(whereCond);

			List<Expression> groupByExps = new ArrayList<Expression>();
			org.vgu.ocl2psql.sql.utils.VariableUtils.addVarToList(sVarsBodyLessV, groupByExps, tmpBodyAlias);

			GroupByElement groupBy = new GroupByElement();
			groupBy.addGroupByExpressions(groupByExps);

			plainSelectInJoin.setGroupByElement(groupBy);

			SubSelect tmpBodyInJoin = new SubSelect(plainSelectInJoin, tmpBodyAlias);

			join.setRightItem(tmpBodyInJoin);

			plainSelect.setJoins(Arrays.asList(join));
			BinaryExpression onExp = null;
			for (Variable refSrc : sVarsSrc) {

				BinaryExpression refSrcEqRefBody = buildBinExp("=",
						new Column(Arrays.asList(tmpSourceAlias, "ref_".concat(refSrc.getName()))),
						new Column(Arrays.asList(tmpBodyAlias, "ref_".concat(refSrc.getName()))));

				if (onExp == null) {
					onExp = refSrcEqRefBody;
				} else {
					onExp = buildBinExp("and", onExp, refSrcEqRefBody);
				}
			}

			join.addOnExpression(onExp);

		}

		return plainSelect;
	}

	private PlainSelect mapExists(IteratorExp exp) {
		// Preparation for Case 1, 2, 3, 4
		PlainSelect plainSelect = new PlainSelect();
		plainSelect.createTrueValColumn();

		Function countAll = new Function();
		countAll.setName("COUNT");
		countAll.setAllColumns(true);

		BinaryExpression countAllEq0 = buildBinExp(">", countAll, new LongValue(0L));
		// .END preparation

		String tmpSourceAlias = "TEMP_SOURCE";
		OclExp sourceExp = (OclExp) exp.getSource();
		sourceExp.accept(this);

		Select sourceMap = this.getSelect();
		SubSelect tmpSource = new SubSelect(sourceMap.getSelectBody(), tmpSourceAlias);

		String tmpBodyAlias = "TEMP_body";
		com.vgu.se.jocl.expressions.Expression bodyExp = exp.getBody();
		bodyExp.accept(this);

		Select bodyMap = this.getSelect();
		SubSelect tmpBody = new SubSelect(bodyMap.getSelectBody(), tmpBodyAlias);

		Variable v = exp.getIterator();

		List<Variable> fVarsBody = VariableUtils.FVars(bodyExp);
		boolean isVarInFvBody = fVarsBody.contains(v);

		List<Variable> fVarsExp = VariableUtils.FVars(exp);
		boolean isEmptyFvExp = fVarsExp.isEmpty();

		if (isEmptyFvExp) {
			ResSelectExpression res = new ResSelectExpression(countAllEq0);
			plainSelect.setRes(res);

			// Case 1
			if (isVarInFvBody) {
				plainSelect.setFromItem(tmpBody);

			} else { // Case 3
				plainSelect.setFromItem(tmpSource);

				Join join = new Join();

				join.setRightItem(tmpBody);

				plainSelect.setJoins(Arrays.asList(join));
			}

			BinaryExpression where = buildBinExp("=", new Column(Arrays.asList(tmpBodyAlias, "res")),
					new LongValue(1L));

			plainSelect.setWhere(where);

		} else {
			// Preparation for Case 2, 4
			List<Variable> sVarsSrc = VariableUtils.SVars(sourceExp);
			List<Variable> sVarsBody = VariableUtils.SVars(bodyExp);
			Variable outerVar = v;

			List<Variable> sVarsComplement = new ArrayList<Variable>(sVarsBody);
			sVarsComplement.removeAll(sVarsSrc);
			sVarsComplement.remove(v);
			// .End preparation

			CaseExpression resCase = new CaseExpression();

			IsNullExpression isNullSrcRef = new IsNullExpression();
			isNullSrcRef.setLeftExpression(new Column(Arrays.asList(tmpBodyAlias, "ref_".concat(outerVar.getName()))));

			resCase.setSwitchExpression(isNullSrcRef);

			WhenClause when1Then0 = new WhenClause();
			when1Then0.setWhenExpression(new LongValue(1L));
			when1Then0.setThenExpression(new LongValue(0L));

			resCase.setWhenClauses(Arrays.asList(when1Then0));

			resCase.setElseExpression(new Column(Arrays.asList(tmpBodyAlias, "res")));

			ResSelectExpression res = new ResSelectExpression(resCase);

			plainSelect.setRes(res);

			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsSrc, plainSelect, tmpSourceAlias);
			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsComplement, plainSelect, tmpBodyAlias);

			plainSelect.setFromItem(tmpSource);

			Join join = new Join();
			// Case 2
			if (isVarInFvBody) {
				join.setLeft(true);
			} else { // Case 4
				// No need!
			}

			// Right item of left join
			PlainSelect plainSelectInJoin = new PlainSelect();

			ResSelectExpression resInJoin = new ResSelectExpression(countAllEq0);
			plainSelectInJoin.setRes(resInJoin);

			List<Variable> sVarsBodyLessV = new ArrayList<Variable>(sVarsBody);
//            sVarsBodyLessV.remove(v);

			org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsBodyLessV, plainSelectInJoin, tmpBodyAlias);

			plainSelectInJoin.setFromItem(tmpBody);

			BinaryExpression whereCond = buildBinExp("=", new Column(Arrays.asList(tmpBodyAlias, "res")),
					new LongValue(1L));
			plainSelectInJoin.setWhere(whereCond);

			List<Expression> groupByExps = new ArrayList<Expression>();
			org.vgu.ocl2psql.sql.utils.VariableUtils.addVarToList(sVarsBodyLessV, groupByExps, tmpBodyAlias);

			GroupByElement groupBy = new GroupByElement();
			groupBy.addGroupByExpressions(groupByExps);

			plainSelectInJoin.setGroupByElement(groupBy);

			SubSelect tmpBodyInJoin = new SubSelect(plainSelectInJoin, tmpBodyAlias);

			join.setRightItem(tmpBodyInJoin);

			plainSelect.setJoins(Arrays.asList(join));
			BinaryExpression onExp = null;
			for (Variable refSrc : sVarsSrc) {
				BinaryExpression refSrcEqRefBody = buildBinExp("=",
						new Column(Arrays.asList(tmpSourceAlias, "ref_".concat(refSrc.getName()))),
						new Column(Arrays.asList(tmpBodyAlias, "ref_".concat(refSrc.getName()))));

				if (onExp == null) {
					onExp = refSrcEqRefBody;
				} else {
					onExp = buildBinExp("and", onExp, refSrcEqRefBody);
				}
			}

			join.addOnExpression(onExp);
		}

		return plainSelect;
	}

	private PlainSelect mapFlatten(IteratorExp exp) {
		if (!(exp.getSource() instanceof IteratorExp)) {
			throw new MappingException("Cannot flatten a source which is " + "not a collection.");
		}

		PlainSelect plainSelect = new PlainSelect();
		String tmpSourceAlias = "TEMP_SOURCE";

		IteratorExp tmpSourceExp = (IteratorExp) exp.getSource();
		tmpSourceExp.accept(this);
		Select tmpSourceMap = this.getSelect();

		SubSelect tmpSource = new SubSelect(tmpSourceMap.getSelectBody(), tmpSourceAlias);

		List<Variable> fVarsExp = VariableUtils.FVars(exp);
		boolean isEmptyFvExp = fVarsExp.isEmpty();

		if (isEmptyFvExp) {
			plainSelect.createTrueValColumn();

			ResSelectExpression res = new ResSelectExpression(new Column(Arrays.asList(tmpSourceAlias, "res")));
			plainSelect.setRes(res);

			plainSelect.setFromItem(tmpSource);

			BinaryExpression where = buildBinExp("=", new Column(Arrays.asList(tmpSourceAlias, "val")),
					new LongValue(1L));
			plainSelect.setWhere(where);

		} else {
			String tmpFlattenAlias = "TEMP_FLATTEN";
			String tmpAlias = "TEMP";

			Variable v = ((IteratorExp) exp.getSource()).getIterator();

			com.vgu.se.jocl.expressions.Expression srcOfSrcExp = ((IteratorExp) exp.getSource()).getSource();
			srcOfSrcExp.accept(this);
			Select srcOfSrcMap = this.getSelect();
			SubSelect tmpSourceOfSource = new SubSelect(srcOfSrcMap.getSelectBody(), tmpSourceAlias);

			List<Variable> sVarsSrcOfSrcExp = VariableUtils.SVars(((IteratorExp) exp.getSource()).getSource());

			List<Variable> fVarsBody = VariableUtils.FVars(exp.getBody());
			boolean isVarInBody = fVarsBody.contains(v);
			isVarInBody = true; // assumtion on paper

			if (isVarInBody) {
				CaseExpression valCase = new CaseExpression();

				IsNullExpression isNull = new IsNullExpression();
				isNull.setLeftExpression(new Column(Arrays.asList(tmpFlattenAlias, "val")));

				valCase.setSwitchExpression(isNull);

				WhenClause when1Then0 = new WhenClause();
				when1Then0.setWhenExpression(new LongValue(1L));
				when1Then0.setThenExpression(new LongValue(0L));

				valCase.setWhenClauses(Arrays.asList(when1Then0));

				valCase.setElseExpression(new Column(Arrays.asList(tmpFlattenAlias, "val")));

				ValSelectExpression val = new ValSelectExpression(valCase);
				plainSelect.setVal(val);

				CaseExpression resCase = new CaseExpression();

				resCase.setSwitchExpression(isNull);

				WhenClause when1ThenNull = new WhenClause();
				when1ThenNull.setWhenExpression(new LongValue(1L));
				when1ThenNull.setThenExpression(new NullValue());

				resCase.setWhenClauses(Arrays.asList(when1ThenNull));

				resCase.setElseExpression(new Column(Arrays.asList(tmpFlattenAlias, "res")));

				ResSelectExpression res = new ResSelectExpression(resCase);
				plainSelect.setRes(res);

				org.vgu.ocl2psql.sql.utils.VariableUtils.addVar(sVarsSrcOfSrcExp, plainSelect, tmpFlattenAlias);

				plainSelect.setFromItem(tmpSourceOfSource);

				Join join = new Join();
				join.setLeft(true);

				// Right item of left join
				PlainSelect plainSelectInJoin = new PlainSelect();
				plainSelectInJoin.addSelectItems(new AllColumns());

				SubSelect tmp = new SubSelect(tmpSourceMap.getSelectBody(), tmpAlias);
				plainSelectInJoin.setFromItem(tmp);

				BinaryExpression valEq1 = buildBinExp("=", new Column(Arrays.asList(tmpAlias, "val")),
						new LongValue(1L));
				plainSelectInJoin.setWhere(valEq1);

				SubSelect tmpFlatten = new SubSelect(plainSelectInJoin, tmpFlattenAlias);
				join.setRightItem(tmpFlatten);
				// .END Right item

				List<Variable> sVarsSrcOfSrcAndV = new ArrayList<Variable>(sVarsSrcOfSrcExp);
				sVarsSrcOfSrcAndV.add(v);

				BinaryExpression onExp = null;
				for (Variable var : sVarsSrcOfSrcExp) {
					BinaryExpression refSrcEqRefFlatten = buildBinExp("=",
							new Column(Arrays.asList(tmpSourceAlias, "ref_".concat(var.getName()))),
							new Column(Arrays.asList(tmpFlattenAlias, "ref_".concat(var.getName()))));

					if (onExp == null) {
						onExp = refSrcEqRefFlatten;
					} else {
						onExp = buildBinExp("and", onExp, refSrcEqRefFlatten);
					}
				}

				join.addOnExpression(onExp);

				plainSelect.setJoins(Arrays.asList(join));
			}
		}

		return plainSelect;
	}

	private BinaryExpression buildBinExp(String operator, Expression leftExp, Expression rightExp) {

		BinaryExpression binExp = null;

		switch (operator.toLowerCase()) {
		case "=":
			binExp = new EqualsTo();
			break;
		case "<>":
			binExp = new NotEqualsTo();
			break;
		case "<":
			binExp = new MinorThan();
			break;
		case "<=":
			binExp = new MinorThanEquals();
			break;
		case ">":
			binExp = new GreaterThan();
			break;
		case ">=":
			binExp = new GreaterThanEquals();
			break;
		case "and":
			binExp = new AndExpression(null, null);
			break;
		case "or":
			binExp = new OrExpression(null, null);
			break;
		}

		binExp.setLeftExpression(leftExp);
		binExp.setRightExpression(rightExp);

		return binExp;
	}

}
