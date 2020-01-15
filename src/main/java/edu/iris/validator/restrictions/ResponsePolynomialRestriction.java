package edu.iris.validator.restrictions;

import edu.iris.station.model.Channel;
import edu.iris.station.model.Response;
import edu.iris.station.model.ResponseStage;

public class ResponsePolynomialRestriction implements Restriction {

	private String name;

	public ResponsePolynomialRestriction() {
		this("ResponsePolynomialRestriction");
	}

	public ResponsePolynomialRestriction(String name) {
		this.name = name;
	}

	@Override
	public boolean qualifies(Response response) {
		if (response == null) {
			throw new IllegalArgumentException("Response cannot be null");
		}

		if (response.getInstrumentPolynomial() != null) {
			return true;
		}

		for (ResponseStage stage : response.getStage()) {
			if (stage.getPolynomial() != null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean qualifies(Channel channel) {
		if (channel == null) {
			return false;
		}
		return this.qualifies(channel.getResponse());
	}

	@Override
	public String toString() {
		return "ResponsePolynomialRestriction [name=" + name + "]";
	}

}
