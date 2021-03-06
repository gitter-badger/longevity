package longevity.integration.subdomain.keyWithMultipleProperties

import longevity.subdomain.annotations.persistent

@persistent(keySet = Set(key(props.id), key(props.secondaryKey)))
case class KeyWithMultipleProperties(
  id: KeyWithMultiplePropertiesId,
  secondaryKey: SecondaryKey)
