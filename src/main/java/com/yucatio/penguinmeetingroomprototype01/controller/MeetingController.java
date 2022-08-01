package com.yucatio.penguinmeetingroomprototype01.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yucatio.penguinmeetingroomprototype01.entity.Meeting;
import com.yucatio.penguinmeetingroomprototype01.entity.reqres.Response;
import com.yucatio.penguinmeetingroomprototype01.service.MeetingService;

@RestController
@RequestMapping("meetings")
public class MeetingController {
  MeetingService service;

  @Autowired
  public MeetingController(MeetingService service) {
    super();
    this.service = service;
  }

  @GetMapping("{id}")
  public Response<Meeting> get(@PathVariable Integer id) {
    Meeting meeting = service.selectOneById(id);

    Response<Meeting> response = new Response<>();
    response.setResultCode("N000");
    response.setResult(meeting);

    return response;
  }

  @PostMapping
  public Response<Meeting> post(@RequestBody Meeting meeting) {
    Integer newId = service.insert(meeting);

    Response<Meeting> response = new Response<>();
    response.setResultCode("N000");
    response.setNewId(newId);

    return response;
  }

  @PutMapping("{id}")
  public Response<Meeting> put(@PathVariable Integer id, @RequestBody Meeting meeting) {
    service.update(id, meeting);

    Response<Meeting> response = new Response<>();
    response.setResultCode("N000");

    return response;
  }

}
