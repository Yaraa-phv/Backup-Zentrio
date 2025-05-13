package org.example.zentrio.service;

import org.example.zentrio.dto.response.CalendarTaskResponse;

import java.util.List;

public interface CalendarService {
    List<CalendarTaskResponse> getCalendarForCurrentUser();
}
