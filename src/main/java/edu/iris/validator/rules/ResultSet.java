package edu.iris.validator.rules;

import java.util.ArrayList;
import java.util.List;

public class ResultSet {

	private List<Result> list = new ArrayList<>();

	public void add(Result result) {
		this.list.add(result);
	}

	public void add(ResultSet resultSet) {
		if (resultSet.get() != null && !resultSet.get().isEmpty()) {
			this.list.addAll(resultSet.get());
		}
	}

	public List<Result> get() {
		return this.list;
	}

	public int size() {
		return this.list.size();
	}

	public boolean isEmpty() {
		return this.list.isEmpty();
	}
}
