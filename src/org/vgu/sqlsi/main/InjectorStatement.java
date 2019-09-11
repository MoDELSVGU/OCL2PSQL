package org.vgu.sqlsi.main;

import org.json.simple.JSONArray;

import net.sf.jsqlparser.statement.Commit;
import net.sf.jsqlparser.statement.SetStatement;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.UseStatement;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.view.AlterView;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.execute.Execute;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.merge.Merge;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.upsert.Upsert;



public class InjectorStatement implements StatementVisitor {
	private JSONArray context;
	private JSONArray policy;
	private JSONArray parameters;
	private Statement result;
	
	public JSONArray getContext() {
		return context;
	}
	public void setContext(JSONArray context) {
		this.context = context;
	}
	public JSONArray getPolicy() {
		return policy;
	}
	public void setPolicy(JSONArray policy) {
		this.policy = policy;
	}
	
	public Statement getResult() {
		return result;
	}
	public void setResult(Statement result) {
		this.result = result;
	}
	
	public JSONArray getParameters() {
		return parameters;
	}
	public void setParameters(JSONArray parameters) {
		this.parameters = parameters;
	}
	
	@Override
	public void visit(Commit commit) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(Delete delete) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(Update update) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(Insert insert) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(Replace replace) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(Drop drop) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(Truncate truncate) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(CreateIndex createIndex) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(CreateTable createTable) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(CreateView createView) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(AlterView alterView) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(Alter alter) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(Statements stmts) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(Execute execute) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(SetStatement set) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(Merge merge) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(Select select) {
		select.accept(this);
		
	}
	@Override
	public void visit(Upsert upsert) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void visit(UseStatement use) {
		// TODO Auto-generated method stub
		
	}
	
}
