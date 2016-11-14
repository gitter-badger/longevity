---
title: controlled vocabularies
layout: page
---

There are two major approaches to doing controlled vocabularies in
Scala: using
[`scala.Enumeration`](http://www.scala-lang.org/api/current/index.html#scala.Enumeration),
or using a sealed trait with case objects. Here's a controlled
vocabulary for account status that uses the latter approach:

```scala
sealed trait AccountStatus
case object Active extends AccountStatus
case object Suspended extends AccountStatus
case object Cancelled extends AccountStatus
```

Because longevity supports case objects and polymorphism, this pattern
directly translates into a longevity-enabled subdomain. We simply have
to mark the top of the hierarchy as `Embeddable`:

```scala
import longevity.subdomain.Embeddable

sealed trait AccountStatus extends Embeddable
case object Active extends AccountStatus
case object Suspended extends AccountStatus
case object Cancelled extends AccountStatus
```

We are now free to use this controlled vocabulary in our domain, such
as:

```scala
import longevity.subdomain.Persistent
import longevity.subdomain.PType

case class Account(
  name: String,
  accountStatus: AccountStatus)
extends Persistent

object Account extends PType[Account] {
  object keys {
  }
  object indexes {
  }
}
```

We just need to add the members of our controlled vocabulary to the
`ETypePool`:

```scala
import longevity.subdomain.DerivedEType
import longevity.subdomain.ETypePool
import longevity.subdomain.PTypePool
import longevity.subdomain.PolyEType
import longevity.subdomain.Subdomain

val subdomain = Subdomain(
  "accounts",
  PTypePool(Account),
  ETypePool(
    PolyEType[AccountStatus],
    DerivedEType[Active.type, AccountStatus],
    DerivedEType[Suspended.type, AccountStatus],
    DerivedEType[Cancelled.type, AccountStatus]))
```

Having to list all the members of the controlled vocabulary here is
less than ideal, and something we want to address. It is presently
covered by the same [user
story](https://www.pivotaltracker.com/story/show/127406543) on our
story board.

Support for `scala.Enumeration` would be a great addition, and is [on
our story board as
well](https://www.pivotaltracker.com/story/show/128589983). If you are
interested, this would be a great user contribution, and we would be
happy to support you in implementing it however we can.

{% assign prevTitle = "polymorphic persistents" %}
{% assign prevLink  = "persistents.html" %}
{% assign upTitle   = "subtype polymorphism" %}
{% assign upLink    = "." %}
{% assign nextTitle = "repositories" %}
{% assign nextLink  = "../repo" %}
{% include navigate.html %}

