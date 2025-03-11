package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SequencedMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GraphData {
    private List<Map<LocalDate, BigDecimal>> income;
    private List<Map<LocalDate, BigDecimal>> expenses;
    private List<Map<LocalDate, BigDecimal>> categoryData;
    private SequencedMap<LocalDate, BigDecimal> history;
}
