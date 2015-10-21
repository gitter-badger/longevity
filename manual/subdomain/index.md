---
title: building your subdomain
layout: page
---

Your main task when working with longevity is building your
`Subdomain`. Once this is done, we provide you with persistence. Your
subdomain classes are the core of you application(s). The other
layers perform their individual functions while using the domain
classes fluidly.

{% capture content %}

As we mentioned earlier, we hope to cover more than just persistence
in the future. But we want to get the persistence part right, first.

{% endcapture %}
{% include longevity-meta.html content=content%}

Domain Driven Design describes a few [different kinds of
subdomains](http://blog.jonathanoliver.com/ddd-strategic-design-core-supporting-and-generic-subdomains/),
such as core, supporting, and generic. In longevity, these are all
the same thing, but you can name it as you please. You can also make
use of multiple longevity `Subdomains` in a single application.

Longevity `Subdomains` are quite easy to construct, and in this
section, we will review all the tools at your disposal to build
them. To get started, here are a few ways to build an empty
`Subdomain`:

```scala
import longevity.subdomain._
val subdomain = Subdomain("blogging", EntityTypePool.empty)

// you can also use these synonyms freely:
val coreDomain: CoreDomain = CoreDomain("blogging", EntityTypePool.empty)
val supportingSubdomain: SupportingSubdomain = SupportingSubdomain("accounts", EntityTypePool.empty)
val genericSubdomain: GenericSubdomain = GenericSubdomain("search", EntityTypePool.empty)
```

egg

{% assign prevTitle = "project setup" %}
{% assign prevLink = "../project-setup.html" %}
{% assign upTitle = "user manual" %}
{% assign upLink = ".." %}
{% assign nextTitle="building your subdomain" %}
{% assign nextLink="subdomain/" %}
{% include navigate.html %}
