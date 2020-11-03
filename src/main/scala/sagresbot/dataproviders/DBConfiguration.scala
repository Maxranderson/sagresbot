package sagresbot.dataproviders

import com.typesafe.config.ConfigFactory
import io.getquill.{JdbcContextConfig, PostgresJdbcContext, SnakeCase}
import org.flywaydb.core.Flyway

object DBConfiguration {

  private val config = JdbcContextConfig(ConfigFactory.systemEnvironment().getConfig("ctx"))
  private val flyway = Flyway.configure().dataSource(config.dataSource).load()

  def migrate() = flyway.migrate()
  lazy val ctx = new PostgresJdbcContext(SnakeCase, "ctx")

}
