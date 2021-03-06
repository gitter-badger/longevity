package longevity.subdomain.ptype


/** a series of properties that determines the partitioning used by the
 * underlying database to distribute data across multiple nodes. used to form a
 * [[PartitionKey]]
 *
 * @param props the properties that determine the partition
 */
case class Partition[P] private[subdomain] (props: Seq[Prop[P, _]])
