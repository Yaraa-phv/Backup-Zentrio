package org.example.zentrio.enums;

import lombok.Getter;

@Getter
public enum ImageExtension {

    PNG("png"),
    SVG("svg"),
    JPG("jpg"),
    JPEG("jpeg"),
    GIF("gif");

    private final String value;

    ImageExtension(String value) {
        this.value = value;
    }

    public String getExtension() {
        return this.value;
    }

}
