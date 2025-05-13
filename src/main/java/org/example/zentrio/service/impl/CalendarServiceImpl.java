package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.response.CalendarTaskResponse;
import org.example.zentrio.model.AppUser;
import org.example.zentrio.repository.CalendarRepository;
import org.example.zentrio.service.CalendarService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {
    private final CalendarRepository calendarRepository;
    @Override
    public List<CalendarTaskResponse> getCalendarForCurrentUser() {
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        return calendarRepository.getCalendarTasksForUser(userId);
    }
}
