package org.example.zentrio.service;

import jakarta.validation.Valid;
import org.example.zentrio.dto.request.ReactRequest;
import org.example.zentrio.model.React;

public interface ReactService {
    React createReact(@Valid ReactRequest reactRequest);
}
