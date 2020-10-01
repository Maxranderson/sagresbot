package sagresbot.dataproviders.schemas

import io.getquill.EntityQuery
import sagresbot.dataproviders.daos.UserDAO
import sagresbot.dataproviders.DBConfiguration.ctx._

private[dataproviders] object UserSchema {
  val users: Quoted[EntityQuery[UserDAO]] = quote {
    querySchema[UserDAO]("users")
  }
}
