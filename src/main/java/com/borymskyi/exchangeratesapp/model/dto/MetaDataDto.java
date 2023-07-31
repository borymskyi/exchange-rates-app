package com.borymskyi.exchangeratesapp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MetaDataDto {

    @JsonProperty(value = "last_updated_at")
    Instant lastUpdatedAt;

    @JsonProperty(value = "source_of_data")
    String sourceOfData;
}
