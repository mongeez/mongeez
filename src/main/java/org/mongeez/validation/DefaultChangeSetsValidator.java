package org.mongeez.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mongeez.commands.ChangeSet;

public class DefaultChangeSetsValidator implements ChangeSetsValidator {

    @Override
    public void validate(List<ChangeSet> changesets) throws ValidationException {
        changeSetIdsNotUnique(changesets);
    }

    private void changeSetIdsNotUnique(List<ChangeSet> changeSets) {
        Set<String> idSet = new HashSet<String>();
        for(ChangeSet changeSet: changeSets) {
            if (idSet.contains(changeSet.getChangeId())) {
                throw new ValidationException("ChangeSetId " + changeSet.getChangeId() + " is not unique.");
            }
            idSet.add(changeSet.getChangeId());
        }
    }
}
