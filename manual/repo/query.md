---
title: retrieval by query
layout: page
---

You can use queries to retrieve zero or more entities of a given
type. For instance, looking up all the blog posts for a blog published
in the last week:

```scala
import com.github.nscala_time.time.Imports._
import longevity.persistence.PState
import longevity.subdomain.ptype.Query
import scala.concurrent.Future

def getBlogState(): PState[Blog] = ???
val blogState: PState[Blog] = getBlogState()

val queryResult: Future[Seq[PState[BlogPost]]] = blogPostRepo.retrieveByQuery(
  Query.and(
    Query.eqs(BlogPost.props.blog, blogState.assoc),
    Query.gt(BlogPost.props.postDate, DateTime.now - 1.week)))
```

<div class="blue-side-bar">

Of course, we would <a href =
"https://www.pivotaltracker.com/story/show/109042398">prefer to
return a stream here</a>, rather than a sequence wrapped in a future.

</div>

Anything you can do with the `Query` factory methods, as shown above,
you can do using the Query DSL instead:

```scala
import com.github.nscala_time.time.Imports._
import longevity.persistence.PState
import scala.concurrent.Future

def getBlogState(): PState[Blog] = ???
val blogState: PState[Blog] = getBlogState()

import BlogPost.queryDsl._
val recentPosts: Future[Seq[PState[BlogPost]]] = blogPostRepo.retrieveByQuery(
  BlogPost.props.blog eqs blogState.assoc and
  BlogPost.props.postDate gt DateTime.now - 1.week)
```

If you don't want the DSL wildcard imports to infect other parts of
your program, it is quite easy to localize them:

```scala
import com.github.nscala_time.time.Imports._
import longevity.persistence.PState
import scala.concurrent.Future

def getBlogState(): PState[Blog] = ???
val blogState: PState[Blog] = getBlogState()

val recentPosts: Future[Seq[PState[BlogPost]]] = blogPostRepo.retrieveByQuery {
  import BlogPost.queryDsl._
  import BlogPost.props._
  blog eqs blogState.assoc and postDate gt DateTime.now - 1.week
}
```

The query syntax is currently quite limited, and is a [focal point of
future
work](https://www.pivotaltracker.com/epic/show/2253386). Currently,
the following query keywords are supported:

  - `and`, `or`, `eqs`, `neq`, `lt`, `lte`, `gt`, `gte`

The six comparator operators all take a
[property](../ptype/properties.html) on the left-hand side, and a
value on the right-hand side.

[Keys](../ptype/keys.html) and
[indexes](../ptype/indexes.html) will aid query performance in an
intuitive manner. For finer details on just how your query will run,
please see the chapters on how your subdomain is translated to your
NoSQL backend ([Mongo](../mongo) and [Cassandra](../cassandra)).

{% assign prevTitle = "retrieval by key value" %}
{% assign prevLink = "retrieve-keyval.html" %}
{% assign upTitle = "repositories" %}
{% assign upLink = "." %}
{% assign nextTitle = "cassandra query limitations" %}
{% assign nextLink = "cassandra-query-limits.html" %}
{% include navigate.html %}