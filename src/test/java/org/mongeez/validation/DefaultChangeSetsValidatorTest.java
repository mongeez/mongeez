package org.mongeez.validation;

import java.util.ArrayList;
import java.util.List;

import org.mongeez.commands.ChangeSet;
import org.testng.annotations.Test;

public class DefaultChangeSetsValidatorTest {

    @Test(expectedExceptions = ValidationException.class)
    public void testDetectDuplicateFirstHalf() throws Exception {
        DefaultChangeSetsValidator validator = new DefaultChangeSetsValidator();

        List<ChangeSet> changeSets = new ArrayList<ChangeSet>();

        changeSets.add(makeChangeSet("1"));
        changeSets.add(makeChangeSet("10"));
        changeSets.add(makeChangeSet("3"));
        changeSets.add(makeChangeSet("4"));
        changeSets.add(makeChangeSet("5"));
        changeSets.add(makeChangeSet("6"));
        changeSets.add(makeChangeSet("7"));
        changeSets.add(makeChangeSet("8"));
        changeSets.add(makeChangeSet("9"));
        changeSets.add(makeChangeSet("10"));
        validator.validate(changeSets);
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testDetectDuplicateSecondHalf() throws Exception {
        DefaultChangeSetsValidator validator = new DefaultChangeSetsValidator();

        List<ChangeSet> changeSets = new ArrayList<ChangeSet>();

        changeSets.add(makeChangeSet("1"));
        changeSets.add(makeChangeSet("2"));
        changeSets.add(makeChangeSet("3"));
        changeSets.add(makeChangeSet("4"));
        changeSets.add(makeChangeSet("5"));
        changeSets.add(makeChangeSet("6"));
        changeSets.add(makeChangeSet("7"));
        changeSets.add(makeChangeSet("8"));
        changeSets.add(makeChangeSet("10"));
        changeSets.add(makeChangeSet("10"));
        validator.validate(changeSets);
    }

    @Test
    public void testValidateNoDuplicates() throws Exception {
        DefaultChangeSetsValidator validator = new DefaultChangeSetsValidator();

        List<ChangeSet> changeSets = new ArrayList<ChangeSet>();

        changeSets.add(makeChangeSet("1"));
        changeSets.add(makeChangeSet("2"));
        changeSets.add(makeChangeSet("3"));
        changeSets.add(makeChangeSet("4"));
        changeSets.add(makeChangeSet("5"));
        changeSets.add(makeChangeSet("6"));
        changeSets.add(makeChangeSet("7"));
        changeSets.add(makeChangeSet("8"));
        changeSets.add(makeChangeSet("9"));
        changeSets.add(makeChangeSet("10"));
        validator.validate(changeSets);
    }

    private ChangeSet makeChangeSet(String id) {
        ChangeSet changeSet = new ChangeSet();
        changeSet.setChangeId(id);
        return changeSet;
    }

}