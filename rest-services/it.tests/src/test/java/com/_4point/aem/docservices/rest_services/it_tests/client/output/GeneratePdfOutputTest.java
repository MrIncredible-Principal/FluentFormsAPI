package com._4point.aem.docservices.rest_services.it_tests.client.output;

import static com._4point.aem.docservices.rest_services.it_tests.TestUtils.SAMPLE_FORM_DATA_XML;
import static com._4point.aem.docservices.rest_services.it_tests.TestUtils.SAMPLE_FORM_XDP;
import static com._4point.aem.docservices.rest_services.it_tests.TestUtils.TEST_MACHINE_NAME;
import static com._4point.aem.docservices.rest_services.it_tests.TestUtils.TEST_MACHINE_PORT;
import static com._4point.aem.docservices.rest_services.it_tests.TestUtils.TEST_USER;
import static com._4point.aem.docservices.rest_services.it_tests.TestUtils.TEST_USER_PASSWORD;

import java.nio.file.Path;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com._4point.aem.docservices.rest_services.client.output.RestServicesOutputServiceAdapter;
import com._4point.aem.docservices.rest_services.it_tests.TestUtils;
import com._4point.aem.fluentforms.api.Document;
import com._4point.aem.fluentforms.api.PathOrUrl;
import com._4point.aem.fluentforms.api.output.OutputService;
import com._4point.aem.fluentforms.impl.SimpleDocumentFactoryImpl;
import com._4point.aem.fluentforms.impl.UsageContext;
import com._4point.aem.fluentforms.impl.output.OutputServiceImpl;
import com.adobe.fd.output.api.AcrobatVersion;

class GeneratePdfOutputTest {

	private static final String CRX_CONTENT_ROOT = "crx:/content/dam/formsanddocuments/sample-forms";

	private OutputService underTest;
	
	@BeforeEach
	void setUp() throws Exception {
		RestServicesOutputServiceAdapter adapter = RestServicesOutputServiceAdapter.builder()
				.machineName(TEST_MACHINE_NAME)
				.port(TEST_MACHINE_PORT)
				.basicAuthentication(TEST_USER, TEST_USER_PASSWORD)
				.useSsl(false)
				.build();

		underTest = new OutputServiceImpl(adapter, UsageContext.CLIENT_SIDE);
	}

	@Test
	@DisplayName("Test generatePdfOutput() Just Form and Data.")
	void testGeneratePdfOutput_JustFormAndData() throws Exception {
		Document pdfResult =  underTest.generatePDFOutput()
									.executeOn(SAMPLE_FORM_XDP, SimpleDocumentFactoryImpl.getFactory().create(SAMPLE_FORM_DATA_XML.toFile()));
		
		TestUtils.validatePdfResult(pdfResult.getInlineData(), "GeneratePdfOutput_JustFormAndData.pdf", false, false, false);
	}

	@Test
	@DisplayName("Test generatePdfOutput() Just Form Document and Data.")
	void testGeneratePdfOutput_JustFormDocAndData() throws Exception {
		Document pdfResult =  underTest.generatePDFOutput()
									.executeOn(SimpleDocumentFactoryImpl.getFactory().create(SAMPLE_FORM_XDP.toFile()), SimpleDocumentFactoryImpl.getFactory().create(SAMPLE_FORM_DATA_XML.toFile()));
		
		TestUtils.validatePdfResult(pdfResult.getInlineData(), "GeneratePdfOutput_JustFormAndData.pdf", false, false, false);
	}

	@Test
	@DisplayName("Test generatePdfOutput() CRX Form and Data.")
	void testGeneratePdfOutput_CRXFormAndData() throws Exception {
		Document pdfResult =  underTest.generatePDFOutput()
									.setContentRoot(PathOrUrl.from(CRX_CONTENT_ROOT))
									.executeOn(SAMPLE_FORM_XDP.getFileName(), SimpleDocumentFactoryImpl.getFactory().create(SAMPLE_FORM_DATA_XML.toFile()));
		
		TestUtils.validatePdfResult(pdfResult.getInlineData(), "GeneratePdfOutput_CRXFormAndData.pdf", false, false, false);		
	}

	@Test
	@DisplayName("Test generatePdfOutput() All Arguments.")
	void testGeneratePdfOutput_AllArgs() throws Exception {
		AcrobatVersion acrobatVersion = AcrobatVersion.Acrobat_10_1;
		Path contentRoot = SAMPLE_FORM_XDP.getParent();
//		Path debugDir = null;
		
		Document pdfResult =  underTest.generatePDFOutput()
									.setAcrobatVersion(acrobatVersion)
									.setContentRoot(contentRoot)
//									.setDebugDir(debugDir)
									.setEmbedFonts(true)
									.setLinearizedPDF(true)
									.setLocale(Locale.CANADA_FRENCH)
									.setRetainPDFFormState(true)
									.setRetainUnsignedSignatureFields(true)
									.setTaggedPDF(true)
									.executeOn(SAMPLE_FORM_XDP, SimpleDocumentFactoryImpl.getFactory().create(SAMPLE_FORM_DATA_XML.toFile()));
		
		TestUtils.validatePdfResult(pdfResult.getInlineData(), "GeneratePdfOutput_AllArgs.pdf", false, false, false);		
	}
}
