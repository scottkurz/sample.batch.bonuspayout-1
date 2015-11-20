/*
 * Copyright 2015 International Business Machines Corp.
 * 
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.websphere.samples.batch.test;

import static net.sf.expectit.filter.Filters.removeNonPrintable;
import static net.sf.expectit.matcher.Matchers.contains;
import static net.sf.expectit.matcher.Matchers.eof;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;
import net.sf.expectit.Result;


public class FirstIT {

    public static final String WIN_CMD = System.getenv("COMSPEC");
    private Expect expect;
    private Process process;

    @BeforeClass
    public static void ignoreOnNonWindows() {
        assumeTrue(WIN_CMD != null && new File(WIN_CMD).canExecute());
    }

    @Before
    public void setup() throws IOException {
        ProcessBuilder builder = new ProcessBuilder(WIN_CMD, "/Q");
        process = builder.start();
        expect = new ExpectBuilder()
                .withInputs(process.getInputStream(), process.getErrorStream())
                .withOutput(process.getOutputStream())
                .withInputFilters(removeNonPrintable())
                .withEchoInput(System.err)
                .withEchoOutput(System.out)
                .build();
    }

    @After
    public void cleanup() throws IOException, InterruptedException {
        process.destroy();
        process.waitFor();
        expect.close();
    }
    
    @Test
    public void testBP() throws Exception {
    	String submitCmd = "C:/WLPS/8557.full.1/bin/batchManager submit --batchManager=localhost:9443 " +
    "--trustSslCertificates --user=bob --password=bobpwd " + 
    			"--applicationName=batch-bonuspayout-application --jobXMLName=BonusPayoutJob " + 
    "--jobPropertiesFile=C:/git/sample.batch.bonuspayout/batch-bonuspayout-wlpcfg/" +
    			"shared/resources/runToCompletionParms.txt --wait --pollingInterval_s=2";
        expect.sendLine(submitCmd);

        expect.sendLine("exit");
        // expect the process to finish
        expect.expect(eof());
    	 

    }

    @Test
    @Ignore
    public void test() throws IOException, InterruptedException {

        System.out.println(expect.expect(contains(">")).getBefore());
        expect.sendLine("echo test-123");
        Result res = expect.expect(contains("test-123"));
        assertTrue(res.isSuccessful());
        assertFalse(res.getBefore().contains("echo"));
        assertTrue(expect.expect(contains(">")).isSuccessful());
        expect.sendLine("echo %cd%");
        System.err.println("pwd: " + expect.expect(contains("\n")).getBefore());
        expect.sendLine("exit");
        assertTrue(expect.expect(eof()).isSuccessful());
    }
}
