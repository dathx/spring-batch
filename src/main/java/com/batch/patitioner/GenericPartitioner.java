package com.batch.patitioner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

@Component
public class GenericPartitioner implements Partitioner {

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		Map<String, ExecutionContext> partitionData = new HashMap<>();

		for (int i = 0; i < gridSize; i++) {
			ExecutionContext value = new ExecutionContext();

			value.put("partitionList", new ArrayList<>());

			value.putInt("threadName", i);
			value.putInt("gridSize", gridSize);

			partitionData.put("partition: " + i, value);
		}
		return partitionData;
	}

}
