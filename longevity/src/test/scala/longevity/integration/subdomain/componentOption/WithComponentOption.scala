package longevity.integration.subdomain.componentOption

import longevity.subdomain.annotations.persistent

@persistent(keySet = Set(key(WithComponentOption.props.id)))
case class WithComponentOption(
  id: WithComponentOptionId,
  component: Option[Component])
