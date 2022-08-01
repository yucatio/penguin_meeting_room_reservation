package com.yucatio.penguinmeetingroomprototype01.service;

import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yucatio.penguinmeetingroomprototype01.entity.Meeting;
import com.yucatio.penguinmeetingroomprototype01.entity.MeetingTransitionModel;
import com.yucatio.penguinmeetingroomprototype01.exception.MeetingNotFoundException;
import com.yucatio.penguinmeetingroomprototype01.exception.ValidationException;
import com.yucatio.penguinmeetingroomprototype01.repository.MeetingRepository;
import com.yucatio.penguinmeetingroomprototype01.util.BeanUtil;
import com.yucatio.penguinmeetingroomprototype01.validation.group.InsertOnly;
import com.yucatio.penguinmeetingroomprototype01.validation.group.Single;
import com.yucatio.penguinmeetingroomprototype01.validation.group.UpdateOnly;
import com.yucatio.penguinmeetingroomprototype01.validation.group.ValidationOrder;

@Service
public class MeetingServiceImpl implements MeetingService {
  MeetingRepository meetingRepository;

  Validator validator;

  @Autowired
  public MeetingServiceImpl(MeetingRepository meetingRepository, Validator validator) {
    super();
    this.meetingRepository = meetingRepository;
    this.validator = validator;
  }

  @Override
  public Meeting selectOneById(Integer id) {
    Optional<Meeting> meeting = meetingRepository.selectOneById(id);

    return meeting.orElseThrow(() -> new MeetingNotFoundException());
  }

  @Override
  public Integer insert(Meeting meeting) {
    meeting.setId(null);

    // Single field validation
    Set<ConstraintViolation<Meeting>> errors1 = validator.validate(meeting, InsertOnly.class, Single.class);

    if (!errors1.isEmpty()) {
      throw new ValidationException(errors1);
    }

    MeetingTransitionModel transitionModel = new MeetingTransitionModel(meeting, null, meeting);
    Set<ConstraintViolation<MeetingTransitionModel>> errors2 = validator.validate(transitionModel,
        ValidationOrder.class);

    if (!errors2.isEmpty()) {
      throw new ValidationException(errors2);
    }

    return meetingRepository.insert(meeting);
  }

  @Override
  public void update(Integer id, Meeting meeting) {
    meeting.setId(id);

    // Single field validation
    Set<ConstraintViolation<Meeting>> errors1 = validator.validate(meeting, UpdateOnly.class, Single.class);

    if (!errors1.isEmpty()) {
      throw new ValidationException(errors1);
    }

    Optional<Meeting> dbMeetingOption = meetingRepository.selectOneById(id);

    Meeting dbMeeting = dbMeetingOption.orElseThrow(() -> new MeetingNotFoundException());

    // Copy
    Meeting merged = SerializationUtils.clone(dbMeeting);

    // Create merged model
    // Overwrite properties except for null
    BeanUtils.copyProperties(meeting, merged, BeanUtil.getNullPropertyNames(meeting));

    // Validation
    MeetingTransitionModel transitionModel = new MeetingTransitionModel(merged, dbMeeting, meeting);

    Set<ConstraintViolation<MeetingTransitionModel>> errors2 = validator.validate(transitionModel,
        ValidationOrder.class);

    if (!errors2.isEmpty()) {
      throw new ValidationException(errors2);
    }

    // Update
    meetingRepository.update(merged);
  }

}
