package org.example.zentrio.service;

import jakarta.validation.Valid;
import org.example.zentrio.dto.request.ReactRequest;
import org.example.zentrio.model.React;

import java.util.UUID;

public interface ReactService {
    React createReact(@Valid ReactRequest reactRequest);

    React UpdateReact(UUID reactId,  ReactRequest reactRequest);

    React GetReactById(UUID reactId);

    Void DeleteReactById(UUID reactId);
}
