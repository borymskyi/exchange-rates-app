package com.borymskyi.exchangeratesapp.model.pojo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MetaData {

    Instant lastUpdatedAt;

    String sourceOfData;

}