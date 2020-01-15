package edu.iris.validator.restrictions;

import edu.iris.station.model.Channel;
import edu.iris.station.model.Response;

public interface Restriction {

	public boolean qualifies(Channel channel);

	public boolean qualifies(Response response);
}
