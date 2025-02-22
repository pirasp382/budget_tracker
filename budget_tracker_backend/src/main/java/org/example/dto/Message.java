package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

  @Schema(
          description = "title of the Message",
          implementation = String.class,
          examples = {"Username is null"}
  )
  private String title;
}
