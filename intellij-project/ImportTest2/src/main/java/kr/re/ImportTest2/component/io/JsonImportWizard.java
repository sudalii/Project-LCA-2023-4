package kr.re.ImportTest2.component.io;

import kr.re.ImportTest2.component.derdyDb.Database;
import org.openlca.jsonld.ZipStore;
import org.openlca.jsonld.input.JsonImport;
import org.openlca.jsonld.input.UpdateMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class JsonImportWizard {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private File initialFile;

    public static void of(File file) {
        Logger log = LoggerFactory.getLogger(JsonImportWizard.class);

        if (Database.isNoneActive()) {
            log.error("No Database Opened, Need Open Database");
            return;
        }
        JsonImportWizard w = new JsonImportWizard();
        w.initialFile = file;
        w.performFinish();
    }

    /**
     * method type: boolean -> void로 변경
     */
    public void performFinish() {
        var zip = initialFile;
        if (zip == null || !zip.exists())
            return;
        try {
            doRun(zip);
        } catch (Exception e) {
            log.error("JSON import failed", e);
        }
    }

    private void doRun(File zip) throws Exception {
        log.info("Import JSON LD package {}", zip);
        try (var store = ZipStore.open(zip)) {
            LibraryResolver.resolve(store.getLibraryLinks(), success -> {
                if (!success)
                    return;
                try {
                    var importer = new JsonImport(store, Database.get());
                    importer.run();
                } catch (Exception e) {
                    log.error("JSON import failed", e);
                }
            });
        }
    }

    /**
     * Contains settings for the JSON-LD import.
     */
    private final UpdateMode[] mods = {
            UpdateMode.NEVER,
            UpdateMode.IF_NEWER,
            UpdateMode.ALWAYS
    };
    UpdateMode updateMode = UpdateMode.NEVER;
    private String getText(UpdateMode mode) {
        return switch (mode) {
            case NEVER -> "Never update a data set that already exists";
            case IF_NEWER -> "Update data sets with newer versions";
            case ALWAYS -> "Overwrite all existing data sets";
        };
    }
}
