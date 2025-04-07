import com.example.demo.dataModels.BaseExercise;

import com.example.demo.serverSide.ServerSideAdapter;

import com.example.demo.usecases.createExercise.CreateExerciseUsecase;
import com.example.demo.usecases.IExerciseServerSide;

//import tools.IDatabaseConnector;
//import tools.PostgresqlConnector;
import com.example.demo.tools.DataSourceConfig;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.SQLException;

public class Main {

//    private static Logger logger = LogManager.getLogger(Main.class);
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        IExerciseServerSide serverSideInterface = null;

        try {
//            PostgresqlConnector potsgresqlConnector = new PostgresqlConnector();
//            Connection databaseConnector = potsgresqlConnector.getConnection();
            DataSourceConfig databaseSource = new DataSourceConfig();
            DataSource databaseConnector = databaseSource.dataSource();
            serverSideInterface = new ServerSideAdapter(databaseConnector);
        } catch (Exception databaseException) {
            logger.error(databaseException.getMessage());
            return;
        }

        BaseExercise exo = new BaseExercise("toto", "muscu", 666, 777, 888, 999, 111);
        CreateExerciseUsecase exerciseCreation = new CreateExerciseUsecase(serverSideInterface);
        try {
            exerciseCreation.execute(exo);
        } catch (IllegalArgumentException exception) {
            logger.error(exception.getMessage());
        } catch (Exception exception) {  // TODO : à regarder
            logger.error(exception.getMessage());
        }
    }
}
