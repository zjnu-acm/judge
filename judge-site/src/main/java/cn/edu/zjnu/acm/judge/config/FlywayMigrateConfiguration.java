package cn.edu.zjnu.acm.judge.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class FlywayMigrateConfiguration {

    @Bean
    public FlywayMigrationStrategy flywayChecksumFixture(DataSource dataSource) {
        return flyway -> {
            try (Connection connection = dataSource.getConnection()) {
                if (changeChecksum(connection, "20170830", -67687205, 1960302326)) {
                    final String sql = "ALTER TABLE problem_i18n DROP FOREIGN KEY FK_problem_i18n_locale";
                    final String sql2 = "ALTER TABLE locale DROP INDEX UQ_ID, ADD UNIQUE INDEX UQ_ID (id)";
                    final String sql3 = "ALTER TABLE problem_i18n ADD CONSTRAINT FK_problem_i18n_locale FOREIGN KEY (locale) REFERENCES locale (id)";
                    if (changeChecksum(connection, "20190430.1", 1414750918, 1414750918)) {
                        execute(connection, sql);
                        execute(connection, sql2);
                        execute(connection, sql3);
                    } else {
                        execute(connection, sql2);
                    }
                }
                changeChecksum(connection, "00001", -101734729, 592369862);
                changeChecksum(connection, "20170415", -2048294486, -589197382);
                changeChecksum(connection, "20170830", 1960302326, -617876408);
                changeChecksum(connection, "20170904", -582277005, 1502423353);
                changeChecksum(connection, "20180102", -609124434, 965422677);
                changeChecksum(connection, "20190511.1", -1132220883, 2055755532);
                changeChecksum(connection, "20190511.1", 2055755532, 2140581436);
            } catch (SQLException ex) {
                // usually there is no table flyway_schema_history
                log.error("{}", ex.getMessage());
            }
            flyway.migrate();
        };
    }

    private void execute(Connection connection, String sql) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.executeUpdate();
        }
        log.info("succeed execute '{}'", sql);
    }

    // @throws SQLException if there is no table flyway_schema_history
    private boolean changeChecksum(Connection connection, String version, int old, int checksum)
            throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE flyway_schema_history SET checksum=? WHERE version=? and checksum=?")) {
            ps.setInt(1, checksum);
            ps.setString(2, version);
            ps.setInt(3, old);
            boolean result = ps.executeUpdate() > 0;
            if (result) {
                log.info("succeed to change checksum of {} from {} to {}", version, old, checksum);
            } else {
                log.debug("failed to change checksum of {} from {} to {}", version, old, checksum);
            }
            return result;
        }
    }

}
