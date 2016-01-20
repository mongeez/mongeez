/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package org.mongeez.reader;

import org.mongeez.commands.ChangeSet;
import org.mongeez.validation.ValidationException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.testng.annotations.Test;

import java.nio.charset.Charset;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class FormattedJavascriptChangeSetReaderTest {
    @Test
    public void testGetChangeSets1() throws Exception {
        List<ChangeSet> changeSets = parse("changeset1.js");
        assertEquals(changeSets.size(), 2);
        assertChangeSetEquals(changeSets.get(0), "mlysaght", "ChangeSet-1",
                false, "changeset1.js",
                "db.organization.insert({\n" +
                        "    \"Organization\" : \"10Gen\",\n" +
                        "    \"Location\" : \"NYC\",\n" +
                        "    DateFounded : {\"Year\" : 2008, \"Month\" : 01, \"day\" :01}\n" +
                        "});\n" +
                        "db.organization.insert({\n" +
                        "    \"Organization\" : \"SecondMarket\",\n" +
                        "    \"Location\" : \"NYC\",\n" +
                        "    DateFounded : {\"Year\" : 2004, \"Month\" : 05, \"day\" :04}\n" +
                        "});\n");
        assertChangeSetEquals(changeSets.get(1), "mlysaght", "ChangeSet-2",
                false, "changeset1.js",
                "db.user.insert({ \"Name\" : \"Michael Lysaght\"});\n" +
                        "db.user.insert({ \"Name\" : \"Oleksii Iepishkin\"});\n");
    }

    @Test
    public void testGetChangeSets2() throws Exception {
        List<ChangeSet> changeSets = parse("changeset2.js");
        assertEquals(changeSets.size(), 2);
        assertChangeSetEquals(changeSets.get(0), "someuser", "cs3", true,
                "changeset2.js",
                "db.organization.update({Location : \"NYC\"}, {$set : {Location : \"NY\"}}, false, true);\n");
        assertChangeSetEquals(changeSets.get(1), "someotheruser", "cs4",
                false, "changeset2.js",
                "db.organization.find().forEach(function(org) {\n" +
                        "    var year = org.DateFounded.Year;\n" +
                        "    var month = org.DateFounded.Month;\n" +
                        "    var day = org.DateFounded.day;\n" +
                        "    //Year is minimum required information\n" +
                        "    if (year != null) {\n" +
                        "    var date = new Date();\n" +
                        "    if (month != null) {\n" +
                        "    if (day != null) {\n" +
                        "    date.setUTCDate(day);\n" +
                        "    }\n" +
                        "date.setMonth(month - 1);\n" +
                        "}\n" +
                        "date.setFullYear(year);\n" +
                        "}\n" +
                        "if (date != null) {\n" +
                        "    db.organization.update({Organization : org.Organization}, {$set : {DateFounded : date}});\n" +
                        "}\n" +
                        "else {\n" +
                        "    db.organization.update({Organization : org.Organization}, {$unset : {DateFounded : 1 }});\n" +
                        "}\n" +
                        "});\n");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testGetChangeSetsNoHeader() throws Exception {
        parse("changeset_noheader.js");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testGetChangeSetsEmptyScript() throws Exception {
        parse("changeset_emptyscript.js");
    }

    @Test
    public void testGetChangeSetsAlternateEncoding() throws Exception {
        List<ChangeSet> changeSets = parse(Charset.forName("Cp1252"), "changeset_Cp1252.js");
        assertEquals(changeSets.size(), 2);
        assertChangeSetEquals(changeSets.get(0), "mlysaght", "ChangeSet-1",
                false, "changeset_Cp1252.js",
                "db.organization.insert({\n" +
                        "    \"Organization\" : \"10Gen\",\n" +
                        "    \"Location\" : \"NYC\",\n" +
                        "    DateFounded : {\"Year\" : 2008, \"Month\" : 01, \"day\" :01}\n" +
                        "});\n" +
                        "db.organization.insert({\n" +
                        "    \"Organization\" : \"SecondMarket\",\n" +
                        "    \"Location\" : \"NYC\",\n" +
                        "    DateFounded : {\"Year\" : 2004, \"Month\" : 05, \"day\" :04}\n" +
                        "});\n");
        assertChangeSetEquals(changeSets.get(1), "mlysaght", "ChangeSet-2",
                false, "changeset_Cp1252.js",
                "db.user.insert({ \"Name\" : \"Michaël Lyságht\"});\n" +
                        "db.user.insert({ \"Name\" : \"Oleksïï Iepishkin\"});\n");
    }

    /**
     * Verification that the file used to test encoding support is actually a
     * valid test.  For example, a file that included only ASCII characters
     * would not be a valid test of the difference between ASCII, UTF-8, and
     * Cp1252 encodings, as ASCII is a subset of the other two encodings.
     */
    @Test
    public void testGetChangeSetsWrongEncoding() throws Exception {
        List<ChangeSet> changeSets = parse("changeset_Cp1252.js");
        assertEquals(changeSets.size(), 2);
        assertChangeSetEquals(changeSets.get(0), "mlysaght", "ChangeSet-1",
                false, "changeset_Cp1252.js",
                "db.organization.insert({\n" +
                        "    \"Organization\" : \"10Gen\",\n" +
                        "    \"Location\" : \"NYC\",\n" +
                        "    DateFounded : {\"Year\" : 2008, \"Month\" : 01, \"day\" :01}\n" +
                        "});\n" +
                        "db.organization.insert({\n" +
                        "    \"Organization\" : \"SecondMarket\",\n" +
                        "    \"Location\" : \"NYC\",\n" +
                        "    DateFounded : {\"Year\" : 2004, \"Month\" : 05, \"day\" :04}\n" +
                        "});\n");
        assertChangeSetEquals(changeSets.get(1), "mlysaght", "ChangeSet-2",
                false, "changeset_Cp1252.js",
                "db.user.insert({ \"Name\" : \"Micha�l Lys�ght\"});\n" +
                        "db.user.insert({ \"Name\" : \"Oleks�� Iepishkin\"});\n");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testGetChangeSetsIOFailure() throws Exception {
        parse("changeset_nonexistant.js");
    }

    @Test
    public void testChangeSetWithContexts() throws Exception {
        List<ChangeSet> changeSets = parse("changeset_contexts.js");
        assertEquals(changeSets.size(), 5);
        assertEquals("users", changeSets.get(0).getContexts());

        assertEquals("users,organizations", changeSets.get(1).getContexts());

        assertTrue(changeSets.get(2).isRunAlways());
        assertEquals("users,organizations", changeSets.get(2).getContexts());

        assertEquals("users, organizations", changeSets.get(3).getContexts());

        assertTrue(changeSets.get(4).isRunAlways());
        assertEquals("users, organizations", changeSets.get(4).getContexts());
    }

    private List<ChangeSet> parse(String fileName) {
        return parse(null, fileName);
    }

    private List<ChangeSet> parse(Charset charset, String fileName) {
        FormattedJavascriptChangeSetReader reader = charset != null ?
                new FormattedJavascriptChangeSetReader(charset) :
                new FormattedJavascriptChangeSetReader();
        Resource file = new ClassPathResource(fileName, getClass());
        List<ChangeSet> changeSets = reader.getChangeSets(file);
        return changeSets;
    }

    private void assertChangeSetEquals(ChangeSet actual, String expectedAuthor, String expectedChangeId, boolean expectedRunAlways, String expectedFile, String expectedBody) {
        assertEquals(actual.getAuthor(), expectedAuthor);
        assertEquals(actual.getChangeId(), expectedChangeId);
        assertEquals(actual.isRunAlways(), expectedRunAlways);
        assertEquals(actual.getFile(), expectedFile);
        assertEquals(actual.getCommands().size(), 1);
        assertEquals(actual.getCommands().get(0).getBody(), expectedBody);
    }
}
