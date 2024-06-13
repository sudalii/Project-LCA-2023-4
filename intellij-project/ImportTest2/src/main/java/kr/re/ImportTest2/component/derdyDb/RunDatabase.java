package kr.re.ImportTest2.component.derdyDb;

import org.openlca.core.DataDir;
import org.openlca.core.database.*;
import org.openlca.core.database.config.DerbyConfig;
import org.openlca.core.model.*;
import org.openlca.core.model.Process;
import org.openlca.nativelib.NativeLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class RunDatabase {

    private final Logger log = LoggerFactory.getLogger(RunDatabase.class);

    private String dbName = null;
    private static IDatabase db = null;

    /**
     * Front Controller와 연결되는 변수들
     *  - TARGET_AMOUNT
     *  - PRODUCT_NAME
     *  - SELECTED_PROCESSES
     *  - METHOD (고정이었나?) -> pass
     *  - CATEGORY (GWP, Water use, Resource depletion) -> 전부 출력
     *  -
     */

    // return: db
    public IDatabase runDb() {
        NativeLib.loadFrom(DataDir.get().root());

//        removeAllDb();  // *** Test 시 사용 ***

        // db name: 사용자 입력 받을건지, 내부에서 임의로 돌아갈건지.
        // db name은 ... 특별한 문제가 없다면 "user"로 간주,
        // 현재는 product 단위, 로그인 X 이므로 일단 하나로 쭉 하고, 필요시 삭제 기능 사용
        dbName = "user01";

        // db config 설정
        DatabaseWizard databaseWizard = new DatabaseWizard();

        // true: 기존에 생성된 db / false: 기존에 없는 db
        boolean exists = isExist(dbName);

        // db activate
        databaseWizard.runDatabase(true, dbName);

        // db open
//        IDatabase db = Database.get();
        db = DataDir.get().openDatabase(dbName);

        log.info("imported db list = {}, {}", new ProcessDao(db).getAll().size(), new ProcessDao(db).getAll());
        // *** Test 시 사용 ***, 향후 계산 완료 시점으로 이동해서 userProcess만 지우기


        // 기존에 없는 db일 경우 db files import
        try {
//            pdTestFileImport();
//            db.clear();
            if (!exists) {
                lciaFileImport();
                koreaDbFileImport();
                return db;
            }

            else if (checkImportedKoreaDb()) {
                log.info("db has already been imported.");
                saveOutputPrint();
                return db;
            }
            else if (!checkImportedKoreaDb()) {
                koreaDbFileImport();
            }

        } catch (Exception e) { // db import 도중 문제가 생기면 clear 후 다시 import
            try {
                db.clear();
                lciaFileImport();
                koreaDbFileImport();
                log.info("because {} db is not imported, clear and re-create.", dbName);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
//        }
        saveOutputPrint();
        return db;
    }

    /**
     * 국가 DB는 모두 import가 되어야 하므로 하나라도 없으면 false, 모두 있으면 true return
     */
    private boolean checkImportedKoreaDb() {
//        String savePath = "D:/Dropbox/2022-KETI/01-Project/01-EXE/산업부-KEIT-리사이클링/02-수행/06-2024/2024-01-SW개발/save_to_txt/koreaLciDbNames.txt";
        String home = System.getProperty("user.home");
        String prePath = home + "/server/txt";
        File file = new File(prePath);
        if (file.mkdirs())
            log.info("create /server/txt/ directory.");
        String savePath = prePath + "/koreaLciDbNames.txt";

        if (Files.exists(Paths.get(savePath))) {
            try (BufferedReader br = new BufferedReader(new FileReader(savePath))) {
                String name;
                while ((name = br.readLine()) != null) {    // 한 줄씩 읽기
                    Process p = new ProcessDao(db).getForName(name).get(0);    // 해당 name의 process가 없으면 true 적재
                    if (p == null)
                        return false;   // 국가DB는 모두 import가 되어야 하므로 하나라도 없으면 false 리턴
                    else if (p.quantitativeReference == null)
                        return false;
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;   // 파일 자체가 없으면 false
    }

    // 없앨수도 있을 것 같음
    public void closeDb() throws IOException {
//        db.clear();
        db.close();
        db = null;
    }

    private boolean isExist(String dbName) {
        return Database.getConfigurations().nameExists(dbName);
    }

    private void lciaFileImport() {
        String home = System.getProperty("user.home");
        String prePath = home + "/server/refDb";
//        String prePath = "D:/Dropbox/2022-KETI/01-Project/01-EXE/산업부-KEIT-리사이클링/02-수행/05-2023/2023-결과물관련";
        String lciaPath = prePath + "/ref/openlca2_v221_20230426.zip";
//        String lciaPath = prePath + "/openlca2_v221_20230426_without_flows.zip";

        FileImport f = new FileImport();

        long beforeTime = System.currentTimeMillis(); // 코드 실행 전에 시간 받아오기
        log.info("importing lcia db...");
        f.run(lciaPath);

        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long diffTime = (afterTime - beforeTime)/1000; // 두 개의 실행 시간
        log.info("execute time(sec): {}", diffTime);
    }

    private void koreaDbFileImport() {
        String home = System.getProperty("user.home");
        String prePath = home + "/server/refDb";
//        String prePath = "D:/Dropbox/2022-KETI/01-Project/01-EXE/산업부-KEIT-리사이클링/02-수행/05-2023/2023-결과물관련";
//        String koreaDbPath = prePath + "/exported";
        String koreaDbPath = prePath + "/koreaDb";
        String pdTest = prePath + "/pdTest";

        FileImport f = new FileImport();

        long beforeTime = System.currentTimeMillis(); // 코드 실행 전에 시간 받아오기
        File excelFiles = new File(koreaDbPath);

        List<String> syncedNames = new ArrayList<>();
        log.info("importing korea db...");
        for (String file : Objects.requireNonNull(excelFiles.list())) {
            String path = koreaDbPath + "/" + file;
            log.info("path={}", path);
            f.run(path);
            syncedNames.add(FileImport.syncedName);
        }
        saveSyncedProcessName(syncedNames); // RunDatabase에서 국가DB가 모두 import 된 상태인지 확인을 위한 txt 저장

        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long diffTime = (afterTime - beforeTime)/1000; // 두 개의 실행 시간
        log.info("execute time(sec): {}", diffTime);
    }

    private void pdTestFileImport() {
//        db.clear();

        String prePath = "D:/Dropbox/2022-KETI/01-Project/01-EXE/산업부-KEIT-리사이클링/02-수행/05-2023/2023-결과물관련";
        String pdTest = prePath + "/pdTest/processes.zip";

        FileImport f = new FileImport();

        long beforeTime = System.currentTimeMillis(); // 코드 실행 전에 시간 받아오기
        log.info("importing pdTest db...");
        f.run(pdTest);

        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long diffTime = (afterTime - beforeTime)/1000; // 두 개의 실행 시간
        log.info("execute time(sec): {}", diffTime);
    }

    public void saveSyncedProcessName(List<String> syncedNames) {
//        String savePath = "D:/Dropbox/2022-KETI/01-Project/01-EXE/산업부-KEIT-리사이클링/02-수행/06-2024/2024-01-SW개발/save_to_txt/koreaLciDbNames.txt";
        String home = System.getProperty("user.home");
        String prePath = home + "/server/txt";
        File file = new File(prePath);
        if (file.mkdirs())
            log.info("create /server/txt/ directory.");
        String savePath = prePath + "/koreaLciDbNames.txt";

        if (!Files.exists(Paths.get(savePath))) {
            try (FileWriter fw = new FileWriter(savePath, true);
                 PrintWriter pw = new PrintWriter(fw)) {
                for (String name : syncedNames) {
                    if (name != null)
                        pw.println(name);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void notInDirButRegistered(String[] dbNames) {
        log.info("Before Database.getConfigurations={}", Database.getConfigurations().getDerbyConfigs());
        List<String> list = new ArrayList<>(Arrays.asList(dbNames));

        for (String s : list) {
            DerbyConfig config = new DerbyConfig();
            Database.remove(config.name(s));
            log.info("remove {} db", s);
        }
        log.info("After Database.getConfigurations={}", Database.getConfigurations().getDerbyConfigs());
    }

    private void removeAllDb() {
        log.info("Before Database.getConfigurations={}", Database.getConfigurations().getDerbyConfigs());
        String[] dbNames = DataDir.get().getDatabasesDir().list();
        if (dbNames != null) {
            for (String dbName : dbNames) {
                DataDir.get().getDatabaseDir(dbName).delete();
                Database.remove(new DerbyConfig().name(dbName));
            }
            log.info("remove all db & processes");
        }
        log.info("After Database.getConfigurations={}", Database.getConfigurations().getDerbyConfigs());
    }

    private void saveOutputPrint() {
//        String prePath = "D:/Dropbox/2022-KETI/01-Project/01-EXE/산업부-KEIT-리사이클링/02-수행/06-2024/2024-01-SW개발/save_to_txt/";
        String home = System.getProperty("user.home");
        String prePath = home + "/server/txt";
        File file = new File(prePath);
        if (file.mkdirs())
            log.info("create /server/txt/ directory.");

        // PrintWriter(): 파일  쓰기 모드로 open, 파일 존재 시 지우고 새로 write
        try (PrintWriter writer = new PrintWriter(prePath+"/importedDbInfo.txt")) {
            var categoryAll = new ImpactCategoryDao(db).getAll();
            var methods = new ImpactMethodDao(db).getAll();
            var flows = new FlowDao(db).getAll();
            var processes = new ProcessDao(db).getAll();

            writer.println("db.getName(): " + db.getName());
            writer.println("db.getFileStorageLocation(): " + db.getFileStorageLocation());

            writer.println("\n\ncategoryAll: ");
            for (ImpactCategory c : categoryAll) {
                writer.println(c + " " + c.referenceUnit);
            }

            writer.println("\n\nmethods: ");
            for (ImpactMethod m : methods) {
                writer.println(m);
/*                if (m.name.equals("CML-IA baseline")){
                    for (ImpactCategory cg : m.impactCategories) {
                        if (cg.name.contains("GWP")) {
                            log.info("GWP find, {}", cg);
                            for (ImpactFactor f : cg.impactFactors) {
                                log.info("Factor test : GWP - {} {}", f.flow.name, f.value);
                            }
                        }
                    }
                }*/
            }

            writer.println("\n\nsaved processes of korea db: ");
            for (Process p : processes) {
                writer.println(p);
                writer.println(p.quantitativeReference+"\n");
            }

            writer.println("\n\nnumber of categoryDb: " + categoryAll.size());
            writer.println("number of methods: " + methods.size());
            writer.println("number of processes: " + processes.size());
            writer.println("number of flows: " + flows.size());

            for (ImpactMethod m : methods) {
                if (m.name.equals("\n\nCML-IA baseline")) {
                    writer.println(m.name + " => " + m.category);

                    for (ImpactCategory cg : m.impactCategories) {
                        writer.println("cg = " + cg);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: Unable to save koreaDb information info to file.");
            e.printStackTrace();
        }
    }


}
