package com.ehrbridge.hospital.dto.consent;

import java.util.Date;

import jakarta.annotation.sql.DataSourceDefinition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsentPermission {
    public DateRange dateRange;
    public Date consent_validity;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class DateRange {
    public Date from;
    public Date to;
}