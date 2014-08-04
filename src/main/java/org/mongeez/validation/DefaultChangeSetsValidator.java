package org.mongeez.validation;

import java.util.List;

import org.mongeez.commands.ChangeSet;

public class DefaultChangeSetsValidator implements ChangeSetsValidator {

    @Override
    public void validate(List<ChangeSet> changesets) throws ValidationException {
        changeSetIdsNotUnique(changesets);
    }

    private void changeSetIdsNotUnique(List<ChangeSet> changeSets) {
        for (int i = 0; i < changeSets.size() / 2; i++) {
            ChangeSet changeSetI = changeSets.get(i);
            String changeSetIId = changeSetI.getChangeId();

            for (int j = i + 1; j < changeSets.size(); j++) {
                ChangeSet changeSetJ = changeSets.get(j);
                String changeSetJId = changeSetJ.getChangeId();

                if (changeSetIId.equals(changeSetJId)) {
                    throw new ValidationException("ChangeSetId " + changeSetIId + " is not unique.");
                }
            }
        }
    }
}
