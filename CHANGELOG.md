# Longevity Change Log

## [0.7-SNAPSHOT] - Entity Polymorphism



## [0.6.0] - 2016.03.20 - API Improvements

- 2016.03.02 - add implicit execution context parameter to: all `Repo`
  methods; `RepoPool.createMany`; and `LongevityContext.repoCrudSpec`
  and `inMemTestRepoCrudSpec`. users now need to provide execution
  contexts to use all these methods. the easiest way to do this is to
  include `import scala.concurrent.ExecutionContext.Implicits.global`
  at the top of the file.
- 2016.03.08 - update to latest version of library dependencies
  casbah (3.1.1) and cassandra (3.0.0).
- 2016.03.08 - add sub-projects `longevity-cassandra-deps` and
  `longevity-mongo-deps`.
- 2016.03.10 - replace `Root` with `Persistent`. give `Persistent`
  three child traits: `Root`, `ViewItem`, and `Event`. these changes
  should not affect existing code that uses `Root`.
- 2016.03.10 - replace `RootType` with `PType`. give `PType` three
  child traits: `RootType`, `View`, and `EventType`. these changes
  should not affect existing code that uses `RootType`.
- 2016.03.25 - rework `PType` API for `keySet` and `indexSet`. please
  see the latest documentation for a review of the new API.

## [0.5.0] - 2016.03.02 - Cassandra Back End

- 2016.01.12 - rename `RootType.keys` to `RootType.keySet`.
- 2016.01.12 - rename `RootType.indexes` to `RootType.indexSet`.
- 2016.03.01 - add Cassandra back end.
- 2016.03.01 - deprecate all methods that allow for use of a string
  property path in place of a `Prop`. affected methods are
  `RootEntity.{ key, index }`, `Query.{ eqs, neq, lt, lte, gt, gte }`,
  and the corresponging methods in the `QueryDsl`.
- 2016.03.01 - rework `QuerySpec`.
- 2016.03.02 - provide a parent trait `PRef` for `Assoc` and
  `KeyVal`. merge the two versions of the `Repo` methods `retrieve`
  and `retrieveOne` so that they take a `PRef`. this shouldn't affect
  any client code.

## [0.4.1] - 2016.01.12 - Remove Printlns

## [0.4.0] - 2016.01.12 - Initial Public Release