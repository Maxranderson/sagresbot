package sagresbot.dataproviders.schemas

import io.getquill.EntityQuery
import sagresbot.core.entity.UserStatus
import sagresbot.dataproviders.DBConfiguration.ctx._

private[dataproviders] object UserStatusSchema {

  val usersStatus: Quoted[EntityQuery[UserStatus]] = quote {
    querySchema[UserStatus]("user_status")
  }

}
