---
title: limitations on persistents, entities and shorthands
layout: page
---

Longevity currently places the following limitations on the kinds of
classes you can use for your [persistents](persistent), [entites](entities), and
[shorthands](shorthands):

- They must be a case class.
- They must not be an [inner class](http://docs.scala-lang.org/tutorials/tour/inner-classes.html).
- They must have a primary constructor with a single parameter list.
- The primary constructor for a shorthand must have a single parameter
  of a [basic type](basics.html).

We would like to relax these limitations in the future. If you find
these limitations to be too cumbersome for you, please [let us
know](http://longevityframework.github.io/longevity/discussions.html)
what you are trying to do, and we will see what we can to do help.
Just keep in mind that, whatever possibilities we allow for, longevity
has the following requirements:

- The set of properties that an entity contains must be clearly defined.
- We must be able to retrieve a property value from a persistent or entity instance.
- We must be able to construct a new persistent or entity instance from a complete set of property values.
- We must be able to retrieve an abbreviated value from a shorthand instance.
- We must be able to construct a new shorthand instance from an abbreviated value.

Case classes are quite convenient things for fulfilling the
requirements we have. They also seem a natural choice for modeling out
a domain. We chose to start with them for these reasons.

{% assign prevTitle = "entities and value objects" %}
{% assign prevLink = "entities/value-objects.html" %}
{% assign upTitle = "user manual" %}
{% assign upLink = "." %}
{% assign nextTitle = "assocations" %}
{% assign nextLink = "associations" %}
{% include navigate.html %}