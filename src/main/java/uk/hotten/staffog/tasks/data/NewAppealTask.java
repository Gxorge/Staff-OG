package uk.hotten.staffog.tasks.data;

import lombok.Getter;
import lombok.Setter;

public class NewAppealTask {

    public int getId() {
		return id;
	}
	public String getBy() {
		return by;
	}
	public String getType() {
		return type;
	}
	@Getter @Setter private int id;
    @Getter @Setter private String by;
    @Getter @Setter private String type;

}