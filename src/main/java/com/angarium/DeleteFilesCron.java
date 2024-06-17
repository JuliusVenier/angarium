package com.angarium;

import com.angarium.service.FileService;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;
import lombok.extern.jbosslog.JBossLog;

import java.io.IOException;

@JBossLog
@RequiredArgsConstructor
@ApplicationScoped
public class DeleteFilesCron {
    private final FileService fileService;

    /**
     * Geplante Methode, die je nach Einstellung ausgeführt wird,
     * um alle Dateien zu löschen, die ihr Download-Limit erreicht haben.
     *
     * @throws IOException Wenn ein Fehler beim Löschen der Dateien auftritt.
     */
    @Scheduled(cron = "{angarium.config.delete.expr}")
    void deleteFiles() throws IOException {
        log.info("All files that have reached their limit are deleted");
        fileService.deleteFilesThatHaveReachedTheirLimit();
    }
}
