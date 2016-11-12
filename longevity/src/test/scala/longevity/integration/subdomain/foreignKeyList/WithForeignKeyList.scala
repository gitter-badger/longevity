package longevity.integration.subdomain.foreignKeyList

import longevity.subdomain.PType

case class WithForeignKeyList(
  id: WithForeignKeyListId,
  associated: List[AssociatedId])

object WithForeignKeyList extends PType[WithForeignKeyList] { 
  object props {
    val id = prop[WithForeignKeyListId]("id")
  }
  object keys {
    val id = key(props.id)
  }
}
