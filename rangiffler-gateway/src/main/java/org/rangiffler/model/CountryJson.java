package org.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryJson {

  @JsonProperty("id")
  private UUID id;

  @JsonProperty("code")
  private String code;

  @JsonProperty("name")
  private String name;
}
