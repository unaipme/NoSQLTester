package com.unai.app.hbase.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.unai.app.hbase.model.Column;
import com.unai.app.hbase.model.ColumnFamily;
import com.unai.app.hbase.model.Row;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/hbase")
@Api
public class HBaseRestController {
	
	private Configuration config = HBaseConfiguration.create();
	
	private Connection conn = null;
	
	public static final String isNumeric = "[-+]?[0-9]*\\.?[0-9]+?";
	
	public HBaseRestController() {
		connect();
	}
	
	private Logger log = LoggerFactory.getLogger(HBaseRestController.class);
	
	private void connect() {
		try {
			if (conn == null)
				conn = ConnectionFactory.createConnection(config);
		} catch (IOException e) {
			if (conn != null) {
				conn.abort(e.getMessage(), e);
			}
		}
	}
	
	
	@SuppressWarnings("deprecation")
	@GetMapping("/tables/{table}/{rowid}")
	@ApiOperation(notes="public ResponseEntity<?> getAll(String, String)::81", value = "Gets all columns of a row, sorted by column family.")
	public ResponseEntity<?> getAll(@PathVariable("table") String tablename, @PathVariable("rowid") String rowid) throws IOException {
		Table table = null;
		try {
			connect();
			table = conn.getTable(TableName.valueOf(tablename));
			Get get = new Get(Bytes.toBytes(rowid));
			get.setMaxVersions(5);
			ListIterator<Cell> it = table.get(get).listCells().listIterator();
			Row row = new Row(rowid);
			while (it.hasNext()) {
				Cell cell = it.next();
				ColumnFamily cf = row.getColumnFamily(cell.getFamily());
				Column c = cf.getColumn(cell.getQualifier());
				c.put(cell.getTimestamp(), Bytes.toString(cell.getValue()));
			}
			return ResponseEntity.ok(row);
		} catch (TableNotFoundException | NullPointerException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (table != null) table.close();
		}
	}
	
	@SuppressWarnings("deprecation")
	@GetMapping("/tables/{table}/{rowid}/{cf}")
	@ApiOperation(notes="public ResponseEntity<?> getCf(String, String, String)::114", value="Gets all columns of the given column family and row.")
	public ResponseEntity<?> getCf(@PathVariable("table") String tablename, @PathVariable("rowid") String rowid, @PathVariable("cf") String cf) throws IOException {
		Table table = null;
		try {
			connect();
			table = conn.getTable(TableName.valueOf(tablename));
			Get get = new Get(Bytes.toBytes(rowid));
			get.addFamily(Bytes.toBytes(cf));
			get.setMaxVersions(5);
			ListIterator<Cell> it = table.get(get).listCells().listIterator();
			ColumnFamily cfamily = new ColumnFamily(cf);
			while (it.hasNext()) {
				Cell c = it.next();
				Column column = cfamily.getColumn(c.getQualifier());
				column.put(c.getTimestamp(), Bytes.toString(c.getValue()));
			}
			return ResponseEntity.ok(cfamily);
		} catch (TableNotFoundException | NullPointerException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (table != null) table.close();
		}
	}
	
	@SuppressWarnings("deprecation")
	@GetMapping("/tables/{table}/{rowid}/{cf}/{col}")
	@ApiOperation(notes="public ResponseEntity<?> getValue(String, String, String, String)::147", value="Gets the value of a given column, of given column family and row.")
	public ResponseEntity<?> getValue(@PathVariable("table") String tablename, @PathVariable("rowid") String rowid, @PathVariable("cf") String cf,
										@PathVariable("col") String col) throws IOException {
		Table table = null;
		try {
			connect();
			table = conn.getTable(TableName.valueOf(tablename));
			Get get = new Get(Bytes.toBytes(rowid));
			get.addColumn(Bytes.toBytes(cf), Bytes.toBytes(col));
			get.setMaxVersions(5);
			Column column = new Column(col);
			ListIterator<Cell> it = table.get(get).listCells().listIterator();
			while (it.hasNext()) {
				Cell c = it.next();
				column.put(c.getTimestamp(), Bytes.toString(c.getValue()));
			}
			return ResponseEntity.ok(column);
		} catch (TableNotFoundException | NullPointerException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (table != null) table.close();
		}
	}
	
	@GetMapping("/tables")
	@ApiOperation(notes="public ResponseEntity<?> getTables()::179", value="Lists all the tables in the server.")
	public ResponseEntity<?> getTables() {
		try {
			connect();
			Function<HColumnDescriptor, String> conv = d -> {return d.getNameAsString();};
			HashMap<String, List<String>> ret = new HashMap<>();
			for (TableName tn : conn.getAdmin().listTableNames()) {
				ret.putIfAbsent(tn.getNameAsString(), new ArrayList<>());
				Table table = conn.getTable(tn);
				HTableDescriptor desc = table.getTableDescriptor();
				List<String> cfs = Arrays.asList(desc.getColumnFamilies()).stream().map(conv).collect(Collectors.toList());
				ret.put(tn.getNameAsString(), cfs);
			}
			return ResponseEntity.ok(ret);
		} catch (IOException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/tables/{table}/{rowid}/{cf}/{col}/{value}", method={RequestMethod.POST, RequestMethod.PUT})
	@ApiOperation(notes="public ResponseEntity<?> put(String, String, String, String, String)::202", value="Puts the value in the given table and column family's column.")
	public ResponseEntity<?> put(@PathVariable("table") String tablename, @PathVariable("rowid") String rowid, @PathVariable("cf") String cf,
									@PathVariable("col") String col, @PathVariable("value") String value) {
		try {
			connect();
			Put put = new Put(Bytes.toBytes(rowid));
			put.addColumn(Bytes.toBytes(cf), Bytes.toBytes(col), Bytes.toBytes(value));
			conn.getTable(TableName.valueOf(tablename)).put(put);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (TableNotFoundException | NullPointerException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/tables/{table}/{rowid}", method={RequestMethod.POST, RequestMethod.PUT})
	@ApiOperation(notes="public ResponseEntity<?> putMany(String, String, HashMap<String, Object>)::225", value="Puts many values into the said table.")
	public ResponseEntity<?> putMany(@PathVariable("table") String table, @PathVariable("rowid") String rowid, @RequestBody HashMap<String, Object> values) {
		try {
			connect();
			Put put = new Put(Bytes.toBytes(rowid));
			for (Entry<String, Object> entry : values.entrySet()) {
				String cf = entry.getKey().split(":")[0];
				String qual = entry.getKey().split(":")[1];
				Object value = entry.getValue();
				if (value.toString().matches(isNumeric)) {
					put.addColumn(Bytes.toBytes(cf), Bytes.toBytes(qual), Bytes.toBytes((Double) entry.getValue()));
				} else {
					put.addColumn(Bytes.toBytes(cf), Bytes.toBytes(qual), Bytes.toBytes(entry.getValue().toString()));
				}
			}
			conn.getTable(TableName.valueOf(table)).put(put);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (TableNotFoundException | NullPointerException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value="/tables/{table}")
	@ApiOperation(notes="public ResponseEntity<?> createTable(String, List<String>)::255", value="Creates a table with the given column families.")
	public ResponseEntity<?> createTable(@PathVariable("table") String tablename, @RequestBody List<String> cfs) {
		try {
			connect();
			Admin admin = conn.getAdmin();
			HTableDescriptor htable = new HTableDescriptor(TableName.valueOf(tablename));
			ListIterator<String> it = cfs.listIterator();
			while (it.hasNext()) {
				HColumnDescriptor hcol = new HColumnDescriptor(it.next());
				htable.addFamily(hcol);
			}
			admin.createTable(htable);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (IOException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping(value="/tables/{table}")
	@ApiOperation(notes="public ResponseEntity<?> enableTable(String)::278", value="Enables the table of given name.")
	public ResponseEntity<?> enableTable(@PathVariable String table) {
		try {
			connect();
			Admin admin = conn.getAdmin();
			admin.enableTable(TableName.valueOf(table));
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (IOException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/tables/{table}")
	@ApiOperation(notes="public ResponseEntity<?> disableThenDeleteTable(String)::295", value="The first time, it disables the said table. The second time, it deletes the table.")
	public ResponseEntity<?> disableThenDeleteTable(@PathVariable String table) {
		try {
			connect();
			Admin admin = conn.getAdmin();
			TableName tablename = TableName.valueOf(Bytes.toBytes(table));
			Map<String, String> resp = new HashMap<>();
			if (admin.isTableDisabled(tablename)) {
				admin.deleteTable(tablename);
				resp.put("message", "Table deleted successfully");
				return new ResponseEntity<>(resp, HttpStatus.GONE);
			} else {
				admin.disableTable(tablename);
				resp.put("message", "Table disabled. Repeat request to delete.");
				return new ResponseEntity<>(resp, HttpStatus.ACCEPTED);
			}
		} catch (TableNotFoundException | NullPointerException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
