package edu.iris.validator.conditions;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.iris.dmc.common.utils.TimeUtil;
import edu.iris.station.model.BaseNodeType;
import edu.iris.station.model.Channel;
import edu.iris.station.model.FDSNStationXML;
import edu.iris.station.model.Network;
import edu.iris.station.model.Station;
import edu.iris.validator.rules.Message;
import edu.iris.validator.rules.Result;
import edu.iris.validator.rules.XmlUtil;

public class EpochOverlapCondition extends AbstractCondition {

	public EpochOverlapCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public Message evaluate(FDSNStationXML document) {
		if (document == null) {
			return Result.success();
		}

		if (document.getNetwork() == null || document.getNetwork().isEmpty()) {
			return Result.success();
		}

		Message result = run(document.getNetwork());
		return result;
	}

	@Override
	public Message evaluate(Network network) {
		if (network == null) {
			return Result.success();
		}

		if (network.getStations() == null || network.getStations().isEmpty()) {
			return Result.success();
		}
		Message result = run(network.getStations());
		result.setNetwork(network);
		return result;
	}

	@Override
	public Message evaluate(Station station) {
		if (station == null) {
			return Result.success();
		}

		if (station.getChannels() == null || station.getChannels().isEmpty()) {
			return Result.success();
		}
		Message result = run(station.getChannels());
		result.setStation(station);
		return result;
	}

	@Override
	public Message evaluate(Channel channel) {
		throw new IllegalArgumentException("Not supported for channels");
	}

	private Message run(List<? extends BaseNodeType> list) {

		int i = 0;
		Map<String, List<Tuple>> map = new HashMap<String, List<Tuple>>();
		for (BaseNodeType node : list) {
			String key = node.getCode();
			if (node instanceof Channel) {
				key = key + ((Channel) node).getLocationCode();
			}

			List<Tuple> tuples = map.get(key);
			if (tuples == null) {
				tuples = new ArrayList<Tuple>();
				map.put(key, tuples);
			}

			if (node instanceof Channel) {
				tuples.add(new Tuple(node.getCode(), ((Channel) node).getLocationCode(), node.getStartDate(),
						node.getEndDate(), i));
			} else {
				tuples.add(new Tuple(node.getCode(), node.getCode(), node.getStartDate(), node.getEndDate(), i));
			}

			i++;
		}

		if (!map.isEmpty()) {
			int mapsize = map.size();
			int i2 = 1;
			String description ="";
            map.keySet();
			for(String key : map.keySet()) {
				List<Tuple> tuples = map.get(key);
				List<Tuple[]> invalidRanges = checkRanges(tuples);
				StringBuilder builder = new StringBuilder();
				for (Tuple[] tuple : invalidRanges) {
					builder.append("[(").append(tuple[0].code).append("|").append(XmlUtil.toText(tuple[0].start))
							.append("|").append(XmlUtil.toText(tuple[0].end)).append(")(").append(tuple[1].code)
							.append("|").append(XmlUtil.toText(tuple[1].start)).append("|")
							.append(XmlUtil.toText(tuple[1].end)).append(")]");
				}
			    description = description + builder.toString();
				
				if (mapsize == i2 && invalidRanges != null && !invalidRanges.isEmpty()) {

					return Result.error(description);
				}
				i2 ++;
			}
		}
		return Result.success();
	}

	private List<Tuple[]> checkRanges(List<Tuple> tuples) {
		List<Tuple[]> overlappingDatePairs = new ArrayList<Tuple[]>();
		Collections.sort(tuples);
		for (int i = 1; i < tuples.size(); i++) {
			Tuple tuple1 = tuples.get(i - 1);
			Tuple tuple2 = tuples.get(i);
			if (tuple1.end == null || tuple2.start == null || TimeUtil.isAfter(tuple1.end, tuple2.start)) {
				overlappingDatePairs.add(new Tuple[] { tuple1, tuple2 });
			}
		}
		return overlappingDatePairs;
	}

	private class Tuple implements Comparable<Tuple> {
		public final ZonedDateTime start;
		private ZonedDateTime end;
		public final int index;
		public String code;
		public String location;

		public Tuple(String code, String location, ZonedDateTime start, ZonedDateTime end, int index) {
			this.code = code;
			this.location = location;
			this.start = start;
			this.end = end;
			this.index = index;
		}

		public int compareTo(Tuple other) {
			return TimeUtil.compare(start, other.start);
		}

		@Override
		public String toString() {
			return "Tuple [code=" + code + ", location=" + location + ", start=" + start + ", end=" + end + ", index="
					+ index + "]";
		}

	}

}
