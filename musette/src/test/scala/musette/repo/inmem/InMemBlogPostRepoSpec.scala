package musette.repo.inmem

import org.scalatest._
import org.scalatest.OptionValues._
import longevity.testUtil.RepoSpec
import musette.domain.testUtil._
import musette.domain.BlogPost

class InMemBlogPostRepoSpec extends RepoSpec[BlogPost] {

  private val repoLayer = new InMemRepoLayer
  def ename = "blog post"
  def repo = repoLayer.blogPostRepo
  def domainConfig = musette.domain.domainConfig
  def persistedShouldMatchUnpersisted = entityMatchers.persistedBlogPostShouldMatchUnpersisted _

}


