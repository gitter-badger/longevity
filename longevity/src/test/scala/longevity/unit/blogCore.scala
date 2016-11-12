package longevity.unit

package object blogCore {

  import longevity.subdomain.EType
  import longevity.subdomain.ETypePool
  import longevity.subdomain.EType
  import longevity.subdomain.KeyVal
  import longevity.subdomain.Subdomain
  import longevity.subdomain.PTypePool
  import longevity.subdomain.PType

  case class Email(email: String)
  extends KeyVal[User, Email]

  case class Username(username: String)
  extends KeyVal[User, Username]

  case class Markdown(markdown: String)

  case class Uri(uri: String)

  implicit def toEmail(email: String) = Email(email)
  implicit def toUsername(username: String) = Username(username)
  implicit def toMarkdown(markdown: String) = Markdown(markdown)
  implicit def toUri(uri: String) = Uri(uri)

  case class User(
    username: Username,
    email: Email,
    fullname: String,
    profile: Option[UserProfile] = None)
 

  object User extends PType[User] {
    object props {
      val username = prop[Username]("username")
      val email = prop[Email]("email")
    }
    object keys {
      val username = key(props.username)
      val email = key(props.email)
    }
  }

  case class UserProfile(
    tagline: String,
    imageUri: Uri,
    description: Markdown)
 

  case class BlogUri(uri: Uri)
  extends KeyVal[Blog, BlogUri]

  case class Blog(
    uri: BlogUri,
    title: String,
    description: Markdown,
    authors: Set[Username])
 

  object Blog extends PType[Blog] {
    object props {
      val uri = prop[BlogUri]("uri")
    }
    object keys {
      val uri = key(props.uri)
    }
  }

  case class BlogPostUri(uri: Uri)
  extends KeyVal[BlogPost, BlogPostUri]

  case class BlogPost(
    uri: BlogPostUri,
    title: String,
    slug: Option[Markdown] = None,
    content: Markdown,
    labels: Set[String] = Set(),
    blog: BlogUri,
    authors: Set[Username])
 

  object BlogPost extends PType[BlogPost] {
    object props {
      val uri = prop[BlogPostUri]("uri")
      val blog = prop[BlogUri]("blog")
    }
    object keys {
      val uri = key(props.uri)
    }
    object indexes {
      val blog = props.blog
    }
  }

  object BlogCore extends Subdomain(
    "blogging",
    PTypePool(User, Blog, BlogPost),
    ETypePool(EType[Markdown], EType[Uri], EType[UserProfile]))

}
