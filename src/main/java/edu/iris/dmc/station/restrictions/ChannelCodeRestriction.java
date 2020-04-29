package edu.iris.dmc.station.restrictions;

import java.util.Arrays;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Response;

public class ChannelCodeRestriction implements Restriction {

	private String name;
	private String[] codes = new String[] { "ACE", "ATC", "EX1", "EX2", "EX3", "EX4", "EX5", "EX6", "EX7", "EX8",
			"EX9", "GAN", "GEL", "GLA", "GLO", "GNS", "GPL", "GPS", "GST", "LCA", "LCB", "LCC", "LCD", "LCE", "LCF",
			"LCG", "LCH", "LCI", "LCJ", "LCK", "LCL", "LCM", "LCN", "LCO", "LCP", "LCQ", "LCR", "LCS", "LCT", "LCU",
			"LCV", "LCW", "LCX", "LCY", "LCZ", "LDE", "LDN", "LDZ", "LEE", "LED", "LEO", "LEP", "LII", "LKI",
			"LOG", "LPL", "OAC", "OCF", "QBD", "QBP", "QDG", "QDL", "QDR", "QEF", "QG1", "QGD", "QID", "QLD", "QPD",
			"QRD", "QRT", "QTP", "QTH", "QTP", "QWD", "SBT", "SCA", "SCB", "SCC", "SCD", "SCE", "SCF", "SCG", "SCH",
			"SCI", "SCJ", "SCK", "SCL", "SCM", "SCN", "SCO", "SCP", "SCQ", "SCR", "SCS", "SCT", "SCU", "SCV", "SCW",
			"SCX", "SCY", "SCZ", "SDG", "SDL", "SDT", "SIO", "SOH", "SMD", "SNI", "SPK", "SPO", "SRD", "SSL", "SSQ",
			"STH", "SWR", "TSA", "TSB", "TSC", "TSD", "TSE", "TSF", "TSG", "TSH", "TSI", "TSJ", "TSK", "TSL", "TSM",
			"TSN", "TSO", "TSP", "TSQ", "TSR", "TSS", "TST", "TSU", "TSV", "TSW", "TSX", "TSY", "TSZ", "TS0", "TS1",
			"TS2", "TS3", "TS4", "TS5", "TS6", "TS7", "TS8", "TS9", "VAP", "VCE", "VCO", "VCQ", "VDT", "VEA", "VEB",
			"VEC", "VED", "VEE", "VEF", "VEG", "VEH", "VEI", "VEJ", "VEK", "VEL", "VEM", "VEN", "VEO", "VEP", "VEQ",
			"VER", "VES", "VET", "VEU", "VEV", "VEW", "VEX", "VEY", "VEZ", "VFP", "VKI", "VPB", "VMA", "VMB", "VMC",
			"VMD", "VME", "VMF", "VMG", "VMH", "VMI", "VMJ", "VMK", "VML", "VMM", "VMN", "VMO", "VMP", "VMQ", "VMR",
			"VMS", "VMT", "VMU", "VMV", "VMW", "VMX", "VMY", "VMZ", "VM0", "VM1", "VM2", "VM3", "VM4", "VM5", "VM6",
			"VM7", "VM8", "VM9", "VPB" };

	public ChannelCodeRestriction() {
		this("CodeRestriction");
	}

	public ChannelCodeRestriction(String name) {
		this.name = name;
	}

	@Override
	public boolean qualifies(Channel channel) {
		if (channel == null || channel.getCode() == null) {
			throw new IllegalArgumentException("Channel|code cannot be null");
		}
		for (String code : codes) {
			if (code.equals(channel.getCode())) {
				return true;
			}
		}
		return false;
	}

	public boolean qualifies(String channelCode) {
		if (channelCode == null) {
			throw new IllegalArgumentException("Channel|code cannot be null");
		}
		for (String code : codes) {
			if (code.equals(channelCode)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean qualifies(Response response) {
		return false;
	}

	@Override
	public String toString() {
		return "ChannelCodeRestriction [name=" + name + ", codes=" + Arrays.toString(codes) + "]";
	}

}
