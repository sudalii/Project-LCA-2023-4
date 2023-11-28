package kr.re.ImportTest2.component.io;

import kr.re.ImportTest2.component.derdyDb.Database;
import org.openlca.io.xls.process.XlsProcessReader;
import org.openlca.jsonld.input.UpdateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class ExcelImportWizard {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private File initialFiles;

    public static void of(File file) {
        Logger log = LoggerFactory.getLogger(JsonImportWizard.class);

        if (Database.isNoneActive()) {
            log.error("No Database Opened, Need Open Database");
            return;
        }
        ExcelImportWizard w = new ExcelImportWizard();
        w.initialFiles = file;
        w.performFinish();
    }

    public void performFinish() {
        List<File> files;
        files = initialFiles != null
                ? List.of(initialFiles)
                : List.of();

/*        files = initialFiles.listFiles() != null
                ? List.of(initialFiles.listFiles())
                : List.of();*/

        if (files.isEmpty()) {
            log.error("Files is empty");
            return;
        }
        try {
            var reader = XlsProcessReader.of(Database.get());
            for (var file : files) {
                reader.sync(file);
            }
        } catch (Exception e) {
            log.error("Failed to import Excel file(s)", e);
        }
    }


    private final boolean[] _updateMode = {false, true, false};

    private String updateLabel(int i) {
        return switch (i) {
            case 0 -> "Keep the version in the database";
            case 1 -> "Update it in the database if it is newer";
            case 2 -> "Always update it in the database";
            default -> "?";
        };
    }

    private UpdateMode updateMode() {
        var order = new UpdateMode[]{
                UpdateMode.NEVER,
                UpdateMode.IF_NEWER,
                UpdateMode.ALWAYS
        };
        for (int i = 0; i < order.length; i++) {
            if (_updateMode[i]) {
                return order[i];
            }
        }
        return UpdateMode.IF_NEWER;
    }
}
