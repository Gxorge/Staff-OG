package uk.hotten.staffog.tasks.data;

import lombok.Getter;
import uk.hotten.staffog.punish.data.PunishType;

public class UnpunishTask {

	public int getId() {
		return id;
	}

	public PunishType getType() {
		return type;
	}

	@Getter private PunishType type;
	@Getter private int id;
	@Getter private String name;

	public UnpunishTask(String type, int id) {

		this.type = PunishType.valueOf(type);
		this.id = id;

	}

}