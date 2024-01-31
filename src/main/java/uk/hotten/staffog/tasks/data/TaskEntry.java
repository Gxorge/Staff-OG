package uk.hotten.staffog.tasks.data;

import lombok.Getter;
import lombok.Setter;

public class TaskEntry {

	public String getData() {
		return data;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Getter @Setter private int id;
	@Getter @Setter private String task;
	@Getter @Setter private String data;
	public int getId() {
		return id;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getTask() {
		return task;
	}

}