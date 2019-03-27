package dinerapp.backup;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class DatabaseBackupController {

	private int counter = 0;

	@Scheduled(fixedRate = 86400000)
	public void backupDB() {

		String path = "C:\\Users\\a740114\\Desktop\\Backup\\";

		Process p = null;

		try {
			Runtime runtime = Runtime.getRuntime();
			p = runtime.exec("mysqldump -uroot -p14481448  --add-drop-database -B dinerapp -r " + path + "\\BackUp"
					+ counter++ + "min.sql");
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}
}
