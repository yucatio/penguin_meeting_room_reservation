package com.yucatio.penguinmeetingroomprototype01.validation.group;

import javax.validation.GroupSequence;

@GroupSequence({ FieldCorrelation.class, WithDBModel.class, NonFunctional.class })
public interface ValidationOrder {

}
