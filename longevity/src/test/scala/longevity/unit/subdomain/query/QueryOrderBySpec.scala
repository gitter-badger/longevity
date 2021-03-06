package longevity.unit.subdomain.query

import longevity.integration.subdomain.basics.Basics
import longevity.integration.subdomain.basics.BasicsId
import longevity.integration.subdomain.basics.subdomain
import longevity.subdomain.query.Ascending
import longevity.subdomain.query.Descending
import longevity.subdomain.query.QueryOrderBy
import longevity.subdomain.query.QuerySortExpr
import org.joda.time.DateTime
import org.scalatest.FlatSpec
import org.scalatest.Matchers

/** unit tests for [[QueryOrderBy.ordering]] */
class QueryOrderBySpec extends FlatSpec with Matchers {

  private val realizedPType = subdomain.realizedPTypes(Basics)

  val degenerate = Basics(BasicsId("id"), false, '0', 0D, 0F, 0, 0L, "", DateTime.now)
  def withInt(i: Int) = degenerate.copy(int = i)
  def withIntAndDouble(i: Int, d: Double) = degenerate.copy(int = i, double = d)

  behavior of "QueryOrderBy.ordering"

  it should "return an ordering that always produces 0 when there are no sort exprs" in {
    val orderBy = QueryOrderBy[Basics](Seq())
    val ordering = QueryOrderBy.ordering(orderBy, realizedPType)
    ordering.compare(withInt(0), withInt(0)) should equal (0)
    ordering.compare(withInt(0), withInt(1)) should equal (0)
    ordering.compare(withInt(1), withInt(0)) should equal (0)
  }

  it should "return a valid ordering when there is one sort expr" in {
    var orderBy = QueryOrderBy[Basics](Seq(
      QuerySortExpr(Basics.props.int, Ascending)))
    var ordering = QueryOrderBy.ordering(orderBy, realizedPType)
    ordering.compare(withInt(0), withInt(0)) should equal (0)
    ordering.compare(withInt(0), withInt(1)) should be < 0
    ordering.compare(withInt(1), withInt(0)) should be > 0

    orderBy = QueryOrderBy[Basics](Seq(
      QuerySortExpr(Basics.props.int, Descending)))
    ordering = QueryOrderBy.ordering(orderBy, realizedPType)
    ordering.compare(withInt(0), withInt(0)) should equal (0)
    ordering.compare(withInt(0), withInt(1)) should be > 0
    ordering.compare(withInt(1), withInt(0)) should be < 0
  }

  it should "return a valid ordering when there are two sort exprs" in {
    var orderBy = QueryOrderBy[Basics](Seq(
      QuerySortExpr(Basics.props.int, Ascending),
      QuerySortExpr(Basics.props.double, Ascending)))
    var ordering = QueryOrderBy.ordering(orderBy, realizedPType)
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble( 0,  0D)) should equal (0)
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble(-1,  0D)) should be > 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble( 1,  0D)) should be < 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble( 0, -1D)) should be > 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble(-1, -1D)) should be > 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble( 1, -1D)) should be < 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble( 0,  1D)) should be < 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble(-1,  1D)) should be > 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble( 1,  1D)) should be < 0

    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble( 0,  0D)) should equal (0)
    ordering.compare(withIntAndDouble(-1,  0D), withIntAndDouble( 0,  0D)) should be < 0
    ordering.compare(withIntAndDouble( 1,  0D), withIntAndDouble( 0,  0D)) should be > 0
    ordering.compare(withIntAndDouble( 0, -1D), withIntAndDouble( 0,  0D)) should be < 0
    ordering.compare(withIntAndDouble(-1, -1D), withIntAndDouble( 0,  0D)) should be < 0
    ordering.compare(withIntAndDouble( 1, -1D), withIntAndDouble( 0,  0D)) should be > 0
    ordering.compare(withIntAndDouble( 0,  1D), withIntAndDouble( 0,  0D)) should be > 0
    ordering.compare(withIntAndDouble(-1,  1D), withIntAndDouble( 0,  0D)) should be < 0
    ordering.compare(withIntAndDouble( 1,  1D), withIntAndDouble( 0,  0D)) should be > 0

    orderBy = QueryOrderBy[Basics](Seq(
      QuerySortExpr(Basics.props.int, Ascending),
      QuerySortExpr(Basics.props.double, Descending)))
    ordering = QueryOrderBy.ordering(orderBy, realizedPType)
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble( 0,  0D)) should equal (0)
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble(-1,  0D)) should be > 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble( 1,  0D)) should be < 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble( 0, -1D)) should be < 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble(-1, -1D)) should be > 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble( 1, -1D)) should be < 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble( 0,  1D)) should be > 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble(-1,  1D)) should be > 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble( 1,  1D)) should be < 0

    orderBy = QueryOrderBy[Basics](Seq(
      QuerySortExpr(Basics.props.int, Descending),
      QuerySortExpr(Basics.props.double, Ascending)))
    ordering = QueryOrderBy.ordering(orderBy, realizedPType)
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble( 0,  0D)) should equal (0)
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble(-1,  0D)) should be < 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble( 1,  0D)) should be > 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble( 0, -1D)) should be > 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble(-1, -1D)) should be < 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble( 1, -1D)) should be > 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble( 0,  1D)) should be < 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble(-1,  1D)) should be < 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble( 1,  1D)) should be > 0

    orderBy = QueryOrderBy[Basics](Seq(
      QuerySortExpr(Basics.props.int, Descending),
      QuerySortExpr(Basics.props.double, Descending)))
    ordering = QueryOrderBy.ordering(orderBy, realizedPType)
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble( 0,  0D)) should equal (0)
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble(-1,  0D)) should be < 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble( 1,  0D)) should be > 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble( 0, -1D)) should be < 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble(-1, -1D)) should be < 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble( 1, -1D)) should be > 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble( 0,  1D)) should be > 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble(-1,  1D)) should be < 0
    ordering.compare(withIntAndDouble( 0,  0D), withIntAndDouble( 1,  1D)) should be > 0

  }

}
