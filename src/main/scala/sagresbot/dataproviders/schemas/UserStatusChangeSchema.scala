package sagresbot.dataproviders.schemas

import sagresbot.dataproviders.DBConfiguration.ctx._
import sagresbot.dataproviders.daos.UserStatusChangeDAO

private[dataproviders] object UserStatusChangeSchema {
  val usersStatusChanges = quote {
    querySchema[UserStatusChangeDAO]("users_status_changes")
  }
}
