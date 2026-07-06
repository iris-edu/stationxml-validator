package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.*;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class ComplexConjugateCondition extends ChannelRestrictedCondition {

    private static final Logger LOGGER = Logger.getLogger(ComplexConjugateCondition.class.getName());

    private final DecimalFormat df = new DecimalFormat("#.#####");

    public ComplexConjugateCondition(boolean required, String description, Restriction... restrictions) {
        super(required, description, restrictions);
    }

    @Override
    public Message evaluate(Network network) {
        throw new IllegalArgumentException("method not supported!");
    }

    @Override
    public Message evaluate(Station station) {
        throw new IllegalArgumentException("method not supported!");
    }

    @Override
    public Message evaluate(Channel channel) {
        if (channel == null) {
            return Result.success();
        }
        return evaluate(channel, channel.getResponse());
    }

    @Override
    public Message evaluate(Channel channel, Response response) {

        if (isRestricted(channel)) {
            return Result.success();
        }
        if (this.required) {
            if (response == null) {
                return Result.error("expected response but was null");
            }
        }
        if (response.getStage() != null && !response.getStage().isEmpty()) {
            List<ResponseStage> stages = response.getStage();
            int stage = 1;
            for (ResponseStage s : stages) {
                if (s.getPolesZeros() != null) {
                    if (s.getPolesZeros().getZero() != null) {
                        Map<Double, Set<Double>> conjugatePairMap = new ConcurrentHashMap<>();
                        // first, collect potential conjugate pairs
                        for (PoleZero z : s.getPolesZeros().getZero()) {
                            double realValue = z.getReal().getValue();
                            double imaginaryValue = z.getReal().getValue();
                            if (imaginaryValue == 0.) {
                                continue;
                            }
                            if (!conjugatePairMap.containsKey(z.getReal().getValue())) {
                                conjugatePairMap.put(realValue, new HashSet<>());
                            }
                            conjugatePairMap.get(realValue).add(imaginaryValue);
                        }
                        // now, check existence of conjugate pairs
                        for (Double key : conjugatePairMap.keySet()) {
                            Set<Double> pairs = conjugatePairMap.get(key);
                            for (Double imaginaryValue : pairs) {
                                Double conjugate = -1 * imaginaryValue;
                                if (!pairs.contains(conjugate)){
                                    // stage 2 zero with real term -2.31400 and nonzero imaginary term -3.17000 has no conjugate pair
                                    return Result.error("stage " + s.getNumber() + " zero with real term " +
                                            df.format(key) + " and nonzero imaginary term " +
                                            df.format(imaginaryValue) + " has no conjugate pair");
                                }
                            }
                        }
                    }
                    if (s.getPolesZeros().getPole() != null) {
                        Map<Double, Set<Double>> conjugatePairMap = new ConcurrentHashMap<>();
                        // first, collect potential conjugate pairs
                        for (PoleZero p : s.getPolesZeros().getPole()) {
                            double realValue = p.getReal().getValue();
                            double imaginaryValue = p.getReal().getValue();
                            if (imaginaryValue == 0.) {
                                continue;
                            }
                            if (!conjugatePairMap.containsKey(p.getReal().getValue())) {
                                conjugatePairMap.put(realValue, new HashSet<>());
                            }
                            conjugatePairMap.get(realValue).add(imaginaryValue);
                        }
                        // now, check existence of conjugate pairs
                        for (Double key : conjugatePairMap.keySet()) {
                            Set<Double> pairs = conjugatePairMap.get(key);
                            for (Double imaginaryValue : pairs) {
                                Double conjugate = -1 * imaginaryValue;
                                if (!pairs.contains(conjugate)){
                                    // stage 2 pole with real term -2.31400 and nonzero imaginary term -3.17000 has no conjugate pair
                                    return Result.error("stage " + s.getNumber() + " pole with real term " +
                                            df.format(key) + " and nonzero imaginary term " +
                                            df.format(imaginaryValue) + " has no conjugate pair");
                                }
                            }
                        }
                    }
                }
                stage++;
            }

        }

        return Result.success();
    }

}
