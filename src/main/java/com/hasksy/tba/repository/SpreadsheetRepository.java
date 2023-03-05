package com.hasksy.tba.repository;

import com.hasksy.tba.model.spreadsheet.Spreadsheet;
import com.hasksy.tba.model.spreadsheet.SpreadsheetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpreadsheetRepository extends JpaRepository<Spreadsheet, Long> {
    Optional<Spreadsheet> findByGroupChatIdAndTypeAndPeriodMarker(long groupChatId, SpreadsheetType type, String periodMarker);

}