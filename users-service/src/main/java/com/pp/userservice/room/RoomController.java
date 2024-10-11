package com.pp.userservice.room;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = RoomController.ROOM_ENDPOINT)
public class RoomController {
    static final String ROOM_ENDPOINT = "/rooms";
}
