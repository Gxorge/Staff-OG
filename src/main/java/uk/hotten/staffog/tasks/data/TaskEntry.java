package uk.hotten.staffog.tasks.data;

import lombok.Getter;
import lombok.Setter;

public class TaskEntry {

	@Getter @Setter private int id;
	@Getter @Setter private String task;
	@Getter @Setter private String data;

}