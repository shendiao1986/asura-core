package org.asura.core.data;

import java.util.List;

public interface IEditor {
	public void begineTransaction();

	public void commit();

	public void execute(String sql);

	public void updateRecord(DataRecord record);

	public void addRecord(DataRecord record);

	public void addRecords(List<DataRecord> records);

	public void addRecords(List<DataRecord> records, boolean override);

	public void addRecord(DataRecord record, boolean override);

	public void deleteRecord(DataRecord record);

	public void deleteRecords(List<DataRecord> records);

	public void processRecord(DataRecord record);

	public boolean containsRecord(DataRecord record);
}
