package edu.iris.dmc.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Path.Node;
import javax.validation.groups.Default;

import org.hibernate.validator.engine.HibernateConstraintViolation;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Decimation;
import edu.iris.dmc.fdsn.station.model.Equipment;
import edu.iris.dmc.fdsn.station.model.Filter;
import edu.iris.dmc.fdsn.station.model.Gain;
import edu.iris.dmc.fdsn.station.model.LEVEL;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Polynomial;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Sensitivity;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.validation.rule.Rule;
import edu.iris.dmc.validation.rule.Severity;
import edu.iris.dmc.validation.validator.ResponseGroup;
import edu.iris.dmc.validation.validator.Util;
import edu.iris.dmc.validation.rule.UnitTable;

public class ValidatorServiceImp implements ValidatorService {

	private javax.validation.Validator validator;

	public ValidatorServiceImp(javax.validation.Validator validator) {
		this.validator = validator;
	}

	public List<Rule> getRules() {
		return Util.rules();
	}

	public UnitTable getUnits() {
		return Util.units();
	}

	public Errors run(List<Network> list, LEVEL level, List<Integer> ignoreList) {
		Errors errors = new Errors(ignoreList);
		for (Network network : list) {
			Set<ConstraintViolation<Network>> constraintViolations = validator.validate(network);

			for (ConstraintViolation<Network> violation : constraintViolations) {
				errors.add(Severity.ERROR, network.getCode(), network.getStartDate(), network.getEndDate(), null, null,
						null, null, null, null, null, map(violation.getPropertyPath()), violation.getInvalidValue(),
						violation.getMessage());
			}
			if (level.getValue() >= LEVEL.NETWORK.getValue()) {
				for (Station station : network.getStations()) {
					Set<ConstraintViolation<Station>> stationConstraintViolations = validator.validate(station);
					for (ConstraintViolation<Station> violation : stationConstraintViolations) {
						errors.add(Severity.ERROR, network.getCode(), network.getStartDate(), network.getEndDate(),
								station.getCode(), station.getStartDate(), station.getEndDate(), null, null, null, null,
								map(violation.getPropertyPath()), violation.getInvalidValue(), violation.getMessage());
					}
					if (level.getValue() >= LEVEL.STATION.getValue()) {
						if (station.getChannels() == null) {
							continue;
						}
						for (Channel channel : station.getChannels()) {
							Set<ConstraintViolation<Channel>> channelConstraintViolations = null;
							if (level.getValue() > LEVEL.CHANNEL.getValue()) {
								channelConstraintViolations = validator.validate(channel, Default.class,
										ResponseGroup.class);
							} else {
								channelConstraintViolations = validator.validate(channel);
							}

							for (ConstraintViolation<Channel> violation : channelConstraintViolations) {
								errors.add(Severity.ERROR, network.getCode(), network.getStartDate(),
										network.getEndDate(), station.getCode(), station.getStartDate(),
										station.getEndDate(), channel.getLocationCode(), channel.getCode(),
										channel.getStartDate(), channel.getEndDate(), map(violation.getPropertyPath()),
										violation.getInvalidValue(), violation.getMessage());
							}

							if (channel.getSensor() != null) {
								Set<ConstraintViolation<Equipment>> eViolations = validator
										.validate(channel.getSensor());
								for (ConstraintViolation<Equipment> violation : eViolations) {
									errors.add(Severity.ERROR, network.getCode(), network.getStartDate(),
											network.getEndDate(), station.getCode(), station.getStartDate(),
											station.getEndDate(), channel.getLocationCode(), channel.getCode(),
											channel.getStartDate(), channel.getEndDate(),
											map(violation.getPropertyPath()), violation.getInvalidValue(),
											violation.getMessage());
								}
							}
							if (level.getValue() >= LEVEL.CHANNEL.getValue()) {
								Response response = channel.getResponse();
								if (response == null) {
									// not sure what to do, what kind of
									// rules here?????
								}
								Set<ConstraintViolation<Response>> responseConstraintViolations = validator
										.validate(response);
								for (ConstraintViolation<Response> violation : responseConstraintViolations) {
									errors.add(Severity.ERROR, network.getCode(), network.getStartDate(),
											network.getEndDate(), station.getCode(), station.getStartDate(),
											station.getEndDate(), channel.getLocationCode(), channel.getCode(),
											channel.getStartDate(), channel.getEndDate(),
											map(violation.getPropertyPath()), violation.getInvalidValue(),
											violation.getMessage());
								}

								if (response.getInstrumentSensitivity() != null) {
									Set<ConstraintViolation<Sensitivity>> stageConstraintViolations = validator
											.validate(response.getInstrumentSensitivity());
									for (ConstraintViolation<Sensitivity> violation : stageConstraintViolations) {
										// System.out.println("Violation:
										// "+violation);
										HibernateConstraintViolation<?> hibernateViolation = violation
												.unwrap(HibernateConstraintViolation.class);
										if (hibernateViolation.getDynamicPayload(Severity.class) == Severity.ERROR) {
											errors.add(Severity.ERROR, network.getCode(), network.getStartDate(),
													network.getEndDate(), station.getCode(), station.getStartDate(),
													station.getEndDate(), channel.getLocationCode(), channel.getCode(),
													channel.getStartDate(), channel.getEndDate(),
													map(violation.getPropertyPath()), violation.getInvalidValue(),
													violation.getMessage());
										} else {
											errors.add(Severity.WARN, network.getCode(), network.getStartDate(),
													network.getEndDate(), station.getCode(), station.getStartDate(),
													station.getEndDate(), channel.getLocationCode(), channel.getCode(),
													channel.getStartDate(), channel.getEndDate(),
													map(violation.getPropertyPath()), violation.getInvalidValue(),
													violation.getMessage());
										}
									}
								}

								if (response.getInstrumentPolynomial() != null) {
									Set<ConstraintViolation<Polynomial>> stageConstraintViolations = validator
											.validate(response.getInstrumentPolynomial());
									for (ConstraintViolation<Polynomial> violation : stageConstraintViolations) {
										errors.add(Severity.ERROR, network.getCode(), network.getStartDate(),
												network.getEndDate(), station.getCode(), station.getStartDate(),
												station.getEndDate(), channel.getLocationCode(), channel.getCode(),
												channel.getStartDate(), channel.getEndDate(),
												map(violation.getPropertyPath()), violation.getInvalidValue(),
												violation.getMessage());
									}
								}

								for (ResponseStage stage : response.getStage()) {

									Set<ConstraintViolation<ResponseStage>> stageConstraintViolations = validator
											.validate(stage);
									for (ConstraintViolation<ResponseStage> violation : stageConstraintViolations) {
										errors.add(Severity.ERROR, network.getCode(), network.getStartDate(),
												network.getEndDate(), station.getCode(), station.getStartDate(),
												station.getEndDate(), channel.getLocationCode(), channel.getCode(),
												channel.getStartDate(), channel.getEndDate(),
												map(violation.getPropertyPath()), violation.getInvalidValue(),
												violation.getMessage() + "[stage:" + stage.getNumber() + "]");
									}

									if (stage.getFilters() != null) {
										for (Filter filter : stage.getFilters()) {
											Set<ConstraintViolation<Filter>> filterConstraintViolations = validator
													.validate(filter);
											for (ConstraintViolation<Filter> violation : filterConstraintViolations) {
												errors.add(Severity.ERROR, network.getCode(), network.getStartDate(),
														network.getEndDate(), station.getCode(), station.getStartDate(),
														station.getEndDate(), channel.getLocationCode(),
														channel.getCode(), channel.getStartDate(), channel.getEndDate(),
														map(violation.getPropertyPath()), violation.getInvalidValue(),
														violation.getMessage() + "[stage:" + stage.getNumber() + "]");
											}
										}
										if (stage.getDecimation() != null) {
											Set<ConstraintViolation<Decimation>> filterConstraintViolations = validator
													.validate(stage.getDecimation());
											for (ConstraintViolation<Decimation> violation : filterConstraintViolations) {
												errors.add(Severity.ERROR, network.getCode(), network.getStartDate(),
														network.getEndDate(), station.getCode(), station.getStartDate(),
														station.getEndDate(), channel.getLocationCode(),
														channel.getCode(), channel.getStartDate(), channel.getEndDate(),
														map(violation.getPropertyPath()), violation.getInvalidValue(),
														violation.getMessage());
											}
										}
										if (stage.getStageGain() != null) {
											Set<ConstraintViolation<Gain>> filterConstraintViolations = validator
													.validate(stage.getStageGain());
											for (ConstraintViolation<Gain> violation : filterConstraintViolations) {
												/////// violation.getConstraintDescriptor().getPayload();
												errors.add(Severity.ERROR, network.getCode(), network.getStartDate(),
														network.getEndDate(), station.getCode(), station.getStartDate(),
														station.getEndDate(), channel.getLocationCode(),
														channel.getCode(), channel.getStartDate(), channel.getEndDate(),
														map(violation.getPropertyPath()), violation.getInvalidValue(),
														violation.getMessage());
											}
										}
									}
								}
							}

						}
					}
				}
			}
		}
		return errors;
	}

	private String map(Path path) {
		StringBuilder builder = new StringBuilder();
		Iterator<Path.Node> iter = path.iterator();
		while (iter.hasNext()) {
			Node node = iter.next();
			builder.append(node.toString());
			if (iter.hasNext()) {
				builder.append(".");
			}
		}
		return builder.toString();
	}
}
