package com.unai.app.hbase.model;

import java.util.ArrayList;

import org.apache.hadoop.hbase.util.Bytes;

public class ColumnFamily {
	
	private String name;
	private ArrayList<Column> cols = new ArrayList<>();
	
	public ColumnFamily(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public Column getColumn(String name) {
		Column c = new Column(name);
		if (contains(c)) {
			return get(indexOf(c));
		} else {
			add(c);
			return c;
		}
	}
	
	public Column getColumn(byte [] name) {
		return getColumn(Bytes.toString(name));
	}
	
	public boolean contains(Column cf) {
		return cols.contains(cf);
	}
	
	public boolean add(Column cf) {
		return cols.add(cf);
	}
	
	public int indexOf(Column cf) {
		return cols.indexOf(cf);
	}
	
	public Column get(int pos) {
		return cols.get(pos);
	}
	
	public ArrayList<Column> getCols() {
		return cols;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ColumnFamily) || o == null) return false;
		ColumnFamily cf = (ColumnFamily) o;
		if (cf.getName().equals(getName())) return true;
		return false;
	}
	
}
