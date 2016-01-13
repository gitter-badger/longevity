package longevity.exceptions.subdomain

import longevity.exceptions.LongevityException

/** an exception involving subdomain creation or use */
class SubdomainException(message: String, cause: Exception) extends LongevityException(message, cause) {

  def this(message: String) { this(message, null) }

}