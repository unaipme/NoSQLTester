package com.unai.app.hbase.model;

import java.util.ArrayList;

import org.apache.hadoop.hbase.util.Bytes;

public class Row {
	
	private String id;
	private ArrayList<ColumnFamily> cfs = new ArrayList<>();
	
	public Row(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public ColumnFamily getColumnFamily(String name) {
		ColumnFamily cf = new ColumnFamily(name);
		if (contains(cf)) {
			return get(indexOf(cf));
		} else {
			add(cf);
			return cf;
		}
	}
	
	public ColumnFamily getColumnFamily(byte [] name) {
		return getColumnFamily(Bytes.toString(name));
	}
	
	public boolean contains(ColumnFamily cf) {
		return cfs.contains(cf);
	}
	
	public boolean add(ColumnFamily cf) {
		return cfs.add(cf);
	}
	
	public int indexOf(ColumnFamily cf) {
		return cfs.indexOf(cf);
	}
	
	public ColumnFamily get(int pos) {
		return cfs.get(pos);
	}
	
	public ArrayList<ColumnFamily> getCfs() {
		return cfs;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Row) || o == null) return false;
		Row r = (Row) o;
		if (r.getId().equals(getId())) return true;
		return false;
	}
	
}
