package com.spg.batch.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import jakarta.validation.constraints.NotNull;

public class MyBatisItemWriter<T> implements ItemWriter<T>, StepExecutionListener {

	private SqlSessionTemplate sqlSessionTemplate;
	private Map<String, Object> parmap;
	private String statment;
	private List<String> statments;
	private String itemName;
	private StepExecution stepExecution;

	@Override
	public void beforeStep(@NotNull StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}

	@Override
	public void write(Chunk<? extends T> chunk) throws Exception {
		if (chunk != null && chunk.getItems().size() > 0) {
			Map<String, Object> parameter = new HashMap<>();
			if (parmap != null) {
				parameter.putAll(parmap);
			}
			if (stepExecution == null) {
				stepExecution = StepSynchronizationManager.getContext().getStepExecution();
			}
			for (Map.Entry<String, Object> entry : stepExecution.getExecutionContext().entrySet()) {
				if (!entry.getKey().startsWith("batch")) {
					parameter.put(entry.getKey(), entry.getValue());
				}
			}
			for (T item : chunk) {
				System.out.println(itemName + "=" + item);
				parameter.put(itemName, item);
				executeUpdate(parameter);
			}

		}
	}

	private void executeUpdate(Map<String, Object> parameter) {
		if (statments != null && statments.size() > 0) {
			for (String smt : statments) {
				System.out.println("run" + smt + "with" + parameter);
				update(smt, parameter);
			}
			sqlSessionTemplate.flushStatements();
		} else if (statment != null) {
			update(statment, parameter);
		}
	}

	private int update(String stmt, Map<String, Object> parameter) {

		String[] parsed = stmt.split(":");
		if (parsed.length < 2) {
			return sqlSessionTemplate.update(stmt, parameter);
		} else {
			String[] looped = parsed[1].split(",");
			int start = Integer.parseInt(looped[0]);
			int end = Integer.parseInt(looped[1]);
			String indexName = looped[2];
			int total = 0;
			for (int idx = start; idx <= end; idx++) {
				parameter.put(indexName, idx);
				int count = sqlSessionTemplate.update(parsed[0], parameter);
				total += count;
				if (count <= 0) {
					break;
				}
			}
			return total;
		}
	}
}
