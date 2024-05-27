package kr.re.ImportTest2.component.derdyDb;

import kr.re.ImportTest2.component.io.ExcelImportWizard;
import kr.re.ImportTest2.component.io.JsonImportWizard;
import org.openlca.io.Format;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FileImport {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    public static String syncedName;

    public void run(String path) {

        if (path == null) {   // 외부에서 받아올때만 사용
            log.error("Path is null... no run.");
            return;
        }
        var file = new File(path);
        if (!file.exists() || file.isDirectory()) {
//        if (!file.exists()) {
            log.error("Not a file path={} is not an existing file.", path);
            return;
        }

        // handle script files
        var name = file.getName().toLowerCase();
/*        if (name.endsWith(".py")
                || name.endsWith(".sql")) {
            SaveScriptDialog.forImportOf(file);
            return;
        }*/

        // check if it is a known import format
        var format = Format.detect(file).orElse(null);
        if (format != null) {
            handleFormat(file, format);
            return;
        }
        log.info("Unknown format, openLCA could not detect the format of the file = {}.", file.getName());
    }

    /**
     * excel: .xlsx만 인식 - 여러 개의 파일은 for문으로 넣어야 함
     */
    private void handleFormat(File file, Format format) {
        switch (format) {
            case EXCEL -> {
                ExcelImportWizard.of(file);
                syncedName = ExcelImportWizard.syncedName;
            }
            case JSON_LD_ZIP -> JsonImportWizard.of(file);
//            case ZOLCA -> importZOLCA(file);
            default -> log.error(
                    "No import found Format '{}' was detected but no import wizard could be found.",
                    format);
        }
    }

/*    private void importZOLCA(File file) {
        var db = Database.get();
        if (db == null) {
            var b = log.info("Import database?",
                    "Import file '" + file.getName() + "' as new database?");
            if (b) {
                DbRestoreAction.run(file);
            }
            return;
        }
        new FileImport.ZolcaImportDialog(file, db).open();
    }

    private static class ZolcaImportDialog {

        private final File zolca;
        private final IDatabase activeDB;
        private boolean intoActiveDB = false;

        ZolcaImportDialog(File zolca, IDatabase activeDB) {
            this.zolca = zolca;
            this.activeDB = activeDB;
        }

        @Override
        protected void okPressed() {
            if (intoActiveDB) {
                DbImportWizard.of(zolca);
            } else {
                DbRestoreAction.run(zolca);
            }
            super.okPressed();
        }
    }*/
}
