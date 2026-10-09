package edu.iris.dmc.station.conditions;

import edu.iris.dmc.DocumentMarshaller;
import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.station.RuleEngineServiceTest;
import edu.iris.dmc.station.restrictions.ChannelCodeRestriction;
import edu.iris.dmc.station.restrictions.ChannelTypeRestriction;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Success;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class Condition417Test {

    private FDSNStationXML theDocument;

    @Before
    public void init() throws Exception {

    }

    @Test
    public void fail() throws Exception {
        // dummy file with zero value with unmatched imaginary value
        try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("F1_417.xml")) {
            theDocument = DocumentMarshaller.unmarshal(is);

            Network iu = theDocument.getNetwork().get(0);
            Channel bhz00 = iu.getStations().get(0).getChannels().get(0);

            Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(),
                    new ChannelTypeRestriction() };

            ComplexConjugateCondition condition = new ComplexConjugateCondition(true, "", restrictions);

            Response response = bhz00.getResponse();
            Message result = condition.evaluate(bhz00, response);
            Assert.assertTrue(result instanceof edu.iris.dmc.station.rules.Error);
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    @Test
    public void pass() throws Exception {
        // this file includes complex conjugate values and should pass examination
        try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("AUMCQBHZ_stageNOSEQUENCE.xml")) {
            theDocument = DocumentMarshaller.unmarshal(is);

            Network iu = theDocument.getNetwork().get(0);
            Channel bhz00 = iu.getStations().get(0).getChannels().get(0);

            Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(),
                    new ChannelTypeRestriction() };

            ComplexConjugateCondition condition = new ComplexConjugateCondition(true, "", restrictions);
            Response response = bhz00.getResponse();
            Message result = condition.evaluate(bhz00, response);
            System.out.println(result);
            Assert.assertTrue(result instanceof Success);
        }

    }
}
