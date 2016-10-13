---
title: limitations on cassandra queries
layout: page
---

Due to the limitations on querying in Cassandra, there are a number of
queries that can be built that will not work on Cassandra. Here's
the rundown.

While a Mongo query on a column that is not mentioned in a
[key](../ptype/keys.html) or an [index](../ptype/indexes.html)
may not perform well, it will still run. A Cassandra query on any
column that is not mentioned in a key or index will fail to run.

Queries using the _not-equals_ operator are not supported by Cassandra.

Queries using the _or_ operator are not supported by Cassandra.

Queries must contain at least one _equals_ clause.

Ordering query operators - _less-than_, _less-than-or-equals_,
_greater-than_, and _greater-than-or-equals_ - can only be used with
properties that are composed of a single basic type. The reason for
this is that to construct an ordering query for a compound property
requires the use of the _not-equals_ operator, which is not supported
by Cassandra CQL.

The `filterAll` query filter is not supported by Cassandra.

Cassandra queries do not support offset clauses.

Currently, Cassandra queries do not support order-by clauses. This
will change when we implement [partition
keys](../translation/keys.html).

{% assign prevTitle = "stream by query" %}
{% assign prevLink = "stream-by.html" %}
{% assign upTitle = "queries" %}
{% assign upLink = "." %}
{% assign nextTitle = "testing your subdomain" %}
{% assign nextLink = "../testing" %}
{% include navigate.html %}