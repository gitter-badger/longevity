package longevity.integration.subdomain.foreignKey

import longevity.subdomain.PType

case class WithForeignKey(
  id: WithForeignKeyId,
  associated: AssociatedId)

object WithForeignKey extends PType[WithForeignKey] {
  object props {
    val id = prop[WithForeignKeyId]("id")
    val associated = prop[AssociatedId]("associated")
  }
  object keys {
    val id = key(props.id)
  }
  object indexes {
    val associated = index(props.associated)
  }
}
