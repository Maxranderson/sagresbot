package sagresbot.dataproviders

import com.typesafe.config.ConfigFactory
import io.getquill.{JdbcContextConfig, SnakeCase, SqliteJdbcContext}
import org.flywaydb.core.Flyway

object DBConfiguration {

  private val config = JdbcContextConfig(ConfigFactory.systemEnvironment().getConfig("ctx"))
  private val flyway = Flyway.configure().dataSource(config.dataSource).load()

  def migrate() = flyway.migrate()
  val ctx = new SqliteJdbcContext(SnakeCase, config)

}
