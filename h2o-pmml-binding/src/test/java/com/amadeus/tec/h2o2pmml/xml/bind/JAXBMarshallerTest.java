package com.amadeus.tec.h2o2pmml.xml.bind;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.math.BigInteger;

import org.dmg.pmml_4_3.*;

public class JAXBMarshallerTest {
	
	/*
	 * TreeModel example from http://dmg.org/pmml/v4-3/TreeModel.html
	 */
	public static final String TREE_MODEL_SIMPLE = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" + 
			"<PMML version=\"4.3\" xmlns=\"http://www.dmg.org/PMML-4_3\">\n" + 
			"    <Header copyright=\"www.dmg.org\" description=\"A very small binary tree model to show structure.\"/>\n" + 
			"    <DataDictionary numberOfFields=\"5\">\n" + 
			"        <DataField name=\"temperature\" optype=\"continuous\" dataType=\"double\"/>\n" + 
			"        <DataField name=\"humidity\" optype=\"continuous\" dataType=\"double\"/>\n" + 
			"        <DataField name=\"windy\" optype=\"categorical\" dataType=\"string\">\n" + 
			"            <Value value=\"true\"/>\n" + 
			"            <Value value=\"false\"/>\n" + 
			"        </DataField>\n" + 
			"        <DataField name=\"outlook\" optype=\"categorical\" dataType=\"string\">\n" + 
			"            <Value value=\"sunny\"/>\n" + 
			"            <Value value=\"overcast\"/>\n" + 
			"            <Value value=\"rain\"/>\n" + 
			"        </DataField>\n" + 
			"        <DataField name=\"whatIdo\" optype=\"categorical\" dataType=\"string\">\n" + 
			"            <Value value=\"will play\"/>\n" + 
			"            <Value value=\"may play\"/>\n" + 
			"            <Value value=\"no play\"/>\n" + 
			"        </DataField>\n" + 
			"    </DataDictionary>\n" + 
			"    <TreeModel modelName=\"golfing\" functionName=\"classification\">\n" + 
			"        <MiningSchema>\n" + 
			"            <MiningField name=\"temperature\"/>\n" + 
			"            <MiningField name=\"humidity\"/>\n" + 
			"            <MiningField name=\"windy\"/>\n" + 
			"            <MiningField name=\"outlook\"/>\n" + 
			"            <MiningField name=\"whatIdo\" usageType=\"target\"/>\n" + 
			"        </MiningSchema>\n" + 
			"        <Node score=\"will play\">\n" + 
			"            <True/>\n" + 
			"            <Node score=\"will play\">\n" + 
			"                <SimplePredicate field=\"outlook\" operator=\"equal\" value=\"sunny\"/>\n" + 
			"                <Node score=\"will play\">\n" + 
			"                    <CompoundPredicate booleanOperator=\"and\">\n" + 
			"                        <SimplePredicate field=\"temperature\" operator=\"lessThan\" value=\"90\"/>\n" + 
			"                        <SimplePredicate field=\"temperature\" operator=\"greaterThan\" value=\"50\"/>\n" + 
			"                    </CompoundPredicate>\n" + 
			"                    <Node score=\"will play\">\n" + 
			"                        <SimplePredicate field=\"humidity\" operator=\"lessThan\" value=\"80\"/>\n" + 
			"                    </Node>\n" + 
			"                    <Node score=\"no play\">\n" + 
			"                        <SimplePredicate field=\"humidity\" operator=\"greaterOrEqual\" value=\"80\"/>\n" + 
			"                    </Node>\n" + 
			"                </Node>\n" + 
			"                <Node score=\"no play\">\n" + 
			"                    <CompoundPredicate booleanOperator=\"or\">\n" + 
			"                        <SimplePredicate field=\"temperature\" operator=\"greaterOrEqual\" value=\"90\"/>\n" + 
			"                        <SimplePredicate field=\"temperature\" operator=\"lessOrEqual\" value=\"50\"/>\n" + 
			"                    </CompoundPredicate>\n" + 
			"                </Node>\n" + 
			"            </Node>\n" + 
			"            <Node score=\"may play\">\n" + 
			"                <CompoundPredicate booleanOperator=\"or\">\n" + 
			"                    <SimplePredicate field=\"outlook\" operator=\"equal\" value=\"overcast\"/>\n" + 
			"                    <SimplePredicate field=\"outlook\" operator=\"equal\" value=\"rain\"/>\n" + 
			"                </CompoundPredicate>\n" + 
			"                <Node score=\"may play\">\n" + 
			"                    <CompoundPredicate booleanOperator=\"and\">\n" + 
			"                        <SimplePredicate field=\"temperature\" operator=\"greaterThan\" value=\"60\"/>\n" + 
			"                        <SimplePredicate field=\"temperature\" operator=\"lessThan\" value=\"100\"/>\n" + 
			"                        <SimplePredicate field=\"outlook\" operator=\"equal\" value=\"overcast\"/>\n" + 
			"                        <SimplePredicate field=\"humidity\" operator=\"lessThan\" value=\"70\"/>\n" + 
			"                        <SimplePredicate field=\"windy\" operator=\"equal\" value=\"false\"/>\n" + 
			"                    </CompoundPredicate>\n" + 
			"                </Node>\n" + 
			"                <Node score=\"no play\">\n" + 
			"                    <CompoundPredicate booleanOperator=\"and\">\n" + 
			"                        <SimplePredicate field=\"outlook\" operator=\"equal\" value=\"rain\"/>\n" + 
			"                        <SimplePredicate field=\"humidity\" operator=\"lessThan\" value=\"70\"/>\n" + 
			"                    </CompoundPredicate>\n" + 
			"                </Node>\n" + 
			"            </Node>\n" + 
			"        </Node>\n" + 
			"    </TreeModel>\n" + 
			"</PMML>";
	
	public static final PMML getTreeModelSimple() {
		return new PMML().withVersion("4.3")
				.withHeader(new Header().withCopyright("www.dmg.org").withDescription("A very small binary tree model to show structure."))
				.withDataDictionary(new DataDictionary()
						.withNumberOfFields(BigInteger.valueOf(5))
						.withDataField(new DataField().withName("temperature").withOptype(OPTYPE.CONTINUOUS).withDataType(DATATYPE.DOUBLE),
								new DataField().withName("humidity").withOptype(OPTYPE.CONTINUOUS).withDataType(DATATYPE.DOUBLE),
								new DataField().withName("windy").withOptype(OPTYPE.CATEGORICAL).withDataType(DATATYPE.STRING).withValue(new Value().withValue("true"), new Value().withValue("false")),
								new DataField().withName("outlook").withOptype(OPTYPE.CATEGORICAL).withDataType(DATATYPE.STRING).withValue(new Value().withValue("sunny"), new Value().withValue("overcast"), new Value().withValue("rain")),
								new DataField().withName("whatIdo").withOptype(OPTYPE.CATEGORICAL).withDataType(DATATYPE.STRING).withValue(new Value().withValue("will play"), new Value().withValue("may play"), new Value().withValue("no play"))))
				.withAssociationModelOrBayesianNetworkModelOrBaselineModel(new TreeModel()
						.withModelName("golfing")
						.withFunctionName(MININGFUNCTION.CLASSIFICATION)
						.withContent(
								new MiningSchema().withMiningField(
										new MiningField().withName("temperature"),
										new MiningField().withName("humidity"),
										new MiningField().withName("windy"),
										new MiningField().withName("outlook"),
										new MiningField().withName("whatIdo").withUsageType(FIELDUSAGETYPE.TARGET)),
								new Node().withScore("will play")
									.withContent(
											new True(),
											new Node().withScore("will play")
												.withContent(
														new SimplePredicate().withField("outlook").withOperator("equal").withValue("sunny"),
														new Node().withScore("will play")
															.withContent(
																	new CompoundPredicate().withBooleanOperator("and")
																		.withSimplePredicateOrCompoundPredicateOrSimpleSetPredicate(
																				new SimplePredicate().withField("temperature").withOperator("lessThan").withValue("90"),
																				new SimplePredicate().withField("temperature").withOperator("greaterThan").withValue("50")),
																	new Node().withScore("will play")
																		.withContent(
																				new SimplePredicate().withField("humidity").withOperator("lessThan").withValue("80")),
																	new Node().withScore("no play")
																		.withContent(
																				new SimplePredicate().withField("humidity").withOperator("greaterOrEqual").withValue("80"))),
															new Node().withScore("no play")
																.withContent(
																	new CompoundPredicate().withBooleanOperator("or")
																		.withSimplePredicateOrCompoundPredicateOrSimpleSetPredicate(
																				new SimplePredicate().withField("temperature").withOperator("greaterOrEqual").withValue("90"),
																				new SimplePredicate().withField("temperature").withOperator("lessOrEqual").withValue("50")))),
											new Node().withScore("may play")
												.withContent(
														new CompoundPredicate().withBooleanOperator("or")
															.withSimplePredicateOrCompoundPredicateOrSimpleSetPredicate(
																	new SimplePredicate().withField("outlook").withOperator("equal").withValue("overcast"),
																	new SimplePredicate().withField("outlook").withOperator("equal").withValue("rain")),
														new Node().withScore("may play")
															.withContent(
																	new CompoundPredicate().withBooleanOperator("and")
																		.withSimplePredicateOrCompoundPredicateOrSimpleSetPredicate(
																				new SimplePredicate().withField("temperature").withOperator("greaterThan").withValue("60"),
																				new SimplePredicate().withField("temperature").withOperator("lessThan").withValue("100"),
																				new SimplePredicate().withField("outlook").withOperator("equal").withValue("overcast"),
																				new SimplePredicate().withField("humidity").withOperator("lessThan").withValue("70"),
																				new SimplePredicate().withField("windy").withOperator("equal").withValue("false"))),
														new Node().withScore("no play")
															.withContent(
																	new CompoundPredicate().withBooleanOperator("and")
																		.withSimplePredicateOrCompoundPredicateOrSimpleSetPredicate(
																			new SimplePredicate().withField("outlook").withOperator("equal").withValue("rain"),
																			new SimplePredicate().withField("humidity").withOperator("lessThan").withValue("70")))))));
	}
	
	@Test
	public void h2oJAXBtoString() {
		PMML pmml = getTreeModelSimple();
		StringWriter stringWriter = new StringWriter();
		JAXBMarshaller.marshal(pmml, stringWriter);
		assertEquals(TREE_MODEL_SIMPLE, stringWriter.toString().trim());
	}

}
