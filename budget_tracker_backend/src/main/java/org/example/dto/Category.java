package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.util.ColorUtil;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
  private String title;
  @Builder.Default private String description = "";
  @Builder.Default private String color = ColorUtil.generateRandomColor();
  @Builder.Default private String icon = "";
}
