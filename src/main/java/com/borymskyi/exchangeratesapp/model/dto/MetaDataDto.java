package com.borymskyi.exchangeratesapp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.Instant;

@Builder
@RequiredArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MetaDataDto implements Serializable {

    private static final long serialVersionUID = -3934848262232482546L;

    @JsonProperty(value = "last_updated_at")
    Instant lastUpdatedAt;

    @JsonProperty(value = "source_of_data")
    String sourceOfData;
}
