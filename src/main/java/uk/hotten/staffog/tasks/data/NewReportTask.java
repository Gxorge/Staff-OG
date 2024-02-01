package uk.hotten.staffog.tasks.data;

import lombok.Getter;
import lombok.Setter;

public class NewReportTask {

	@Getter @Setter private int id;
	@Getter @Setter private String offender;
	@Getter @Setter private String by;
	@Getter @Setter private String type;

}