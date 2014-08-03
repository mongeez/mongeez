package org.mongeez.validation;

import java.util.List;


import org.mongeez.commands.ChangeSet;

public interface ChangeSetsValidator {
    public void validate(List<ChangeSet> changeSets) throws ValidationException;
}
