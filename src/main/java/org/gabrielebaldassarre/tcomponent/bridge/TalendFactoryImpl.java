package org.gabrielebaldassarre.tcomponent.bridge;

public class TalendFactoryImpl implements TalendFlowFactory, TalendRowFactory, TalendValueFactory{

	private TalendFlowModelImpl model;
	
	public TalendFactoryImpl(TalendFlowModelImpl model){
		this.model = model;
	}
	
	
	public void setModel(TalendFlowModelImpl model){
		this.model = model;
	}
	
	public TalendFlow newFlow(String name) {
		TalendFlowImpl table = new TalendFlowImpl(model, name);
		model.addFlow(name, table);
		return table;
		
	}


	public TalendRow newRow(String table) {
		TalendRowImpl row = new TalendRowImpl(model.getFlow(table));
		model.getFlow(table).addRow(row);
		return row;
	}


	public TalendRow newRow(TalendFlow table) {
		return newRow(table.getName());
	}


	public TalendValue newValue(TalendColumn column, Object value) {
		return new TalendValueImpl((TalendColumnImpl) column, value);
	}


	
}